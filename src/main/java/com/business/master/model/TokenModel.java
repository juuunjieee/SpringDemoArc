package com.business.master.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TokenModel {
	
	@JsonProperty("Id") 
	private int id;
	
	@JsonProperty("LoginName") 
	private String loginName;
	
	@JsonProperty("UserName") 
	private String userName;
	
	@JsonProperty("Exp") 
	private long exp;
	
	public TokenModel(int id,String userName,String loginName,long exp) {
		super();
		this.loginName = loginName;
		this.userName = userName;
		this.id = id;
		this.exp = exp;
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

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}
}
