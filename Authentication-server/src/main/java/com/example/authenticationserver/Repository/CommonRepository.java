package com.example.authenticationserver.Repository;

import com.example.authenticationserver.Entity.CommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommonRepository extends JpaRepository<CommonEntity,Integer> {

    Optional<CommonEntity> findByUsername(String username);


}