/*
--更新业务类别
  update a  set a.BusinessType=b.ywlb
from uf_proj a ,formtable_main_132 b  where a.reqid = b.requestid
*/

--祖项项目期初视图
drop view wv_report_proj0;
create view wv_report_proj0 as (
   select
   case a.projYear when 9999 then null ELSE a.projYear end as projYear,
   a.companyNo,
   a.businessType,
   a.projNo as proj0No,a.projName as proj0Name,
   d.KOSTL AS costNo,d.LTEXT as costName,
   a.projCreAmt as proj0Amt,a.id as proj0id,a.creDate as proj0CreDate,a.creUser as proj0CreUser,a.projPerson as proj0Person
   from uf_proj a
   join OA_SAP_Cbzx d on d.KOSTL = a.projDeptNo
   where a.projtype = 0
);
--父项项目期初视图
drop view wv_report_proj1;
create view wv_report_proj1 as (
  select a.*,
  null as proj1No,null as proj1Name,
  null as proj1Amt,null as proj1id,null as proj1CreDate,null as proj1CreUser,null as proj1Person,null as projCategory
  from wv_report_proj0 a
  UNION all
  select a.*,
  b.projNo as proj1No,b.projName as proj1Name,
  b.projCreAmt as proj1Amt,b.id as proj1id,b.projDate as proj1CreDate,b.projManager as proj1CreUser,b.projPerson as proj1Person,b.projCategory
  from wv_report_proj0 a
  join (select projid as proj1ID,ftbm as proj0ID from wv_v_proj1more ) k on k.proj0ID = a.proj0id
  join uf_proj b on k.proj1ID = b.id


);
--子项项目期初视图
drop view wv_report_proj2;
create view wv_report_proj2 as (
  select a.*,
  null as proj2No,null as proj2Name,
  null as proj2Amt,null as proj2id,null as proj2CreDate,null as proj2CreUser,null as proj2Person
  from wv_report_proj1 a
  UNION  all
  select a.*,
  b.projNo as proj2No,b.projName as proj2Name,
  b.projCreAmt as proj2Amt,b.id as proj2id,b.projDate as proj2CreDate,b.projManager as proj2CreUser,b.projPerson as proj2Person
  from wv_report_proj1 a
  join uf_proj b on a.proj1id = b.pid

);
--孙子项项目期初视图
drop view wv_report_proj3;
create view wv_report_proj3 as (
  select a.*,
  null as proj3No,null as proj3Name,
  null as proj3Amt,null as proj3id,null as proj3CreDate,null as proj3CreUser,null as proj3Person
  from wv_report_proj2 a
  UNION ALL
  select a.*,
  b.projNo as proj3No,b.projName as proj3Name,
  b.projCreAmt as proj3Amt,b.id as proj3id,b.projDate as proj3CreDate,b.projManager as proj3CreUser,b.projPerson as proj3Person
  from wv_report_proj2 a
  join uf_proj b on a.proj2id = b.pid
);
--项项目期初视图
drop view wv_report_proj;
create view wv_report_proj as (
  select projYear,companyNo,businessType,costNo,costName,
  proj0No,proj0Name,proj0Person,proj0Amt,proj0CreUser,
  proj1No,proj1Name,projCategory,
  proj2No,proj2Name,
  proj3No,proj3Name,

  case when proj3id is not null then proj3id
  when proj2id is not null then proj2id
  when proj1id is not null then proj1id
  else proj0id end as projID,

  proj0id,

  case when proj3id is not null then proj3CreDate
  when proj2id is not null then proj2CreDate
  when proj1id is not null then proj1CreDate
  else proj0CreDate end as projCreDate,

 case when proj3id is not null then cast(cast(proj3CreUser as varchar) as int)
  when proj2id is not null then cast(cast(proj2CreUser as varchar) as int)
  when proj1id is not null then cast(cast(proj1CreUser as varchar) as int)
  else proj0CreUser end as projCreUser,

  case when proj3id is not null then proj3Person
  when proj2id is not null then proj2Person
  when proj1id is not null then proj1Person
  else proj0Person end as projPerson,

  case when proj3id is not null then proj3Amt
  when proj2id is not null then proj2Amt
  when proj1id is not null then proj1Amt
  else proj0Amt end as projAmt

  from wv_report_proj3
)
--项目执行明细
drop view wv_report_projDetail;
create view wv_report_projDetail as (
    select sum(useAmt) as useAmt,a.projID,a.useType
    from wv_proj_excuDetail a
    join workflow_requestbase b on a.requestID = b.requestID
    where b.currentnodetype !=0 and a.expType != 2
    group by a.projID,a.useType
)
--项目调整 2,"释放" 3,"调拨 6,"追加" 9,"冲销"
drop view wv_report_adjust;
create view wv_report_adjust as (
    select -sum(useAmt) as useAmt,a.projID
    from wv_report_projDetail a
    where a.useType in (2,3,6,9)
    group by a.projID
)
--项目调整 2,"释放" 3,"调拨 6,"追加" 9,"冲销"
drop view wv_report_excu;
create view wv_report_excu as (
    select -sum(useAmt) as useAmt,a.projID
    from wv_report_projDetail a
    where a.useType not in (2,3,6,9)
    group by a.projID
)

