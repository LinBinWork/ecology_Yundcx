<%@ page import="com.westvalley.project.dto.ResultDto" %>
<%@ page import="com.westvalley.project.entity.ProjEntity" %>
<%@ page import="com.westvalley.project.service.BudgetService" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.util.DevUtil" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String cmd = Util.null2String(request.getParameter("cmd"));
	if("getProjInfo".equals(cmd)){
		//获取父项ID和可用余额
	    int projID=Util.getIntValue(request.getParameter("projID"));
	    String requestID=Util.null2String(request.getParameter("requestID"));
	    String flag=Util.null2String(request.getParameter("flag"));
		BudgetService service = new BudgetService();
		ProjEntity entity = service.getProjInfo(projID, requestID);
		String msg = "";
		String pNo = "";

		String proj1Manager = "";
		String proj1Person = "";
		String proj2Manager = "";
		String proj2Person = "";

//		String proj1ManagerName = "";
//		String proj1PersonName = "";
//		String proj2ManagerName = "";
//		String proj2PersonName = "";

		double balance = 0.00;

		JSONObject obj = new JSONObject();
		RecordSet rs = new RecordSet();
		if(entity == null){
			msg = "获取项目数据失败！";
		}else{
			balance = entity.getProjBalance();

			int pid = 0;
			int projType = entity.getProjType();
			if(projType == 3 && "true".equals(flag)){
				//如果是孙子，获取子项
				ProjEntity projInfo = service.getProjInfo(entity.getPID(), requestID);
				if(projInfo == null){
					msg = "获取父项数据失败！";
				}else{
					obj.put("projInfo",projInfo);
					pid = projInfo.getPID();
					rs.executeQuery("select projManager,projPerson from uf_proj where id = ? ",projInfo.getId());
					if(rs.next()){
						proj2Manager = rs.getString("projManager");
						proj2Person = rs.getString("projPerson");
					}
					rs.executeQuery("select projManager,projPerson from uf_proj where id = ? ",projInfo.getPID());
					if(rs.next()){
						proj1Manager = rs.getString("projManager");
						proj1Person = rs.getString("projPerson");
					}
				}
			}else{
			    //子项
				pid = entity.getPID();
				rs.executeQuery("select projManager,projPerson from uf_proj where id = ? ",entity.getId());
				if(rs.next()){
					proj2Manager = rs.getString("projManager");
					proj2Person = rs.getString("projPerson");
				}
				rs.executeQuery("select projManager,projPerson from uf_proj where id = ? ",entity.getPID());
				if(rs.next()){
					proj1Manager = rs.getString("projManager");
					proj1Person = rs.getString("projPerson");
				}

			}
			if(pid > 0) {
				pNo = DevUtil.executeQuery("select projNo from uf_proj where id = ? ", pid);
			}
		}

		obj.put("pNo",pNo);
		obj.put("balance",balance);
		obj.put("en",entity);

		obj.put("proj1Manager",proj1Manager);
		obj.put("proj1Person",proj1Person);
		obj.put("proj2Manager",proj2Manager);
		obj.put("proj2Person",proj2Person);

		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,obj));
	}else if("getProjBalance".equals(cmd)){
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

		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,obj));
	}else{
		out.clear();
		out.print(ResultDto.error("错误的操作"));
	}
	
	
%>
