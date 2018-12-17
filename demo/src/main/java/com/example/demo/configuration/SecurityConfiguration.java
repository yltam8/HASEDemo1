package com.example.demo.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.example.demo.authentication.Authenticator;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Value("${portal.admin.role}")
	private String adminRole;
	
	@Value("${portal.user.role}")
	private String userRole;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Initalize HttpSecurity
		http
			.authorizeRequests()
			.antMatchers("/console/**").hasAnyAuthority(this.adminRole.trim(), this.userRole.trim())
			.and()
			.formLogin();
		
		http.csrf().disable();
		
		//Add Handler for access denied
		http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
			@Override
			public void handle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse,
					AccessDeniedException paramAccessDeniedException) throws IOException, ServletException {
				paramHttpServletResponse.setStatus(403);
				paramHttpServletResponse.sendRedirect("/HASElogin?accessDeny=true");	
			}
		});
		
		//Add Configuration log out
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/HASElogin").invalidateHttpSession(true);
		
		//Add Configuration log in
	 	http.formLogin().loginPage("/HASElogin").usernameParameter("userName")
	 	.passwordParameter("passWord").defaultSuccessUrl("/console").failureUrl("/HASElogin?error=InvalidUserNamePassword");
	}
	
    @Bean  
    public AuthenticationProvider authenticationProvider(){  
    	AuthenticationProvider authenticator = new Authenticator();  
        return authenticator;  
    }  
	

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
}
