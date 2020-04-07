package com.westvalley.project.dto;

import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;

/**
 * 合同相关预算扣减
 */
public class ContractDto {
    private String requestID;
    private String detailID;
    /**项目ID*/
    private int projID;
    /**父项目类型 0祖项 1父项 2子项 3孙子项*/
    private int projType;
    /**预算使用类型 CONTRACT_FREZEE CONTRACT_USED*/
    private BudTypeEnum useType;
    /**控制级别*/
    private CtrlLevelEnum ctrlLevel;
    /**使用金额*/
    private double useAmt;
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
     * 获取 预算使用类型 CONTRACT_FREZEE CONTRACT_USED
     *
     * @return useType 预算使用类型 CONTRACT_FREZEE CONTRACT_USED
     */
    public BudTypeEnum getUseType() {
        return this.useType;
    }

    /**
     * 设置 预算使用类型 CONTRACT_FREZEE CONTRACT_USED
     *
     * @param useType 预算使用类型 CONTRACT_FREZEE CONTRACT_USED
     */
    public void setUseType(BudTypeEnum useType) {
        this.useType = useType;
    }

    /**
     * 获取 使用金额
     *
     * @return useAmt 使用金额
     */
    public double getUseAmt() {
        return this.useAmt;
    }

    /**
     * 设置 使用金额
     *
     * @param useAmt 使用金额
     */
    public void setUseAmt(double useAmt) {
        this.useAmt = useAmt;
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
     * 获取 控制级别
     *
     * @return ctrlLevel 控制级别
     */
    public CtrlLevelEnum getCtrlLevel() {
        return this.ctrlLevel;
    }

    /**
     * 设置 控制级别
     *
     * @param ctrlLevel 控制级别
     */
    public void setCtrlLevel(CtrlLevelEnum ctrlLevel) {
        this.ctrlLevel = ctrlLevel;
    }
}
