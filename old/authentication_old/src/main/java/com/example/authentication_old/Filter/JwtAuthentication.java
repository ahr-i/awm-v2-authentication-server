package com.example.authentication_old.Filter;

import com.example.authentication_old.Dto.UserDto;
import com.example.authentication_old.JWT.JWTUtil;
import com.example.authentication_old.JpaClass.UserTable.UserEntity;
import com.example.authentication_old.Repository.JpaRepository.UserRepository;
import com.example.authentication_old.Service.SpringSecurityLogin.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JwtAuthentication extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager manager;
    private final UserRepository repository;

    public JwtAuthentication(AuthenticationManager manager, UserRepository repository) {
        super(manager);
        this.manager = manager;
        this.repository = repository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserDto user = objectMapper.readValue(request.getInputStream(), UserDto.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user.getUserId(), user.getPassword(), List.of(new SimpleGrantedAuthority("USER"))
            );
            return manager.authenticate(token);
        } catch (IOException e) {
            log.error("Error reading user input", e);
            throw new AuthenticationException("Error reading user input", e) {};
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = JWTUtil.createJwt(principal.getUserInfo());
        log.info("Generated JWT Token: {}", jwtToken);

        Optional<UserEntity> byUserId = repository.findByUserId(principal.getUserInfo().getUserId());
        if(byUserId.isPresent()) {
            UserDto dto = UserDto.UserEntityToUserDto(byUserId.get());
            //log.info("Converted UserEntity to UserDto: {}", dto);

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String jsonUser = mapper.writeValueAsString(dto);

            response.addHeader("Authorization", "Bearer " + jwtToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonUser);
            log.info("Response with user info and token sent.");
        } else {
            log.warn("User not found in repository for userId: {}", principal.getUserInfo().getUserId());
        }
    }
}
