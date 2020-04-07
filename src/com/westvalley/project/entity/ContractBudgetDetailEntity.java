package com.westvalley.project.entity;

/**
 * 合同相关信息
 */
public class ContractBudgetDetailEntity {
    /**项目ID*/
    private int projID;
    /**父项目类型 0祖项 1父项 2子项 3孙子项*/
    private int projType;

    private double freezAmt;


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
     * 获取 父项目类型 0祖项 1父项 2子项 3孙子项
     *
     * @return projType 父项目类型 0祖项 1父项 2子项 3孙子项
     */
    public int getProjType() {
        return this.projType;
    }

    /**
     * 设置 父项目类型 0祖项 1父项 2子项 3孙子项
     *
     * @param projType 父项目类型 0祖项 1父项 2子项 3孙子项
     */
    public void setProjType(int projType) {
        this.projType = projType;
    }

    /**
     * 获取
     *
     * @return freezAmt
     */
    public double getFreezAmt() {
        return this.freezAmt;
    }

    /**
     * 设置
     *
     * @param freezAmt
     */
    public void setFreezAmt(double freezAmt) {
        this.freezAmt = freezAmt;
    }
}
