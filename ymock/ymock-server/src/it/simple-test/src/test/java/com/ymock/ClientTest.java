/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
package com.ymock.server;

import com.ymock.server.YMockServer;
import org.junit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public final class ClientTest {

    @Test
    public void testEntireCycleThroughMocks() throws Exception {
        final YMockServer server = new YMockServer("test", provider);
        server.when(".*", new LineLengthResponse());
        // run some clients
        new YMockServerTest.SimpleClient(connector).run();
        // verify that calls were made
        // server.verify(this.REQUEST);
    }

    private static class LineLengthResponse implements Response {
        @Override
        public String getText() {
        }
    }

}
