package com.westvalley.project.action.p3;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.*;
import com.westvalley.project.entity.Proj1Entity;
import com.westvalley.project.entity.Proj2Entity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.service.*;
import com.westvalley.util.DevUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import java.util.*;

/**
 * 孙子项立项拆解归档
 */
public class Proj3OKAction extends DevAbstractAction{
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

        String proj2ID = DevUtil.executeQuery("select id from uf_proj where reqID = ? and projType = 3 ", requestID);
        if(!StringUtil.isEmpty(proj2ID)){
            initPermission(requestID);
            return ResultDto.ok("已生成孙子项，不需再次生成");
        }

        List<Map<String, String>> detailList1 = DevUtil.getWFDetailByReqID(requestID,1);
        log.d("detailList1",detailList1);

        int projID = Integer.valueOf(wfMainMap.get("zxxmmc"));
        Proj2Service service = new Proj2Service();
        Proj2Entity proj2 = service.getProj2(projID, requestID);


        ResultDto dto = null;
        if(CtrlLevelEnum.PARENT.compareTo(proj2.getCtrlLevel()) == 0){
            Proj1Service proj1Service = new Proj1Service();
            Proj1Entity proj1 = proj1Service.getProj1(proj2.getPID(), requestID);
            List<Budget1Dto> budget1DtoList = getbudget1DtoList(requestID, proj1,proj2, wfMainMap,detailList1);
            BudgetSplitService budgetSplitService = new BudgetSplitService();
            dto = budgetSplitService.splitBudget1(budget1DtoList,BudTypeEnum.SPLIT_USED,true);
        }else{
            //冻结子项预算
            List<Budget23Dto> budget2DtoList = getbudget2DtoList(requestID,proj2,wfMainMap,detailList1);
            BudgetService budgetService = new BudgetService();
            dto = budgetService.executeBudget23(budget2DtoList,BudTypeEnum.USED);
        }

