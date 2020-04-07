<%@ page import="com.westvalley.project.dto.ResultDto" %>
<%@ page import="com.westvalley.project.service.Proj1Service" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.westvalley.project.entity.Proj1Entity" %>
<%@ page import="com.westvalley.project.enums.CtrlLevelEnum" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String cmd = Util.null2String(request.getParameter("cmd"));
	if("getProj1Amt".equals(cmd)){
		//获取父项可用预算
	    int projID=Util.getIntValue(request.getParameter("projID"));
	    String requestID=Util.null2String(request.getParameter("requestID"));
		Proj1Service service = new Proj1Service();
		Proj1Entity entity = service.getProj1(projID, requestID);
		double amt = 0.00;
		String msg = "";
		if(entity != null){
		    if(CtrlLevelEnum.PARENT.compareTo(entity.getCtrlLevel()) == 0){
				amt = entity.getProjCanSplitAmt();
			}else{
				amt = entity.getProjBalance();
			}
		}else{
			msg = "获取父项可用预算失败！";
		}
		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,amt));
	}else{
		out.clear();
		out.print(ResultDto.error("错误的操作"));
	}
	
	
%>
