package com.customization.sap;

import com.sap.conn.jco.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SapFunction {
    public SapConn initSapConn() {
        SapConn sapConn = new SapConn();
        return sapConn;
    }

    public Map<String,SapTable> getSapData(String functionName, Map<String,String> params , List<SapTable> tables){
        Map<String,SapTable> tableMap = new HashMap<>();
        try {
            JCoDestination jCoDestination = SapConnUtils.connect(initSapConn());
            JCoFunction function = jCoDestination.getRepository().getFunction(functionName);
            JCoParameterList importParameterList = function.getImportParameterList(); //获取入参参数
            JCoParameterList tableParameterList = function.getTableParameterList();   //获取内表参数
            //封装内表
            if(tables != null && tables.size() > 0){
                for (SapTable sapTable : tables){
                    JCoTable table = tableParameterList.getTable(sapTable.getTableName()); //定位表
                    for (int i=0; i<sapTable.getSize();i++){
                        table.setRow(i);        //定位行
                        for (int j = 0 ;j<sapTable.getFieldCount();j++){
                            String key = sapTable.getFieldName().get(j);
                            String value = sapTable.getData().get(i).get(key);
                            table.setValue(key,value);
                        }
                    }
                }
            }
            //封装 入参
            if(params!=null && params.size() > 0){
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    importParameterList.setValue(key,value);
                }
            }
            function.execute(jCoDestination);


            //获取输出结果
           for(JCoField jCoField : tableParameterList){
               JCoTable table = jCoField.getTable();
               String tableName = jCoField.getName(); //表名
               List<String> keys = getKeys(table);  //获取键值
               int numRows = table.getNumRows();      //行数
               int fieldCount = table.getFieldCount(); //列数
               List<Map<String,String>> data = new ArrayList<>();
               for (int i=0;i<numRows;i++){
                   table.setRow(i);
                   Map<String,String> map  = new HashMap<>();
                   for (int j=0;j<fieldCount;j++){
                       String key = keys.get(j);
                       String value = table.getString(key);
                       map.put(key,value);
                   }
                   data.add(map);
               }
               SapTable sapTable = new SapTable(tableName, data); //封装数据表
              tableMap.put(tableName,sapTable);
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return tableMap;
    }

    public List<String> getKeys(JCoTable jCoTable){
        JCoRecordMetaData recordMetaData = jCoTable.getRecordMetaData();
        int fieldCount = recordMetaData.getFieldCount();
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < fieldCount; i++) {
            String key = recordMetaData.getName(i);
            keys.add(key);
        }
        return keys;
    }
}
