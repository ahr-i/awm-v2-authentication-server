package com.example.authentication_old.Controller;

import com.example.authentication_old.Dto.UserInformationDto;
import com.example.authentication_old.Service.SpringSecurityLogin.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
@Slf4j
@RestController
public class authenticationController {
    @PostMapping("/user/authentication/get-id")
    public ResponseEntity authenticationJWT(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        UserInformationDto response = new UserInformationDto();

        response.setUserId(principalDetails.getUserInfo().getUserId());
        response.setNickName(principalDetails.getUserInfo().getNickName());
        response.setRankScore(principalDetails.getUserInfo().getRankScore());
        response.setImage(principalDetails.getUserInfo().getImage());
        response.setState(principalDetails.getUserInfo().getState());
        response.setProvider(principalDetails.getUserInfo().getProvider());

        return ResponseEntity.ok().body(response);
    }
}
