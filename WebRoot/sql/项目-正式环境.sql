--祖项建模表 ： uf_OrgProject
--祖项成本中心建模表 ： uf_Projcost
--项目预算建模表 ： uf_proj
--项目权限建模表 ： uf_projshare

--项目预算执行明细表
drop table wv_proj_excuDetail;
create table wv_proj_excuDetail(
    id int PRIMARY KEY identity(1,1),
    requestID int NOT NULL , -- 执行流程
    detailID int , -- 执行流程明细ID
    projID int NOT NULL , -- 项目ID
    projNo VARCHAR(50) , -- 项目编码
    projDeptNo VARCHAR(999)  , --成本中心
    projType int NOT NULL , -- 0祖项 1父项 2子项 3孙子项
    useType int NOT NULL ,--使用类型
    expType int NOT NULL ,--费用类型 0增加 1减少 2不计算
    splitStatu VARCHAR(5),  -- 0失效 只针对拆解有效
    useAmt DECIMAL(38,2)  NOT NULL ,--使用金额
    creDate VARCHAR(10) , -- 创建日期
    creTime VARCHAR(8) , -- 创建时间
    creUser int, -- 创建人
    remark text --备注
);

--父项分摊视图更新表
drop table wv_proj1_precent;
create table wv_proj1_precent(
    id int PRIMARY KEY identity(1,1),
    proj1RequestID int NOT NULL , -- 父项立项流程
    creRequestID int NOT NULL , -- 更新的流程
    proj1ID int NOT NULL , -- 父项ID
    proj0ID int NOT NULL , -- 追加的祖项ID
    balance DECIMAL(38,2),--追加时可用金额
    precent DECIMAL(38,2),--分摊比率  无效  实际计算
    person int,--分摊部门负责人
    ccy int ,--币种
    rate decimal(38,4),--	汇率
    useAmt DECIMAL(38,2)  NOT NULL ,--追加金额
    creDate VARCHAR(10) , -- 创建日期
    creTime VARCHAR(8) , -- 创建时间
    creUser int, -- 创建人
    remark text --备注
);

--分摊合并视图
drop view wv_v_proj1moreTemp;
create view wv_v_proj1moreTemp as (
    select a.requestid,a.xmbh,a.xmmc,
    b.id,b.ftbm,b.lxkyje,b.bclxje,b.ftbl as precent,b.ftbmfzr,
    c.id as projid,'初始立项' as remark
    from formtable_main_134 a
    join formtable_main_134_dt2 b on a.id = b.mainid
    join uf_proj c on c.reqid = a.requestid and c.projtype = 1
    union ALL
    select h.proj1RequestID,j.projno,j.projName,
    h.id,h.proj0ID,h.balance,h.useAmt,h.precent,h.person,
    h.proj1ID,h.remark
    from wv_proj1_precent h
    join uf_proj j on j.id = h.proj1ID
    join workflow_requestbase wr on wr.requestid = h.creRequestID and wr.currentnodetype = 3
);
--父项分摊视图 中间1
drop view wv_v_proj1moreTemp2;
create view wv_v_proj1moreTemp2 as (
  select a.*,k.total ,round(a.bclxje * 100 / k.total,2)  as ftbl from wv_v_proj1moreTemp  a
  join (select projid,sum(bclxje) as total from wv_v_proj1moreTemp  group by projid) k on k.projid = a.projid
)
--父项分摊视图 合并相同的祖项
drop view wv_v_proj1more;
create view wv_v_proj1more as (
  select
  max(requestid) as requestid , --父项立项流程
  max(xmbh) as xmbh, -- 父项编号
  max(xmmc) as xmmc, --父项名称
  max(id) as id, --没啥用
  max(ftbm) as ftbm, --祖项
  max(lxkyje) as lxkyje, --立项/追加时的余额
  sum(bclxje) as bclxje, --立项/追加时的金额
  max(ftbmfzr) as ftbmfzr, --负责人
  max(projid) as projid, --父项ID
  max(total) as total, --父项总金额
  sum(ftbl) as ftbl --祖项分摊比例
  from wv_v_proj1moreTemp2
  group by projid ,ftbm
)

