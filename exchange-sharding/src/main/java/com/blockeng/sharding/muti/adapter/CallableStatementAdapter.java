package com.blockeng.sharding.muti.adapter;

import com.blockeng.sharding.muti.MutiShardingConnection;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public abstract class CallableStatementAdapter extends  PrePareStatementAdapter implements CallableStatement {

    public CallableStatementAdapter(MutiShardingConnection mutiShardingConnection) {
        super(mutiShardingConnection);
    }

    public CallableStatementAdapter(MutiShardingConnection mutiShardingConnection, int resultSetType, int resultSetConcurrency) {
        super(mutiShardingConnection, resultSetType, resultSetConcurrency);
    }

    public CallableStatementAdapter(MutiShardingConnection mutiShardingConnection, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        super(mutiShardingConnection, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    private CallableStatement getTrueStatement0() throws SQLException {
        return (CallableStatement) getTrueStatement();
    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        getTrueStatement0().setNClob(parameterName,reader,length);
    }

    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        return getTrueStatement0().getNClob(parameterIndex);
    }

    @Override
    public NClob getNClob(String parameterName) throws SQLException {
        return getTrueStatement0().getNClob(parameterName);
    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        getTrueStatement0().setSQLXML(parameterName,xmlObject);
    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        return getTrueStatement0().getSQLXML(parameterIndex);
    }

    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        return getTrueStatement0().getSQLXML(parameterName);
    }

    @Override
    public String getNString(int parameterIndex) throws SQLException {
        return getTrueStatement0().getNString(parameterIndex);
    }

    @Override
    public String getNString(String parameterName) throws SQLException {
        return getTrueStatement0().getNString(parameterName);
    }

    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        return getTrueStatement0().getNCharacterStream(parameterIndex);
    }

    @Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        return getTrueStatement0().getNCharacterStream(parameterName);
    }

    @Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
        return getTrueStatement0().getCharacterStream(parameterIndex);
    }

    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
        return getTrueStatement0().getCharacterStream(parameterName);
    }

    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
        getTrueStatement0().setBlob(parameterName,x);
    }

    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
        getTrueStatement0().setClob(parameterName,x);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        getTrueStatement0().setAsciiStream(parameterName,x,length);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        getTrueStatement0().setBinaryStream(parameterName,x,length);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        getTrueStatement0().setCharacterStream(parameterName,reader,length);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        getTrueStatement0().setAsciiStream(parameterName,x);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        getTrueStatement0().setBinaryStream(parameterName,x);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        getTrueStatement0().setCharacterStream(parameterName,reader);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        getTrueStatement0().setNCharacterStream(parameterName,value);
    }

    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {
        getTrueStatement0().setClob(parameterName,reader);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        getTrueStatement0().setBlob(parameterName,inputStream);
    }

    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
        getTrueStatement0().setNClob(parameterName,reader);
    }

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        return getTrueStatement0().getObject(parameterIndex,type);
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        return getTrueStatement0().getObject(parameterName,type);
    }

    @Override
    public void setObject(String parameterName, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        getTrueStatement0().setObject(parameterName,x,targetSqlType,scaleOrLength);
    }

    @Override
    public void setObject(String parameterName, Object x, SQLType targetSqlType) throws SQLException {
        getTrueStatement0().setObject(parameterName,x,targetSqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, SQLType sqlType) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterIndex,sqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, SQLType sqlType, int scale) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterIndex,sqlType,scale);
    }

    @Override
    public void registerOutParameter(int parameterIndex, SQLType sqlType, String typeName) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterIndex,sqlType,typeName);
    }

    @Override
    public void registerOutParameter(String parameterName, SQLType sqlType) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterName,sqlType);
    }

    @Override
    public void registerOutParameter(String parameterName, SQLType sqlType, int scale) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterName,sqlType,scale);
    }

    @Override
    public void registerOutParameter(String parameterName, SQLType sqlType, String typeName) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterName,sqlType,typeName);
    }

    @Override
    public byte getByte(String parameterName) throws SQLException {
        return getTrueStatement0().getByte(parameterName);
    }

    @Override
    public short getShort(String parameterName) throws SQLException {
        return getTrueStatement0().getShort(parameterName);
    }

    @Override
    public int getInt(String parameterName) throws SQLException {
        return getTrueStatement0().getInt(parameterName);
    }

    @Override
    public long getLong(String parameterName) throws SQLException {
        return getTrueStatement0().getLong(parameterName);
    }

    @Override
    public float getFloat(String parameterName) throws SQLException {
        return getTrueStatement0().getFloat(parameterName);
    }

    @Override
    public double getDouble(String parameterName) throws SQLException {
        return getTrueStatement0().getDouble(parameterName);
    }

    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        return getTrueStatement0().getBytes(parameterName);
    }

    @Override
    public Date getDate(String parameterName) throws SQLException {
        return getTrueStatement0().getDate(parameterName);
    }

    @Override
    public Time getTime(String parameterName) throws SQLException {
        return getTrueStatement0().getTime(parameterName);
    }

    @Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return getTrueStatement0().getTimestamp(parameterName);
    }

    @Override
    public Object getObject(String parameterName) throws SQLException {
        return getTrueStatement0().getObject(parameterName);
    }

    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return getTrueStatement0().getBigDecimal(parameterName);
    }

    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return getTrueStatement0().getObject(parameterName,map);
    }

    @Override
    public Ref getRef(String parameterName) throws SQLException {
        return getTrueStatement0().getRef(parameterName);
    }

    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        return getTrueStatement0().getBlob(parameterName);
    }

    @Override
    public Clob getClob(String parameterName) throws SQLException {
        return getTrueStatement0().getClob(parameterName);
    }

    @Override
    public Array getArray(String parameterName) throws SQLException {
        return getTrueStatement0().getArray(parameterName);
    }

    @Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return getTrueStatement0().getDate(parameterName,cal);
    }

    @Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return getTrueStatement0().getTime(parameterName,cal);
    }

    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        return getTrueStatement0().getTimestamp(parameterName,cal);
    }

    @Override
    public URL getURL(String parameterName) throws SQLException {
        return getURL(parameterName);
    }

    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        return getTrueStatement0().getRowId(parameterIndex);
    }

    @Override
    public RowId getRowId(String parameterName) throws SQLException {
        return getTrueStatement0().getRowId(parameterName);
    }

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
        getTrueStatement0().setRowId(parameterName,x);
    }

    @Override
    public void setNString(String parameterName, String value) throws SQLException {
        getTrueStatement0().setNString(parameterName,value);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        getTrueStatement0().setNCharacterStream(parameterName, value, length);
    }

    @Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
        getTrueStatement0().setNClob(parameterName, value);
    }

    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        getTrueStatement0().setClob(parameterName, reader, length);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        getTrueStatement0().setBlob(parameterName, inputStream, length);
    }


    @Override
    public URL getURL(int parameterIndex) throws SQLException {
        return getTrueStatement0().getURL(parameterIndex);
    }

    @Override
    public void setURL(String parameterName, URL val) throws SQLException {
        getTrueStatement0().setURL(parameterName,val);
    }

    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        getTrueStatement0().setNull(parameterName,sqlType);
    }

    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        getTrueStatement0().setBoolean(parameterName,x);
    }

    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        getTrueStatement0().setByte(parameterName,x);
    }

    @Override
    public void setShort(String parameterName, short x) throws SQLException {
        getTrueStatement0().setShort(parameterName, x);
    }

    @Override
    public void setInt(String parameterName, int x) throws SQLException {
        getTrueStatement0().setInt(parameterName, x);
    }

    @Override
    public void setLong(String parameterName, long x) throws SQLException {
        getTrueStatement0().setLong(parameterName, x);
    }

    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
        getTrueStatement0().setFloat(parameterName, x);
    }

    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
        getTrueStatement0().setDouble(parameterName, x);
    }

    @Override
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        getTrueStatement0().setBigDecimal(parameterName, x);
    }

    @Override
    public void setString(String parameterName, String x) throws SQLException {
        getTrueStatement0().setString(parameterName, x);
    }

    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        getTrueStatement0().setBytes(parameterName, x);
    }

    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
        getTrueStatement0().setDate(parameterName, x);
    }

    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
        getTrueStatement0().setTime(parameterName, x);
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        getTrueStatement0().setTimestamp(parameterName, x);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        getTrueStatement0().setAsciiStream(parameterName, x, length);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        getTrueStatement0().setBinaryStream(parameterName, x, length);
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        getTrueStatement0().setObject(parameterName, x, targetSqlType, scale);
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        getTrueStatement0().setObject(parameterName, x, targetSqlType);
    }

    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
        getTrueStatement0().setObject(parameterName, x);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        getTrueStatement0().setCharacterStream(parameterName, reader, length);
    }

    @Override
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        getTrueStatement0().setDate(parameterName, x, cal);
    }

    @Override
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        getTrueStatement0().setTime(parameterName, x, cal);
    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        getTrueStatement0().setTimestamp(parameterName, x, cal);
    }

    @Override
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        getTrueStatement0().setNull(parameterName, sqlType, typeName);
    }

    @Override
    public String getString(String parameterName) throws SQLException {
        return getTrueStatement0().getString(parameterName);
    }

    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        return getTrueStatement0().getBoolean(parameterName);
    }
    @Override
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterIndex,sqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterIndex,sqlType,scale);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return getTrueStatement0().wasNull();
    }

    @Override
    public String getString(int parameterIndex) throws SQLException {
        return getTrueStatement0().getString(parameterIndex);
    }

    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        return getTrueStatement0().getBoolean(parameterIndex);
    }

    @Override
    public byte getByte(int parameterIndex) throws SQLException {
        return getTrueStatement0().getByte(parameterIndex);
    }

    @Override
    public short getShort(int parameterIndex) throws SQLException {
        return getTrueStatement0().getShort(parameterIndex);
    }

    @Override
    public int getInt(int parameterIndex) throws SQLException {
        return getTrueStatement0().getInt(parameterIndex);
    }

    @Override
    public long getLong(int parameterIndex) throws SQLException {
        return getTrueStatement0().getLong(parameterIndex);
    }

    @Override
    public float getFloat(int parameterIndex) throws SQLException {
        return getTrueStatement0().getFloat(parameterIndex);
    }

    @Override
    public double getDouble(int parameterIndex) throws SQLException {
        return getTrueStatement0().getDouble(parameterIndex);
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return getTrueStatement0().getBigDecimal(parameterIndex,scale);
    }

    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        return getTrueStatement0().getBytes(parameterIndex);
    }

    @Override
    public Date getDate(int parameterIndex) throws SQLException {
        return getTrueStatement0().getDate(parameterIndex);
    }

    @Override
    public Time getTime(int parameterIndex) throws SQLException {
        return getTrueStatement0().getTime(parameterIndex);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return getTrueStatement0().getTimestamp(parameterIndex);
    }

    @Override
    public Object getObject(int parameterIndex) throws SQLException {
        return getObject(parameterIndex);
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return getTrueStatement0().getBigDecimal(parameterIndex);
    }

    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        return getTrueStatement0().getObject(parameterIndex,map);
    }

    @Override
    public Ref getRef(int parameterIndex) throws SQLException {
        return getTrueStatement0().getRef(parameterIndex);
    }

    @Override
    public Blob getBlob(int parameterIndex) throws SQLException {
        return getTrueStatement0().getBlob(parameterIndex);
    }

    @Override
    public Clob getClob(int parameterIndex) throws SQLException {
        return getTrueStatement0().getClob(parameterIndex);
    }

    @Override
    public Array getArray(int parameterIndex) throws SQLException {
        return getTrueStatement0().getArray(parameterIndex);
    }

    @Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return getTrueStatement0().getDate(parameterIndex,cal);
    }

    @Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return getTrueStatement0().getTime(parameterIndex,cal);
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return getTrueStatement0().getTimestamp(parameterIndex,cal);
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterIndex,sqlType,typeName);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterName,sqlType);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterName,sqlType,scale);
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        getTrueStatement0().registerOutParameter(parameterName,sqlType,typeName);
    }
    @Override
    public String enquoteLiteral(String val) throws SQLException {
        return getTrueStatement0().enquoteLiteral(val);
    }

    @Override
    public String enquoteIdentifier(String identifier, boolean alwaysQuote) throws SQLException {
        return getTrueStatement0().enquoteIdentifier(identifier,alwaysQuote);
    }

    @Override
    public boolean isSimpleIdentifier(String identifier) throws SQLException {
        return getTrueStatement0().isSimpleIdentifier(identifier);
    }

    @Override
    public String enquoteNCharLiteral(String val) throws SQLException {
        return getTrueStatement0().enquoteNCharLiteral(val);
    }
    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        getTrueStatement0().setObject(parameterIndex,x,targetSqlType,scaleOrLength);
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
        getTrueStatement0().setObject(parameterIndex,x,targetSqlType);
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        return getTrueStatement().getLargeUpdateCount();
    }

    @Override
    public void setLargeMaxRows(long max) throws SQLException {
        getTrueStatement0().setLargeMaxRows(max);
    }

    @Override
    public long getLargeMaxRows() throws SQLException {
        return getTrueStatement0().getLargeMaxRows();
    }

}
