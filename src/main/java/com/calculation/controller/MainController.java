package com.calculation.controller;

import com.calculation.entity.Account;
import com.calculation.entity.calculation.Calculation;
import com.calculation.entity.calculation.Estimated;
import com.calculation.entity.calculation.Orders;
import com.calculation.entity.calculation.Product;
import com.calculation.repository.AccountRepo;
import com.calculation.repository.calculation.CalculationRepo;
import com.calculation.repository.calculation.EstimatedRepo;
import com.calculation.repository.calculation.OrdersRepo;
import com.calculation.repository.calculation.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
public class MainController {

    private final CalculationRepo calculationRepo;
    private final EstimatedRepo estimatedRepo;
    private final OrdersRepo ordersRepo;
    private final ProductRepo productRepo;
    private final AccountRepo accountRepo;

    @Autowired
    public MainController(CalculationRepo calculationRepo, EstimatedRepo estimatedRepo, OrdersRepo ordersRepo, ProductRepo productRepo, AccountRepo accountRepo) {
        this.calculationRepo = calculationRepo;
        this.estimatedRepo = estimatedRepo;
        this.ordersRepo = ordersRepo;
        this.productRepo = productRepo;
        this.accountRepo = accountRepo;
    }

    public static final int sizePage = 10;
    @Value("${uploadDir}")
    private String uploadFolder;

    @GetMapping("/estimate")
    public String showEstimate(
            @AuthenticationPrincipal Account account,
            @RequestParam(name = "page", defaultValue = "0") int numpage,
            @RequestParam(name = "search", defaultValue = "") String search,
            Estimated estimated,
            Model model) {

        Pageable pageable = PageRequest.of(numpage, sizePage, Sort.by("id"));
        Page<Estimated> page;
        if (search != null && !search.equals("")) {
            model.addAttribute("search", search);
            model.addAttribute("url", "/estimate?search=" + search);
            page = estimatedRepo.findAllByContractContainingOrBuildingContainingAndAccountId(search, search, pageable, account.getId());
        } else {
            model.addAttribute("url", "/estimate?");
            page = estimatedRepo.findAllByAccountId(pageable, account.getId());
        }

        model.addAttribute("title", "Estimate");
        model.addAttribute("account", account);
        model.addAttribute("estimated", estimated);
        model.addAttribute("numbers", new int[page.getTotalPages()]);
        model.addAttribute("page", page);
        model.addAttribute("size", sizePage);
        model.addAttribute("sort", "id");

        return "estimate";
    }

    @PostMapping("/add-estimated")
    public String addEstimated(@AuthenticationPrincipal Account account, Estimated estimated) {
        if (estimated.getDate() == null) {
            estimated.setDate(LocalDate.now());
        }
        estimated.setAccountId(account.getId());
        calculationPrice(estimated);
        return "redirect:/calculation/" + estimated.getId();
    }

    @GetMapping("/delete-estimated/{id}")
    public String deleteEstimated(@PathVariable Long id, @AuthenticationPrincipal Account account) {
        Long accountId = estimatedRepo.getOne(id).getAccountId();
        if (account.getId() == accountId) {
            estimatedRepo.deleteById(id);
        }
        return "redirect:/estimate";
    }

    @GetMapping("/calculation/{estId}")
    public String showCalculation(
            @AuthenticationPrincipal Account account,
            @PathVariable Long estId,
            Calculation calculation,
            Orders orders,
            Model model) {
        Estimated estimated = estimatedRepo.getOne(estId);
        if (estimated == null || estimated.getAccountId() != account.getId()) {
            return "page-not-found";
        }
        model.addAttribute("estimated", estimated);
        List<Calculation> calculations = calculationRepo.findAllByEstimateId(estId);
        model.addAttribute("calculations", calculations);
        model.addAttribute("calculation", calculation);
        model.addAttribute("orders", orders);
        List<Product> products = productRepo.findAllByAccountId(account.getId());
        model.addAttribute("products", products);
        model.addAttribute("cId", calculation.getId());
        model.addAttribute("title", "Calculation");

        return "calculation";
    }

    @GetMapping("/delete-calculation/{id}")
    public String deleteCalculation(@PathVariable Long id, @AuthenticationPrincipal Account account) {
        long estimateId = calculationRepo.getOne(id).getEstimateId();

        Long accountId = calculationRepo.getOne(id).getEstimated().getAccountId();
        if (account.getId() != accountId) {
            return "page-not-found";
        }

        calculationRepo.deleteById(id);
        Estimated estimated = estimatedRepo.getOne(estimateId);
        double sumEst = estimated.getCalculations().stream()
                .filter(c -> c.getCalculationPrice() != null)
                .map(Calculation::getCalculationPrice)
                .mapToDouble(Double::intValue).sum();

        estimated.setEstimatePrice(sumEst);

        calculationPrice(estimated);

        return "redirect:/calculation/" + estimateId;
    }