        if(!dto.isOk()){
            return dto;
        }
        int p2ID = Util.getIntValue(wfMainMap.get("zxxmmc"));
        List<Proj3Dto> proj3DtoList = pkgProj3DtoList(requestID,wfMainMap,detailList1);
        Proj3Service proj3Service = new Proj3Service();
        dto = proj3Service.createProj3Bud(p2ID,proj3DtoList);
        if(dto.isOk()){
            //更新孙子项编码
            for (Proj3Dto proj3Dto : proj3DtoList) {
                String detailID = proj3Dto.getDetailID();
                String proj3No = proj3Service.getProj3No(requestID, detailID);
                DevUtil.updateWFDetailData(requestID,1,detailID,"szxxmbh",proj3No);
            }

            initPermission(requestID);
        }
        return dto;
    }


    /**
     * 初始项目权限
     * @param requestID
     */
    public void initPermission(String requestID){
        try {

            String sql = "select id,projNo,projManager,projPerson,projExcuer from uf_proj where reqid = ? and projtype = 3";

            RecordSet rs = new RecordSet();
            rs.executeQuery(sql,requestID);
            List<PermissionDto> permissionDtoList = new ArrayList<>();
            while(rs.next()){
                PermissionDto permissionDto = new PermissionDto();
                permissionDto.setProjID(rs.getInt("id"));
                permissionDto.setProjNo(rs.getString("projNo"));

                //可拆解的人员为 项目经理 、 项目负责人、项目执行人员
                Set<String> splits = new HashSet<>();
                //可分享的人员为 项目经理 、 项目负责人、项目执行人员
                Set<String> persons = new HashSet<>();

                //项目经理
                String projManager = rs.getString("projManager");//单选
                if(!StringUtil.isEmpty(projManager)){
                    splits.add(projManager);
                    persons.add(projManager);
                }
                //项目负责人
                String projPerson = rs.getString("projPerson");//单选
                if(!StringUtil.isEmpty(projPerson)){
                    splits.add(projPerson);
                    persons.add(projPerson);
                }
                //项目执行人员
                String projExcuer = rs.getString("projExcuer");//多选
                if(!StringUtil.isEmpty(projExcuer)){
                    for(String s : projExcuer.split(",")){
                        if(!StringUtil.isEmpty(s)){
                            splits.add(s);
                            persons.add(s);
                        }
                    }
                }

                permissionDto.setProjSplits(StringUtil.list2Str(splits));
                permissionDto.setProjPersons(StringUtil.list2Str(persons));

                //建模可编辑权限  项目经理
                permissionDto.setProjManager(projManager);

                permissionDtoList.add(permissionDto);
            }
            ProjPermissionServoce permissionServoce = new ProjPermissionServoce();
            ResultDto dto = permissionServoce.savePermission(permissionDtoList);
            log.d("initPermission",dto);
        } catch (Exception e) {
            log.e("initPermission",e);
        }
    }

    /**
     * 封装
     * @return
     */
    public List<Proj3Dto> pkgProj3DtoList(String requestID, Map<String, String> wfMainMap, List<Map<String, String>> detailList1){
        List<Proj3Dto> proj3DtoList = new ArrayList<>();
        int p2ID = Util.getIntValue(wfMainMap.get("zxxmmc"));
        for (Map<String, String> map : detailList1) {
            double useAmt = Double.valueOf(StringUtil.toNum(map.get("hsrmbje")));

            Proj3Dto dto = new Proj3Dto();
            dto.setRequestID(requestID);
            dto.setDetailID(map.get("id"));
//            dto.setProjNo();
//            dto.setProjDeptNo();
            dto.setProjAmt(String.valueOf(useAmt));
            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(getMapV(wfMainMap,"shenqr"));
            dto.setPID(p2ID);
            dto.setProjName(getMapV(map,"szx"));
            dto.setProjDesc(getMapV(map,"szxsm"));
            dto.setProjDate(getMapV(wfMainMap,"fjrq"));
            dto.setProjManager(getMapV(map,"szxxmjl"));
//            dto.setProjPerson(getMapV(map,"zxxmry"));
            dto.setProjExcuer(getMapV(map,"zxxmry"));

            proj3DtoList.add(dto);
        }
        return proj3DtoList;
    }


    /**
     * 封装
     * @return
     */
    public List<Budget23Dto> getbudget2DtoList(String requestID, Proj2Entity proj2, Map<String, String> wfMainMap, List<Map<String, String>> detailList1){
        int projID = Integer.valueOf(wfMainMap.get("zxxmmc"));
        List<Budget23Dto> budget2DtoList = new ArrayList<>();
        boolean isParentCtrl = CtrlLevelEnum.PARENT.compareTo(proj2.getCtrlLevel()) == 0;

        for (Map<String, String> map : detailList1) {

            double useAmt = Double.valueOf(StringUtil.toNum(map.get("hsrmbje")));

            Budget23Dto dto = new Budget23Dto();
            dto.setRequestID(requestID);
            dto.setDetailID(map.get("id"));
            dto.setProjID(projID);
            dto.setProjDeptNo(proj2.getProjDeptNo());
            if(isParentCtrl){
                dto.setUseType(BudTypeEnum.SPLIT_USED);
            }else {
                dto.setUseType(BudTypeEnum.USED);
            }
            dto.setUseAmt(useAmt);

            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(getMapV(wfMainMap,"shenqr"));
//            dto.setModDate();
//            dto.setModTime();
            dto.setPID(proj2.getPID());
            dto.setCtrlLevel(proj2.getCtrlLevel());
            dto.setProjNo(proj2.getProjNo());

            budget2DtoList.add(dto);
        }
        return budget2DtoList;
    }



    /**
     * 封装父项冻结预算
     * @return
     */
    public List<Budget1Dto> getbudget1DtoList(String requestID, Proj1Entity proj1, Proj2Entity proj2, Map<String, String> wfMainMap, List<Map<String, String>> detailList1){
        List<Budget1Dto> budget1DtoList = new ArrayList<>();

        String shenqr = wfMainMap.get("shenqr");

        boolean isParentCtrl = CtrlLevelEnum.PARENT.compareTo(proj1.getCtrlLevel()) == 0;
        for (Map<String, String> map : detailList1) {
            double useAmt = Double.valueOf(StringUtil.toNum(map.get("hsrmbje")));

            Budget1Dto dto = new Budget1Dto();
            dto.setRequestID(requestID);
            dto.setDetailID(map.get("id"));
            dto.setProjID(proj1.getId());
            dto.setProjDeptNo(proj1.getProjDeptNo());
            if(isParentCtrl){
                dto.setUseType(BudTypeEnum.SPLIT_USED);
            }else {
                dto.setUseType(BudTypeEnum.FREZEE);
            }
            dto.setUseAmt(useAmt);
            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
//        dto.setModDate();
//        dto.setModTime();
            dto.setPID(proj1.getPID());
            dto.setCtrlLevel(proj1.getCtrlLevel());
            dto.setProjNo(proj1.getProjNo());

            budget1DtoList.add(dto);
        }
        if(isParentCtrl){
            /*
                孙子项拆解  需要把子项的拆解金额释放  取子项和孙子项的最大值
                孙子项合计金额为 A  子项金额为B
                A <= B  释放金额为 A
                A > B 释放金额为 B
=             */
            double A = Double.valueOf(StringUtil.toNum(wfMainMap.get("zxjehj")));
            double B = proj2.getProjAmt();
            double useAmt = A <= B ? A : B;
            Budget1Dto dto = new Budget1Dto();
            dto.setRequestID(requestID);
            dto.setDetailID("0");
            dto.setProjID(proj1.getId());
            dto.setProjDeptNo(proj1.getProjDeptNo());
            dto.setUseType(BudTypeEnum.SPLIT_USED);
            dto.setUseAmt(-useAmt);
            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
//        dto.setModDate();
//        dto.setModTime();
            dto.setPID(proj1.getPID());
            dto.setCtrlLevel(proj1.getCtrlLevel());
            dto.setProjNo(proj1.getProjNo());
            dto.setSplit("0");

            budget1DtoList.add(dto);
        }

        return budget1DtoList;
    }

}
