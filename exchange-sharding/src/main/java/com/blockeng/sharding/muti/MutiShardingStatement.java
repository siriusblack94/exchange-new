package com.blockeng.sharding.muti;


import com.blockeng.sharding.muti.adapter.StatementAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Slf4j
public class MutiShardingStatement extends StatementAdapter {

    private MutiShardingConnection mutiShardingConnection;

    private Map<String, DataSource> dataSourceMap;

    private SQLParser parser = new SQLParser();

    private Map<String, Statement> cachedStatements;

    private List<Object[]> batchIndex = new LinkedList<>();

    private Map<Statement, AtomicInteger> counts = new ConcurrentHashMap<>();

    private volatile Statement trueStatement;

    private int resultSetType;

    private int resultSetConcurrency;

    private int resultSetHoldability;

    public MutiShardingStatement(MutiShardingConnection mutiShardingConnection) {
        this(mutiShardingConnection, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }

    public MutiShardingStatement(MutiShardingConnection mutiShardingConnection, int resultSetType, int resultSetConcurrency) {
        this(mutiShardingConnection, resultSetType, resultSetConcurrency, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }

    public MutiShardingStatement(MutiShardingConnection mutiShardingConnection, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.resultSetHoldability = resultSetHoldability;
        this.mutiShardingConnection = mutiShardingConnection;
        this.cachedStatements = new ConcurrentHashMap();
        this.dataSourceMap = mutiShardingConnection.getDataSource().getDataSourceMap();
    }

    @Override
    protected Map<String, Statement> getStatements() {
        return this.cachedStatements;
    }

    protected Connection cachedConnectionAndGet(MutiShardingConnection mutiShardingConnection, String tableName) throws SQLException {
        if (!mutiShardingConnection.getCachedConnections().containsKey(tableName)) {

            Assert.isTrue(getDataSourceMap().containsKey(tableName), String.format("Miss table %s", tableName));

            Connection connection = null;
            connection = mutiShardingConnection.isHasPassword() ?
                    getDataSourceMap().get(tableName).getConnection(mutiShardingConnection.getUsername(), mutiShardingConnection.getPassword()) :
                    getDataSourceMap().get(tableName).getConnection();

            mutiShardingConnection.getCachedConnections().put(tableName, connection);
            mutiShardingConnection.setTrueConnection(connection);
            mutiShardingConnection.setTrueConnectionTableName(tableName);
            connection.setAutoCommit(mutiShardingConnection.getAutoCommit());
            return connection;
        } else {
            Connection connection = mutiShardingConnection.getCachedConnections().get(tableName);
            mutiShardingConnection.setTrueConnection(connection);
            mutiShardingConnection.setTrueConnectionTableName(tableName);
            connection.setAutoCommit(mutiShardingConnection.getAutoCommit());
            return connection;
        }
    }

    @Override
    protected Statement getTrueStatement() throws SQLException {
        return null == getTrueStatement() ? cachedStatementsAndGet("SELECT 1") : getTrueStatement();
    }

    @Override
    protected Statement cachedStatementsAndGet(String sql) throws SQLException {
        if (isClosed()) throw new RuntimeException("statement is closed");
        String tableName = ((tableName = getParser().parserTableName(sql)) != null) ? tableName :
                getMutiShardingConnection().getDataSource().getDataSourceMap().keySet().iterator().next();
        if (!getCachedStatements().containsKey(tableName)) {
            Connection connection = cachedConnectionAndGet(getMutiShardingConnection(), tableName);
            Statement statement = connection.createStatement(getResultSetType(), getResultSetConcurrency(), getResultSetHoldability());
            getCachedStatements().put(tableName, statement);
        }
        Statement st = getCachedStatements().get(tableName);
        setTrueStatement(st);
        return st;
    }


    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return this.cachedStatementsAndGet(sql).executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return this.cachedStatementsAndGet(sql).executeUpdate(sql);
    }


    @Override
    public boolean execute(String sql) throws SQLException {
        return cachedStatementsAndGet(sql).execute(sql);
    }


    @Override
    public void addBatch(String sql) throws SQLException {
        Statement statement = this.cachedStatementsAndGet(sql);
        statement.addBatch(sql);
        if (!counts.containsKey(statement)) {
            counts.put(statement, new AtomicInteger(0));
        }
        batchIndex.add(new Object[]{statement, String.valueOf(counts.get(statement).getAndIncrement())});
    }

    @Override
    public void clearBatch() throws SQLException {
        for (Map.Entry entry : this.getStatements().entrySet()) {
            ((Statement) entry.getValue()).clearBatch();
        }
        batchIndex.clear();
        counts.clear();
    }


    @Override
    public int[] executeBatch() throws SQLException {
        Map<Statement, int[]> results = new HashMap<>();
        for (Map.Entry entry : this.getStatements().entrySet()) {
            results.put((Statement) entry.getValue(), ((Statement) entry.getValue()).executeBatch());
        }
        int[] result = batchIndex.stream().mapToInt(objects -> {
            Object[] arr = (Object[]) objects;
            return results.get(arr[0])[Integer.valueOf(arr[1].toString())];
        }).toArray();
        batchIndex.clear();
        counts.clear();
        return result;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getMutiShardingConnection();
    }


    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return cachedStatementsAndGet(sql).executeUpdate(sql, autoGeneratedKeys);
    }


    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return cachedStatementsAndGet(sql).executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return cachedStatementsAndGet(sql).executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return cachedStatementsAndGet(sql).execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return cachedStatementsAndGet(sql).execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return cachedStatementsAndGet(sql).execute(sql, columnNames);
    }


}
