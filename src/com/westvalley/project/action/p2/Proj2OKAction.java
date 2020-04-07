package com.westvalley.project.action.p2;

import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.*;
import com.westvalley.project.entity.Proj1Entity;
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
 * 子项立项拆解归档
 */
public class Proj2OKAction extends DevAbstractAction{
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
        List<Map<String, String>> detailList1 = DevUtil.getWFDetailByReqID(requestID,1);
        log.d("detailList1",detailList1);

        String proj2ID = DevUtil.executeQuery("select id from uf_proj where reqID = ? and projType = 2 ", requestID);
        if(!StringUtil.isEmpty(proj2ID)){
            initPermission(requestID);
            return ResultDto.ok("已生成子项，不需再次生成");
        }

        int p1ID = Util.getIntValue(wfMainMap.get("glfx"));

        Proj1Service proj1Service = new Proj1Service();
        Proj1Entity proj1 = proj1Service.getProj1(p1ID, requestID);

        List<Budget1Dto> budget1DtoList = getbudget1DtoList(requestID,proj1,wfMainMap,detailList1);
        //子项立项，父项预算冻结改为已使用
        ResultDto dto = null;
        if(CtrlLevelEnum.PARENT.compareTo(proj1.getCtrlLevel()) == 0){

            BudgetSplitService budgetSplitService = new BudgetSplitService();
            dto = budgetSplitService.splitBudget1(budget1DtoList,BudTypeEnum.SPLIT_USED,true);
        }else{
            BudgetService budgetService = new BudgetService();
            dto = budgetService.executeBudget1(budget1DtoList,BudTypeEnum.USED,true);
        }

        if(!dto.isOk()){
            return dto;
        }

        List<Proj2Dto> proj2DtoList = pkgProj2DtoList(requestID,wfMainMap,detailList1);
        Proj2Service proj2Service = new Proj2Service();
        dto = proj2Service.createProj2Bud(p1ID,proj2DtoList);
        if(dto.isOk()){
            ////更新子项编码
            for (Proj2Dto proj2Dto : proj2DtoList) {
                String detailID = proj2Dto.getDetailID();
                String proj2No = proj2Service.getProj2No(requestID, detailID);
                DevUtil.updateWFDetailData(requestID,1,detailID,"zxxmbh",proj2No);
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

            String sql = "select id,projNo,projManager,projPerson,projExcuer from uf_proj where reqid = ? and projtype = 2";

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
    public List<Proj2Dto> pkgProj2DtoList(String requestID, Map<String, String> wfMainMap, List<Map<String, String>> detailList1){
        List<Proj2Dto> proj2DtoList = new ArrayList<>();
        String fjrq = wfMainMap.get("fjrq");
        String shenqr = wfMainMap.get("shenqr");
        int p1ID = Util.getIntValue(wfMainMap.get("glfx"));
        for (Map<String, String> map : detailList1) {
            double useAmt = Double.valueOf(StringUtil.toNum(map.get("hsrmbje")));

            Proj2Dto dto = new Proj2Dto();

            dto.setRequestID(requestID);
            dto.setDetailID(map.get("id"));
//            dto.setProjNo();
//            dto.setProjDeptNo();String.valueOf()
            dto.setProjAmt(String.valueOf(useAmt));
            dto.setProjDate(fjrq);
            dto.setCreDate(TimeUtil.getCurrentDateString());
            dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setCreUser(shenqr);
            dto.setPID(p1ID);
            dto.setProjName(map.get("zx"));
            dto.setProjDesc(StringUtil.removeHTMLTag(map.get("zxsm")));

            dto.setProjManager(getMapV(map,"zxxmjl"));
            dto.setProjPerson(getMapV(map,"zxxmfzr"));
            dto.setProjExcuer(getMapV(map,"zxxmry"));
            dto.setProjExcustatus(getMapV(map,"sffazx"));

            //是否交付中心
            dto.setDeliveryCenter(Util.getIntValue(getMapV(map,"sfjfzxcl"),1));//默认为否

            proj2DtoList.add(dto);
        }


        return proj2DtoList;
    }

    /**
     * 子项立项，父项预算冻结改为已使用
     * @return
     */
    public List<Budget1Dto> getbudget1DtoList(String requestID, Proj1Entity proj1, Map<String, String> wfMainMap, List<Map<String, String>> detailList1){
        List<Budget1Dto> budget1DtoList = new ArrayList<>();

        String fjrq = wfMainMap.get("fjrq");
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
                dto.setUseType(BudTypeEnum.USED);
            }
            dto.setUseAmt(useAmt);
//            dto.setCreDate(fjrq);
//            dto.setCreTime();
            dto.setCreUser(shenqr);
            dto.setModDate(TimeUtil.getCurrentDateString());
            dto.setModTime(TimeUtil.getOnlyCurrentTimeString());
            dto.setPID(proj1.getPID());
            dto.setCtrlLevel(proj1.getCtrlLevel());
            dto.setProjNo(proj1.getProjNo());

            budget1DtoList.add(dto);
        }


        return budget1DtoList;
    }

}
