package com.westvalley.action.prepay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.westvalley.project.action.DevAbstractAction;
import com.westvalley.project.dto.ActionDto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.util.ProjUtil;
import com.westvalley.util.DevUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSetTrans;
import weaver.general.Util;
import weaver.soa.workflow.request.RequestInfo;

import java.math.BigDecimal;
import java.util.*;

/**
 * 预付款生成凭证分录
 */
public class PrepayCreAction extends DevAbstractAction{

    /**
     * 业务逻辑
     *
     * @param info
     * @param actionDto
     * @return 返回 null 或者ok 则代表通过
     */
    @Override
    protected ResultDto runCode(RequestInfo info, ActionDto actionDto) {
        return updateDetail(actionDto.getRequestID(),actionDto.getWorkflowID());
    }

    public ResultDto updateDetail(String requestID,String workflowID){
        Map<String, String> mainMap = DevUtil.getWFMainMapByReqID(requestID);
        log.d("mainMap",mainMap);
        JSONArray debitList = getDebitList(requestID,mainMap);
        JSONArray crebitList = getCrebitList(requestID,mainMap);
        log.d("生成凭证分录",debitList,crebitList);
        RecordSetTrans trans = new RecordSetTrans();
        trans.setAutoCommit(false);
        try {
            String mainid = DevUtil.getWFMainIDByReqID(requestID);
            String detailTable2 = DevUtil.getWFDetailTableName(workflowID, 2);
            String sql2 = "insert into "+detailTable2+"(mainid,jzm,tbzz,zy,hjkm,jfje,hsxm,cbzx,gys,bz)" +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            trans.executeUpdate("delete from "+detailTable2 +" where mainid = ? ",mainid);
            for (int i = 0; i < debitList.size(); i++) {
                JSONObject obj = debitList.getJSONObject(i);
                trans.executeUpdate(sql2,
                        mainid,
                        obj.getString("jzm"),
                        obj.getString("tbzz"),
                        obj.getString("zy"),
                        obj.getString("hjkm"),
                        obj.getString("jfje"),
                        obj.getString("hsxm"),
                        "",//obj.getString("cbzx"),
                        obj.getString("gys"),
                        obj.getString("bz")
                );
            }
            String detailTable3 = DevUtil.getWFDetailTableName(workflowID, 3);
            String sql3 = "insert into "+detailTable3+"(mainid,jzm,tbzz,zy,hjkm,dfje,xjl,hsxm,bz) values(" +
                    "?,?,?,?,?,?,?,?,?)";
            trans.executeUpdate("delete from "+detailTable3 +" where mainid = ? ",mainid);
            for (int i = 0; i < crebitList.size() ; i++) {
                JSONObject obj = crebitList.getJSONObject(i);
                trans.executeUpdate(sql3,
                        mainid,
                        obj.getString("jzm"),
                        obj.getString("tbzz"),
                        obj.getString("zy"),
                        obj.getString("hjkm"),
                        obj.getString("dfje"),
                        obj.getString("xjl"),
                        obj.getString("hsxm"),
                        obj.getString("bz")
                );
            }
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            log.e("更新分录失败",e);
            return ResultDto.error("更新分录失败，"+e.getMessage());
        }
        return ResultDto.ok("更新分录成功");
    }



