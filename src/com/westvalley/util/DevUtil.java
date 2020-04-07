package com.westvalley.util;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.soa.workflow.request.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开发工具类
 * @author oys
 */
public class DevUtil {

	private static LogUtil log = LogUtil.getLogger();

	/**
	 * 查询单个数据
	 * @param sql
	 * @param objs
	 * @return
	 */
	public static  String executeQuery(String sql,Object...objs){
		RecordSet reSet = getRecordSet();
		String cellValue = "";
		reSet.executeQuery(sql, objs);
		if(reSet.next()){
			cellValue=reSet.getString(1);
		}
		return cellValue;
	}
	
	/**
	 * 更新数据
	 * @param sql
	 * @param objs
	 * @return
	 */
	public static boolean executeUpdate(String sql,Object...objs){
		RecordSet reSet = getRecordSet();
		boolean b = reSet.executeUpdate(sql, objs);
		log.d("executeUpdate sqls", sql,objs,b);
		return b;
	}
	
	
	
	/**
	 * 手动OA事务
	 * @param sqls
	 * @return
	 */
	public static boolean exeuteTrans(List<String> sqls){
		RecordSetTrans setTrans = getRecordSetTrans();
		if(setTrans == null){
			setTrans = new RecordSetTrans();
		}
		boolean result = false;
		try {
			setTrans.setAutoCommit(false);
			for (String sql :sqls) {
				log.d("exeuteTrans sqls", sql);
				result = setTrans.executeSql(sql);
				if(!result){
					setTrans.rollback();
					return false;
				}
			}
			return setTrans.commit();
		} catch (Exception e) {
			log.d("使用事务出错", sqls,e);
			if(setTrans != null){
				setTrans.rollback();
			}
			return false;
		}
	}
	
	/**
	 * 更新主表数据
	 * @param requestID
	 * @param fieldName
	 * @param fieldValue
	 */
	public static boolean updateWFMainData(String requestID, String fieldName,Object fieldValue) {
		String workflowID = getWFReqWorkflowID(requestID);
		String tableName = getWFTableNameByWFID(workflowID);
		String sql = "UPDATE " + tableName + " SET " + fieldName+ " = ? WHERE REQUESTID = ?";
		return executeUpdate(sql, fieldValue, requestID);

	}

	/**
	 * 更新明细表表数据
	 * @param requestID
	 * @param index 明细表下标<br> formtable_main_10_dt1 --> 1<br> formtable_main_10_dt2 --> 2
	 * @param rowid 行id
	 * @param fieldName 字段名
	 * @param fieldValue
	 * @return
	 */
	public static boolean updateWFDetailData(String requestID, int index,String rowid, String fieldName, String fieldValue) {
		String workflowID = getWFReqWorkflowID(requestID);
		String tableName = getWFDetailTableName(workflowID, index);
		String mainTableName = getWFTableNameByReqID(requestID);
		String mainid = executeQuery(String.format("SELECT ID FROM %s WHERE REQUESTID = %s", mainTableName,requestID));
		String sql = String.format("Update %s SET %s = ? WHERE MAINID = ? AND  ID = ? ",tableName, fieldName);
		return executeUpdate(sql, fieldValue, mainid, rowid);
	}

	/**
	 * 删除明细数据
	 * @param requestID
	 * @param index 明细表下标<br> formtable_main_10_dt1 --> 1<br> formtable_main_10_dt2 --> 2
	 * @return
	 */
	public static boolean deleteWFDetailData(String requestID, int index) {
		String workflowID = getWFReqWorkflowID(requestID);
		String tableName = getWFDetailTableName(workflowID, index);
		String mainTableName = getWFTableNameByReqID(requestID);
		String mainid = executeQuery(String.format("SELECT ID FROM %s WHERE REQUESTID = %s", mainTableName,requestID));
		String sql = String.format("DELETE FROM %s WHERE MAINID = ? ",tableName);
		return executeUpdate(sql,mainid);
	}
	/**
	 * 获取workflow_base表的列数据
	 * @param id
	 * @param col
	 * @return
	 */
	public static String getWFBaseCol(String id,String col){
		return executeQuery(String.format(" SELECT %s FROM WORKFLOW_BASE WHERE ID = ? ", col), id);
	}
	/** 流程名 */
	public static String getWFBaseName(String id){
		return getWFBaseCol(id, "WORKFLOWNAME");
	}
	/** 表单id */
	public static String getWFBaseFormid(String id){
		return getWFBaseCol(id, "FORMID");
	}
	/** 附件上传分类 */
	public static String getWFBaseDocCategory(String id){
		return getWFBaseCol(id, "DOCCATEGORY");
	}
	
