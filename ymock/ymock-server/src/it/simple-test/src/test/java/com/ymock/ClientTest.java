/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
package com.ymock;

import com.ymock.server.Response;
import com.ymock.server.YMockServer;
import com.ymock.server.matchers.RegexMatcher;
import org.junit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public final class ClientTest {

    @Test
    public void testEntireCycleThroughMocks() throws Exception {
        final YMockServer server = new YMockServer("calculator");
        server.when(new RegexMatcher(".*"), new LineLengthResponse());
        final String text = "some text";
        final Integer length = new Client().calculate(text);
        assertThat(length, equalTo(text.length()));
    }

    private static class LineLengthResponse implements Response {
        @Override
        public String process(final String request) {
            return Integer.toString(request.length());
        }
    }

}
