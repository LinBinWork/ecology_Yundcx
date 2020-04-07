----根据父项ID带出分摊明细视图
drop view WV_V_Apportion;
create view WV_V_Apportion as
select * from (
select t1.id,t3.ftbm,t4.Ltext,t3.bclxje,t3.ftbl,t3.ftbmfzr,t5.lastname from uf_proj t1
left join formtable_main_134 t2 on t1.reqID = t2.requestId
left join formtable_main_134_dt2 t3 on t2.id = t3.mainid
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
left join formtable_main_135 t2 on t1.reqID = t2.requestId
left join formtable_main_135_dt1 t3 on t2.id = t3.mainid
left join hrmresource t4 on t4.id = t3.zxxmjl
)T

----孙子项视图
drop view WV_V_Grandson;
create view WV_V_Grandson as
select * from (
select t1.id,t2.zxxmjl,t2.fxxmjl,t4.lastname as 'fxxmName',t5.lastname as 'zxxmName'
from uf_proj t1
left join formtable_main_136 t2 on t1.reqID = t2.requestId
left join formtable_main_136_dt1  t3 on t2.id = t3.mainid
left join hrmresource t4 on t4.id = t2.zxxmjl
left join hrmresource t5 on t5.id = t2.fxxmjl
)T

----子项项目经理视图
drop view WV_V_SubitemManager;
create view WV_V_SubitemManager as
select * from (
select t1.id,t2.zxxmjl as 'xmjl',t2.zxxmfzr as 'zxxmfzr',t4.lastname as 'jlname',t5.lastname as 'zxxmfzrname' from uf_proj t1
left join formtable_main_135_dt1 t2 on t1.detailID = t2.id
left join hrmresource t4 on t2.zxxmjl = t4.id
left join hrmresource t5 on t2.zxxmfzr = t5.id
)T


----子项项目经理,上级项目经理
drop view WV_V_SubitemProject;
create view WV_V_SubitemProject as
select * from (
select t1.id,t2.zxxmjl as 'zxxmjl',t3.fxxmjl as 'fxxmjl',t4.lastname as 'zxxmName',t7.lastname as 'fxxmName' from uf_proj t1
left join formtable_main_135_dt1 t2 on t1.detailID = t2.id
left join formtable_main_135 t3 on t2.mainid = t3.id
left join hrmresource t4 on t2.zxxmjl = t4.id
left join uf_proj t5 on t3.glfx = t5.id
left join formtable_main_134 t6 on t6.requestId = t5.reqID
left join hrmresource t7 on t6.xmjl = t7.id
)T


----子项项目经理,上级项目经理
drop view WV_V_SubitemProject;
create view WV_V_SubitemProject as
select * from (
select t1.id,t2.zxxmjl as 'zxxmjl',t3.fxxmjl as 'fxxmjl',t4.lastname as 'zxxmName',t7.lastname as 'fxxmName' from uf_proj t1
left join formtable_main_135_dt1 t2 on t1.detailID = t2.id
left join formtable_main_135 t3 on t2.mainid = t3.id
left join hrmresource t4 on t2.zxxmjl = t4.id
left join uf_proj t5 on t3.glfx = t5.id
left join formtable_main_134 t6 on t6.requestId = t5.reqID
left join hrmresource t7 on t6.xmjl = t7.id
)T

----孙子项项目经理,上级项目经理
drop view WV_V_GrandsonProject;
create view WV_V_GrandsonProject as
select * from (
select t1.id,t2.szxxmjl as 'zxxmjl',t3.zxxmjl as 'fxxmjl',t4.lastname as 'zxxmName',t5.lastname as 'fxxmName' from uf_proj t1
left join formtable_main_136_dt1 t2 on t1.detailID = t2.id
left join formtable_main_136 t3 on t2.mainid = t3.id
left join hrmresource t4 on t2.szxxmjl = t4.id
left join hrmresource t5 on t3.zxxmjl = t5.id
)T

------查出项目是否需要执行方案
drop view WV_V_Execute;
create view WV_V_Execute as
select * from (
select t1.id as 'id',t1.projName as 'projName' from uf_proj t1
left join formtable_main_135_dt1 t2 on t1.projNo = t2.zxxmbh
where t2.sffazx = '0'
)T

------存在需要执行方案申请的项目
drop view WV_V_ExistExecute;
create view WV_V_ExistExecute as
select * from (
select t1.glxm from formtable_main_137 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype = '3'
)T

----项目分摊明细视图
drop view WV_V_XMFTMX
create view WV_V_XMFTMX as
select * from (
select t1.requestId,xmbh,projNo,ftje,ftbl from formtable_main_138 t1
left join formtable_main_138_dt2 t2 on t1.id = t2.mainid
left join uf_proj t3 on t2.xmbh = t3.id
)T


-----祖项浏览按钮
create view WV_V_zx as
select * from (
select distinct projNo,orgCode,orgName,businessType as 'business',case businessType when '0' then '新零售' when '1' then '海外'when '2' then '品牌运营中心' else '其他' end as 'businessType',case allUse when '0' then '是' else '否' end as 'allUse' from uf_OrgProject t1,uf_projcost t2 where t1.orgCode = t2.projNo
 union all
 select distinct projNo,orgCode,orgName,businessType as 'business',case businessType when '0' then '新零售' when '1' then '海外'when '2' then '品牌运营中心' else '其他' end as 'businessType',case allUse when '0' then '是' else '否' end as 'allUse' from uf_OrgProject t1,uf_projcost t2 where t1.orgCode = t2.projNo and t2.allUse = '0'
) T


-----在途流程视图
drop view WV_V_Flow;
create view WV_V_Flow as
select id,requestId from (
--项目立项父项分解申请单【子项】
select t1.glfx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_135 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目分解审批申请单【孙子项】
select t1.zxxmmc as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_136 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目合同审批申请单
select t3.xmbh as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_138 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_138_dt2 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目执行方案审批单
select t1.glxm as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_137 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目子项/孙子项预算调拨单
select t3.zxszx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_140 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_140_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目子项/孙子项预算调拨单
select t3.brzxszx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_140 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_140_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目祖项/父项预算调整单
select t3.zxfx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_149 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_149_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目祖项/父项预算调整单
select t3.brzxfx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_149 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_149_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--个人借款申请单
select t1.xmmc as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_142 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--个人还款申请单
select t1.xmmc as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_143 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--营销项目差旅费用报销单
select t1.xmbh as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_145 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目其他费用报销单
select t3.zxszxbh as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_146 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_146_dt6 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--营销项目预付款单
select t3.zxszx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_147 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_147_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--营销项目对外付款申请流程
select t3.zxszxbh as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_148 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_148_dt4 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
)T

------已申请关闭的项目
create view WV_V_CLOSE as
select t1.* from formtable_main_139 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where  t2.currentnodetype in ('0','1','2','3')

------未核销完的项目
create view WV_V_NoRepayment as
select * from (
select id,userid,projid,w1.requestid,w1.requestcode,w1.requestname,brodate,repaydate,remark,isnull(money,0) as money,currentNodeType,payment,
isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow') and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('3')),0) as moneyed,
isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow') and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2') ),0) as moneying,
isnull(w1.money - isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')
and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2','3')),0),0) as moneylast
from WV_T_BorrowData w1
left join workflow_RequestBase w2 on w1.requestid = w2.requestid
where w1.rectype = 'borrow'
 and w2.currentNodeType = '3'
 )T where moneylast > 0


































































