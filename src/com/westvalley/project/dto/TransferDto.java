package com.westvalley.project.dto;

import com.westvalley.project.enums.BudTypeEnum;

/**
 * 预算调拨
 */
public class TransferDto {
    private String requestID;
    private String detailID;
    /**调出上级ID*/
    private int outPID;
    /**调出ID*/
    private int outID;
    /**调入上级ID*/
    private int inPID;
    /**调入ID*/
    private int inID;
    /**项目类型 0祖项 1父项 2子项 3孙子项*/
    private int projType;
    /**预算使用类型 0冻结 1调拨 /*/
    private BudTypeEnum useType;
    /**调拨预算金额*/
    private double useAmt;
    /**创建日期*/
    private String creDate;
    /**创建时间*/
    private String creTime;
    /**创建人*/
    private String creUser;
    /**备注*/
    private String remark;


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
     * 获取 调出上级ID
     *
     * @return outPID 调出上级ID
     */
    public int getOutPID() {
        return this.outPID;
    }

    /**
     * 设置 调出上级ID
     *
     * @param outPID 调出上级ID
     */
    public void setOutPID(int outPID) {
        this.outPID = outPID;
    }

    /**
     * 获取 调出ID
     *
     * @return outID 调出ID
     */
    public int getOutID() {
        return this.outID;
    }

    /**
     * 设置 调出ID
     *
     * @param outID 调出ID
     */
    public void setOutID(int outID) {
        this.outID = outID;
    }

    /**
     * 获取 调入上级ID
     *
     * @return inPID 调入上级ID
     */
    public int getInPID() {
        return this.inPID;
    }

    /**
     * 设置 调入上级ID
     *
     * @param inPID 调入上级ID
     */
    public void setInPID(int inPID) {
        this.inPID = inPID;
    }

    /**
     * 获取 调入ID
     *
     * @return inID 调入ID
     */
    public int getInID() {
        return this.inID;
    }

    /**
     * 设置 调入ID
     *
     * @param inID 调入ID
     */
    public void setInID(int inID) {
        this.inID = inID;
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
     * 获取 预算使用类型 0冻结 1已调拨
     *
     * @return useType 预算使用类型 0冻结 1已调拨
     */
    public BudTypeEnum getUseType() {
        return this.useType;
    }

    /**
     * 设置 预算使用类型 0冻结 1已调拨
     *
     * @param useType 预算使用类型 0冻结 1已调拨
     */
    public void setUseType(BudTypeEnum useType) {
        this.useType = useType;
    }

    /**
     * 获取 调拨预算金额
     *
     * @return useAmt 调拨预算金额
     */
    public double getUseAmt() {
        return this.useAmt;
    }

    /**
     * 设置 调拨预算金额
     *
     * @param useAmt 调拨预算金额
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
}
