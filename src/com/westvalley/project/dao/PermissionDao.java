package com.westvalley.project.dao;

import com.westvalley.project.Const;
import com.westvalley.project.dto.*;
import com.westvalley.project.enums.BudTypeEnum;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.TimeUtil;
import weaver.general.Util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 项目权限默认
 */
public class PermissionDao {
    private LogUtil log = LogUtil.getLogger(getClass());

    public PermissionDao(){
    }

    public ResultDto savePermission(List<PermissionDto> permissionDtoList){
        if(permissionDtoList == null || permissionDtoList.size() == 0){
            log.d("需要保存的permissionDtoList数据不能为空！");
            return ResultDto.error("需要保存的permissionDtoList数据不能为空！");
        }
        log.d("permissionDtoList",permissionDtoList.size(),permissionDtoList);
        String formmodeid = Const.getProjPermisionmodeid();
        String modedatacreater = "";
        String modedatacreatertype = "0";
        String modedatacreatedate = TimeUtil.getCurrentDateString();
        String modedatacreatetime = TimeUtil.getOnlyCurrentTimeString();

        StringBuilder sb = new StringBuilder();

        sb.append(" insert into uf_projshare(");
        sb.append(" formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,");
        sb.append(" projName,projNo,splitPermission,persion");
        sb.append(" )values(?,?,?,?,?,?,?,?,?)  ");

        String addSql = sb.toString();
        RecordSetTrans trans = new RecordSetTrans();
        trans.setAutoCommit(false);

        try {
            String projIDs = "";
            for (PermissionDto dto : permissionDtoList) {
                if("".equals(projIDs)){
                    projIDs = ""+dto.getProjID();
                }else{
                    projIDs += ",";
                    projIDs += dto.getProjID();
                }
            }
            trans.executeUpdate("delete from uf_projshare where projName in("+projIDs+") ");

            String initPerssionUsers = getInitPerssionUsers();

            for (PermissionDto dto : permissionDtoList) {
                modedatacreater =  dto.getProjManager();

                String projPersons = dto.getProjPersons();
                if(!StringUtil.isEmpty(initPerssionUsers)) {
                    if (StringUtil.isEmpty(projPersons)) {
                        projPersons = initPerssionUsers;
                    } else {
                        projPersons = projPersons + "," + initPerssionUsers;
                    }
                }
                trans.executeUpdate(addSql,
                        formmodeid,
                        modedatacreater,
                        modedatacreatertype,
                        modedatacreatedate,
                        modedatacreatetime,
                        dto.getProjID(),
                        dto.getProjNo(),
                        dto.getProjSplits(),
                        projPersons
                );
            }
            trans.commit();

            ModeRightInfo ModeRightInfo = new ModeRightInfo();
            ModeRightInfo.setNewRight(true);
            //刷新权限
            RecordSet rs = new RecordSet();
            for (PermissionDto dto : permissionDtoList) {
                rs.executeQuery("select id from uf_projshare where projName = ? ",dto.getProjID());
                if(rs.next()){
                    ModeRightInfo.editModeDataShare(Util.getIntValue(dto.getProjManager()),Util.getIntValue(formmodeid),Util.getIntValue(rs.getString(1)));
                }
            }
        } catch (Exception e) {
            trans.rollback();
            String msg = "初始化项目权限出错";
            log.e(msg,e);
            return ResultDto.error(msg+e.getMessage());
        }
        return ResultDto.ok("初始化项目权限成功！");
    }

    /**
     * 获取初始权限人员
     * @return
     */
    public String getInitPerssionUsers(){
        String roleid = Const.getProjPermisionRoleid();
        RecordSet rs = new RecordSet();
        Set<String> ids = new HashSet<>();
        rs.executeQuery("select resourceid from hrmrolemembers where roleid =  ?",roleid);
        while(rs.next()){
            ids.add(rs.getString(1));
        }
        return StringUtil.list2Str(ids);
    }
}