    /**获取借方明细*/
    public JSONArray getDebitList(String requestID, Map<String, String> mainMap){
//        field7105	jzm	记账码	varchar(999)
//        field7106	tbzz	特别总账	varchar(999)
//        field7107	zy	摘要	varchar(999)
//        field7108	hjkm	会计科目	browser.KJKM
//        field7109	jfje	借方金额	decimal(38,2)
//        field7110	lrzx	利润中心	browser.LRZX
//        field7151	hsxm	核算项目	varchar(999)
//        field7152	cbzx	成本中心	browser.CBZX
//        field7153	ry	人员	text
//        field7154	bz	备注	varchar(999)
//        field7220	wbbb	外币币别	browser.BZ
//        field7221	wbje	外币金额	decimal(38,2)
//        field7222	wbhl	外币汇率	decimal(38,4)
//        String shenqbm = getMapV(mainMap, "shenqbm");
        String supplierCode = getMapV(mainMap, "fwsmc");
        String jzm = "29";
        String tbzz = "C";
//        String zy = "付_"+DevUtil.getHrmLastName(getMapV(mainMap, "shenqr"))+"_"+getSupplierName(supplierCode)+StringUtil.removeHTMLTag(getMapV(mainMap,"yfksy"));
        String bt = DevUtil.getWFReqName(requestID);
        String zy = "预付:付"+DevUtil.getHrmLastName(getMapV(mainMap, "shenqr"))+" "+getSupplierName(supplierCode)+ " " + bt;
        String hjkm = getAccountcode(supplierCode);
        String jfje = "";
        String cbzx = "";//getCostcenter(shenqbm);
        String gys = supplierCode;
        String bz = "";
        String hsxm = "";
//        String wbbb = "";
//        String wbje = "";
//        String wbhl = "";


        //获取分摊信息
        List<Map<String, String>> wfDetail4 = DevUtil.getWFDetailByReqID(requestID, 4);
        log.d("wfDetail4",wfDetail4);

        List<Map<String, String>> precentList = new ArrayList<>();
        for (Map<String, String> map:wfDetail4) {

            Map<String, String> temp = new HashMap<>();
            String proj1ID = getMapV(map, "proj1");
            String proj0ID = getMapV(map, "ftbm");
            String amt = String.valueOf(getMapAmtV(map, "bclxje"));

            temp.put("proj1ID",proj1ID);
            temp.put("proj0ID",proj0ID);
            temp.put("amt",amt);
            precentList.add(temp);
        }
        Map<String, List<Map<String, String>>> precentData = getPrecentData(precentList);

        log.d("父项成本中心分摊百分比",precentData);

        JSONArray array = new JSONArray();

        //借方需要跟进祖项分摊
        List<Map<String, String>> wfDetail1 = DevUtil.getWFDetailByReqID(requestID, 1);
        for (Map<String, String> map : wfDetail1) {

            int projID = Util.getIntValue(getMapV(map, "zxszx"));//子项/孙子项
            int proj1ID = ProjUtil.getProj1ID4Child(projID);


            hsxm = getProjno(getMapV(map,"zxszx"));
            BigDecimal totalPayAmt = new BigDecimal(String.valueOf(getMapAmtV(map,"fkje")));
            BigDecimal tempPayAmt = BigDecimal.ZERO;
            int m = 1;
            List<Map<String, String>> tempList = precentData.get(String.valueOf(proj1ID));
            for (Map<String, String> precentMap : tempList) {
                //根据父项分摊计算核算
                BigDecimal precent = new BigDecimal(precentMap.get("precent"));
                BigDecimal payAmt = totalPayAmt.multiply(precent).setScale(2, BigDecimal.ROUND_HALF_UP);
                if(m == tempList.size()){
                    jfje = totalPayAmt.subtract(tempPayAmt).toString();
                }else{
                    jfje = payAmt.toString();
                }
                m++;
                tempPayAmt = tempPayAmt.add(payAmt);

                cbzx = precentMap.get("costcenter");//成本中心
                JSONObject obj = new JSONObject();
                obj.put("jzm",jzm);
                obj.put("tbzz",tbzz);
                obj.put("zy",zy);
                obj.put("hjkm",hjkm);
                obj.put("jfje",jfje);
                obj.put("cbzx",cbzx);
                obj.put("gys",gys);
                obj.put("bz",bz);
                obj.put("hsxm",hsxm);
//        obj.put("wbbb",wbbb);
//        obj.put("wbje",wbje);
//        obj.put("wbhl",wbhl);
                array.add(obj);
            }

        }
        return array;
    }
    /**获取贷方明细*/
    public JSONArray getCrebitList(String requestID, Map<String, String> mainMap){

//        field7111	jzm	记账码	varchar(999)
//        field7112	tbzz	特别总账	varchar(999)
//        field7113	zy	摘要	varchar(999)
//        field7114	hjkm	会计科目	browser.KJKM
//        field7115	dfje	贷方金额	decimal(38,2)
//        field7116	xjl	现金流	varchar(999)
//        field7117	lrzx	利润中心	browser.LRZX
//        field7155	hsxm	核算项目	varchar(999)
//        field7223	wbbb	外币币别	browser.BZ
//        field7224	wbje	外币金额	decimal(38,2)
//        field7225	wbhl	外币汇率	decimal(38,4)
//        field7226	bz	备注	varchar(999)
        String jzm = "50";
        String tbzz = "";
        String bt = DevUtil.getWFReqName(requestID);
        String zy = "预付:付"+DevUtil.getHrmLastName(getMapV(mainMap, "shenqr"))+" "+getSupplierName(getMapV(mainMap,"fwsmc"))+ " " + bt;
//        String zy = "付_"+DevUtil.getHrmLastName(getMapV(mainMap,"shenqr"))+"_"+getSupplierName(getMapV(mainMap,"fwsmc"))+StringUtil.removeHTMLTag(getMapV(mainMap,"yfksy"));
        String hjkm = "1002010100";
        String dfje = StringUtil.toNum(getMapV(mainMap,"yfkje"));
        String xjl = "B4";
//        String lrzx = "";
//        String wbbb = "";
//        String wbje = "";
//        String wbhl = "";
        String bz = "";

        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("jzm",jzm);
        obj.put("tbzz",tbzz);
        obj.put("zy",zy);
        obj.put("hjkm",hjkm);
        obj.put("dfje",dfje);
        obj.put("xjl",xjl);
//        obj.put("lrzx",lrzx);
//        obj.put("wbbb",wbbb);
//        obj.put("wbje",wbje);
//        obj.put("wbhl",wbhl);
        obj.put("bz",bz);
        array.add(obj);
        return array;
    }
    /**获取供应商名称*/
    public String getSupplierName(String code){
        return DevUtil.executeQuery("select NAME1 from OA_SAP_GYS where PARTNER = ? ",code);
    }

