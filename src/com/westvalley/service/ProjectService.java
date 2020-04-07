package com.westvalley.service;

import com.westvalley.dao.ProjectDao;
import com.westvalley.entity.BudgetEntity;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.util.LogUtil;

public class ProjectService {

    private LogUtil log = LogUtil.getLogger(getClass());

    private ProjectDao projectDao;

    public ProjectService() {
        this.projectDao = new ProjectDao();
    }

    /**
     * 判断是否是子项
     *
     * @param xmbh
     * @return
     */
    public String executeSubitem(String xmbh) {
        return projectDao.executeSubitem(xmbh);
    }

    /**
     * 判断是否是孙子项
     *
     * @param xmbh
     * @return
     */
    public String executeGrandson(String xmbh) {
        return projectDao.executeGrandson(xmbh);
    }

    /**
     * 根据id获取项目预算信息
     *
     * @param id
     * @return
     */
    public BudgetEntity getBudgetEntityById(int id) {
        return projectDao.getBudgetEntityById(id);
    }

    /**
     * 根据父项id检验当前是否存在应合同结案但未完成结案的情况，如存在则进行提示，不允许流程提交
     *
     * @param id
     * @return
     */
    public ResultDto checkClose1(int id) {
        try {
            String msg = projectDao.checkClose1(id);
            return ResultDto.init(msg.length() == 0, msg, null);
        } catch (Exception e) {
            e.printStackTrace();
            log.d("校验发生错误：", e);
            return ResultDto.error("校验发生错误。");
        }
    }

    /**
     * 修改项目为已关闭状态
     *
     * @param id
     * @return
     */
    public ResultDto closeProject(int id) {
        try {
            String msg = projectDao.closeProject(id);
            return ResultDto.init(msg.length() == 0, msg, null);
        } catch (Exception e) {
            e.printStackTrace();
            log.d("合同关闭发生错误：", e);
            return ResultDto.error("合同关闭发生错误。");
        }
    }

    /**
     * 校验是否有在途流程
     *
     * @param id
     * @return
     */
    public ResultDto checkClose3(int id) {
        try {
            String msg = projectDao.checkClose3(id);
            return ResultDto.init(msg.length() == 0, msg, null);
        } catch (Exception e) {
            e.printStackTrace();
            log.d("校验发生错误：", e);
            return ResultDto.error("校验发生错误。");
        }
    }


    /**
     * 校验是否存在借款未核销的情况
     *
     * @param id
     * @return
     */
    public ResultDto checkClose4(int id) {
        try {
            String msg = projectDao.checkClose4(id);
            return ResultDto.init(msg.length() == 0, msg, null);
        } catch (Exception e) {
            e.printStackTrace();
            log.d("校验发生错误：", e);
            return ResultDto.error("校验发生错误。");
        }
    }

}
