--------借款数据----
drop table WV_T_BorrowData
create table WV_T_BorrowData(
      Id int primary key not null identity(1,1)
      ,requestid varchar(255)  ----借款流程
      ,requestcode varchar(255)  ----借款流程编号
      ,requestname varchar(255)  ----借款流程名称
      ,detailid varchar(50)  ----借款明细Id(单内序号)
      ,projid int  ----项目id
      ,userid varchar(255)  ----借款人
      ,payee varchar(255) ---收款人
      ,payBank varchar(255) ---收款人开户银行
      ,payAccount varchar(255) ---收款人银行账号
      ,payment varchar(10)  ----方式
      ,brovou varchar(255)  ----借款凭证号
      ,brodate varchar(255)  ----借款日期
      ,repaydate varchar(255)  ----预计还款日期
      ,money decimal(15,2)  ------金额
      ,rectype varchar(255)  ------记录类型/借款(borrow)、还款(repay)、冲销(expense)
      ,payid varchar(255)  ------还款/冲销流程id
      ,paydetail varchar(255)  ------还款/冲销流程明细id
      ,payvou varchar(255)  ------借款/还款/冲销凭证号
      ,remark varchar(4000) ------描述
	  ,isHistory varchar(1) ----是否历史数据 Y 是  N 否
);



select
w1.requestid,
isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')
and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('3') ),0) as moneyed,  --已核销借款
isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')
and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2') ),0) as moneying,  --在途核销借款
isnull(w1.money - isnull((select sum(money) from WV_T_BorrowData t1 where t1.requestid = w1.requestid and t1.rectype not in ('borrow')
and (select currentNodeType from workflow_RequestBase t2 where t1.payid = t2.requestid) in ('1','2','3') ),0),0) as moneylast --借款余额
from WV_T_BorrowData w1
left join workflow_RequestBase w2 on w1.requestid = w2.requestid
where w1.rectype = 'borrow' ;




































































































