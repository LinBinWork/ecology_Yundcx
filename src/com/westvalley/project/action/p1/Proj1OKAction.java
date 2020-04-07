package com.westvalley.project.action.p1;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.*;
import com.westvalley.project.entity.Proj0Entity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.service.BudgetService;
import com.westvalley.project.service.Proj0Service;
import com.westvalley.project.service.Proj1Service;
import com.westvalley.project.service.ProjPermissionServoce;
import com.westvalley.util.DevUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.general.TimeUtil;
import weaver.soa.workflow.request.RequestInfo;

import java.util.*;

/**
 * 父项立项拆解归档
 */
public class Proj1OKAction extends DevAbstractAction{
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

        String xmbh = getMapV(wfMainMap, "xmbh");
        if(!StringUtil.isEmpty(xmbh)){
            //如果已生成项目编码代表已归档
            initPermission(requestID,wfMainMap);
            return ResultDto.ok("已生成父项项目编号，不再执行");
        }
        List<Map<String, String>> detailList2 = DevUtil.getWFDetailByReqID(requestID,2);
        log.d("detailList2",detailList2);

        List<Budget0Dto> budget0DtoList = getbudget0DtoList(requestID,wfMainMap,detailList2);
        BudgetService budgetService = new BudgetService();
        //父项立项归档，将祖项冻结预算 改为已使用
        ResultDto dto = budgetService.executeBudget0(budget0DtoList,BudTypeEnum.USED,true);
        if(!dto.isOk()){
            return dto;
        }
        //归档
        Proj1Service proj1Service = new Proj1Service();
        dto = proj1Service.createProj1Bud(pkgProj1Dto(requestID,wfMainMap,detailList2));
        if(dto.isOk()){
            //更新父项编码
            String proj1No = proj1Service.getProj1No(requestID);
            DevUtil.updateWFMainData(requestID,"xmbh",proj1No);

            initPermission(requestID,wfMainMap);

        }
        return dto;
    }

    /**
     * 初始项目权限
     * @param requestID
     * @param wfMainMap
     */
    public void initPermission(String requestID, Map<String, String> wfMainMap){
        try {
            String sql = "select id,projNo from uf_proj where reqid = ? and projtype = 1";
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
     * 获取祖项数据，将祖项冻结预算 改为已使用
     * @param requestID
     * @param wfMainMap
     * @param detailList2
     * @return
     */
    private List<Budget0Dto> getbudget0DtoList(String requestID, Map<String, String> wfMainMap, List<Map<String, String>> detailList2) {

        List<Budget0Dto> budget0DtoList = new ArrayList<>();

        Proj0Service proj0Service = new Proj0Service();

        String lxrq = wfMainMap.get("lxrq");
        String shenqr = wfMainMap.get("shenqr");

        for (Map<String, String> map : detailList2) {

            int projID = Integer.valueOf(map.get("ftbm"));
            double useAmt = Double.valueOf(StringUtil.toNum(map.get("bclxje")));

            Proj0Entity entity = proj0Service.getProj0(projID,requestID);
            Budget0Dto dto = new Budget0Dto();

            dto.setRequestID(requestID);
            dto.setDetailID(map.get("id"));
            dto.setProjID(projID);
            dto.setProjDeptNo(entity.getProjNo());
            dto.setUseType(BudTypeEnum.USED);
            dto.setUseAmt(useAmt);
            dto.setCreDate(lxrq);
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
//            dto.setModDate();
//            dto.setModTime();
            dto.setPID(0);
            dto.setCtrlLevel(entity.getCtrlLevel());
            dto.setProjNo(entity.getProjNo());

            budget0DtoList.add(dto);

        }

        return budget0DtoList;
    }

    /**
     * 封装父项数据
     * @return
     */
    public Proj1Dto pkgProj1Dto(String requestID, Map<String, String> wfMainMap, List<Map<String, String>> detailList2){
//        List<Proj1Dto> proj1DtoList = new ArrayList<>();
        Proj0Service proj0Service = new Proj0Service();

        int projID = 0;
        String detailID = "";
        String projDeptNo = "";
//        Map<String, String> map = detailList2.get(0);
        int index = 0;
        for (Map<String, String> map : detailList2) {
            projID = Integer.valueOf(map.get("ftbm"));//祖项年度预算ID
            Proj0Entity entity = proj0Service.getProj0(projID,requestID);

            if(index == 0){
                detailID = getMapV(map,"id");
                projDeptNo = entity.getProjDeptNo();
            }else{
//                detailID += ",";
//                detailID += getMapV(map,"id");

                projDeptNo += ",";
                projDeptNo += entity.getProjDeptNo();
            }

            index++;
        }
        Proj1Dto dto = new Proj1Dto();
        dto.setRequestID(requestID);
        dto.setDetailID(detailID);
//            dto.setProjNo(entity.getProjNo());//service会自动生成
        dto.setProjDeptNo(projDeptNo);//成本中心
        dto.setProjDesc(StringUtil.toStr(getMapV(wfMainMap,"xmgsxsal")));
        dto.setProjAmt(StringUtil.toNum(getMapV(wfMainMap,"lxzje")));
        dto.setProjDate(getMapV(wfMainMap,"lxrq"));
        dto.setCreDate(TimeUtil.getCurrentDateString());//立项日期
        dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
        dto.setCreUser(getMapV(wfMainMap,"shenqr"));
        dto.setPID(projID);//都是同一个祖项，随便取一个ID
        dto.setProjName(getMapV(wfMainMap,"xmmc"));
        dto.setProjDesc(getMapV(wfMainMap,"xmgsxsal"));

        dto.setProjManager(getMapV(wfMainMap,"xmjl"));
        dto.setProjPerson(getMapV(wfMainMap,"xmfzr"));


        dto.setProjCategory(getMapV(wfMainMap,"xmlb"));//项目类别

        return dto;
    }

}
