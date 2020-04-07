package com.westvalley.project.dao;

import com.westvalley.project.Const;
import com.westvalley.project.dto.*;
import com.westvalley.project.enums.ProjtypeEnum;
import com.westvalley.project.po.BudgetExcuPo;
import com.westvalley.project.po.Proj0Po;
import com.westvalley.project.util.EnumsUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有项目
 */
public class ProjDao {

    private LogUtil log = LogUtil.getLogger(getClass());


    /**
     * 获取祖项标准数据
     * @param no 祖项编码
     * @return
     */
    public Proj0Po getProj0ByNo(String no){
        Proj0Po proj0Po =  null;

        RecordSet rs = new RecordSet();
        rs.executeQuery("select a.*,b.ctrlLevel from uf_orgproject a left join uf_projctrl b on a.companyCode=b.companyCode where a.orgCode = ? ",no);
        if(rs.next()){
            proj0Po = new Proj0Po();
            proj0Po.setCompanyCode(rs.getString("companyCode"));
            proj0Po.setOrgCode(rs.getString("orgCode"));
            proj0Po.setOrgName(rs.getString("orgName"));
            proj0Po.setOrgDesp(rs.getString("orgDesp"));
            proj0Po.setIsOepn(rs.getString("isOepn"));
            proj0Po.setIsPlan(rs.getString("isPlan"));
            proj0Po.setOrgType(rs.getString("orgType"));
            proj0Po.setCtrlLevel(EnumsUtil.getCtrlLevelEnum(Util.getIntValue(rs.getString("ctrlLevel"))));
        }
        return proj0Po;
    }






    /**
     * 获取祖项编码
     * @param id 祖项ID
     * @return
     */
    public String getProj0No4p1(int id){
        String no =  "";

        RecordSet rs = new RecordSet();
        rs.executeQuery("select a.projNo from  uf_proj a where a.id = ? ",id);
        if(rs.next()){
            no = rs.getString("projNo");
        }
        return no;
    }

    /**
     * 获取祖项编码
     * @param p1id 父ID
     * @return
     */
    public String getProj0No4p2(int p1id){
        String no =  "";

        RecordSet rs = new RecordSet();
        rs.executeQuery("select a.pid from  uf_proj a where a.id = ? ",p1id);
        if(rs.next()){
            int pid = Util.getIntValue(rs.getString(1));
            no = getProj0No4p1(pid);
        }
        return no;
    }

    /**
     * 获取祖项编码
     * @param p2id 子ID
     * @return
     */
    public String getProj0No4p3(int p2id){
        String no =  "";

        RecordSet rs = new RecordSet();
        rs.executeQuery("select a.pid from  uf_proj a where a.id = ? ",p2id);
        if(rs.next()){
            int pid = Util.getIntValue(rs.getString(1));
            no = getProj0No4p2(pid);
        }
        return no;
    }

    /**
     * 创建项目预算
     * @return
     */
    public ResultDto createProjBud(List<ProjDto> ProjDtoList){
        if(ProjDtoList == null || ProjDtoList.size() == 0){
            log.d("需要保存的ProjDtoList数据不能为空！");
            return ResultDto.error("需要保存的ProjDtoList数据不能为空！");
        }
        log.d("ProjDtoList",ProjDtoList.size(),ProjDtoList);
        String formmodeid = Const.getProjModelID();
        String modedatacreater = "";
        String modedatacreatertype = "0";
        String modedatacreatedate = "";
        String modedatacreatetime = "";

        StringBuilder sb = new StringBuilder();
        sb.append(" insert into uf_proj(");
        sb.append(" formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,");
        sb.append(" reqID,detailID,pID,companyNo,projNo,projDeptNo,projName,projDesc,projType,projCreAmt,projAmt,projYear,ctrlLevel,projDate,projManager,projPerson,projExcuer,projExcustatus,projCategory,deliveryCenter,businessType,");
        sb.append(" creDate,creTime,creUser,releaseAmt,modDate,modTime,remark");
        sb.append(" )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");

        String addSql = sb.toString();
        RecordSetTrans trans = new RecordSetTrans();
        trans.setAutoCommit(false);

        try {
            RecordSet rs = new RecordSet();
            for (ProjDto dto : ProjDtoList) {
                modedatacreater =  dto.getCreUser();
                modedatacreatedate = dto.getCreDate();
                modedatacreatetime = dto.getCreTime();

                //删除重复数据
//                rs.executeQuery("select id from uf_proj where pID =? and projNo = ? and projType = ? and projDeptNo = ? ",
//                        dto.getPID(),
//                        dto.getProjNo(),
//                        dto.getProjType(),
//                        dto.getProjDeptNo());
//                if(rs.next()){
//                    trans.executeUpdate("delete from uf_proj where pID =? and projNo = ? and projType = ? and projDeptNo = ? ");
//                }

                String projAmt = String.valueOf(dto.getProjAmt());
                if(projAmt.contains("E")){//如果是科学计数
                    projAmt = new BigDecimal(dto.getProjAmt()).toString();
                }

                trans.executeUpdate(addSql,
                        formmodeid,
                        modedatacreater,
                        modedatacreatertype,
                        modedatacreatedate,
                        modedatacreatetime,

                        dto.getReqID(),
                        dto.getDetailID(),
                        dto.getPID(),
                        dto.getCompanyNo(),
                        dto.getProjNo(),
                        dto.getProjDeptNo(),
                        dto.getProjName(),
                        Util.null2String(dto.getProjDesc()),
                        dto.getProjType(),
                        projAmt,
                        projAmt,
                        dto.getProjYear(),
                        dto.getCtrlLevel().getLevel(),
                        dto.getProjDate(),
                        Util.null2String(dto.getProjManager()),
                        Util.null2String(dto.getProjPerson()),
                        Util.null2String(dto.getProjExcuer()),
                        dto.getProjExcustatus(),
                        Util.null2String(dto.getProjCategory()),
                        dto.getDeliveryCenter(),
                        dto.getBusinessType(),

                        dto.getCreDate(),
                        dto.getCreTime(),
                        dto.getCreUser(),
                        dto.getReleaseAmt(),
                        dto.getModDate(),
                        dto.getModTime(),
                        Util.null2String(dto.getRemark())
                );
            }
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            String msg = "导入项目预算出错";
            log.e(msg,e);
            return ResultDto.error(msg+e.getMessage());
        }
        return ResultDto.ok();
    }

