package com.mcfuturepartners.crm.api.security.jwt;

import com.mcfuturepartners.crm.api.security.filter.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http){
        TokenFilter securityFilter = new TokenFilter(tokenProvider);
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
