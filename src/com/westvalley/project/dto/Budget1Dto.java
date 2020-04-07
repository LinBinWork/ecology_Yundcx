package com.westvalley.project.dto;

import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.CtrlLevelEnum;

/**
 * 父项预算
 */
public class Budget1Dto {

    private String requestID;
    private String detailID;
    /**父ID*/
    private int pID;
    /**父项ID*/
    private int projID;
    /**项目编码*/
    private String projNo;
    /**部门编码 成本中心*/
    private String projDeptNo;
    /**预算使用类型*/
    private BudTypeEnum useType;
    /**控制级别*/
    private CtrlLevelEnum ctrlLevel;
    /**使用预算金额*/
    private double useAmt;
    /**项目创建日期*/
    private String creDate;
    /**项目创建时间*/
    private String creTime;
    /**项目创建人*/
    private String creUser;
    /**项目归档日期*/
    private String modDate;
    /**项目归档时间*/
    private String modTime;
    /**项目拆解  拆解流程专用 控制级别为父项 其他流程无效*/
    private String split;
    /**是否自动创建 0是*/
    private String autoCre;

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
     * 获取 父ID
     *
     * @return pID 父ID
     */
    public int getPID() {
        return this.pID;
    }

    /**
     * 设置 父ID
     *
     * @param pID 父ID
     */
    public void setPID(int pID) {
        this.pID = pID;
    }

    /**
     * 获取 父项ID
     *
     * @return projID 父项ID
     */
    public int getProjID() {
        return this.projID;
    }

    /**
     * 设置 父项ID
     *
     * @param projID 父项ID
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
     * 获取 部门编码 成本中心
     *
     * @return projDeptNo 部门编码 成本中心
     */
    public String getProjDeptNo() {
        return this.projDeptNo;
    }

    /**
     * 设置 部门编码 成本中心
     *
     * @param projDeptNo 部门编码 成本中心
     */
    public void setProjDeptNo(String projDeptNo) {
        this.projDeptNo = projDeptNo;
    }

    /**
     * 获取 预算使用类型
     *
     * @return useType 预算使用类型
     */
    public BudTypeEnum getUseType() {
        return this.useType;
    }

    /**
     * 设置 预算使用类型
     *
     * @param useType 预算使用类型
     */
    public void setUseType(BudTypeEnum useType) {
        this.useType = useType;
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

    /**
     * 获取 使用预算金额
     *
     * @return useAmt 使用预算金额
     */
    public double getUseAmt() {
        return this.useAmt;
    }

    /**
     * 设置 使用预算金额
     *
     * @param useAmt 使用预算金额
     */
    public void setUseAmt(double useAmt) {
        this.useAmt = useAmt;
    }

    /**
     * 获取 项目创建日期
     *
     * @return creDate 项目创建日期
     */
    public String getCreDate() {
        return this.creDate;
    }

    /**
     * 设置 项目创建日期
     *
     * @param creDate 项目创建日期
     */
    public void setCreDate(String creDate) {
        this.creDate = creDate;
    }

    /**
     * 获取 项目创建时间
     *
     * @return creTime 项目创建时间
     */
    public String getCreTime() {
        return this.creTime;
    }

    /**
     * 设置 项目创建时间
     *
     * @param creTime 项目创建时间
     */
    public void setCreTime(String creTime) {
        this.creTime = creTime;
    }

    /**
     * 获取 项目创建人
     *
     * @return creUser 项目创建人
     */
    public String getCreUser() {
        return this.creUser;
    }

    /**
     * 设置 项目创建人
     *
     * @param creUser 项目创建人
     */
    public void setCreUser(String creUser) {
        this.creUser = creUser;
    }

    /**
     * 获取 项目归档日期
     *
     * @return modDate 项目归档日期
     */
    public String getModDate() {
        return this.modDate;
    }

    /**
     * 设置 项目归档日期
     *
     * @param modDate 项目归档日期
     */
    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    /**
     * 获取 项目归档时间
     *
     * @return modTime 项目归档时间
     */
    public String getModTime() {
        return this.modTime;
    }

    /**
     * 设置 项目归档时间
     *
     * @param modTime 项目归档时间
     */
    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    /**
     * 获取 项目拆解  拆解流程专用 其他流程无效
     *
     * @return split 项目拆解  拆解流程专用 其他流程无效
     */
    public String getSplit() {
        return this.split;
    }

    /**
     * 设置 项目拆解  拆解流程专用 其他流程无效
     *
     * @param split 项目拆解  拆解流程专用 其他流程无效
     */
    public void setSplit(String split) {
        this.split = split;
    }

    /**
     * 获取 是否自动创建 0是
     *
     * @return autoCre 是否自动创建 0是
     */
    public String getAutoCre() {
        return this.autoCre;
    }

    /**
     * 设置 是否自动创建 0是
     *
     * @param autoCre 是否自动创建 0是
     */
    public void setAutoCre(String autoCre) {
        this.autoCre = autoCre;
    }
}
