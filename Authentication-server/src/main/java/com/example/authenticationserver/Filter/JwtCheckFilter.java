package com.example.authenticationserver.Filter;

import com.example.authenticationserver.Dto.UserEntityDto;
import com.example.authenticationserver.Entity.CommonEntity;
import com.example.authenticationserver.Entity.UserEntity;
import com.example.authenticationserver.JwtUtil.JsonWebToken;
import com.example.authenticationserver.Mapper.TransferClass;
import com.example.authenticationserver.Repository.CommonRepository;
import com.example.authenticationserver.Repository.UserRepository;
import com.example.authenticationserver.Service.AuthenticationDetailsUserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.authenticationserver.mediator.UserInformation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtCheckFilter extends OncePerRequestFilter {
    private final CommonRepository commonRepository;
    private final UserRepository userRepository;


    public String jwtCheckUser(HttpServletRequest request, HttpServletResponse response) {
        String authorization = null;

        authorization = request.getHeader("Authorization");

        String token = authorization.split(" ")[1];
        Claims userJwtToken = JsonWebToken.getToken(token);

        String username = userJwtToken.get("username", String.class);



        return commonRepository.findByUsername(username)
                .filter(commonEntity -> "FormLogin".equals(commonEntity.getProvider()))
                .map(CommonEntity::getUserName)
                .orElseThrow(() -> new NoSuchElementException("해당하는 사용자가 없습니다."));

    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = null;

        authorization = request.getHeader("Authorization");

        if (request.getRequestURI().startsWith("/login") || request.getRequestURI().startsWith("/register")) {

            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = authorization.split(" ")[1];
            Claims userToken = JsonWebToken.getToken(token);
            String provider = userToken.get("provider", String.class);

            if (provider.equals("FormLogin")) {
                String username = userToken.get("username", String.class);
                Optional<CommonEntity> byUsername = commonRepository.findByUsername(username);

                if (byUsername.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else if (provider.equals("Oauth")) {
            }
            UserEntity userEntity = userRepository.findByUsername(userToken.get("username", String.class)).get();

            UserEntityDto transferDto = TransferClass.getTransfer(userEntity, UserEntityDto.class);
            UserInformation transferUserInformation = TransferClass.getTransfer(transferDto, UserInformation.class);

            AuthenticationDetailsUserService userService = new AuthenticationDetailsUserService(transferUserInformation);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                    (userService, userService.getPassword(), userService.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {

            log.info("Stack Trace = {}", e.getMessage());

            if (e instanceof io.jsonwebtoken.security.SignatureException) {
                log.error("SignatureException: 토큰 서명 검증에 실패했습니다.", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 일치하지 않습니다.");
            }
        }
    }
}