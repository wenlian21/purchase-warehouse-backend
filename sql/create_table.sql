create database if not exists pharmacyWarehouse default character set utf8mb4 collate utf8mb4_unicode_ci;
use pharmacyWarehouse;

drop table if exists systemExceptionLog;
drop table if exists loginLog;
drop table if exists operationLog;
drop table if exists emailVerifyCode;
drop table if exists salesReturn;
drop table if exists salesOrderItem;
drop table if exists salesOrder;
drop table if exists inventoryFlow;
drop table if exists inventoryBatch;
drop table if exists medicine;
drop table if exists supplier;
drop table if exists medicineCategory;
drop table if exists user;
drop table if exists role;

create table if not exists role
(
    id                bigint auto_increment primary key comment '主键',
    roleName          varchar(64)                        not null comment '角色名称',
    roleCode          varchar(64)                        not null comment '角色编码',
    roleDesc          varchar(255)                       null comment '角色描述',
    menuPermissions   text                               null comment '菜单权限JSON',
    buttonPermissions text                               null comment '按钮权限JSON',
    isSystem          tinyint  default 0                 not null comment '是否系统内置角色',
    createTime        datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint  default 0                 not null comment '是否删除',
    unique key uk_roleCode (roleCode)
) comment '角色表';

create table if not exists user
(
    id            bigint auto_increment primary key comment '主键',
    userAccount   varchar(128)                          not null comment '用户名',
    userPassword  varchar(512)                          not null comment '密码',
    userName      varchar(64)                           not null comment '姓名',
    employeeNo    varchar(64)                           not null comment '工号',
    phone         varchar(32)                           null comment '手机号',
    email         varchar(128)                          null comment '邮箱',
    department    varchar(64)                           null comment '部门',
    roleId        bigint                                not null comment '角色id',
    userRole      varchar(64)                           not null comment '角色编码',
    userAvatar    varchar(1024)                         null comment '头像',
    userProfile   varchar(255)                          null comment '简介',
    userStatus    varchar(32) default '启用'            not null comment '状态',
    lastLoginTime datetime                              null comment '最后登录时间',
    createTime    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint     default 0                 not null comment '是否删除',
    unique key uk_userAccount (userAccount),
    unique key uk_employeeNo (employeeNo),
    unique key uk_email (email),
    key idx_roleId (roleId)
) comment '员工账号表';

create table if not exists medicineCategory
(
    id             bigint auto_increment primary key comment '主键',
    categoryName   varchar(128)                       not null comment '分类名称',
    categoryRemark varchar(512)                       null comment '分类描述',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除',
    unique key uk_categoryName (categoryName)
) comment '药品分类表';

create table if not exists supplier
(
    id                    bigint auto_increment primary key comment '主键',
    supplierCode          varchar(64)                           not null comment '供应商编号',
    supplierName          varchar(128)                          not null comment '供应商名称',
    contactPerson         varchar(64)                           null comment '联系人',
    contactPhone          varchar(32)                           null comment '联系电话',
    qualificationNo       varchar(128)                          null comment '资质编号',
    qualificationExpireAt datetime                              null comment '资质到期时间',
    ratingLevel           varchar(16) default 'A'               not null comment '评级',
    settlementCycle       varchar(64)                           null comment '结算周期',
    agreementPriceRemark  varchar(255)                          null comment '协议说明',
    isBlacklisted         tinyint     default 0                 not null comment '是否黑名单',
    createTime            datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime            datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete              tinyint     default 0                 not null comment '是否删除',
    unique key uk_supplierCode (supplierCode),
    unique key uk_supplierName (supplierName)
) comment '供应商表';

create table if not exists medicine
(
    id             bigint auto_increment primary key comment '主键',
    categoryId     bigint                                not null comment '分类id',
    medicineName   varchar(128)                          not null comment '药品名称',
    specification  varchar(128)                          not null comment '规格',
    manufacturer   varchar(255)                          not null comment '生产厂家',
    barcode        varchar(32)                           not null comment '条形码',
    approvalNumber varchar(128)                          null comment '批准文号',
    unitName       varchar(32) default '盒'              not null comment '单位',
    salePrice      decimal(10, 2)                        not null comment '销售单价',
    shelfLifeDays  int                                   not null comment '保质期天数',
    safeStockMin   int         default 0                 not null comment '安全库存下限',
    safeStockMax   int         default 0                 not null comment '安全库存上限',
    totalStock     int         default 0                 not null comment '总库存',
    isPrescription tinyint     default 0                 not null comment '是否处方药',
    status         varchar(32) default '启用'            not null comment '状态',
    createTime     datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint     default 0                 not null comment '是否删除',
    unique key uk_medicineName (medicineName),
    unique key uk_barcode (barcode),
    key idx_categoryId (categoryId)
) comment '药品档案表';

