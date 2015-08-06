package com.aspiderngi.prepaid.api.service.agent;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.aspiderngi.common.service.entity.AgentDetails;
import com.aspiderngi.common.service.entity.User;

// TODO: vsapiha: remove User reference from this class
public class AgentAuthentication implements Authentication {
	 
	private static final long serialVersionUID = -3060646508937901054L;

	private final AgentDetails agent;
    private boolean authenticated = true;
 
    public AgentAuthentication(AgentDetails agent) {
        this.agent = agent;
    }
 
    @Override
    public String getName() {
        return agent.getLogin();
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return agent.getAuthorities();
    }
 
    @Override
    public Object getCredentials() {
        return agent.getPassword();
    }
 
    @Override
    public AgentDetails getDetails() {
        return agent;
    }
 
    @Override
    public Object getPrincipal() {
        return agent.getUsername();
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