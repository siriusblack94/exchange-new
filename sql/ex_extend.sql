SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `points`;
CREATE TABLE `points` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主建',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `coin_id` bigint(20) NOT NULL COMMENT '币种ID',
  `coin_name` varchar(20) NOT NULL COMMENT '币种名称',
  `count` decimal(20,8) NOT NULL COMMENT '金额',
  `plus_or_minus` varchar(2) NOT NULL COMMENT '加减 0转入 1转出',
  `sign` varchar(255) DEFAULT NULL COMMENT '签名',
  `type` varchar(40) NOT NULL COMMENT '币种类型 0 eth 1 usdt 2 btc 3 gtb',
  `status` int(4) NOT NULL COMMENT '1同步成功，0等待同步',
  `remark` varchar(50) NOT NULL COMMENT '0 等待兑换 1余额不足 2余额冻结成功 3兑换系统异常 4资金扣减(增加)异常 5兑换成功\n6资金解冻成功',
  `message` varchar(50) NOT NULL COMMENT '返回消息',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '''创建日期',
  `returnurl` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS  `user_syn`;
CREATE TABLE `user_syn` (
  `id` bigint(18) NOT NULL COMMENT '用户id',
  `parent_id` bigint(18) NOT NULL COMMENT '邀请人id',
  `account` varchar(255) NOT NULL COMMENT '账号',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `mobile` varchar(255) DEFAULT NULL COMMENT '手机号',
  `mail` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `real_name` varchar(255) NOT NULL COMMENT '真实姓名',
  `status` int(11) DEFAULT NULL COMMENT '1同步成功,0同步失败',
  `token` varchar(255) DEFAULT NULL COMMENT 'token',
  `message` varchar(255) DEFAULT NULL COMMENT 'message',
  `ctime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `utime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_ctime` (`ctime`) USING BTREE,
  KEY `idx_username` (`user_name`(191)) USING BTREE,
  KEY `idx_realname` (`real_name`(191)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息同步表';

DROP TABLE IF EXISTS  `user_syn_exception`;
CREATE TABLE `user_syn_exception` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_info` varchar(1000) NOT NULL COMMENT '用户信息',
  `type` varchar(20) NOT NULL COMMENT '类型 register:注册异常，update：更新异常',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1059781957727100931 DEFAULT CHARSET=utf8mb4 COMMENT='用户同步异常表';

SET FOREIGN_KEY_CHECKS = 1;

