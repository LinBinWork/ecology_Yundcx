package com.westvalley.project.dao;

import com.sap.tc.logging.interfaces.IBaseLog;
import com.westvalley.project.dto.Proj2Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj2Entity;
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
 * 子项相关数据库操作
 */
public class Proj2Dao {
    public Proj2Dao(){

    }

    /**
     * 创建子项
     * @param proj2DtoList
     * @return
     */
    public ResultDto createProj2Bud(List<Proj2Dto> proj2DtoList) {
        ProjDao projDao = new ProjDao();
        return projDao.createProj2Bud(proj2DtoList);
    }


    /**
     * 获取子项数据
     * @param id
     * @return
     */
    public Proj2Entity getProj2ByID(int id){
        return getProj2BudgetByID(id,null);
    }

    /**
     * 获取当前子项预算，排除流程
     * @param id
     * @param requestID
     * @return
     */
    public Proj2Entity getProj2BudgetByID(int id, String requestID){
        StringBuilder sb = new StringBuilder();
        sb.append(" select p.* ");
        sb.append(" from uf_proj p  ");
        sb.append(" join workflow_requestbase wr on p.reqID = wr.requestid and wr.currentnodetype != 0  ");
        sb.append(" where p.id = ? ");

        Proj2Entity pe = null;

        RecordSet rs = new RecordSet();
        rs.executeQuery(sb.toString(),id);
        if(rs.next()){
            pe = new Proj2Entity();
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

            pe.setDeliveryCenter(Util.getIntValue(rs.getString("deliveryCenter"),1));

            pe.setProjFreezeAmt(0);
            pe.setProjUsedAmt(0);
            pe.setProjBalance(0);
        }
        if(pe != null){
            double freezAmt = getFreezAmt(pe.getPID(), requestID);
            double releaseAmt = getReleaseAmt(pe.getPID(), requestID);
            double addAmt = getAddAmt(pe.getPID(), requestID);
            double usedAmt = getUsedAmt(pe.getPID(), requestID);
            double balance = pe.getProjAmt() + addAmt - usedAmt - releaseAmt;
            pe.setProjBalance(balance);
            pe.setProjUsedAmt(usedAmt);
            pe.setProjFreezeAmt(freezAmt);
            pe.setProjAddAmt(addAmt);

        }

        return pe;
    }

    /**
     * 根据子项IDs查询 在途/已使用 预算
     * @param id
     * @param type
     * @return
     */
    public double getProj2AmtByType(int id,ExpTypeEnum type,String requestID){
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_proj_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid  ");
        sb.append(" where p.projID = ?  and p.expType = ? and p.projtype = 2 and remark not in ('预算冻结-合同申请','预算冻结-合同归档待付款') and  (wr.currentnodetype != 0 or p.useType = 9) ");
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
     *  根据子项ID获取冻结金额
     * @param id
     * @param requestid
     * @return
     */
    public double getFreezAmt(int id,String requestid){
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_proj_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid  ");
        sb.append(" where p.projID = ?  and p.expType = 1 and p.usetype  = 1 and p.projtype = 2  and  (wr.currentnodetype != 0 or p.useType = 9) ");
        RecordSet rs = new RecordSet();
        if(StringUtil.isEmpty(requestid)){
            rs.executeQuery(sb.toString(),id);
        }else{
            sb.append(" and (p.requestID != ? or p.useType = 9) ");
            rs.executeQuery(sb.toString(),id,requestid);
        }
        double projAmt = 0;
        if(rs.next()){
            projAmt = Util.getDoubleValue(rs.getString(1),0);
        }
        return projAmt;
    }

    /**
     *  根据子项ID获取增加金额
     * @param id
     * @param requestid
     * @return
     */
    public double getAddAmt(int id,String requestid){
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_proj_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid  ");
        sb.append(" where p.projID = ?  and p.usetype  in (3,6) and p.projtype = 2  and  (wr.currentnodetype != 0 or p.useType = 9) ");
        RecordSet rs = new RecordSet();
        if(StringUtil.isEmpty(requestid)){
            rs.executeQuery(sb.toString(),id);
        }else{
            sb.append(" and (p.requestID != ? or p.useType = 9) ");
            rs.executeQuery(sb.toString(),id,requestid);
        }
        double projAmt = 0;
        if(rs.next()){
            projAmt = Util.getDoubleValue(rs.getString(1),0);
        }
        return -projAmt;
    }
    /**
     *  根据子项ID获取已使用金额
     * @param id
     * @param requestid
     * @return
     */
    public double getUsedAmt(int id,String requestid){
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_proj_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid  ");
        sb.append(" where p.projID = ?  and p.usetype  in (0,8) and p.exptype = 1 and p.projtype = 2  and  (wr.currentnodetype != 0 or p.useType = 9) ");
        RecordSet rs = new RecordSet();
        if(StringUtil.isEmpty(requestid)){
            rs.executeQuery(sb.toString(),id);
        }else{
            sb.append(" and (p.requestID != ? or p.useType = 9) ");
            rs.executeQuery(sb.toString(),id,requestid);
        }
        double projAmt = 0;
        if(rs.next()){
            projAmt = Util.getDoubleValue(rs.getString(1),0);
        }
        return -projAmt;
    }
    /**
     *  根据子项ID获取释放金额
     * @param id
     * @param requestid
     * @return
     */
    public double getReleaseAmt(int id,String requestid){
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_proj_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid  ");
        sb.append(" where p.projID = ?  and p.usetype = 2 and p.exptype = 1 and p.projtype = 2  and  (wr.currentnodetype != 0 or p.useType = 9) ");
        RecordSet rs = new RecordSet();
        if(StringUtil.isEmpty(requestid)){
            rs.executeQuery(sb.toString(),id);
        }else{
            sb.append(" and (p.requestID != ? or p.useType = 9) ");
            rs.executeQuery(sb.toString(),id,requestid);
        }
        double projAmt = 0;
        if(rs.next()){
            projAmt = Util.getDoubleValue(rs.getString(1),0);
        }
        return projAmt;
    }

    /**
     * 获取子项编码
     * @param requestID
     * @param detailID
     * @return
     */
    public String getProj2No(String requestID,String detailID){
        return DevUtil.executeQuery("select p.projNo from uf_proj p where p.reqID = ? and p.detailID = ? ",requestID,detailID);
    }

}
