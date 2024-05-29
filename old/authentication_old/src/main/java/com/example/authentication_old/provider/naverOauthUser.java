package com.example.authentication_old.provider;

import java.util.Map;

public class naverOauthUser implements Oauth2UserInfo {
    private Map<String,Object> attribute;

    public naverOauthUser(Map<String, Object> attribute){
        this.attribute=attribute;
    }
    @Override
    public String getProviderId() {
        return (String)attribute.get("email");
    }

    @Override
    public String getProvider() {
        return "naver";
    }
}
