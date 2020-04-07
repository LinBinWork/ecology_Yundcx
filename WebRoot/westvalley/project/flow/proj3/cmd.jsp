<%@ page import="com.westvalley.project.dto.ResultDto" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.westvalley.project.service.Proj2Service" %>
<%@ page import="com.westvalley.project.entity.Proj2Entity" %>
<%@ page import="com.westvalley.project.service.Proj1Service" %>
<%@ page import="com.westvalley.project.enums.CtrlLevelEnum" %>
<%@ page import="com.westvalley.project.entity.Proj1Entity" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String cmd = Util.null2String(request.getParameter("cmd"));
	if("getProj2Amt".equals(cmd)){
		//获取子项可用预算
	    int projID=Util.getIntValue(request.getParameter("projID"));
	    String requestID=Util.null2String(request.getParameter("requestID"));
		Proj2Service service = new Proj2Service();
		Proj2Entity entity = service.getProj2(projID, requestID);
		double amt = 0.00;
		String msg = "";
		if(entity != null){
			amt = entity.getProjBalance();
		}else{
			msg = "获取子项可用预算失败！";
		}
		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,amt));
	}else if("getProj1Amt".equals(cmd)){
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

				//可用余额为当前可用余额加上子项可用余额
				int proj2ID=Util.getIntValue(request.getParameter("proj2ID"));
				Proj2Service service2 = new Proj2Service();
				Proj2Entity entity2 = service2.getProj2(proj2ID, requestID);

				BigDecimal a = new BigDecimal(String.valueOf(amt));
				BigDecimal b = new BigDecimal(String.valueOf(entity2.getProjBalance()));
				amt = a.add(b).doubleValue();
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
