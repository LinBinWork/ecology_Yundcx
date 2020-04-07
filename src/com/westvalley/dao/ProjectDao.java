package com.westvalley.dao;

import com.westvalley.entity.BudgetEntity;
import com.westvalley.util.LogUtil;
import weaver.conn.RecordSet;

public class ProjectDao {

    private LogUtil log = LogUtil.getLogger(getClass());

    public String executeSubitem(String xmbh) {
        RecordSet recordSet = new RecordSet();
        String sql = "select zxxmbh  " +
                "from formtable_main_21 t1 " +
                "join formtable_main_21_dt1 t2 on t1.id = t2.mainid " +
                "join workflow_requestbase t3 on t1.requestId = t3.requestId " +
                "where t3.currentnodetype = '3' and t2.zxxmbh = '" + xmbh + "'";
        //查询是否是子项项目
        recordSet.execute(sql);
        return recordSet.next() ? "0" : null;
    }


    public String executeGrandson(String xmbh) {
        RecordSet recordSet = new RecordSet();
        String sql = "select zxxmbh  " +
                "from formtable_main_25 t1 " +
                "join formtable_main_25_dt1 t2 on t1.id = t2.mainid " +
                "join workflow_requestbase t3 on t1.requestId = t3.requestId " +
                "where t3.currentnodetype = '3' and t2.szxxmbh = '" + xmbh + "'";
        //查询是否是孙子项项目
        recordSet.execute(sql);
        return recordSet.next() ? "1" : null;
    }

    /**
     * 根据id获取项目预算信息
     *
     * @param id
     * @return
     */
    public BudgetEntity getBudgetEntityById(int id) {
        RecordSet recordSet = new RecordSet();
        BudgetEntity entity = new BudgetEntity();
        String sql = "select * from uf_proj where id = ?";
        recordSet.executeQuery(sql, id);
        if (recordSet.next()) {
            entity.setId(recordSet.getInt("id"));
            entity.setReqID(recordSet.getString("reqID"));
            entity.setDetailID(recordSet.getInt("detailID"));
            entity.setpID(recordSet.getInt("pID"));
            entity.setCompanyNo(recordSet.getString("companyNo"));
            entity.setBusinessType(recordSet.getString("businessType"));
            entity.setProjNo(recordSet.getString("projNo"));
            entity.setProjDeptNo(recordSet.getString("projDeptNo"));
            entity.setProjName(recordSet.getString("projName"));
            entity.setProjDesc(recordSet.getString("projDesc"));
            entity.setProjType(recordSet.getString("projType"));
            entity.setProjAmt(recordSet.getDouble("projAmt"));
            entity.setProjYear(recordSet.getInt("projYear"));
            entity.setCtrlLevel(recordSet.getString("ctrlLevel"));
        }
        return entity;
    }

    /**
     * 根据父项id检验当前是否存在应合同结案但未完成结案的情况，如存在则进行提示，不允许流程提交
     *
     * @param id
     * @return
     */
    public String checkClose1(int id) {
        try {
            StringBuffer sb = new StringBuffer();
            //子项
            String sql = "select t1.projName AS 'pName',t2.* from uf_proj t1 " +
                    "left join uf_proj t2 on t1.id = t2.pid " +
                    "left join uf_YXHTTZ_dt2 t3 on t2.id = t3.xmbh " +
                    "left join  uf_YXHTTZ t4 on t3.mainid = t4.id " +
                    "where t2.id is not null and t4.htzt = '2' and t1.id = ? ";
            log.d("sql ==== ", sql);
            RecordSet rs = new RecordSet();
            rs.executeQuery(sql, id);
            while (rs.next()) {
                String pName = rs.getString("pName");
                String projNo = rs.getString("projNo");
                String projName = rs.getString("projName");
                sb.append("<br>父项：" + pName + "存在 子项项目名称：" + projName + "，编码：" + projNo + " 未完成合同结案无法提交。");
            }
            //孙子项
            String sql2 = "select t1.projName AS 'pName',t3.* from uf_proj t1 " +
                    "left join uf_proj t2 on t1.id = t2.pid " +
                    "left join uf_proj t3 on t2.id = t3.pid " +
                    "left join uf_YXHTTZ_dt2 t4 on t3.id = t4.xmbh " +
                    "left join uf_YXHTTZ t5 on t4.mainid = t5.id " +
                    "where  t5.htzt = '2' and t1.id = ? ";
            rs.executeQuery(sql2, id);
            while (rs.next()) {
                String pName = rs.getString("pName");
                String projNo = rs.getString("projNo");
                String projName = rs.getString("projName");
                sb.append("<br>父项：" + pName + "存在 孙子项名称：" + projName + "，编码：" + projNo + " 未完成合同结案无法提交。");
            }
            return sb.toString();
        } catch (Exception e) {
            return "系统发生错误.";
        }
    }

