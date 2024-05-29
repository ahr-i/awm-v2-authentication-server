package com.example.authentication_old.Service.SpringSecurityLogin;

import com.example.authentication_old.Dto.UserDto;
import com.example.authentication_old.JpaClass.UserTable.Oauth2UserEntity;
import com.example.authentication_old.Repository.Oauth2Repository.Oauth2Repository;
import com.example.authentication_old.provider.Oauth2UserInfo;
import com.example.authentication_old.provider.googleOauthUser;
import com.example.authentication_old.provider.naverOauthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final Oauth2Repository oauth2Repository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Oauth2UserInfo userInfo = null;
        Oauth2UserEntity entity = null;
        Optional<Oauth2UserEntity> oauth2User = null;
        UserDto dto = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            log.info("Google request");
            userInfo = new googleOauthUser(oAuth2User.getAttributes());
            oauth2User = oauth2Repository.findByProviderUserId(userInfo.getProviderId());
            if(!oauth2User.isPresent()) {
                entity = Oauth2UserEntity.saveTransferOauth2User(userInfo);
                oauth2Repository.save(entity);
            }

            Optional<Oauth2UserEntity> oauthUser = oauth2Repository.findByProviderUserId(userInfo.getProviderId());
            dto = UserDto.oauthTransferEntity(oauthUser.get());
        }

        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            log.info("Naver request");
            userInfo = new naverOauthUser((Map)oAuth2User.getAttributes().get("response"));
            oauth2User = oauth2Repository.findByProviderUserId(userInfo.getProviderId());
            if(!oauth2User.isPresent()) {
                entity = Oauth2UserEntity.saveTransferOauth2User(userInfo);
                oauth2Repository.save(entity);
            }

            Optional<Oauth2UserEntity> oauthUser = oauth2Repository.findByProviderUserId(userInfo.getProviderId());
            dto = UserDto.oauthTransferEntity(oauthUser.get());

        }
        return new PrincipalDetails(oAuth2User.getAttributes(),dto);
    }
}
