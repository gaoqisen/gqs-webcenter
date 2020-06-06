CREATE database if NOT EXISTS `webcenter` default character set utf8;
use `webcenter`;
create table sys_code
(
    id               bigint auto_increment
        primary key,
    name             varchar(20)  not null comment '名称',
    remark           varchar(500) null comment '描述',
    create_time      datetime     null comment '创建时间',
    client_id        varchar(50)  null comment '客户端ID',
    secret_key       varchar(50)  null comment '密钥',
    application_name varchar(200) not null
)
    comment '系统编码';
create table sys_code_menu
(
    id      bigint auto_increment
        primary key,
    sys_id  varchar(50) not null comment '系统ID',
    menu_id varchar(20) not null comment '菜单ID'
)
    comment '系统菜单关联';
create table sys_log
(
    id          bigint auto_increment
        primary key,
    username    varchar(50)   null comment '用户名',
    operation   varchar(50)   null comment '用户操作',
    method      varchar(200)  null comment '请求方法',
    params      varchar(5000) null comment '请求参数',
    time        bigint        not null comment '执行时长(毫秒)',
    ip          varchar(64)   null comment 'IP地址',
    create_date datetime      null comment '创建时间'
)
    comment '系统日志';
create table sys_menu
(
    menu_id   bigint auto_increment
        primary key,
    parent_id bigint       null comment '父菜单ID，一级菜单为0',
    name      varchar(50)  null comment '菜单名称',
    url       varchar(200) null comment '菜单URL',
    type      int          null comment '类型   0：目录   1：菜单   2：按钮',
    icon      varchar(50)  null comment '菜单图标',
    order_num int          null comment '排序'
)
    comment '菜单管理';
create table sys_rest
(
    digest           varchar(32)                not null comment 'url和应用名称的摘要',
    url              varchar(500)               not null comment 'url链接',
    api_name         varchar(500)               null comment 'api名称',
    create_time      datetime                   not null comment '创建时间',
    application_name varchar(200)               not null comment '应用名称',
    permissions      varchar(40) default 'anon' null comment '权限（anon不用登录就可以访问，authc登录后即可访问，perms[权限]拥有此权限方可访问）,logout 退出'
)
    comment '系统接口';
create table sys_role
(
    role_id        bigint auto_increment
        primary key,
    role_name      varchar(100) null comment '角色名称',
    remark         varchar(100) null comment '备注',
    create_user_id bigint       null comment '创建者ID',
    create_time    datetime     null comment '创建时间'
)
    comment '角色';
create table sys_role_menu
(
    id      bigint auto_increment
        primary key,
    role_id bigint null comment '角色ID',
    menu_id bigint null comment '菜单ID'
)
    comment '角色菜单关联';
create table sys_role_rest
(
    id      bigint auto_increment
        primary key,
    role_id bigint      not null comment '角色ID',
    rest_id varchar(36) not null comment '菜单ID'
)
    comment '系统角色关联';
create table sys_user
(
    user_id        bigint auto_increment
        primary key,
    username       varchar(50)  not null comment '用户名',
    password       varchar(100) null comment '密码',
    salt           varchar(20)  null comment '盐',
    email          varchar(100) null comment '邮箱',
    mobile         varchar(100) null comment '手机号',
    status         tinyint      null comment '状态  0：禁用   1：正常',
    create_user_id bigint       null comment '创建者ID',
    create_time    datetime     null comment '创建时间',
    constraint username
        unique (username)
)
    comment '系统用户';
create table sys_user_role
(
    id      bigint auto_increment
        primary key,
    user_id bigint null comment '用户ID',
    role_id bigint null comment '角色ID'
)
    comment '用户角色关联';

INSERT INTO sys_code (id, name, remark, create_time, client_id, secret_key, application_name) VALUES (1, '网络中心', '用于管理各个系统', '2020-03-17 22:14:35', '1', '1', 'webcenter-console');
INSERT INTO sys_code (id, name, remark, create_time, client_id, secret_key, application_name) VALUES (2, '客户端测试', '客户端测试', '2020-05-13 02:21:43', 'WZUIIXWZUIIX', 'qOIWRbzeFvOnXUYTspfSt2ibfJPe1vtG', 'webcenter-sample');

