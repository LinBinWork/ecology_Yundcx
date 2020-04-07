<%@ page import="com.alibaba.fastjson.JSONArray" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.project.dto.ResultDto" %>
<%@ page import="com.westvalley.project.entity.ContractBudgetEntity" %>
<%@ page import="com.westvalley.project.entity.ProjEntity" %>
<%@ page import="com.westvalley.project.enums.CtrlLevelEnum" %>
<%@ page import="com.westvalley.project.service.BudgetContractService" %>
<%@ page import="com.westvalley.project.service.BudgetService" %>
<%@ page import="com.westvalley.project.util.ProjUtil" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.westvalley.project.Const" %>
<%@ page import="com.westvalley.action.prepay.PrepayCreAction" %>
<%@ page import="com.westvalley.util.DevUtil" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String cmd = Util.null2String(request.getParameter("cmd"));
	if("getContracMoney".equals(cmd)){
		//获取合同未付金额
	    String requestID=Util.null2String(request.getParameter("requestID"));
	    String contractRequestID=Util.null2String(request.getParameter("contractRequestID"));
		BudgetContractService contractService = new BudgetContractService();
		ContractBudgetEntity cben = contractService.getContractBudgetEntity(contractRequestID, requestID);

		String msg = "";
		if(cben ==null){
			msg = "关联合同数据出错！";
		}
		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,cben));
	}else if("getProjAmt".equals(cmd)){
		int projID=Util.getIntValue(request.getParameter("projID"));
		String requestID=Util.null2String(request.getParameter("requestID"));
		BudgetService budgetService = new BudgetService();
		ProjEntity projInfo = budgetService.getProjInfo(projID, requestID);
		String msg = "";
		double balance = CtrlLevelEnum.PARENT.compareTo(projInfo.getCtrlLevel()) == 0 ? projInfo.getParentBalance():projInfo.getProjBalance();

		JSONObject obj = new JSONObject();
		obj.put("balance",balance);
		obj.put("proj1ID", ProjUtil.getProj1ID4Child(projID));
		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,obj));
	}else if("checkExcustatus".equals(cmd)){
	    //校验项目是否已走完执行方案流程

		String projIDs=Util.null2String(request.getParameter("projIDs"));
		String[] split = projIDs.split(",");

		StringBuffer msg = new StringBuffer();
		RecordSet rs = new RecordSet();
		RecordSet rs1 = new RecordSet();
		for (String id : split) {

			rs.executeQuery("select a.projExcustatus,a.projno,a.projname from uf_proj a where a.id = ? and a.projExcustatus = 0 ",Util.getIntValue(id));
			if(rs.next()){
				rs1.executeQuery("select g.glxm from formtable_main_35 g join workflow_requestbase wr on g.requestid = wr.requestid where wr.currentnodetype = 3 and g.glxm = ? ",id);
				if(rs1.next()){

				}else{
					msg.append(" 项目编码：").append(rs.getString("projno"));
					msg.append(",项目名称：").append(rs.getString("projname"));
					msg.append(",未完成执行方案审批！");
				}
			}
		}

		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg.toString(),null));
	}else if("delProj1".equals(cmd)){
		String projIDs=Util.null2String(request.getParameter("projIDs"));
		RecordSet rs = new RecordSet();
		rs.executeQuery("select ftbm,projid from wv_v_proj1more where projid in ("+projIDs+")");

		JSONArray arr = new JSONArray();
		while(rs.next()){
			arr.add(rs.getString(1)+"@"+rs.getString(2));
		}
		out.clear();
		out.print(ResultDto.ok(arr));
	}else if("autoClick".equals(cmd)){
	//手动创建分录
		String requestID = Util.null2String(request.getParameter("requestID"));
		Const.setAutoData(requestID);
		out.clear();
		out.print(ResultDto.ok("1"));
	}else if("autoFlush".equals(cmd)){
		//加载时创建分录
		String requestID = Util.null2String(request.getParameter("requestID"));
		boolean isAuting = Const.isAuting(requestID);
		if(isAuting){
			PrepayCreAction service = new PrepayCreAction();
			ResultDto dto = service.updateDetail(requestID, DevUtil.getWFReqWorkflowID(requestID));
			if(dto == null){
			    dto = ResultDto.ok("更新分录成功");
            }
			Const.removeAutoData(requestID);
			out.clear();
            out.print(dto);
        }else{
            out.clear();
			out.print(ResultDto.ok("1"));
		}
	}else{
		out.clear();
		out.print(ResultDto.error("错误的操作"));
	}
	
	
%>
