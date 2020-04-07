package com.westvalley.project.po;

/**
 * 父项分摊信息
 */
public class Proj1PrecentPo {
    /**父项立项流程*/
    private String proj1RequestID;
    /**更新的流程*/
    private String creRequestID;
    /**父项ID*/
    private int proj1ID;
    /**追加的祖项ID*/
    private int proj0ID;
    /**追加时可用金额*/
    private double balance;
    /**分摊部门负责人*/
    private String person;
    /**币种*/
    private String ccy;
    /**汇率*/
    private double rate;
    /**追加金额*/
    private double useAmt;
    /**创建人*/
    private String creUser;
    /**备注*/
    private String remark;

    /**
     * 获取 父项立项流程
     *
     * @return proj1RequestID 父项立项流程
     */
    public String getProj1RequestID() {
        return this.proj1RequestID;
    }

    /**
     * 设置 父项立项流程
     *
     * @param proj1RequestID 父项立项流程
     */
    public void setProj1RequestID(String proj1RequestID) {
        this.proj1RequestID = proj1RequestID;
    }

    /**
     * 获取 更新的流程
     *
     * @return creRequestID 更新的流程
     */
    public String getCreRequestID() {
        return this.creRequestID;
    }

    /**
     * 设置 更新的流程
     *
     * @param creRequestID 更新的流程
     */
    public void setCreRequestID(String creRequestID) {
        this.creRequestID = creRequestID;
    }

    /**
     * 获取 父项ID
     *
     * @return proj1ID 父项ID
     */
    public int getProj1ID() {
        return this.proj1ID;
    }

    /**
     * 设置 父项ID
     *
     * @param proj1ID 父项ID
     */
    public void setProj1ID(int proj1ID) {
        this.proj1ID = proj1ID;
    }

    /**
     * 获取 追加的祖项ID
     *
     * @return proj0ID 追加的祖项ID
     */
    public int getProj0ID() {
        return this.proj0ID;
    }

    /**
     * 设置 追加的祖项ID
     *
     * @param proj0ID 追加的祖项ID
     */
    public void setProj0ID(int proj0ID) {
        this.proj0ID = proj0ID;
    }

    /**
     * 获取 追加时可用金额
     *
     * @return balance 追加时可用金额
     */
    public double getBalance() {
        return this.balance;
    }

    /**
     * 设置 追加时可用金额
     *
     * @param balance 追加时可用金额
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * 获取 分摊部门负责人
     *
     * @return person 分摊部门负责人
     */
    public String getPerson() {
        return this.person;
    }

    /**
     * 设置 分摊部门负责人
     *
     * @param person 分摊部门负责人
     */
    public void setPerson(String person) {
        this.person = person;
    }

    /**
     * 获取 币种
     *
     * @return ccy 币种
     */
    public String getCcy() {
        return this.ccy;
    }

    /**
     * 设置 币种
     *
     * @param ccy 币种
     */
    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    /**
     * 获取 汇率
     *
     * @return rate 汇率
     */
    public double getRate() {
        return this.rate;
    }

    /**
     * 设置 汇率
     *
     * @param rate 汇率
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * 获取 追加金额
     *
     * @return useAmt 追加金额
     */
    public double getUseAmt() {
        return this.useAmt;
    }

    /**
     * 设置 追加金额
     *
     * @param useAmt 追加金额
     */
    public void setUseAmt(double useAmt) {
        this.useAmt = useAmt;
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
}
