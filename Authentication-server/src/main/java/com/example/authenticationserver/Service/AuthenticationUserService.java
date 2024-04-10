package com.example.authenticationserver.Service;
import com.example.authenticationserver.Dto.UserEntityDto;
import com.example.authenticationserver.Entity.UserEntity;
import com.example.authenticationserver.Mapper.TransferClass;
import com.example.authenticationserver.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.example.authenticationserver.mediator.UserInformation;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthenticationUserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Optional<UserEntity> byUsername = userRepository.findByUsername(username);

        if(byUsername.isPresent()) {
            UserEntityDto transfer = TransferClass.getTransfer(byUsername.get(), UserEntityDto.class);


            UserInformation information = new UserInformation(transfer);

            AuthenticationDetailsUserService detailsUserService = new AuthenticationDetailsUserService(information);

            return detailsUserService;
        }else {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }
}