package com.arakxz.core.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.arakxz.core.business.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	public Role findByName(String name);
	
	public List<Role> findAll();
	
}
