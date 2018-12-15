package com.arakxz.core.business.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arakxz.core.business.entity.Role;
import com.arakxz.core.business.entity.User;
import com.arakxz.core.business.repository.UserRepository;

@Service
public class UserService {

	public static final int OK = 1;	
	public static final int ERROR_PASSWORD = 2;
	public static final int ERROR_PASSWORD_NOT_MATCH = 3;
	public static final int ERROR_FIELD_EMPTY = 4;
	public static final int ERROR_USERNAME_REGISTERED = 5;
	public static final int ERROR_USERNAME_NOT_REGISTERED = 6;
	public static final int SUCCESS_CREATE_USER = 7;
	public static final int SUCCESS_UPDATE_USER = 8;

	@Autowired
	private UserRepository userrepo;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 
	 * @param username
	 * @param password
	 * @param passwordconfirm
	 * @param email
	 * 
	 * @return
	 */
	public int register(String username, String password, String passwordconfirm, String email) {

		if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
			return ERROR_FIELD_EMPTY;
		}

		if (!password.equals(passwordconfirm)) {
			return ERROR_PASSWORD;
		}

		User user = this.userrepo.findByUsername(username);

		if (user != null) {
			return ERROR_USERNAME_REGISTERED;
		}

		this.userrepo.save(new User(username, passwordHash(password), email));

		return OK;

	}

	/**
	 * 
	 * @param user
	 * 
	 * @return
	 */
	public int authenticate(User user) {

		if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
			return ERROR_FIELD_EMPTY;
		}

		User userdb = this.userrepo.findByUsername(user.getUsername());

		if (userdb == null) {
			return ERROR_USERNAME_NOT_REGISTERED;
		}

		if (!passwordHash(user.getPassword()).equals(userdb.getPassword())) {
			return ERROR_PASSWORD_NOT_MATCH;
		}

		HttpSession session = this.request.getSession(true);
		session.setAttribute("user", userdb);

		return OK;

	}

	/**
	 * 
	 * @return authenticated user
	 */
	public User authenticated() {
		return (User) this.request.getSession().getAttribute("user");
	}
	
	
	public List<User> all() {
		return this.userrepo.findAll();
	}
	
	
	public List<User> top5Users() {
		return this.userrepo.findTop5ByOrderByIdDesc();
	}
	
	
	public List<User> searchUser(String username) {
		return this.userrepo.findByUsernameContaining(username);
	}
	
	public int updateRoles(String username, List<Role> roles) {
		
		if (username.isEmpty() || roles.isEmpty()) {
			return ERROR_FIELD_EMPTY;
		}
		
		User user = this.userrepo.findByUsername(username);

		if (user == null) {
			return ERROR_USERNAME_NOT_REGISTERED;
		}

		user.setRoles(roles);
		
		this.userrepo.save(user);
		
		return SUCCESS_UPDATE_USER;
	}

	
	/**
	 * 
	 * @param password or string to be encrypted
	 * 
	 * @return encrypted string
	 * 
	 * @throws RuntimeException
	 */
	public static String passwordHash(String password) throws RuntimeException {
		try {

			MessageDigest hash = MessageDigest.getInstance("MD5");
			byte[] digest = hash.digest(password.getBytes());
			BigInteger number = new BigInteger(1, digest);

			String hashtext = number.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}

			return hashtext;

		} catch (NoSuchAlgorithmException error) {
			throw new RuntimeException(error);
		}
	}
	
}
