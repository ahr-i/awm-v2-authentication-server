package com.example.authenticationserver.JwtUtil;

import com.example.authenticationserver.Entity.UserEntity;
import com.example.authenticationserver.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

public class JsonWebToken {

    private final static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String createToken(String username, UserRepository userRepository){
        Optional<UserEntity> byUsername = userRepository.findByUsername(username);


        Claims claims = Jwts.claims();
        UserEntity userEntity = byUsername.get();
        claims.put("username",userEntity.getUserName());
        claims.put("provider",userEntity.getProvider());
        claims.put("role",userEntity.getRole());

        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (60000 * 30 * 24)))
                .signWith(key).compact();
    }
    public static Claims  getToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}