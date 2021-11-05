package com.calculation.services;

import com.calculation.entity.Account;
import com.calculation.entity.Role;
import com.calculation.repository.AccountRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
public class RegisterAccountService implements UserDetailsService {

    @Value("URL")
   private static String URL;

    private final PasswordEncoder passwordEncoder;
    private final MailSend mailSend;
    private final AccountRepo accountRepo;
    @Autowired
    public RegisterAccountService(AccountRepo accountRepo, PasswordEncoder passwordEncoder, MailSend mailSend) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSend = mailSend;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepo.findByEmail(email);
        if (account == null || !account.isStatus()) {
            throw new UsernameNotFoundException("Account not found exist!");
        }
        return account;
    }

    public boolean addAccount(Account account) {
        String email = account.getEmail();
        Account accountFormDb = accountRepo.findByEmail(account.getEmail());

        if (accountFormDb != null) {
            log.warn("Данный аккаунт существует");
            return false;
        }
        account.setRole(Collections.singleton(Role.USER));

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setActivationCode(UUID.randomUUID().toString());

        if ( email != null && !email.isEmpty()) {
            String message = String.format("Здраствуйте, это сообщение было сформировано ватоматически \n" +
                            "Для продолжения регистрации перейдите по ссылке: %s/activation/%s \n" +
                            "______________________________________________________________________\n" +
                            "With best wishes, the site administration"
                    , URL,account.getActivationCode());
            try {

                mailSend.sendEmail(email,message,"Регистрация аккаунта");
            } catch (MessagingException e) {
                log.warn(e.toString());
                e.printStackTrace();
                return false;
            }
        }
        accountRepo.save(account);
        return true;
    }

    public boolean activatedAccount(String code) {
        Account account = accountRepo.findByActivationCode(code);
        if (account == null){
            return false;
        }
        account.setStatus(true);
        account.setActivationCode(null);
        accountRepo.save(account);
        return true;
    }

}
