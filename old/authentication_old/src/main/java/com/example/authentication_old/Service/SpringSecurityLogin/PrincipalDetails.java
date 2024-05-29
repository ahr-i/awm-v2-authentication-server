package com.example.authentication_old.Service.SpringSecurityLogin;

import com.example.authentication_old.Dto.UserDto;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
    private UserDto userInfo;
    private Map<String, Object> attribute;

    public PrincipalDetails(UserDto userInfo) {
        this.userInfo = userInfo;
    }

    public PrincipalDetails(Map<String, Object> attribute, UserDto dto) {
        this.attribute = attribute;
        this.userInfo = dto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attribute != null ? attribute : Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return userInfo != null ? userInfo.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return userInfo.getUserId();
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

    @Override
    public String getName() {
        return userInfo != null ? userInfo.getUserId() : null;
    }
}
