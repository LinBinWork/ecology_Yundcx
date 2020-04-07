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
 * 引用节点：归档节点
 * 主要功能：项目更改为已关闭状态,释放金额
 */
public class CloseReturnActtion implements Action {

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
        int id = Integer.valueOf(wfMainMap.get("xmmc"));
        //项目状态更改为已关闭
        ProjectService projectService = new ProjectService();
        ResultDto resultDto = projectService.closeProject(id);
        log.d("resultDto === ", resultDto);
        if (resultDto != null && !resultDto.isOk()) {
            return resultDto.getMsg();
        } else {
            //释放金额
            Date newDate = new Date();
            String rq = new SimpleDateFormat("yyyy-MM-dd").format(newDate);
            String sj = new SimpleDateFormat("HH:mm:ss").format(newDate);
            BudgetService budgetService = new BudgetService();
            BudgetReleaseService budgetReleaseService = new BudgetReleaseService();

            List<ReleaseDto> releaseDtos = new ArrayList<>();
            int fid = Integer.valueOf(wfMainMap.get("xmmc"));
            ProjEntity projEntity = budgetService.getProjInfo(fid, requestId);
            log.d("父项：", projEntity);
            ReleaseDto releaseDto = new ReleaseDto();
            releaseDto.setRequestID(requestId);
            releaseDto.setPID(projEntity.getPID());
            releaseDto.setProjID(projEntity.getId());
            releaseDto.setProjType(projEntity.getProjType());
            releaseDto.setUseType(BudTypeEnum.RELEASE);
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
                budget23Dto.setUseType(BudTypeEnum.USED);
                budget23Dto.setCtrlLevel(projEntity2.getCtrlLevel());
                budget23Dto.setUseAmt(Double.valueOf(map.get("yfsje")));
                budget23Dto.setModTime(rq);
                budget23Dto.setModTime(sj);
                budget23Dto.setCreUser(wfMainMap.get("shenqr"));
                budget23DtoList.add(budget23Dto);
            }
            releaseDto.setBudget23DtoList(budget23DtoList);
            releaseDtos.add(releaseDto);
            ResultDto resultDto3 = budgetReleaseService.releaseBudget(releaseDtos, BudTypeEnum.RELEASE, true);
            return resultDto3 != null && resultDto3.isOk() ? "" : resultDto3.getMsg();
        }
    }
}
