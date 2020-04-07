<%@ page import="com.westvalley.project.dto.ResultDto" %>
<%@ page import="com.westvalley.project.entity.ProjEntity" %>
<%@ page import="com.westvalley.project.service.BudgetService" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.util.DevUtil" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="com.alibaba.fastjson.JSONArray" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String cmd = Util.null2String(request.getParameter("cmd"));
	if("getProjInfo".equals(cmd)){
		//获取父项ID和可用余额
	    int projID=Util.getIntValue(request.getParameter("projID"));
	    String requestID=Util.null2String(request.getParameter("requestID"));
		BudgetService service = new BudgetService();
		ProjEntity entity = service.getProjInfo(projID, requestID);
		String msg = "";

		double balance = 0.00;

		JSONObject obj = new JSONObject();
		RecordSet rs = new RecordSet();
		if(entity == null){
			msg = "获取项目数据失败！";
		}else{
			balance = entity.getProjBalance();

		}
		obj.put("balance",balance);
		obj.put("en",entity);

		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,obj));
	}else if("getAllProj0".equals(cmd)){
		//获取父项ID和可用余额
		int proj1ID=Util.getIntValue(request.getParameter("proj1ID"));
		String requestID=Util.null2String(request.getParameter("requestID"));
		BudgetService service = new BudgetService();
		ProjEntity entity = service.getProjInfo(proj1ID, requestID);
		String msg = "";

		JSONObject obj = new JSONObject();
		obj.put("proj1ID",proj1ID);
		obj.put("proj1Name",entity.getProjName());
		obj.put("proj1Balance",entity.getProjBalance());

		JSONArray array = new JSONArray();

		RecordSet rs = new RecordSet();
		if(entity == null){
			msg = "获取项目数据失败！";
		}else{
			rs.executeQuery("select ftbm as proj0ID,ftbl from wv_v_proj1more where projid = ? ",proj1ID);
			while(rs.next()){
				entity = service.getProjInfo(rs.getInt(1), requestID);
				JSONObject obj0 = new JSONObject();
				obj0.put("proj0ID",entity.getId());
				obj0.put("proj0Name",entity.getProjName());
				obj0.put("proj0Balance",entity.getProjBalance());
				obj0.put("proj0Precent",rs.getString(2));
				array.add(obj0);
			}

		}
		obj.put("proj0List",array);
		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,obj));
	}else{
		out.clear();
		out.print(ResultDto.error("错误的操作"));
	}
	
	
%>