    @PostMapping("/add-calculation")
    public String addCalculation(Calculation calculation) {
        long estimatedId = calculation.getEstimateId();
        calculationRepo.save(calculation);

        return "redirect:/calculation/" + estimatedId;
    }

    @GetMapping("/add-orders")
    public String addOrders(
            @RequestParam String name,
            @RequestParam String calculationId) {
        Long calcId = Long.valueOf(calculationId);
        long estimateId = calculationRepo.getOne(calcId).getEstimateId();
        if (name != null && !name.equals("")) {
            String[] value = name.split("!");
            for (String v : value) {
                String[] keyVal = v.split("-");
                Long productId = Long.valueOf(keyVal[0]);
                int count = Integer.parseInt(keyVal[1]);
                Product product = productRepo.getOne(productId);

                List<Orders> ordersList = calculationRepo.getOne(calcId).getOrders();
                for (Orders o : ordersList) {
                    if (o.getShortName().equals(product.getShortName())) {
                        o.setCount(o.getCount() + count);
                        o.setSummary(o.getCount() * product.getPrice());
                        ordersRepo.save(o);
                        calculationEstimate(calcId);
                        calculationPrice(estimatedRepo.getOne(estimateId));
                        return "redirect:/calculation/" + estimateId;
                    }
                }

                Orders orders = new Orders();
                orders.setCalculationId(calcId);
                orders.setShortName(product.getShortName());
                orders.setFullName(product.getFullName());
                orders.setMeasurement(product.getMeasurement());
                orders.setPrice(product.getPrice());
                orders.setCount(count);
                orders.setSummary(count * product.getPrice());

                ordersRepo.save(orders);
            }
        }

        calculationEstimate(calcId);
        calculationPrice(estimatedRepo.getOne(estimateId));
        return "redirect:/calculation/" + estimateId;
    }

    @GetMapping("/delete-orders/{id}")
    public String deleteOrders(@PathVariable Long id, @AuthenticationPrincipal Account account) {
        long calculationId = ordersRepo.getOne(id).getCalculationId();
        long estimateId = calculationRepo.getOne(calculationId).getEstimateId();

        Long accountId = ordersRepo.getOne(id).getCalculation().getEstimated().getAccountId();
        if (account.getId() == accountId) {
            ordersRepo.deleteById(id);
        }

        calculationEstimate(calculationId);
        calculationPrice(estimatedRepo.getOne(estimateId));

        return "redirect:/calculation/" + estimateId;
    }


    @GetMapping("/print/{estId}")
    public String showPrint(
            @PathVariable Long estId,
            @AuthenticationPrincipal Account account,
            Model model) {
        Estimated estimated = estimatedRepo.getOne(estId);

        Long accountId = estimated.getAccountId();
        if (account.getId() != accountId) {
            return "page-not-found";
        }
        model.addAttribute("estimated", estimated);
        model.addAttribute("account", account);
        model.addAttribute("title", "Сметный расчет №" + estId);
        return "print";
    }

    private void calculationEstimate(long calculationId) {
        int sumCalc = ordersRepo.findAllByCalculationId(calculationId).stream()
                .filter(o -> o.getPrice() != null)
                .map(Orders::getSummary)
                .mapToInt(Double::intValue).sum();

        Calculation calculation = calculationRepo.getOne(calculationId);
        calculation.setCalculationPrice((double) sumCalc);
        calculationRepo.save(calculation);

        Estimated estimated = calculation.getEstimated();
        double sumEst = estimated.getCalculations().stream()
                .filter(c -> c.getCalculationPrice() != null)
                .map(Calculation::getCalculationPrice)
                .mapToDouble(Double::intValue).sum();

        estimated.setEstimatePrice(sumEst);
        estimatedRepo.save(estimated);
    }

    private void calculationPrice(Estimated estimated) {

        if (estimated.getDiscount() != null && estimated.getEstimatePrice() != null) {
            if (estimated.getDiscount() > 0 && estimated.getDiscount() < 100) {
                double price = estimated.getEstimatePrice() - (estimated.getEstimatePrice() * estimated.getDiscount() / 100);
                double scale = Math.pow(10, 1);
                estimated.setPrice(Math.ceil(price * scale) / scale);
            }
        } else if (estimated.getPrice() != null && estimated.getEstimatePrice() != null) {
            if (estimated.getPrice() < estimated.getEstimatePrice() && estimated.getPrice() > 0) {
                double discount = (100 - ((estimated.getPrice() * 100) / estimated.getEstimatePrice()));
                double scale = Math.pow(10, 3);
                estimated.setDiscount(Math.ceil(discount * scale) / scale);
            } else {
                estimated.setPrice(estimated.getEstimatePrice());
            }
        }

        estimatedRepo.save(estimated);
    }

