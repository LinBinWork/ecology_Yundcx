package com.westvalley.project.po;

import com.westvalley.project.enums.CtrlLevelEnum;

/**
 * 祖项数据库字段
 */
public class Proj0Po {
    /**公司代码*/
    private String companyCode;
    /**祖项编码*/
    private String orgCode;
    /**祖项名*/
    private String orgName;
    /**祖项名*/
    private String orgDesp;
    /**是否启用 0 是*/
    private String isOepn ;
    /**是否执行方案 0 是*/
    private String isPlan ;
    /**祖项类别<br>0 执行发起：父项立项完成后自动生成一条子项　<br>1执行审批：通过父项立项拆解生成子项*/
    private String orgType ;
    /**控制级别*/
    private CtrlLevelEnum ctrlLevel;


    /**
     * 获取 公司代码
     *
     * @return companyCode 公司代码
     */
    public String getCompanyCode() {
        return this.companyCode;
    }

    /**
     * 设置 公司代码
     *
     * @param companyCode 公司代码
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * 获取 祖项编码
     *
     * @return orgCode 祖项编码
     */
    public String getOrgCode() {
        return this.orgCode;
    }

    /**
     * 设置 祖项编码
     *
     * @param orgCode 祖项编码
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 获取 祖项名
     *
     * @return orgName 祖项名
     */
    public String getOrgName() {
        return this.orgName;
    }

    /**
     * 设置 祖项名
     *
     * @param orgName 祖项名
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 获取 祖项名
     *
     * @return orgDesp 祖项名
     */
    public String getOrgDesp() {
        return this.orgDesp;
    }

    /**
     * 设置 祖项名
     *
     * @param orgDesp 祖项名
     */
    public void setOrgDesp(String orgDesp) {
        this.orgDesp = orgDesp;
    }

    /**
     * 获取 是否启用 0 是
     *
     * @return isOepn 是否启用 0 是
     */
    public String getIsOepn() {
        return this.isOepn;
    }

    /**
     * 设置 是否启用 0 是
     *
     * @param isOepn 是否启用 0 是
     */
    public void setIsOepn(String isOepn) {
        this.isOepn = isOepn;
    }

    /**
     * 获取 是否执行方案 0 是
     *
     * @return isPlan 是否执行方案 0 是
     */
    public String getIsPlan() {
        return this.isPlan;
    }

    /**
     * 设置 是否执行方案 0 是
     *
     * @param isPlan 是否执行方案 0 是
     */
    public void setIsPlan(String isPlan) {
        this.isPlan = isPlan;
    }

    /**
     * 获取 祖项类别<br>0 执行发起：父项立项完成后自动生成一条子项　<br>1执行审批：通过父项立项拆解生成子项
     *
     * @return orgType 祖项类别<br>0 执行发起：父项立项完成后自动生成一条子项　<br>1执行审批：通过父项立项拆解生成子项
     */
    public String getOrgType() {
        return this.orgType;
    }

    /**
     * 设置 祖项类别<br>0 执行发起：父项立项完成后自动生成一条子项　<br>1执行审批：通过父项立项拆解生成子项
     *
     * @param orgType 祖项类别<br>0 执行发起：父项立项完成后自动生成一条子项　<br>1执行审批：通过父项立项拆解生成子项
     */
    public void setOrgType(String orgType) {
        this.orgType = orgType;
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
