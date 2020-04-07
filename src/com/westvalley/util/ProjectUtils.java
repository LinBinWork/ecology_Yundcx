package com.westvalley.util;

import weaver.conn.RecordSet;

public class ProjectUtils {


    /**
     * 根据祖项编码获取祖项名称
     *
     * @param bm
     * @return
     */
    public static String getZXMC(String bm) {
        RecordSet rs = new RecordSet();
        rs.executeQuery("select orgName from uf_OrgProject where orgCode = ?", bm);
        String zxmc = "";
        if (rs.next()) {
            zxmc = rs.getString("orgName");
        }
        return zxmc;
    }
}
