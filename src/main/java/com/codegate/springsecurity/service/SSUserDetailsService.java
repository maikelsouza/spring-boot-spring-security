package com.codegate.springsecurity.service;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codegate.springsecurity.model.Role;
import com.codegate.springsecurity.model.User;
import com.codegate.springsecurity.repository.UserRepository;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	public SSUserDetailsService(UserRepository userRepository) {	
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {	
		try {
			User user = userRepository.findByUsername(username);
			if (user == null) {
				return null;
			}
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthories(user));
		}catch (Exception e) {
			throw new UsernameNotFoundException("User not found.");
		}		
	}
	
	private Set<GrantedAuthority> getAuthories(User user) {
		Set<GrantedAuthority> authority = new HashSet<>();		
		for (Role role : user.getRoles()) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());			
			authority.add(grantedAuthority);			
		}
		return authority;
	}

}
