package com.westvalley.action.contract;

import com.westvalley.entity.ContractEntity;
import com.westvalley.entity.PaymentEntity;
import com.westvalley.entity.ProjectEntity;
import com.westvalley.service.ContractService;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 写入合同台账记录。
 */
public class ContractWriteAction implements Action {

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
        try {
            String requestId = request.getRequestid();//请求ID
            Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);
            log.d("wfMainMap:", wfMainMap);
            String htbh = wfMainMap.get("htbh");//合同编号
            String htmc = request.getRequestManager().getRequestname();//标题
            log.d("合同名称：", htmc);
            String fzbm = wfMainMap.get("shenqbm");//负责部门
            String htqdf = wfMainMap.get("gysmc");//合同签定方
            String htqsrq = wfMainMap.get("htksrq");//合同签署日期
            String jbr = wfMainMap.get("shenqr");//经办人
            String htje = wfMainMap.get("htje");//合同金额
            String bz = wfMainMap.get("bz");//币种
            String htlx = wfMainMap.get("htlxmc");//合同类型
            String sfxhtja = wfMainMap.get("sfxhtja");//是否需合同结案
            ContractEntity contractEntity = new ContractEntity();
            contractEntity.setHtbh(htbh);//合同编号
            contractEntity.setHtmc(htmc);//合同名称
            contractEntity.setHtqdf(htqdf);//合同签定方
            contractEntity.setFzbm(fzbm);//负责部门
            contractEntity.setHtqsrq(htqsrq);//合同签署日期
            contractEntity.setJbr(jbr);//经办人
            contractEntity.setHtje(htje.equals("") ? "0.00" : htje);
            if (htlx.equals("1")) {
                //无发票置类型直接关闭合同
                contractEntity.setHtzt("0");//合同状态  关闭状态
            } else {
                contractEntity.setHtzt("2");//合同状态  未结案状态
            }
            contractEntity.setReqId(requestId);
            contractEntity.setHtlx(htlx);//合同类型
            contractEntity.setSfxhtja(sfxhtja);//是否需合同结案

            Date newDate = new Date();
            String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
            String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
            contractEntity.setCreDate(rq);
            contractEntity.setCreTime(sj);
            contractEntity.setCreUser(jbr);
            List<PaymentEntity> paymentEntities = new ArrayList<>();
            List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 1);//明细表数据
            for (Map map : wfMainMapList) {
                PaymentEntity paymentEntity = new PaymentEntity();
                paymentEntity.setBz(bz);//币种
                paymentEntity.setFkje(map.get("fkje").toString());//付款金额
                paymentEntity.setSl(map.get("sl1").toString());//税率
                paymentEntity.setFkzt("0");//付款状态
                paymentEntity.setFktj(map.get("fktj").toString());//付款条件
                paymentEntity.setFkqs(map.get("fkqs1").toString());//付款期数
                paymentEntity.setReqId(requestId);
                paymentEntity.setYfje(0.00);//已付金额
                paymentEntity.setSyje(Double.valueOf(map.get("fkje").toString()));//剩余金额
                paymentEntities.add(paymentEntity);
            }
            List<Map<String, String>> wfMainMapList2 = DevUtil.getWFDetailByReqID(requestId, 2);//明细表数据
            List<ProjectEntity> paymentEntitys = new ArrayList<>();
            for (Map<String, String> map : wfMainMapList2) {
                ProjectEntity paymentEntity = new ProjectEntity();
                paymentEntity.setXmbh(map.get("xmbh"));//项目编号
                paymentEntity.setXmmc(map.get("xmmc"));//项目名称
                paymentEntity.setFtje(StringUtils.isEmpty(map.get("ftje")) ? 0 : Double.valueOf(map.get("ftje")));//分摊金额
                paymentEntity.setFtbl(StringUtils.isEmpty(map.get("ftbl")) ? 0 : Double.valueOf(map.get("ftbl")));//分摊比率
                paymentEntity.setXmjl(map.get("xmjl"));//项目经理
                paymentEntity.setSjxmli(map.get("sjxmjl"));//上级项目经理
                paymentEntity.setBz(map.get("bz"));//备注
                paymentEntitys.add(paymentEntity);
            }
            contractEntity.setFkjh(paymentEntities);//付款计划
            contractEntity.setXmxx(paymentEntitys);//项目信息
            ContractService contractService = new ContractService();
            log.d("contractEntity：", contractEntity);
            return contractService.insertContract(contractEntity);
        } catch (Exception e) {
            e.printStackTrace();
            log.e("写入合同台账记录发生错误", e);
            return "写入合同台账记录发生错误。" + e.getMessage();
        }
    }
}
