package com.example.moduleauth.entity;

import com.example.moduleauth.role.MemberRole;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class Auth {
	
	private final Long id;
	private final String email;
	private final MemberRole role;
	private final List<? extends GrantedAuthority> authority;
	
	public Auth(Long id, String email, MemberRole role) {
		this.id = id;
		this.email = email;
		this.role = role;
		this.authority = List.of(new SimpleGrantedAuthority(role.name()));
	}
}
