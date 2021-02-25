package com.westvalley.project.entity;

import com.westvalley.project.enums.CtrlLevelEnum;

/**
 * 子项
 */
public class Proj2Entity {
    private int id;
    /**父项ID*/
    private int pID;
    /**项目编码*/
    private String projNo;
    /**部门编码*/
    private String projDeptNo;
    /**项目名称*/
    private String projName;
    /**项目描述*/
    private String projDesc;
    /**项目期初金额*/
    private double projAmt;
    /**项目在途/冻结金额*/
    private double projFreezeAmt;
    /**项目增加金额*/
    private double projAddAmt;
    /**项目已使用金额*/
    private double projUsedAmt;
    /**项目剩余金额*/
    private double projBalance;
    /**控制级别*/
    private CtrlLevelEnum ctrlLevel;
    /**项目创建日期*/
    private String creDate;
    /**项目创建时间*/
    private String creTime;
    /**项目创建人*/
    private String creUser;
    /**是否交付中心处理*/
    private int deliveryCenter;

    /**
     * 获取
     *
     * @return id
     */
    public int getId() {
        return this.id;
    }

    /**
     * 设置
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取 父项ID
     *
     * @return pID 父项ID
     */
    public int getPID() {
        return this.pID;
    }

    /**
     * 设置 父项ID
     *
     * @param pID 父项ID
     */
    public void setPID(int pID) {
        this.pID = pID;
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
     * 获取 部门编码
     *
     * @return projDeptNo 部门编码
     */
    public String getProjDeptNo() {
        return this.projDeptNo;
    }

    /**
     * 设置 部门编码
     *
     * @param projDeptNo 部门编码
     */
    public void setProjDeptNo(String projDeptNo) {
        this.projDeptNo = projDeptNo;
    }

    /**
     * 获取 项目名称
     *
     * @return projName 项目名称
     */
    public String getProjName() {
        return this.projName;
    }

    /**
     * 设置 项目名称
     *
     * @param projName 项目名称
     */
    public void setProjName(String projName) {
        this.projName = projName;
    }

    /**
     * 获取 项目描述
     *
     * @return projDesc 项目描述
     */
    public String getProjDesc() {
        return this.projDesc;
    }

    /**
     * 设置 项目描述
     *
     * @param projDesc 项目描述
     */
    public void setProjDesc(String projDesc) {
        this.projDesc = projDesc;
    }

    /**
     * 获取 项目期初金额
     *
     * @return projAmt 项目期初金额
     */
    public double getProjAmt() {
        return this.projAmt;
    }

    /**
     * 设置 项目期初金额
     *
     * @param projAmt 项目期初金额
     */
    public void setProjAmt(double projAmt) {
        this.projAmt = projAmt;
    }

    /**
     * 获取 项目在途冻结金额
     *
     * @return projFreezeAmt 项目在途冻结金额
     */
    public double getProjFreezeAmt() {
        return this.projFreezeAmt;
    }

    /**
     * 设置 项目在途冻结金额
     *
     * @param projFreezeAmt 项目在途冻结金额
     */
    public void setProjFreezeAmt(double projFreezeAmt) {
        this.projFreezeAmt = projFreezeAmt;
    }

    /**
     * 获取 项目已使用金额
     *
     * @return projUsedAmt 项目已使用金额
     */
    public double getProjUsedAmt() {
        return this.projUsedAmt;
    }

    /**
     * 设置 项目已使用金额
     *
     * @param projUsedAmt 项目已使用金额
     */
    public void setProjUsedAmt(double projUsedAmt) {
        this.projUsedAmt = projUsedAmt;
    }

    public double getProjAddAmt() {
        return projAddAmt;
    }

    public void setProjAddAmt(double projAddAmt) {
        this.projAddAmt = projAddAmt;
    }

    /**
     * 获取 项目剩余金额
     *
     * @return projBalance 项目剩余金额
     */
    public double getProjBalance() {
        return this.projBalance;
    }

    /**
     * 设置 项目剩余金额
     *
     * @param projBalance 项目剩余金额
     */
    public void setProjBalance(double projBalance) {
        this.projBalance = projBalance;
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
     * 获取 是否交付中心处理
     *
     * @return deliveryCenter 是否交付中心处理
     */
    public int getDeliveryCenter() {
        return this.deliveryCenter;
    }

    /**
     * 设置 是否交付中心处理
     *
     * @param deliveryCenter 是否交付中心处理
     */
    public void setDeliveryCenter(int deliveryCenter) {
        this.deliveryCenter = deliveryCenter;
    }
}
