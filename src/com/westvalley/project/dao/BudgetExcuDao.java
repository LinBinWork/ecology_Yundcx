package com.westvalley.project.dao;

import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.po.BudgetExcuPo;
import com.westvalley.util.LogUtil;
import weaver.conn.RecordSetTrans;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.List;

/**
 * 预算结算
 */
public class BudgetExcuDao {

    private LogUtil log = LogUtil.getLogger(getClass());

    /**
     * 执行预算
     * @param excuPoList 执行预算数据
     * @return 执行结果
     */
    public ResultDto excuBudget(List<BudgetExcuPo> excuPoList,boolean isDelete) {

        log.d("开始执行预算，是否删除数据",isDelete);
        ResultDto resultDto;
        if(excuPoList == null || excuPoList.size() == 0){
            resultDto = ResultDto.ok("执行预算数据为空,数据不执行");
        }else{
            log.d("本次执行预算条数",excuPoList.size());
            log.d("本次执行预算实际数据",excuPoList);

            RecordSetTrans trans = new RecordSetTrans();
            trans.setAutoCommit(false);
            try {
                if(isDelete) {
                    String del1ExcuSQL = "delete from wv_proj_excuDetail where requestID = ? and projID = ? ";
                    String del2ExcuSQL = "delete from wv_proj_excuDetail where requestID = ? ";
                    for (BudgetExcuPo po : excuPoList) {
                        //如果自动创建，不能只根据requestid删除
                        if ("0".equals(po.getAutoCre())) {
                            trans.executeUpdate(del1ExcuSQL, po.getRequestID(), po.getProjID());
                        } else {
                            trans.executeUpdate(del2ExcuSQL, po.getRequestID());
                        }
                    }
                }
                String addExcuSQL = "insert into wv_proj_excuDetail(requestID,detailID,projID,projNo,projDeptNo,projType,useType,expType,splitStatu,useAmt,creDate,creTime,creUser,remark) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                for (BudgetExcuPo po : excuPoList) {

                    String useAmt = String.valueOf(po.getUseAmt());
                    if(useAmt.contains("E")){//如果是科学计数
                        useAmt = new BigDecimal(po.getUseAmt()).toString();
                    }
                    trans.executeUpdate(addExcuSQL,
                            po.getRequestID(),
                            po.getDetailID(),
                            po.getProjID(),
                            Util.null2String(po.getProjNo()),
                            Util.null2String(po.getProjDeptNo()),
                            po.getProjtype().getType(),
                            po.getUseType().getType(),
                            po.getExpType().getType(),
                            po.getSplitStatu(),
                            useAmt,
                            po.getCreDate(),
                            po.getCreTime(),
                            po.getCreUser(),
                            Util.null2String(po.getRemark())
                    );
                }
                trans.commit();
                resultDto = ResultDto.ok("本次执行预算成功",null);
            }catch (Exception e) {
                trans.rollback();

                String msg = "本次执行预算出错:";
                log.e(msg,e);
                resultDto =  ResultDto.error(msg+e.getMessage());
            }
        }
        log.d("结束执行预算",resultDto);
        return resultDto;
    }
}
