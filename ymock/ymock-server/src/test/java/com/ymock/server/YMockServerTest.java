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
import com.ymock.commons.YMockException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link YMockServer}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class YMockServerTest {

    /**
     * ID.
     */
    private static final String ID = "test";

    /**
     * Request.
     */
    private static final String REQUEST = "really works?";

    /**
     * Response.
     */
    private static final String RESPONSE = "works fine!";

    /**
     * Entire cycle.
     * @throws Exception If something goes wrong
     */
    @Test
    public void testEntireCycleThroughMocks() throws Exception {
        final SimpleProvider provider = new YMockServerTest.SimpleProvider();
        final Connector connector =
            new YMockServerTest.SimpleConnector(provider);
        final YMockServer server = new YMockServer(this.ID, provider);
        server.when(String.format("\\Q%s\\E", this.REQUEST), this.RESPONSE);
        new YMockServerTest.SimpleClient(connector).run();
    }

    /**
     * With custom matchers.
     * @throws Exception If something goes wrong
     */
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

    /**
     * With duplicate matching.
     * @throws Exception If something goes wrong
     */
    @Test(expected = IllegalStateException.class)
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

    /**
     * With error in response.
     * @throws Exception If something goes wrong
     */
    @Test(expected = IllegalStateException.class)
    public void testEntireCycleThroughMocksWithErrorInResponse()
        throws Exception {
        final SimpleProvider provider = new YMockServerTest.SimpleProvider();
        final Connector connector =
            new YMockServerTest.SimpleConnector(provider);
        new YMockServerTest.SimpleClient(connector).run();
    }

    /**
     * Simple client.
     */
    private static class SimpleClient {
        /**
         * Client.
         */
        private transient YMockClient client;
        /**
         * Public ctor.
         */
        public SimpleClient() {
            this.client = new YMockClient(YMockServerTest.ID);
        }
        /**
         * Public ctor.
         * @param connector The connector to use
         */
        public SimpleClient(final Connector connector) {
            this.client = new YMockClient(YMockServerTest.ID, connector);
        }
        /**
         * Run.
         */
        public void run() {
            String response;
            try {
                response = this.client.call(YMockServerTest.REQUEST);
            } catch (YMockException ex) {
                throw new IllegalStateException(ex);
            }
            MatcherAssert.assertThat(
                response,
                Matchers.equalTo(YMockServerTest.RESPONSE)
            );
        }
    }

    /**
     * Simple connector.
     */
    private static class SimpleConnector implements Connector {
        /**
         * Provider.
         */
        private transient SimpleProvider provider;
        /**
         * Public ctor.
         * @param prv The provider
         */
        public SimpleConnector(final SimpleProvider prv) {
            this.provider = prv;
        }
        /**
         * {@inheritDoc}
         * @checkstyle RedundantThrows (3 lines)
         */
        @Override
        public String call(final String request) throws YMockException {
            return this.provider.call(request);
        }
    }

    /**
     * Simple provider.
     */
    private static class SimpleProvider implements CallsProvider {
        /**
         * Catcher.
         */
        private transient Catcher catcher;
        /**
         * {@inheritDoc}
         */
        @Override
        public void register(final Catcher ctr) {
            this.catcher = ctr;
        }
        /**
         * Call.
         * @param request The request
         * @return The response
         * @throws YMockException If something goes wrong
         * @checkstyle RedundantThrows (3 lines)
         */
        public String call(final String request) throws YMockException {
            return this.catcher.call(request).process(request);
        }
    }

    /**
     * Simple matcher.
     */
    private static class SimpleMatcher implements Matcher {
        /**
         * Text.
         */
        private transient String text;
        /**
         * Match.
         */
        private transient boolean match;
        /**
         * Public ctor.
         * @param txt The text
         * @param mtch Matcher
         */
        public SimpleMatcher(final String txt, final boolean mtch) {
            this.text = txt;
            this.match = mtch;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean matches(final String request) {
            return this.match && request.equals(this.text);
        }
    }

    /**
     * Simple response.
     */
    private static class SimpleResponse implements Response {
        /**
         * The text.
         */
        private transient String text;
        /**
         * Public ctor.
         * @param txt The text
         */
        public SimpleResponse(final String txt) {
            this.text = txt;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String process(final String request) {
            return this.text;
        }
    }

    /**
     * Through live HTTP.
     * @throws Exception If something goes wrong
     */
    @org.junit.Ignore
    @Test
    public void testEntireCycleThroughLiveHttp() throws Exception {
        final YMockServer server = new YMockServer(this.ID);
        server.when(this.REQUEST, this.RESPONSE);
        new YMockServerTest.SimpleClient().run();
    }

}
