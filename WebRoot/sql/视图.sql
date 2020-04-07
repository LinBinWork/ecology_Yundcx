
----根据父项ID带出分摊明细视图
drop view WV_V_Apportion;
create view WV_V_Apportion as
select * from (
select t1.id,t3.ftbm,t4.Ltext,t3.bclxje,t3.ftbl,t3.ftbmfzr,t5.lastname from uf_proj t1
left join formtable_main_18 t2 on t1.reqID = t2.requestId
left join formtable_main_18_dt2 t3 on t2.id = t3.mainid
left join (select a.id,case a.projyear when 9999 then null  else a.projyear end projyear ,b.LTEXT,b.KOSTL
from uf_proj  a
join OA_SAP_Cbzx  b on a.projDeptNo = b.KOSTL where a.projType = 0) t4 on t4.id = t3.ftbm
left join hrmresource t5 on t5.id = t3.ftbmfzr
) T

----子项视图
drop view WV_V_Subitem
create view WV_V_Subitem as
select * from (
select t1.id,t2.fxxmjl,t3.zxxmjl,t4.lastname as 'zxxmName'
from uf_proj t1
left join formtable_main_21 t2 on t1.reqID = t2.requestId
left join formtable_main_21_dt1 t3 on t2.id = t3.mainid
left join hrmresource t4 on t4.id = t3.zxxmjl
)T

----孙子项视图
drop view WV_V_Grandson;
create view WV_V_Grandson as
select * from (
select t1.id,t2.zxxmjl,t2.fxxmjl,t4.lastname as 'fxxmName',t5.lastname as 'zxxmName'
from uf_proj t1
left join formtable_main_25 t2 on t1.reqID = t2.requestId
left join formtable_main_25_dt1  t3 on t2.id = t3.mainid
left join hrmresource t4 on t4.id = t2.zxxmjl
left join hrmresource t5 on t5.id = t2.fxxmjl
)T

----子项项目经理视图
drop view WV_V_SubitemManager;
create view WV_V_SubitemManager as
select * from (
select t1.id,t2.zxxmjl as 'xmjl',t2.zxxmfzr as 'zxxmfzr',t4.lastname as 'jlname',t5.lastname as 'zxxmfzrname' from uf_proj t1
left join formtable_main_21_dt1 t2 on t1.detailID = t2.id
left join hrmresource t4 on t2.zxxmjl = t4.id
left join hrmresource t5 on t2.zxxmfzr = t5.id
)T


----子项项目经理,上级项目经理
drop view WV_V_SubitemProject;
create view WV_V_SubitemProject as
select * from (
select t1.id,t2.zxxmjl as 'zxxmjl',t3.fxxmjl as 'fxxmjl',t4.lastname as 'zxxmName',t7.lastname as 'fxxmName' from uf_proj t1
left join formtable_main_21_dt1 t2 on t1.detailID = t2.id
left join formtable_main_21 t3 on t2.mainid = t3.id
left join hrmresource t4 on t2.zxxmjl = t4.id
left join uf_proj t5 on t3.glfx = t5.id
left join formtable_main_18 t6 on t6.requestId = t5.reqID
left join hrmresource t7 on t6.xmjl = t7.id
)T


----子项项目经理,上级项目经理
drop view WV_V_SubitemProject;
create view WV_V_SubitemProject as
select * from (
select t1.id,t2.zxxmjl as 'zxxmjl',t3.fxxmjl as 'fxxmjl',t4.lastname as 'zxxmName',t7.lastname as 'fxxmName' from uf_proj t1
left join formtable_main_21_dt1 t2 on t1.detailID = t2.id
left join formtable_main_21 t3 on t2.mainid = t3.id
left join hrmresource t4 on t2.zxxmjl = t4.id
left join uf_proj t5 on t3.glfx = t5.id
left join formtable_main_18 t6 on t6.requestId = t5.reqID
left join hrmresource t7 on t6.xmjl = t7.id
)T

----孙子项项目经理,上级项目经理
drop view WV_V_GrandsonProject;
create view WV_V_GrandsonProject as
select * from (
select t1.id,t2.szxxmjl as 'zxxmjl',t3.zxxmjl as 'fxxmjl',t4.lastname as 'zxxmName',t5.lastname as 'fxxmName' from uf_proj t1
left join formtable_main_25_dt1 t2 on t1.detailID = t2.id
left join formtable_main_25 t3 on t2.mainid = t3.id
left join hrmresource t4 on t2.szxxmjl = t4.id
left join hrmresource t5 on t3.zxxmjl = t5.id
)T

------查出项目是否需要执行方案
drop view WV_V_Execute;
create view WV_V_Execute as
select * from (
select t1.id as 'id',t1.projName as 'projName' from uf_proj t1
left join formtable_main_21_dt1 t2 on t1.projNo = t2.zxxmbh
where t2.sffazx = '0'
)T

------存在需要执行方案申请的项目
drop view WV_V_ExistExecute;
create view WV_V_ExistExecute as
select * from (
select t1.glxm from formtable_main_35 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype = '3'
)T

------未完结的报销流程
drop view WV_V_ExistExpense;
create view WV_V_ExistExpense as
select * from (
select t1.id as 'id',t1.projName as 'pName',t2.* from uf_proj t1
left join uf_proj t2 on t1.id = t2.pid
left join formtable_main_45 t3 on t2.id = t3.xmbh
left join workflow_requestbase t4 on t3.requestId = t4.requestid
where t2.id is not null and t4.currentnodetype != '3' and t4.currentnodetype != '0'
)T

----项目分摊明细视图
drop view WV_V_XMFTMX
create view WV_V_XMFTMX as
select * from (
select t1.requestId,xmbh,projNo,ftje,ftbl from formtable_main_26 t1
left join formtable_main_26_dt2 t2 on t1.id = t2.mainid
left join uf_proj t3 on t2.xmbh = t3.id
)T

-----祖项浏览按钮
create view WV_V_zx as
select * from (
select distinct projNo,orgCode,orgName,businessType as 'business',case businessType when '0' then '新零售' when '1' then '海外'when '2' then '品牌运营中心' else '其他' end as 'businessType',case allUse when '0' then '是' else '否' end as 'allUse' from uf_OrgProject t1,uf_projcost t2 where t1.orgCode = t2.projNo
 union all
 select distinct projNo,orgCode,orgName,businessType as 'business',case businessType when '0' then '新零售' when '1' then '海外'when '2' then '品牌运营中心' else '其他' end as 'businessType',case allUse when '0' then '是' else '否' end as 'allUse' from uf_OrgProject t1,uf_projcost t2 where t1.orgCode = t2.projNo and t2.allUse = '0'
) T


------已申请关闭的项目
create view WV_V_CLOSE as
select * from formtable_main_33 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where  t2.currentnodetype in ('0','1','2','3')









































