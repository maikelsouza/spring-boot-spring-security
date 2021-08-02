package com.codegate.springsecurity.repository;

import org.springframework.data.repository.CrudRepository;

import com.codegate.springsecurity.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Role findByRole(String role);

}
