package com.westvalley.action.other;


import com.westvalley.project.dto.Budget23Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.service.BudgetService;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSetTrans;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 引用流程：其它费用报销流程
 * 引用节点：归档节点-节点前附加操作
 * 主要功能：1.核销借款 2.预算改为已使用
 */
public class OtherReturnAction implements Action {

    private LogUtil log = LogUtil.getLogger(getClass());


    @Override
    public String execute(RequestInfo requestInfo) {
        String msg = doAct(requestInfo);
        if (StringUtils.isEmpty(msg)) {
            return SUCCESS;
        } else {
            BaseUtils.setRequestErrorMessage(requestInfo, msg);
            return FAILURE_AND_CONTINUE;
        }
    }

    private String doAct(RequestInfo request) {
        RecordSetTrans recordSetTrans = new RecordSetTrans();
        recordSetTrans.setAutoCommit(false);
        try {
            Map<String, Object> parameter = new HashMap<>();
            String requestId = request.getRequestid();//请求ID
            Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);
            log.d("requestId == ", requestId);
            String deleteSQL = "delete from WV_T_BorrowData where payid = ?";//删除原先的记录
            recordSetTrans.executeUpdate(deleteSQL, requestId);
            log.d("deleteSQL ==", deleteSQL);
            List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 4);//借款历史记录
            Date newDate = new Date();
            String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
            String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
            List<Budget23Dto> budget23DtoList = new ArrayList<>();
            BudgetService budgetService = new BudgetService();
            for (Map<String, String> map : wfMainMapList) {
                log.d("map === ", map);
                if (StringUtils.isEmpty(map.get("hkcxje"))) continue;
                if (Double.valueOf(map.get("hkcxje")) == 0) continue;
                StringBuffer sb = new StringBuffer();
                sb.append("insert into WV_T_BorrowData(");
                sb.append("requestid,requestcode,requestname,detailid,projid,userid,payee,payBank,payAccount,payment,brovou,brodate,repaydate,money,rectype,payid,paydetail,payvou,remark)");
                sb.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                String jkid = map.get("jkid");
                String selectSQL = "select * from WV_T_BorrowData where requestid = '" + jkid + "' and rectype='borrow'";
                log.d("selectSQL ===", selectSQL);
                recordSetTrans.execute(selectSQL);
                String requestcode = "";//借款流程编号
                String requestname = "";//借款流程名称
                String detailid = "";//借款明细Id
                String projid = "";//项目id
                String userid = "";//借款人
                String payee = "";//收款人
                String payBank = "";//收款人开户银行
                String payAccount = "";//收款人银行账号
                String payment = "";//方式
                String brovou = "";//借款凭证号
                String brodate = "";//借款日期
                String repaydate = "";//预计还款日期
                if (recordSetTrans.next()) {
                    requestcode = recordSetTrans.getString("requestcode");
                    requestname = recordSetTrans.getString("requestname");
                    detailid = recordSetTrans.getString("detailid");
                    projid = recordSetTrans.getString("projid");
                    userid = recordSetTrans.getString("userid");
                    payee = recordSetTrans.getString("payee");
                    payBank = recordSetTrans.getString("payBank");
                    payAccount = recordSetTrans.getString("payAccount");
                    payment = recordSetTrans.getString("payment");
                    brovou = recordSetTrans.getString("brovou");
                    brodate = recordSetTrans.getString("brodate");
                    repaydate = recordSetTrans.getString("repaydate");
                }
                log.d("insertSQL === ", sb.toString());
                String hkcxje = map.get("hkcxje");
                recordSetTrans.executeUpdate(sb.toString(),
                        jkid,
                        requestcode,
                        requestname,
                        detailid == null ? "" : detailid,
                        projid,
                        userid,
                        payee,
                        payBank,
                        payAccount,
                        payment,
                        brovou == null ? "" : brovou,
                        brodate,
                        repaydate,
                        hkcxje,//冲销金额
                        "expense",//冲销
                        requestId,//冲销流程id
                        map.get("id"),//冲销流程明细id
                        wfMainMap.get("pzh"),//冲销凭证号
                        "其他费用冲销"
                );

                //更新借款关联项目的预算
                ProjEntity projEntity = budgetService.getProjInfo(Integer.valueOf(projid), null);
                log.d("projEntity :", projEntity);
                Budget23Dto budget23Dto = new Budget23Dto();
                budget23Dto.setRequestID(requestId);
                budget23Dto.setPID(projEntity.getPID());
                budget23Dto.setProjID(projEntity.getId());
                budget23Dto.setProjNo(projEntity.getProjNo());
                budget23Dto.setProjDeptNo(projEntity.getProjDeptNo());
                budget23Dto.setProjtype(EnumsUtil.getProjtypeEnum(projEntity.getProjType()));
                budget23Dto.setUseType(BudTypeEnum.BORROW_REVERSAL);
                budget23Dto.setCtrlLevel(projEntity.getCtrlLevel());
                budget23Dto.setUseAmt(Double.valueOf(hkcxje));//报销金额
                budget23Dto.setModDate(rq);
                budget23Dto.setModTime(sj);
                budget23Dto.setCreUser(wfMainMap.get("shenqr"));
                budget23DtoList.add(budget23Dto);
            }
            if (budget23DtoList.size() > 0) {
                ResultDto resultDto = budgetService.executeBudget23(budget23DtoList, BudTypeEnum.BORROW_REVERSAL, true);
                if (!resultDto.isOk()) return resultDto.getMsg();
            }
            List<Budget23Dto> budget23DtoList2 = new ArrayList<>();
            List<Map<String, String>> wfMainMapList1 = DevUtil.getWFDetailByReqID(requestId, 6);//分摊金额明细
            for (Map<String, String> map : wfMainMapList1) {
                ProjEntity projEntity = budgetService.getProjInfo(Integer.valueOf(map.get("zxszxbh")), null);
                log.d("projEntity :", projEntity);
                Budget23Dto budget23Dto = new Budget23Dto();
                budget23Dto.setRequestID(requestId);
                budget23Dto.setPID(projEntity.getPID());
                budget23Dto.setProjID(projEntity.getId());
                budget23Dto.setProjNo(projEntity.getProjNo());
                budget23Dto.setProjDeptNo(projEntity.getProjDeptNo());
                budget23Dto.setProjtype(EnumsUtil.getProjtypeEnum(projEntity.getProjType()));
                budget23Dto.setUseType(BudTypeEnum.USED);
                budget23Dto.setCtrlLevel(projEntity.getCtrlLevel());
                budget23Dto.setUseAmt(Double.valueOf(map.get("ftje")));//分摊金额
                budget23Dto.setModDate(rq);
                budget23Dto.setModTime(sj);
                budget23Dto.setCreUser(wfMainMap.get("shenqr"));
                budget23DtoList2.add(budget23Dto);
            }
            ResultDto resultDto2 = budgetService.executeBudget23(budget23DtoList2, BudTypeEnum.USED, budget23DtoList.size() > 0 ? false : true);
            if (!resultDto2.isOk()) return resultDto2.getMsg();
            recordSetTrans.commit();
            return null;
        } catch (Exception e) {
            recordSetTrans.rollback();
            e.printStackTrace();
            log.e("核销借款发生错误：", e);
            return "核销借款发生错误:" + e.getMessage();
        }
    }
}