create table if not exists inventoryBatch
(
    id              bigint auto_increment primary key comment '主键',
    medicineId      bigint                                not null comment '药品id',
    supplierId      bigint                                null comment '供应商id',
    batchNo         varchar(64)                           not null comment '批次号',
    productionDate  date                                  not null comment '生产日期',
    expiryDate      date                                  not null comment '有效期',
    storageLocation varchar(128)                          null comment '存放位置',
    purchasePrice   decimal(10, 2)                        not null comment '进货价',
    availableStock  int                                   not null comment '可用库存',
    batchStatus     varchar(32) default '正常'            not null comment '批次状态',
    createTime      datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint     default 0                 not null comment '是否删除',
    unique key uk_medicineBatch (medicineId, batchNo),
    key idx_supplierId (supplierId),
    key idx_expiryDate (expiryDate)
) comment '库存批次表';

create table if not exists inventoryFlow
(
    id           bigint auto_increment primary key comment '主键',
    medicineId   bigint                             not null comment '药品id',
    batchId      bigint                             null comment '批次id',
    bizType      varchar(32)                        not null comment '业务类型',
    changeType   varchar(16)                        not null comment '变动方向',
    quantity     int                                not null comment '变动数量',
    unitPrice    decimal(10, 2)                     null comment '单价',
    documentNo   varchar(64)                        null comment '关联单号',
    operatorName varchar(64)                        null comment '操作人',
    remark       varchar(255)                       null comment '备注',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    key idx_medicineId (medicineId),
    key idx_documentNo (documentNo),
    key idx_createTime (createTime)
) comment '库存流水表';

create table if not exists salesOrder
(
    id                  bigint auto_increment primary key comment '主键',
    orderNo             varchar(64)                             not null comment '销售单号',
    memberName          varchar(64)                             null comment '会员姓名',
    memberPhone         varchar(32)                             null comment '会员手机号',
    discountRate        decimal(5, 2) default 1.00              not null comment '折扣率',
    originalAmount      decimal(10, 2)                          not null comment '原始金额',
    discountAmount      decimal(10, 2)                          not null comment '优惠金额',
    actualAmount        decimal(10, 2)                          not null comment '实收金额',
    pointsEarned        int           default 0                 not null comment '赠送积分',
    prescriptionChecked tinyint       default 0                 not null comment '是否已核验处方',
    cashierName         varchar(64)                             null comment '收银员',
    orderStatus         varchar(32)   default '已完成'          not null comment '订单状态',
    createTime          datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime          datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete            tinyint       default 0                 not null comment '是否删除',
    unique key uk_orderNo (orderNo),
    key idx_memberPhone (memberPhone),
    key idx_createTime (createTime)
) comment '销售订单表';

create table if not exists salesOrderItem
(
    id           bigint auto_increment primary key comment '主键',
    salesOrderId bigint                             not null comment '销售订单id',
    medicineId   bigint                             not null comment '药品id',
    medicineName varchar(128)                       not null comment '药品名称快照',
    batchNos     varchar(255)                       null comment '扣减批次号',
    quantity     int                                not null comment '销售数量',
    unitPrice    decimal(10, 2)                     not null comment '销售单价',
    lineAmount   decimal(10, 2)                     not null comment '行金额',
    costAmount   decimal(10, 2)                     not null comment '成本金额',
    grossProfit  decimal(10, 2)                     not null comment '毛利金额',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    key idx_salesOrderId (salesOrderId),
    key idx_medicineId (medicineId)
) comment '销售订单明细表';

create table if not exists salesReturn
(
    id           bigint auto_increment primary key comment '主键',
    salesOrderId bigint                             not null comment '销售订单id',
    returnNo     varchar(64)                        not null comment '退货单号',
    returnAmount decimal(10, 2)                     not null comment '退货金额',
    returnReason varchar(255)                       null comment '退货原因',
    operatorName varchar(64)                        null comment '操作人',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    unique key uk_returnNo (returnNo),
    key idx_salesOrderId (salesOrderId)
) comment '销售退货表';

create table if not exists emailVerifyCode
(
    id         bigint auto_increment primary key comment '主键',
    email      varchar(128)                       not null comment '邮箱',
    verifyCode varchar(8)                         not null comment '验证码',
    bizType    varchar(32)                        not null comment '业务类型',
    expireTime datetime                           not null comment '过期时间',
    usedStatus tinyint  default 0                 not null comment '是否已使用',
    sendTime   datetime default CURRENT_TIMESTAMP not null comment '发送时间',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    key idx_email_bizType (email, bizType)
) comment '邮箱验证码表';

