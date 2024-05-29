package com.example.authentication_old.Filter;

import com.example.authentication_old.Dto.UserDto;
import com.example.authentication_old.JWT.JWTUtil;
import com.example.authentication_old.JpaClass.UserTable.Oauth2UserEntity;
import com.example.authentication_old.JpaClass.UserTable.UserEntity;
import com.example.authentication_old.Repository.JpaRepository.UserRepository;
import com.example.authentication_old.Repository.Oauth2Repository.Oauth2Repository;
import com.example.authentication_old.Service.SpringSecurityLogin.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private UserRepository repository;
    private Oauth2Repository oauth2Repository;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository repository, Oauth2Repository oauth2Repository) {
        super(authenticationManager);
        this.repository = repository;
        this.oauth2Repository = oauth2Repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        log.info("Authorization header: {}", authorization);

        if (!request.getRequestURI().startsWith("/user/")) {
            chain.doFilter(request, response);
            return;
        }

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String token = authorization.split(" ")[1];
            log.info("Extracted token: {}", token);

            Claims userName = JWTUtil.getUserName(token, response);
            if(userName != null) {
                String provider = userName.get("provider", String.class);
                log.info("Extracted provider from token: {}", provider);

                if (provider != null) {
                    if (provider.equals("google") || provider.equals("naver")) {
                        String oauth2UserProviderId = userName.get("username", String.class);
                        Oauth2UserEntity entity = oauth2Repository.findByProviderUserId(oauth2UserProviderId).get();
                        UserDto dto = UserDto.oauthTransferEntity(entity);
                        PrincipalDetails details = new PrincipalDetails(dto);
                        Authentication authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("OAuth2 user authenticated.");
                        chain.doFilter(request, response);
                    } else {
                        String userId = userName.get("username", String.class);
                        Optional<UserEntity> byUserId = repository.findByUserId(userId);
                        if (byUserId.isPresent()) {
                            UserDto dto = UserDto.UserEntityToUserDto(byUserId.get());
                            PrincipalDetails details = new PrincipalDetails(dto);
                            Authentication authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.info("Standard user authenticated.");
                            chain.doFilter(request, response);
                        } else {
                            log.warn("User not found for userId: {}", userId);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        }
                    }
                }
            }
        } catch (SignatureException e) {
            log.error("SignatureException: Invalid JWT signature.", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
