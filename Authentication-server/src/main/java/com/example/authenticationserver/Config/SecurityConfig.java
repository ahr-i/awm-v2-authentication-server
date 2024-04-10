package com.example.authenticationserver.Config;
import com.example.authenticationserver.Filter.JwtCheckFilter;
import com.example.authenticationserver.Filter.UserCheckFilter;
import com.example.authenticationserver.Repository.CommonRepository;
import com.example.authenticationserver.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final UserRepository userRepository;
    private final CommonRepository commonRepository;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity security, AuthenticationConfiguration configuration) throws Exception {

        security.httpBasic(AbstractHttpConfigurer :: disable);
        security.formLogin(AbstractHttpConfigurer :: disable);
        security.csrf(AbstractHttpConfigurer :: disable);
        security.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        security.addFilterBefore(new UserCheckFilter(configuration.getAuthenticationManager(),userRepository), UsernamePasswordAuthenticationFilter.class);
        security.addFilterBefore(new JwtCheckFilter(commonRepository,userRepository), UsernamePasswordAuthenticationFilter.class);
        security.authorizeHttpRequests(request -> request.antMatchers("/register","/user/authentication-api").permitAll());
        return security.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}