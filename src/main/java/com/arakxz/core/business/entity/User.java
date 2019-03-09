package com.arakxz.core.business.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 32)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Date created;
    
    @OneToOne(mappedBy = "user")
    private Profile profile;
    
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "users_roles",
            joinColumns = { @JoinColumn(name = "id_user") },
            inverseJoinColumns = { @JoinColumn(name = "id_role") }
    )
    private Collection<Role> roles = new ArrayList<Role>();

    
    public User() {

    }

    /**
     * 
     * @param username
     * @param password
     * @param email
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @PrePersist
    protected void onCreate() {
        this.created = new Date();
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Collection<Role> getRoles() {
        return roles;
    }

    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    public boolean hasRole(String rolename) {
        for (Role role : this.roles) {
            if (role.getName().equals(rolename)) {
                return true;
            }
        }
        return false;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return this.hasRole(Role.ROLE_ADMIN);
    }

}
