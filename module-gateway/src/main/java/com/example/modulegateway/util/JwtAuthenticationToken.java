package com.example.modulegateway.util;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import com.example.modulegateway.entity.Auth;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
	
	private final Auth auth;
	
	public JwtAuthenticationToken(Auth auth) {
		super(auth.getAuthority());
		this.auth = auth;
		setAuthenticated(true);
	}
	
	@Override
	public Object getCredentials() { return null; }
	
	@Override
	public Object getPrincipal() { return auth; }
}
