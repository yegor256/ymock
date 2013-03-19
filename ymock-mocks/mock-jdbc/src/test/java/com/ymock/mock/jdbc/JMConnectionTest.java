/**
 * Copyright (c) 2011-2012, yMock.com
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

import java.sql.Connection;
import java.sql.Driver;
import java.util.HashMap;
import java.util.Properties;
import org.junit.Test;

/**
 * Test case of {@link JMConnection}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class JMConnectionTest {

    /**
     * Test it.
     * @throws Exception If some problem inside
     * @checkstyle ExecutableStatementCount (60 lines)
     */
    @Test
    public void testCallsAllMethods() throws Exception {
        final Driver driver = new JMDriver();
        final Connection ctn = driver.connect(
            "url:something",
            new Properties()
        );
        try {
            ctn.isWrapperFor(String.class);
            ctn.unwrap(java.io.InputStream.class);
            ctn.clearWarnings();
            ctn.close();
            ctn.commit();
            ctn.createArrayOf("test", new Object[] {});
            ctn.createBlob();
            ctn.createClob();
            ctn.createNClob();
            ctn.createSQLXML();
            ctn.createStatement();
            ctn.createStatement(1, 1);
            ctn.createStatement(1, 1, 1);
            ctn.createStruct("test struct", new Object[] {});
            ctn.getAutoCommit();
            ctn.getCatalog();
            ctn.getClientInfo();
            ctn.getHoldability();
            ctn.getMetaData();
            ctn.getTransactionIsolation();
            ctn.getTypeMap();
            ctn.getWarnings();
            ctn.isClosed();
            ctn.isReadOnly();
            ctn.isValid(1);
            ctn.nativeSQL("select name from project");
            ctn.prepareCall("SELECT a FROM user");
            ctn.prepareCall("SELECT b FROM user", 1, 1);
            ctn.prepareCall("SELECT c FROM user", 1, 1, 1);
            ctn.prepareStatement("SELECT d FROM user");
            ctn.prepareStatement("SELECT e FROM user", 1);
            ctn.prepareStatement("SELECT f FROM user", new int[] {});
            ctn.prepareStatement("SELECT g FROM user", 1, 1);
            ctn.prepareStatement("SELECT h FROM user", 1, 1, 1);
            ctn.releaseSavepoint(null);
            ctn.rollback();
            ctn.rollback(null);
            ctn.setAutoCommit(true);
            ctn.setCatalog("test catalog");
            ctn.setClientInfo(new Properties());
            ctn.setClientInfo("key", "value");
            ctn.setSavepoint();
            ctn.setSavepoint("test savepoint");
            ctn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            ctn.setTypeMap(new HashMap<String, Class<?>>());
        } finally {
            ctn.close();
        }
    }

}