INSERT INTO sys_code_menu (id, sys_id, menu_id) VALUES (1, '1', '1');
INSERT INTO sys_code_menu (id, sys_id, menu_id) VALUES (2, '1', '2');
INSERT INTO sys_code_menu (id, sys_id, menu_id) VALUES (3, '1', '3');
INSERT INTO sys_code_menu (id, sys_id, menu_id) VALUES (4, '1', '4');
INSERT INTO sys_code_menu (id, sys_id, menu_id) VALUES (5, '1', '5');

INSERT INTO sys_menu (menu_id, parent_id, name, url, type, icon, order_num) VALUES (1, 0, '管理员列表', 'sys/user', 1, 'fa fa-user', 1);
INSERT INTO sys_menu (menu_id, parent_id, name, url, type, icon, order_num) VALUES (2, 0, '角色管理', 'sys/role', 1, 'fa fa-universal-access', 2);
INSERT INTO sys_menu (menu_id, parent_id, name, url, type, icon, order_num) VALUES (3, 0, '菜单管理', 'sys/menu', 1, 'fa fa-book', 3);
INSERT INTO sys_menu (menu_id, parent_id, name, url, type, icon, order_num) VALUES (4, 0, '系统管理', 'sys/code', 1, 'fa fa-send-o', 2);
INSERT INTO sys_menu (menu_id, parent_id, name, url, type, icon, order_num) VALUES (5, 0, 'REST管理', 'sys/rest', 1, 'fa fa-plug', 0);
INSERT INTO sys_menu (menu_id, parent_id, name, url, type, icon, order_num) VALUES (6, 0, '日志管理', '/sys/log', 1, 'fa fa-angle-double-down', 0);

INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('d2ee06feade13ad24adf33ba8668ec0a', '/sys/login', null, '2020-04-29 07:48:32', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('8bae459fc6d750cf99a298ee58e94212', '/sys/config/list', '获取系统配置', '2020-05-25 20:21:07', 'webcenter-console', 'authc');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('328d605b98fc258d84f204bce31b84ea', '/sys/menu/update', '更新菜单', '2020-05-25 20:21:07', 'webcenter-console', 'perms[328d605b98fc258d84f204bce31b84ea]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('8d77be49e83486802459bd417bf4543d', '/sys/menu/delete/{menuId}', '通过菜单ID删除菜单', '2020-05-25 20:21:07', 'webcenter-console', 'perms[8d77be49e83486802459bd417bf4543d]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('32779c236a2bb9cd2eaf721e0a701b37', '/sys/menu/list', '获取菜单列表', '2020-05-25 20:21:07', 'webcenter-console', 'perms[32779c236a2bb9cd2eaf721e0a701b37]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('31af187714ce5831d55394f98b660073', '/sys/menu/save', '保存菜单', '2020-05-25 20:21:07', 'webcenter-console', 'perms[31af187714ce5831d55394f98b660073]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('665742656274fcb5921428f2db519d8d', '/sys/menu/info/{menuId}', '通过菜单ID获取菜单信息', '2020-05-25 20:21:07', 'webcenter-console', 'authc');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('853d70691e764d3c58cd7e1443b9854b', '/sys/menu/nav', null, '2020-05-25 20:21:07', 'webcenter-console', 'perms[853d70691e764d3c58cd7e1443b9854b]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('7e2b170d8f0b0ac17920591f0591ee5c', '/main/login', '系统登录', '2020-05-25 20:21:07', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('a14a09fdde8c9201a32171e1ea0f0e85', '/main/api/login', 'API登录', '2020-05-20 09:31:48', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('5f7522330a335422822ad94b7adcd6a6', '/main/captcha.jpg', '验证码', '2020-05-25 20:21:07', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('4100f9115dc05fc206f92f5df1136af9', '/main/logout', '系统退出', '2020-05-25 20:21:07', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('7d09d95bc3476c622d00b85168e59fa8', '/sys/menu/queryMenu', '查询所有菜单', '2020-05-25 20:21:07', 'webcenter-console', 'perms[7d09d95bc3476c622d00b85168e59fa8]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('c1db84e8741cd980a1e6d1c69d416007', '/sys/code/queryAll', '获取所有系统信息', '2020-05-25 20:21:07', 'webcenter-console', 'perms[c1db84e8741cd980a1e6d1c69d416007]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('3a63db81ea1421974e32ac498bd20d1', '/sys/code/update', '系统信息修改', '2020-05-25 20:21:07', 'webcenter-console', 'perms[3a63db81ea1421974e32ac498bd20d1]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('3b7ff725e4ad43202303e7aafdef37e4', '/sys/code/delete/{id}', '系统信息删除', '2020-05-25 20:21:07', 'webcenter-console', 'perms[3b7ff725e4ad43202303e7aafdef37e4]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('dabae2301abb372a5c60a74e065a1d06', '/sys/code/info/{id}', '通过ID获取系统信息', '2020-05-25 20:21:07', 'webcenter-console', 'perms[dabae2301abb372a5c60a74e065a1d06]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('5c69e7448dc256c0a372695dd2c2bc6c', '/sys/rest/update', 'REST接口修改', '2020-05-25 20:21:07', 'webcenter-console', 'perms[5c69e7448dc256c0a372695dd2c2bc6c]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('47c0b8454c7d5bc1cc613bfc6ac89b08', '/sys/rest/list', '分页获取REST接口信息', '2020-05-25 20:21:07', 'webcenter-console', 'perms[47c0b8454c7d5bc1cc613bfc6ac89b08]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('345c649db3eb2655dcc7e3947f3e89a7', '/sys/user/update', '更新用户', '2020-05-25 20:21:07', 'webcenter-console', 'perms[345c649db3eb2655dcc7e3947f3e89a7]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('57cbf9d66c5423377b3f244039f42fff', '/sys/user/delete/{id}', '删除用户', '2020-05-08 08:13:49', 'webcenter-console', 'perms[57cbf9d66c5423377b3f244039f42fff]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('2dee80b3157eb2ac200f20a12603ad49', '/sys/user/list', '分页获取用户列表', '2020-05-25 20:21:07', 'webcenter-console', 'perms[2dee80b3157eb2ac200f20a12603ad49]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('2722e214e611fccd97ca5c546eeaa5ba', '/sys/user/save', '新增用户', '2020-05-25 20:21:07', 'webcenter-console', 'perms[2722e214e611fccd97ca5c546eeaa5ba]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('5f6574cd8a8ac4984d95910e2f17d4dd', '/sys/role/queryAll', '获取全部角色', '2020-05-25 20:21:07', 'webcenter-console', 'perms[5f6574cd8a8ac4984d95910e2f17d4dd]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('2772ab28f745060a2fc081c3fc9e265b', '/sys/user/info/{id}', '获取用户信息', '2020-05-25 20:21:07', 'webcenter-console', 'perms[2772ab28f745060a2fc081c3fc9e265b]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('39a77375bf3bc871830c03f989d45c0f', '/sys/user/delete', '删除用户', '2020-05-25 20:21:07', 'webcenter-console', 'perms[39a77375bf3bc871830c03f989d45c0f]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('7f78739a00501e4e477f789495bd1f61', '/sys/role/delete', '删除角色', '2020-05-25 20:21:07', 'webcenter-console', 'perms[7f78739a00501e4e477f789495bd1f61]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('498d4a72d5a1addde4f9f88279e152b7', '/sys/role/list', '分页查询角色', '2020-05-25 20:21:07', 'webcenter-console', 'perms[498d4a72d5a1addde4f9f88279e152b7]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('16640e87d13b03c49baa5ee539a151c4', '/sys/role/save', '增加角色', '2020-05-25 20:21:07', 'webcenter-console', 'perms[16640e87d13b03c49baa5ee539a151c4]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('4421dcb4c2e1b8204147d0aa5bc74681', '/sys/role/update', '修改角色', '2020-05-25 20:21:07', 'webcenter-console', 'perms[4421dcb4c2e1b8204147d0aa5bc74681]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('f4d73a308a2765662b45597d4582685f', '/sys/role/info/{roleId}', '通过ID获取角色信息', '2020-05-25 20:21:07', 'webcenter-console', 'perms[f4d73a308a2765662b45597d4582685f]');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('d70a720416045d3af441bab0ee1a4108', '/sys/rest/listObject', '获取rest接口对象', '2020-05-09 09:58:44', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('faad9ce8e60a90e35e622bd24913d159', '/sys/menu/listObject', '获取菜单接口对象', '2020-05-09 09:58:44', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('2792b4fbb983fd16ed6d610c156de556', '/sys/rest/listRestByApplicationName', '获取rest接口对象', '2020-05-25 20:21:07', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('f98272279105f093a557b6333c25b54', '/sys/user/password', '修改密码', '2020-05-25 20:21:07', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('b52bd0cb0e6efdc96578970d9b03cc0f', '/sys/code/save', '系统信息保存', '2020-05-25 20:21:07', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('4140174b466967b88d5218ce11966e9c', '/sys/code/queryAllInfo', '获取所有系统信息', '2020-05-25 20:21:07', 'webcenter-console', 'anon');
INSERT INTO sys_rest (digest, url, api_name, create_time, application_name, permissions) VALUES ('e48d454650d06a939b610f92f2d56e33', '/main/sso/login', '单点登录判断', '2020-05-25 20:21:07', 'webcenter-console', 'anon');

INSERT INTO sys_role (role_id, role_name, remark, create_user_id, create_time) VALUES (1, '管理员', '超级管理员', null, '2020-05-10 22:41:00');

INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (9, 1, 1);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (10, 1, 2);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (11, 1, 3);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (12, 1, 4);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (13, 1, 5);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (19, 1, 6);

INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (43, 1, 'd2ee06feade13ad24adf33ba8668ec0a');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (44, 1, '8bae459fc6d750cf99a298ee58e94212');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (45, 1, '328d605b98fc258d84f204bce31b84ea');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (46, 1, '8d77be49e83486802459bd417bf4543d');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (47, 1, '32779c236a2bb9cd2eaf721e0a701b37');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (48, 1, '31af187714ce5831d55394f98b660073');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (49, 1, '665742656274fcb5921428f2db519d8d');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (50, 1, '853d70691e764d3c58cd7e1443b9854b');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (51, 1, '7e2b170d8f0b0ac17920591f0591ee5c');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (52, 1, 'a14a09fdde8c9201a32171e1ea0f0e85');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (53, 1, '5f7522330a335422822ad94b7adcd6a6');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (54, 1, '4100f9115dc05fc206f92f5df1136af9');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (55, 1, '7d09d95bc3476c622d00b85168e59fa8');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (56, 1, 'c1db84e8741cd980a1e6d1c69d416007');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (57, 1, '3a63db81ea1421974e32ac498bd20d1');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (58, 1, '3b7ff725e4ad43202303e7aafdef37e4');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (59, 1, 'dabae2301abb372a5c60a74e065a1d06');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (60, 1, '5c69e7448dc256c0a372695dd2c2bc6c');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (61, 1, '47c0b8454c7d5bc1cc613bfc6ac89b08');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (62, 1, '345c649db3eb2655dcc7e3947f3e89a7');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (63, 1, '57cbf9d66c5423377b3f244039f42fff');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (64, 1, '2dee80b3157eb2ac200f20a12603ad49');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (65, 1, '2722e214e611fccd97ca5c546eeaa5ba');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (66, 1, '5f6574cd8a8ac4984d95910e2f17d4dd');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (67, 1, '2772ab28f745060a2fc081c3fc9e265b');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (68, 1, '39a77375bf3bc871830c03f989d45c0f');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (69, 1, '7f78739a00501e4e477f789495bd1f61');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (70, 1, '498d4a72d5a1addde4f9f88279e152b7');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (71, 1, '16640e87d13b03c49baa5ee539a151c4');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (72, 1, '4421dcb4c2e1b8204147d0aa5bc74681');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (73, 1, 'f4d73a308a2765662b45597d4582685f');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (74, 1, 'd70a720416045d3af441bab0ee1a4108');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (75, 1, 'faad9ce8e60a90e35e622bd24913d159');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (76, 1, '2792b4fbb983fd16ed6d610c156de556');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (77, 1, 'f98272279105f093a557b6333c25b54');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (78, 1, 'b52bd0cb0e6efdc96578970d9b03cc0f');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (79, 1, '4140174b466967b88d5218ce11966e9c');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (80, 1, 'e48d454650d06a939b610f92f2d56e33');
INSERT INTO sys_role_rest (id, role_id, rest_id) VALUES (81, 1, '90cded533a47ae286b7febd20326560e');

INSERT INTO sys_user (user_id, username, password, salt, email, mobile, status, create_user_id, create_time) VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', '', '1073825890@qq.com', '15885529693', 1, 1, '2016-11-11 11:11:11');

INSERT INTO sys_user_role (id, user_id, role_id) VALUES (1, 1, 1);