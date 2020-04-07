package com.westvalley.project.dto;

/**
 * 项目权限
 */
public class PermissionDto {
    /**项目ID*/
    private int projID;
    /**项目编码*/
    private String projNo;
    /**项目可拆解人员*/
    private String projSplits;
    /**项目可报销人员*/
    private String projPersons;
    /**项目经理，创建权限*/
    private String projManager;

    /**
     * 获取 项目ID
     *
     * @return projID 项目ID
     */
    public int getProjID() {
        return this.projID;
    }

    /**
     * 设置 项目ID
     *
     * @param projID 项目ID
     */
    public void setProjID(int projID) {
        this.projID = projID;
    }

    /**
     * 获取 项目编码
     *
     * @return projNo 项目编码
     */
    public String getProjNo() {
        return this.projNo;
    }

    /**
     * 设置 项目编码
     *
     * @param projNo 项目编码
     */
    public void setProjNo(String projNo) {
        this.projNo = projNo;
    }

    /**
     * 获取 项目可拆解人员
     *
     * @return projSplits 项目可拆解人员
     */
    public String getProjSplits() {
        return this.projSplits;
    }

    /**
     * 设置 项目可拆解人员
     *
     * @param projSplits 项目可拆解人员
     */
    public void setProjSplits(String projSplits) {
        this.projSplits = projSplits;
    }

    /**
     * 获取 项目可报销人员
     *
     * @return projPersons 项目可报销人员
     */
    public String getProjPersons() {
        return this.projPersons;
    }

    /**
     * 设置 项目可报销人员
     *
     * @param projPersons 项目可报销人员
     */
    public void setProjPersons(String projPersons) {
        this.projPersons = projPersons;
    }

    /**
     * 获取 项目经理，创建权限
     *
     * @return projManager 项目经理，创建权限
     */
    public String getProjManager() {
        return this.projManager;
    }

    /**
     * 设置 项目经理，创建权限
     *
     * @param projManager 项目经理，创建权限
     */
    public void setProjManager(String projManager) {
        this.projManager = projManager;
    }
}
