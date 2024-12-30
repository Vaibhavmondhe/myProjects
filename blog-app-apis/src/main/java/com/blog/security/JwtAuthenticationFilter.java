package com.blog.security;

import java.io.IOException;
import java.nio.charset.MalformedInputException;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		//token
		String requestToken=request.getHeader("Authorization");
		
		//Bearer 2352523sdgsg
		
		System.out.println(requestToken);
		
		String username=null;
		
		String token=null;
		
		if(requestToken!=null && requestToken.startsWith("Bearer")) {
			
			
			token=requestToken.substring(7);
			
			try {
			
			username=this.jwtTokenHelper.getUserNameFromToken(token);
			}
			catch(IllegalArgumentException e){
				System.out.println("enable to get jwt token");
			}catch(ExpiredJwtException e) {
				System.out.println(" jwt token is expired");
			}catch(MalformedJwtException e) {
				System.out.println("invalid error");
			}
		}else
		{
			System.out.println("jwt token  does not begin with bearer");
		}
		
		//once we get the token ,now validate
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
			
			if(this.jwtTokenHelper.validateToken(token, userDetails)) {
				
				//working fine
				//authentication karana hai
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
				
			}else
			{
				System.out.println("invalid jwt token");
			}
				
			
		}else
		{
			System.out.println("username is null and context is null");
		}
		filterChain.doFilter(request, response);
	}

}
