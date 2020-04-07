package com.westvalley.action.close;

import com.westvalley.project.dto.Budget23Dto;
import com.westvalley.project.dto.ReleaseDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.service.BudgetReleaseService;
import com.westvalley.project.service.BudgetService;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.service.ProjectService;
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
 * 引用流程：项目关闭审批流程
 * 引用节点：申请节点-节点后附加操作
 * 主要功能：1、检验当前是否存在应合同结案但未完成结案的情况，如存在则进行提示，不允许流程提交。2、校验是否当前项目包含在途流程，如包含不允许关闭。3、检验当前是否存在未完结的报销流程的情况，如存在则进行提示，不允许流程提交。
 */
public class CloseApplyAction implements Action {

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
        ProjectService projectService = new ProjectService();
        int fid = Integer.valueOf(wfMainMap.get("xmmc"));
        log.d("fid:", fid);
        StringBuffer msg = new StringBuffer();
        //检验当前是否存在应合同结案但未完成结案的情况
        ResultDto resultDto = projectService.checkClose1(fid);
        if (resultDto != null && !resultDto.isOk()) {
            msg.append(resultDto.getMsg());
        }
        //校验是否存在在途流程
        ResultDto resultDto1 = projectService.checkClose3(fid);
        if (resultDto1 != null && !resultDto1.isOk()) {
            msg.append("<br><br>" + resultDto1.getMsg());
        }
        ResultDto resultDto2 = projectService.checkClose4(fid);
        if (resultDto2 != null && !resultDto2.isOk()) {
            msg.append("<br><br>" + resultDto2.getMsg());
        }
        if (StringUtils.isEmpty(msg.toString())) {
            //冻结剩余金额
            //释放金额
            Date newDate = new Date();
            String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
            String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
            BudgetService budgetService = new BudgetService();
            BudgetReleaseService budgetReleaseService = new BudgetReleaseService();

            List<ReleaseDto> releaseDtos = new ArrayList<>();
            ProjEntity projEntity = budgetService.getProjInfo(fid, requestId);
            log.d("父项：", projEntity);
            ReleaseDto releaseDto = new ReleaseDto();
            releaseDto.setRequestID(requestId);
            releaseDto.setPID(projEntity.getPID());
            releaseDto.setProjID(projEntity.getId());
            releaseDto.setProjType(projEntity.getProjType());
            releaseDto.setUseType(BudTypeEnum.FREZEE);
            releaseDto.setUseAmt(projEntity.getProjBalance());
            releaseDto.setCreDate(rq);
            releaseDto.setCreTime(sj);
            releaseDto.setCreUser(wfMainMap.get("shenqr"));

            List<Budget23Dto> budget23DtoList = new ArrayList<>();
            List<Map<String, String>> wfMainMapList1 = DevUtil.getWFDetailByReqID(requestId, 1);
            for (Map<String, String> map : wfMainMapList1) {
                if (map.get("sfjfzxcl").equals("1")) {
                    //不是交付中心
                    continue;
                }
                ProjEntity projEntity2 = budgetService.getProjInfo(Integer.valueOf(map.get("zxszxbh")), requestId);
                log.d("子项/孙子项 ====== ", projEntity2);
                Budget23Dto budget23Dto = new Budget23Dto();
                budget23Dto.setRequestID(requestId);
                budget23Dto.setDetailID(map.get("id"));
                budget23Dto.setPID(projEntity2.getPID());
                budget23Dto.setProjID(projEntity2.getId());
                budget23Dto.setProjNo(projEntity2.getProjNo());
                budget23Dto.setProjDeptNo(projEntity2.getProjDeptNo());
                budget23Dto.setProjtype(EnumsUtil.getProjtypeEnum(projEntity2.getProjType()));
                budget23Dto.setUseType(BudTypeEnum.FREZEE);
                budget23Dto.setCtrlLevel(projEntity2.getCtrlLevel());
                budget23Dto.setUseAmt(Double.valueOf(map.get("yfsje")));
                budget23Dto.setCreDate(rq);
                budget23Dto.setCreTime(sj);
                budget23Dto.setCreUser(wfMainMap.get("shenqr"));
                budget23DtoList.add(budget23Dto);
            }
            releaseDto.setBudget23DtoList(budget23DtoList);
            releaseDtos.add(releaseDto);
            ResultDto resultDto3 = budgetReleaseService.releaseBudget(releaseDtos, BudTypeEnum.FREZEE, true);
            return resultDto3 != null && resultDto3.isOk() ? "" : resultDto3.getMsg();
        } else {
            return msg.toString();
        }
    }
}
