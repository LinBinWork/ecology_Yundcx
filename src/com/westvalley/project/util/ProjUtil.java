package com.westvalley.project.util;

import com.alibaba.fastjson.JSONObject;
import com.westvalley.project.dto.ResultDto;
import com.westvalley.project.enums.CtrlLevelEnum;
import com.westvalley.project.enums.ProjtypeEnum;
import com.westvalley.util.DevUtil;
import com.westvalley.util.LogUtil;
import com.westvalley.util.StringUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目工具
 */
public class ProjUtil {
    /**
     * 获取父项ID
     * @param projID 孙子项/子项
     * @return 父项ID
     */
    public static int getProj1ID4Child(int projID){
        int proj1Id = 0;
        RecordSet rs = new RecordSet();
        rs.executeQuery("select pid,projtype from uf_proj where id = ? and projtype in(2,3)",projID);
        if(rs.next()){
            proj1Id = rs.getInt(1);//上级ID
            int projtype = rs.getInt(2);
            //如果当前类型为孙子项，则还要网上找一次
            if(EnumsUtil.getProjtypeEnum(projtype).compareTo(ProjtypeEnum.GRANDSON) == 0){
                rs.executeQuery("select pid from uf_proj where id = ? ",proj1Id);
                if(rs.next()){
                    proj1Id = rs.getInt(1);
                }
            }
        }
        return proj1Id;
    }

    /**
     * 获取该项目下的所有子项目 不包含本身
     * @param projID 项目ID
     * @return 子项目ID集合
     */
    public static List<Integer> getChilrenProjIDs(int projID){
        List<Integer> ids = new ArrayList<>();

        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();
        RecordSet rs3 = new RecordSet();
        //最多查3次
        rs1.executeQuery("select id from uf_proj where pid = ? ",projID);
        while(rs1.next()){
            int id1 = rs1.getInt(1);
            ids.add(id1);

            rs2.executeQuery("select id from uf_proj where pid = ? ",id1);
            while(rs2.next()){
                int id2 = rs2.getInt(1);
                ids.add(id2);

                rs3.executeQuery("select id from uf_proj where pid = ? ",id2);
                while(rs3.next()){
                    ids.add(rs3.getInt(1));
                }
            }
        }
        return ids;
    }


    /**
     * 校验该项目下的所有项目是否已关闭
     * @param projID
     * @return
     */
    public static ResultDto isAllChildrenClose(int projID,String requestID){

        List<Integer> chilrenProjIDs = getChilrenProjIDs(projID);
        if(chilrenProjIDs.size() == 0){
            return ResultDto.ok();
        }
        LogUtil log = LogUtil.getLogger(ProjUtil.class);
        String ids = StringUtil.list2Str(chilrenProjIDs);
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.projNo,a.projName,b.projid,b.requestid ");
        sb.append(" from uf_proj a  ");
        sb.append(" join wv_proj_excuDetail b on a.id = b.projid  ");
        sb.append(" join workflow_requestbase c on c.requestid = b.requestid and c.currentnodetype != 0 ");
        sb.append(" where b.useType in(1,4) ");
        sb.append(" and b.id in(").append(ids).append(") and b.requestid != ?");

        String sql = sb.toString();
        RecordSet rs = new RecordSet();
        log.d("isAllChildrenClose",sql,requestID);
        rs.executeQuery(sql,requestID);
        if(rs.next()){
            return ResultDto.error("当前项目及所有下级项目中存在【在途流程】！");
        }
        return ResultDto.ok();
    }

    /**
     * 判断项目是否是父端控制
     * @param projID
     * @return
     */
    public static boolean isParentCtrlLevel(int projID){
        String ctrlLevel = DevUtil.executeQuery("select ctrlLevel from uf_proj where id = ? ", projID);
        if(Util.getIntValue(ctrlLevel) == CtrlLevelEnum.PARENT.getLevel()){
            return true;
        }
        return false;
    }

    /**
     * 获取项目创建时的流程请求ID  虚拟祖项会不存在这个数据
     * @param projID
     * @return
     */
    public static String getProjCreRequestID(int projID){

        return DevUtil.executeQuery("select reqID from uf_proj where id = ? ", projID);
    }
}
