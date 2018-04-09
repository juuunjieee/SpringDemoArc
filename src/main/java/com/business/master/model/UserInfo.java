package com.business.master.model;

public class UserInfo {
    private Integer id;

    private String username;
    
    private String password;
    
    private Integer unumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Integer getUnumber() {
        return unumber;
    }

    public void setUnumber(Integer unumber) {
        this.unumber = unumber;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}