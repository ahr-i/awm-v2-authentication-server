package com.example.authentication_old.Controller;

import com.example.authentication_old.Dto.UserDto;
import com.example.authentication_old.Service.SpringSecurityLogin.PrincipalDetails;
import com.example.authentication_old.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class joinController {
    private final BCryptPasswordEncoder encoder;
    private final UserService service;

    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity join(@RequestBody UserDto info){
        String rockPassword = encoder.encode(info.getPassword());
        info.setPassword(rockPassword);
        UserDto byUserInfo = service.findByUser(info);

        if(byUserInfo != null){
            return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The user already exists or cannot register.");
        } else {
            service.join(info);
        }

        return ResponseEntity.ok().body("Registration is complete.");
    }
}