    @GetMapping("/products")
    public String showProducts(@AuthenticationPrincipal Account account, Product product, Model model) {
        List<Product> products = productRepo.findAllByAccountId(account.getId());
        model.addAttribute("products", products);
        model.addAttribute("product", product);
        model.addAttribute("title", "Products");
        return "products";
    }

    @PostMapping("/add-product")
    public String addProduct(@AuthenticationPrincipal Account account, Product product) {
        product.setAccountId(account.getId());
        productRepo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id, @AuthenticationPrincipal Account account) {
        Long accountId = productRepo.getOne(id).getAccountId();
        if (account.getId() == accountId) {
            productRepo.deleteById(id);
        }
        return "redirect:/products";
    }

    @GetMapping("/account")
    public String showAccount(
            @AuthenticationPrincipal Account account,
            @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam(defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ending,
            Account accountDAO,
            Model model) {
        model.addAttribute("account", account);
        model.addAttribute("accountDAO", accountDAO);
        model.addAttribute("title", "Account");
        if (begin == null) {
            begin = LocalDate.parse("2000-01-01");
        }
        if (ending == null) {
            ending = LocalDate.now();
        }
        model.addAttribute("begin", begin);
        model.addAttribute("ending", ending);
        List<Estimated> estimateds = estimatedRepo.findAllByAccountIdAndDateIsBetween(account.getId(), begin, ending);
        List<Double> graph = new ArrayList<>();
        double max = 0;
        double min = 0;
        double sum = 0;
        if (estimateds != null) {
            for (Estimated est : estimateds) {
                double price;
                if (est.getPrice() == null) {
                    if (est.getEstimatePrice() != null) {
                        price = est.getEstimatePrice();
                        graph.add(price);
                    }
                } else {
                    price = est.getPrice();
                    graph.add(price);
                }
            }
            if (graph != null && !graph.isEmpty()) {
                max = graph.stream().max(Double::compareTo).get();
                min = graph.stream().min(Double::compareTo).get();
                sum = graph.stream().mapToDouble(Double::doubleValue).sum();
                model.addAttribute("max", max);
                model.addAttribute("min", min);
                model.addAttribute("sum", sum);
                model.addAttribute("graph", graph);
            }
        }

        return "account";
    }

    @PostMapping("/edit-account")
    public String editAccount(@AuthenticationPrincipal Account account, Account accountDAO) {
        account.setCompany(accountDAO.getCompany());
        account.setDepartment(accountDAO.getDepartment());
        account.setAddress(accountDAO.getAddress());
        account.setSite(accountDAO.getSite());
        account.setPhone(accountDAO.getPhone());
        account.setPosition(accountDAO.getPosition());
        account.setName(accountDAO.getName());
        accountRepo.save(account);
        return "redirect:/account";
    }

    public boolean fileFormat(MultipartFile file) {
        String[] formatFile = new String[]{"JPG", "PNG", "GIF", "TIFF", "JPEG", "BMP"};
        String fileName = file.getOriginalFilename();
        boolean avaError = false;
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            String actualFormat = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
            if (!Arrays.asList(formatFile).contains(actualFormat)) {
                avaError = true;
            }
        } else avaError = true;
        return avaError;
    }

    @PostMapping("/add-logo")
    public String addLogo(
            @AuthenticationPrincipal Account account,
            HttpServletRequest request,
            Account accountDAO,
            Model model,
            MultipartFile file) {
        if (file.getSize() > 1048576 || fileFormat(file)) {
            model.addAttribute("mess", "Файл должен являтся изображением и не превышать - 1Mb");
            return showAccount(account, null, null, accountDAO, model);
        }

        String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
        String fileName = file.getOriginalFilename();
        String filePath = Paths.get(uploadDirectory, fileName).toString();
        if (fileName == null || fileName.contains("..")) {
            return "redirect:/account";
        }

        try {
            File dir = new File(uploadDirectory);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(filePath)));) {
                os.write(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] imageData = file.getBytes();
            account.setImage(imageData);
            accountRepo.save(account);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/account";
    }

    @GetMapping("/img/display/{id}")
    void showLogo(@PathVariable("id") Long id, HttpServletResponse response, Optional<Account> imgLogo) throws ServletException, IOException {
        imgLogo = accountRepo.findById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(imgLogo.get().getImage());
        response.getOutputStream().close();
    }

}