    /**
     * 获取成本中心
     * @param deptid 申请人部门ID
     * @return
     */
    public String getCostcenter(String deptid){
        return DevUtil.executeQuery("select sapcbzxbm from uf_uf_OA_SAP_BMCB where oabmmc = ? ",deptid);
    }

    /**
     * 获取成本中心
     * @param proj0ID 祖项
     * @return
     */
    public String getProjCostcenter(int proj0ID){
        return DevUtil.executeQuery("select projDeptNo from uf_proj where projtype = 0 and id = ?  ",proj0ID);
    }

    /**
     * 获取会计科目
     * @param supplierCode 供应商编码
     * @return
     */
    public String getAccountcode(String supplierCode){
        return DevUtil.executeQuery("select AKONT from OA_SAP_GYS where PARTNER = ? ",supplierCode);
    }
    /**
     * 获取项目编号
     * @param projid 项目ID
     * @return
     */
    public String getProjno(String projid){
        return DevUtil.executeQuery("select projno from uf_proj where id = ? ",projid);
    }

    /**
     * 重新计算分摊比例
     * @return
     * @param precentList
     */
    public Map<String,List<Map<String,String>>> getPrecentData(List<Map<String, String>> precentList){

        //保存同一父项的祖项分摊信息
        Map<String,Map<String,BigDecimal>> proj1AmtMap = new HashMap<>();
        //父项总金额
        Map<String,BigDecimal> proj1Total = new HashMap<>();

        for (Map<String, String> map : precentList) {
            String proj1ID = map.get("proj1ID");
            String proj0ID = map.get("proj0ID");
            BigDecimal amt = new BigDecimal(map.get("amt"));

            Map<String, BigDecimal> proj1Map = proj1AmtMap.get(proj1ID);
            if(proj1Map == null){
                proj1Map = new HashMap<>();
            }
            proj1Map.put(proj0ID,amt);
            proj1AmtMap.put(proj1ID,proj1Map);

            //合计父项总金额
            BigDecimal total = proj1Total.get(proj1ID);
            if(total == null){
                total = BigDecimal.ZERO;
            }
            total = total.add(amt);
            proj1Total.put(proj1ID,total);
        }

        //保存分摊信息
        Map<String,List<Map<String,String>>> data = new HashMap<>();

        //计算分摊比例
        for (String proj1ID : proj1AmtMap.keySet()) {
            BigDecimal totalAmt = proj1Total.get(proj1ID);

            Map<String, BigDecimal> proj0Map = proj1AmtMap.get(proj1ID);
            int size = proj0Map.keySet().size();
            BigDecimal tempPrecent = BigDecimal.ZERO;
            BigDecimal onePrecent = new BigDecimal("1");

            List<Map<String,String>> list = new ArrayList<>();
            for (String proj0ID : proj0Map.keySet()) {
                size--;
                BigDecimal proj0Amt = proj0Map.get(proj0ID);
                BigDecimal precent;
                if(size == 0){
                    //最后一条
                    precent = onePrecent.subtract(tempPrecent);
                }else{
                    precent = proj0Amt.divide(totalAmt,4, BigDecimal.ROUND_HALF_UP);
                    tempPrecent = tempPrecent.add(precent);
                }
                //祖项成本中心
                String costcenter = getProjCostcenter(Util.getIntValue(proj0ID));

                Map<String,String> proj0Precent = new HashMap<>();
                proj0Precent.put("costcenter",costcenter);
                proj0Precent.put("precent",precent.toString());

                list.add(proj0Precent);

            }
            data.put(proj1ID,list);
        }
        return data;
    }

    public BigDecimal add(double a, double b){
        BigDecimal aa = new BigDecimal(String.valueOf(a));
        BigDecimal bb = new BigDecimal(String.valueOf(b));
        return aa.add(bb);
    }
}
