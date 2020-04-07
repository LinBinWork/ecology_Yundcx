package com.westvalley.project.service;

import com.westvalley.project.dao.Proj2Dao;
import com.westvalley.project.dao.Proj3Dao;
import com.westvalley.project.dto.Proj3Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj2Entity;
import com.westvalley.project.entity.Proj3Entity;
import com.westvalley.util.LogUtil;
import weaver.general.Util;

import java.util.List;

/**
 * 子项预算控制
 */
public class Proj3Service {

    private Proj2Dao proj2Dao;
    private Proj3Dao proj3Dao;

    private SerialService serialService;

    public Proj3Service(){
        proj2Dao = new Proj2Dao();
        proj3Dao = new Proj3Dao();

        serialService = new SerialService();
    }

    /**
     * 孙子项申请
     * @return
     */
    public ResultDto createProj3Bud(int p2ID,List<Proj3Dto> proj3DtoList){
        Proj2Entity proj2 = proj2Dao.getProj2ByID(p2ID);
        int proj3NoIndex = serialService.createProjNo(p2ID);
        if(proj3NoIndex == -1){
            return ResultDto.error("生成孙子项流水失败！");
        }
        for (Proj3Dto proj3Dto : proj3DtoList) {
            String proj3No = null;
            try {
                proj3No = serialService.createProj3No(proj2.getProjNo(),Util.add0(proj3NoIndex,2));
            } catch (Exception e) {
                return ResultDto.error("生成孙子项编号失败:"+e.getMessage());
            }
            proj3Dto.setProjNo(proj3No);
            proj3NoIndex++;
        }
        return proj3Dao.createProj3Bud(proj3DtoList);
    }


    /**
     * 获取孙子项编码
     * @param requestID
     * @param detailID
     * @return
     */
    public String getProj3No(String requestID,String detailID){
        return proj3Dao.getProj3No(requestID,detailID);
    }


    /**
     * 获取孙子项可用预算
     * @param id
     * @param requestID
     * @return
     */
    public Proj3Entity getProj3(int id, String requestID){
        return proj3Dao.getProj3BudgetByID(id,requestID);
    }

}
