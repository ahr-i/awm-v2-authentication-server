package com.example.authentication_old.JWT;

import com.example.authentication_old.Dto.UserDto;
import com.example.authentication_old.Service.SpringSecurityLogin.PrincipalDetails;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
public class JWTUtil {
    private final static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static Claims getUserName(String token, HttpServletResponse response) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("JWT processing error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return null;
    }

    public static String createJwt(UserDto dto) {
        try {
            Claims claims = Jwts.claims();
            claims.put("username", dto.getUserId());
            claims.put("provider", dto.getProvider());
            claims.put("nickName", dto.getNickName());
            claims.put("rankScore", dto.getRankScore());
            return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (60000 * 30 * 24)))
                    .signWith(key).compact();
        } catch (Exception e) {
            log.error("JWT creation error: {}", e.getMessage());
            return null;
        }
    }

    public static String createOauthJwt(PrincipalDetails user) {
        try {
            long expireTime = 60000 * 30 * 24;
            Claims claims = Jwts.claims();
            claims.put("username", user.getUserInfo().getUserId());
            claims.put("provider", user.getUserInfo().getProvider());
            claims.put("nickName", user.getUserInfo().getNickName());
            claims.put("rankScore", user.getUserInfo().getRankScore());
            return Jwts.builder()
                    .setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                    .signWith(key).compact();
        } catch (Exception e) {
            log.error("OAuth JWT creation error: {}", e.getMessage());
            return null;
        }
    }
}

