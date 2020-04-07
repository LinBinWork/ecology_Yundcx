package com.westvalley.action.payment;


import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;

public class Payment {

    public static String getPayDataSQL(String gysid, String requestId) {
        StringBuffer sb = new StringBuffer();
        //预付款记录
        sb.append("select id,gysid,requestid,requestcode,detailid,bz,requestname,xmid,payDate,remark,money,moneyed,moneying,moneylast from " +
                "( ");
        sb.append("select " +
                " id,gysid,w1.requestid,w1.requestcode,detailid,bz,w1.requestname,xmid,payDate,remark,isnull(money,0) as money,currentNodeType," +
                " isnull((select sum(money) from WV_T_PaymentData t1 where t1.requestid = w1.requestid and t1.rectype not in ('payment') and w1.detailid = t1.detailid " +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('3') and t1.payid not in ('" + requestId + "')),0) as moneyed," +// 已核销借款
                " isnull((select sum(money) from WV_T_PaymentData t1 where t1.requestid = w1.requestid and t1.rectype not in ('payment') and w1.detailid = t1.detailid " +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2') and t1.payid not in ('" + requestId + "')),0) as moneying," +//在途核销借款
                " isnull(w1.money - isnull((select sum(money) from WV_T_PaymentData t1 where t1.requestid = w1.requestid and t1.rectype not in ('payment') and w1.detailid = t1.detailid " +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2','3') and t1.payid not in ('" + requestId + "')),0),0) as moneylast " +//借款余额
                " from WV_T_PaymentData w1" +
                " left join workflow_RequestBase w2 on w1.requestid = w2.requestid" +
                " where w1.rectype = 'payment' " +
                " and w2.currentNodeType = '3' " +
                " and gysid = '" + gysid + "' ");
        sb.append(")T where moneylast > 0");
        return sb.toString();
    }

    /**
     * SQL语句查询结果集转换成Json字符串
     *
     * @param sql sql语句
     * @return
     */
    public static JSONObject rsToJson(String sql) {
        JSONObject json = new JSONObject();
        try {
            RecordSet rs = new RecordSet();
            rs.executeSql(sql);
            String[] keySet = rs.getColumnName();
            int num = 0;
            while (rs.next()) {
                JSONObject dajson = new JSONObject();
                for (String key : keySet) {
                    dajson.put(key.toLowerCase(), Util.null2String(rs.getString(key)));
                }
                json.put(num + "", dajson);
                num++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


}
