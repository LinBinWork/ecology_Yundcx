package com.westvalley.project.service;

import com.westvalley.project.dao.Proj0Dao;
import com.westvalley.project.dao.Proj1Dao;
import com.westvalley.project.dto.Proj1Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj0Entity;
import com.westvalley.project.entity.Proj1Entity;
import com.westvalley.util.LogUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 父项预算控制
 */
public class Proj1Service {

    private Proj0Dao proj0Dao;
    private Proj1Dao proj1Dao;
    private SerialService serialService;

    public Proj1Service(){
        proj0Dao = new Proj0Dao();
        proj1Dao = new Proj1Dao();

        serialService = new SerialService();
    }

    /**
     * 父项立项流程归档  生成父项预算
     * @return
     */
    public synchronized ResultDto createProj1Bud(Proj1Dto proj1Dto){
        List<Proj1Dto> proj1DtoList = new ArrayList<>();

        Proj0Entity proj0 = proj0Dao.getProj0ByID(proj1Dto.getPID());
        String proj0No = proj0.getProjNo();
        //生成父项流水编号

        String proj1No = serialService.createProjNo(proj0No,proj0.getProjYear());
        if("".equals(proj1No)){
            return ResultDto.error("生成父项流水失败！");
        }
        try {
            proj1No = serialService.createProjectNo(proj0No,proj1No);
        } catch (Exception e) {
            return ResultDto.error("生成父项编号失败:"+e.getMessage());
        }
        proj1Dto.setProjNo(proj1No);
        proj1DtoList.add(proj1Dto);
        return proj1Dao.createProj1Bud(proj1DtoList);
    }
    /**
     * 获取父项编码
     * @param requestID
     * @return
     */
    public String getProj1No(String requestID){
        return proj1Dao.getProj1No(requestID);
    }

    /**
     * 获取父项可用预算
     * @param id
     * @param requestID
     * @return
     */
    public Proj1Entity getProj1(int id, String requestID){
        return proj1Dao.getProj1BudgetByID(id,requestID);
    }

}
