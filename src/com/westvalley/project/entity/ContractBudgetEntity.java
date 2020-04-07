package com.westvalley.project.entity;

import java.util.List;

/**
 * 合同相关信息
 */
public class ContractBudgetEntity {
    /**合同流程编号*/
    private String contractRequestID;
    /**合同编码*/
    private String contractNo;
    /**合同名称*/
    private String contractName;
    /**合同期初金额*/
    private double contractAmt;
    /**合同在途/冻结金额*/
    private double contractFreezeAmt;
    /**合同已使用金额*/
    private double contractUsedAmt;
    /**合同剩余金额*/
    private double contractBalance;

    private List<ContractBudgetDetailEntity> detailEntityList;

    /**
     * 获取 合同流程编号
     *
     * @return contractRequestID 合同流程编号
     */
    public String getContractRequestID() {
        return this.contractRequestID;
    }

    /**
     * 设置 合同流程编号
     *
     * @param contractRequestID 合同流程编号
     */
    public void setContractRequestID(String contractRequestID) {
        this.contractRequestID = contractRequestID;
    }

    /**
     * 获取 合同编码
     *
     * @return contractNo 合同编码
     */
    public String getContractNo() {
        return this.contractNo;
    }

    /**
     * 设置 合同编码
     *
     * @param contractNo 合同编码
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * 获取 合同名称
     *
     * @return contractName 合同名称
     */
    public String getContractName() {
        return this.contractName;
    }

    /**
     * 设置 合同名称
     *
     * @param contractName 合同名称
     */
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    /**
     * 获取 合同期初金额
     *
     * @return contractAmt 合同期初金额
     */
    public double getContractAmt() {
        return this.contractAmt;
    }

    /**
     * 设置 合同期初金额
     *
     * @param contractAmt 合同期初金额
     */
    public void setContractAmt(double contractAmt) {
        this.contractAmt = contractAmt;
    }

    /**
     * 获取 合同在途冻结金额
     *
     * @return contractFreezeAmt 合同在途冻结金额
     */
    public double getContractFreezeAmt() {
        return this.contractFreezeAmt;
    }

    /**
     * 设置 合同在途冻结金额
     *
     * @param contractFreezeAmt 合同在途冻结金额
     */
    public void setContractFreezeAmt(double contractFreezeAmt) {
        this.contractFreezeAmt = contractFreezeAmt;
    }

    /**
     * 获取 合同已使用金额
     *
     * @return contractUsedAmt 合同已使用金额
     */
    public double getContractUsedAmt() {
        return this.contractUsedAmt;
    }

    /**
     * 设置 合同已使用金额
     *
     * @param contractUsedAmt 合同已使用金额
     */
    public void setContractUsedAmt(double contractUsedAmt) {
        this.contractUsedAmt = contractUsedAmt;
    }

    /**
     * 获取 合同剩余金额
     *
     * @return contractBalance 合同剩余金额
     */
    public double getContractBalance() {
        return this.contractBalance;
    }

    /**
     * 设置 合同剩余金额
     *
     * @param contractBalance 合同剩余金额
     */
    public void setContractBalance(double contractBalance) {
        this.contractBalance = contractBalance;
    }

    /**
     * 获取
     *
     * @return detailEntityList
     */
    public List<ContractBudgetDetailEntity> getDetailEntityList() {
        return this.detailEntityList;
    }

    /**
     * 设置
     *
     * @param detailEntityList
     */
    public void setDetailEntityList(List<ContractBudgetDetailEntity> detailEntityList) {
        this.detailEntityList = detailEntityList;
    }
}
