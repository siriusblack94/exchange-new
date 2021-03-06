CREATE TABLE `${tableName}`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT,
  `market_id` bigint(18) NOT NULL COMMENT '市场ID',
  `market_type` int(11) NULL DEFAULT NULL COMMENT '交易对类型：1-币币交易；2-创新交易；',
  `trade_type` tinyint(4) NOT NULL COMMENT '交易类型:1 买 2卖',
  `symbol` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易对标识符',
  `market_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易对名称',
  `sell_user_id` bigint(18) NOT NULL COMMENT '卖方用户ID',
  `sell_coin_id` bigint(18) NOT NULL COMMENT '卖方币种ID',
  `sell_order_id` bigint(18) NOT NULL COMMENT '卖方委托订单ID',
  `sell_price` decimal(40, 20) NOT NULL COMMENT '卖方委托价格',
  `sell_fee_rate` decimal(40, 20) NOT NULL COMMENT '卖方手续费率',
  `sell_volume` decimal(40, 20) NOT NULL COMMENT '卖方委托数量',
  `buy_user_id` bigint(18) NOT NULL COMMENT '买方用户ID',
  `buy_coin_id` bigint(18) NOT NULL COMMENT '买方币种ID',
  `buy_order_id` bigint(18) NOT NULL COMMENT '买方委托订单ID',
  `buy_volume` decimal(40, 20) NOT NULL COMMENT '买方委托数量',
  `buy_price` decimal(40, 20) NOT NULL COMMENT '买方委托价格',
  `buy_fee_rate` decimal(40, 20) NOT NULL COMMENT '买方手续费率',
  `order_id` bigint(18) NOT NULL COMMENT '委托订单ID',
  `amount` decimal(40, 20) NOT NULL COMMENT '成交总额',
  `price` decimal(40, 20) NOT NULL DEFAULT 0.00000000000000000000 COMMENT '成交价格',
  `volume` decimal(40, 20) NOT NULL COMMENT '成交数量',
  `deal_sell_fee` decimal(40, 20) NOT NULL COMMENT '成交卖出手续费',
  `deal_sell_fee_rate` decimal(40, 20) NOT NULL COMMENT '成交卖出手续费率',
  `deal_buy_fee` decimal(40, 20) NOT NULL COMMENT '成交买入手续费',
  `deal_buy_fee_rate` decimal(40, 20) NOT NULL COMMENT '成交买入成交率费',
  `status` tinyint(4) NOT NULL COMMENT '状态0待成交，1已成交，2撤销，3.异常',
  `last_update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un_sell_buy_order_id`(`sell_order_id`, `buy_order_id`) USING BTREE,
  INDEX `idx_create_time`(`created`) USING BTREE,
  INDEX `idx_market_id`(`market_id`) USING BTREE,
  INDEX `turnover_sellorder_buyorder_market_index`(`market_id`, `sell_order_id`, `buy_order_id`) USING BTREE,
  INDEX `idx_selluserid`(`sell_user_id`) USING BTREE,
  INDEX `idx_buyuserid`(`buy_user_id`) USING BTREE,
  INDEX `idx_bid_sid`(`sell_order_id`, `buy_order_id`) USING BTREE,
  INDEX `idx_symbol`(`symbol`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_buy_order_id`(`buy_order_id`) USING BTREE,
  INDEX `idx_sell_order_id`(`sell_order_id`) USING BTREE,
  INDEX `turnover_symbol_status_buyorderid_index`(`symbol`, `status`, `buy_order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1036646594075639810 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '成交订单' ROW_FORMAT = Dynamic;

