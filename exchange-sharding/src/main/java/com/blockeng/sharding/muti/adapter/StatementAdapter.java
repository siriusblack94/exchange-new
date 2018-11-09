package com.blockeng.sharding.muti.adapter;

import com.blockeng.sharding.muti.MutiShardingConnection;
import lombok.Data;

import java.sql.*;
import java.util.Map;

@Data
public abstract class StatementAdapter implements Statement {

    private boolean closed;

    protected abstract Map<String, Statement> getStatements() ;

    protected abstract Statement cachedStatementsAndGet(String sql) throws SQLException;


    protected abstract Connection cachedConnectionAndGet(MutiShardingConnection mutiShardingConnection, String tableName) throws SQLException ;


    protected abstract Statement getTrueStatement() throws SQLException;

    @Override
    public void close() throws SQLException {

        this.getStatements().values().forEach(
                statement -> {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        );

        setClosed(true);
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return getTrueStatement().getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).setMaxFieldSize(max);
        }

    }

    @Override
    public int getMaxRows() throws SQLException {
        return getTrueStatement().getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).setMaxRows(max);
        }
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).setEscapeProcessing(enable);
        }
    }



    @Override
    public int getQueryTimeout() throws SQLException {
        return getTrueStatement().getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).setQueryTimeout(seconds);
        }
    }

    @Override
    public void cancel() throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).cancel();
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return getTrueStatement().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).clearWarnings();
        }
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).setCursorName(name);
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return getTrueStatement().getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        int count = 0;
        for (Map.Entry entry : this.getStatements().entrySet()) {
            count += ((Statement) entry.getValue()).getUpdateCount();
        }
        return count;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return getTrueStatement().getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).setFetchDirection(direction);
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return  getTrueStatement().getFetchDirection();
    }


    @Override
    public int getResultSetConcurrency() throws SQLException {
        return getTrueStatement().getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return getTrueStatement().getResultSetType();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return getTrueStatement().getMoreResults();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return getTrueStatement().getGeneratedKeys();
    }
    @Override
    public int getResultSetHoldability() throws SQLException {
        return getTrueStatement().getResultSetHoldability();
    }


    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return isClosed();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getTrueStatement().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getTrueStatement().isWrapperFor(iface);
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).setFetchSize(rows);
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        return getTrueStatement().getFetchSize();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).setPoolable(poolable);
        }
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return getTrueStatement().isPoolable();
    }
}
