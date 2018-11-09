CREATE TABLE `${tableName}`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(18) NOT NULL COMMENT '用户id',
  `coin_id` bigint(18) NOT NULL COMMENT '币种id',
  `account_id` bigint(18) NOT NULL COMMENT '账户id',
  `ref_account_id` bigint(18) NOT NULL COMMENT '关联方的账户ID',
  `order_id` bigint(18) NOT NULL DEFAULT 0 COMMENT '订单ID',
  `direction` tinyint(4) NOT NULL COMMENT '资金方向：入账为-1；出账为-2',
  `business_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型',
  `amount` decimal(40, 20) NOT NULL COMMENT '数量/金额',
  `fee` decimal(40, 20) NULL DEFAULT 0.00000000000000000000 COMMENT '手续费',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备注',
  `created` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `payment_detail_accountid_fk`(`account_id`) USING BTREE,
  INDEX `payment_detail_userid_fk`(`user_id`) USING BTREE,
  INDEX `payment_detail_refaccountid_fk`(`ref_account_id`) USING BTREE,
  INDEX `fk_accountdetail_coinid`(`coin_id`) USING BTREE,
  INDEX `payment_detail_created_fk`(`created`) USING BTREE,
  INDEX `idx_business_type`(`business_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1025326424497310770 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资金账户流水' ROW_FORMAT = Dynamic;

