package com.westvalley.project.dao;

import com.westvalley.project.dto.ContractDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.ContractBudgetDetailEntity;
import com.westvalley.project.entity.ContractBudgetEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.general.TimeUtil;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 合同预算相关数据获取/执行
 */
public class ContractDao {

    private LogUtil log = LogUtil.getLogger(getClass());

    /**
     * 使用合同预算
     * @param contractRequestID
     * @param currentRequestID
     * @param contractDtoList
     * @return
     */
    public ResultDto contractBudget(String contractRequestID,String currentRequestID,List<ContractDto> contractDtoList) {

        if(contractDtoList == null || contractDtoList.size() == 0){
            return ResultDto.ok("使用合同预算数据为空,不执行");
        }
        log.d("appendDtoList",contractRequestID,contractDtoList);

        String createDate = TimeUtil.getCurrentDateString();
        String createTime = TimeUtil.getOnlyCurrentTimeString();

        RecordSetTrans trans = new RecordSetTrans();
        trans.setAutoCommit(false);

        //添加合同使用记录
        String addContractExcuSQL = "insert into wv_contract_excuDetail(contractRequestID,requestID,detailID,projID,useType,useAmt,creDate,creTime,creUser,remark) values(?,?,?,?,?,?,?,?,?,?)";
        String delContractExcuSQL = "delete from wv_contract_excuDetail where requestID = ?";
        ResultDto resultDto;
        try {

            //清空合同使用记录
            trans.executeUpdate(delContractExcuSQL,currentRequestID);
            for (ContractDto dto : contractDtoList) {
                BudTypeEnum useType = dto.getUseType();

                String useAmt = String.valueOf(dto.getUseAmt());
                if(useAmt.contains("E")){//如果是科学计数
                    useAmt = new BigDecimal(dto.getUseAmt()).toString();
                }

                //添加合同使用记录
                trans.executeUpdate(addContractExcuSQL,
                        contractRequestID,
                        dto.getRequestID(),
                        dto.getDetailID(),
                        dto.getProjID(),
                        useType.getType(),
                        useAmt,
                        createDate,
                        createTime,
                        dto.getCreUser(),
                        useType.getName()
                );
            }
            trans.commit();
            resultDto = ResultDto.ok("使用合同预算成功");
        } catch (Exception e) {
            trans.rollback();
            String msg = "使用合同预算出错:";
            log.e(msg,e);
            resultDto =  ResultDto.error(msg+e.getMessage());
        }
        return resultDto;
    }


    /**
     * 获取合同相关信息
     *
     * @param contractRequestID 合同流程
     * @param currentRequestID  当前流程
     * @return
     */
    public ContractBudgetEntity getContractBudgetEntity(String contractRequestID, String currentRequestID){
        ContractBudgetEntity en = null;

        RecordSet rs = new RecordSet();
        rs.executeQuery("select requestid,max(htbh) as htbh ,max(htmc) as htmc ,sum(useAmt) as amt from wv_v_contractinfo where requestid = ? group by  requestid "
                        ,contractRequestID);
        if(rs.next()){
            en = new ContractBudgetEntity();
            en.setContractRequestID(rs.getString("requestid"));
            en.setContractNo(rs.getString("htbh"));
            en.setContractName(rs.getString("htmc"));
            en.setContractAmt(Util.getDoubleValue(rs.getString("amt"),0));
        }

        if(en != null){
            String requestid = en.getContractRequestID();
            double freezeAmt = getContractAmtByType(requestid, BudTypeEnum.CONTRACT_FREZEE.getType(),currentRequestID);
            double usedAmt = getContractAmtByType(requestid, BudTypeEnum.CONTRACT_USED.getType(),currentRequestID);

            BigDecimal total = new BigDecimal(String.valueOf(en.getContractAmt()));
            BigDecimal freeze = new BigDecimal(String.valueOf(freezeAmt));
            BigDecimal used = new BigDecimal(String.valueOf(usedAmt));
            BigDecimal balance = total.subtract(freeze).subtract(used);

            en.setContractFreezeAmt(freeze.doubleValue());
            en.setContractUsedAmt(used.doubleValue());
            en.setContractBalance(balance.doubleValue());

            rs.executeQuery("select projID,projType,useAmt from wv_v_contractinfo where requestid = ? ",contractRequestID);
            List<ContractBudgetDetailEntity> detailList = new ArrayList<>();
            while(rs.next()){
                ContractBudgetDetailEntity cbde = new ContractBudgetDetailEntity();
                cbde.setProjID(rs.getInt("projID"));
                cbde.setProjType(rs.getInt("projType"));
                cbde.setFreezAmt(Util.getDoubleValue(rs.getString("useAmt"),0));
                detailList.add(cbde);
            }
            en.setDetailEntityList(detailList);
        }
        return en;
    }


    /**
     * 根据合同requestID 在途/已使用 预算
     * @param requestID 合同requestID
     * @param type
     * @param currentRequestID
     * @return
     */
    public double getContractAmtByType(String requestID,int type,String currentRequestID) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_contract_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid and wr.currentnodetype != 0   ");
        sb.append(" where p.contractRequestID = ?  and p.useType = ?  ");

        RecordSet rs = new RecordSet();
        if (StringUtil.isEmpty(currentRequestID)) {
            rs.executeQuery(sb.toString(), requestID, type);
        } else {
            sb.append(" and p.requestID != ?  ");
            rs.executeQuery(sb.toString(), requestID, type, currentRequestID);
        }
        double projAmt = 0;
        if (rs.next()) {
            projAmt = Util.getDoubleValue(rs.getString(1),0);
        }
        return projAmt;
    }
}
