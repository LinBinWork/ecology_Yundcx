package com.westvalley.project.po;

import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.ExpTypeEnum;
import com.westvalley.project.enums.ProjtypeEnum;

/**
 * 预算结算
 */
public class BudgetExcuPo {
    /**执行流程ID*/
    private String requestID;
    /**执行明细ID*/
    private String detailID;
    /**项目ID*/
    private int projID;
    /**项目编号*/
    private String projNo;
    /**项目成本中心*/
    private String projDeptNo;
    /**项目类型*/
    private ProjtypeEnum projtype;
    /**预算类型*/
    private BudTypeEnum useType;
    /**预算结算类型*/
    private ExpTypeEnum expType;
    /**拆解状态*/
    private String splitStatu;
    /**结算金额*/
    private double useAmt;
    /**结算日期*/
    private String creDate;
    /**结算时间*/
    private String creTime;
    /**创建人*/
    private String creUser;
    /**备注*/
    private String remark;
    /**是否自动创建 0是*/
    private String autoCre;


    /**
     * 获取 执行流程ID
     *
     * @return requestID 执行流程ID
     */
    public String getRequestID() {
        return this.requestID;
    }

    /**
     * 设置 执行流程ID
     *
     * @param requestID 执行流程ID
     */
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    /**
     * 获取 执行明细ID
     *
     * @return detailID 执行明细ID
     */
    public String getDetailID() {
        return this.detailID;
    }

    /**
     * 设置 执行明细ID
     *
     * @param detailID 执行明细ID
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
     * 获取 项目编号
     *
     * @return projNo 项目编号
     */
    public String getProjNo() {
        return this.projNo;
    }

    /**
     * 设置 项目编号
     *
     * @param projNo 项目编号
     */
    public void setProjNo(String projNo) {
        this.projNo = projNo;
    }

    /**
     * 获取 项目成本中心
     *
     * @return projDeptNo 项目成本中心
     */
    public String getProjDeptNo() {
        return this.projDeptNo;
    }

    /**
     * 设置 项目成本中心
     *
     * @param projDeptNo 项目成本中心
     */
    public void setProjDeptNo(String projDeptNo) {
        this.projDeptNo = projDeptNo;
    }

    /**
     * 获取 项目类型
     *
     * @return projtype 项目类型
     */
    public ProjtypeEnum getProjtype() {
        return this.projtype;
    }

    /**
     * 设置 项目类型
     *
     * @param projtype 项目类型
     */
    public void setProjtype(ProjtypeEnum projtype) {
        this.projtype = projtype;
    }

    /**
     * 获取 预算类型
     *
     * @return useType 预算类型
     */
    public BudTypeEnum getUseType() {
        return this.useType;
    }

    /**
     * 设置 预算类型
     *
     * @param useType 预算类型
     */
    public void setUseType(BudTypeEnum useType) {
        this.useType = useType;
    }

    /**
     * 获取 预算结算类型
     *
     * @return expType 预算结算类型
     */
    public ExpTypeEnum getExpType() {
        return this.expType;
    }

    /**
     * 设置 预算结算类型
     *
     * @param expType 预算结算类型
     */
    public void setExpType(ExpTypeEnum expType) {
        this.expType = expType;
    }

    /**
     * 获取 拆解状态
     *
     * @return splitStatu 拆解状态
     */
    public String getSplitStatu() {
        return this.splitStatu;
    }

    /**
     * 设置 拆解状态
     *
     * @param splitStatu 拆解状态
     */
    public void setSplitStatu(String splitStatu) {
        this.splitStatu = splitStatu;
    }

    /**
     * 获取 结算金额
     *
     * @return useAmt 结算金额
     */
    public double getUseAmt() {
        return this.useAmt;
    }

    /**
     * 设置 结算金额
     *
     * @param useAmt 结算金额
     */
    public void setUseAmt(double useAmt) {
        this.useAmt = useAmt;
    }

    /**
     * 获取 结算日期
     *
     * @return creDate 结算日期
     */
    public String getCreDate() {
        return this.creDate;
    }

    /**
     * 设置 结算日期
     *
     * @param creDate 结算日期
     */
    public void setCreDate(String creDate) {
        this.creDate = creDate;
    }

    /**
     * 获取 结算时间
     *
     * @return creTime 结算时间
     */
    public String getCreTime() {
        return this.creTime;
    }

    /**
     * 设置 结算时间
     *
     * @param creTime 结算时间
     */
    public void setCreTime(String creTime) {
        this.creTime = creTime;
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