    /**
     * 校验是否存在借款未核销的情况
     *
     * @param id
     * @return
     */
    public String checkClose4(int id) {
        try {
            StringBuffer ids = new StringBuffer();
            ids.append(id + "");
            String sql = "select t2.id as 'id' from uf_proj t1 " +
                    " left join uf_proj t2 on t1.id = t2.pid " +
                    " where t2.id is not null and t1.id = ? " +
                    " union all " +
                    " select t3.id as 'id' from uf_proj t1 " +
                    " left join uf_proj t2 on t1.id = t2.pid " +
                    " left join uf_proj t3 on t2.id = t3.pid " +
                    " where t3.id is not null and t1.id = ? ";
            log.d("sql ==== ", sql);
            RecordSet rs = new RecordSet();
            rs.executeQuery(sql, id, id);
            while (rs.next()) {
                ids.append("," + rs.getString("id"));
            }
            log.d("ids === ", ids);
            String sql2 = "select * from WV_V_NoRepayment where projid in(" + ids.toString() + ")";
            RecordSet rs2 = new RecordSet();
            log.d("sql2 ===", sql2);
            rs2.executeQuery(sql2);
            log.d("rs2.toString() ===", rs2.toString());
            StringBuffer proName = new StringBuffer();
            RecordSet recordSet = new RecordSet();
            while (rs2.next()) {
                String proID = rs2.getString("projid");
                String userID = rs2.getString("userid");
                String lastMoney = rs2.getString("moneylast");
                String sql3 = "select projName from uf_proj where id = ?";
                log.d("sql3 == ", sql3);
                recordSet.executeQuery(sql3, proID);
                String projName = "";
                if (recordSet.next()) {
                    projName = recordSet.getString("projName");
                }
                String lastname = "";
                String sql4 = "select * from hrmresource where id = ?";
                log.d("sql4 ======== ", sql4);
                recordSet.executeQuery(sql4, userID);
                if (recordSet.next()) {
                    lastname = recordSet.getString("lastname");
                }
                if (proName.toString().equals("")) {
                    proName.append("当前项目下有借款未核销完，不允许关闭：");
                    proName.append("<br>项目名称：" + projName + " 借款人：" + lastname + " 剩余借款金额：" + lastMoney);
                } else {
                    proName.append("<br>项目名称：" + projName + " 借款人：" + lastname + " 剩余借款金额：" + lastMoney);
                }
            }
            return proName.toString();
        } catch (Exception e) {
            return "系统发生错误.";
        }
    }

    public String checkClose3(int id) {
        try {
            StringBuffer ids = new StringBuffer();
            ids.append(id + "");
            String sql = "select t2.id as 'id' from uf_proj t1 " +
                    " left join uf_proj t2 on t1.id = t2.pid " +
                    " where t2.id is not null and t1.id = ? " +
                    " union all " +
                    " select t3.id as 'id' from uf_proj t1 " +
                    " left join uf_proj t2 on t1.id = t2.pid " +
                    " left join uf_proj t3 on t2.id = t3.pid " +
                    " where t3.id is not null and t1.id = ? ";
            log.d("sql ==== ", sql);
            RecordSet rs = new RecordSet();
            rs.executeQuery(sql, id, id);
            while (rs.next()) {
                ids.append("," + rs.getString("id"));
            }
            log.d("ids === ", ids);
            String sql2 = "select * from WV_V_Flow where id in(" + ids.toString() + ")";
            RecordSet rs2 = new RecordSet();
            log.d("sql2 ===", sql2);
            rs2.executeQuery(sql2);
            log.d("rs2.toString() ===", rs2.toString());
            StringBuffer proName = new StringBuffer();
            RecordSet recordSet = new RecordSet();
            while (rs2.next()) {
                String proID = rs2.getString("id");
                String reqID = rs2.getString("requestId");
                log.d("reqID === ", reqID);
                String sql3 = "select projName from uf_proj where id = ?";
                log.d("sql3 == ", sql3);
                recordSet.executeQuery(sql3, proID);
                String projName = "";
                if (recordSet.next()) {
                    projName = recordSet.getString("projName");
                }
                String requestname = "";
                String createdate = "";
                String createtime = "";
                String lastname = "";
                String sql4 = "select t1.requestname,t1.createdate,t1.createtime,t2.lastname from workflow_requestbase t1 " +
                        " left join hrmresource t2 on t1.creater = t2.id where t1.requestid = ?";
                log.d("sql4 ======== ", sql4);
                recordSet.executeQuery(sql4, reqID);
                if (recordSet.next()) {
                    requestname = recordSet.getString("requestname");
                    createdate = recordSet.getString("createdate");
                    createtime = recordSet.getString("createtime");
                    lastname = recordSet.getString("lastname");
                }
                if (proName.toString().equals("")) {
                    proName.append("当前项目下的包含在途流程，不允许关闭：");
                    proName.append("<br>项目名称：" + projName + " 流程标题：" + requestname + " 流程ID：" + reqID + " 申请人：" + lastname + " 创建时间：" + createdate + " " + createtime);
                } else {
                    proName.append("<br>项目名称：" + projName + " 流程标题：" + requestname + " 流程ID：" + reqID + " 申请人：" + lastname + " 创建时间：" + createdate + " " + createtime);
                }
            }
            return proName.toString();
        } catch (Exception e) {
            return "系统发生错误.";
        }
    }

    /**
     * 修改项目为已关闭状态
     *
     * @param id
     * @return
     */
    public String closeProject(int id) {
        try {
            String sql = "select t4.id as 'id' from uf_proj t1 " +
                    "left join uf_proj t2 on t1.id = t2.pid " +
                    "left join uf_YXHTTZ_dt2 t3 on t2.id = t3.xmbh " +
                    "left join uf_YXHTTZ t4 on t3.mainid = t4.id " +
                    "where t2.id is not null and t1.id = ? ";
            RecordSet rs = new RecordSet();
            rs.executeQuery(sql, id);
            if (rs.next()) {
                int ufid = rs.getInt("id");
                String updateSql = "update uf_YXHTTZ set htzt = '0' where id = ? ";
                rs.executeUpdate(updateSql, ufid);
            }
            return "";
        } catch (Exception e) {
            return "系统发生错误.";
        }
    }

}
