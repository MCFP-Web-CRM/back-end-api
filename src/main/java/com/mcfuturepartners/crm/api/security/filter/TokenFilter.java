package com.mcfuturepartners.crm.api.security.filter;

import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Configuration
public class TokenFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.resolveToken(request);
        try{
            if(token != null && tokenProvider.validateToken(token)){
                Authentication auth = tokenProvider.getAuthentication(token);

                SecurityContextHolder.clearContext();
                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        filterChain.doFilter(request,response);
    }

}
