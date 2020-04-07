package com.westvalley.dao;

import weaver.conn.RecordSet;

public class UserDao {


    public String getUserNameById(int id) {
        RecordSet recordSet = new RecordSet();
        String sql = "select lastname from hrmresource where id = " + id;
        recordSet.execute(sql);
        return recordSet.next() ? recordSet.getString("lastname") : null;

    }
}
