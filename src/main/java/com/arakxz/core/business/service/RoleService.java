package com.arakxz.core.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arakxz.core.business.entity.Role;
import com.arakxz.core.business.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository rolerepo;
	
	public List<Role> all() {
		return this.rolerepo.findAll();
	}
	
	public List<Role> findByUsernameIn(String[] roles) {
		
		List<Role> collection = new ArrayList<Role>();

		for (String name : roles) {
			collection.add(this.rolerepo.findByName(name));
		}
		
		return collection;
		
	}
	
}
