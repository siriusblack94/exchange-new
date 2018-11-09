SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(18) DEFAULT NULL COMMENT '上级菜单ID',
  `parent_key` varchar(255) DEFAULT NULL COMMENT '上级菜单唯一KEY值',
  `type` tinyint(4) NOT NULL DEFAULT '2' COMMENT '类型 1-分类 2-节点',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `desc` varchar(256) DEFAULT NULL COMMENT '描述',
  `target_url` varchar(128) DEFAULT NULL COMMENT '目标地址',
  `sort` int(11) DEFAULT NULL COMMENT '排序索引',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 0-无效； 1-有效；',
  `create_by` bigint(18) DEFAULT NULL COMMENT '创建人',
  `modify_by` bigint(18) DEFAULT NULL COMMENT '修改人',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单';

DROP TABLE IF EXISTS  `sys_privilege`;
CREATE TABLE `sys_privilege` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_id` bigint(18) DEFAULT NULL COMMENT '所属菜单Id',
  `name` varchar(255) DEFAULT NULL COMMENT '功能点名称',
  `description` varchar(255) DEFAULT NULL COMMENT '功能描述',
  `url` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `create_by` bigint(18) DEFAULT NULL COMMENT '创建人',
  `modify_by` bigint(18) DEFAULT NULL COMMENT '修改人',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `unq_name` (`name`(191)) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1010101010101010435 DEFAULT CHARSET=utf8mb4 COMMENT='权限配置';

DROP TABLE IF EXISTS  `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `code` varchar(32) NOT NULL COMMENT '代码',
  `description` varchar(128) DEFAULT NULL COMMENT '描述',
  `create_by` bigint(18) DEFAULT NULL COMMENT '创建人',
  `modify_by` bigint(18) DEFAULT NULL COMMENT '修改人',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态0:禁用 1:启用',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1059423709228285955 DEFAULT CHARSET=utf8mb4 COMMENT='角色';

DROP TABLE IF EXISTS  `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(18) DEFAULT NULL,
  `menu_id` bigint(18) DEFAULT NULL,
  `create_by` bigint(18) DEFAULT NULL COMMENT '创建人',
  `modify_by` bigint(18) DEFAULT NULL COMMENT '修改人',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1060459680640077826 DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单';

DROP TABLE IF EXISTS  `sys_role_privilege`;
CREATE TABLE `sys_role_privilege` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(18) NOT NULL,
  `privilege_id` bigint(18) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1060459680531025923 DEFAULT CHARSET=utf8mb4 COMMENT='角色权限配置';

DROP TABLE IF EXISTS  `sys_role_privilege_user`;
CREATE TABLE `sys_role_privilege_user` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(18) NOT NULL COMMENT '角色Id',
  `user_id` bigint(18) NOT NULL COMMENT '用户Id',
  `privilege_id` bigint(18) NOT NULL COMMENT '权限Id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `pk_role_id_user_id_privilege_id` (`role_id`,`user_id`,`privilege_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1010101010101010196 DEFAULT CHARSET=utf8mb4 COMMENT='用户权限配置';

DROP TABLE IF EXISTS  `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) NOT NULL COMMENT '账号',
  `password` varchar(256) NOT NULL COMMENT '密码',
  `fullname` varchar(32) DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 0-无效； 1-有效；',
  `create_by` bigint(18) DEFAULT NULL COMMENT '创建人',
  `modify_by` bigint(18) DEFAULT NULL COMMENT '修改人',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1059423651665657858 DEFAULT CHARSET=utf8mb4 COMMENT='平台用户';

DROP TABLE IF EXISTS  `sys_user_log`;
CREATE TABLE `sys_user_log` (
  `id` bigint(18) NOT NULL COMMENT '主键',
  `group` varchar(255) DEFAULT NULL COMMENT '组',
  `user_id` bigint(18) DEFAULT NULL COMMENT '用户Id',
  `type` smallint(4) DEFAULT NULL COMMENT '日志类型 1查询 2修改 3新增 4删除 5导出 6审核',
  `method` varchar(255) DEFAULT NULL COMMENT '方法',
  `params` text COMMENT '参数',
  `time` bigint(20) DEFAULT NULL COMMENT '时间',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志';

DROP TABLE IF EXISTS  `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(18) DEFAULT NULL COMMENT '角色ID',
  `user_id` bigint(18) DEFAULT NULL COMMENT '用户ID',
  `create_by` bigint(18) DEFAULT NULL COMMENT '创建人',
  `modify_by` bigint(18) DEFAULT NULL COMMENT '修改人',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1059425917734862851 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色配置';

SET FOREIGN_KEY_CHECKS = 1;

