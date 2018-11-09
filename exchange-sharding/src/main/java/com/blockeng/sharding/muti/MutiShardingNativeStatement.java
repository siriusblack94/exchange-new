package com.blockeng.sharding.muti;

import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.SQLException;

public class MutiShardingNativeStatement {

    private MutiShardingConnection mutiShardingConnection;

    private SQLParser sqlParser = new SQLParser();

    public MutiShardingNativeStatement(MutiShardingConnection mutiShardingConnection) {
        this.mutiShardingConnection = mutiShardingConnection;
    }

    public String nativeSQL(String sql) throws SQLException {
        String tableName = ((tableName = sqlParser.parserTableName(sql)) != null) ? tableName :
                        mutiShardingConnection.getDataSource().getDataSourceMap().keySet().iterator().next();
        Connection connection = cachedConnectionAndGet(mutiShardingConnection, tableName);
        return connection.nativeSQL(sql);
    }

    protected Connection cachedConnectionAndGet(MutiShardingConnection mutiShardingConnection, String tableName) throws SQLException {
        if (!mutiShardingConnection.getCachedConnections().containsKey(tableName)) {
            Assert.isTrue(mutiShardingConnection.getDataSource().getDataSourceMap()
                    .containsKey(tableName), String.format("Miss table %s", tableName));
            Connection connection = null;
            if (mutiShardingConnection.isHasPassword()) {
                connection = mutiShardingConnection.getDataSource().getDataSourceMap().get(tableName)
                        .getConnection(mutiShardingConnection.getUsername(), mutiShardingConnection.getPassword());
            } else {
                connection = mutiShardingConnection.getDataSource().getDataSourceMap().get(tableName).getConnection();
            }
            mutiShardingConnection.getCachedConnections().put(tableName, connection);
            mutiShardingConnection.setTrueConnection(connection);
            return connection;
        } else {
            Connection connection = mutiShardingConnection.getCachedConnections().get(tableName);
            mutiShardingConnection.setTrueConnectionTableName(tableName);
            mutiShardingConnection.setTrueConnection(connection);
            return connection;
        }
    }
}
