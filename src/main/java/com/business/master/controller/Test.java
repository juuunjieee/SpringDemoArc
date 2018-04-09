package com.business.master.controller;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.business.utils.JsonUtil;

public class Test {
	public static void main(String[] args) {
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJJZCI6MSwiTG9naW5OYW1lIjoiaHlwIiwiVXNlck5hbWUiOiJoeXAifQ==.a7bwIZx6sq9a/M2o7xxgS2CG5Q38vp/Qk298hDz9NQc=";
		try {
			Map<String,Object> tokenMap = new HashMap<String,Object>();
		    DecodedJWT jwt = JWT.decode(token);
		    Map<String, Claim> claims = jwt.getClaims();
		    for(String key : claims.keySet()){
		    	String str = claims.get(key).asString();
		    	if(str==null){
		    		Integer i = claims.get(key).asInt();
		    		if(i!=null){
		    			tokenMap.put(key, i);
		    		}
		    	}else{
		    		tokenMap.put(key, str);
		    	}
		    }
		    String str = JsonUtil.getJsonStr(tokenMap);
		    System.out.println(str);
		} catch (JWTDecodeException exception){
		    //Invalid token
			exception.printStackTrace();
		}
	}
}
