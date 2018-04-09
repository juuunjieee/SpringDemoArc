package com.business.master.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TokenModel {
	
	@JsonProperty("Id") 
	private int id;
	
	@JsonProperty("LoginName") 
	private String loginName;
	
	@JsonProperty("UserName") 
	private String userName;
	
	public TokenModel(int id,String userName,String loginName) {
		super();
		this.loginName = loginName;
		this.userName = userName;
		this.id = id;
	}

	public TokenModel() {
		// TODO Auto-generated constructor stub
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
}
