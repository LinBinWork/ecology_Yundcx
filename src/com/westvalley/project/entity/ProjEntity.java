package com.westvalley.project.entity;

import com.westvalley.project.enums.CtrlLevelEnum;

/**
 * 项目共性
 */
public class ProjEntity {
    private int id;
    /**
     * 上级ID
     */
    private int pID;
    /**
     * 项目编码
     */
    private String projNo;
    /**
     * 项目部门
     */
    private String projDeptNo;
    /**
     * 项目名称
     */
    private String projName;
    /**
     * 项目描述
     */
    private String projDesc;
    /**
     * 项目期初金额
     */
    private double projAmt;
    /**
     * 项目在途/冻结金额
     */
    private double projFreezeAmt;
    /**
     * 项目已使用金额
     */
    private double projUsedAmt;
    /**
     * 项目剩余金额
     */
    private double projBalance;
    /**
     * 项目类型
     */
    private int projType;
    /**
     * 控制级别
     */
    private CtrlLevelEnum ctrlLevel;
    /**
     * 父项可用预算
     */
    private double parentBalance;

    /**项目类别*/
    private String projCategory;
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
     * 获取 项目部门
     *
     * @return projDeptNo 项目部门
     */
    public String getProjDeptNo() {
        return this.projDeptNo;
    }

    /**
     * 设置 项目部门
     *
     * @param projDeptNo 项目部门
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
     * 获取 项目类型
     *
     * @return projType 项目类型
     */
    public int getProjType() {
        return this.projType;
    }

    /**
     * 设置 项目类型
     *
     * @param projType 项目类型
     */
    public void setProjType(int projType) {
        this.projType = projType;
    }

    /**
     * 获取 父项可用预算
     *
     * @return parentBalance 父项可用预算
     */
    public double getParentBalance() {
        return this.parentBalance;
    }

    /**
     * 设置 父项可用预算
     *
     * @param parentBalance 父项可用预算
     */
    public void setParentBalance(double parentBalance) {
        this.parentBalance = parentBalance;
    }

    /**
     * 获取 项目类别
     *
     * @return projCategory 项目类别
     */
    public String getProjCategory() {
        return this.projCategory;
    }

    /**
     * 设置 项目类别
     *
     * @param projCategory 项目类别
     */
    public void setProjCategory(String projCategory) {
        this.projCategory = projCategory;
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
