package com.westvalley.entity;

/**
 * 项目预算实体类
 */
public class BudgetEntity {

    private int id;//数据id
    private String reqID;//请求id
    private int detailID;//明细id
    private int pID;//父id
    private String companyNo;//公司编码
    private String businessType;//业务类别
    private String projNo;//项目编码
    private String projDeptNo;//部门编码
    private String projName;//项目名称
    private String projDesc;//项目描述
    private String projType;//项目类型
    private double projAmt;//初始金额
    private int projYear;//年份
    private String ctrlLevel;//控制级别
    private String creDate;//创建日期
    private String creTime;//创建时间
    private String creUser;//创建人
    private double releaseAmt;//释放金额
    private String modDate;//修改日期
    private String modTime;//修改时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getProjNo() {
        return projNo;
    }

    public void setProjNo(String projNo) {
        this.projNo = projNo;
    }

    public String getProjDeptNo() {
        return projDeptNo;
    }

    public void setProjDeptNo(String projDeptNo) {
        this.projDeptNo = projDeptNo;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjDesc() {
        return projDesc;
    }

    public void setProjDesc(String projDesc) {
        this.projDesc = projDesc;
    }

    public String getProjType() {
        return projType;
    }

    public void setProjType(String projType) {
        this.projType = projType;
    }

    public double getProjAmt() {
        return projAmt;
    }

    public void setProjAmt(double projAmt) {
        this.projAmt = projAmt;
    }

    public int getProjYear() {
        return projYear;
    }

    public void setProjYear(int projYear) {
        this.projYear = projYear;
    }

    public String getCtrlLevel() {
        return ctrlLevel;
    }

    public void setCtrlLevel(String ctrlLevel) {
        this.ctrlLevel = ctrlLevel;
    }

    public String getCreDate() {
        return creDate;
    }

    public void setCreDate(String creDate) {
        this.creDate = creDate;
    }

    public String getCreTime() {
        return creTime;
    }

    public void setCreTime(String creTime) {
        this.creTime = creTime;
    }

    public String getCreUser() {
        return creUser;
    }

    public void setCreUser(String creUser) {
        this.creUser = creUser;
    }

    public double getReleaseAmt() {
        return releaseAmt;
    }

    public void setReleaseAmt(double releaseAmt) {
        this.releaseAmt = releaseAmt;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public String getModTime() {
        return modTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }
}