------最终 	营销项目预算明细发生记录表
drop view wv_v_projExcuDetail;
create view wv_v_projExcuDetail as (
    select a.*,d.departmentid,d.subcompanyid1 as companyID,
    Isnull(b.useAmt,0) as adjustAmt,Isnull(c.useAmt,0) as excuAmt,a.projAmt+Isnull(b.useAmt,0)+Isnull(c.useAmt,0) as balance
    from wv_report_proj a
    left join wv_report_adjust b on a.projID = b.projID
    left join wv_report_excu c on a.projID = c.projID
    left join hrmresource d on d.id = a.projCreUser
)


----营销项目预算额度汇总表
drop view wv_v_projExcuCollect_1;
create view wv_v_projExcuCollect_1 as (
   select a.projid,a.projtype,a.useType,a.expType,a.useAmt,
   CAST(left(creDate,4) as int ) as useYear,substring(creDate,6,2) as useMonth
   from wv_proj_excuDetail a
   join workflow_requestbase b on a.requestID = b.requestID
   where b.currentnodetype !=0 and a.expType != 2
)
drop view wv_v_projExcuCollect_2;
create view wv_v_projExcuCollect_2 as (
   select a.projid,a.projtype,a.useYear,a.useMonth,
   case useMonth WHEN '01' then a.useAmt else 0 end as monAmt1,
   case useMonth WHEN '02' then a.useAmt else 0 end as monAmt2,
   case useMonth WHEN '03' then a.useAmt else 0 end as monAmt3,
   case useMonth WHEN '04' then a.useAmt else 0 end as monAmt4,
   case useMonth WHEN '05' then a.useAmt else 0 end as monAmt5,
   case useMonth WHEN '06' then a.useAmt else 0 end as monAmt6,
   case useMonth WHEN '07' then a.useAmt else 0 end as monAmt7,
   case useMonth WHEN '08' then a.useAmt else 0 end as monAmt8,
   case useMonth WHEN '09' then a.useAmt else 0 end as monAmt9,
   case useMonth WHEN '10' then a.useAmt else 0 end as monAmt10,
   case useMonth WHEN '11' then a.useAmt else 0 end as monAmt11,
   case useMonth WHEN '12' then a.useAmt else 0 end as monAmt12
   from wv_v_projExcuCollect_1 a
)
--所有项目的月份使用金额
drop view wv_v_projExcuCollect_3;
create view wv_v_projExcuCollect_3 as (
   select a.projid,max(a.projtype) as projtype,a.useYear,
   sum(monAmt1) as monAmt1,sum(monAmt2) as monAmt2,sum(monAmt3) as monAmt3,sum(monAmt4) as monAmt4,
   sum(monAmt5) as monAmt5,sum(monAmt6) as monAmt6,sum(monAmt7) as monAmt7,sum(monAmt8) as monAmt8,
   sum(monAmt9) as monAmt9,sum(monAmt10) as monAmt10,sum(monAmt11) as monAmt11,sum(monAmt12) as monAmt12
   from wv_v_projExcuCollect_2 a
   group by a.projid,a.useYear
)

