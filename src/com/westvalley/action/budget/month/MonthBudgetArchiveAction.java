package com.westvalley.action.budget.month;

import com.westvalley.dto.MonthProj;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.service.MonthProjService;
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
 * 引用流程：月度项目预算申请流程
 * 引用节点：归档节点
 * 主要功能：到归档节点后插入数据到中间表,存在则更新数据，不存则插入数据
 *
 * @author cgd 2019.07.24
 */
public class MonthBudgetArchiveAction implements Action {

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
        String requestId = request.getRequestid();//请求ID
        Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestId);
        log.d("wfMainMap", wfMainMap);
        String ndu = wfMainMap.get("ysnd");//预算年度
        String sqr = wfMainMap.get("shenqr");//申请人
        String ywlb = wfMainMap.get("ywlb");//业务类别
        log.d("年度：", ndu);
        List<MonthProj> monthProjList = new ArrayList<>();
        List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 1);//明细表数据
        Date newDate = new Date();
        String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
        String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
        for (Map map : wfMainMapList) {
            MonthProj monthProj = new MonthProj();
            monthProj.setRequestID(requestId);
            monthProj.setDetailID(map.get("id").toString());
            monthProj.setProjNo(map.get("zxbm").toString());//祖项编码
            monthProj.setProjDeptNo(map.get("fygzbmbm").toString());//部门编码
            monthProj.setProjYear(Integer.parseInt(ndu));//年度
            monthProj.setUpYear(map.get("synck").toString());//上一年参考
            monthProj.setProjDef(map.get("xmdy").toString());//项目定义
            monthProj.setProjAmt(map.get("ndysje").toString());//年度预算
            monthProj.setYwlb(ywlb);//业务类别
            monthProj.setOne(map.get("yy").toString());//一月
            monthProj.setTwo(map.get("ey").toString());//二月
            monthProj.setThree(map.get("sany").toString());//三月
            monthProj.setFour(map.get("siy").toString());//四月
            monthProj.setFive(map.get("wy").toString());//五月
            monthProj.setSix(map.get("ly").toString());//六月
            monthProj.setSeven(map.get("qy").toString());//七月
            monthProj.setEight(map.get("bay").toString());//八月
            monthProj.setNine(map.get("jy").toString());//九月
            monthProj.setTen(map.get("shiy").toString());//十月
            monthProj.setEleven(map.get("syy").toString());//十一月
            monthProj.setTwelve(map.get("sey").toString());//十二月
            monthProj.setCreDate(rq);//创建日期
            monthProj.setCreTime(sj);//创建时间
            monthProj.setCreUser(sqr);//申请人
            monthProjList.add(monthProj);
        }
        log.d("monthProjList", monthProjList);
        MonthProjService monthProjService = new MonthProjService();
        ResultDto resultDto = monthProjService.checkMonthProj(monthProjList);
        log.d("resultDto", resultDto);
        if (resultDto.isOk()) {
            return "";
        } else {
            return resultDto.getMsg();
        }
    }
}
