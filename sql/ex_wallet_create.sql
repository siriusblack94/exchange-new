SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `address_pool`;
CREATE TABLE `address_pool` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `coin_id` bigint(18) NOT NULL COMMENT '币种ID',
  `address` varchar(100) NOT NULL COMMENT '地址',
  `keystore` varchar(1024) DEFAULT '' COMMENT 'keystore',
  `pwd` varchar(200) DEFAULT '' COMMENT '密码',
  `coin_type` varchar(50) NOT NULL DEFAULT '' COMMENT '地址类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `unq_address` (`address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1039033984373712658 DEFAULT CHARSET=utf8mb4 COMMENT='钱包地址池';

DROP TABLE IF EXISTS  `admin_address`;
CREATE TABLE `admin_address` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `coin_id` bigint(18) DEFAULT NULL COMMENT '币种Id',
  `keystore` varchar(1024) DEFAULT NULL COMMENT 'eth keystore',
  `pwd` varchar(200) DEFAULT NULL COMMENT 'eth账号密码',
  `address` varchar(50) DEFAULT NULL COMMENT '地址',
  `status` int(4) DEFAULT NULL COMMENT '1:归账(冷钱包地址),2:打款,3:手续费',
  `coin_type` varchar(50) NOT NULL DEFAULT '' COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1032958549296656387 DEFAULT CHARSET=utf8mb4 COMMENT='钱包归集提币地址管理';

