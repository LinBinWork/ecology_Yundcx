package com.customization.sap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SapTable {
    private String tableName;
    private List<Map<String,String>> data;

    public SapTable(String tableName, List<Map<String, String>> data) {
        this.tableName = tableName;
        this.data = data;
    }

    public SapTable() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public int getSize(){
        return data.size();
    }
    public int getFieldCount(){
        return  data.get(0).size();
    }
    public List<String> getFieldName(){
        return data.get(0).keySet().stream().collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return "SapTable{" +
                "tableName='" + tableName + '\'' +
                ", data=" + data +
                '}';
    }
}
