package com.example.authenticationserver.Entity;

import com.example.authenticationserver.Dto.UserEntityDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String nickName;
    private String username;
    private String password;
    private String role;
    private String provider;
    private String email;
    private String userIp;



    @Builder
    public UserEntity(String nickName, String username, String password, String role, String provider, String email,String userIp) {
        this.nickName = nickName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.email = email;
        this.userIp = userIp;
    }



}