drop view wv_v_projExcuCollect_3_1;
create view wv_v_projExcuCollect_3_1 as (
   select b.projID ,a.projYear ,
   monAmt1,monAmt2,monAmt3,monAmt4,
   monAmt5,monAmt6,monAmt7,monAmt8,
   monAmt9,monAmt10,monAmt11,monAmt12
   from wv_v_projExcuDetail a
   join wv_v_projExcuCollect_3 b on a.projID = b.projid and a.projYear = b.useYear and b.projtype in (2,3)--2019-11-28增加限制，费用使用只展示子项孙子项合计
   union all
   select b.projID,b.useYear ,
   monAmt1,monAmt2,monAmt3,monAmt4,
   monAmt5,monAmt6,monAmt7,monAmt8,
   monAmt9,monAmt10,monAmt11,monAmt12
   from wv_v_projExcuDetail a
   join wv_v_projExcuCollect_3 b on a.projID = b.projid  and b.projtype in (2,3)--2019-11-28增加限制，费用使用只展示子项孙子项合计
   where a.projYear is null
)

--包含项目拆分的数据
drop view wv_v_projExcuCollect_4;
create view wv_v_projExcuCollect_4 as (
   select a.proj0id,a.projYear ,
   sum(monAmt1) as monAmt1,sum(monAmt2) as monAmt2,sum(monAmt3) as monAmt3,sum(monAmt4) as monAmt4,
   sum(monAmt5) as monAmt5,sum(monAmt6) as monAmt6,sum(monAmt7) as monAmt7,sum(monAmt8) as monAmt8,
   sum(monAmt9) as monAmt9,sum(monAmt10) as monAmt10,sum(monAmt11) as monAmt11,sum(monAmt12) as monAmt12
   from wv_v_projExcuDetail a
   join wv_v_projExcuCollect_3_1 b on a.projID = b.projID and a.projYear = b.projYear
   group by a.proj0id,a.projYear
   UNION ALL
   select a.proj0id,b.projYear ,
   sum(monAmt1) as monAmt1,sum(monAmt2) as monAmt2,sum(monAmt3) as monAmt3,sum(monAmt4) as monAmt4,
   sum(monAmt5) as monAmt5,sum(monAmt6) as monAmt6,sum(monAmt7) as monAmt7,sum(monAmt8) as monAmt8,
   sum(monAmt9) as monAmt9,sum(monAmt10) as monAmt10,sum(monAmt11) as monAmt11,sum(monAmt12) as monAmt12
   from wv_v_projExcuDetail a
   join wv_v_projExcuCollect_3_1 b on a.projID = b.projID
   where a.projYear is null
   group by a.proj0id,b.projYear

)
drop view wv_v_projExcuCollect_5;
create view wv_v_projExcuCollect_5 as (
    select a.proj0id,a.companyNo,a.businessType,a.proj0No,a.proj0Name,a.costNo,a.costName,a.proj0Person,
    case WHEN a.projYear is not null then a.projYear else c.projYear end projYear,
    --待考究是否用实际的预算
    Isnull(a.proj0Amt,0) as yearAmt,
    Isnull(b.one ,0)+ Isnull(b.two ,0)+ Isnull(b.three ,0)+ Isnull(b.four ,0)+ Isnull(b.five ,0)+ Isnull(b.six ,0)+
    Isnull(b.seven ,0)+ Isnull(b.eight ,0)+ Isnull(b.nine ,0)+ Isnull(b.ten ,0)+ Isnull(b.eleven ,0)+ Isnull(b.twelve,0) as totalMonAmt,
    --b.one + b.two + b.three + b.four + b.five + b.six + b.seven + b.eight + b.nine + b.ten + b.eleven + b.twelve as totalMonAmt,
    --c.monAmt1 +c.monAmt2 +c.monAmt3 +c.monAmt4 +c.monAmt5 +c.monAmt6 +c.monAmt7 +c.monAmt8 +c.monAmt9 +c.monAmt10 +c.monAmt11 +c.monAmt12   as totalMonExcuAmt,
    Isnull(c.monAmt1 ,0)+Isnull(c.monAmt2 ,0)+Isnull(c.monAmt3 ,0)+Isnull(c.monAmt4 ,0)+Isnull(c.monAmt5 ,0)+
    Isnull(c.monAmt6 ,0)+Isnull(c.monAmt7 ,0)+Isnull(c.monAmt8 ,0)+Isnull(c.monAmt9 ,0)+Isnull(c.monAmt10 ,0)+Isnull(c.monAmt11 ,0)+Isnull(c.monAmt12,0)   as totalMonExcuAmt,
    Isnull(b.one ,0) as  monAmt1, Isnull(c.monAmt1 ,0) as  monExcuAmt1, Isnull(b.one ,0) -  Isnull(c.monAmt1 ,0) as  monBalance1,
    Isnull(b.two ,0) as  monAmt2, Isnull(c.monAmt2 ,0) as  monExcuAmt2, Isnull(b.two ,0) -  Isnull(c.monAmt2 ,0) as  monBalance2,
    Isnull(b.three ,0) as  monAmt3, Isnull(c.monAmt3 ,0) as  monExcuAmt3, Isnull(b.three ,0) -  Isnull(c.monAmt3 ,0) as  monBalance3,
    Isnull(b.four ,0) as  monAmt4, Isnull(c.monAmt4 ,0) as  monExcuAmt4, Isnull(b.four ,0) -  Isnull(c.monAmt4 ,0) as  monBalance4,
    Isnull(b.five ,0) as  monAmt5, Isnull(c.monAmt5 ,0) as  monExcuAmt5, Isnull(b.five ,0) -  Isnull(c.monAmt5 ,0) as  monBalance5,
    Isnull(b.six ,0) as  monAmt6, Isnull(c.monAmt6 ,0) as  monExcuAmt6, Isnull(b.six ,0) -  Isnull(c.monAmt6 ,0) as  monBalance6,
    Isnull(b.seven ,0) as  monAmt7, Isnull(c.monAmt7 ,0) as  monExcuAmt7, Isnull(b.seven ,0) -  Isnull(c.monAmt7 ,0) as  monBalance7,
    Isnull(b.eight ,0) as  monAmt8, Isnull(c.monAmt8 ,0) as  monExcuAmt8, Isnull(b.eight ,0) -  Isnull(c.monAmt8 ,0) as  monBalance8,
    Isnull(b.nine ,0) as  monAmt9, Isnull(c.monAmt9 ,0) as  monExcuAmt9, Isnull(b.nine ,0) -  Isnull(c.monAmt9 ,0) as  monBalance9,
    Isnull(b.ten ,0) as  monAmt10, Isnull(c.monAmt10 ,0) as  monExcuAmt10, Isnull(b.ten ,0) -  Isnull(c.monAmt10 ,0) as  monBalance10,
    Isnull(b.eleven ,0) as  monAmt11, Isnull(c.monAmt11 ,0) as  monExcuAmt11, Isnull(b.eleven ,0) -  Isnull(c.monAmt11 ,0) as  monBalance11,
    Isnull(b.twelve ,0) as  monAmt12, Isnull(c.monAmt12 ,0) as  monExcuAmt12, Isnull(b.twelve ,0) -  Isnull(c.monAmt12 ,0) as  monBalance12

    from wv_report_proj0 a
    left join wv_v_projExcuCollect_4 c on a.proj0id = c.proj0id
    left join wv_month_proj_p0 b on a.proj0No = b.projNo and a.costNo = b.projDeptNo
)
--最终 营销项目预算额度汇总表
drop view wv_v_projExcuCollect;
create view wv_v_projExcuCollect as (
  select a.* ,a.totalMonAmt - a.totalMonExcuAmt as totalBalance, case when a.yearAmt > 0 then a.totalMonExcuAmt * 100 / a.yearAmt else 0 end as excuRate
  from wv_v_projExcuCollect_5 a
)



