package com.github.gaoqisen.webcenter.utils;


import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */
public class Result extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	
	public Result() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static Result error() {
		return error(1, "网络异常！");
	}
	
	public static Result error(String msg) {
		return error(1, msg);
	}

	public static Result logout(String msg) {
		return error(3, msg);
	}
	
	public static Result error(int code, String msg) {
		Result result = new Result();
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	public static Result success(String msg) {
		Result result = new Result();
		result.put("msg", msg);
		return result;
	}
	
	public static Result success(Map<String, Object> map) {
		Result result = new Result();
		result.putAll(map);
		return result;
	}
	
	public static Result success() {
		return new Result();
	}

	@Override
	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	public Result putData(Object value) {
		super.put("data", value);
		return this;
	}
}