	/**
	 * 获取workflow_requestbase表的列数据
	 * @param requestID
	 * @param col
	 * @return
	 */
	public static String getWFReqCol(String requestID,String col){
		return executeQuery(String.format(" SELECT %s FROM WORKFLOW_REQUESTBASE WHERE REQUESTID = ? ", col), requestID);
	}
	/** 流程ID */
	public static String getWFReqWorkflowID(String requestID){
		return getWFReqCol(requestID, "WORKFLOWID");
	}
	/** 当前节点id */
	public static String getWFReqCurrentNodeID(String requestID){
		return getWFReqCol(requestID, "CURRENTNODEID");
	}
	/** 当前节点类型 */
	public static String getWFReqCurrentNodeType(String requestID){
		return getWFReqCol(requestID, "CURRENTNODETYPE");
	}
	/** 流程请求标题 */
	public static String getWFReqName(String requestID){
		return getWFReqCol(requestID, "REQUESTNAME");
	}
	/** 流程请求编号 */
	public static String getWFReqMark(String requestID){
		return getWFReqCol(requestID, "REQUESTMARK");
	}
	/** 流程请求创建人 */
	public static String getWFReqCreater(String requestID){
		return getWFReqCol(requestID, "CREATER");
	}
	/** 获取流程创建日期 */
	public static  String getWFReqDate(String requestID) {
		return getWFReqCol(requestID, "CREATEDATE");
	}

	/** 获取流程创建时间 */
	public static  String getWFReqTime(String requestID) {
		return getWFReqCol(requestID, "CREATETIME");
	}
	
	/** 获取最新版本的流程ID */
	public static String getWFBaseLastID(String workflowID){
		String sql = "SELECT ID FROM WORKFLOW_BASE WHERE ISVALID = 1 AND  FORMID = (SELECT FORMID FROM WORKFLOW_BASE WHERE ID =  ?)	";
		return executeQuery(sql, workflowID); 
	}
	/** 获取最新版本的流程ID */
	public static String getWFBaseLastIDByReqID(String requestID){
		return getWFBaseLastID(getWFReqWorkflowID(requestID)); 
	}
	
	/**
	 * 判断流程对于某个用户是否是待办
	 * @param requestID
	 * @param userID
	 * @return
	 */
	public static boolean isWFPendingForUser(String requestID,String userID){
		boolean flag =false;
		RecordSet rs =new RecordSet();
		RecordSet rs1 =new RecordSet();
		rs.executeQuery("select * from (select receivedate,receivetime from workflow_currentoperator where requestid='"+requestID+"'  order by receivedate||' '||receivetime desc) where rownum=1");
		if(rs.next()){
			rs1.executeQuery("select userid from  workflow_currentoperator where requestid='"+requestID+"' and receivedate ='"+rs.getString("receivedate")+"' and receivetime='"+rs.getString("receivetime")+"'");
			while(rs1.next()){
				if(userID.equals(rs1.getString("userid"))){
					flag =true;
				}
			}
		}
		if(flag){
			rs.executeQuery("select currentnodetype from workflow_requestbase where requestid='"+requestID+"'");
			String type ="";
			if(rs.next()){
				type = rs.getString("currentnodetype");
			}
			if("3".equals(type)){
				flag = false;
			}
		}
		return flag;
	}
	/** 是否为创建节点 */
	public static boolean isWFReqStart(String requestID){
		String nodeType = getWFReqCurrentNodeType(requestID);
		return "0".equals(nodeType);
	}
	/** 是否归档 */
	public static boolean isWFReqEnd(String requestID){
		String nodeType = getWFReqCurrentNodeType(requestID);
		return "3".equals(nodeType);
	}
	
