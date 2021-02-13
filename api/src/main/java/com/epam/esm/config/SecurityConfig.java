package com.epam.esm.config;

import com.epam.esm.security.JwtConfigurer;
import com.epam.esm.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/api/auth/signup").permitAll()

                .antMatchers(HttpMethod.GET, "/api/certificates/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/certificates/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/certificates/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/certificates/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/certificates/*").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/tags/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/tags/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/tags/*").hasRole("ADMIN")

                .antMatchers("/api/statistics/*").hasRole("ADMIN")

                .antMatchers("/api/orders/*").hasAnyRole("ADMIN", "USER")

                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}