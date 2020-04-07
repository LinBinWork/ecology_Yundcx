package com.westvalley.action.foreign;


import com.westvalley.entity.PaymentEntity;
import com.westvalley.project.dto.Budget23Dto;
import com.westvalley.project.dto.ContractDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.service.BudgetContractService;
import com.westvalley.project.service.BudgetService;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.service.ContractService;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSetTrans;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 引用流程：营销项目对外付款申请
 * 引用节点：归档节点-节点前附加操作
 * 主要功能：1.合同预算改为已使用 2.合同状态修改为已结案
 */
public class ForeignReturnAction implements Action {

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
            String requestId = request.getRequestid();//请求ID
            Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);
            log.d("requestId == ", requestId);
            log.d("wfMainMap ==== ", wfMainMap);
            String fkzsfbhwk = wfMainMap.get("fkzsfbhwk");//付款中是否包含尾款
            String htlc = wfMainMap.get("htlc");//合同流程
            log.d("htlc ==== ", htlc);
            BudgetContractService budgetContractService = new BudgetContractService();
            boolean isYXTZ = budgetContractService.isContract(htlc);
            if (fkzsfbhwk.equals("0") && isYXTZ) {
                //修改合同状态为已结案状态
                String updateSQL = "update uf_YXHTTZ set htzt = '1' where reqid = ?";
                log.d("updateSQL ===== ", updateSQL);
                recordSetTrans.executeUpdate(updateSQL, htlc);
            }
            if (isYXTZ) {
                List<Map<String, String>> wfMainMapList1 = DevUtil.getWFDetailByReqID(requestId, 1);//明细表数据
                log.d("wfMainMapList1：", wfMainMapList1);
                List<PaymentEntity> paymentEntityList = new ArrayList<>();
                for (Map<String, String> map : wfMainMapList1) {
                    if (StringUtils.isEmpty(map.get("mxid"))) continue;
                    double jhfkje = Double.valueOf(map.get("jhfkje"));//计划付款金额
                    double sjfkje = Double.valueOf(map.get("sjfkje"));//实际付款金额
                    if (sjfkje == 0 || sjfkje == 0.00) continue;
                    PaymentEntity paymentEntity = new PaymentEntity();
                    paymentEntity.setId(map.get("mxid"));
                    paymentEntity.setReqId(htlc);
                    paymentEntity.setYfje(sjfkje);
                    if (sjfkje >= jhfkje) {
                        paymentEntity.setFkzt("2");//已付款
                    } else {
                        paymentEntity.setFkzt("1");//部分付款
                    }
                    paymentEntityList.add(paymentEntity);
                }
                log.d("paymentEntityList ==== ", paymentEntityList);
                if (paymentEntityList != null && paymentEntityList.size() > 0) {
                    ContractService contractService = new ContractService();
                    String msg = contractService.updateContractList(paymentEntityList);
                    if (!StringUtils.isEmpty(msg)) return msg;
                }
            }
            String deleteSQL = "delete from WV_T_PaymentData where payid = ?";//删除原先的记录
            recordSetTrans.executeUpdate(deleteSQL, requestId);
            log.d("deleteSQL ==", deleteSQL);
            List<Map<String, String>> wfMainMapList2 = DevUtil.getWFDetailByReqID(requestId, 2);//预付信息
            BigDecimal releaseAmt = BigDecimal.ZERO;//预付款核销总金额
            for (Map<String, String> map : wfMainMapList2) {
                log.d("map === ", map);
                if (StringUtils.isEmpty(map.get("hxje"))) continue;
                if (Double.valueOf(map.get("hxje")) > Double.valueOf(map.get("syje")))
                    return String.format("预付单号%s:核销金额 %s 大于剩余金额 %s,请重新获取！", map.get("yfdh"), map.get("hxje"), map.get("syje"));
                StringBuffer sb = new StringBuffer();
                sb.append("insert into WV_T_PaymentData(");
                sb.append("requestid,requestcode,requestname,detailid,xmid,gysid,payDate,money,bz,rectype,payid,paydetail,payvou,remark)");
                sb.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                String yfid = map.get("yfid");
                String mxid = map.get("mxid");
                log.d("mxid ===== ", mxid);
                String selectSQL = "select * from WV_T_PaymentData where requestid = '" + yfid + "' and detailid = '" + mxid + "' and rectype='payment'";
                log.d("selectSQL ===", selectSQL);
                recordSetTrans.execute(selectSQL);
                String requestcode = "";//流程编号
                String requestname = "";//流程名称
                String detailid = "";//明细Id
                String gysid = "";//供应商
                String payDate = "";//预付日期
                String bz = "";//币种
                String xmid = "";//项目id
                if (recordSetTrans.next()) {
                    requestcode = recordSetTrans.getString("requestcode");
                    requestname = recordSetTrans.getString("requestname");
                    detailid = recordSetTrans.getString("detailid");
                    gysid = recordSetTrans.getString("gysid");
                    payDate = recordSetTrans.getString("payDate");
                    xmid = recordSetTrans.getString("xmid");
                    bz = recordSetTrans.getString("bz");
                }
                BigDecimal hxje = new BigDecimal(map.get("hxje"));//冲销金额
                releaseAmt = releaseAmt.add(hxje);
                log.d("insertSQL === ", sb.toString());
                recordSetTrans.executeUpdate(sb.toString(),
                        yfid,
                        requestcode,
                        requestname,
                        detailid == null ? "" : detailid,
                        xmid,
                        gysid,
                        payDate,
                        map.get("hxje"),//冲销金额
                        bz,
                        "expense",//冲销
                        requestId,//冲销流程id
                        map.get("id"),//冲销流程明细id
                        wfMainMap.get("pzh"),//冲销凭证号
                        "付款冲销"
                );
            }
            List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 4);//项目信息分摊明细
            log.d("wfMainMapList ====== ", wfMainMapList);
            String shenqr = wfMainMap.get("shenqr");//申请人
            BudgetService budgetService = new BudgetService();
            ResultDto resultDto = null;
            if (isYXTZ) {
                //有合同付款
                List<ContractDto> contractDtoList = new ArrayList<>();
                for (Map<String, String> map : wfMainMapList) {
                    if (!StringUtil.isEmpty(map.get("ftje")) && (Float.valueOf(map.get("ftje")) == 0 || Float.valueOf(map.get("ftje")) == 0.00)) {
                        continue;
                    }
                    ProjEntity projEntity = budgetService.getProjInfo(Integer.valueOf(map.get("zxszxbh")), null);
                    log.d("projEntity ====== ", projEntity);
                    ContractDto contractDto = new ContractDto();
                    contractDto.setRequestID(requestId);
                    contractDto.setDetailID(map.get("id"));
                    contractDto.setProjID(projEntity.getId());
                    contractDto.setProjType(Integer.valueOf(projEntity.getProjType()));
                    contractDto.setUseType(BudTypeEnum.CONTRACT_USED);
                    contractDto.setCtrlLevel(projEntity.getCtrlLevel());//控制级别
                    contractDto.setUseAmt(Double.valueOf(map.get("ftje")));
                    contractDto.setCreUser(shenqr);
                    contractDtoList.add(contractDto);
                }
                resultDto = budgetContractService.contractBudget(htlc, requestId, releaseAmt.doubleValue(), contractDtoList, BudTypeEnum.CONTRACT_USED);

            } else {
                //无合同付款
                log.d("本次实付金额：", wfMainMap.get("bcsfje"));
                if (Double.valueOf(wfMainMap.get("bcsfje")) == 0) {
                    log.d("全核销的情况不走资金计划冻结");
                    recordSetTrans.commit();
                    return "";
                }
                List<Budget23Dto> budget23DtoList = new ArrayList<>();
                Date newDate = new Date();
                String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
                String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
                BigDecimal one = new BigDecimal("100");
                for (Map<String, String> map : wfMainMapList) {
                    BigDecimal ftbl = new BigDecimal(map.get("ftbl"));//分摊比率
                    ftbl = ftbl.divide(one, 4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal ftje = new BigDecimal(map.get("ftje"));//分摊金额
                    if (!StringUtil.isEmpty(map.get("ftje")) && (Float.valueOf(map.get("ftje")) == 0 || Float.valueOf(map.get("ftje")) == 0.00)) {
                        continue;
                    }
                    if (ftje.subtract(releaseAmt.multiply(ftbl)).doubleValue() < 0 && ftje.subtract(releaseAmt.multiply(ftbl)).doubleValue() > -3) {
                        continue;
                    }
                    ProjEntity projEntity = budgetService.getProjInfo(Integer.valueOf(map.get("zxszxbh")), null);
                    log.d("projEntity ====== ", releaseAmt.toString(), projEntity);
                    Budget23Dto budget23Dto = new Budget23Dto();
                    budget23Dto.setRequestID(requestId);
                    budget23Dto.setPID(projEntity.getPID());
                    budget23Dto.setProjID(projEntity.getId());
                    budget23Dto.setProjNo(projEntity.getProjNo());
                    budget23Dto.setProjDeptNo(projEntity.getProjDeptNo());
                    budget23Dto.setProjtype(EnumsUtil.getProjtypeEnum(projEntity.getProjType()));
                    budget23Dto.setUseType(BudTypeEnum.USED);
                    budget23Dto.setCtrlLevel(projEntity.getCtrlLevel());
                    budget23Dto.setUseAmt(ftje.subtract(releaseAmt.multiply(ftbl)).doubleValue());// 冻结金额 = 分摊金额 - 核销总金额 * 分摊比率
                    budget23Dto.setModDate(rq);
                    budget23Dto.setModTime(sj);
                    budget23Dto.setCreUser(shenqr);
                    budget23DtoList.add(budget23Dto);
                }
                resultDto = budgetService.executeBudget23(budget23DtoList, BudTypeEnum.USED);
            }
            recordSetTrans.commit();
            return resultDto.isOk() ? null : resultDto.getMsg();
        } catch (Exception e) {
            recordSetTrans.rollback();
            e.printStackTrace();
            log.e("合同预算扣减发生错误：", e);
            return "合同预算扣减发生错误:" + e.getMessage();
        }
    }

}


