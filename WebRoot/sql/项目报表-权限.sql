drop view wv_v_reportPermission;
create view wv_v_reportPermission as (
  select businessType,costNo,
  case when proj3id is not null then proj3id
  when proj2id is not null then proj2id
  when proj1id is not null then proj1id
  else proj0id end as projID,

  case
  when proj3id is not null
	  then cast(proj3CreUser as varchar)+','+cast(proj2CreUser as varchar)+','+cast(proj1CreUser as varchar)+','+cast(proj0CreUser as varchar)
  when proj2id is not null
    then cast(proj2CreUser as varchar)+','+cast(proj1CreUser as varchar)+','+cast(proj0CreUser as varchar)
  when proj1id is not null
    then cast(proj1CreUser as varchar)+','+cast(proj0CreUser as varchar)
  else cast(proj0CreUser as varchar)
  end as projManager,

  case when proj3id is not null
    then cast(proj3Person as varchar)+','+cast(proj2Person as varchar)+','+cast(proj1Person as varchar)+','+cast(proj0Person as varchar)
  when proj2id is not null
    then cast(proj2Person as varchar)+','+cast(proj1Person as varchar)+','+cast(proj0Person as varchar)
  when proj1id is not null
    then cast(proj1Person as varchar)+','+cast(proj0Person as varchar)
  else cast(proj0Person as varchar)
  end as projPerson

  from wv_report_proj3
)
--整合配置的权限
--1、只有业务类型、部门为空
--2、既有业务类型也有部门
--3、是项目经理（下级继承上级）
--4、是项目负责人（下级继承上级）
select * from wv_v_projExcuDetail t1 where 1=1 and

t1.businessType in (select businessType from uf_reportpms where dept is null and  cast(showUser  as varchar ) like '%'+cast($UserId$ as varchar)+'%')
or
t1.costNo +'@@'+ cast(t1.businessType as varchar) in (select dept+'@@'+cast(businessType as varchar) from uf_reportpms where dept is not null and cast(showUser  as varchar ) like '%'+cast($UserId$ as varchar)+'%')
or
t1.projid in (select projid from wv_v_reportPermission where ','+projManager+',' like '%,'+cast($UserId$ as varchar)+',%')
or
t1.projid in (select projid from wv_v_reportPermission where ','+projPerson+',' like '%,'+cast($UserId$ as varchar)+',%')


