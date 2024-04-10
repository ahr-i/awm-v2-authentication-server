package com.example.authenticationserver.mediator;
import com.example.authenticationserver.Dto.UserEntityDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j

public class UserInformation {

    private String username;
    private String nickname;
    private String password;
    private String role;
    private String provider;

    @Builder
    public UserInformation(String username, String nickname, String password, String role, String provider) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.provider = provider;
    }

    public UserInformation(UserEntityDto dto){
        this.username = dto.getUsername();
        this.nickname = dto.getNickName();
        this.role = dto.getRole();
        this.password = dto.getPassword();
        this.provider = dto.getProvider();
    }

}