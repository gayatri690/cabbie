package com.cabbie.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET_KEY = "cabbiSecretKeycabbiSecretKeycabbiSecretKey";

//    JWT library expects a Key object, not a String.
//    So we convert:
//    String → byte[] → Key
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(UserDetails userDetails, String role){

        return Jwts.builder()
                .setSubject(userDetails.getUsername())   //Subject means who owns the token.
                .claim("role", role)
                .setIssuedAt(new Date())                  //This stores when token was created.
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24)) //This stores when token will expired. (48 hrs)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)   //This creates the signature.
                .compact();                                          //This converts everything into single string token.
    }

    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
