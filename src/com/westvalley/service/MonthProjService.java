package com.westvalley.service;

import com.westvalley.dao.MonthProjDao;
import com.westvalley.dto.MonthProj;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.util.LogUtil;

import java.util.List;

/**
 * 月度预算控制
 */
public class MonthProjService {

    private LogUtil log = LogUtil.getLogger(getClass());

    private MonthProjDao monthProjDao;

    public MonthProjService() {
        this.monthProjDao = new MonthProjDao();
    }

    /**
     * 导入校验，存在则更新数据，不存在则插入数据
     *
     * @param monthProjList
     * @return
     */
    public ResultDto checkMonthProj(List<MonthProj> monthProjList) {
        return monthProjDao.checkMonthProj(monthProjList);
    }
}
