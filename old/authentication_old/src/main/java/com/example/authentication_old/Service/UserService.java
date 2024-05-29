package com.example.authentication_old.Service;

import com.example.authentication_old.Dto.UserDto;
import com.example.authentication_old.Dto.UserInformationDto;
import com.example.authentication_old.JpaClass.UserTable.UserEntity;
import com.example.authentication_old.Repository.JpaRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;

    public void join(UserDto info) {
        UserEntity userEntity = UserDto.UserDtoTransferUser(info);
        repository.save(userEntity);
    }

    public UserDto findByUser(UserDto info) {
        Optional<UserEntity> byUserId = repository.findByUserId(info.getUserId());
        if(byUserId.isPresent()) {
            return UserDto.UserEntityToUserDto(byUserId.get());
        } return  null;
    }
}
