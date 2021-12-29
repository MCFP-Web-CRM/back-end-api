package com.mcfuturepartners.crm.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean  {

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${security.jwt.token.security-key:secret-key}")
    private String securityKey;

    @Value("${security.jwt.token.expiration-length:36000000}")
    private long validityInMs = 36000000; // 10h

    @Override
    public void afterPropertiesSet() throws Exception {
        securityKey = Base64.getEncoder().encodeToString(securityKey.getBytes());
    }

    public String createToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity = new Date(now + validityInMs);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256,securityKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(securityKey)
                .parseClaimsJws(token)
                .getBody();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(),"",authorities);
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String authToken) throws Exception {
        try {
            Jwts.parser().setSigningKey(securityKey).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new Exception();
        }
    }
    public Long getValidityTime(){
        return validityInMs;
    }
}