--营销项目祖项执行汇总记录表

--祖项项目汇总项目汇总  -祖项
drop view wv_v_excuRecord1;
create view wv_v_excuRecord1 as (
    select sum(useAmt) as useAmt,a.projID,a.useType,a.expType
    from wv_proj_excuDetail a
    join workflow_requestbase b on a.requestID = b.requestID
    where b.currentnodetype !=0 and a.expType != 2 and a.projType = 0
    group by a.projID,a.useType,a.expType
)
--预算调入金额  3,"调拨 6,"追加"
drop view wv_v_excuRecordIn;
create view wv_v_excuRecordIn as (
    select -sum(useAmt) as useAmt,a.projID
    from wv_v_excuRecord1 a
    where a.useType in (3,6) and a.expType=0
    group by a.projID
)
--预算调出金额  3,"调拨 6,"追加"
drop view wv_v_excuRecordOut;
create view wv_v_excuRecordOut as (
    select -sum(useAmt) as useAmt,a.projID
    from wv_v_excuRecord1 a
    where a.useType in (3,6) and a.expType=1
    group by a.projID
)
--调入\调出合计
drop view wv_v_projExcuRecordCollect_1;
create view wv_v_projExcuRecordCollect_1 as (
    select a.proj0id,a.companyNo,a.businessType,a.proj0No,a.proj0Name,a.costNo,a.costName,a.proj0CreUser,
    a.projYear,
    Isnull(a.proj0Amt,0) as proj0Amt,
    Isnull(b.useAmt,0) as transInAmt,
    Isnull(c.useAmt,0) as transOutAmt
    from wv_report_proj0 a
    left join wv_v_excuRecordIn b on a.proj0id = b.projID
    left join wv_v_excuRecordOut c on a.proj0id = c.projID
)

