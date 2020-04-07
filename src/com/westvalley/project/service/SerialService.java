package com.westvalley.project.service;

import weaver.conn.RecordSet;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 编号生成
 */
public class SerialService {

    public SerialService(){

    }

    /**
     *   前两位：根据祖项项目编号，如：01
     3位到6位：根据当前年月（年份仅取后2位），如：1906
     7位到8位：根据父项年度自然流水编号：如01
     9位到10位：根据子项项目年度自然流水号：如01
     11位到12位：如存在孙子项情况，根据孙子项项目年度自然流水号：如01
     * @return
     * @throws Exception
     */
    public String createProjectNo(String p0No,String p1No) throws Exception{
        if(p0No == null || p0No.length() != 2){
            throw new IllegalArgumentException("祖项编码不正确："+p0No);
        }
        if(p1No == null || p1No.length() != 2){
            throw new IllegalArgumentException("父项编码不正确："+p1No);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
        StringBuilder sb = new StringBuilder();
        sb.append(p0No).append(sdf.format(new Date())).append(p1No);
        return sb.toString();
    }


    /**
     * 创建子项编码
     * @param p1No
     * @param p2No
     * @return
     * @throws Exception
     */
    public String createProj2No(String p1No,String p2No) throws Exception{
        if(p1No == null || p1No.length() != 8){
            throw new IllegalArgumentException("父项编码不正确："+p1No);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
        StringBuilder sb = new StringBuilder();
        sb.append(p1No);
        if(p2No != null ){
            if(p2No.length() != 2){
                throw new IllegalArgumentException("子项自然流水不正确:"+p2No);
            }
            sb.append(p2No);
        }
        return sb.toString();
    }

    /**
     * 创建孙子项编码
     * @param p2No
     * @param p3No
     * @return
     * @throws Exception
     */
    public String createProj3No(String p2No,String p3No) throws Exception{
        if(p2No == null || p2No.length() != 10){
            throw new IllegalArgumentException("子项编码不正确："+p2No);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
        StringBuilder sb = new StringBuilder();
        sb.append(p2No);
        if(p3No != null ){
            if(p3No.length() != 2){
                throw new IllegalArgumentException("孙子项自然流水不正确:"+p3No);
            }
            sb.append(p3No);
        }
        return sb.toString();
    }


    /**
     * 生成该祖项在当年度下的流水
     * @param proj0No 祖项编码
     * @param year 祖项年度
     * @return
     */
    public String createProjNo(String proj0No,int year){
        RecordSet rs = new RecordSet();
        rs.executeQuery("select count(id ) from uf_proj  where pid in (select id from uf_proj p1 where p1.projNo = ?  and p1.projYear = ?)",proj0No,year);
        String no = "";
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        if(rs.next()){
            int count = rs.getInt(1);
            if(curYear == 2019){//项目初次上线，之前已发生流水，所以直接从50开始
                count = count + 49;
            }
            count++;
            no = Util.add0(count,2);
        }
        return no;
    }

    /**
     * 生成根据父ID生成子流水
     * @param pID 父ID
     * @return
     */
    public int createProjNo(int pID){
        RecordSet rs = new RecordSet();
        rs.executeQuery("select count(p3.id) from uf_proj p3 where p3.pid = ? ",pID);
        int no = -1;
        if(rs.next()){
            int count = rs.getInt(1);
            count ++ ;
            no = count;
        }
        return no;
    }

}
