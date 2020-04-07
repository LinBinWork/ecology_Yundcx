--祖项月度项目预算初始导入
drop table wv_month_proj_p0;
create table wv_month_proj_p0(
    id int PRIMARY KEY identity(1,1),
    requestID int not NULL ,
    detailID int not NULL ,
    projNo VARCHAR(5) NOT NULL , -- 祖项编码
    projDeptNo VARCHAR(50) NOT NULL , --部门编码
    projYear int NOT NULL ,--年度
    upYear VARCHAR(50),--上一年参考
    projDef VARCHAR(100),--项目定义
    projAmt DECIMAL(38,2) NOT null, -- 年度预算
    ywlb varchar (2), --业务类别
    one DECIMAL(38,2),--一月
    two DECIMAL(38,2),--二月
    three DECIMAL(38,2),--三月
    four DECIMAL(38,2),--四月
    five DECIMAL(38,2),--五月
    six DECIMAL(38,2),--六月
    seven DECIMAL(38,2),--七月
    eight DECIMAL(38,2),--八月
    nine DECIMAL(38,2),--九月
    ten DECIMAL(38,2),--十月
    eleven DECIMAL(38,2),--十一月
    twelve DECIMAL(38,2),--十二月
    creDate VARCHAR(10) , -- 创建日期
    creTime VARCHAR(8) , -- 创建时间
    creUser int, -- 创建人
    updDate VARCHAR (10),--更新日期
    updTime VARCHAR(8) ,--更新时间
    updUser int --更新人
);

--执行方案申请关联项目浏览按钮
select
currentnodetype,
zxxmbh,
xmmc,
hsrmbje
from formtable_main_21 t1
join formtable_main_21_dt1 t2 on t1.id = t2.mainid
join workflow_requestbase t3 on t1.requestId = t3.requestId
where t3.currentnodetype = '3' and t2.sffazx = '0'