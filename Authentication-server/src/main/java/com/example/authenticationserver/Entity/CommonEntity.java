package com.example.authenticationserver.Entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
public class CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commonId;
    private String userName;
    private String provider;
    private String nickname;
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userTablePK",referencedColumnName = "userId")
    private UserEntity userEntity;


    public CommonEntity(UserEntity userEntity) {
        this.nickname = userEntity.getNickName();
        this.role = userEntity.getRole();
        this.provider = userEntity.getProvider();
        this.userEntity = userEntity;
        this.userName = userEntity.getUserName();
    }
    @Builder
    public CommonEntity(String username, String provider, String nickname, String role) {
        this.userName = username;
        this.provider = provider;
        this.nickname = nickname;
        this.role = role;

    }

    public CommonEntity() {

    }
}