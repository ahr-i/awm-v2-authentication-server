package com.example.authentication_old.Repository.Oauth2Repository;

import com.example.authentication_old.JpaClass.UserTable.Oauth2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Oauth2Repository extends JpaRepository<Oauth2UserEntity,Integer> {
    Optional<Oauth2UserEntity> findByProviderUserId(String providerUserId);
}
