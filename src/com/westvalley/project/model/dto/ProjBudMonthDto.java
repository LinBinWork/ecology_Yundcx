package com.westvalley.project.model.dto;

/**
 * 月度预算数据
 */
public class ProjBudMonthDto {

    /**月份*/
    private int month;
    /**月度预算金额*/
    private String budTotalAmt;
    /**月度执行金额*/
    private String budExcuAmt;
    /**超支+/节约-*/
    private String calcAmt;
    /**备注*/
    private String remark;

    /**
     * 获取 月度预算金额
     *
     * @return budTotalAmt 月度预算金额
     */
    public String getBudTotalAmt() {
        return this.budTotalAmt;
    }

    /**
     * 设置 月度预算金额
     *
     * @param budTotalAmt 月度预算金额
     */
    public void setBudTotalAmt(String budTotalAmt) {
        this.budTotalAmt = budTotalAmt;
    }

    /**
     * 获取 月度执行金额
     *
     * @return budExcuAmt 月度执行金额
     */
    public String getBudExcuAmt() {
        return this.budExcuAmt;
    }

    /**
     * 设置 月度执行金额
     *
     * @param budExcuAmt 月度执行金额
     */
    public void setBudExcuAmt(String budExcuAmt) {
        this.budExcuAmt = budExcuAmt;
    }

    /**
     * 获取 超支+节约-
     *
     * @return calcAmt 超支+节约-
     */
    public String getCalcAmt() {
        return this.calcAmt;
    }

    /**
     * 设置 超支+节约-
     *
     * @param calcAmt 超支+节约-
     */
    public void setCalcAmt(String calcAmt) {
        this.calcAmt = calcAmt;
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
     * 获取 月份
     *
     * @return month 月份
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * 设置 月份
     *
     * @param month 月份
     */
    public void setMonth(int month) {
        this.month = month;
    }
}
