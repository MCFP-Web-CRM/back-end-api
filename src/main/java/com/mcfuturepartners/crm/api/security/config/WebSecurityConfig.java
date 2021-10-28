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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
       http.cors();
       http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
       http.authorizeRequests()
               .antMatchers("/**").permitAll()
               //.antMatchers("/admin/**").hasAuthority("ADMIN")
               //.antMatchers("/department/**").hasAuthority("USER")
               //.antMatchers("/api/**").hasAuthority("USER")
               //.antMatchers("/counsel/**").hasAuthority("USER")
               //.antMatchers("/product/**").hasAuthority("USER")
               //.antMatchers("/users/signup").permitAll()
               //.antMatchers("/users/signin").permitAll()
               //.antMatchers("/users").hasAuthority("ADMIN")
               //.antMatchers("/h2-console/**/**").permitAll()
               //.antMatchers("/users/admin").hasAuthority("ADMIN")
               //.antMatchers("/users/user").hasAuthority("USER")
               .anyRequest().authenticated();
       http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
   }
   /*@Bean
   CorsConfigurationSource corsConfigurationSource(){
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(Arrays.asList("*"));
      configuration.setAllowedMethods(Arrays.asList("*"));
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
   }*/


}
