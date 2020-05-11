package com.westvalley.action.foreign;


import com.westvalley.project.dto.Budget23Dto;
import com.westvalley.project.dto.ContractDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.service.BudgetContractService;
import com.westvalley.project.service.BudgetService;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 引用流程：营销项目对外付款申请
 * 引用节点：申请节点-节点后附加操作
 * 主要功能：1.合同预算扣减
 */
public class ForeignApplyAction implements Action {

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
            String htlc = wfMainMap.get("htlc");//合同流程
            BudgetContractService budgetContractService = new BudgetContractService();
            if (budgetContractService.isContract(htlc)) {
                String sql = "select id from uf_YXHTTZ where htzt = '0' and reqid = ? ";
                RecordSet rs = new RecordSet();
                rs.executeQuery(sql, htlc);
                if (rs.next()) {
                    return "该合同已经关闭,无法提交申请。";
                }
            }
            String deleteSQL = "delete from WV_T_PaymentData where payid = ?";//删除原先的记录
            recordSetTrans.executeUpdate(deleteSQL, requestId);
            boolean isHx = false;
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
                log.d("insertSQL === ", sb.toString());
                BigDecimal hxje = new BigDecimal(map.get("hxje"));//冲销金额
                releaseAmt = releaseAmt.add(hxje);
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
                        "",//冲销凭证号
                        "付款冲销"
                );
                isHx = true;
            }
            List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 4);//项目信息分摊明细
            log.d("wfMainMapList ====== ", wfMainMapList);
            String shenqr = wfMainMap.get("shenqr");//申请人
            BudgetService budgetService = new BudgetService();
            ResultDto resultDto = ResultDto.ok();
            log.d("resultDto == ", resultDto);
            log.d("htlc == ", htlc);
            String sfhtfk = wfMainMap.get("sfhtfk");//是否合同付款
//            if (budgetContractService.isContract(htlc)) {
            if ("0".equals(sfhtfk) || "2".equals(sfhtfk)) {
                //有合同付款
                String bcsfje = wfMainMap.get("bcsfje");//本次实付金额
                log.d("本次实付金额：", bcsfje);
                //存在核销的情况，需要全部核销才可以提单，不是全部核销要求申请人提多一条流程
                if (isHx && !"".equals(bcsfje) && Double.valueOf(bcsfje) > 0) return "存在核销的情况,需要全部核销,部分付款的情况请分开提单。";
                //本次实付金额大于0才走合同预算扣减
                if (!"".equals(bcsfje) && Double.valueOf(bcsfje) > 0) {
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
                        contractDto.setUseType(BudTypeEnum.CONTRACT_FREZEE);//冻结
                        contractDto.setCtrlLevel(projEntity.getCtrlLevel());//控制级别
                        contractDto.setUseAmt(Double.valueOf(map.get("ftje")));
                        contractDto.setCreUser(shenqr);
                        contractDtoList.add(contractDto);
                    }
                    resultDto = budgetContractService.contractBudget(htlc, requestId, releaseAmt.doubleValue(), contractDtoList, BudTypeEnum.CONTRACT_FREZEE);
                }
            } else {
                log.d("本次实付金额：", wfMainMap.get("bcsfje"));
                if (Double.valueOf(wfMainMap.get("bcsfje")) == 0) {
                    log.d("全核销的情况不走资金计划冻结");
                    recordSetTrans.commit();
                    return "";
                }
                //无合同付款
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
                    log.d("projEntity ====== ", releaseAmt, projEntity);
                    Budget23Dto budget23Dto = new Budget23Dto();
                    budget23Dto.setRequestID(requestId);
                    budget23Dto.setPID(projEntity.getPID());
                    budget23Dto.setProjID(projEntity.getId());
                    budget23Dto.setProjNo(projEntity.getProjNo());
                    budget23Dto.setProjDeptNo(projEntity.getProjDeptNo());
                    budget23Dto.setProjtype(EnumsUtil.getProjtypeEnum(projEntity.getProjType()));
                    budget23Dto.setUseType(BudTypeEnum.FREZEE);
                    budget23Dto.setCtrlLevel(projEntity.getCtrlLevel());
                    budget23Dto.setUseAmt(ftje.subtract(releaseAmt.multiply(ftbl)).doubleValue());// 冻结金额 = 分摊金额 - 核销总金额 * 分摊比率
                    budget23Dto.setCreDate(rq);
                    budget23Dto.setCreTime(sj);
                    budget23Dto.setCreUser(shenqr);
                    budget23DtoList.add(budget23Dto);
                }
                resultDto = budgetService.executeBudget23(budget23DtoList, BudTypeEnum.FREZEE);
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


