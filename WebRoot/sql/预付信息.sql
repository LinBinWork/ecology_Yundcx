--------预付款信息------
drop table WV_T_PaymentData
create table WV_T_PaymentData(
      Id int primary key not null identity(1,1)
      ,requestid varchar(255)  ----流程id
      ,requestcode varchar(255)  ----流程编号
      ,requestname varchar(255)  ----借款流程名称
      ,detailid varchar(50)  ----明细Id(单内序号)
      ,xmid varchar(50)  ----项目id
      ,gysid varchar(50)  ----供应商
      ,payDate varchar(255)  ----预付日期
      ,money decimal(15,2)  ---金额
      ,bz varchar(255)  ----币种
      ,rectype varchar(255)  ------记录类型/预付(payment)、冲销(expense)
      ,payid varchar(255)  ------冲销流程id
      ,paydetail varchar(255)  ------冲销流程明细id
      ,payvou varchar(255)  ------冲销凭证号
      ,remark varchar(255) ------描述
);
