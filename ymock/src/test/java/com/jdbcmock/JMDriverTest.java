/**
 * @version $Id$
 */
package com.ymock;

import org.junit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.sql.Driver;
import java.util.Properties;

public class JMDriverTest {

    @Test
    public void testSampleOperations() throws Exception {
        Driver driver = new JMDriver();
        assertThat(driver.acceptsURL("url:something"), is(true));
        assertThat(
            driver.connect("url:something", new Properties()),
            is(not(nullValue()))
        );
        assertThat(driver.getMajorVersion(), is(not(nullValue())));
        assertThat(driver.getMinorVersion(), is(not(nullValue())));
        assertThat(driver.jdbcCompliant(), is(true));
    }

}
