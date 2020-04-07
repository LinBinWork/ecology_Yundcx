package com.westvalley.project.dao;

import com.westvalley.project.dto.Proj0Dto;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.entity.Proj0Entity;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.project.enums.ExpTypeEnum;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.List;

/**
 * 祖项相关数据库操作
 */
public class Proj0Dao {


    public Proj0Dao() {

    }

    /**
     * 根据id获取祖项预算数据
     *
     * @param id
     * @return
     */
    public Proj0Entity getProj0ByID(int id) {
        return getProj0BudgetByID(id, null);
    }

    public Proj0Entity getProj0BudgetByID(int id, String requestID) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select p.*,b.orgName,b.orgDesp ");
        sb.append(" from uf_proj p  ");
        sb.append(" join uf_OrgProject b on p.projNo = b.orgCode  ");
        //虚拟祖项没有requestid
        sb.append(" left join workflow_requestbase wr on p.reqID = wr.requestid and wr.currentnodetype != 0   ");
        sb.append(" where p.projType = 0 and p.id = ?   ");

        Proj0Entity proj0 = null;
        RecordSet rs = new RecordSet();
        rs.executeQuery(sb.toString(), id);
        if (rs.next()) {
            proj0 = new Proj0Entity();
            proj0.setId(rs.getInt("id"));
            proj0.setProjNo(rs.getString("projNo"));
            proj0.setProjDeptNo(rs.getString("projDeptNo"));
            proj0.setProjName(rs.getString("orgName"));
            proj0.setProjDesc(rs.getString("orgDesp"));
            proj0.setProjYear(rs.getInt("projYear"));
            proj0.setProjAmt(Util.getDoubleValue(rs.getString("projAmt"),0));
            proj0.setCtrlLevel(EnumsUtil.getCtrlLevelEnum(Util.getIntValue(rs.getString("ctrlLevel"))));
            proj0.setCreDate(rs.getString("creDate"));
            proj0.setCreTime(rs.getString("creTime"));
            proj0.setCreUser(rs.getString("creUser"));

            proj0.setProjFreezeAmt(0);
            proj0.setProjUsedAmt(0);
            proj0.setProjBalance(0);
        }
        if (proj0 != null) {
            //获取在途预算
//            proj0.setProjFreezeAmt(getProj1AmtByType(proj0.getId(), BudTypeEnum.FREZEE.getType(),requestID));


            double decrease = getProj0AmtByType(proj0.getId(), ExpTypeEnum.DECREASE, requestID);
            double increase = getProj0AmtByType(proj0.getId(), ExpTypeEnum.INCREASE, requestID);

            BigDecimal total = new BigDecimal(String.valueOf(proj0.getProjAmt()));
            BigDecimal decreaseAmt = new BigDecimal(String.valueOf(decrease));//减少的金额  正数
            BigDecimal increaseAmt = new BigDecimal(String.valueOf(increase));//增加的金额  负数
            //获取已使用预算
            proj0.setProjUsedAmt(decreaseAmt.add(increaseAmt).doubleValue());

            BigDecimal balance = total.subtract(decreaseAmt).subtract(increaseAmt);
            //获取剩余预算
            proj0.setProjBalance(balance.doubleValue());
        }
        return proj0;
    }


    /**
     * 创建祖项预算
     *
     * @return
     */
    public ResultDto createProj0Bud(List<Proj0Dto> Proj0DtoList) {
        ProjDao projDao = new ProjDao();
        return projDao.createProj0Bud(Proj0DtoList);
    }

    /**
     * 导入校验
     *
     * @param Proj0DtoList
     * @return
     */
    public ResultDto checkProj0Bud(List<Proj0Dto> Proj0DtoList) {
        //对于同一年度，如系统中已存在有数据，不允许再次导入
        StringBuilder msg = new StringBuilder();

        RecordSet rs = new RecordSet();
        for (Proj0Dto dto : Proj0DtoList) {
            rs.executeQuery("select p.* from uf_proj p join workflow_requestbase wr on p.reqID = wr.requestid and wr.currentnodetype != 0  where p.projNo = ? and p.projDeptNo = ? and p.projYear = ? and p.projType = 0",
                    dto.getProjNo(), dto.getProjDeptNo(), dto.getYear());
            if (rs.next()) {
                msg.append(" 祖项编码：").append(dto.getProjNo());
                msg.append(" 成本中心：").append(dto.getProjDeptNo());
                msg.append(" 年度：").append(dto.getYear());
                msg.append(" 已存在数据，不能二次导入！\n<br>");
            }
        }
        String s = msg.toString();
        return ResultDto.init("".equals(s), s, null);
    }


    /**
     * 根据祖项ID查询 在途/已使用 预算
     *
     * @param id
     * @param type
     * @return
     */
    public double getProj0AmtByType(int id, ExpTypeEnum type, String requestID) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select sum(p.useAmt) from wv_proj_excuDetail p  ");
        sb.append(" join workflow_requestbase wr on p.requestID = wr.requestid and wr.currentnodetype != 0   ");
        sb.append(" where p.projID = ?  and p.exptype = ? and p.projType = 0 ");
        RecordSet rs = new RecordSet();
        if (StringUtil.isEmpty(requestID)) {
            rs.executeQuery(sb.toString(), id, type.getType());
        } else {
            sb.append(" and p.requestID != ?  ");
            rs.executeQuery(sb.toString(), id, type.getType(), requestID);
        }
        double projAmt = 0;
        if (rs.next()) {
            projAmt = Util.getDoubleValue(rs.getString(1),0);
        }
        return projAmt;
    }

}
