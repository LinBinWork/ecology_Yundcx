package com.westvalley.project.service;

import com.westvalley.project.dao.Proj0Dao;
import com.westvalley.project.dao.ProjDao;
import com.westvalley.project.dto.Proj0Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj0Entity;
import com.westvalley.project.po.Proj0Po;
import com.westvalley.util.LogUtil;

import java.util.List;

/**
 * 祖项预算控制
 */
public class Proj0Service {

    private Proj0Dao dao;
    private ProjDao projDao;

    public Proj0Service() {
        dao = new Proj0Dao();
        projDao = new ProjDao();
    }

    /**
     * 创建祖项预算
     *
     * @return
     */
    public ResultDto createProj0Bud(List<Proj0Dto> Proj0DtoList) {
        ResultDto dto = checkProj0Bud(Proj0DtoList);
        if(!dto.isOk()){
            return dto;
        }
        return dao.createProj0Bud(Proj0DtoList);
    }

    /**
     * 对于同一年度，如系统中已存在有数据，不允许再次导入
     *
     * @return
     */
    public ResultDto checkProj0Bud(List<Proj0Dto> Proj0DtoList) {
        return dao.checkProj0Bud(Proj0DtoList);
    }


    /**
     * 获取祖项可用预算
     * @param id
     * @param requestID
     * @return
     */
    public Proj0Entity getProj0(int id,String requestID){
        return dao.getProj0BudgetByID(id,requestID);
    }

    /**
     * 获取祖项标标准数据
     * @param no
     * @return
     */
    public Proj0Po getProj0ByNo(String no){
        return projDao.getProj0ByNo(no);
    }

}
