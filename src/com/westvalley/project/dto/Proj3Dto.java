package com.westvalley.project.dto;

/**
 * 孙子项立项
 */
public class Proj3Dto {

    private String requestID;
    private String detailID;
    private String projNo;
    /**父项id*/
    private int pID ;
    /**项目部门*/
    private String projDeptNo;
    private String projName;
    private String projDesc;
    private String projAmt;
    /**项目创建日期*/
    private String creDate;
    /**项目创建时间*/
    private String creTime;
    /**项目创建人*/
    private String creUser;
    /**立项日期*/
    private String projDate;
    /**项目经理*/
    private String projManager;
    /**项目负责人*/
    private String projPerson;
    /**项目执行人员*/
    private String projExcuer;

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
     * 获取
     *
     * @return projNo
     */
    public String getProjNo() {
        return this.projNo;
    }

    /**
     * 设置
     *
     * @param projNo
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
     * 获取
     *
     * @return projAmt
     */
    public String getProjAmt() {
        return this.projAmt;
    }

    /**
     * 设置
     *
     * @param projAmt
     */
    public void setProjAmt(String projAmt) {
        this.projAmt = projAmt;
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
     * 获取 祖项id
     *
     * @return pID 祖项id
     */
    public int getPID() {
        return this.pID;
    }

    /**
     * 设置 祖项id
     *
     * @param pID 祖项id
     */
    public void setPID(int pID) {
        this.pID = pID;
    }

    /**
     * 获取
     *
     * @return projName
     */
    public String getProjName() {
        return this.projName;
    }

    /**
     * 设置
     *
     * @param projName
     */
    public void setProjName(String projName) {
        this.projName = projName;
    }

    /**
     * 获取
     *
     * @return projDesc
     */
    public String getProjDesc() {
        return this.projDesc;
    }

    /**
     * 设置
     *
     * @param projDesc
     */
    public void setProjDesc(String projDesc) {
        this.projDesc = projDesc;
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
}
