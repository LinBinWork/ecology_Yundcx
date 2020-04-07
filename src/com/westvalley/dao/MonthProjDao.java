package com.westvalley.dao;

import com.westvalley.dto.MonthProj;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.util.LogUtil;
import weaver.conn.RecordSetTrans;

import java.util.List;

/**
 * 月度项目预算数据库操作
 */
public class MonthProjDao {

    private LogUtil log = LogUtil.getLogger(getClass());


    /**
     * 导入校验，存在则更新数据，不存在则插入数据
     *
     * @param monthProjList
     * @return
     */
    public ResultDto checkMonthProj(List<MonthProj> monthProjList) {
        if (monthProjList == null || monthProjList.size() == 0) {
            return ResultDto.error("需要保存的monthProjList数据不能为空！");
        }
        RecordSetTrans recordSetTrans = new RecordSetTrans();
        recordSetTrans.setAutoCommit(false);//事务
        StringBuffer insert = new StringBuffer();
        insert.append(" insert into wv_month_proj_p0(requestID,detailID,projNo,projDeptNo,projYear,upYear,projDef,projAmt,ywlb,one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,creDate,creTime,creUser)");
        insert.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        String insertSql = insert.toString();
        log.d("insertSql === ",insertSql);
        StringBuffer update = new StringBuffer();
        update.append(" update wv_month_proj_p0 set requestID = ?,detailID = ?,projNo = ?,projDeptNo = ?,projYear = ?,upYear = ?,projDef = ?,projAmt = ?,ywlb = ?,one = ?,two = ?,three = ?,four = ?,five = ?,six = ?,seven = ?,eight = ?,nine = ?,ten = ?,eleven = ?,twelve = ?,updDate = ?,updTime = ?,updUser = ? where id = ?");
        String updateSql = update.toString();
        log.d("updateSql === ",updateSql);
        try {
            for (MonthProj monthProj : monthProjList) {
                String selectSql = "select id from wv_month_proj_p0 where projNo = '" + monthProj.getProjNo() + "' and projDeptNo = '" + monthProj.getProjDeptNo() + "' and projYear = '" + monthProj.getProjYear() + "'";
                log.d("selectSql === ",selectSql);
                recordSetTrans.executeQuery(selectSql);
                if (recordSetTrans.next()) {
                    //如果存在，更新数据
                    recordSetTrans.executeUpdate(updateSql,
                            monthProj.getRequestID(),
                            monthProj.getDetailID(),
                            monthProj.getProjNo(),
                            monthProj.getProjDeptNo(),
                            monthProj.getProjYear(),
                            monthProj.getUpYear(),
                            monthProj.getProjDef(),
                            monthProj.getProjAmt(),
                            monthProj.getYwlb(),
                            monthProj.getOne(),
                            monthProj.getTwo(),
                            monthProj.getThree(),
                            monthProj.getFour(),
                            monthProj.getFive(),
                            monthProj.getSix(),
                            monthProj.getSeven(),
                            monthProj.getEight(),
                            monthProj.getNine(),
                            monthProj.getTen(),
                            monthProj.getEleven(),
                            monthProj.getTwelve(),
                            monthProj.getCreDate(),
                            monthProj.getCreTime(),
                            monthProj.getCreUser(),
                            recordSetTrans.getString("id")
                    );
                } else {
                    //不存在，插入数据
                    recordSetTrans.executeUpdate(insertSql,
                            monthProj.getRequestID(),
                            monthProj.getDetailID(),
                            monthProj.getProjNo(),
                            monthProj.getProjDeptNo(),
                            monthProj.getProjYear(),
                            monthProj.getUpYear(),
                            monthProj.getProjDef(),
                            monthProj.getProjAmt(),
                            monthProj.getYwlb(),
                            monthProj.getOne(),
                            monthProj.getTwo(),
                            monthProj.getThree(),
                            monthProj.getFour(),
                            monthProj.getFive(),
                            monthProj.getSix(),
                            monthProj.getSeven(),
                            monthProj.getEight(),
                            monthProj.getNine(),
                            monthProj.getTen(),
                            monthProj.getEleven(),
                            monthProj.getTwelve(),
                            monthProj.getCreDate(),
                            monthProj.getCreTime(),
                            monthProj.getCreUser()
                    );
                }
            }
            recordSetTrans.commit();//提交
            return ResultDto.ok();
        } catch (Exception e) {
            recordSetTrans.rollback();//回滚
            log.e("导入月度项目预算数据发生错误：", e);
            return ResultDto.error("导入月度项目预算数据发生错误：", e.getMessage());
        }
    }

}
