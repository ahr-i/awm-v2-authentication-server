package com.example.authenticationserver.Converter;

import com.example.authenticationserver.Dto.UserEntityDto;
import com.example.authenticationserver.Entity.CommonEntity;
import com.example.authenticationserver.Entity.UserEntity;
import com.example.authenticationserver.Repository.CommonRepository;
import com.example.authenticationserver.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
public class UserConverter {


    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final CommonRepository commonRepository;

    public boolean FormLoginConverter(UserEntityDto dto, HttpServletRequest request){


        Optional<UserEntity> byUsername = userRepository.findByUsername(dto.getUsername());

        if(byUsername.isEmpty()) {
            UserEntity userEntity = UserEntity.builder().email(dto.getEmail())
                    .password(encoder.encode(dto.getPassword()))
                    .username(dto.getUsername())
                    .userIp(request.getRemoteAddr())
                    .nickName("익명")
                    .role("ROLE_USER")
                    .provider("FormLogin")
                    .email(dto.getEmail() != null ? dto.getEmail() : "이메일 없음")
                    .build();
            userRepository.save(userEntity);

            CommonEntity commonEntity = new CommonEntity(userEntity);

            commonRepository.save(commonEntity);



            return true;
        }
        return false;
    }
}