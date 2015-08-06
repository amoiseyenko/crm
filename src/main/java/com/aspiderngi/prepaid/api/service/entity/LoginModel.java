package com.aspiderngi.prepaid.api.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginModel {

	private String userName;
	private String password;

	public LoginModel() {
		super();
	}

	public LoginModel(String userName, String password) {
		super();

		this.userName = userName;
		this.password = password;
	}

	@JsonProperty
	public String getUserName() {
		return userName;
	}
	
	@JsonProperty
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginModel [userName=" + userName + ", password=" + password + "]";
	}
}