create table if not exists operationLog
(
    id            bigint auto_increment primary key comment '主键',
    traceId       varchar(64)                        not null comment '链路id',
    moduleName    varchar(64)                        null comment '模块名称',
    operationType varchar(64)                        null comment '操作类型',
    description   varchar(255)                       null comment '描述',
    requestMethod varchar(16)                        null comment '请求方法',
    requestUrl    varchar(255)                       null comment '请求地址',
    requestParams text                               null comment '请求参数',
    responseData  text                               null comment '响应结果',
    operatorId    bigint                             null comment '操作人id',
    operatorName  varchar(64)                        null comment '操作人姓名',
    ip            varchar(64)                        null comment 'IP',
    executeStatus varchar(16)                        null comment '执行状态',
    durationMs    bigint                             null comment '耗时毫秒',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    key idx_createTime (createTime)
) comment '操作日志表';

create table if not exists loginLog
(
    id                     bigint auto_increment primary key comment '主键',
    userId                 bigint                             null comment '用户id',
    userAccount            varchar(128)                       null comment '用户名',
    userName               varchar(64)                        null comment '姓名',
    loginType              varchar(32)                        null comment '登录方式',
    loginStatus            varchar(16)                        not null comment '登录状态',
    failReason             varchar(255)                       null comment '失败原因',
    ip                     varchar(64)                        null comment 'IP',
    loginTime              datetime default CURRENT_TIMESTAMP not null comment '登录时间',
    logoutTime             datetime                           null comment '登出时间',
    sessionDurationSeconds bigint                             null comment '会话时长秒',
    createTime             datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    key idx_loginTime (loginTime)
) comment '登录日志表';

-- 采购订单表
create table if not exists purchase_order
(
    id           bigint auto_increment primary key comment '主键',
    orderNo      varchar(64)                           not null comment '采购单号',
    supplierId   bigint                                not null comment '供应商 id',
    totalAmount  decimal(10, 2)                        not null default 0.00 comment '总金额',
    totalCount   int                                   not null default 0 comment '总数量',
    orderStatus  varchar(32) default '待审核'          not null comment '订单状态（待审核/审核通过/审核不通过/已完成）',
    auditOpinion varchar(255)                          null comment '审核意见',
    auditorId    bigint                                null comment '审核人 id',
    auditorName  varchar(64)                           null comment '审核人姓名',
    auditTime    datetime                              null comment '审核时间',
    createTime   datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint     default 0                 not null comment '是否删除',
    unique key uk_orderNo (orderNo),
    key idx_supplierId (supplierId),
    key idx_orderStatus (orderStatus),
    key idx_createTime (createTime)
) comment '采购订单表';

-- 采购订单明细表
create table if not exists purchase_order_item
(
    id              bigint auto_increment primary key comment '主键',
    purchaseOrderId bigint                             not null comment '采购订单 id',
    medicineId      bigint                             not null comment '药品 id',
    medicineName    varchar(128)                       not null comment '药品名称快照',
    quantity        int                                not null comment '采购数量',
    purchasePrice   decimal(10, 2)                     not null comment '采购单价',
    lineAmount      decimal(10, 2)                     not null comment '行金额',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除',
    key idx_purchaseOrderId (purchaseOrderId),
    key idx_medicineId (medicineId)
) comment '采购订单明细表';

create table if not exists systemExceptionLog
(
    id               bigint auto_increment primary key comment '主键',
    exceptionType    varchar(128)                          not null comment '异常类型',
    exceptionMessage varchar(512)                          null comment '异常信息',
    stackTrace       longtext                              null comment '堆栈信息',
    requestUrl       varchar(255)                          null comment '请求地址',
    requestParams    text                                  null comment '请求参数',
    userId           bigint                                null comment '用户 id',
    userName         varchar(64)                           null comment '用户姓名',
    ip               varchar(64)                           null comment 'IP',
    handleStatus     varchar(32) default '待处理'          not null comment '处理状态',
    remark           varchar(255)                          null comment '备注',
    createTime       datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    key idx_createTime (createTime)
) comment '系统异常日志表';

ALTER TABLE salesOrder
    ADD COLUMN totalAmount DECIMAL(10, 2) COMMENT '订单金额';

