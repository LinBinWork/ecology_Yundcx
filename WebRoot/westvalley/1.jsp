<%@ page import="com.westvalley.project.dto.ResultDto" %>
<%@ page import="com.westvalley.util.StringUtil" %>
<%@ page import="weaver.general.Util" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.westvalley.project.service.BudgetService" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.westvalley.project.entity.Proj2Entity" %>
<%@ page import="weaver.conn.RecordSet" %>
<%@ page import="com.westvalley.project.util.EnumsUtil" %>
<%@ page import="com.westvalley.project.enums.ExpTypeEnum" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.alibaba.fastjson.JSONArray" %>
<%@ page import="com.westvalley.util.DevUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.westvalley.project.util.ProjUtil" %>
<%@ page import="java.io.IOException" %>
<%@ page import="weaver.general.GCONST" %>
<%@ page import="java.io.File" %>
<%@ page import="com.westvalley.project.entity.Proj1Entity" %>
<%@ page import="com.westvalley.project.enums.BudTypeEnum" %>
<%@ page import="com.westvalley.project.dao.Proj1Dao" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%

    try {
        String requestID = "263681";
//        JSONArray debitList = getDebitList(requestID, DevUtil.getWFMainMapByReqID(requestID),response);
        BudgetService s = new BudgetService();
        String path = GCONST.getRootPath()+"/westvalley/project/model/xlsx/projbud.xlsx";
        File file = new File(path);

        out.print("<br>分录信息<textarea>"+ JSONObject.toJSONString(getProj1BudgetByID(248,""))+"</textarea>");
    } catch (Exception e) {
       e.printStackTrace(response.getWriter());
    }


