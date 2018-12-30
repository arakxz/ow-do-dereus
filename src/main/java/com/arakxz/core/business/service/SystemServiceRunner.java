package com.arakxz.core.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.arakxz.core.business.entity.Role;
import com.arakxz.core.business.entity.User;
import com.arakxz.core.business.repository.RoleRepository;
import com.arakxz.core.business.repository.UserRepository;
import com.arakxz.core.business.service.storage.StorageService;


@Component
public class SystemServiceRunner implements ApplicationRunner {

	@Autowired
	private UserRepository userrepo;

	@Autowired
	private RoleRepository rolerepo;
	
	@Autowired
	private StorageService storageService;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		this.rolerepo.save(new Role(Role.ROLE_ADMIN));
		this.rolerepo.save(new Role(Role.ROLE_MANAGER));

		User user = new User("dagoberto", UserService.passwordHash("123456"), "dossow.albornoz@gmail.com");
		user.addRole(this.rolerepo.findByName(Role.ROLE_ADMIN));

		this.userrepo.save(user);
		
		this.storageService.deleteAll();
		this.storageService.init();

	}

}
