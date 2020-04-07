<%@ page import="com.westvalley.project.dto.ResultDto" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.westvalley.project.service.Proj0Service" %>
<%@ page import="com.westvalley.project.entity.Proj0Entity" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String cmd = Util.null2String(request.getParameter("cmd"));
	if("getProj0Amt".equals(cmd)){
		//获取祖项可用预算
	    int projID=Util.getIntValue(request.getParameter("projID"));
	    String requestID=Util.null2String(request.getParameter("requestID"));
		Proj0Service service = new Proj0Service();
		Proj0Entity entity = service.getProj0(projID, requestID);
		double amt = 0.00;
		String msg = "";
		if(entity != null){
			amt = entity.getProjBalance();
		}else{
			msg = "获取祖项可用预算失败！";
		}
		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,amt));
	}else{
		out.clear();
		out.print(ResultDto.error("错误的操作"));
	}
	
	
%>
