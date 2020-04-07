package com.westvalley.project.dto;

import com.sun.xml.internal.bind.v2.model.core.ID;
import com.westvalley.project.enums.BudTypeEnum;

import java.util.List;

/**
 * 预算释放
 */
public class ReleaseDto {
    private String requestID;
    private String detailID;
    /**上级ID*/
    @Deprecated
    private int pID;
    /**释放的项目ID*/
    private int projID;
    /**项目类型 0祖项 1父项 2子项 3孙子项*/
    private int projType;
    /**预算使用类型 0冻结 2释放*/
    private BudTypeEnum useType;
    /**释放金额*/
    private double useAmt;
    /**创建日期*/
    private String creDate;
    /**创建时间*/
    private String creTime;
    /**创建人*/
    private String creUser;
    /**备注*/
    private String remark;

    /**交付中心使用的预算，如果不是交付中心，则不用添加*/
    private List<Budget23Dto> budget23DtoList;


    /**
     * 获取
     *
     * @return requestID
     */
    public String getRequestID() {
        return this.requestID;
    }

    /**
     * 设置
     *
     * @param requestID
     */
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    /**
     * 获取
     *
     * @return detailID
     */
    public String getDetailID() {
        return this.detailID;
    }

    /**
     * 设置
     *
     * @param detailID
     */
    public void setDetailID(String detailID) {
        this.detailID = detailID;
    }

    /**
     * 获取 释放的项目ID
     *
     * @return projID 释放的项目ID
     */
    public int getProjID() {
        return this.projID;
    }

    /**
     * 设置 释放的项目ID
     *
     * @param projID 释放的项目ID
     */
    public void setProjID(int projID) {
        this.projID = projID;
    }

    /**
     * 获取 项目类型 0祖项 1父项 2子项 3孙子项
     *
     * @return projType 项目类型 0祖项 1父项 2子项 3孙子项
     */
    public int getProjType() {
        return this.projType;
    }

    /**
     * 设置 项目类型 0祖项 1父项 2子项 3孙子项
     *
     * @param projType 项目类型 0祖项 1父项 2子项 3孙子项
     */
    public void setProjType(int projType) {
        this.projType = projType;
    }

    /**
     * 获取 预算使用类型 0冻结 1调拨
     *
     * @return useType 预算使用类型 0冻结 1调拨
     */
    public BudTypeEnum getUseType() {
        return this.useType;
    }

    /**
     * 设置 预算使用类型 0冻结 1调拨
     *
     * @param useType 预算使用类型 0冻结 1调拨
     */
    public void setUseType(BudTypeEnum useType) {
        this.useType = useType;
    }

    /**
     * 获取 释放金额
     *
     * @return useAmt 释放金额
     */
    public double getUseAmt() {
        return this.useAmt;
    }

    /**
     * 设置 释放金额
     *
     * @param useAmt 释放金额
     */
    public void setUseAmt(double useAmt) {
        this.useAmt = useAmt;
    }

    /**
     * 获取 创建日期
     *
     * @return creDate 创建日期
     */
    public String getCreDate() {
        return this.creDate;
    }

    /**
     * 设置 创建日期
     *
     * @param creDate 创建日期
     */
    public void setCreDate(String creDate) {
        this.creDate = creDate;
    }

    /**
     * 获取 创建时间
     *
     * @return creTime 创建时间
     */
    public String getCreTime() {
        return this.creTime;
    }

    /**
     * 设置 创建时间
     *
     * @param creTime 创建时间
     */
    public void setCreTime(String creTime) {
        this.creTime = creTime;
    }

    /**
     * 获取 创建人
     *
     * @return creUser 创建人
     */
    public String getCreUser() {
        return this.creUser;
    }

    /**
     * 设置 创建人
     *
     * @param creUser 创建人
     */
    public void setCreUser(String creUser) {
        this.creUser = creUser;
    }

    /**
     * 获取 备注
     *
     * @return remark 备注
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * 设置 备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取 上级ID
     *
     * @return pID 上级ID
     */
    public int getPID() {
        return this.pID;
    }

    /**
     * 设置 上级ID
     *
     * @param pID 上级ID
     */
    public void setPID(int pID) {
        this.pID = pID;
    }

    /**
     * 交付中心使用的预算，如果不是交付中心，则不用添加
     *
     * @return budget23DtoList 特别使用的预算
     */
    public List<Budget23Dto> getBudget23DtoList() {
        return this.budget23DtoList;
    }

    /**
     * 交付中心使用的预算，如果不是交付中心，则不用添加
     *
     * @param budget23DtoList 特别使用的预算
     */
    public void setBudget23DtoList(List<Budget23Dto> budget23DtoList) {
        this.budget23DtoList = budget23DtoList;
    }
}
