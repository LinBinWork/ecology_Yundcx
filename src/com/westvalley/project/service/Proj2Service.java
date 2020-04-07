package com.westvalley.project.service;

import com.westvalley.project.dao.Proj1Dao;
import com.westvalley.project.dao.Proj2Dao;
import com.westvalley.project.dto.Proj2Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj1Entity;
import com.westvalley.project.entity.Proj2Entity;
import com.westvalley.util.LogUtil;
import weaver.general.Util;

import java.util.List;

/**
 * 子项预算控制
 */
public class Proj2Service {

    private Proj1Dao proj1Dao;
    private Proj2Dao proj2Dao;
    private SerialService serialService;

    public Proj2Service(){
        proj1Dao = new Proj1Dao();
        proj2Dao = new Proj2Dao();
        serialService = new SerialService();
    }

    /**
     * 子项立项
     * @return
     */
    public ResultDto createProj2Bud(int p1ID,List<Proj2Dto> proj2DtoList){

        Proj1Entity proj1 = proj1Dao.getProj1ByID(p1ID);
        int proj2NoIndex = serialService.createProjNo(p1ID);
        if(proj2NoIndex == -1){
            return ResultDto.error("生成子项流水失败！");
        }

        for (Proj2Dto proj2Dto : proj2DtoList) {
            String proj2No = null;
            try {
                proj2No = serialService.createProj2No(proj1.getProjNo(), Util.add0(proj2NoIndex,2));
            } catch (Exception e) {
                return ResultDto.error("生成子项编号失败:"+e.getMessage());
            }
            proj2Dto.setProjNo(proj2No);
            proj2NoIndex++;
        }
        return proj2Dao.createProj2Bud(proj2DtoList);
    }

    /**
     * 获取子项编码
     * @param requestID
     * @param detailID
     * @return
     */
    public String getProj2No(String requestID,String detailID){
        return proj2Dao.getProj2No(requestID,detailID);
    }

    /**
     * 获取子项可用预算
     * @param id
     * @param requestID
     * @return
     */
    public Proj2Entity getProj2(int id, String requestID){
        return proj2Dao.getProj2BudgetByID(id,requestID);
    }
}
