---公司代码
create table OA_SAP_Gsdm(
  Id int primary key not null identity(1,1),
  BUKRS  varchar(10),---公司代码
  BUTXT  varchar(50)---公司名称
);

---会计科目
create table OA_SAP_Kjkm(
  Id int primary key not null identity(1,1),
  BUKRS  varchar(100),---公司代码
  HKONT  varchar(200),---科目
  TXT50  varchar(200),---总帐科目文本
  SPRAS  varchar(200),---语言
  XSPEB  varchar(500) ---为记账冻结,X为冻结
);

---成本中心
create table OA_SAP_Cbzx(
  Id int primary key not null identity(1,1),
  BUKRS  varchar(100),---公司代码
  KOSTL  varchar(200),---成本中心
  LTEXT  varchar(500),---成本中心名称
  SPRAS  varchar(200),---语言
  BKZKP  varchar(500) ---为记账冻结,X为冻结
);


---供应商编码
create table OA_SAP_Gys(
  Id int primary key not null identity(1,1),
  PARTNER  varchar(100),---供应商或债权人的帐号
  NAME1  varchar(500),---名称1
  GH varchar(100),---工号
  ZGYLX  varchar(50),---供应商类型
  AKONT  varchar(100),---统驭科目
  BUKRS  varchar(100),---公司代码
  ZBANK  varchar(255),---银行帐户号码
  ACCNAME  varchar(255),---帐户名称
  XBLCK    varchar(50), ---为记账冻结,X为冻结
);





