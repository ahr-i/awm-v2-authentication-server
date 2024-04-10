package com.example.authenticationserver.Dto;

import lombok.Data;

@Data
public class UserEntityDto {

    private String userId;
    private String nickName;
    private String username;
    private String password;
    private String role;
    private String provider;
    private String email;
    private String userIp;

}