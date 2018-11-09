package com.blockeng.sharding.muti;

import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
@Setter
public class MutiShardingDataSource implements DataSource {

    private Map<String, DataSource> dataSourceMap;

    private PrintWriter printWriter = new PrintWriter(System.out);

    private int timeout = 5;

    @Override
    public Connection getConnection() throws SQLException {

        return new MutiShardingConnection(this);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new MutiShardingConnection(this, username, password, timeout);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return printWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.printWriter = out;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.timeout = seconds;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.timeout;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getGlobal();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSourceMap.values().iterator().next().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return dataSourceMap.values().iterator().next().isWrapperFor(iface);
    }

    @Override
    public String toString() {
        return "MutiShardingDataSource{" +
                "dataSourceMap=" + dataSourceMap.entrySet().stream().map(entry ->
                "(" + entry.getKey() + ":" + entry.getValue().getClass() + ")"
        ).collect(Collectors.toList()) +
                '}';
    }
}
