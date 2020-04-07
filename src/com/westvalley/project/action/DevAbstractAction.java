package com.westvalley.project.action;

import com.westvalley.project.dto.ActionDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

/**
 * action基本类
 * @author oys
 */
public abstract class DevAbstractAction implements Action {
	protected LogUtil log;

	public DevAbstractAction(){
		log = LogUtil.getLogger(getClass());
	}

	@Override
	public final String execute(RequestInfo reqInfo) {
		try {
			ActionDto actionDto = ActionDto.init(reqInfo);
			log.info(actionDto.begin());
			ResultDto runAction = runCode(reqInfo,actionDto);
			actionDto.setResultDto(runAction);
			log.info(actionDto.end());
			if(runAction == null || runAction.isOk()){
				return SUCCESS;
			}else{
				setRequestErrorMessage(reqInfo, runAction.getMsg());
				return FAILURE_AND_CONTINUE;
			}
		} catch (Exception e) {
			log.e("Action执行异常",e);
			setRequestErrorMessage(reqInfo, "出现异常，信息："+e.getMessage());
			return FAILURE_AND_CONTINUE;
		}
	}
	/**流程异常信息处理*/
	private void setRequestErrorMessage(RequestInfo reqInfo, String msg) {
		reqInfo.getRequestManager().setMessageid(reqInfo.getRequestid());
		reqInfo.getRequestManager().setMessagecontent(msg+"<br/>"+errorMsg(reqInfo.getRequestid()));
		reqInfo.getRequestManager().setMessage(reqInfo.getRequestManager().getRequestname());
	}
	private String errorMsg(String requestID){
		 return "<p>当前单据标识<b>"+requestID+"</b>，请将此标识发送给管理员解决此问题;</p>";
	}
	/**
	 * 获取map值
	 * @param map
	 * @param key
	 * @return
	 */
	public String getMapV(Map<String, String> map,String key){
		return StringUtil.toStr(map.get(key));
	}


	/**
	 * 获取map中的金额值
	 * @param map
	 * @param key
	 * @return
	 */
	public double getMapAmtV(Map<String, String> map,String key){
		return Util.getDoubleValue(StringUtil.toNum(map.get(key)),0);
	}



	/**
	 * 业务逻辑
	 * @param info
	 * @param actionDto
	 * @return 返回 null 或者ok 则代表通过
	 */
	protected abstract ResultDto runCode(RequestInfo info, ActionDto actionDto);
	
	
	
	
	
}