    /**
     * 创建祖项
     * @param ProjDto0List
     * @return
     */
    public ResultDto createProj0Bud(List<Proj0Dto> ProjDto0List){
        List<ProjDto> ProjDtoList = new ArrayList<>();
        for (Proj0Dto dto0 : ProjDto0List) {
            ProjDto dto = new ProjDto();




            dto.setReqID(dto0.getRequestID());
            dto.setDetailID(dto0.getDetailID());
            dto.setPID(0);

            dto.setProjNo(dto0.getProjNo());
            dto.setProjDeptNo(dto0.getProjDeptNo());
            dto.setProjName(dto0.getProjName());
            dto.setProjDesc(StringUtil.toStr(dto0.getProjDesc()));
            dto.setProjType(0);
            dto.setProjAmt(dto0.getProjAmt());
            dto.setCreDate(dto0.getCreDate());
            dto.setCreTime(dto0.getCreTime());
            dto.setCreUser(dto0.getCreUser());

            Proj0Po proj0ByNo = getProj0ByNo(dto0.getProjNo());
            if(proj0ByNo != null){
                dto.setCompanyNo(proj0ByNo.getCompanyCode());
                dto.setCtrlLevel(proj0ByNo.getCtrlLevel());
            }
//            dto.setReleaseAmt();
//            dto.setModDate();
//            dto.setModTime();
//            dto.setRemark();
            dto.setBusinessType(dto0.getYwlb());
            dto.setProjYear(dto0.getYear());
            ProjDtoList.add(dto);
        }
        return createProjBud(ProjDtoList);
    }


