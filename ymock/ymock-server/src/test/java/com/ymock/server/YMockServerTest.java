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
package com.ymock.server;

import com.ymock.client.Connector;
import com.ymock.client.YMockClient;
import com.ymock.client.YMockException;
import org.junit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class YMockServerTest {

    private static final String ID = "test";

    private static final String REQUEST = "really works?";

    private static final String RESPONSE = "works fine!";

    @Test
    public void testEntireCycleThroughMocks() throws Exception {
        // mocks
        final SimpleProvider provider = new YMockServerTest.SimpleProvider();
        final Connector connector =
            new YMockServerTest.SimpleConnector(provider);
        // start listening server
        final YMockServer server = new YMockServer(this.ID, provider);
        // add some listeners
        server.when("\\Q" + this.REQUEST + "\\E", this.RESPONSE);
        // run some clients
        new YMockServerTest.SimpleClient(connector).run();
        // verify that calls were made
        // server.verify(this.REQUEST);
    }

    @Test
    public void testEntireCycleWithCustomMatchersAndResponses()
        throws Exception {
        final SimpleProvider provider = new YMockServerTest.SimpleProvider();
        final Connector connector =
            new YMockServerTest.SimpleConnector(provider);
        final YMockServer server = new YMockServer(this.ID, provider);
        server.when(
            new SimpleMatcher(this.REQUEST, false),
            new SimpleResponse(this.RESPONSE)
        );
        server.when(
            new SimpleMatcher(this.REQUEST, true),
            new SimpleResponse(this.RESPONSE)
        );
        new YMockServerTest.SimpleClient(connector).run();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntireCycleWithDuplicateMatching()
        throws Exception {
        final SimpleProvider provider = new YMockServerTest.SimpleProvider();
        final Connector connector =
            new YMockServerTest.SimpleConnector(provider);
        final YMockServer server = new YMockServer(this.ID, provider);
        server.when(
            new SimpleMatcher(this.REQUEST, true),
            new SimpleResponse(this.RESPONSE)
        );
        server.when(
            new SimpleMatcher(this.REQUEST, true),
            new SimpleResponse(this.RESPONSE)
        );
        new YMockServerTest.SimpleClient(connector).run();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntireCycleThroughMocksWithErrorInResponse()
        throws Exception {
        final SimpleProvider provider = new YMockServerTest.SimpleProvider();
        final Connector connector =
            new YMockServerTest.SimpleConnector(provider);
        final YMockServer server = new YMockServer(this.ID, provider);
        new YMockServerTest.SimpleClient(connector).run();
    }

    private static class SimpleClient {
        private YMockClient client;
        public SimpleClient() {
            this.client = new YMockClient(YMockServerTest.ID);
        }
        public SimpleClient(final Connector connector) {
            this.client = new YMockClient(YMockServerTest.ID, connector);
        }
        public void run() {
            String response;
            try {
                response = this.client.call(YMockServerTest.REQUEST);
            } catch (YMockException ex) {
                throw new IllegalStateException(ex);
            }
            assertThat(response, is(equalTo(YMockServerTest.RESPONSE)));
        }
    }

    private static class SimpleConnector implements Connector {
        private SimpleProvider provider;
        public SimpleConnector(final SimpleProvider pvr) {
            this.provider = pvr;
        }
        @Override
        public String call(final String request) {
            return this.provider.call(request);
        }
    }

    private static class SimpleProvider implements CallsProvider {
        private Catcher catcher;
        @Override
        public void register(final Catcher ctr) {
            this.catcher = ctr;
        }
        public String call(final String request) {
            final Response response = this.catcher.call(request);
            if (!response.isSuccessful()) {
                throw new UnsupportedOperationException(response.getText());
            }
            return response.getText();
        }
    }

    private static class SimpleMatcher implements Matcher {
        private String text;
        private boolean match;
        public SimpleMatcher(final String txt, final boolean mtch) {
            this.text = txt;
            this.match = mtch;
        }
        @Override
        public boolean matches(final String request) {
            return this.match && request.equals(this.text);
        }
    }

    private static class SimpleResponse implements Response {
        private String text;
        public SimpleResponse(final String txt) {
            this.text = txt;
        }
        @Override
        public String getText() {
            return this.text;
        }
        @Override
        public boolean isSuccessful() {
            return true;
        }
    }

    @Ignore
    @Test
    public void testEntireCycleThroughLiveHttp() throws Exception {
        // start listening server
        final YMockServer server = new YMockServer(this.ID);
        // add some listeners
        server.when(this.REQUEST, this.RESPONSE);
        // run some clients
        new YMockServerTest.SimpleClient().run();
    }

}
