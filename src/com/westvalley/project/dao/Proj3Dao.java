package com.westvalley.project.dao;

import com.westvalley.project.dto.Proj3Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj3Entity;
import com.westvalley.project.enums.ExpTypeEnum;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.List;

/**
 * 孙子项相关数据库操作
 */
public class Proj3Dao {
    public Proj3Dao(){
    }
    public ResultDto createProj3Bud(List<Proj3Dto> proj3DtoList) {
        ProjDao projDao = new ProjDao();
        return projDao.createProj3Bud(proj3DtoList);
    }

    /**
     * 获取子项数据
     * @param id
     * @return
     */
    public Proj3Entity getProj3ByID(int id){
       return getProj3BudgetByID(id,null);
    }

    /**
     * 获取当前孙子项预算，排除流程
     * @param id
     * @param requestID
     * @return
     */
    public Proj3Entity getProj3BudgetByID(int id, String requestID){
        StringBuilder sb = new StringBuilder();
        sb.append(" select p.* ");
        sb.append(" from uf_proj p  ");
        sb.append(" join workflow_requestbase wr on p.reqID = wr.requestid and wr.currentnodetype != 0  ");
        sb.append(" where p.id = ? ");

        Proj3Entity pe = null;

        RecordSet rs = new RecordSet();
        rs.executeQuery(sb.toString(),id);
        if(rs.next()){
            pe = new Proj3Entity();
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

            pe.setProjFreezeAmt(0);
            pe.setProjUsedAmt(0);
            pe.setProjBalance(0);
        }
        if(pe != null){
            //获取在途预算
//            pe.setProjFreezeAmt(getProj3FreezeAmt(pe.getId(),requestID));
//            //获取已使用预算
//            pe.setProjUsedAmt(getProj3UsedAmt(pe.getId(),requestID));

            double decrease = getProj3AmtByType(pe.getId(), ExpTypeEnum.DECREASE, requestID);
            double increase = getProj3AmtByType(pe.getId(), ExpTypeEnum.INCREASE, requestID);


            BigDecimal total = new BigDecimal(String.valueOf(pe.getProjAmt()));
            BigDecimal decreaseAmt = new BigDecimal(String.valueOf(decrease));//减少的金额  正数
            BigDecimal increaseAmt = new BigDecimal(String.valueOf(increase));//增加的金额  负数

            pe.setProjUsedAmt(decreaseAmt.add(increaseAmt).doubleValue());
            BigDecimal balance = total.subtract(decreaseAmt).subtract(increaseAmt);
            //获取剩余预算
            pe.setProjBalance(balance.doubleValue());
        }

        return pe;
    }


    public double getProj3AmtByType(int id,ExpTypeEnum type,String requestID){
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_proj_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid ");
        sb.append(" where p.projID = ?  and p.expType = ?  and p.projtype = 3 and (wr.currentnodetype != 0 or p.useType = 9)");
        RecordSet rs = new RecordSet();
        if(StringUtil.isEmpty(requestID)){
            rs.executeQuery(sb.toString(),id,type.getType());
        }else{
            sb.append(" and (p.requestID != ? or p.useType = 9) ");
            rs.executeQuery(sb.toString(),id,type.getType(),requestID);
        }
        double projAmt = 0;
        if(rs.next()){
            projAmt = Util.getDoubleValue(rs.getString(1),0);
        }
        return projAmt;
    }

    /**
     * 获取孙子项编码
     * @param requestID
     * @param detailID
     * @return
     */
    public String getProj3No(String requestID,String detailID){
        return DevUtil.executeQuery("select p.projNo from uf_proj p where p.reqID = ? and p.detailID = ? ",requestID,detailID);
    }
}
