package com.westvalley.action;

import com.westvalley.consts.Vouc;
import com.westvalley.util.LogUtil;
import com.westvalley.voucher.webservice.sap_com.*;
import com.westvalley.voucher.webservice.sap_com.holders.TABLE_OF_ZSFI_PARK_AC_HEADHolder;
import com.westvalley.voucher.webservice.sap_com.holders.TABLE_OF_ZSFI_PARK_AC_ITEMHolder;
import com.westvalley.voucher.webservice.sap_com.holders.TABLE_OF_ZSFI_PARK_AC_RETHolder;
import org.apache.commons.lang3.StringUtils;
import weaver.conn.RecordSet;
import weaver.soa.workflow.request.RequestInfo;

import javax.xml.rpc.ServiceException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Voucher {

    private static LogUtil log = LogUtil.getLogger();

    public static Map<String, String> toCreateVoucher(Map<String, Object> parameter, List<Map<String, Object>> par) {
        Map<String, String> resultMap = new HashMap();
        try {
            log.d("开始生成凭证");
            String url = Vouc.getURL();
            if (StringUtils.isEmpty(url)) {
                //为空为默认地址
                url = "http://39.108.66.86:8010/sap/bc/srt/rfc/sap/zffi_01/300/zffi_01/zffi_01";
            }
            log.d("url ====== ", url);
            String userName = Vouc.getUSERNAME();
            String passWord = Vouc.getPASSWORD();
            if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(passWord)) {
                resultMap.put("status", "E");
                resultMap.put("msg", "获取不到用户名和密码，请配置文件。");
                return resultMap;
            }
            log.d("parameter ===== ", parameter);
            URL portAddress = new URL(url);
            ZFFI_01_BindingStub stub = (ZFFI_01_BindingStub) new ZFFI_01_ServiceLocator().getZFFI_01(portAddress);
            stub.setUsername(userName);
            stub.setPassword(passWord);
            ZSFI_PARK_AC_HEAD[] heads = new ZSFI_PARK_AC_HEAD[1];
            ZSFI_PARK_AC_HEAD head = new ZSFI_PARK_AC_HEAD();
            head.setITEM(parameter.get("ITEM").toString());//行号
            head.setBUKRS(parameter.get("BUKRS").toString());//公司代码
            head.setBLDAT(parameter.get("BLDAT").toString());//凭证日期
            head.setBUDAT(parameter.get("BUDAT").toString());//过账日期
            head.setBLART(parameter.get("BLART").toString());//凭证类型
            head.setWAERS(parameter.get("WAERS").toString());//货币
            head.setHWAER(parameter.get("HWAER").toString());//本位币
            head.setKURSF(new BigDecimal(parameter.get("KURSF").toString()));//汇率
            head.setBKTXT(parameter.get("BKTXT").toString());//抬头文本
            heads[0] = head;
            log.d("par ======", par);
            if (par == null || par.size() == 0) {
                resultMap.put("status", "E");
                resultMap.put("msg", "凭证分录没有数据，请传数据。");
                return resultMap;
            }
            ZSFI_PARK_AC_ITEM[] items = new ZSFI_PARK_AC_ITEM[par.size()];
            int i = 0;
            for (Map map : par) {
                ZSFI_PARK_AC_ITEM item = new ZSFI_PARK_AC_ITEM();
                item.setITEM(map.get("ITEM") == null ? "" : map.get("ITEM").toString());//行号
                item.setBSCHL(map.get("BSCHL") == null ? "" : map.get("BSCHL").toString());//记账码
                item.setHKONT(map.get("HKONT") == null ? "" : map.get("HKONT").toString());//科目
                item.setUMSKZ(map.get("UMSKZ") == null ? "" : map.get("UMSKZ").toString());//特别总账标识
                item.setANBWA(map.get("ANBWA") == null ? "" : map.get("ANBWA").toString());//资产业务类型
                BigDecimal je = new BigDecimal(map.get("WRBTR") == null ? "" : map.get("WRBTR").toString());
                item.setWRBTR(je);//金额
                BigDecimal bbje = new BigDecimal(map.get("DMBTR") == null ? "" : map.get("DMBTR").toString());
                item.setDMBTR(bbje);//本币金额
                item.setKOSTL(map.get("KOSTL") == null ? "" : map.get("KOSTL").toString());//成本中心
                item.setRSTGR(map.get("RSTGR") == null ? "" : map.get("RSTGR").toString());//现金流
                item.setSGTXT(map.get("SGTXT") == null ? "" : map.get("SGTXT").toString());//摘要
                item.setZZ04(map.get("ZZ04") == null ? "" : map.get("ZZ04").toString());//员工
                item.setZZ02(map.get("ZZ02") == null ? "" : map.get("ZZ02").toString());//供应商
                item.setZZ11(map.get("ZZ11") == null ? "" : map.get("ZZ11").toString());//项目号
                item.setZINFO(map.get("ZINFO") == null ? "" : map.get("ZINFO").toString());//项目描述
                item.setZZ09(map.get("ZZ09") == null ? "" : map.get("ZZ09").toString());//业务类型
                items[i] = item;
                i++;
            }
            TABLE_OF_ZSFI_PARK_AC_RETHolder ZTFI_SC = new TABLE_OF_ZSFI_PARK_AC_RETHolder();
            TABLE_OF_ZSFI_PARK_AC_HEADHolder ZTFI_TT = new TABLE_OF_ZSFI_PARK_AC_HEADHolder(heads);
            TABLE_OF_ZSFI_PARK_AC_ITEMHolder ZTFI_MX = new TABLE_OF_ZSFI_PARK_AC_ITEMHolder(items);
            stub.ZFFI_01(ZTFI_MX, ZTFI_SC, ZTFI_TT);
            log.d("抬头传递参数:", ZTFI_TT.toString());
            log.d("明细传递参数:", ZTFI_MX.toString());
            log.d("返回参数:", ZTFI_SC.toString());
            ZSFI_PARK_AC_RET acRet = ZTFI_SC.value[0];
            resultMap.put("status", acRet.getISOK());
            resultMap.put("msg", acRet.getMSG());//凭证生成说明
            resultMap.put("BELNR", acRet.getBELNR());//凭证号
            resultMap.put("GJAHR", acRet.getGJAHR());//凭证年
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.e("凭证生成错误：", e);
            resultMap.put("status", "E");
            resultMap.put("msg", "凭证生成错误：" + e.getMessage());
            return resultMap;
        }
    }

    /**
     * 接口调用回写表单状态
     *
     * @param request
     * @param pzh     凭证号
     * @param pzsczt  凭证生成状态
     * @param pzxx    凭证信息
     * @return
     */
    public static String writeResult(RequestInfo request, String pzh, String pzsczt, String pzxx) {
        String msg = "";
        try {
            String requestID = request.getRequestid();
            String workflowID = request.getWorkflowid();
            RecordSet recordSet = new RecordSet();
            StringBuffer tSQL = new StringBuffer();
            tSQL.append("update " + getRequestTableName(workflowID));
            tSQL.append(" set ");
            if (!StringUtils.isEmpty(pzsczt) && pzsczt.equals("0")) {
                tSQL.append(" pzh ='" + pzh + "' ,");
            }
            tSQL.append(" pzsczt ='" + pzsczt + "'"
                    + ", pzxx ='" + pzxx + "' "
                    + " where requestid=" + requestID);
            boolean isOk = recordSet.executeUpdate(tSQL.toString());
            log.d(requestID + "-接口调用回写表单状态SQL:" + tSQL);
            if (!isOk) {
                msg = "接口调用回写表单状态失败:e" + tSQL;
            }
        } catch (Exception e) {
            msg = "接口调用回写表单状态失败" + e.getMessage();
            log.e("接口调用回写表单状态失败", e);
        }
        return msg;
    }

    public static String getRequestTableName(String workflowId) {
        String formId = "";
        String sql = "select formid from workflow_base where id='" + workflowId + "'";
        try {
            RecordSet rs = new RecordSet();
            if (rs.execute(sql) && rs.next()) {
                formId = rs.getString("formId").replace("-", "");
            }
        } catch (Exception e) {
            log.e("查询表单ID异常:" + sql, e);
        }
        return "formtable_main_" + formId;
    }

    public static void main(String[] args) throws MalformedURLException, ServiceException, RemoteException {
        Voucher voucher = new Voucher();
        Map<String, Object> parameter = new HashMap<>();
        String num = "JK201907090999";
        parameter.put("ITEM", num);//行号
        parameter.put("BUKRS", "1000");//公司代码
        parameter.put("BLDAT", "2020-03-07");//凭证日期
        parameter.put("BUDAT", "2020-03-07");//过账日期
        parameter.put("BLART", "SA");//凭证类型
        parameter.put("WAERS", "CNY");//货币
        parameter.put("HWAER", "CNY");//本位币
        parameter.put("KURSF", "1");//汇率
        parameter.put("BKTXT", "借款凭证");//抬头文本
        List<Map<String, Object>> par = new ArrayList<>();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("ITEM", num);//行号
        map2.put("BSCHL", "21");//记账码
        map2.put("HKONT", "0000700118");//科目
        map2.put("UMSKZ", "");//特别总账标识
        map2.put("ANBWA", "");//资产业务类型
        map2.put("WRBTR", "1000");//金额
        map2.put("DMBTR", "1000");//本币金额
        map2.put("KOSTL", "0000100503");//成本中心
        map2.put("RSTGR", "");//现金流zy
        map2.put("SGTXT", "线下渠道组");//摘要
        map2.put("ZZ04", "0000700118");//员工
        map2.put("ZZ02", "");//供应商
        map2.put("ZZ11", "");//项目号
        map2.put("ZINFO", "");//项目描述
//        map2.put("ZZ09", "1");//业务类型
        par.add(map2);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("ITEM", num);//行号
        map3.put("BSCHL", "50");//记账码
        map3.put("HKONT", "1002010200");//科目
        map3.put("UMSKZ", "");//特别总账标识
        map3.put("ANBWA", "");//资产业务类型
        map3.put("WRBTR", "1000");//金额
        map3.put("DMBTR", "1000");//本币金额
        map3.put("KOSTL", "0000100503");//成本中心
        map3.put("RSTGR", "B4");//现金流
        map3.put("SGTXT", "线下渠道");//摘要
        map3.put("ZZ04", "0000700118");//员工
        map3.put("ZZ02", "");//供应商
        map3.put("ZZ11", "");//项目号
        map3.put("ZINFO", "");//项目描述
//        map3.put("ZZ09", "1");//业务类型
        par.add(map3);
        Map<String, String> resultMap = voucher.toCreateVoucher(parameter, par);
        System.out.println(resultMap.toString());
    }


    public static String getAccountingName(String code) {
        RecordSet rs = new RecordSet();
        String sql = "select * from OA_SAP_Kjkm where HKONT = ? ";
        rs.executeQuery(sql, code);
        return rs.next() ? rs.getString("TXT50") : "";
    }

}
