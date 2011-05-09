/**
 * @version $Id$
 */
package com.jdbcmock;

import org.junit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;
import java.util.HashMap;

public class JMConnectionTest {

    @Test
    public void testCallsAllMethods() throws Exception {
        Driver driver = new JMDriver();
        Connection ctn = driver.connect("url:something", new Properties());
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
        ctn.createStruct("test", new Object[] {});
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
        ctn.isValid(10);
        ctn.nativeSQL("test");
        ctn.prepareCall("SELECT * FROM user");
        ctn.prepareCall("SELECT * FROM user", 1, 1);
        ctn.prepareCall("SELECT * FROM user", 1, 1, 1);
        ctn.prepareStatement("SELECT * FROM user");
        ctn.prepareStatement("SELECT * FROM user", 1);
        ctn.prepareStatement("SELECT * FROM user", new int[] {});
        ctn.prepareStatement("SELECT * FROM user", 1, 1);
        ctn.prepareStatement("SELECT * FROM user", 1, 1, 1);
        ctn.releaseSavepoint(null);
        ctn.rollback();
        ctn.rollback(null);
        ctn.setAutoCommit(true);
        ctn.setCatalog("test");
        ctn.setClientInfo(new Properties());
        ctn.setClientInfo("key", "value");
        ctn.setSavepoint();
        ctn.setSavepoint("test");
        ctn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        ctn.setTypeMap(new HashMap<String, Class<?>>());
    }

}
