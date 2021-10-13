package com.mcfuturepartners.crm.api.security.config;

import com.mcfuturepartners.crm.api.security.filter.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
   private final TokenFilter securityFilter;
   @Bean
   PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
   @Bean
   ModelMapper mapper(){return new ModelMapper();}

   @Override
   public void configure(WebSecurity webSecurity) throws Exception{
      webSecurity.ignoring()
              .antMatchers("/h2-console/**");
   }

   @Override
    protected void configure(HttpSecurity http) throws Exception{
       http.csrf().disable();
       http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
       http.authorizeRequests()
               .antMatchers("/api/signup").permitAll()
               .antMatchers("/api/signin").permitAll()
               .antMatchers("/api/authenticate").permitAll()
               .antMatchers("/api/authority").permitAll()
               .antMatchers("/h2-console/**/**").permitAll()
               .antMatchers("/api/admin").hasAuthority("ADMIN")
               .antMatchers("/api/user").hasAuthority("USER")
               .anyRequest().authenticated();
       http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
   }

}