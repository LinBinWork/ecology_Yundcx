package com.westvalley.project.service;

import com.westvalley.project.dao.ProjDao;
import com.westvalley.project.dto.Budget23Dto;
import com.westvalley.project.dto.ReleaseDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.ProjEntity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.po.BudgetExcuPo;
import com.westvalley.project.util.ProjUtil;
import com.westvalley.util.LogUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 预算关闭释放金额
 */
public class BudgetReleaseService {

    private LogUtil log = LogUtil.getLogger(getClass());

    public BudgetReleaseService(){
    }

    /**
     * 父项项目关闭
     * @param releaseDtoList 释放数据
     * @param budTypeEnum FREZEE代表申请节点提交  RELEASE 代表归档
     * @return 执行结果
     */
    public synchronized ResultDto releaseBudget(List<ReleaseDto> releaseDtoList, BudTypeEnum budTypeEnum,boolean isDelete){
        log.d("releaseDtoList",releaseDtoList);
        ResultDto dto = checkRelease(releaseDtoList);
        if(!dto.isOk()){
            return dto;
        }
        BudgetExcuService excuService = new BudgetExcuService();
        List<BudgetExcuPo> data4Release = excuService.getData4Release(releaseDtoList, budTypeEnum);
        dto = excuService.excuBudget(data4Release,isDelete);

        //释放后需要将项目状态更新为关闭
        if(BudTypeEnum.RELEASE.compareTo(budTypeEnum) == 0){
            ProjDao projDao = new ProjDao();
            dto = projDao.closeProj(data4Release);
        }
        return dto;
    }
    /**
     * 校验项目关闭
     * @param releaseDtoList 释放数据
     * @return 校验结果
     */
    public ResultDto checkRelease(List<ReleaseDto> releaseDtoList){
        BudgetService budgetService = new BudgetService();
        StringBuilder msg = new StringBuilder();
        for (ReleaseDto dto : releaseDtoList) {
            ResultDto resultDto = ProjUtil.isAllChildrenClose(dto.getProjID(),dto.getRequestID());
            if(!resultDto.isOk()){
                return resultDto;
            }
            //如果是子项是交付中心的
            List<Budget23Dto> budget23DtoList = dto.getBudget23DtoList();
            if(budget23DtoList.size() > 0){
                //校验使用金额不能大于剩余金额
                int proj1ID = dto.getProjID();
                ProjEntity proj1Info = budgetService.getProjInfo(proj1ID, dto.getRequestID());
                BigDecimal total = BigDecimal.ZERO;
                boolean isParentCtrl = CtrlLevelEnum.PARENT.compareTo(proj1Info.getCtrlLevel()) == 0;

                for (Budget23Dto budget23Dto : budget23DtoList) {
                    int proj23ID = budget23Dto.getProjID();
                    double useAmt = budget23Dto.getUseAmt();
                    //父端控制
                    if(isParentCtrl){
                        total = total.add(new BigDecimal(String.valueOf(useAmt)));
                    }else{
                        ProjEntity proj23 = budgetService.getProjInfo(proj23ID, dto.getRequestID());
                        double projBalance = proj23.getProjBalance();
                        if(useAmt > projBalance){
                            msg.append("<br>项目预算使用/冻结总金额不能大于可用余额！")
                                    .append("项目编号：").append(proj23.getProjNo())
                                    .append(",项目名称：").append(proj23.getProjName())
                                    .append(",使用/冻结总金额：").append(useAmt)
                                    .append(",可用余额：").append(projBalance);
                        }
                    }
                }
                if(isParentCtrl){
                    double projBalance = proj1Info.getProjBalance();
                    if(total.doubleValue() > projBalance){
                        msg.append("<br>父端控制，项目预算使用/冻结总金额不能大于可用余额！")
                                .append(",使用/冻结总金额：").append(total.doubleValue())
                                .append(",可用余额：").append(projBalance);
                    }
                }
            }
        }
        return ResultDto.init(msg.length() == 0,msg.toString(),null);
    }


    public BigDecimal add(double a,double b){
        BigDecimal aa = new BigDecimal(String.valueOf(a));
        BigDecimal bb = new BigDecimal(String.valueOf(b));
        return aa.add(bb);
    }


}
