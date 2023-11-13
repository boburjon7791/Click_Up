package com.example.demo.utils;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class JwtTokenUtils {
    private final CustomUserDetailsService customUserDetailsService;
    @Value("${jwt.token-key}")
    private String key;

    @Value("${jwt.text-key}")
    private String textKey;

    private final PasswordEncoder passwordEncoder;

    public String encode(@NonNull  String text){
        Date date = new Date(System.currentTimeMillis()+1000*60*3);
        return Jwts.builder()
                .setSubject(text)
                .setIssuer("Click Up")
                .setIssuedAt(new Date())
                .setExpiration(date)
                .signWith(textKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String decode(@NonNull String encodedText){
        Claims claims;
        try {
            claims=Jwts.parserBuilder()
                    .setSigningKey(textKey())
                    .build()
                    .parseClaimsJws(encodedText)
                    .getBody();
        }catch (Exception e){
            throw new BadRequestException("Incorrect text");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new BadRequestException("Time out");
        }
        return claims.getSubject();
    }
    public String generateToken(@NonNull String username, String password){
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        String detailsPassword = userDetails.getPassword();
        if (!passwordEncoder.matches(password, detailsPassword)) {
            throw new BadRequestException("Password is not correct");
        }
        Date date = new Date(System.currentTimeMillis()+1000*60*60);
        String token;
        try {
             token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(date)
                    .setIssuer("Click Up")
                    .signWith(signinKey(), SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e){
            throw new UnauthorizedException();
        }
        return token;
    }
    public String getEmail(@NonNull String authorization) {
        Claims claims;
        try {
             claims = Jwts.parserBuilder()
                    .setSigningKey(this.signinKey())
                    .build()
                    .parseClaimsJws(authorization)
                    .getBody();
        }catch (Exception e){
            throw new UnauthorizedException();
        }
        System.out.println(authorization);
        if (claims.getExpiration().before(new Date())) {
            throw new UnauthorizedException();
        }
        return claims.getSubject();
    }
    private Key signinKey(){
        byte[] decode = Decoders.BASE64.decode(this.key);
        return Keys.hmacShaKeyFor(decode);
    }
    private Key textKey(){
        byte[] decode = Decoders.BASE64.decode(this.textKey);
        return Keys.hmacShaKeyFor(decode);
    }

    public boolean isValid(String authorization) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(this.signinKey())
                    .build()
                    .parseClaimsJws(authorization)
                    .getBody();
            System.out.println(authorization);
        }catch (Exception e){
            throw new UnauthorizedException();
        }
        return claims.getExpiration().after(new Date());
    }
}
