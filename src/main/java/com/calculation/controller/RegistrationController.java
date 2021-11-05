package com.calculation.controller;

import com.calculation.entity.Account;
import com.calculation.services.RegisterAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final RegisterAccountService accountService;

    @Autowired
    public RegistrationController(RegisterAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal Account account, Model model) {
        model.addAttribute("account", account);
        return "login";
    }

    @GetMapping("/registration")
    public String registration(
            Account account,
            Model model) {
        model.addAttribute("account", account);
        model.addAttribute("title", "Registration");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("passwordConfirm") String passwordConfirm,
            Model model,
            @Valid Account account,
            BindingResult bindingResult) {
//        boolean isConfirmEmpty = StringUtils.isEmpty(password2);

        if (bindingResult.hasErrors()) {
            return registration(account, model);
        }

        if (passwordConfirm.isEmpty()) {
            model.addAttribute("password2Error", "Поле неможет быть пустым");
            return "registration";
        }

        if (account.getPassword() != null && !account.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Пароли не совпадают!");
            return "registration";
        }
        boolean addAccount = accountService.addAccount(account);
        if (!addAccount) {
            model.addAttribute("message", "Пользователь с данным email уже зарегистрирован!");
            return "registration";
        }
        model.addAttribute("message", "Проверьте вашу почту: " + account.getEmail());
        return "login";
    }

    @GetMapping("/activation/{code}")
    public String activation(Model model, @PathVariable String code) {
        boolean isActivated = accountService.activatedAccount(code);
        if (isActivated) {
            model.addAttribute("message", "Ваш аккаунт активирован");
        } else {
            model.addAttribute("message", "Аккаунт не найден, повторите регистрацию");
        }
        return "login";
    }
}
