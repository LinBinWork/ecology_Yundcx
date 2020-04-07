package com.westvalley.action.borrow;


import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;

/**
 * 主要功能：1.将借款信息写入借款台账表
 */
public class Borrow {

    public static String getBorSQL(String userId, String requestId) {
        StringBuffer sb = new StringBuffer();
        //借款记录
        sb.append("select id,userid,projid,requestid,requestcode,payment,requestname,brodate,repaydate,remark,money,moneyed,moneying,moneylast from " +
                "( ");
        sb.append("select " +
                " id,userid,projid,w1.requestid,w1.requestcode,w1.requestname,brodate,repaydate,remark,isnull(money,0) as money,currentNodeType," +
                " payment," +
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('3') and t1.payid not in ('" + requestId + "')),0) as moneyed," +// 已核销借款
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2') and t1.payid not in ('" + requestId + "')),0) as moneying," +//在途核销借款
                " isnull(w1.money - isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2','3') and t1.payid not in ('" + requestId + "')),0),0) as moneylast " +//借款余额
                " from WV_T_BorrowData w1" +
                " left join workflow_RequestBase w2 on w1.requestid = w2.requestid" +
                " where w1.rectype = 'borrow' " +
                " and w2.currentNodeType = '3' " +
                " and userid = '" + userId + "' ");
        sb.append(" union all ");
        //导入的历史借款记录
        sb.append("select " +
                " id,userid,projid,w1.requestid,w1.requestcode,w1.requestname,brodate,repaydate,remark,isnull(money,0) as money,'3' as currentnodetype," +
                " payment," +
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('3') and t1.payid not in ('" + requestId + "')),0) as moneyed," +// 已核销借款
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2') and t1.payid not in ('" + requestId + "')),0) as moneying," +//在途核销借款
                " isnull(w1.money - isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2','3') and t1.payid not in ('" + requestId + "')),0),0) as moneylast " +//借款余额
                " from WV_T_BorrowData w1 " +
                " where w1.rectype = 'borrow' " +
                " and w1.ishistory = 'Y' " +
                " and userid = '" + userId + "' ");
        sb.append(")T where moneylast > 0");
        return sb.toString();
    }

    public static String getUserBorSQL(String userId, String requestId) {
        StringBuffer sb = new StringBuffer();
        //借款记录
        sb.append("select userid,sum(money) as 'money',sum(moneyed) as 'moneyed',sum(moneying) as 'moneying',sum(moneylast) as 'moneylast' from " +
                "( ");
        sb.append("select " +
                " userid,isnull(money,0) as money," +
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('3') and t1.payid not in ('" + requestId + "')),0) as moneyed," +// 已核销借款
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2') and t1.payid not in ('" + requestId + "')),0) as moneying," +//在途核销借款
                " isnull(w1.money - isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2','3') and t1.payid not in ('" + requestId + "')),0),0) as moneylast " +//借款余额
                " from WV_T_BorrowData w1" +
                " left join workflow_RequestBase w2 on w1.requestid = w2.requestid" +
                " where w1.rectype = 'borrow' " +
                " and w2.currentNodeType = '3' " +
                " and userid = '" + userId + "' ");
        sb.append(" union all ");
        //导入的历史借款记录
        sb.append("select " +
                " userid,isnull(money,0) as money," +
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('3') and t1.payid not in ('" + requestId + "')),0) as moneyed," +// 已核销借款
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2') and t1.payid not in ('" + requestId + "')),0) as moneying," +//在途核销借款
                " isnull(w1.money - isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2','3') and t1.payid not in ('" + requestId + "')),0),0) as moneylast " +//借款余额
                " from WV_T_BorrowData w1 " +
                " where w1.rectype = 'borrow' " +
                " and w1.ishistory = 'Y' " +
                " and userid = '" + userId + "' ");
        sb.append(")T group by userid");
        return sb.toString();
    }


    /**
     * 根据requestId带出选着的流程剩余未还的金额
     *
     * @param requestId
     * @return
     */
    public static String getReqBorSQL(String requestId) {
        StringBuffer sb = new StringBuffer();
        //借款记录
        sb.append("select userid,sum(money) as 'money',sum(moneyed) as 'moneyed',sum(moneying) as 'moneying',sum(moneylast) as 'moneylast' from " +
                "( ");
        sb.append("select " +
                " userid,isnull(money,0) as money," +
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('3')),0) as moneyed," +// 已核销借款
                " isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2')),0) as moneying," +//在途核销借款
                " isnull(w1.money - isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')" +
                " and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2','3')),0),0) as moneylast " +//借款余额
                " from WV_T_BorrowData w1" +
                " left join workflow_RequestBase w2 on w1.requestid = w2.requestid" +
                " where w1.rectype = 'borrow' " +
                " and w2.currentNodeType = '3' " +
                " and w1.requestid = '" + requestId + "'");
        sb.append(")T group by userid");
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
