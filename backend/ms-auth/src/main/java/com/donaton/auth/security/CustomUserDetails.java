package com.donaton.auth.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.donaton.auth.model.UserAccount;

public class CustomUserDetails implements UserDetails {

	private final UserAccount userAccount;

	public CustomUserDetails(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = userAccount.getRole().getPermissions().stream()
			.map(permission -> new SimpleGrantedAuthority(permission.name()))
			.collect(Collectors.toSet());
		authorities.add(new SimpleGrantedAuthority("ROLE_" + userAccount.getRole().name()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return userAccount.getPasswordHash();
	}

	@Override
	public String getUsername() {
		return userAccount.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return userAccount.isEnabled();
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}
}