--父项关键事项视图
drop view wv_v_proj1thing;
create view wv_v_proj1thing as (
    select a.requestid,a.xmbh,a.xmmc,
    b.id,b.gjsx as name ,REPLACE(b.gjsxje,',','') as amt ,b.bz as ccy ,b.hshl as rate,b.hsrmbje as rmbAmt ,b.gjsxsm as reason,
    c.id as projid
    from formtable_main_134 a
    join formtable_main_134_dt1 b on a.id = b.mainid
    join uf_proj c on c.reqid = a.requestid
);

--项目权限视图
drop view wv_v_projpermission;
create view wv_v_projpermission as (
    select a.*,
    CAST(splitPermission as varchar ) as splitUsers,
    CAST(persion as varchar ) as persons,
    CAST(dept as varchar ) as depts
    from uf_proj a
    join uf_projshare b on a.id = b.projname
    where a.projClose is null Or a.projClose != 0
);

--只展示末端的子项孙子项
drop view wv_v_projpermission23;
create view wv_v_projpermission23 as (
    SELECT * FROM wv_v_projpermission A WHERE NOT EXISTS(
      SELECT * FROM wv_v_projpermission B WHERE a.id=b.pid
    )
);
-- 非交付中心 deliveryCenter != 0


--合同使用明细视图
drop table wv_contract_excuDetail;
create table wv_contract_excuDetail(
    id int PRIMARY KEY identity(1,1),
    contractRequestID int NOT NULL , -- 合同相关流程
    requestID int NOT NULL , -- 对应流程
    detailID int , -- 对应流程明细
    projID INT NOT NULL ,--项目ID
    useType int NOT NULL ,--使用类型
    useAmt DECIMAL(38,2)  NOT NULL ,--使用金额
    creDate VARCHAR(10) , -- 创建日期
    creTime VARCHAR(8) , -- 创建时间
    creUser int, -- 创建人
    remark text --备注
);

--合同流程
drop view wv_v_contractFlow;
create view wv_v_contractFlow as (
    select k.requestid,k.type,k.vendorName,k.vendorNo,
    d.requestmark,d.requestname,d.creater,d.createDate, h.lastname,
    e.id as workflowid,e.workflowname
    from (
      select a.requestid,c.NAME1 as vendorName,a.gysmc as vendorNo, 'Y' as type  from formtable_main_138 a--营销合同
      join OA_SAP_GYS c on a.gysmc = c.PARTNER
      UNION ALL
      select a.requestid,a.dfgsmc,'' as vendorNo, 'N' as type  from formtable_main_20 a--收款合同  TODO 正式环境叫0301 经销合同审批流程
      UNION ALL
      select a.requestid,a.dfgsmc,'' as vendorNo, 'N' as type  from formtable_main_23 a--交付中心合同
      UNION ALL
      select a.requestid,a.dfgsmc,'' as vendorNo, 'N' as type  from formtable_main_28 a--其他合同
    ) k
    join workflow_requestbase d on d.requestid = k.requestid and d.currentnodetype = 3
    join workflow_base e on e.id = d.workflowid
    left join hrmresource h on h.id = d.creater
);

--合同信息视图
drop view wv_v_contractinfo;
create view wv_v_contractinfo as (
    select a.requestid,a.htbh,a.htmc,
    b.id as detailID,b.xmbh as projID ,b.ftbl as precent,b.bz as reason,
    c.projType,c.useAmt
    from formtable_main_138 a
    join formtable_main_138_dt2 b on a.id = b.mainid
    join wv_proj_excuDetail c  on c.requestid = a.requestid and b.xmbh = c.projID
    join workflow_requestbase d on d.requestid = a.requestid and d.currentnodetype = 3
);
--子项尚未走 执行方案流程视图
drop view wv_v_projexcustatus;
create view wv_v_projexcustatus as (
    select * from uf_proj a where a.id not in(select g.glxm
    from formtable_main_137 g
    join workflow_requestbase wr on g.requestid = wr.requestid
    where wr.currentnodetype = 3) and a.projtype = 2 and a.projExcustatus = 0
);



