    /**
     * 创建父项
     * @param ProjDto1List
     * @return
     */
    public ResultDto createProj1Bud(List<Proj1Dto> ProjDto1List){
        List<ProjDto> ProjDtoList = new ArrayList<>();
        for (Proj1Dto dto1 : ProjDto1List) {
            ProjDto dto = new ProjDto();

            dto.setReqID(dto1.getRequestID());
            dto.setDetailID(dto1.getDetailID());
            dto.setPID(dto1.getPID());
            dto.setProjNo(dto1.getProjNo());
            dto.setProjDeptNo(dto1.getProjDeptNo());
            dto.setProjName(dto1.getProjName());
            dto.setProjDesc(dto1.getProjDesc());
            dto.setProjType(1);
            dto.setProjAmt(dto1.getProjAmt());
            dto.setProjDate(dto1.getProjDate());
            dto.setCreDate(dto1.getCreDate());
            dto.setCreTime(dto1.getCreTime());
            dto.setCreUser(dto1.getCreUser());
            dto.setProjManager(dto1.getProjManager());
            dto.setProjPerson(dto1.getProjPerson());
            dto.setProjCategory(dto1.getProjCategory());

            Proj0Po proj0ByNo = getProj0ByNo(getProj0No4p1(dto1.getPID()));
            if(proj0ByNo != null){
                dto.setCompanyNo(proj0ByNo.getCompanyCode());
                dto.setCtrlLevel(proj0ByNo.getCtrlLevel());
            }

//            dto.setReleaseAmt();
//            dto.setModDate();
//            dto.setModTime();
//            dto.setRemark();
//            dto.setProjYear(dto1.getYear());
            ProjDtoList.add(dto);
        }
        return createProjBud(ProjDtoList);
    }
    /**
     * 创建子项
     * @param ProjDto2List
     * @return
     */
    public ResultDto createProj2Bud(List<Proj2Dto> ProjDto2List){
        List<ProjDto> ProjDtoList = new ArrayList<>();
        for (Proj2Dto dto2 : ProjDto2List) {
            ProjDto dto = new ProjDto();

            dto.setReqID(dto2.getRequestID());
            dto.setDetailID(dto2.getDetailID());
            dto.setPID(dto2.getPID());
            dto.setProjNo(dto2.getProjNo());
            dto.setProjDeptNo(dto2.getProjDeptNo());
            dto.setProjName(dto2.getProjName());
            dto.setProjDesc(dto2.getProjDesc());
            dto.setProjType(2);
            dto.setProjAmt(dto2.getProjAmt());
            dto.setProjDate(dto2.getProjDate());
            dto.setCreDate(dto2.getCreDate());
            dto.setCreTime(dto2.getCreTime());
            dto.setCreUser(dto2.getCreUser());
            dto.setProjManager(dto2.getProjManager());
            dto.setProjPerson(dto2.getProjPerson());
            dto.setProjExcuer(dto2.getProjExcuer());
            dto.setProjExcustatus(dto2.getProjExcustatus());
            dto.setDeliveryCenter(dto2.getDeliveryCenter());

            Proj0Po proj0ByNo = getProj0ByNo(getProj0No4p2(dto2.getPID()));
            if(proj0ByNo != null){
                dto.setCompanyNo(proj0ByNo.getCompanyCode());
                dto.setCtrlLevel(proj0ByNo.getCtrlLevel());
            }

//            dto.setReleaseAmt();
//            dto.setModDate();
//            dto.setModTime();
//            dto.setRemark();
//            dto.setProjYear(dto1.getYear());
            ProjDtoList.add(dto);
        }
        return createProjBud(ProjDtoList);
    }
    /**
     * 创建孙子项
     * @param ProjDto3List
     * @return
     */
    public ResultDto createProj3Bud(List<Proj3Dto> ProjDto3List){
        List<ProjDto> ProjDtoList = new ArrayList<>();
        for (Proj3Dto dto3 : ProjDto3List) {
            ProjDto dto = new ProjDto();

            dto.setReqID(dto3.getRequestID());
            dto.setDetailID(dto3.getDetailID());
            dto.setPID(dto3.getPID());
            dto.setProjNo(dto3.getProjNo());
            dto.setProjDeptNo(dto3.getProjDeptNo());
            dto.setProjName(dto3.getProjName());
            dto.setProjDesc(dto3.getProjDesc());
            dto.setProjType(3);
            dto.setProjAmt(dto3.getProjAmt());
            dto.setProjDate(dto3.getProjDate());
            dto.setCreDate(dto3.getCreDate());
            dto.setCreTime(dto3.getCreTime());
            dto.setCreUser(dto3.getCreUser());
            dto.setProjManager(dto3.getProjManager());
            dto.setProjPerson(dto3.getProjPerson());
            dto.setProjExcuer(dto3.getProjExcuer());
            dto.setDeliveryCenter(1);//默认为否
            Proj0Po proj0ByNo = getProj0ByNo(getProj0No4p3(dto3.getPID()));
            if(proj0ByNo != null){
                dto.setCompanyNo(proj0ByNo.getCompanyCode());
                dto.setCtrlLevel(proj0ByNo.getCtrlLevel());
            }
//            dto.setReleaseAmt();
//            dto.setModDate();
//            dto.setModTime();
//            dto.setRemark();
//            dto.setProjYear(dto1.getYear());
            ProjDtoList.add(dto);
        }
        return createProjBud(ProjDtoList);
    }

    /**
     * 更新
     * @param excuPoList
     * @return
     */
    public ResultDto closeProj(List<BudgetExcuPo> excuPoList){
        log.d("closeProj",excuPoList);
        if(excuPoList == null || excuPoList.size() == 0){
            return ResultDto.error("关闭项目数据不能为空！");
        }
        log.d("关闭项目总条数",excuPoList.size());
        RecordSetTrans trans = new RecordSetTrans();
        trans.setAutoCommit(false);
        try {
            String closeSQL = " update uf_proj set projClose = ? where id = ? ";
            for (BudgetExcuPo po : excuPoList) {
                //祖项是不需要关闭的
                if(ProjtypeEnum.ORG.compareTo(po.getProjtype()) == 0){
                    continue;
                }
                trans.executeUpdate(closeSQL,'0',po.getProjID());
            }
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            String msg = "关闭项目出错";
            log.e(msg,e);
            return ResultDto.error(msg+e.getMessage());
        }
        return ResultDto.ok();
    }
}
