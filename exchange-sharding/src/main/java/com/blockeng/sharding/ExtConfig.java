package com.blockeng.sharding;

import lombok.Data;

@Data
public class ExtConfig {

    private boolean enableCreateTable; //是否自动创建表
    private String createTableSQL;  //建库sql
    private String createTableTime; //每天建表检查的时间 cron表达式
    private int createTableDays; //多创建几天的空表
    private boolean enableCreateRoute; //是否创建路由
    private String createRouteTime; //每天路由检查的时间 cron表达式
    private int createRouteDays; //多创建几天的路由
    private String tableName; // 表明
    private String dataSource; // ds名

}
