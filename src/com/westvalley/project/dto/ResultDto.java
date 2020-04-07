package com.westvalley.project.dto;

import com.alibaba.fastjson.JSONObject;


/**
 * 处理结果基本类
 * @author ys ou
 * @Despration
 */
public class ResultDto {

	private String code;
	private String msg;
	private Object data;
	public static final String SUCCESS = "0";
	public static final String ERROR = "1";
	
	private ResultDto(String code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static ResultDto init(boolean result, String msg, Object data){
		String code = result ? SUCCESS : ERROR;
		return new ResultDto(code, msg, data);
	}
	
	public static ResultDto ok(){
		return init(true, null, null);
	}
	public static ResultDto ok(Object data){
		return init(true, "", data);
	}
	public static ResultDto ok(String msg, Object data){
		return init(true, msg, data);
	}
	
	public static ResultDto error(String msg){
		return init(false, msg, null);
	}
	public static ResultDto error(String msg, Object data){
		return init(false, msg, data);
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public boolean isOk(){
		return SUCCESS.equalsIgnoreCase(getCode()) ? true :false;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	
	
}