insert into role (roleName, roleCode, roleDesc, menuPermissions, buttonPermissions, isSystem)
values ('系统管理员', 'systemAdmin', '拥有全部菜单与按钮权限',
        '["dashboard","category","medicine","inventory","sales","supplier","employee","role","log"]',
        '["category:add","category:edit","category:delete","medicine:add","medicine:edit","medicine:delete","inventory:in","inventory:out","sales:create","supplier:add","supplier:edit","supplier:delete","employee:add","employee:edit","employee:delete","role:add","role:edit","role:delete","log:view"]',
        1),
       ('店长', 'storeManager', '负责门店经营与库存管理',
        '["dashboard","category","medicine","inventory","sales","supplier"]',
        '["category:add","category:edit","medicine:add","medicine:edit","inventory:in","inventory:out","sales:create","supplier:add","supplier:edit"]',
        1),
       ('库管员', 'inventoryManager', '负责药品库存与供应商管理',
        '["dashboard","medicine","inventory","supplier"]',
        '["inventory:in","inventory:out","supplier:add","supplier:edit"]',
        1),
       ('销售员', 'salesClerk', '负责销售收银与基础查询',
        '["dashboard","medicine","sales"]',
        '["sales:create"]',
        1);
-- 插入 10 个药品信息
INSERT INTO medicine (categoryId, medicineName, specification, manufacturer, barcode, approvalNumber, unitName,
                      salePrice, shelfLifeDays, safeStockMin, safeStockMax, totalStock, isPrescription, status)
VALUES (1, '感冒灵颗粒', '10g*9 袋/盒', '华润三九医药股份有限公司', '6901234567890', '国药准字 Z44020001', '盒', 15.80,
        730, 50, 200, 120, 0, '启用'),
       (1, '布洛芬缓释胶囊', '0.3g*20 粒/盒', '中美天津史克制药有限公司', '6901234567891', '国药准字 H10900089', '盒',
        28.50, 600, 30, 150, 85, 1, '启用'),
       (1, '连花清瘟胶囊', '0.35g*24 粒/盒', '石家庄以岭药业股份有限公司', '6901234567892', '国药准字 Z20040063', '盒',
        32.00, 730, 40, 180, 95, 1, '启用'),
       (2, '奥美拉唑肠溶胶囊', '20mg*14 粒/盒', '阿斯利康制药有限公司', '6901234567893', '国药准字 H20033917', '盒',
        45.00, 540, 25, 120, 68, 1, '启用'),
       (2, '蒙脱石散', '3g*10 袋/盒', '博福 - 益普生制药有限公司', '6901234567894', '国药准字 H20000690', '盒', 22.80,
        730, 35, 160, 142, 0, '启用'),
       (2, '健胃消食片', '0.5g*36 片/盒', '江中药业股份有限公司', '6901234567895', '国药准字 Z36021108', '盒', 18.60,
        600, 45, 200, 178, 0, '启用'),
       (3, '二甲双胍片', '0.25g*48 片/盒', '中美上海施贵宝制药有限公司', '6901234567896', '国药准字 H20023370', '盒',
        12.50, 730, 60, 300, 256, 1, '启用'),
       (3, '硝苯地平控释片', '30mg*7 片/盒', '拜耳医药保健有限公司', '6901234567897', '国药准字 J20180024', '盒', 38.90,
        600, 30, 140, 89, 1, '启用'),
       (3, '阿托伐他汀钙片', '20mg*7 片/盒', '辉瑞制药有限公司', '6901234567898', '国药准字 J20120047', '盒', 52.00,
        730, 25, 120, 73, 1, '启用'),
       (1, '复方甘草片', '100 片/瓶', '北京同仁堂科技发展股份有限公司', '6901234567899', '国药准字 Z11020403', '瓶',
        9.80, 730, 50, 250, 198, 0, '启用');
insert into user (userAccount, userPassword, userName, employeeNo, phone, email, department, roleId, userRole,
                  userStatus)
select 'admin',
       md5('pharmacy12345678'),
       '系统管理员',
       'EMP0001',
       '13800000001',
       'admin@pharmacy.com',
       '信息部',
       id,
       'systemAdmin',
       '启用'
from role
where roleCode = 'systemAdmin';

insert into medicineCategory (categoryName, categoryRemark)
values ('感冒用药', '感冒发热、咳嗽类常用药'),
       ('肠胃用药', '胃痛、腹泻及消化类药品'),
       ('慢病用药', '高血压、糖尿病等慢病药品');

insert into supplier (supplierCode, supplierName, contactPerson, contactPhone, qualificationNo, qualificationExpireAt,
                      ratingLevel, settlementCycle, agreementPriceRemark, isBlacklisted)
values ('SUP001', '华东医药供应链', '张敏', '13800000001', 'Q-2026-001', '2027-12-31 23:59:59', 'A', '月结 30 天',
        '常用药协议价稳定', 0),
       ('SUP002', '国康药品配送中心', '李峰', '13800000002', 'Q-2026-002', '2026-10-31 23:59:59', 'B', '半月结',
        '促销药品价格需复核', 0);