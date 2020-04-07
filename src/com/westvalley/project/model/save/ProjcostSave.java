package com.westvalley.project.model.save;

import com.westvalley.project.dto.Proj0Dto;
import com.westvalley.project.service.Proj0Service;
import com.westvalley.util.LogUtil;
import weaver.conn.RecordSet;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.soa.workflow.request.RequestInfo;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 祖项成本中心保存后，如果是虚拟祖项，自动创建一个虚拟祖项
 */
public class ProjcostSave extends AbstractModeExpandJavaCode {

    private LogUtil log = LogUtil.getLogger(getClass());


    @Override
    public void doModeExpand(Map<String, Object> param) throws Exception {
        User user = (User)param.get("user");
        int billid = -1;//数据id
        int modeid = -1;//模块id
        RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");
        if(requestInfo!=null){
            billid = Util.getIntValue(requestInfo.getRequestid());
            modeid = Util.getIntValue(requestInfo.getWorkflowid());
            if(billid>0&&modeid>0){
                //判断是否是虚拟祖项
                RecordSet rs =new RecordSet();
                rs.executeQuery("select a.isVirtual from uf_projcost a  where a.id = ? ",billid);
                if(rs.next()){
                    int isVirtual = Util.getIntValue(rs.getString(1), -1);
                    if(isVirtual == 0) {
                        createVirtualProj0(billid,user.getUID());
                    }
                }
            }
        }
    }

    /**
     * 创建虚拟祖项
     * @param billid
     */
    public void createVirtualProj0(int billid,int userID){

        try {
            log.d("开始创建虚拟祖项",billid,userID);
            RecordSet rs =new RecordSet();
            rs.executeQuery("select a.*,b.orgName from uf_projcost a join uf_orgproject b on a.projNo = b.orgCode where a.id = ? ",billid);
            if(rs.next()){
                List<Proj0Dto> Proj0DtoList = new ArrayList<>();
                Proj0Dto dto = new Proj0Dto();
                dto.setRequestID("");
                dto.setDetailID("");
                dto.setProjNo(rs.getString("projNo"));
                dto.setProjDeptNo(rs.getString("costNo"));
                dto.setProjName(rs.getString("orgName")+"(虚拟)");
                dto.setProjAmt("0");
                dto.setYear(9999);
                dto.setYwlb(rs.getString("businessType"));
                dto.setCreDate(TimeUtil.getCurrentDateString());
                dto.setCreTime(TimeUtil.getOnlyCurrentTimeString());
                dto.setCreUser(""+userID);
                dto.setProjDesc("虚拟祖项");

                Proj0DtoList.add(dto);

                Proj0Service proj0Service = new Proj0Service();
                proj0Service.createProj0Bud(Proj0DtoList);
            }
            log.d("创建虚拟祖项完成");
        } catch (Exception e) {
            log.e("创建虚拟祖项失败",e);
        }
    }

}

