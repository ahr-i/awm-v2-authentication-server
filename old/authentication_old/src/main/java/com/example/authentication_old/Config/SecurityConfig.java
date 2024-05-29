package com.example.authentication_old.Config;

import com.example.authentication_old.Dto.UserDto;
import com.example.authentication_old.Filter.JWTAuthorizationFilter;
import com.example.authentication_old.Filter.JwtAuthentication;
import com.example.authentication_old.JWT.JWTUtil;
import com.example.authentication_old.Repository.JpaRepository.UserRepository;
import com.example.authentication_old.Repository.Oauth2Repository.Oauth2Repository;
import com.example.authentication_old.Service.SpringSecurityLogin.PrincipalDetails;
import com.example.authentication_old.Service.SpringSecurityLogin.PrincipalOauth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final UserRepository repository;
    private final PrincipalOauth2UserService service;
    private final Oauth2Repository oauth2Repository;

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration configuration) throws Exception {
        http.csrf(AbstractHttpConfigurer:: disable);
        http.httpBasic(AbstractHttpConfigurer :: disable);
        http.formLogin(AbstractHttpConfigurer :: disable);
        http.addFilterBefore(new JwtAuthentication(configuration.getAuthenticationManager(),repository), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(new JWTAuthorizationFilter(configuration.getAuthenticationManager(),repository,oauth2Repository));
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/join").permitAll()
                        .anyRequest().permitAll()
                );

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.oauth2Login(oauth2 -> oauth2.successHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
                String jwtToken = JWTUtil.createOauthJwt(principal);
                ObjectMapper mapper = new ObjectMapper();
                UserDto userDto = principal.getUserInfo();
                log.info("토큰 정보 : {}",jwtToken);
                String oauthUserInfo = mapper.writeValueAsString(userDto);
                response.setContentType("application/json");
                response.addHeader("Authorization","Bearer "+ jwtToken);
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter writer = response.getWriter();
                writer.write(oauthUserInfo);
                writer.flush();
            }
        }));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource source(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
