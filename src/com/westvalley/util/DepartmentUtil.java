package com.westvalley.util;

import weaver.conn.RecordSet;

public class DepartmentUtil {

    public int  getDepartmentA(int deptid){
        int idA  = deptid;
        RecordSet rs = new RecordSet();
        String sql = "select id,departmentname,tlevel,supdepid from HrmDepartment where id = ?";
        boolean flag = true;
        while (flag){
            rs.executeQuery(sql,deptid);
            rs.next();
            int tlevel = rs.getInt("tlevel");
            if(tlevel==2){
                flag = false;
                idA = deptid;
            }
            else {
                deptid = rs.getInt("supdepid");
            }
        }
        return  idA ;
    }
    public int  getDepartmentB(int deptid){
        int idB  = deptid;
        RecordSet rs = new RecordSet();
        String sql = "select id,departmentname,tlevel,supdepid from HrmDepartment where id = ?";
        boolean flag = true;
        while (flag){
            rs.executeQuery(sql,deptid);
            rs.next();
            int tlevel = rs.getInt("tlevel");
            if(tlevel==3){
                flag = false;
                idB = deptid;
            }
            else {
                deptid = rs.getInt("supdepid");
            }
        }
        return  idB ;
    }
}
