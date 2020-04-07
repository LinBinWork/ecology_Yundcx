package com.westvalley.entity;

/**
 * 年度项目预算
 */
public class YearProjectEntity {
    private String zxbm = "";//主项编码
    private String zxmc = "";//主项名称
    private String fygsbmbm = "";//费用归属部门编码
    private String fygsbmmc = "";//费用归属部门名称
    private String ndysje = "";//年度预算金额
    private String bzsm = "";//编制说明

    public String getZxbm() {
        return zxbm;
    }

    public void setZxbm(String zxbm) {
        this.zxbm = zxbm;
    }

    public String getZxmc() {
        return zxmc;
    }

    public void setZxmc(String zxmc) {
        this.zxmc = zxmc;
    }

    public String getFygsbmbm() {
        return fygsbmbm;
    }

    public void setFygsbmbm(String fygsbmbm) {
        this.fygsbmbm = fygsbmbm;
    }

    public String getFygsbmmc() {
        return fygsbmmc;
    }

    public void setFygsbmmc(String fygsbmmc) {
        this.fygsbmmc = fygsbmmc;
    }

    public String getNdysje() {
        return ndysje;
    }

    public void setNdysje(String ndysje) {
        this.ndysje = ndysje;
    }

    public String getBzsm() {
        return bzsm;
    }

    public void setBzsm(String bzsm) {
        this.bzsm = bzsm;
    }
}
