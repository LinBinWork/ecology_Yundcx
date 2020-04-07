drop view WV_V_Flow;
create view WV_V_Flow as
select id,requestId from (
--项目立项父项分解申请单【子项】
select t1.glfx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_21 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目分解审批申请单【孙子项】
select t1.zxxmmc as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_25 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目合同审批申请单
select t3.xmbh as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_26 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_26_dt2 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目执行方案审批单
select t1.glxm as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_35 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目子项/孙子项预算调拨单
select t3.zxszx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_36 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_36_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目子项/孙子项预算调拨单
select t3.brzxszx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_36 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_36_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目祖项/父项预算调整单
select t3.zxfx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_37 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_37_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目祖项/父项预算调整单
select t3.brzxfx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_37 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_37_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--个人借款申请单
select t1.xmmc as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_38 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--个人还款申请单
select t1.xmmc as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_39 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--营销项目差旅费用报销单
select t1.xmbh as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_45 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--项目其他费用报销单
select t3.zxszxbh as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_43 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_43_dt6 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--营销项目预付款单
select t3.zxszx as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_46 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_46_dt1 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
union all
--营销项目对外付款申请流程
select t3.zxszxbh as 'id',t1.requestId as 'requestId',t2.creater from formtable_main_48 t1
left join workflow_requestbase t2 on t1.requestId = t2.requestId
left join formtable_main_48_dt4 t3 on t3.mainid = t1.id
where t2.currentnodetype != 0 and t2.currentnodetype != 3
)T



