--项目汇总  -子项-孙子项  只包含使用和冻结的
drop view wv_v_excuRecord2;
create view wv_v_excuRecord2 as (
    select sum(useAmt) as useAmt,a.projID,a.useType
    from wv_proj_excuDetail a
    join workflow_requestbase b on a.requestID = b.requestID
    where b.currentnodetype !=0 and a.useType in(0,1,8,9) and a.projType in(2,3)
    group by a.projID,a.useType
)

-- 已执行金额 汇总子项和孙子项使用金额
drop view wv_v_excuRecordUse;
create view wv_v_excuRecordUse as (
    select -sum(useAmt) as useAmt,a.projID
    from wv_v_excuRecord2 a
    where a.useType in (0,8,9)
    group by a.projID
)

--冻结金额 汇总子项和孙子项冻结金额
drop view wv_v_excuRecordFreeze;
create view wv_v_excuRecordFreeze as (
    select -sum(useAmt) as useAmt,a.projID
    from wv_v_excuRecord1 a
    where a.useType in (1)
    group by a.projID
)
--已使用合计
drop view wv_v_projExcuRecordCollect_3;
create view wv_v_projExcuRecordCollect_3 as (
    select a.proj0id,
    sum(b.useAmt) as usedAmt
    from wv_report_proj a
    left join wv_v_excuRecordUse b on a.projid = b.projID
    GROUP by a.proj0id
)
--冻结合计
drop view wv_v_projExcuRecordCollect_4;
create view wv_v_projExcuRecordCollect_4 as (
   select a.proj0id,
    sum(b.useAmt) as freezeAmt
    from wv_report_proj a
    left join wv_v_excuRecordFreeze b on a.projid = b.projID
    GROUP by a.proj0id
)

-- 最终  营销项目祖项执行汇总记录表
drop view wv_v_projExcuRecordCollect;
create view wv_v_projExcuRecordCollect as (
    select a.proj0id,a.companyNo,a.businessType,a.proj0No,a.proj0Name,a.costNo,a.costName,a.proj0CreUser,
    a.projYear,a.transInAmt,a.transOutAmt,
    b.usedAmt,c.freezeAmt
    from wv_v_projExcuRecordCollect_1 a
    left join wv_v_projExcuRecordCollect_3 b on a.proj0id = b.proj0ID
    left join wv_v_projExcuRecordCollect_4 c on a.proj0id = c.proj0id
)

