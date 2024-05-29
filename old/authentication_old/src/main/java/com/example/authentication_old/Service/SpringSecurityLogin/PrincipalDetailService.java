package com.example.authentication_old.Service.SpringSecurityLogin;

import com.example.authentication_old.Dto.UserDto;
import com.example.authentication_old.JpaClass.UserTable.UserEntity;
import com.example.authentication_old.Repository.JpaRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserEntity> findByUser = userRepository.findByUserId(userId);

        if(findByUser != null) {
            UserDto dto = UserDto.UserEntityToUserDto(findByUser.get());
            return new PrincipalDetails(dto);
        }else throw new UsernameNotFoundException("User not found.");
    }
}
