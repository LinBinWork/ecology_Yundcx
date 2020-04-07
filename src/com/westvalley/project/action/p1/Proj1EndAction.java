package com.westvalley.project.action.p1;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.*;
import com.westvalley.project.entity.Proj1Entity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.po.Proj0Po;
import com.westvalley.project.service.*;
import com.westvalley.util.DevUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.general.TimeUtil;
import weaver.soa.workflow.request.RequestInfo;

import java.util.*;

/**
 * 父项立项拆解归档
 */
public class Proj1EndAction extends DevAbstractAction{
    /**
     * 业务逻辑
     *
     * @param info
     * @param actionDto
     * @return 返回 null 或者ok 则代表通过
     */
    @Override
    protected ResultDto runCode(RequestInfo info, ActionDto actionDto) {
        String requestID = actionDto.getRequestID();

        Map<String, String> wfMainMap = DevUtil.getWFMainMapByReqID(requestID);
        log.d("wfMainMap",wfMainMap);

        Proj0Service proj0Service = new Proj0Service();
        Proj0Po proj0ByNo = proj0Service.getProj0ByNo(getMapV(wfMainMap,"glzx"));
        String orgType = proj0ByNo.getOrgType();
        log.d("orgType",orgType);
        if("0".equals(orgType)){
            //执行发起，自动生成一条子项
            String proj2ID = DevUtil.executeQuery("select id from uf_proj where reqID = ? and projType = 2 ", requestID);
            if(!StringUtil.isEmpty(proj2ID)){
                initPermission(requestID,wfMainMap);
                return ResultDto.ok("已自动生成子项，不需再次生成");
            }
            String proj1ID = DevUtil.executeQuery("select id from uf_proj where reqID = ? and projType = 1 ", requestID);
            int pID = Integer.parseInt(proj1ID);

            Proj1Service proj1Service = new Proj1Service();
            Proj1Entity proj1 = proj1Service.getProj1(pID, requestID);

            List<Map<String, String>> detailList2 = DevUtil.getWFDetailByReqID(requestID,2);
            log.d("detailList2",detailList2);

            ResultDto dto = null;
            if(CtrlLevelEnum.PARENT.compareTo(proj1.getCtrlLevel()) == 0){
                //控制级别为父项
                BudgetSplitService budgetSplitService = new BudgetSplitService();
                List<Budget1Dto> bud1DtoList = getBud1DtoList(BudTypeEnum.SPLIT_USED, requestID, proj1, detailList2);
                dto = budgetSplitService.splitBudget1(bud1DtoList,BudTypeEnum.SPLIT_USED,true);
                if (!dto.isOk()) {
                    return dto;
                }
            }else {
                //冻结父项预算
                BudgetService budgetService = new BudgetService();

                //使用父项预算
                List<Budget1Dto> bud1DtoList = getBud1DtoList(BudTypeEnum.USED, requestID, proj1, detailList2);
                dto = budgetService.executeBudget1(bud1DtoList, BudTypeEnum.USED,true);
                log.d("使用父项预算", dto);
                if (!dto.isOk()) {
                    return dto;
                }
            }

            //创建子项
            Proj2Service proj2Service = new Proj2Service();
            dto = proj2Service.createProj2Bud(pID, getProj2List(requestID,proj1, wfMainMap, detailList2));
            if(dto.isOk()){
                initPermission(requestID,wfMainMap);
            }
            return dto;
        }else{
            return ResultDto.ok("非执行发起，不生成子项");
        }
    }

