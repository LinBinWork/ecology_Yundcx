package com.westvalley.action.budget.year;

import com.westvalley.project.dto.Proj0Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.service.Proj0Service;
import com.westvalley.util.BaseUtils;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 引用流程：年度项目预算申请流程
 * 引用节点：申请节点
 * 主要功能：判断明细表是否已经导入有数据
 *
 * @author cgd 2019.07.23
 */
public class YearBudgetApplyAction implements Action {

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
        log.d("年度：", ndu);
        List<Proj0Dto> Proj0DtoList = new ArrayList<>();
        List<Map<String, String>> wfMainMapList = DevUtil.getWFDetailByReqID(requestId, 1);//明细表数据
        for (Map map : wfMainMapList) {
            Proj0Dto proj0Dto = new Proj0Dto();
            proj0Dto.setRequestID(requestId);
            proj0Dto.setProjNo(map.get("zxbm").toString());//主项编码
            proj0Dto.setProjDeptNo(map.get("bmbm").toString());//部门编码
            proj0Dto.setYear(Integer.parseInt(ndu));//年度
            Proj0DtoList.add(proj0Dto);
        }
        log.d("Proj0DtoList", Proj0DtoList);
        Proj0Service proj0Service = new Proj0Service();
        ResultDto resultDto = proj0Service.checkProj0Bud(Proj0DtoList);
        log.d("resultDto", resultDto);
        if (resultDto.isOk()) {
            return "";
        } else {
            return resultDto.getMsg();
        }
    }
}
