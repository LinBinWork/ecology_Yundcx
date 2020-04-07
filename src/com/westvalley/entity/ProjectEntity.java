package com.westvalley.entity;

public class ProjectEntity {

    private String xmbh;//项目编号
    private String xmmc;//项目名称
    private double ftje;//分摊金额
    private double ftbl;//分摊比率
    private String xmjl;//项目经理
    private String sjxmli;//上级项目经理
    private String bz;//备注

    public String getXmbh() {
        return xmbh;
    }

    public void setXmbh(String xmbh) {
        this.xmbh = xmbh;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public double getFtje() {
        return ftje;
    }

    public void setFtje(double ftje) {
        this.ftje = ftje;
    }

    public double getFtbl() {
        return ftbl;
    }

    public void setFtbl(double ftbl) {
        this.ftbl = ftbl;
    }

    public String getXmjl() {
        return xmjl;
    }

    public void setXmjl(String xmjl) {
        this.xmjl = xmjl;
    }

    public String getSjxmli() {
        return sjxmli;
    }

    public void setSjxmli(String sjxmli) {
        this.sjxmli = sjxmli;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
