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
import org.junit.Test;

/**
 * Test case for {@link YMockServer}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class YMockServerTest {

    /**
     * Request.
     */
    public static final String REQUEST = "really works?";

    /**
     * Response.
     */
    public static final String RESPONSE = "works fine!";

    /**
     * Entire cycle.
     * @throws Exception If something goes wrong
     */
    @Test
    public void testEntireCycleThroughMocks() throws Exception {
        final SimpleProvider provider = new SimpleProvider();
        final Connector connector = new SimpleConnector(provider);
        final YMockServer server = new YMockServer(SimpleClient.ID, provider);
        server.when(String.format("\\Q%s\\E", this.REQUEST), this.RESPONSE);
        new SimpleClient(connector).run();
    }

    /**
     * With custom matchers.
     * @throws Exception If something goes wrong
     */
    @Test
    public void testEntireCycleWithCustomMatchersAndResponses()
        throws Exception {
        final SimpleProvider provider = new SimpleProvider();
        final Connector connector = new SimpleConnector(provider);
        final YMockServer server = new YMockServer(SimpleClient.ID, provider);
        server.when(
            new SimpleMatcher(this.REQUEST, false),
            new SimpleResponse(this.RESPONSE)
        );
        server.when(
            new SimpleMatcher(this.REQUEST, true),
            new SimpleResponse(this.RESPONSE)
        );
        new SimpleClient(connector).run();
    }

    /**
     * With duplicate matching.
     * @throws Exception If something goes wrong
     */
    @Test(expected = IllegalStateException.class)
    public void testEntireCycleWithDuplicateMatching()
        throws Exception {
        final SimpleProvider provider = new SimpleProvider();
        final Connector connector = new SimpleConnector(provider);
        final YMockServer server = new YMockServer(SimpleClient.ID, provider);
        server.when(
            new SimpleMatcher(this.REQUEST, true),
            new SimpleResponse(this.RESPONSE)
        );
        server.when(
            new SimpleMatcher(this.REQUEST, true),
            new SimpleResponse(this.RESPONSE)
        );
        new SimpleClient(connector).run();
    }

    /**
     * With error in response.
     * @throws Exception If something goes wrong
     */
    @Test(expected = IllegalStateException.class)
    public void testEntireCycleThroughMocksWithErrorInResponse()
        throws Exception {
        final SimpleProvider provider = new SimpleProvider();
        final Connector connector = new SimpleConnector(provider);
        new YMockServer(SimpleClient.ID, provider);
        new SimpleClient(connector).run();
    }

    /**
     * Through live HTTP.
     * @throws Exception If something goes wrong
     */
    @org.junit.Ignore
    @Test
    public void testEntireCycleThroughLiveHttp() throws Exception {
        final YMockServer server = new YMockServer(SimpleClient.ID);
        server.when(this.REQUEST, this.RESPONSE);
        new SimpleClient().run();
    }

}
