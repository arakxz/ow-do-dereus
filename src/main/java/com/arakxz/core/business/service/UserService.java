package com.arakxz.core.business.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arakxz.core.business.entity.Profile;
import com.arakxz.core.business.entity.Role;
import com.arakxz.core.business.entity.User;
import com.arakxz.core.business.repository.ProfileRepository;
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
	private UserRepository iuser;
	
	@Autowired
	private ProfileRepository iprofile;

	
	@Autowired
	private HttpServletRequest request;

	
	/**
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

		User user = this.iuser.findByUsername(username);

		if (user != null) {
			return ERROR_USERNAME_REGISTERED;
		}

		this.iuser.save(new User(username, passwordHash(password), email));

		return OK;

	}
	
	/**
	 * @param name
	 * @param phone
	 * @param message
	 * 
	 * @return
	 */
	public int profile(String name, String phone, String message) {
		
		User user = this.authenticated();
		Profile profile = user.getProfile();

		if (profile == null) {
			profile = new Profile();
			
			user.setProfile(profile);
			profile.setUser(user);
		}

		profile.setCountry("country");
		profile.setMessage(message);
		profile.setName(name);
		profile.setPhone(phone);
		
		this.iprofile.save(profile);
		
		return OK;
	}

	/**
	 * @param user
	 * 
	 * @return
	 */
	public int authenticate(User user) {

		if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
			return ERROR_FIELD_EMPTY;
		}

		User userdb = this.iuser.findByUsername(user.getUsername());

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
	 * @return authenticated user
	 */
	public User authenticated() {
		return (User) this.request.getSession().getAttribute("user");
	}
	
	
	/**
	 * @return
	 */
	public List<User> all() {
		return this.iuser.findAll();
	}
	
	
	/**
	 * @return
	 */
	public List<User> top5Users() {
		return this.iuser.findTop5ByOrderByIdDesc();
	}
	
	
	/**
	 * @param username
	 * 
	 * @return
	 */
	public List<User> searchUser(String username) {
		return this.iuser.findByUsernameContaining(username);
	}
	
	
	/** 
	 * @param id, the user's identifier
	 * 
	 * @return
	 */
	public User find(long id) {
		return this.iuser.findById(id)
				   .orElse(null);
	}
	
	
	/** 
	 * @param id, the user's identifier
	 * 
	 * @return
	 */
	public User find(String id) {
		return this.find(Long.parseLong(id));
	}
	
	
	/**
	 * @param username
	 * @param roles
	 * 
	 * @return
	 */
	public int updateRoles(String username, List<Role> roles) {
		
		if (username.isEmpty() || roles.isEmpty()) {
			return ERROR_FIELD_EMPTY;
		}
		
		User user = this.iuser.findByUsername(username);

		if (user == null) {
			return ERROR_USERNAME_NOT_REGISTERED;
		}

		user.setRoles(roles);
		
		this.iuser.save(user);
		
		return SUCCESS_UPDATE_USER;
	}

	
	/**
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
