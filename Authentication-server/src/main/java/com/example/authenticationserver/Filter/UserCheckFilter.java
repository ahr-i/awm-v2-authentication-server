package com.example.authenticationserver.Filter;

import com.example.authenticationserver.Entity.UserEntity;
import com.example.authenticationserver.JwtUtil.JsonWebToken;
import com.example.authenticationserver.Repository.UserRepository;
import com.example.authenticationserver.Service.AuthenticationDetailsUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Lazy
public class UserCheckFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public UserCheckFilter(AuthenticationManager manager,UserRepository userRepository){
        super(manager);
        this.authenticationManager = manager;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserEntity userEntity = objectMapper.readValue(request.getInputStream(), UserEntity.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEntity.getUserName()
                    ,userEntity.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));

            return authenticationManager.authenticate(token);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthenticationDetailsUserService principal = (AuthenticationDetailsUserService) authResult.getPrincipal();

        String jwtToken = JsonWebToken.createToken(principal.getUsername(),userRepository);

        if(jwtToken != null) {
            response.addHeader("Authorization","Bearer "+jwtToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }
    }
}