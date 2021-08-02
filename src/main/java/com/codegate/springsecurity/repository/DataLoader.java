package com.codegate.springsecurity.repository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.codegate.springsecurity.model.Role;
import com.codegate.springsecurity.model.User;

@Component
public class DataLoader implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	

	@Override
	public void run(String... args) throws Exception {		
		roleRepository.save(new Role("USER"));
		roleRepository.save(new Role("ADMIN"));
		
		Role adminRole = roleRepository.findByRole("ADMIN"); 
		Role userRole = roleRepository.findByRole("USER");
		
		
		User user = new User("admin@code.com", bCryptPasswordEncoder.encode("password"), "Admin", "Super", true, "admin");
		user.setRoles(Arrays.asList(adminRole));
		
		userRepository.save(user);
		
		user = new User("user@code.com", bCryptPasswordEncoder.encode("password"), "User", "Super", true, "user");
		user.setRoles(Arrays.asList(userRole));
		
		userRepository.save(user);
		
	}

}
