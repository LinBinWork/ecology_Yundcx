package com.westvalley.action.budget.year;

import com.westvalley.project.dto.Proj0Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.service.Proj0Service;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.ProjectUtils;
import org.apache.commons.lang3.StringUtils;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 引用流程：年度项目预算申请流程
 * 引用节点：归档节点
 * 主要功能：到归档节点后插入数据到中间表
 *
 * @author cgd 2019.07.24
 */
public class YearBudgetArchiveAction implements Action {

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
        String requestid = request.getRequestid();//请求ID
        Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestid);
        log.d("wfMainMap", wfMainMap);
        String ndu = wfMainMap.get("ysnd");//预算年度
        String sqr = wfMainMap.get("shenqr");//申请人
        String ywlb = wfMainMap.get("ywlb");//业务类别
        log.d("业务类别:", ywlb);
        log.d("年度：", ndu);
        List<Proj0Dto> Proj0DtoList = new ArrayList<>();
        List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestid, 1);//明细表数据
        Date newDate = new Date();
        String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
        String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
        for (Map map : wfMainMapList) {
            Proj0Dto proj0Dto = new Proj0Dto();
            proj0Dto.setRequestID(requestid);
            proj0Dto.setDetailID(map.get("id").toString());
            log.d("id=== ", map.get("id").toString());
            String projName = ProjectUtils.getZXMC(map.get("zxbm").toString());
            log.d("projName:", projName);
            proj0Dto.setProjName(projName);
            proj0Dto.setProjNo(map.get("zxbm").toString());//主项编码
            proj0Dto.setProjDeptNo(map.get("bmbm").toString());//部门编码
            proj0Dto.setYear(Integer.parseInt(ndu));//年度
            proj0Dto.setProjAmt(map.get("ndysje").toString());//年度预算
            proj0Dto.setCreDate(rq);//创建日期
            proj0Dto.setYwlb(ywlb);//业务类别
            proj0Dto.setCreTime(sj);//创建时间
            proj0Dto.setCreUser(sqr);//创建人
            Proj0DtoList.add(proj0Dto);
        }
        log.d("Proj0DtoList", Proj0DtoList);
        Proj0Service proj0Service = new Proj0Service();
        ResultDto resultDto = proj0Service.createProj0Bud(Proj0DtoList);
        log.d("resultDto", resultDto);
        if (resultDto.isOk()) {
            return "";
        } else {
            return resultDto.getMsg();
        }
    }
}
