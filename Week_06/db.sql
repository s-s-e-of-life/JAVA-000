
drop table `ds_user`;
create table `ds_user` (
  `id` varchar (50) primary key not null comment 'uuid主键',
  `username` varchar(30) not null comment '用户名',
  `password` varchar (100) not null comment '用户密码(加密后)',
  `email` varchar(25) default null comment '用户的电子邮箱',
  `create_time` bigint not null comment '创建时间'
) comment '用户表';

drop table `ds_user_info`;
create table `ds_user_info`(
  `id` varchar(50) primary key not null comment 'uuid主键',
  `name` nvarchar(20) not null comment '姓名',
  `identify` varchar(18) not null comment '身份证',
  `nickname` nvarchar(20) not null comment '昵称',
  `user_id` varchar(50) not null comment '关联用户表主键',
  `province` nvarchar(50) default null comment '省id',
  `city` nvarchar(50) default null comment '省市id',
  `county` nvarchar(50) default null comment '县id',
  `address` nvarchar(100) default null comment '详细地址',
  `phone` varchar(15) default null comment '手机号',
  `create_time` bigint not null comment '创建时间',
  `modify_time` bigint not null comment '上次修改时间'
) comment '用户信息表';

drop table `ds_user_rev_addr`;
create table `ds_user_rev_addr`(
  `id` varchar(50) primary key not null comment 'uuid主键',
  `user_id` varchar(50) not null comment '用户id',
  `rev_name` nvarchar(10) not null comment '收获人姓名',
  `rev_phone` varchar(15) not null comment '收获人手机号',
  `rev_addr` nvarchar(100) not null comment '收获人详细地址',
  `del` tinyint(1) not null default 0 comment '是否删除: 0-未删除;1- 删除',
  `create_time` bigint not null comment '创建时间',
  `modify_time` bigint not null comment '上次修改时间'
) comment '用户收货地址表';

drop table `ds_order`;
create table `ds_order` (
  `order_no` varchar(50) primary key not null comment '订单号',
  `user_id` varchar(50) not null comment '用户id',
  `state` tinyint(1) not null default 0 comment '订单状态: 0-未付款;1-已付款;2-已取消;',
  `del` tinyint(1) not null default 0 comment '是否删除: 0-未删除;1- 删除',
  `create_time` bigint not null comment '创建时间'
) comment '订单表';

drop table `ds_order_goods`;
create table `ds_order_goods`(
  `order_no` varchar(50) not null comment '订单号',
  `good_id` varchar(50) not null comment '商品号',
  `price` varchar(10) not null comment '单价',
  `count` tinyint(4) not null default 0 comment '数量',
  `del` tinyint(1) not null default 0 comment '是否删除: 0-未删除;1- 删除'
) comment '订单货物表';

drop table `ds_goods`;
create table `ds_goods` (
  `id` varchar(50) primary key not null comment '主键',
  `name` nvarchar(30) not null comment '商品名称',
  `price` varchar(10) not null comment '价格',
  `pic` varchar(50) not null comment '主展示图片id',
  `own_shop` varchar(50) not null comment '店家的id',
  `create_time` bigint not null comment '创建时间',
  `modify_time` bigint not null comment '上次修改时间',
  `del` tinyint(1) not null default 0 comment '是否删除: 0-未删除;1- 删除'
) comment '商品表';

drop table `ds_goods_description`;
create table `ds_goods_description` (
  `good_id` varchar(50) primary key not null comment '',
  `description` longtext not null comment '商品名称',
  `modify_time` bigint not null comment '上次修改时间'
) comment '商品描述表';

drop table `ds_goods_selling_point`;
create table `ds_goods_selling_point` (
  `id` varchar(50) primary key not null comment '',
  `name` nvarchar(30) not null comment '卖点',
  `own_shop` varchar(50) not null comment '店家的id',
  `create_time` bigint not null comment '创建时间',
  `modify_time` bigint not null comment '上次修改时间'
) comment '卖点表';


