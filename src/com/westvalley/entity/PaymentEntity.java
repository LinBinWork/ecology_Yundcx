package com.westvalley.entity;

/**
 * 付款计划
 */
public class PaymentEntity {

    private String id;
    private String reqId;
    private String fkqs;//付款期数
    private String fkje;//付款金额
    private String bz;//币种
    private String sl;//税率
    private String fktj;//付款条件
    private String fkzt;//付款状态 0:未付款 1:部分付款 2:已付款
    private double yfje;//已付金额
    private double syje;//剩余金额
    private String sfxyys;//是否需要验收

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getFkqs() {
        return fkqs;
    }

    public void setFkqs(String fkqs) {
        this.fkqs = fkqs;
    }

    public String getFkje() {
        return fkje;
    }

    public void setFkje(String fkje) {
        this.fkje = fkje;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getFktj() {
        return fktj;
    }

    public void setFktj(String fktj) {
        this.fktj = fktj;
    }

    public String getFkzt() {
        return fkzt;
    }

    public void setFkzt(String fkzt) {
        this.fkzt = fkzt;
    }

    public String getSfxyys() {
        return sfxyys;
    }

    public void setSfxyys(String sfxyys) {
        this.sfxyys = sfxyys;
    }

    public double getYfje() {
        return yfje;
    }

    public void setYfje(double yfje) {
        this.yfje = yfje;
    }

    public double getSyje() {
        return syje;
    }

    public void setSyje(double syje) {
        this.syje = syje;
    }
}