	/** 获取流程对应的主表名称 */
	public static String getWFTableNameByBillID(String billID){
		return executeQuery(String.format(" SELECT %s FROM WORKFLOW_BILL WHERE ID = ? ", "TABLENAME"), billID);
	}
	/** 获取流程对应的主表名称*/
	public static  String getWFTableNameByWFID(String workflowID) {
		String id = getWFBaseFormid(workflowID);
		if (StringUtil.isEmpty(id)) {
			return "";
		}
		return getWFTableNameByBillID(id);
	}
	/** 获取主表名 */
	public static  String getWFTableNameByReqID(String requestID) {
		String id = getWFReqWorkflowID(requestID);
		if (StringUtil.isEmpty(id)) {
			return "";
		}
		return getWFTableNameByWFID(id);
	}
	/**
	 * 获取主表数据ID
	 * @param requestid
	 * @return
	 */
	public static  String getWFMainIDByReqID(String requestID) {
		if (StringUtil.isEmpty(requestID))return "";
		String table = getWFTableNameByReqID(requestID);
		return executeQuery(String.format("SELECT %s FROM %s WHERE REQUESTID = ? ", "ID",table), requestID);
	}
	/**
	 * 获取流程主表数据
	 * @param requestid
	 * @return 
	 */
	public static Map<String, String> getWFMainMapByReqID(String requestID) {
		Map<String, String> map = new HashMap<String, String>();
		String table = DevUtil.getWFTableNameByReqID(requestID);
		RecordSet rs = new RecordSet();
		rs.executeQuery(String.format("SELECT * FROM %s WHERE REQUESTID = ? ", table), requestID);
		String[] columnName = rs.getColumnName();
		if(rs.next()){
			for (String key : columnName) {
				String str = rs.getString(key);
				if(str != null){
					str.trim();
				}
				map.put(key, str);
			}
			
		}
		return map;
	}
	/**
	 * 主表 property转到map
	 * @param property
	 * @return
	 */
	public static  Map<String, String> getWFMainMapByInfo(RequestInfo info) {
		Map<String, String> m = new HashMap<String, String>();
		if(info != null && info.getMainTableInfo() != null && info.getMainTableInfo().getProperty() != null){
			for(Property p : info.getMainTableInfo().getProperty()){
				m.put( p.getName(), p.getValue());
			}
		}
		return m;
	}
	/**
	 * 获取明细表名
	 * @param workflowID
	 * @param index 明细表下标<br> formtable_main_10_dt1 --> 1<br> formtable_main_10_dt2 --> 2
	 * @return
	 */
	public static  String getWFDetailTableName(String workflowID, int index) {
		String sql = "SELECT B.TABLENAME FROM WORKFLOW_BASE A JOIN WORKFLOW_BILLDETAILTABLE B ON A.FORMID = B.BILLID WHERE A.ID = ? AND B.ORDERID = ?";
		return executeQuery(sql, workflowID, index);
	}
	