    /**
     * 初始项目权限
     * @param requestID
     * @param wfMainMap
     */
    public void initPermission(String requestID, Map<String, String> wfMainMap){
        try {
            String sql = "select id,projNo from uf_proj where reqid = ? and projtype = 2";
            RecordSet rs = new RecordSet();
            rs.executeQuery(sql,requestID);
            PermissionDto permissionDto = null;
            if(rs.next()){
                permissionDto = new PermissionDto();
                permissionDto.setProjID(rs.getInt("id"));
                permissionDto.setProjNo(rs.getString("projNo"));
            }
            if(permissionDto != null){
                List<PermissionDto> permissionDtoList = new ArrayList<>();

                //可拆解的人员为 项目经理 、 项目负责人、项目执行人员
                Set<String> splits = new HashSet<>();
                //可分享的人员为 项目经理 、 项目负责人、项目执行人员
                Set<String> Persons = new HashSet<>();

                //项目经理
                String manager = getMapV(wfMainMap,"xmjl");
                if(!StringUtil.isEmpty(manager)){
                    splits.add(manager);
                    Persons.add(manager);
                }

                //项目负责人
                String person = getMapV(wfMainMap,"xmfzr");
                if(!StringUtil.isEmpty(person)){
                    splits.add(person);
                    Persons.add(person);
                }

                permissionDto.setProjSplits(StringUtil.list2Str(splits));
                permissionDto.setProjPersons(StringUtil.list2Str(Persons));


                //建模可编辑权限  项目经理
                permissionDto.setProjManager(manager);

                permissionDtoList.add(permissionDto);

                ProjPermissionServoce permissionServoce = new ProjPermissionServoce();
                ResultDto dto = permissionServoce.savePermission(permissionDtoList);
                log.d("initPermission",dto);
            }else{
                log.d("initPermission失败，根据requestID检索不到projID ",sql,requestID);
            }
        } catch (Exception e) {
            log.e("initPermission",e);
        }
    }

    /**
     * 自动生成子项
     * @param requestID
     * @param proj1
     * @param wfMainMap
     * @param detailList2
     * @return
     */
    public List<Proj2Dto> getProj2List(String requestID, Proj1Entity proj1, Map<String, String> wfMainMap, List<Map<String, String>> detailList2){
        List<Proj2Dto> dtoList = new ArrayList<>();

        Proj2Dto dto = new Proj2Dto();

        dto.setRequestID(requestID);
        dto.setDetailID("0");
//        dto.setProjNo();
//        dto.setProjDeptNo();
        dto.setProjAmt(String.valueOf(proj1.getProjAmt()));
        String currentDateString = TimeUtil.getCurrentDateString();
        dto.setProjDate(currentDateString);
        dto.setCreDate(currentDateString);
        dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
        dto.setCreUser(proj1.getCreUser());
        dto.setPID(proj1.getId());
        dto.setProjName(proj1.getProjName()+"(自动生成)");
        dto.setProjDesc(proj1.getProjDesc());

        dto.setProjManager(getMapV(wfMainMap,"xmjl"));
        dto.setProjPerson(getMapV(wfMainMap,"xmfzr"));
        dto.setDeliveryCenter(1);//自动生成的子项，交付中心默认为否

        dtoList.add(dto);
        return dtoList;
    }

    public List<Budget1Dto> getBud1DtoList(BudTypeEnum userType, String requestID, Proj1Entity proj1,List<Map<String, String>> detailList2){
        List<Budget1Dto> budget1DtoList = new ArrayList<>();
        Budget1Dto dto = new Budget1Dto();
        int projID = 0;
        for (Map<String, String> map : detailList2) {
            projID = Integer.valueOf(map.get("ftbm"));//祖项年度预算ID
        }

        dto.setRequestID(requestID);
        dto.setDetailID("0");
        dto.setProjID(proj1.getId());
//        dto.setProjDeptNo();
        dto.setUseType(userType);
        dto.setUseAmt(proj1.getProjAmt());

        String currentDateString = TimeUtil.getCurrentDateString();
        String onlyCurrentTimeString = TimeUtil.getOnlyCurrentTimeString();
        dto.setCreDate(currentDateString);
        dto.setCreTime(onlyCurrentTimeString);
        dto.setCreUser(proj1.getCreUser());
        dto.setModDate(currentDateString);
        dto.setModTime(onlyCurrentTimeString);
        dto.setPID(projID);
        dto.setCtrlLevel(proj1.getCtrlLevel());
//        dto.setProjNo();
        dto.setAutoCre("0");

        budget1DtoList.add(dto);


        return budget1DtoList;
    }

}
