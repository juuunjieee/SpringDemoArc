package com.business.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseData {
	
	public static final String ERRORS_KEY = "errors";
	
	private String msg;
	private int code;
	private boolean success;
	private Object detail;
	private List list;
	private final Map<String, Object> data = new HashMap<>();


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getDetail() {
		return detail;
	}

	public void setDetail(Object detail) {
		this.detail = detail;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Map<String, Object> getData() {
		return data;
	}
	
	public ResponseData putDataValue(String key, Object value) {
		data.put(key, value);
		return this;
	}
	
	/*private ResponseData(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}*/
	
	
	
	
	/*=========状态码start=========*/
	/*public static ResponseData ok() {
		return new ResponseData(200, "Ok");
	}
	
	public static ResponseData notFound() {
		return new ResponseData(404, "Not Found");
	}
	
	public static ResponseData badRequest() {
		return new ResponseData(400, "Bad Request");
	}
	
	public static ResponseData forbidden() {
		return new ResponseData(403, "Forbidden");
	}
	
	public static ResponseData unauthorized() {
		return new ResponseData(401, "unauthorized");
	}
	
	public static ResponseData serverInternalError() {
		return new ResponseData(500, "Server Internal Error");
	}
	
	public static ResponseData customerError() {
		return new ResponseData(1001, "Customer Error");
	}*/
	/*=========状态码end=========*/
}
