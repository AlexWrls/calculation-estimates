package com.calculation.controller;

import com.calculation.entity.Account;
import com.calculation.entity.scheme.Building;
import com.calculation.entity.scheme.PartShied;
import com.calculation.entity.scheme.Shield;
import com.calculation.repository.shield.BuildingRepo;
import com.calculation.repository.shield.PartShieldRepo;
import com.calculation.repository.shield.ShieldRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
public class ShieldController {

    private final ShieldRepo shieldRepo;
    private final PartShieldRepo partShieldRepo;
    private final BuildingRepo buildingRepo;

    @Autowired
    public ShieldController(ShieldRepo shieldRepo, PartShieldRepo partShieldRepo, BuildingRepo buildingRepo) {
        this.shieldRepo = shieldRepo;
        this.partShieldRepo = partShieldRepo;
        this.buildingRepo = buildingRepo;
    }

    private static final int sizePage = 10;

    @GetMapping("/building")
    public String showBuilding(
            @AuthenticationPrincipal Account account,
            @RequestParam(name = "page", defaultValue = "0") int numpage,
            @RequestParam(name = "search", defaultValue = "") String search,
            Building building,
            Model model) {

        Pageable pageable = PageRequest.of(numpage, sizePage, Sort.by("id"));
        Page<Building> page;
        if (search != null && !search.equals("")) {
            model.addAttribute("search", search);
            model.addAttribute("url", "/building?search=" + search);
            page = buildingRepo.findAllByAccountIdAndNameStartingWith(account.getId(), search, pageable);
        } else {
            model.addAttribute("url", "/building?");
            page = buildingRepo.findAllByAccountId(account.getId(), pageable);
        }

        model.addAttribute("title", "Shield");
        model.addAttribute("account", account);
        model.addAttribute("building", building);
        model.addAttribute("numbers", new int[page.getTotalPages()]);
        model.addAttribute("page", page);
        model.addAttribute("size", sizePage);
        model.addAttribute("sort", "id");
        return "shield/building";
    }

    @PostMapping("/add-building")
    public String addBuilding(
            @AuthenticationPrincipal Account account,
            Building building) {
        building.setAccountId(account.getId());
        if (building.getDate() == null) {
            building.setDate(LocalDate.now());
        }
        buildingRepo.save(building);
        return "redirect:/shields/" + building.getId();
    }

    @GetMapping("/delete-building/{id}")
    public String deleteBuilding(
            @PathVariable Long id, @AuthenticationPrincipal Account account) {
        Long accountId = buildingRepo.getOne(id).getAccountId();
        if (account.getId() != accountId) {
            return "page-not-found";
        }

        buildingRepo.deleteById(id);
        return "redirect:/building";
    }

    @GetMapping("/shields/{buildingId}")
    public String showShields
            (@PathVariable Long buildingId,
             @AuthenticationPrincipal Account account,
             Shield shield,
             Model model) {
        List<Shield> shields = shieldRepo.findAllByBuildingId(buildingId);

        Long accountId = buildingRepo.getOne(buildingId).getAccountId();
        if (account.getId() != accountId) {
            return "page-not-found";
        }
        shield.setBuildingId(buildingId);
        model.addAttribute("shields", shields);
        model.addAttribute("title", "Shield");
        model.addAttribute("shield", shield);
        model.addAttribute("building", buildingRepo.getOne(buildingId));

        return "shield/shields";
    }

    @PostMapping("/add-shield")
    public String addShield(
            Shield shield) {
        shieldRepo.save(shield);
        return "redirect:/shields/" + shield.getBuildingId();
    }

    @GetMapping("/delete-shield/{id}")
    public String deleteShield(@PathVariable Long id, @AuthenticationPrincipal Account account) {
        long buildingId = shieldRepo.getOne(id).getBuildingId();
        Long accountId = buildingRepo.getOne(buildingId).getAccountId();
        if (account.getId() != accountId) {
            return "page-not-found";
        }
        shieldRepo.deleteById(id);
        return "redirect:/shields/" + buildingId;
    }

    @GetMapping("/shield/{shieldId}")
    public String printShield(
            @AuthenticationPrincipal Account account,
            @PathVariable Long shieldId,
            Model model) {
        Shield shield = shieldRepo.getOne(shieldId);
        Building building = shield.getBuilding();

        Long accountId = building.getAccountId();
        if (account.getId() != accountId) {
            return "page-not-found";
        }

        List<PartShied> groups = shield.getGroups();
        int count = 0;
        int number = 0;
        List<Shield> shields = building.getShields();
        for (Shield s : shields) {
            if (s.getId().equals(shield.getId())) {
                number = count + 1;
            }
            count++;
        }
        model.addAttribute("count", count);
        model.addAttribute("number", number);
        model.addAttribute("groups", groups);
        model.addAttribute("shield", shield);
        model.addAttribute("account", account);
        model.addAttribute("building", building);

        model.addAttribute("title", "Print");
        return "shield/print-shield";
    }

    @GetMapping("/showshield/{shieldId}")
    public String showShield(
            @PathVariable Long shieldId,
            @AuthenticationPrincipal Account account,
            PartShied partShied,
            Model model) {
        Shield shield = shieldRepo.getOne(shieldId);

        Long accountId = shield.getBuilding().getAccountId();
        if (account.getId() != accountId) {
            return "page-not-found";
        }

        List<PartShied> groups = shield.getGroups();
        Collections.sort(groups);
        model.addAttribute("group", partShied);
        model.addAttribute("groups", groups);
        model.addAttribute("shield", shield);
        model.addAttribute("title", "Shield");
        return "shield/update-shield";
    }

    @PostMapping("/add-group")
    public String addGroup(PartShied group) {
        partShieldRepo.save(group);
        Shield shield = shieldRepo.getOne(group.getShieldID());
        ratedPower(shield);
        return "redirect:/showshield/" + group.getShieldID();
    }

    @GetMapping("/delete-group/{id}")
    public String deleteGroup(@PathVariable Long id, @AuthenticationPrincipal Account account) {
        Long shieldID = partShieldRepo.getOne(id).getShieldID();
        Shield shield = shieldRepo.getOne(shieldID);

        Long accountId = shield.getBuilding().getAccountId();
        if (account.getId() != accountId) {
            return "page-not-found";
        }
        partShieldRepo.deleteById(id);
        ratedPower(shield);
        return "redirect:/showshield/" + shieldID;
    }

    private void ratedPower(Shield shield) {
        double sumCurrent = shield.getGroups().stream()
                .filter(partShied -> partShied.getRatedCurrent() != null)
                .mapToDouble(PartShied::getRatedCurrent)
                .sum();
        double sumUsage = shield.getGroups().stream()
                .filter(partShied -> partShied.getUsageRate() != null)
                .mapToDouble(PartShied::getUsageRate)
                .sum();

        List<Double> power = new ArrayList<>();
        int count = 0;
        List<PartShied> collect = shield.getGroups();
        for (PartShied group : collect) {
            if (group.getUsageRate() != null) {
                count++;
                if (group.getPhase() > 2) {
                    power.add(group.getRatedCurrent() * 0.38);
                } else {
                    power.add(group.getRatedCurrent() * 0.22);
                }
            }
        }

        double sumPower = power.stream().mapToDouble(Double::doubleValue).sum();

        double scale = Math.pow(10, 1);
        if (count != 0) {
            shield.setRatedPower(Math.ceil(((sumPower * sumUsage) / count) * scale) / scale);
            shield.setRatedCurrent(Math.ceil(((sumCurrent * sumUsage) / count) * scale) / scale);
        }
        shieldRepo.save(shield);
    }

}
