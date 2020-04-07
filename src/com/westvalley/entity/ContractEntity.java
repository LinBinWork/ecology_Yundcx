package com.westvalley.entity;

import java.util.List;

/**
 * 合同
 */
public class ContractEntity {

    private String reqId;
    private String htbh;//合同编号
    private String htmc;//合同名称
    private String htqdf;//合同签定方
    private String htqsrq;//合同签署日期
    private String fzbm;//负责部门
    private String htje;//合同金额
    private String jbr;//经办人
    private String htzt;//合同状态
    private String creUser;//创建人
    private String creDate;//创建日期
    private String creTime;//创建时间
    private String htlx;//合同类型
    private String sfxhtja;//是否需合同结案
    private List<PaymentEntity> fkjh;//付款计划
    private List<ProjectEntity> xmxx;//项目信息

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getHtbh() {
        return htbh;
    }

    public void setHtbh(String htbh) {
        this.htbh = htbh;
    }

    public String getHtmc() {
        return htmc;
    }

    public void setHtmc(String htmc) {
        this.htmc = htmc;
    }

    public String getHtqdf() {
        return htqdf;
    }

    public void setHtqdf(String htqdf) {
        this.htqdf = htqdf;
    }

    public String getHtqsrq() {
        return htqsrq;
    }

    public void setHtqsrq(String htqsrq) {
        this.htqsrq = htqsrq;
    }

    public String getFzbm() {
        return fzbm;
    }

    public void setFzbm(String fzbm) {
        this.fzbm = fzbm;
    }

    public String getJbr() {
        return jbr;
    }

    public void setJbr(String jbr) {
        this.jbr = jbr;
    }

    public String getHtzt() {
        return htzt;
    }

    public void setHtzt(String htzt) {
        this.htzt = htzt;
    }

    public List<PaymentEntity> getFkjh() {
        return fkjh;
    }

    public void setFkjh(List<PaymentEntity> fkjh) {
        this.fkjh = fkjh;
    }

    public String getHtje() {
        return htje;
    }

    public void setHtje(String htje) {
        this.htje = htje;
    }

    public String getCreUser() {
        return creUser;
    }

    public void setCreUser(String creUser) {
        this.creUser = creUser;
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

    public List<ProjectEntity> getXmxx() {
        return xmxx;
    }

    public void setXmxx(List<ProjectEntity> xmxx) {
        this.xmxx = xmxx;
    }

    public String getHtlx() {
        return htlx;
    }

    public void setHtlx(String htlx) {
        this.htlx = htlx;
    }

    public String getSfxhtja() {
        return sfxhtja;
    }

    public void setSfxhtja(String sfxhtja) {
        this.sfxhtja = sfxhtja;
    }

}
