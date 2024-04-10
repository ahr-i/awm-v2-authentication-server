package com.example.authenticationserver.Repository;

import com.example.authenticationserver.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    public Optional<UserEntity> findByNickName(String username);
    public Optional<UserEntity> findByUserId(String userId);
    public Optional<UserEntity> findByUsername(String username);
}