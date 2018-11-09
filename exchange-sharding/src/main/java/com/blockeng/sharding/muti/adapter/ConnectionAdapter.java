package com.blockeng.sharding.muti.adapter;


import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public abstract class ConnectionAdapter implements Connection {

    boolean closed;

    protected abstract Connection getAnyTrueConnnection() throws SQLException;

    protected abstract void eachConnections(Consumer<Connection> consumer);

    protected abstract Connection getAnyMateConnection() throws SQLException;


    @Override
    public boolean getAutoCommit() throws SQLException {
        return getAnyTrueConnnection().getAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {

        eachConnections(connection -> {
            try {
                connection.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void commit() throws SQLException {
        eachConnections(connection -> {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void rollback() throws SQLException {
        eachConnections(connection -> {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void close() throws SQLException {
        eachConnections(connection -> {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        closed = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {

        Connection connection = getAnyMateConnection();

        try {
            return connection.getMetaData();
        } finally {
            connection.close();
        }
    }


    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.setReadOnly(readOnly);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return getAnyTrueConnnection().isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.setCatalog(catalog);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String getCatalog() throws SQLException {
        return getAnyTrueConnnection().getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.setTransactionIsolation(level);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return getAnyTrueConnnection().getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return getAnyTrueConnnection().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        eachConnections(connection -> {
            try {
                connection.clearWarnings();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Clob createClob() throws SQLException {
        return getAnyTrueConnnection().createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return getAnyTrueConnnection().createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return getAnyTrueConnnection().createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return getAnyTrueConnnection().createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return getAnyTrueConnnection().isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        eachConnections(connection -> {
            try {
                connection.setClientInfo(name, value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        eachConnections(connection -> {
            try {
                connection.setClientInfo(properties);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return getAnyTrueConnnection().getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return getAnyTrueConnnection().getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getAnyTrueConnnection().createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getAnyTrueConnnection().createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.setSchema(schema);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String getSchema() throws SQLException {
        return getAnyTrueConnnection().getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.abort(executor);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.setNetworkTimeout(executor, milliseconds);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return getAnyTrueConnnection().getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getAnyTrueConnnection().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getAnyTrueConnnection().isWrapperFor(iface);
    }

    @Override
    public int getHoldability() throws SQLException {
        return getAnyTrueConnnection().getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return getAnyTrueConnnection().setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return getAnyTrueConnnection().setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.rollback(savepoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.releaseSavepoint(savepoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getAnyTrueConnnection().getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.setTypeMap(map);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        eachConnections(connection -> {
            try {
                connection.setHoldability(holdability);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
