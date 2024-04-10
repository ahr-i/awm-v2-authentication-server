package com.example.authenticationserver.Service;

import com.example.authenticationserver.mediator.UserInformation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class AuthenticationDetailsUserService implements UserDetails {

    private final UserInformation information;

    public AuthenticationDetailsUserService(UserInformation information){
        this.information = information;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        GrantedAuthority grantedAuthority = () -> {
            return information.getRole();
        };
        authorities.add(grantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return information.getPassword();
    }

    @Override
    public String getUsername() {
        return information.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}