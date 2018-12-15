package com.arakxz.core.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.arakxz.core.business.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	public User findByUsername(String username);

	public List<User> findAll();
	
	public List<User> findTop5ByOrderByIdDesc();
	
	public List<User> findByUsernameContaining(String username);
	
}
