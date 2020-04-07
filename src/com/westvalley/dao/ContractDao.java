package com.westvalley.dao;

import com.westvalley.consts.Mode;
import com.westvalley.entity.ContractEntity;
import com.westvalley.entity.PaymentEntity;
import com.westvalley.entity.ProjectEntity;
import com.westvalley.util.LogUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.formmode.setup.ModeRightInfo;

import java.math.BigDecimal;
import java.util.List;

public class ContractDao {

    private LogUtil log = LogUtil.getLogger(getClass());

    /**
     * 写入合同台账记录
     *
     * @param contractEntity
     */
    public String insertContract(ContractEntity contractEntity) {
        RecordSetTrans recordSet = new RecordSetTrans();
        try {
            recordSet.setAutoCommit(false);
            String insertSql = "insert into uf_YXHTTZ(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,reqid,htbh,htje,htmc,htqdf,htqsrq,fzbm,jbr,htzt,htlx,sfxhtja)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String formmodeid = Mode.getPayinfoId();
            String modedatacreater = contractEntity.getCreUser();
            String modedatacreatertype = "0";
            String modedatacreatedate = contractEntity.getCreDate();
            String modedatacreatetime = contractEntity.getCreTime();
            recordSet.executeUpdate(insertSql,
                    formmodeid,
                    modedatacreater,
                    modedatacreatertype,
                    modedatacreatedate,
                    modedatacreatetime,
                    contractEntity.getReqId(),
                    contractEntity.getHtbh(),
                    contractEntity.getHtje() + "",
                    contractEntity.getHtmc(),
                    contractEntity.getHtqdf(),
                    contractEntity.getHtqsrq(),
                    contractEntity.getFzbm(),
                    contractEntity.getJbr(),
                    contractEntity.getHtzt(),
                    contractEntity.getHtlx(),
                    contractEntity.getSfxhtja()
            );
            int mainId = getMainId(contractEntity.getReqId());
            if (contractEntity.getFkjh() != null && contractEntity.getFkjh().size() > 0) {
                String insertSql2 = "insert into uf_YXHTTZ_dt1(mainid,reqid,fkqs,bz,fkje,sl,fktj,fkzt,yfje,syje)" +
                        " values(?,?,?,?,?,?,?,?,?,?)";
                for (PaymentEntity paymentEntity : contractEntity.getFkjh()) {
                    recordSet.executeUpdate(insertSql2,
                            mainId,
                            paymentEntity.getReqId(),
                            paymentEntity.getFkqs(),
                            paymentEntity.getBz(),
                            paymentEntity.getFkje() + "",
                            paymentEntity.getSl(),
                            paymentEntity.getFktj(),
                            paymentEntity.getFkzt(),
                            paymentEntity.getYfje(),
                            paymentEntity.getSyje()
                    );
                }
            }
            if (contractEntity.getXmxx() != null && contractEntity.getXmxx().size() > 0) {
                String insertSql2 = "insert into uf_YXHTTZ_dt2(mainid,xmbh,xmmc,ftje,ftbl,xmjl,sjxmjl,bz)" +
                        " values(?,?,?,?,?,?,?,?)";
                for (ProjectEntity projectEntity : contractEntity.getXmxx()) {
                    recordSet.executeUpdate(insertSql2,
                            mainId,
                            projectEntity.getXmbh(),
                            projectEntity.getXmmc(),
                            projectEntity.getFtje() + "",
                            projectEntity.getFtbl() + "",
                            projectEntity.getXmjl(),
                            projectEntity.getSjxmli(),
                            projectEntity.getBz()
                    );
                }
            }
            recordSet.commit();
            RecordSet rs = new RecordSet();
            rs.executeQuery("select id from uf_YXHTTZ where reqid = ? ", contractEntity.getReqId());
            log.d("sql === " + "select id from uf_YXHTTZ where reqid = " + contractEntity.getReqId());
            ModeRightInfo ModeRightInfo = new ModeRightInfo();
            ModeRightInfo.setNewRight(true);
            int formModeId = Integer.valueOf(Mode.getPayinfoId());
            log.d("formModeId ==", formModeId);
            while (rs.next()) {
                //刷新建模权限
                ModeRightInfo.editModeDataShare(1, formModeId, rs.getInt(1));
            }
            return "";
        } catch (Exception e) {
            recordSet.rollback();
            e.printStackTrace();
            log.e("写入台账表发生错误：", e);
            return "写入合同台账记录发生错误";
        }
    }

    /**
     * 通过requestId获取主表数据id
     *
     * @param requestId
     * @return
     */
    public int getMainId(String requestId) {
        RecordSet recordSet = new RecordSet();
        String sql = "select id from uf_YXHTTZ where reqid = ?";
        recordSet.executeQuery(sql, requestId);
        return recordSet.next() ? recordSet.getInt("id") : null;
    }

    public String updateContractList(List<PaymentEntity> paymentEntityList) {
        RecordSetTrans recordSetTrans = new RecordSetTrans();
        recordSetTrans.setAutoCommit(false);//事务
        try {
            String selectSQL = "select * from uf_YXHTTZ_dt1 where id = ?";
            String updateSQL = "update uf_YXHTTZ_dt1 set yfje = ?,syje = ?,fkzt = ? where id = ?";
            for (PaymentEntity paymentEntity : paymentEntityList) {
                recordSetTrans.executeQuery(selectSQL, paymentEntity.getId());
                if (recordSetTrans.next()) {
                    BigDecimal yfje = new BigDecimal(recordSetTrans.getString("yfje"));
                    BigDecimal yfje2 = new BigDecimal(paymentEntity.getYfje());
                    BigDecimal syje = new BigDecimal(recordSetTrans.getString("syje"));
                    recordSetTrans.executeUpdate(
                            updateSQL,
                            yfje.add(yfje2).doubleValue(),
                            syje.subtract(yfje2).doubleValue(),
                            paymentEntity.getFkzt(),
                            paymentEntity.getId()
                    );
                }
            }
            recordSetTrans.commit();
        } catch (Exception e) {
            recordSetTrans.rollback();//回滚
            e.printStackTrace();
            log.e("系统发生错误：", e);
        }
        return "";
    }
}
