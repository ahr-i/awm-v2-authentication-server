package com.example.authenticationserver.Config;

import com.example.authenticationserver.Converter.UserConverter;
import com.example.authenticationserver.Repository.CommonRepository;
import com.example.authenticationserver.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SpringConfig {

    private final UserRepository userRepository;
    private final CommonRepository commonRepository;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserConverter converter(){
        return new UserConverter(userRepository,passwordEncoder(),commonRepository);
    }
}