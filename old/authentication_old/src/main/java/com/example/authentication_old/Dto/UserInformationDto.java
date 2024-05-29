package com.example.authentication_old.Dto;

import lombok.Data;

@Data
public class UserInformationDto {
    private String userId;
    private String nickName;

    private int rankScore;
    private byte[] image;
    private int state;
    private String provider;
}
