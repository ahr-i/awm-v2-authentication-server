package com.example.authenticationserver.Controller;

import com.example.authenticationserver.Filter.JwtCheckFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtCheckFilter checkFilter;


    @GetMapping("/user/authentication-api")
    public int AuthenticationCall(HttpServletRequest request, HttpServletResponse response){
        Integer value = checkFilter.jwtCheckUser(request, response);
        return value;
    }
}