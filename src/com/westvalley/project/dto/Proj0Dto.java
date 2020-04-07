package com.westvalley.project.dto;

/**
 * 祖项年度预算导入
 */
public class Proj0Dto {

    private String requestID;
    private String detailID;
    private String projNo;
    /**项目部门*/
    private String projDeptNo;
    private String projName;//项目名称
    private String projDesc;
    private String projAmt;
    private String ywlb;//业务类别
    private int year;
    /**项目创建日期*/
    private String creDate;
    /**项目创建时间*/
    private String creTime;
    /**项目创建人*/
    private String creUser;


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

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
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
     * 获取
     *
     * @return year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * 设置
     *
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    public String getYwlb() {
        return ywlb;
    }

    public void setYwlb(String ywlb) {
        this.ywlb = ywlb;
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
}
