package com.westvalley.project;

import com.westvalley.util.StringUtil;
import weaver.file.Prop;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义常量
 * @Despration
 */
public class Const {

	/*
	 * 以下为获取payment配置文件
	 */
	private static final String PAYMENT = "payment_proj";
	private static String mode = Prop.getPropValue(PAYMENT, "mode");
	/**
	 * 获取payment.properties文件的值
	 * @param key 
	 * @return
	 */
	private static String getPaymentValue(String key){
		return Prop.getPropValue(PAYMENT, mode+"."+key);
	}
	private static String PROJ_MODELID;
	private static String PROJ_PERMISIONMODEID;
	private static String PROJ_PERMISION_ROLEID;

	static{
		PROJ_MODELID = getPaymentValue("projmodeid");
		PROJ_PERMISIONMODEID = getPaymentValue("projPermisionmodeId");
		PROJ_PERMISION_ROLEID = getPaymentValue("projPermisionRole");
	}
	/** 获取项目建模 */
	public static String getProjModelID(){
		return PROJ_MODELID;
	}
	/** 获取项目权限建模 */
	public static String getProjPermisionmodeid(){
		return PROJ_PERMISIONMODEID;
	}
	/** 获取项目权限初始化角色人员 */
	public static String getProjPermisionRoleid(){
		return PROJ_PERMISION_ROLEID;
	}


	/*
	 * 保存自动生成分录的状态
	 */
	private static Map<String, String> autoData = new HashMap<String, String>();
	public static synchronized void setAutoData(String requestID){
		if(!StringUtil.isEmpty(requestID)){
			autoData.put(requestID, requestID);
		}
	}
	public static synchronized void removeAutoData(String requestID){
		if(!StringUtil.isEmpty(requestID)){
			autoData.remove(requestID);
		}
	}
	public static synchronized boolean isAuting(String requestID){
		if(StringUtil.isEmpty(requestID)){
			return false;
		}
		return requestID.equals(autoData.get(requestID));
	}


}
