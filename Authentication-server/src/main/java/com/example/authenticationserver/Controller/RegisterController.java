package com.example.authenticationserver.Controller;


import com.example.authenticationserver.Dto.UserEntityDto;
import com.example.authenticationserver.Service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final LoginService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntityDto dto, HttpServletRequest request){
        boolean join = service.join(dto, request);

        if(join) return ResponseEntity.ok("회원가입이 완료되었습니다.");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입을 할 수 없습니다.");
    }
}