%>
<%!

    public Proj1Entity getProj1BudgetByID(int id,String requestID){
        StringBuilder sb = new StringBuilder();
        sb.append(" select p.* ");
        sb.append(" from uf_proj p  ");
        sb.append(" join workflow_requestbase wr on p.reqID = wr.requestid and wr.currentnodetype != 0  ");
        sb.append(" where p.id = ? ");

        Proj1Entity pe = null;


        RecordSet rs = new RecordSet();
        rs.executeQuery(sb.toString(),id);
        if(rs.next()){
            pe = new Proj1Entity();
            pe.setId(rs.getInt("id"));
            pe.setPID(rs.getInt("pID"));
            pe.setProjNo(rs.getString("projNo"));
            pe.setProjDeptNo(rs.getString("projDeptNo"));
            pe.setProjName(rs.getString("projName"));
            pe.setProjDesc(rs.getString("projDesc"));
            pe.setProjAmt(Util.getDoubleValue(rs.getString("projAmt"),0));
            pe.setCtrlLevel(EnumsUtil.getCtrlLevelEnum(Util.getIntValue(rs.getString("ctrlLevel"))));
            pe.setCreDate(rs.getString("creDate"));
            pe.setCreTime(rs.getString("creTime"));
            pe.setCreUser(rs.getString("creUser"));

            pe.setProjSplitFreezeAmt(0);
            pe.setProjSplitUsedAmt(0);
            pe.setProjCanSplitAmt(0);


            pe.setProjFreezeAmt(0);
            pe.setProjUsedAmt(0);
            pe.setProjBalance(0);


        }
        if(pe != null){
            //获取在途预算
            pe.setProjFreezeAmt(getProj1AmtByUseType(pe.getId(),BudTypeEnum.FREZEE,requestID));

            pe.setProjSplitFreezeAmt(getProj1AmtByUseType(pe.getId(), BudTypeEnum.SPLIT_FREZEE,requestID));
            pe.setProjSplitUsedAmt(getProj1AmtByUseType(pe.getId(), BudTypeEnum.SPLIT_USED,requestID));

            BigDecimal total = new BigDecimal(String.valueOf(pe.getProjAmt()));

            BigDecimal splitFreeze = new BigDecimal(String.valueOf(pe.getProjSplitFreezeAmt()));//拆解-冻结
            BigDecimal splitUsed = new BigDecimal(String.valueOf(pe.getProjSplitUsedAmt()));//拆解-归档

            //获取可拆解金额 可拆解金额 = 父项立项金额 -（追加、调拨、冻结金额） - 拆解-冻结 - 拆解-归档
            double proj1AmtAdjust = getProj1AmtAdjust(id, requestID);
            BigDecimal proj1Adjust = new BigDecimal(String.valueOf(proj1AmtAdjust));

            BigDecimal projCanSplitAmt  = total.subtract(splitFreeze).subtract(splitUsed).subtract(proj1Adjust);
            //如果可拆解金额小于0  那么显示为0就行
            if(projCanSplitAmt.compareTo(BigDecimal.ZERO) <= 0){
                projCanSplitAmt = BigDecimal.ZERO;
            }

            pe.setProjCanSplitAmt(projCanSplitAmt.doubleValue());


            double decrease = getProj1AmtByExpType(pe.getId(), ExpTypeEnum.DECREASE, requestID);
            double increase = getProj1AmtByExpType(pe.getId(), ExpTypeEnum.INCREASE, requestID);

            BigDecimal decreaseAmt = new BigDecimal(String.valueOf(decrease));//减少的金额  正数
            BigDecimal increaseAmt = new BigDecimal(String.valueOf(increase));//增加的金额  负数
            //获取已使用预算
            pe.setProjUsedAmt(decreaseAmt.add(increaseAmt).doubleValue());

            BigDecimal balance = total.subtract(decreaseAmt).subtract(increaseAmt);



            //获取剩余预算
            pe.setProjBalance(balance.doubleValue());
        }

        return pe;
    }
    /**
     * 根据父项ID查询 在途/已使用 预算
     * @param id
     * @param budTypeEnum
     * @param requestID
     * @param expTypeEnum
     * @return
     */
    public double getProj1AmtByType(int id, BudTypeEnum budTypeEnum, String requestID, ExpTypeEnum expTypeEnum){
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_proj_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid and wr.currentnodetype != 0   ");
        sb.append(" where p.projID = ? and p.projtype = 1 ");
        if(expTypeEnum != null){
            sb.append(" and p.expType = ").append(expTypeEnum.getType());
        }else{
            sb.append(" and p.useType = ").append(budTypeEnum.getType());
        }

        RecordSet rs = new RecordSet();
        if(StringUtil.isEmpty(requestID)){
            rs.executeQuery(sb.toString(),id);
        }else{
            sb.append(" and p.requestID != ?  ");
            rs.executeQuery(sb.toString(),id,requestID);
        }
        double projAmt = 0;
        if(rs.next()){
            projAmt = Util.getDoubleValue(rs.getString(1),0);
        }
        return projAmt;
    }


    public double getProj1AmtByExpType(int id,ExpTypeEnum expTypeEnumtype,String requestID){
        return getProj1AmtByType(id,null,requestID,expTypeEnumtype);
    }

    public double getProj1AmtByUseType(int id,BudTypeEnum budTypeEnum,String requestID){
        return getProj1AmtByType(id,budTypeEnum,requestID,null);
    }

    /**
     * （追加、调拨金额）+ 冻结的金额
     * @param id
     * @param requestID
     * @return
     */
    public double getProj1AmtAdjust(int id,String requestID){

        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_proj_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid and wr.currentnodetype != 0   ");
        sb.append(" where p.projID = ? and p.projtype = 1 and p.useType in (1,3,6) ");
        RecordSet rs = new RecordSet();
        if(StringUtil.isEmpty(requestID)){
            rs.executeQuery(sb.toString(),id);
        }else{
            sb.append(" and p.requestID != ?  ");
            rs.executeQuery(sb.toString(),id,requestID);
        }
        double projAmt = 0;
        if(rs.next()){
            projAmt = Util.getDoubleValue(rs.getString(1),0);
        }
        return projAmt;
    }
%>


