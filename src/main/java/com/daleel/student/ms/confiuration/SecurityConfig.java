package com.daleel.student.ms.confiuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
				.antMatchers("/api/auth/**", "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**",
						"/v3/api-docs/**", "/webjars/**", "/swagger-ui/index.html", "/api-docs/**","/api/**")
				.permitAll().antMatchers(HttpMethod.POST, "/api/students").permitAll().anyRequest().authenticated()).csrf().disable().build();
		
		
	}

}
