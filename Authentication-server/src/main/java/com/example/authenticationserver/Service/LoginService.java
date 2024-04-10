package com.example.authenticationserver.Service;
import com.example.authenticationserver.Converter.UserConverter;
import com.example.authenticationserver.Dto.UserEntityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserConverter userConverter;

    public boolean join(UserEntityDto dto, HttpServletRequest request){
        return userConverter.FormLoginConverter(dto,request);
    }
}