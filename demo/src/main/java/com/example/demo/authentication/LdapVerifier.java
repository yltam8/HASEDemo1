package com.example.demo.authentication;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LdapVerifier {
	
	@Value(value = "${authenticate.ldap.server}")
	private String ldapServer;
 
	@Value(value = "${authenticate.ldap.domain}")
	private String ldapDomain;
	
	@Value(value = "${authenticate.ldap.contextfactory}")
	private String contextFactory;
 
	@Value(value = "${authenticate.ldap.authmode}")
	private String authmode;
	
	@Value("${console.login.username}")
	private String userName;
	
	public boolean doVerification(String loginName, String password) throws Exception {
		
		if (loginName.equalsIgnoreCase(userName)) {
			return true;
		} else {
			throw new Exception();
		}
			
		/*** 
		String ldapURL = "LDAP://"+this.ldapServer.trim();
	    Hashtable<String,String> env = new Hashtable<String,String>(6);
 
		env.put(Context.INITIAL_CONTEXT_FACTORY, contextfactory);
		env.put(Context.PROVIDER_URL, ldapURL);
		env.put(Context.SECURITY_AUTHENTICATION, authmode.trim());
		env.put(Context.SECURITY_PRINCIPAL, username + "@" + this.ldapDomain.trim());
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.REFERRAL, "ignore");
		DirContext ctx = null;
		try {
			ctx = new InitialDirContext(env);
			ctx.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (ctx != null)
				try {
					ctx.close();
					ctx = null;
				} catch (NamingException e) {
					e.printStackTrace();
				}
		}
		***/
	}
}
