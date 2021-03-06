package com.blockeng.sharding.muti;

import com.blockeng.sharding.muti.adapter.PrePareStatementAdapter;
import lombok.Getter;

import java.sql.*;
import java.util.function.Function;

@Getter
public class MutiShardingPrepareStatement extends PrePareStatementAdapter implements PreparedStatement {


    private Connection trueConnection;

    private PreparedStatement trueStatement;


    private String sql;

    @Deprecated
    private MutiShardingPrepareStatement(MutiShardingConnection mutiShardingConnection) {
        super(mutiShardingConnection);
    }


    public MutiShardingPrepareStatement(MutiShardingConnection mutiShardingConnection, String sql) {
        super(mutiShardingConnection);
        this.sql = sql;
        try {
            cachedStatementsAndGet(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public MutiShardingPrepareStatement(MutiShardingConnection mutiShardingConnection, String sql,
                                        int resultType, int resultSetConcurrency) {
        super(mutiShardingConnection, resultType, resultSetConcurrency);
        this.sql = sql;
        try {
            cachedStatementsAndGet(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MutiShardingPrepareStatement(MutiShardingConnection mutiShardingConnection, String sql,
                                        int resultType, int resultSetConcurrency, int resultSetHoldability) {
        super(mutiShardingConnection, resultType, resultSetConcurrency, resultSetHoldability);
        this.sql = sql;
        try {
            cachedStatementsAndGet(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MutiShardingPrepareStatement(MutiShardingConnection mutiShardingConnection, String sql, int autoGeneratedKeys) {
        super(mutiShardingConnection);
        this.sql = sql;
        try {
            cachedStatementsAndGet(sql, connection -> {
                try {
                    return connection.prepareStatement(sql, autoGeneratedKeys);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public MutiShardingPrepareStatement(MutiShardingConnection mutiShardingConnection, String sql, int[] columnIndexes) {
        super(mutiShardingConnection);
        this.sql = sql;
        try {
            cachedStatementsAndGet(sql, connection -> {
                try {
                    return connection.prepareStatement(sql, columnIndexes);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MutiShardingPrepareStatement(MutiShardingConnection mutiShardingConnection, String sql, String[] columnNames) {
        super(mutiShardingConnection);
        this.sql = sql;
        try {
            cachedStatementsAndGet(sql, connection -> {
                try {
                    return connection.prepareStatement(sql, columnNames);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    protected Statement cachedStatementsAndGet(String sql, Function<Connection, PreparedStatement> function) throws SQLException {
        if (isClosed()) throw new RuntimeException("statement is closed");
        String tableName = ((tableName = getParser().parserTableName(sql)) != null) ? tableName :
                getMutiShardingConnection().getDataSource().getDataSourceMap().keySet().iterator().next();
        if (!this.getCachedStatements().containsKey(tableName)) {
            Connection connection = cachedConnectionAndGet(getMutiShardingConnection(), tableName);
            PreparedStatement statement = function.apply(connection);
            if (null == trueConnection) {
                trueConnection = connection;
            }
            if (null == trueStatement)
                trueStatement = statement;
            getCachedStatements().put(tableName, statement);
        }
        return getCachedStatements().get(tableName);
    }

    @Override
    protected Statement cachedStatementsAndGet(String sql) throws SQLException {
        if (isClosed()) throw new RuntimeException("statement is closed");
        String tableName = ((tableName = getParser().parserTableName(sql)) != null) ? tableName :
                        getMutiShardingConnection().getDataSource().getDataSourceMap().keySet().iterator().next();
        if (!this.getCachedStatements().containsKey(tableName)) {
            Connection connection = cachedConnectionAndGet(getMutiShardingConnection(), tableName);
            PreparedStatement statement = connection.prepareStatement(sql, getResultSetType(), getResultSetConcurrency(), getResultSetHoldability());
            if (null == this.trueConnection)
                this.trueConnection = connection;
            if (null == this.trueStatement)
                this.trueStatement = statement;
            getCachedStatements().put(tableName, statement);
        }
        return getCachedStatements().get(tableName);
    }


    @Override
    public ResultSet executeQuery() throws SQLException {
        return getTrueStatement().executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return getTrueStatement().executeUpdate();
    }


    @Override
    public void clearParameters() throws SQLException {
        getTrueStatement().clearParameters();
    }


    @Override
    public boolean execute() throws SQLException {
        return getTrueStatement().execute();
    }

    @Override
    public void addBatch() throws SQLException {
        getTrueStatement().addBatch();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        getTrueStatement().addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        getTrueStatement().clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return getTrueStatement().executeBatch();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return getTrueStatement().getMetaData();
    }


}
