package com.example.demo.authentication;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class Authenticator implements AuthenticationProvider {
	
	@Autowired
	private LdapVerifier ldapVerifier;
	
	@Autowired
    private UserService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String username = token.getName();
		String passWord = token.getCredentials().toString();
    	if(StringUtils.isBlank(username) || StringUtils.isBlank(passWord)){
    		throw new BadCredentialsException("Username and password can not be blanked !");
    	}
    	
    	try {
			if(ldapVerifier.doVerification(username, passWord) == false){
				throw new BadCredentialsException("Invalid username/password");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BadCredentialsException("Invalid username/password");
		}
    	
    	UserDetails userDetails = userDetailsService.loadUserByUsername(token.getName());
    	
		return new UsernamePasswordAuthenticationToken(userDetails,username,userDetails.getAuthorities());
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
}