	/**
	 * 获取子表的所有数据
	 * @param index 明细表下标<br> formtable_main_10_dt1 --> 0 <br> formtable_main_10_dt2 --> 1
	 * @return
	 */
	public static  List<Map<String, String>> getWFDetailListByInfo(RequestInfo info, int index){
		List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
		if(info != null && info.getDetailTableInfo() !=null){
			DetailTable[] detailTable = info.getDetailTableInfo().getDetailTable();
			if(detailTable != null && detailTable[index] != null){
				for (Row row : detailTable[index].getRow()){
					Map<String, String> map = new HashMap<String, String>();
					for (Cell cell : row.getCell()){
						map.put(cell.getName(), cell.getValue());
					}
					map.put("id", row.getId());
					listMap.add(map);
				}
			}
		}
		return listMap;
	}
	/**
	 * 获取明细数据
	 * @param requestID
	 * @param index 明细表下标<br> formtable_main_10_dt1 --> 1<br> formtable_main_10_dt2 --> 2
	 * @return
	 */
	public static List<Map<String, String>> getWFDetailByReqID(String requestID, int index){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		String mainID = getWFMainIDByReqID(requestID);
		String workflowID = getWFReqWorkflowID(requestID);
		String table = getWFDetailTableName(workflowID, index);
		RecordSet rs = new RecordSet();
		rs.executeQuery(String.format("SELECT * FROM %s WHERE MAINID = ? ORDER BY ID", table), mainID);
		String[] columnName = rs.getColumnName();
		while(rs.next()){
			Map<String, String> map = new HashMap<String, String>();
			for (String key : columnName) {
				String str = rs.getString(key);
				if(str != null){
					str.trim();
				}
				map.put(key, str);
			}
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取 WORKFLOW_BILLFIELD 表中的某个值
	 * @param workflowID
	 * @param fieldName
	 * @param dtIndex 0是主表 ,明细下标<br> formtable_main_10_dt1 --> 1<br> formtable_main_10_dt2 --> 2
	 * @param comlunName  数据库表列名
	 * @return
	 */
	public static  String getWFFieldInfo(String workflowID, String fieldName,	int dtIndex, String comlunName) {
		String billid = getWFBaseFormid(workflowID);
		if (StringUtil.isEmpty(billid))return "";
		String fieldV = "";
		if (dtIndex > 0) {
			String dtTableName = executeQuery("SELECT TABLENAME FROM WORKFLOW_BILLDETAILTABLE WHERE BILLID = ? AND ORDERID = ?",
							billid, dtIndex);
			fieldV =executeQuery(
					String.format("SELECT %s  FROM WORKFLOW_BILLFIELD WHERE BILLID = ? AND FIELDNAME = ? AND DETAILTABLE = ? ", comlunName),
					billid, fieldName, dtTableName);
		}else{
			fieldV =executeQuery(
					String.format("SELECT %s  FROM WORKFLOW_BILLFIELD WHERE BILLID = ? AND FIELDNAME = ? AND VIEWTYPE = 0", comlunName),
					billid, fieldName);
		}
		return fieldV;
	}

	/**
	 * 获取下拉字段实际值
	 * @param workflowID
	 * @param fieldName
	 * @param selectValue
	 * @param dtIndex 明细表下标，如果为0则为主表
	 * @return
	 */
	public static  String getSelectName(String workflowID, String fieldName,String selectValue, int dtIndex) {
		String fieldId = getWFFieldInfo(workflowID, fieldName, dtIndex, "ID");
		if (StringUtil.isEmpty(fieldId))return "";
		return executeQuery("SELECT SELECTNAME FROM WORKFLOW_SELECTITEM WHERE FIELDID = ? AND SELECTVALUE = ?",
						fieldId, selectValue);

	}

	/**
	 * 获取字段显示中文名
	 * @param workflowID
	 * @param fieldName
	 * @param dtIndex  明细表下标，如果为0则为主表
	 * @param language  默认为7,简体中文
	 * @return
	 */
	public static  String getFieldLableName(String workflowID, String fieldName,int dtIndex,int language) {
		String fieldLabel = getWFFieldInfo(workflowID, fieldName, dtIndex,"FIELDLABEL");
		if (StringUtil.isEmpty(fieldLabel))	return "";
		language = language == 7 || language == 8 || language == 9 ? language : 7 ;
		return executeQuery("SELECT LABELNAME FROM HTMLLABELINFO WHERE INDEXID = ? AND LANGUAGEID = ?",
						fieldLabel, language);

	}
	/**
	 * 获取人力资源表信息
	 * @param id
	 * @param col
	 */
	public static String getHrmResourceCol(String userID,String col){
		return executeQuery(String.format("SELECT %s FROM HRMRESOURCE WHERE ID = ?", col), userID);
	}
	/** 获取用户名 */
	public static  String getHrmLastName(String userID) {
		return getHrmResourceCol(userID, "LASTNAME");
	}
	/** 获取登录名 */
	public static  String getHrmLoginID(String userID) {
		return getHrmResourceCol(userID, "LOGINID");
	}
	/** 获取工号 */
	public static  String getHrmWorkcode(String userID) {
		return getHrmResourceCol(userID, "WORKCODE");
	}
	/** 获取部门ID */
	public static  String getHrmDepartID(String userID) {
		return getHrmResourceCol(userID, "DEPARTMENTID");
	}
	/** 获取分部ID */
	public static  String getHrmSubCompanyID(String userID) {
		return getHrmResourceCol(userID, "SUBCOMPANYID1");
	}
	/** 获取所有下级员工 */
	public static  String getHrmUnderIDs(String userID){
		RecordSet recordSet = getRecordSet();
		recordSet.executeQuery("SELECT ID, MANAGERID FROM	HRMRESOURCE WHERE 1=1 START WITH ID = '"+userID+"' CONNECT BY PRIOR ID = MANAGERID ");
		StringBuffer sb = new StringBuffer();
		while(recordSet.next()){
			sb.append(",").append(recordSet.getString(1));
		}
		return sb.toString().replaceFirst(",", "");
	}
	
	/**
	 * 获取部门资源表信息
	 * @param id
	 * @param col
	 */
	public static String getHrmDepartmentCol(String departmentID,String col){
		return executeQuery(String.format("SELECT %s FROM HRMDEPARTMENT WHERE ID = ?", col), departmentID);
	}
	/** 获取部门名称 */
	public static String getDepartName(String departmentID){
		return getHrmDepartmentCol(departmentID, "DEPARTMENTNAME");
	}
	/** 获取部门编码 */
	public static String getDepartCode(String departmentID){
		return getHrmDepartmentCol(departmentID, "DEPARTMENTCODE");
	}
	/** 获取分部ID */
	public static String getDepartSubCompanyID(String departmentID){
		return getHrmDepartmentCol(departmentID, "SUBCOMPANYID1");
	}
	
	/**
	 * 获取分部资源表信息
	 * @param id
	 * @param col
	 */
	public static String getSubCompanyCol(String subcompanyID,String col){
		return executeQuery(String.format("SELECT %s FROM HRMSUBCOMPANY WHERE ID = ?", col), subcompanyID);
	}
	/** 获取分部名称 */
	public static String getSubCompanyName(String subcompanyID){
		return getSubCompanyCol(subcompanyID, "SUBCOMPANYNAME");
	}
	/** 获取分部编码 */
	public static String getSubCompanyCode(String subcompanyID){
		return getSubCompanyCol(subcompanyID, "SUBCOMPANYCODE");
	}
	
	/**
	 * 查询签字意见
	 * @param requestID
	 * @param nodeName 节点名称
	 * @return
	 */
	public static  String getOptionsByRequestId(String requestID,String nodeName){
		RecordSet reSet = getRecordSet();
		String tSQL="select id from workflow_nodebase where nodename= ? ";
		reSet.executeQuery(tSQL,nodeName);
		if(reSet.next()){
			int nodeID=reSet.getInt(1);
			return getOptionsByRequestId(requestID, nodeID);
		}
		return "";
	}
	/**
	 * 查询签字意见
	 * @param requestID
	 * @param nodeID
	 * @return
	 */
	public static  String getOptionsByRequestId(String requestID,int nodeID){
		RecordSet reSet = getRecordSet();
		StringBuffer  strBu=  new StringBuffer();
		String tSQL="select rl.requestid,rl.nodeid,(select nodename from workflow_nodebase where id = rl.nodeid) nodename, "
				+" rl.remark,(select lastname from (SELECT id,lastname FROM HrmResource  UNION ALL  SELECT id,lastname FROM HrmResourceManager )  T "
				+" where id = rl.operator) operatorName,operator,operatordept  "
				+" ,(SELECT departmentname FROM HrmDepartment WHERE id=rl.operatordept) operatordeptName "
				+" ,rl.operatedate||'  '||rl.operatetime AS operatedate,rl.receivedPersons  "
				+" from workflow_requestLog rl   "
				+" WHERE requestid = ? "
				+" and rl.nodeid in(select id from workflow_nodebase where nodeid= ? ) "
				+" order by operatedate " ;
		reSet.executeQuery(tSQL,requestID,nodeID);
		while(reSet.next()){
			String nodename=reSet.getString("nodename");
			String remark=reSet.getString("remark");
			remark=StringUtil.removeHTMLTag(remark);
			String operatorName=reSet.getString("operatorName");
			String operatordeptName=reSet.getString("operatordeptName");
			String operator=reSet.getString("operator");
			String operatedate=reSet.getString("operatedate");
			String opeatorMsg="";
			//为系统管理员
			if("1".equals(operator)){
				opeatorMsg=operatorName+"  "+operatedate;
			}else{
				opeatorMsg=operatordeptName+"/"+operatorName+" "+operatedate;
			}
			strBu
			.append(remark)
			.append("\r\n")
			.append(nodename)
			.append("\r\n")
			.append(opeatorMsg)
			;
		}
		
		if(strBu.length()>0){
			return strBu.toString();
		}
		return "";
	}
	
	public static RecordSet getRecordSet(){
		return new RecordSet();
	}
	
	public static RecordSetTrans getRecordSetTrans(){
		return new RecordSetTrans();
	}
	
	
}
