package com.calculation.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncodePassword {
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }
}
