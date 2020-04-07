package com.westvalley.project.model.dto;

import java.util.List;

/**
 * 营销项目预算额度汇总表  主表数据
 */
public class ProjBudAllDto {

    /**公司代码*/
    private String companyNo;
    /**业务类别*/
    private String businessType;
    /**祖项编码*/
    private String proj0No;
    /**祖项名称*/
    private String proj0Name;
    /**部门编码*/
    private String projDeptNo;
    /**部门名称*/
    private String projDeptName;
    /**年度预算合计*/
    private String yearBudAmt;
    /**年度达成率   monGrandExcuAmt ÷ yearBudAmt */
    private String yearAchRate;
    /**月度预算合计*/
    private String monGrandTotalAmt;
    /**月度执行合计*/
    private String monGrandExcuAmt;
    /**累计超支+/节约-*/
    private String calcAmt;
    /**月度数据*/
    private List<ProjBudMonthDto> projBudMonthDtoList;


    /**
     * 获取 公司代码
     *
     * @return companyNo 公司代码
     */
    public String getCompanyNo() {
        return this.companyNo;
    }

    /**
     * 设置 公司代码
     *
     * @param companyNo 公司代码
     */
    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
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

    /**
     * 获取 祖项编码
     *
     * @return proj0No 祖项编码
     */
    public String getProj0No() {
        return this.proj0No;
    }

    /**
     * 设置 祖项编码
     *
     * @param proj0No 祖项编码
     */
    public void setProj0No(String proj0No) {
        this.proj0No = proj0No;
    }

    /**
     * 获取 祖项名称
     *
     * @return proj0Name 祖项名称
     */
    public String getProj0Name() {
        return this.proj0Name;
    }

    /**
     * 设置 祖项名称
     *
     * @param proj0Name 祖项名称
     */
    public void setProj0Name(String proj0Name) {
        this.proj0Name = proj0Name;
    }

    /**
     * 获取 年度预算合计
     *
     * @return yearBudAmt 年度预算合计
     */
    public String getYearBudAmt() {
        return this.yearBudAmt;
    }

    /**
     * 设置 年度预算合计
     *
     * @param yearBudAmt 年度预算合计
     */
    public void setYearBudAmt(String yearBudAmt) {
        this.yearBudAmt = yearBudAmt;
    }

    /**
     * 获取 年度达成率   monGrandExcuAmt ÷ yearBudAmt
     *
     * @return yearAchRate 年度达成率   monGrandExcuAmt ÷ yearBudAmt
     */
    public String getYearAchRate() {
        return this.yearAchRate;
    }

    /**
     * 设置 年度达成率   monGrandExcuAmt ÷ yearBudAmt
     *
     * @param yearAchRate 年度达成率   monGrandExcuAmt ÷ yearBudAmt
     */
    public void setYearAchRate(String yearAchRate) {
        this.yearAchRate = yearAchRate;
    }

    /**
     * 获取 月度预算合计
     *
     * @return monGrandTotalAmt 月度预算合计
     */
    public String getMonGrandTotalAmt() {
        return this.monGrandTotalAmt;
    }

    /**
     * 设置 月度预算合计
     *
     * @param monGrandTotalAmt 月度预算合计
     */
    public void setMonGrandTotalAmt(String monGrandTotalAmt) {
        this.monGrandTotalAmt = monGrandTotalAmt;
    }

    /**
     * 获取 月度执行合计
     *
     * @return monGrandExcuAmt 月度执行合计
     */
    public String getMonGrandExcuAmt() {
        return this.monGrandExcuAmt;
    }

    /**
     * 设置 月度执行合计
     *
     * @param monGrandExcuAmt 月度执行合计
     */
    public void setMonGrandExcuAmt(String monGrandExcuAmt) {
        this.monGrandExcuAmt = monGrandExcuAmt;
    }

    /**
     * 获取 累计超支+节约-
     *
     * @return calcAmt 累计超支+节约-
     */
    public String getCalcAmt() {
        return this.calcAmt;
    }

    /**
     * 设置 累计超支+节约-
     *
     * @param calcAmt 累计超支+节约-
     */
    public void setCalcAmt(String calcAmt) {
        this.calcAmt = calcAmt;
    }

    /**
     * 获取 月度数据
     *
     * @return projBudMonthDtoList 月度数据
     */
    public List<ProjBudMonthDto> getProjBudMonthDtoList() {
        return this.projBudMonthDtoList;
    }

    /**
     * 设置 月度数据
     *
     * @param projBudMonthDtoList 月度数据
     */
    public void setProjBudMonthDtoList(List<ProjBudMonthDto> projBudMonthDtoList) {
        this.projBudMonthDtoList = projBudMonthDtoList;
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
     * 获取 部门名称
     *
     * @return projDeptName 部门名称
     */
    public String getProjDeptName() {
        return this.projDeptName;
    }

    /**
     * 设置 部门名称
     *
     * @param projDeptName 部门名称
     */
    public void setProjDeptName(String projDeptName) {
        this.projDeptName = projDeptName;
    }
}
