/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
package com.ymock;

import com.ymock.server.Response;
import com.ymock.server.YMockServer;
import com.ymock.server.matchers.RegexMatcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers.*;
import org.junit.Test;

public final class ClientTest {

    @Test
    public void testEntireCycleThroughMocks() throws Exception {
        final String text = "some text";
        final YMockServer server = new YMockServer("calculator");
        server.when(
            new RegexMatcher("\\Q" + text + "\\E"),
            new LineLengthResponse(text)
        );
        final Integer length = new Client().calculate(text);
        MatcherAssert.assertThat(length, Matchers.equalTo(text.length()));
    }

    private static class LineLengthResponse implements Response {
        private String expected;
        public LineLengthResponse(final String rqt) {
            this.expected = rqt;
        }
        @Override
        public String process(final String request) {
            MatcherAssert.assertThat(request, Matchers.equalTo(this.expected));
            return Integer.toString(request.length());
        }
    }

}
