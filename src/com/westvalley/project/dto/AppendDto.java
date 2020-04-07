package com.westvalley.project.dto;

import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.ProjtypeEnum;

/**
 * 预算追加
 *
 * 子项和孙子项追加 只能追加控制级别为末端的
 *
 */
public class AppendDto {
    private String requestID;
    private String detailID;
    /**上级ID*/
    private int pID;
    /**追加项目ID*/
    private int projID;
    /**项目类型 0祖项 1父项 2子项 3孙子项*/
    private ProjtypeEnum projType;
    /**上级预算结余*/
    private double pidBalance;
    /**金额*/
    private double useAmt;
    /**创建人*/
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
     * 获取 追加项目ID
     *
     * @return projID 追加项目ID
     */
    public int getProjID() {
        return this.projID;
    }

    /**
     * 设置 追加项目ID
     *
     * @param projID 追加项目ID
     */
    public void setProjID(int projID) {
        this.projID = projID;
    }

    /**
     * 获取 被追加的项目类型 0祖项 1父项 2子项 3孙子项
     *
     * @return projType 被追加的项目类型 0祖项 1父项 2子项 3孙子项
     */
    public ProjtypeEnum getProjType() {
        return this.projType;
    }

    /**
     * 设置 被追加的项目类型 0祖项 1父项 2子项 3孙子项
     *
     * @param projType 被追加的项目类型 0祖项 1父项 2子项 3孙子项
     */
    public void setProjType(ProjtypeEnum projType) {
        this.projType = projType;
    }

    /**
     * 获取 释放金额
     *
     * @return useAmt 释放金额
     */
    public double getUseAmt() {
        return this.useAmt;
    }

    /**
     * 设置 释放金额
     *
     * @param useAmt 释放金额
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
     * 获取 上级预算结余
     *
     * @return pidBalance 上级预算结余
     */
    public double getPidBalance() {
        return this.pidBalance;
    }

    /**
     * 设置 上级预算结余
     *
     * @param pidBalance 上级预算结余
     */
    public void setPidBalance(double pidBalance) {
        this.pidBalance = pidBalance;
    }
}
