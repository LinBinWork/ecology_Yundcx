package com.westvalley.project.dto;

import com.westvalley.project.enums.CtrlLevelEnum;

/**
 * 孙子项立项
 */
public class ProjDto {


    /**请求id*/
    private String reqID;
    private String detailID;
    /**父id*/
    private int pID ;
    /**公司编码*/
    private String companyNo;
    /**项目编码*/
    private String projNo;
    /**项目部门*/
    private String projDeptNo;
    /**项目名称*/
    private String projName;
    /**项目描述*/
    private String projDesc;
    /**项目类型 0祖项 1父项 2子项 3孙子项*/
    private int projType;
    /**项目初始金额*/
    private String projAmt;
    /**项目年度*/
    private int projYear;
    /**控制级别*/
    private CtrlLevelEnum ctrlLevel;
    /**立项日期*/
    private String projDate;
    /**项目经理*/
    private String projManager;
    /**项目负责人*/
    private String projPerson;
    /**项目执行人员*/
    private String projExcuer;
    /**项目是否可执行方案*/
    private String ProjExcustatus;
    /**项目类别*/
    private String projCategory;
    /**是否交付中心处理*/
    private int deliveryCenter;
    /**项目创建日期*/
    private String creDate;
    /**项目创建时间*/
    private String creTime;
    /**项目创建人*/
    private String creUser;
    /**项目释放金额*/
    private String releaseAmt;
    /**项目修改日期*/
    private String modDate;
    /**项目修改时间*/
    private String modTime;
    /**项目备注*/
    private String remark;
    /**业务类别*/
    private String businessType;


    /**
     * 获取 项目类别
     *
     * @return projCategory 项目类别
     */
    public String getProjCategory() {
        return projCategory;
    }
    /**
     * 获取 项目类别
     *
     * @return projCategory 项目类别
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
        return deliveryCenter;
    }

    /**
     * 设置 是否交付中心处理
     *
     * @param deliveryCenter 是否交付中心处理
     */
    public void setDeliveryCenter(int deliveryCenter) {
        this.deliveryCenter = deliveryCenter;
    }

    /**
     * 获取 请求id
     *
     * @return reqID 请求id
     */
    public String getReqID() {
        return this.reqID;
    }

    /**
     * 设置 请求id
     *
     * @param reqID 请求id
     */
    public void setReqID(String reqID) {
        this.reqID = reqID;
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
     * 获取 父id
     *
     * @return pID 父id
     */
    public int getPID() {
        return this.pID;
    }

    /**
     * 设置 父id
     *
     * @param pID 父id
     */
    public void setPID(int pID) {
        this.pID = pID;
    }

    /**
     * 获取 公司编码
     *
     * @return companyNo 公司编码
     */
    public String getCompanyNo() {
        return this.companyNo;
    }

    /**
     * 设置 公司编码
     *
     * @param companyNo 公司编码
     */
    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
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
     * 获取 项目初始金额
     *
     * @return projAmt 项目初始金额
     */
    public String getProjAmt() {
        return this.projAmt;
    }

    /**
     * 设置 项目初始金额
     *
     * @param projAmt 项目初始金额
     */
    public void setProjAmt(String projAmt) {
        this.projAmt = projAmt;
    }

    /**
     * 获取 项目年度
     *
     * @return projYear 项目年度
     */
    public int getProjYear() {
        return this.projYear;
    }

    /**
     * 设置 项目年度
     *
     * @param projYear 项目年度
     */
    public void setProjYear(int projYear) {
        this.projYear = projYear;
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
     * 获取 项目释放金额
     *
     * @return releaseAmt 项目释放金额
     */
    public String getReleaseAmt() {
        return this.releaseAmt;
    }

    /**
     * 设置 项目释放金额
     *
     * @param releaseAmt 项目释放金额
     */
    public void setReleaseAmt(String releaseAmt) {
        this.releaseAmt = releaseAmt;
    }

    /**
     * 获取 项目修改日期
     *
     * @return modDate 项目修改日期
     */
    public String getModDate() {
        return this.modDate;
    }

    /**
     * 设置 项目修改日期
     *
     * @param modDate 项目修改日期
     */
    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    /**
     * 获取 项目修改时间
     *
     * @return modTime 项目修改时间
     */
    public String getModTime() {
        return this.modTime;
    }

    /**
     * 设置 项目修改时间
     *
     * @param modTime 项目修改时间
     */
    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    /**
     * 获取 项目备注
     *
     * @return remark 项目备注
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * 设置 项目备注
     *
     * @param remark 项目备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取 立项日期
     *
     * @return projDate 立项日期
     */
    public String getProjDate() {
        return this.projDate;
    }

    /**
     * 设置 立项日期
     *
     * @param projDate 立项日期
     */
    public void setProjDate(String projDate) {
        this.projDate = projDate;
    }

    /**
     * 获取 项目经理
     *
     * @return projManager 项目经理
     */
    public String getProjManager() {
        return this.projManager;
    }

    /**
     * 设置 项目经理
     *
     * @param projManager 项目经理
     */
    public void setProjManager(String projManager) {
        this.projManager = projManager;
    }

    /**
     * 获取 项目负责人
     *
     * @return projPerson 项目负责人
     */
    public String getProjPerson() {
        return this.projPerson;
    }

    /**
     * 设置 项目负责人
     *
     * @param projPerson 项目负责人
     */
    public void setProjPerson(String projPerson) {
        this.projPerson = projPerson;
    }

    /**
     * 获取 项目执行人员
     *
     * @return projExcuer 项目执行人员
     */
    public String getProjExcuer() {
        return this.projExcuer;
    }

    /**
     * 设置 项目执行人员
     *
     * @param projExcuer 项目执行人员
     */
    public void setProjExcuer(String projExcuer) {
        this.projExcuer = projExcuer;
    }

    /**
     * 获取 项目是否可执行方案
     *
     * @return ProjExcustatus 项目是否可执行方案
     */
    public String getProjExcustatus() {
        return this.ProjExcustatus;
    }

    /**
     * 设置 项目是否可执行方案
     *
     * @param ProjExcustatus 项目是否可执行方案
     */
    public void setProjExcustatus(String ProjExcustatus) {
        this.ProjExcustatus = ProjExcustatus;
    }

    /**
     * 获取 业务类别
     *
     * @return businessType 业务类别
     */
    public String getBusinessType() {
        return this.businessType;
    }

    /**
     * 设置 业务类别
     *
     * @param businessType 业务类别
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
