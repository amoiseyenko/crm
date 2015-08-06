package com.aspiderngi.prepaid.api.service.entity;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.aspiderngi.common.service.entity.User;

// TODO: vsapiha: remove User reference from this class
public class UserAuthentication implements Authentication {
	 
	private static final long serialVersionUID = -3060646508937901054L;

	private final User user;
    private boolean authenticated = true;
 
    public UserAuthentication(User user) {
        this.user = user;
    }
 
    @Override
    public String getName() {
        return user.getUsername();
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }
 
    @Override
    public Object getCredentials() {
        return user.getPassword();
    }
 
    @Override
    public User getDetails() {
        return user;
    }
 
    @Override
    public Object getPrincipal() {
        return user.getUsername();
    }
 
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
 
    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}