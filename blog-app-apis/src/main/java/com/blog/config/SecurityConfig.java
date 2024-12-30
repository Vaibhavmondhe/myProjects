package com.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.blog.security.CustomUserDetailService;
import com.blog.security.JwtAuthenticationEntryPoint;
import com.blog.security.JwtAuthenticationFilter;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	
	@Bean
   public  SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		 httpSecurity. 
		 csrf() .disable() .authorizeHttpRequests()
		 .requestMatchers("/api/v1/auth/login")
		 .permitAll()
		 .anyRequest()
		 .authenticated()
		  .and()
		  .exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
		  .and()
		  .sessionManagement()
		  .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		 
		 
		 
		httpSecurity.addFilterBefore(this.jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
		
		 httpSecurity.authenticationProvider(daoAuthenticationProvider());
		 DefaultSecurityFilterChain defaultSecurityFilterChain = httpSecurity.build();
		 return defaultSecurityFilterChain;
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean (AuthenticationConfiguration configration) throws Exception {
	return configration.getAuthenticationManager();
		
	}	
	
	
   @Bean
   public DaoAuthenticationProvider daoAuthenticationProvider() {
	 
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.customUserDetailService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	

}