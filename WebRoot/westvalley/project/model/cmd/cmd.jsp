<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.project.dto.ResultDto" %>
<%@ page import="com.westvalley.util.LogUtil" %>
<%@ page import="com.westvalley.util.StringUtil" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="com.westvalley.project.model.export.ProjBudAllExport" %>
<%@ page import="com.westvalley.project.model.dto.ProjBudYearDto" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="weaver.general.GCONST" %>
<%@ page import="java.io.File" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String cmd = Util.null2String(request.getParameter("cmd"));
	if("delProject0".equals(cmd)){

	    String billid=Util.null2String(request.getParameter("billid"));
		RecordSet rs =new RecordSet();
		//如果以导入预算数据，不允许删除
		rs.executeQuery("select p.projNo from uf_projcost p join uf_OrgProject b on p.projNo = b.orgCode where b.id = ?",billid);
		String msg = "";
		if(rs.next()){
			StringBuilder sb = new StringBuilder();
			sb.append(" 祖项编码：").append(rs.getString("projNo")).append("已配置成本中心，不能删除！");
			msg = sb.toString();
		}else{
			rs.executeUpdate("delete from uf_OrgProject where id = ? ",billid);
		}
		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,null));
	}else if("delProjcost".equals(cmd)){
		String billid=Util.null2String(request.getParameter("billid"));
		RecordSet rs =new RecordSet();
		RecordSet rs2 =new RecordSet();
		//如果以导入预算数据，不允许删除
		rs.executeQuery(" select p.projAmt,p.projYear,p.projNo,p.id,b.isVirtual from uf_proj p join uf_projcost b " +
						" on p.projNo = b.projNo " +
						" and p.projDeptNo = b.costNo " +
//						"and p.businessType = b.businessType " +
						" where b.id = ? ",
				billid);
		String msg = "";
		if(rs.next()){
			int isVirtual = Util.getIntValue(rs.getString("isVirtual"), -1);
			if(isVirtual == 0) {
			    //虚拟祖项可以删除
				//判断是否存在预算使用记录
				rs2.executeQuery("select a.id from wv_proj_excuDetail a join workflow_requestbase b on a.requestid = b.requestid and b.currentnodetype != 0 where a.projID =? and a.projType = 0",rs.getString("id"));
				if(rs2.next()){
					//存在使用预算，不允许删除
					StringBuilder sb = new StringBuilder();
					sb.append(" 祖项编码：").append(rs.getString("projNo"));
					sb.append(" 成本中心：").append(rs.getString("costNo"));
					sb.append(" 于").append(rs.getString("projYear")).append("年");
					sb.append(" 已导入预算：").append(rs.getString("projAmt"));
					msg = sb.toString();
				}else{
					rs.executeUpdate("delete from uf_projcost where id = ? ",billid);
					rs.executeUpdate("delete from uf_proj where id = ? ",rs.getString("id"));
				}
			}else{
				StringBuilder sb = new StringBuilder();
				sb.append(" 祖项编码：").append(rs.getString("projNo"));
				sb.append(" 成本中心：").append(rs.getString("costNo"));
				sb.append(" 于").append(rs.getString("projYear")).append("年");
				sb.append(" 已导入预算：").append(rs.getString("projAmt"));
				msg = sb.toString();
			}
		}else{
			rs.executeUpdate("delete from uf_projcost where id = ? ",billid);
		}
		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,null));
	}else if("saveProjcost".equals(cmd)){
		String billid=Util.null2String(request.getParameter("billid"));
		String businessType=Util.null2String(request.getParameter("businessType"));
		String projNo=Util.null2String(request.getParameter("projNo"));
		String costNo=Util.null2String(request.getParameter("costNo"));
		RecordSet rs =new RecordSet();
		//如果以导入预算数据，不允许删除
		rs.executeQuery(" select * from uf_projcost where  businessType = ? and projNo = ? and costNo = ? and id != ? ",
				businessType,projNo,costNo,billid);
		String msg = "";
		if(rs.next()){
			StringBuilder sb = new StringBuilder();
			String type = "";
			if("0".equals(businessType)){
				type = "新零售";
			}else if("1".equals(businessType)){
				type = "海外";
			}else if("2".equals(businessType)){
				type = "品牌中心";
			}

			sb.append(" 祖项编码：").append(rs.getString("projNo"));
			sb.append(" 成本中心：").append(rs.getString("costNo"));
			sb.append(" 业务类型：").append(type);
			sb.append(" 已存在！");
			msg = sb.toString();
		}
		out.clear();
		out.print(ResultDto.init(msg.length() == 0,msg,null));
	}else if("exportBudAll".equals(cmd)){
		ResultDto dto = null;
		try {
			String ids=Util.null2String(request.getParameter("ids"));

			RecordSet rs = new RecordSet();
			int year = 0;
			if(!StringUtil.isEmpty(ids)){
                rs.executeQuery("select  projYear from wv_v_projExcuCollect where proj0id in ("+ids+") group by projYear order by projYear desc ");
            }else{
			    //导出全部设计权限问题
                rs.executeQuery("select  projYear from wv_v_projExcuCollect group by projYear order by projYear desc");
            }
			if(rs.next()){
                year = rs.getInt(1);
            }


			ProjBudAllExport export = new ProjBudAllExport();
			ProjBudYearDto data = export.getData(year, ids);

			String filePath = "/westvalley/project/model/temp/"+ UUID.randomUUID().toString()+".xls";
			File file = new File(GCONST.getRootPath()+filePath);
			if (!file.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建父目录
				if (file.getParentFile().mkdirs()) {
					file.createNewFile();
				}
			}

			FileOutputStream outputStream = new FileOutputStream(file);

			export.export(data,outputStream);

			JSONObject obj = new JSONObject();
			obj.put("path",filePath);

			dto = ResultDto.ok("生成temp文件成功",obj);
		} catch (Exception e) {
			LogUtil.getLogger().e("导出出错",e);
			dto = ResultDto.error("导出出错 : "+e.getMessage());
		}
		out.clear();
		out.print(dto);
	}else{
		out.clear();
		out.print(ResultDto.error("错误的操作"));
	}
	
	
%>
