package com.mudra.fakebookapi;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/*
 * Security settings
 * 	- HTTP GET calls to /books/** requires fakebookapi.read scope
 * 	- HTTP POST call to /books require fakebookapi.admin scope
 * 	- tokens come in as JWT 
 * 	- No sessions created during requests (No JSESSIONID Cookie created)
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {

	

	   @Bean
	   public PasswordEncoder passwordEncoder() {
	      return new BCryptPasswordEncoder();
	   }

	   @Override
	   public void configure(WebSecurity web) {
	      web.ignoring()
	         .antMatchers(
	            "/*.html",
	            "/favicon.ico",
	            "/**/*.html",
	            "/**/*.css",
	            "/**/*.js",
	            "/h2-console/**"
	         );
	   }
	   
	   
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
          .oauth2ResourceServer(oauth2 -> oauth2.jwt())
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
    
	@Bean
    CorsConfigurationSource corsConfigurationSource() 
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200/"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
