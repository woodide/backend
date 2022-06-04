package com.system.wood.global.config;

import com.system.wood.domain.Role;
import com.system.wood.jwt.AuthenticationEntryPointHandler;
import com.system.wood.jwt.JwtAuthenticationFilter;
import com.system.wood.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors() //(1)
            .and()
            .csrf() //(2)
            .disable()
            .sessionManagement() //(4)
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPointHandler)
            .and()
                .authorizeRequests()
                    .antMatchers("/professor/**").hasRole("PROFESSOR")
                    .antMatchers("/student/**").hasAnyRole("PROFESSOR","STUDENT")
                    .anyRequest().permitAll()
            .and()
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .formLogin().disable().headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}