DROP TABLE IF EXISTS  `coin_balance`;
CREATE TABLE `coin_balance` (
  `id` bigint(18) NOT NULL COMMENT '主键',
  `coin_id` bigint(18) DEFAULT NULL COMMENT '币种ID',
  `coin_name` varchar(16) DEFAULT NULL COMMENT '币种名称',
  `system_balance` decimal(20,8) DEFAULT NULL COMMENT '系统余额（根据充值提币计算）',
  `coin_type` varchar(50) DEFAULT NULL COMMENT '币种类型',
  `collect_account_balance` decimal(20,8) DEFAULT NULL COMMENT '归集账户余额',
  `loan_account_balance` decimal(20,8) DEFAULT NULL COMMENT '钱包账户余额',
  `fee_account_balance` decimal(20,8) DEFAULT NULL COMMENT '手续费账户余额(eth转账需要手续费)',
  `recharge_account_balance` decimal(20,8) DEFAULT NULL COMMENT '充值账户余额(一般xrp,bts这些需要)',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='币种余额';

DROP TABLE IF EXISTS  `coin_config`;
CREATE TABLE `coin_config` (
  `id` bigint(18) NOT NULL COMMENT '币种ID(对应coin表ID)',
  `name` varchar(100) NOT NULL COMMENT '币种名称',
  `coin_type` varchar(50) NOT NULL DEFAULT '' COMMENT '币种类型：btc-比特币系列；eth-以太坊；ethToken-以太坊代币；etc-以太经典；',
  `credit_limit` decimal(20,8) DEFAULT NULL COMMENT '钱包最低留存的币',
  `credit_max_limit` decimal(20,8) DEFAULT NULL COMMENT '当触发改状态的时候,开始归集',
  `auto_draw_limit` decimal(20,8) DEFAULT NULL COMMENT '自动提币的最高额度',
  `auto_recharge` int(4) NOT NULL DEFAULT '1' COMMENT '自动充值。0手动，1自动',
  `auto_draw` int(4) NOT NULL DEFAULT '0' COMMENT '是否自动提币',
  `auto_collect` int(4) NOT NULL DEFAULT '0' COMMENT '自动归集。0手动，1自动',
  `auto_address` int(4) NOT NULL DEFAULT '0' COMMENT '自动地址。0自手动，1自动',
  `rpc_ip` varchar(20) DEFAULT NULL COMMENT 'rpc服务ip',
  `rpc_port` varchar(10) DEFAULT NULL COMMENT 'rpc服务port',
  `rpc_user` varchar(30) DEFAULT NULL COMMENT 'rpc用户',
  `rpc_pwd` varchar(200) DEFAULT NULL COMMENT 'rpc密码',
  `last_block` varchar(100) DEFAULT '' COMMENT '最后一个区块',
  `wallet_user` varchar(64) DEFAULT NULL COMMENT '钱包用户名',
  `wallet_pass` varchar(50) DEFAULT NULL COMMENT '钱包密码',
  `contract_address` varchar(100) DEFAULT '' COMMENT '代币合约地址',
  `context` varchar(50) DEFAULT NULL COMMENT 'context',
  `min_confirm` int(4) DEFAULT '1' COMMENT '最低确认数',
  `task` varchar(50) DEFAULT NULL COMMENT '定时任务',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '是否可用0不可用,1可用',
  `rpc_ip_out` varchar(30) DEFAULT NULL,
  `rpc_port_out` varchar(20) DEFAULT NULL,
  `rpc_user_out` varchar(100) DEFAULT NULL,
  `rpc_pwd_out` varchar(200) DEFAULT NULL,
  `wallet_pass_out` varchar(200) DEFAULT NULL,
  `wallet_user_out` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_status_wallet_type` (`coin_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='币种钱包配置';

DROP TABLE IF EXISTS  `coin_recharge`;
CREATE TABLE `coin_recharge` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(18) unsigned NOT NULL COMMENT '用户id',
  `coin_id` bigint(18) NOT NULL COMMENT '币种id',
  `coin_name` varchar(255) NOT NULL DEFAULT '' COMMENT '币种名称',
  `coin_type` varchar(50) NOT NULL DEFAULT '' COMMENT '币种类型',
  `address` varchar(255) DEFAULT NULL COMMENT '钱包地址',
  `confirm` int(1) NOT NULL COMMENT '充值确认数',
  `status` int(4) DEFAULT '0' COMMENT '状态：0-待入帐；1-充值失败，2到账失败，3到账成功；',
  `txid` varchar(80) DEFAULT NULL COMMENT '交易id',
  `amount` decimal(20,8) DEFAULT NULL COMMENT '实际到账',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1058011340606312450 DEFAULT CHARSET=utf8mb4 COMMENT='数字货币充值记录';

DROP TABLE IF EXISTS  `coin_server`;
CREATE TABLE `coin_server` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `rpc_ip` varchar(50) NOT NULL DEFAULT '' COMMENT '钱包服务器ip',
  `rpc_port` varchar(50) NOT NULL DEFAULT '' COMMENT '钱包服务器ip',
  `running` int(1) NOT NULL COMMENT '服务是否运行 0:正常,1:停止',
  `wallet_number` bigint(255) NOT NULL COMMENT '钱包服务器区块高度',
  `coin_name` varchar(50) NOT NULL COMMENT '钱包名称',
  `mark` varchar(100) DEFAULT NULL COMMENT '备注信息',
  `real_number` bigint(255) DEFAULT NULL COMMENT '真实区块高度',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1015894258013474819 DEFAULT CHARSET=utf8mb4 COMMENT='钱包服务器监控信息';

DROP TABLE IF EXISTS  `coin_withdraw`;
CREATE TABLE `coin_withdraw` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(18) unsigned NOT NULL COMMENT '用户id',
  `coin_id` bigint(18) NOT NULL COMMENT '币种id',
  `coin_name` varchar(255) NOT NULL DEFAULT '' COMMENT '币种名称',
  `coin_type` varchar(50) NOT NULL DEFAULT '' COMMENT '币种类型',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '钱包地址',
  `txid` varchar(80) DEFAULT NULL COMMENT '交易id',
  `num` decimal(20,8) NOT NULL COMMENT '提现量',
  `fee` decimal(20,8) NOT NULL COMMENT '手续费()',
  `mum` decimal(20,8) NOT NULL COMMENT '实际提现',
  `type` tinyint(1) DEFAULT '0' COMMENT '0站内1其他',
  `chain_fee` decimal(20,8) DEFAULT NULL COMMENT '链上手续费花费',
  `block_num` int(11) unsigned DEFAULT '0' COMMENT '区块高度',
  `wallet_mark` varchar(255) DEFAULT NULL COMMENT '钱包提币备注备注',
  `remark` varchar(255) DEFAULT NULL COMMENT '后台审核人员提币备注备注',
  `step` tinyint(4) DEFAULT NULL COMMENT '当前审核级数',
  `status` tinyint(1) NOT NULL COMMENT '状态：0-审核中；1-成功；2-拒绝；3-撤销；4-审核通过；5-打币中；',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `userid` (`user_id`) USING BTREE,
  KEY `coinname` (`coin_id`) USING BTREE,
  KEY `idx_created` (`created`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=245 DEFAULT CHARSET=utf8mb4 COMMENT='数字货币提现记录';

DROP TABLE IF EXISTS  `user_address`;
CREATE TABLE `user_address` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(18) NOT NULL COMMENT '用户ID',
  `coin_id` bigint(18) NOT NULL COMMENT '币种ID',
  `address` varchar(100) NOT NULL DEFAULT '' COMMENT '地址',
  `keystore` varchar(1024) DEFAULT NULL COMMENT 'keystore',
  `pwd` varchar(200) DEFAULT NULL COMMENT '密码',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `USERETH_UID_COINID_INX` (`user_id`,`coin_id`) USING BTREE,
  KEY `USERETH_ADDRESS_INX` (`address`) USING BTREE,
  KEY `idx_coin_id` (`coin_id`) USING BTREE,
  KEY `idx_coin_id_address` (`coin_id`,`address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1060417742729678851 DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包地址信息';

DROP TABLE IF EXISTS  `user_address_for_sgc`;
CREATE TABLE `user_address_for_sgc` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(18) NOT NULL COMMENT '用户ID',
  `coin_id` bigint(18) NOT NULL COMMENT '币种ID',
  `address` varchar(100) NOT NULL DEFAULT '' COMMENT '地址',
  `keystore` varchar(1024) DEFAULT NULL COMMENT 'keystore',
  `pwd` varchar(200) DEFAULT NULL COMMENT '密码',
  `last_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `USERETH_UID_COINID_INX` (`user_id`,`coin_id`) USING BTREE,
  KEY `USERETH_ADDRESS_INX` (`address`) USING BTREE,
  KEY `idx_coin_id` (`coin_id`) USING BTREE,
  KEY `idx_coin_id_address` (`coin_id`,`address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1051834445762220035 DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包地址信息';

DROP TABLE IF EXISTS  `wallet_collect_task`;
CREATE TABLE `wallet_collect_task` (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `coin_id` bigint(18) NOT NULL COMMENT '币种ID',
  `coin_type` varchar(11) NOT NULL DEFAULT '' COMMENT '币种类型',
  `coin_name` varchar(20) DEFAULT NULL COMMENT '币种名称',
  `user_id` bigint(18) DEFAULT NULL COMMENT '来自哪个用户',
  `txid` varchar(100) DEFAULT NULL COMMENT 'txid',
  `amount` decimal(20,8) DEFAULT NULL COMMENT '归集数量',
  `chain_fee` decimal(20,8) DEFAULT NULL COMMENT '链上手续费',
  `mark` varchar(200) DEFAULT '' COMMENT '备注',
  `status` int(10) NOT NULL DEFAULT '0' COMMENT '是否处理',
  `from_address` varchar(100) DEFAULT NULL COMMENT '来自哪个地址',
  `to_address` varchar(80) DEFAULT NULL COMMENT '转到哪里',
  `last_update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1058011340669227010 DEFAULT CHARSET=utf8mb4 COMMENT='钱包归集任务';

DROP TABLE IF EXISTS  `wallet_info`;
CREATE TABLE `wallet_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `coin_id` bigint(20) NOT NULL COMMENT '币种ID',
  `name` varchar(100) NOT NULL COMMENT '币种名称',
  `balance_total` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '系统持币总额',
  `wallet_user_count` int(11) DEFAULT '0' COMMENT '钱包持币用户数量',
  `user_count` int(11) DEFAULT '0' COMMENT '系统持币用户数量',
  `coin_recharge_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '充币数量',
  `coin_withdraw_amount` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '提币数量',
  `balance_total_in` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '转入地址钱包余额',
  `balance_total_out` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '转出地址钱包余额',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始统计时间',
  `date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '存入数据库时间',
  `wallet_balance_total` decimal(20,8) NOT NULL DEFAULT '0.00000000' COMMENT '钱包余额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1052854138508103683 DEFAULT CHARSET=utf8mb4 COMMENT='钱包余额信息表';

SET FOREIGN_KEY_CHECKS = 1;

