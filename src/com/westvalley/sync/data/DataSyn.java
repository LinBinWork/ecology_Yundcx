package com.westvalley.sync.data;

import com.westvalley.consts.Sync;
import com.westvalley.sync.webservice.sap_com.*;
import com.westvalley.sync.webservice.sap_com.holders.*;
import com.westvalley.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSet;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.schedule.CronJob;

import java.net.URL;

/**
 * 数据同步
 */
public class DataSyn extends BaseCronJob implements CronJob {
    private LogUtil log = LogUtil.getLogger();

    /**
     * 同步数据
     *
     * @param synName gsdm:公司代码 km:科目 cbzx:成本中心 gh:供应商编码 zcbm:资产编码
     * @return
     */
    public boolean synData(String synName) {
        try {
            log.d("开始同步数据：", synName);
            String url = Sync.getURL();
            if (StringUtils.isEmpty(url)) {
                //为空为默认地址
                url = "http://39.108.66.86:8010/sap/bc/srt/rfc/sap/zffi_02/300/zffi_02/zffi_02";
            }
            log.d("url ====== ", url);
            TABLE_OF_ZSFI_ACCOUNTHolder etAccount = new TABLE_OF_ZSFI_ACCOUNTHolder();
            TABLE_OF_ZSFI_ASSETHolder etAsset = new TABLE_OF_ZSFI_ASSETHolder();
            TABLE_OF_ZSFI_COMPANY_CODEHolder etCompanyCode = new TABLE_OF_ZSFI_COMPANY_CODEHolder();
            TABLE_OF_ZSFI_COST_CENTERHolder etCostCenter = new TABLE_OF_ZSFI_COST_CENTERHolder();
            TABLE_OF_ZSFI_VENDORHolder etVendor = new TABLE_OF_ZSFI_VENDORHolder();


            URL portAddress = new URL(url);
            ZFFI_02_BindingStub stub = (ZFFI_02_BindingStub) new ZFFI_02_ServiceLocator().getZFFI_02(portAddress);
            String userName = Sync.getUSERNAME();
            String passWord = Sync.getPASSWORD();
            stub.setUsername(userName);
            stub.setPassword(passWord);
            stub.ZFFI_02(etAccount, etAsset, etCompanyCode, etCostCenter, etVendor);
            log.d("synName ======== ", synName);
            RecordSet rs = new RecordSet();
            if ("gsdm".equals(synName)) {
                //公司代码同步
                for (ZSFI_COMPANY_CODE gsdm : etCompanyCode.value) {
                    String selectSql = "select id from OA_SAP_Gsdm where BUKRS = '" + gsdm.getBUKRS() + "'";
                    log.d("selectSql ======= ", selectSql);
                    rs.execute(selectSql);
                    if (rs.next()) {
                        //存在，更新记录
                        String id = rs.getString("id");
                        String updateSql = "update OA_SAP_Gsdm set BUKRS = '" + gsdm.getBUKRS() + "',BUTXT = '" + gsdm.getBUTXT() + "' where id = " + id;
                        log.d("updateSql ====== ", updateSql);
                        rs.execute(updateSql);
                    } else {
                        //不存在，插入记录
                        String insertSql = "insert into OA_SAP_Gsdm(BUKRS,BUTXT) values ('" + gsdm.getBUKRS() + "','" + gsdm.getBUTXT() + "')";
                        log.d("insertSql ====== ", insertSql);
                        rs.execute(insertSql);
                    }
                }
            } else if ("km".equals(synName)) {
                //科目同步
                for (ZSFI_ACCOUNT km : etAccount.value) {
                    String selectSql = "select id from OA_SAP_Kjkm where BUKRS = '" + km.getBUKRS() + "'and HKONT = '" + km.getHKONT() + "'";
                    log.d("selectSql ======= ", selectSql);
                    rs.execute(selectSql);
                    if (rs.next()) {
                        //存在，更新记录
                        String id = rs.getString("id");
                        String updateSql = "update OA_SAP_Kjkm set BUKRS = '" + km.getBUKRS() + "',HKONT = '" + km.getHKONT() + "',TXT50 = '" + km.getTXT50() + "',SPRAS = '" + km.getSPRAS() + "',XSPEB = '" + km.getXSPEB() + "' where id = " + id;
                        log.d("updateSql ====== ", updateSql);
                        rs.execute(updateSql);
                    } else {
                        //不存在，插入记录
                        String insertSql = "insert into OA_SAP_Kjkm(BUKRS,HKONT,TXT50,SPRAS,XSPEB) values ('" + km.getBUKRS() + "','" + km.getHKONT() + "','" + km.getTXT50() + "','" + km.getSPRAS() + "','" + km.getXSPEB() + "')";
                        log.d("insertSql ====== ", insertSql);
                        rs.execute(insertSql);
                    }
                }
            } else if ("cbzx".equals(synName)) {
                //成本中心
                for (ZSFI_COST_CENTER cbzx : etCostCenter.value) {
                    String selectSql = "select id from OA_SAP_Cbzx where BUKRS = '" + cbzx.getBUKRS() + "'and KOSTL = '" + cbzx.getKOSTL() + "'";
                    log.d("selectSql ======= ", selectSql);
                    rs.execute(selectSql);
                    if (rs.next()) {
                        //存在，更新记录
                        String id = rs.getString("id");
                        String updateSql = "update OA_SAP_Cbzx set BUKRS = '" + cbzx.getBUKRS() + "',KOSTL = '" + cbzx.getKOSTL() + "',LTEXT = '" + cbzx.getLTEXT() + "',SPRAS = '" + cbzx.getSPRAS() + "',BKZKP = '" + cbzx.getBKZKP() + "' where id = " + id;
                        log.d("updateSql ====== ", updateSql);
                        rs.execute(updateSql);
                    } else {
                        //不存在，插入记录
                        String insertSql = "insert into OA_SAP_Cbzx(BUKRS,KOSTL,LTEXT,SPRAS,BKZKP) values ('" + cbzx.getBUKRS() + "','" + cbzx.getKOSTL() + "','" + cbzx.getLTEXT() + "','" + cbzx.getSPRAS() + "','" + cbzx.getBKZKP() + "')";
                        log.d("insertSql ====== ", insertSql);
                        rs.execute(insertSql);
                    }
                }
            } else if ("gysbm".equals(synName)) {
                //供应商编码
                for (ZSFI_VENDOR gys : etVendor.value) {
                    String selectSql = "select id from OA_SAP_Gys where PARTNER = '" + gys.getLIFNR() + "'and BUKRS = '" + gys.getBUKRS() + "'";
                    log.d("selectSql ======= ", selectSql);
                    rs.execute(selectSql);
                    if (rs.next()) {
                        //存在，更新记录
                        String id = rs.getString("id");
                        String gh = "";
                        if (gys.getNAME1() != null) {
                            String[] name = gys.getNAME1().split("_");
                            if (name.length > 1) {
                                gh = name[1];
                            }
                        }
                        String updateSql = "update OA_SAP_Gys set PARTNER = '" + gys.getLIFNR() + "',BUKRS = '" + gys.getBUKRS() + "',AKONT = '" + gys.getAKONT() + "',NAME1 = '" + gys.getNAME1() + "',GH = '" + gh + "',ZGYLX = '" + gys.getZGYLX() + "',ZBANK = '" + gys.getZBANK() + "',ACCNAME = '" + gys.getACCNAME() + "',XBLCK = '" + gys.getXBLCK() + "' where id = " + id;
                        log.d("updateSql ====== ", updateSql);
                        rs.execute(updateSql);
                    } else {
                        //不存在，插入记录
                        String gh = "";
                        if (gys.getNAME1() != null) {
                            String[] name = gys.getNAME1().split("_");
                            if (name.length > 1) {
                                gh = name[1];
                            }
                        }
                        String insertSql = "insert into OA_SAP_Gys(PARTNER,NAME1,GH,ZGYLX,AKONT,BUKRS,ZBANK,ACCNAME,XBLCK) values ('" + gys.getLIFNR() + "','" + gys.getNAME1() + "','" + gh + "','" + gys.getZGYLX() + "','" + gys.getAKONT() + "','" + gys.getBUKRS() + "','" + gys.getZBANK() + "','" + gys.getACCNAME() + "','" + gys.getXBLCK() + "')";
                        log.d("insertSql ====== ", insertSql);
                        rs.execute(insertSql);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.e("同步接口发生错误：", e);
            return false;
        }
    }


    /**
     * 定时器同步
     */
    public void execute() {
        try {
            TABLE_OF_ZSFI_ACCOUNTHolder etAccount = new TABLE_OF_ZSFI_ACCOUNTHolder();
            TABLE_OF_ZSFI_ASSETHolder etAsset = new TABLE_OF_ZSFI_ASSETHolder();
            TABLE_OF_ZSFI_COMPANY_CODEHolder etCompanyCode = new TABLE_OF_ZSFI_COMPANY_CODEHolder();
            TABLE_OF_ZSFI_COST_CENTERHolder etCostCenter = new TABLE_OF_ZSFI_COST_CENTERHolder();
            TABLE_OF_ZSFI_VENDORHolder etVendor = new TABLE_OF_ZSFI_VENDORHolder();
            String url = Sync.getURL();
            if (StringUtils.isEmpty(url)) {
                //为空为默认地址
                url = "http://39.108.66.86:8010/sap/bc/srt/rfc/sap/zffi_02/300/zffi_02/zffi_02";
            }

            log.d("url ====== ", url);
            URL portAddress = new URL(url);
            ZFFI_02_BindingStub stub = (ZFFI_02_BindingStub) new ZFFI_02_ServiceLocator().getZFFI_02(portAddress);
            String userName = Sync.getUSERNAME();
            String passWord = Sync.getPASSWORD();
            stub.setUsername(userName);
            stub.setPassword(passWord);
            stub.ZFFI_02(etAccount, etAsset, etCompanyCode, etCostCenter, etVendor);
            //公司代码同步
            for (ZSFI_COMPANY_CODE gsdm : etCompanyCode.value) {
                RecordSet rs = new RecordSet();
                String selectSql = "select id from OA_SAP_Gsdm where BUKRS = '" + gsdm.getBUKRS() + "'";
                log.d("selectSql ======= ", selectSql);
                rs.execute(selectSql);
                if (rs.next()) {
                    //存在，更新记录
                    String id = rs.getString("id");
                    String updateSql = "update OA_SAP_Gsdm set BUKRS = '" + gsdm.getBUKRS() + "',BUTXT = '" + gsdm.getBUTXT() + "' where id = " + id;
                    log.d("updateSql ====== ", updateSql);
                    rs.execute(updateSql);
                } else {
                    //不存在，插入记录
                    String insertSql = "insert into OA_SAP_Gsdm(BUKRS,BUTXT) values ('" + gsdm.getBUKRS() + "','" + gsdm.getBUTXT() + "')";
                    log.d("insertSql ====== ", insertSql);
                    rs.execute(insertSql);
                }
            }
            //科目同步
            for (ZSFI_ACCOUNT km : etAccount.value) {
                RecordSet rs = new RecordSet();
                String selectSql = "select id from OA_SAP_Kjkm where BUKRS = '" + km.getBUKRS() + "'and HKONT = '" + km.getHKONT() + "'";
                log.d("selectSql ======= ", selectSql);
                rs.execute(selectSql);
                if (rs.next()) {
                    //存在，更新记录
                    String id = rs.getString("id");
                    String updateSql = "update OA_SAP_Kjkm set BUKRS = '" + km.getBUKRS() + "',HKONT = '" + km.getHKONT() + "',TXT50 = '" + km.getTXT50() + "',SPRAS = '" + km.getSPRAS() + "',XSPEB = '" + km.getXSPEB() + "' where id = " + id;
                    log.d("updateSql ====== ", updateSql);
                    rs.execute(updateSql);
                } else {
                    //不存在，插入记录
                    String insertSql = "insert into OA_SAP_Kjkm(BUKRS,HKONT,TXT50,SPRAS,XSPEB) values ('" + km.getBUKRS() + "','" + km.getHKONT() + "','" + km.getTXT50() + "','" + km.getSPRAS() + "','" + km.getXSPEB() + "')";
                    log.d("insertSql ====== ", insertSql);
                    rs.execute(insertSql);
                }
            }
            //成本中心
            for (ZSFI_COST_CENTER cbzx : etCostCenter.value) {
                RecordSet rs = new RecordSet();
                String selectSql = "select id from OA_SAP_Cbzx where BUKRS = '" + cbzx.getBUKRS() + "'and KOSTL = '" + cbzx.getKOSTL() + "'";
                log.d("selectSql ======= ", selectSql);
                rs.execute(selectSql);
                if (rs.next()) {
                    //存在，更新记录
                    String id = rs.getString("id");
                    String updateSql = "update OA_SAP_Cbzx set BUKRS = '" + cbzx.getBUKRS() + "',KOSTL = '" + cbzx.getKOSTL() + "',LTEXT = '" + cbzx.getLTEXT() + "',SPRAS = '" + cbzx.getSPRAS() + "',BKZKP = '" + cbzx.getBKZKP() + "' where id = " + id;
                    log.d("updateSql ====== ", updateSql);
                    rs.execute(updateSql);
                } else {
                    //不存在，插入记录
                    String insertSql = "insert into OA_SAP_Cbzx(BUKRS,KOSTL,LTEXT,SPRAS,BKZKP) values ('" + cbzx.getBUKRS() + "','" + cbzx.getKOSTL() + "','" + cbzx.getLTEXT() + "','" + cbzx.getSPRAS() + "','" + cbzx.getBKZKP() + "')";
                    log.d("insertSql ====== ", insertSql);
                    rs.execute(insertSql);
                }
            }
            //供应商编码
            for (ZSFI_VENDOR gys : etVendor.value) {
                RecordSet rs = new RecordSet();
                String selectSql = "select id from OA_SAP_Gys where PARTNER = '" + gys.getLIFNR() + "'and BUKRS = '" + gys.getBUKRS() + "'";
                log.d("selectSql ======= ", selectSql);
                rs.execute(selectSql);
                if (rs.next()) {
                    //存在，更新记录
                    String id = rs.getString("id");
                    String gh = "";
                    if (gys.getNAME1() != null) {
                        String[] name = gys.getNAME1().split("_");
                        if (name.length > 1) {
                            gh = name[1];
                        }
                    }
                    String updateSql = "update OA_SAP_Gys set PARTNER = '" + gys.getLIFNR() + "',BUKRS = '" + gys.getBUKRS() + "',AKONT = '" + gys.getAKONT() + "',NAME1 = '" + gys.getNAME1() + "',GH = '" + gh + "',ZGYLX = '" + gys.getZGYLX() + "',ZBANK = '" + gys.getZBANK() + "',ACCNAME = '" + gys.getACCNAME() + "',XBLCK = '" + gys.getXBLCK() + "' where id = " + id;
                    log.d("updateSql ====== ", updateSql);
                    rs.execute(updateSql);
                } else {
                    //不存在，插入记录
                    String gh = "";
                    if (gys.getNAME1() != null) {
                        String[] name = gys.getNAME1().split("_");
                        if (name.length > 1) {
                            gh = name[1];
                        }
                    }
                    String insertSql = "insert into OA_SAP_Gys(PARTNER,NAME1,GH,ZGYLX,AKONT,BUKRS,ZBANK,ACCNAME,XBLCK) values ('" + gys.getLIFNR() + "','" + gys.getNAME1() + "','" + gh + "','" + gys.getZGYLX() + "','" + gys.getAKONT() + "','" + gys.getBUKRS() + "','" + gys.getZBANK() + "','" + gys.getACCNAME() + "','" + gys.getXBLCK() + "')";
                    log.d("insertSql ====== ", insertSql);
                    rs.execute(insertSql);
                }
            }
        } catch (Exception e) {
            log.e("定时器SAP同步数据异常", e);
            e.printStackTrace();
        }
    }

}
