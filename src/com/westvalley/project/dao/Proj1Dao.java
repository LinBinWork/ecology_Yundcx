package com.westvalley.project.dao;

import com.westvalley.project.dto.ContractDto;
import com.westvalley.project.dto.Proj1Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj1Entity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.ExpTypeEnum;
import com.westvalley.project.po.Proj1PrecentPo;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.general.TimeUtil;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.List;

/**
 * 父项相关数据库操作
 */
public class Proj1Dao {
    public Proj1Dao(){
    }
    /**
     * 创建父项
     * @param proj1DtoList
     * @return
     */
    public ResultDto createProj1Bud(List<Proj1Dto> proj1DtoList) {
        ProjDao projDao = new ProjDao();
        return projDao.createProj1Bud(proj1DtoList);
    }

    /**
     * 获取父项相关数据
     * @param id
     * @return
     */
    public Proj1Entity getProj1ByID(int id){
        return getProj1BudgetByID(id,null);
    }
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
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid ");
        sb.append(" where p.projID = ? and p.projtype = 1 and (wr.currentnodetype != 0 or p.useType = 9) ");
        if(expTypeEnum != null){
            sb.append(" and p.expType = ").append(expTypeEnum.getType());
        }else{
            sb.append(" and p.useType = ").append(budTypeEnum.getType());
        }

        RecordSet rs = new RecordSet();
        if(StringUtil.isEmpty(requestID)){
            rs.executeQuery(sb.toString(),id);
        }else{
            sb.append(" and (p.requestID != ? or p.useType = 9) ");
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


    /**
     * 更新父项分摊数据
     * @param proj1PrecentPoList
     * @return
     */
    public ResultDto updatePrecent(List<Proj1PrecentPo> proj1PrecentPoList){
        LogUtil log = LogUtil.getLogger(getClass());
        log.d("proj1PrecentPoList",proj1PrecentPoList);
        if(proj1PrecentPoList == null || proj1PrecentPoList.size() == 0){
            return ResultDto.error("更新父项分摊数据不能为空");
        }
        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();

        RecordSetTrans trans = new RecordSetTrans();
        trans.setAutoCommit(false);

        String addExcuSQL = "insert into wv_proj1_precent(proj1RequestID,creRequestID,proj1ID,proj0ID,balance,person,ccy,rate,useAmt,creDate,creTime,creUser,remark) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String delExcuSQL = "delete from wv_proj1_precent where creRequestID = ?";
        ResultDto resultDto;
        try {

            for (Proj1PrecentPo dto : proj1PrecentPoList) {
                trans.executeUpdate(delExcuSQL,dto.getCreRequestID());
            }
            for (Proj1PrecentPo dto : proj1PrecentPoList) {
                trans.executeUpdate(addExcuSQL,
                        dto.getProj1RequestID(),
                        dto.getCreRequestID(),
                        dto.getProj1ID(),
                        dto.getProj0ID(),
                        dto.getBalance()+"",
                        dto.getPerson(),
                        dto.getCcy(),
                        dto.getRate()+"",
                        dto.getUseAmt()+"",
                        createDate,
                        createTime,
                        dto.getCreUser(),
                        dto.getRemark()
                );
            }
            trans.commit();
            resultDto = ResultDto.ok("追加父项分摊比例成功");
        } catch (Exception e) {
            trans.rollback();
            String msg = "追加父项分摊出错:";
            log.e(msg,e);
            resultDto =  ResultDto.error(msg+e.getMessage());
        }
        log.d("updatePrecent",resultDto);
        return resultDto;

    }

    /**
     * 获取父项编码
     * @param requestID
     * @return
     */
    public String getProj1No(String requestID){
        return DevUtil.executeQuery("select p.projNo from uf_proj p where p.reqID = ? ",requestID);
    }

}
