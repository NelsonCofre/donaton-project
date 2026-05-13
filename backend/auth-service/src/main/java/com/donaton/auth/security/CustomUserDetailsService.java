package com.donaton.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.donaton.auth.repository.UserAccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserAccountRepository userAccountRepository;

	public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
		this.userAccountRepository = userAccountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userAccountRepository.findByEmailIgnoreCase(username)
			.map(CustomUserDetails::new)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}
