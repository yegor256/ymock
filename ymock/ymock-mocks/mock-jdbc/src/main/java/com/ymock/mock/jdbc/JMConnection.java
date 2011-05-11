/**
 * Copyright (c) 2011, yMock.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the yMock.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ymock.mock.jdbc;

// JDBC API 3.0
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;

// supplementary classes
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// mocking library from org.mockito:mockito-all
import org.mockito.Mockito;

/**
 * Jdbc Mock Connection.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/sql/Connection.html">java.sql.Connection</a>
 */
@SuppressWarnings("PMD.TooManyMethods")
final class JMConnection implements Connection {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T unwrap(final Class<T> iface) {
        return Mockito.mock(iface);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearWarnings() {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Array createArrayOf(final String typeName,
        final Object[] elements) {
        return Mockito.mock(Array.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Blob createBlob() {
        return Mockito.mock(Blob.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clob createClob() {
        return Mockito.mock(Clob.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NClob createNClob() {
        return Mockito.mock(NClob.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLXML createSQLXML() {
        return Mockito.mock(SQLXML.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement createStatement() {
        return Mockito.mock(Statement.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement createStatement(final int rst, final int rsc) {
        return this.createStatement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement createStatement(final int rst,
        final int rsc, final int rsh) {
        return this.createStatement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Struct createStruct(final String typeName,
        final Object[] attributes) {
        return Mockito.mock(Struct.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.BooleanGetMethodName")
    public boolean getAutoCommit() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCatalog() {
        return "mock";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties getClientInfo() {
        return new Properties();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClientInfo(final String name) {
        return "mock-info";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHoldability() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseMetaData getMetaData() {
        return Mockito.mock(DatabaseMetaData.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTransactionIsolation() {
        return this.TRANSACTION_READ_COMMITTED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Class<?>> getTypeMap() {
        return new HashMap<String, Class<?>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLWarning getWarnings() {
        return Mockito.mock(SQLWarning.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isClosed() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReadOnly() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(final int timeout) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nativeSQL(final String sql) {
        return sql;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CallableStatement prepareCall(final String sql) {
        return Mockito.mock(CallableStatement.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CallableStatement prepareCall(final String sql,
        final int type, final int concurrency) {
        return Mockito.mock(CallableStatement.class);
    }

    /**
     * {@inheritDoc}
     * @checkstyle ParameterNumber (5 lines)
     */
    @Override
    public CallableStatement prepareCall(final String sql,
        final int type, final int concurrency,
        final int holdability) {
        return Mockito.mock(CallableStatement.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(final String sql) {
        return Mockito.mock(PreparedStatement.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
        final int keys) {
        return Mockito.mock(PreparedStatement.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
        final int[] indexes) {
        return Mockito.mock(PreparedStatement.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
        final int type, final int concurrency) {
        return Mockito.mock(PreparedStatement.class);
    }

    /**
     * {@inheritDoc}
     * @checkstyle ParameterNumber (5 lines)
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
        final int type, final int concurrency,
        final int holdability) {
        return Mockito.mock(PreparedStatement.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
        final String[] names) {
        return Mockito.mock(PreparedStatement.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void releaseSavepoint(final Savepoint savepoint) {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback(final Savepoint savepoint) {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoCommit(final boolean autoCommit) {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCatalog(final String catalog) {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClientInfo(final Properties properties) {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClientInfo(final String name, final String value) {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHoldability(final int holdability) {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setReadOnly(final boolean readOnly) {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Savepoint setSavepoint() {
        return Mockito.mock(Savepoint.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Savepoint setSavepoint(final String name) {
        return Mockito.mock(Savepoint.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTransactionIsolation(final int level) {
        // intentionally empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTypeMap(final Map<String, Class<?>> map) {
        // intentionally empty
    }

}
