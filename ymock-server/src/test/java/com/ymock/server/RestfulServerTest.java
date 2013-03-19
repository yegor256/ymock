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
package com.ymock.server;

import com.ymock.server.responses.ErrorResponse;
import com.ymock.server.responses.TextResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link RestfulServlet}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class RestfulServerTest {

    /**
     * Test it.
     * @throws Exception If something wrong inside
     */
    @Test
    public void testServerInstantiation() throws Exception {
        final CallsProvider provider = RestfulServer.INSTANCE;
        final String request = "requested message";
        final String message = "some text";
        provider.register(new PositiveCatcher(request, message));
        final HttpClient client = new DefaultHttpClient();
        final HttpPost post = new HttpPost(this.url());
        post.setEntity(new StringEntity(request));
        final ResponseHandler<String> handler = new BasicResponseHandler();
        String body;
        try {
            body = client.execute(post, handler);
        } finally {
            client.getConnectionManager().shutdown();
        }
        MatcherAssert.assertThat(body, Matchers.equalTo(message));
    }

    /**
     * Positive catcher.
     */
    private static class PositiveCatcher implements Catcher {
        /**
         * Expected request.
         */
        private final transient String expected;
        /**
         * Message to return.
         */
        private final transient String message;
        /**
         * Public ctor.
         * @param rqt The expected request
         * @param msg The message to return
         */
        public PositiveCatcher(final String rqt, final String msg) {
            this.expected = rqt;
            this.message = msg;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Response call(final String request) {
            MatcherAssert.assertThat(request, Matchers.equalTo(this.expected));
            return new TextResponse(this.message);
        }
    }

    /**
     * Test it.
     * @throws Exception If something wrong inside
     */
    @Test
    public void testServerWithNegativeResponse() throws Exception {
        final String message = "some error message";
        final CallsProvider provider = RestfulServer.INSTANCE;
        provider.register(new NegativeCatcher(message));
        final HttpClient client = new DefaultHttpClient();
        final HttpPost httppost = new HttpPost(this.url());
        final ResponseHandler<String> handler = new NegativeResponseHandler();
        String body;
        try {
            body = client.execute(httppost, handler);
        } finally {
            client.getConnectionManager().shutdown();
        }
        MatcherAssert.assertThat(body, Matchers.equalTo(message));
    }

    /**
     * Negative catcher.
     */
    private static class NegativeCatcher implements Catcher {
        /**
         * Message.
         */
        private final transient String message;
        /**
         * Public ctor.
         * @param msg The message
         */
        public NegativeCatcher(final String msg) {
            this.message = msg;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public Response call(final String request) {
            return new ErrorResponse(this.message);
        }
    }

    /**
     * Negative response handler.
     */
    private static class NegativeResponseHandler
        implements ResponseHandler<String> {
        /**
         * {@inheritDoc}
         */
        @Override
        public String handleResponse(final HttpResponse response) {
            MatcherAssert.assertThat(
                response.getStatusLine().getStatusCode(),
                Matchers.equalTo(
                    javax.ws.rs.core.Response.Status.BAD_REQUEST
                    .getStatusCode()
                )
            );
            try {
                return IOUtils.toString(response.getEntity().getContent());
            } catch (java.io.IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    /**
     * Create URL.
     * @return The URL
     */
    private String url() {
        return String.format(
            "http://localhost:%d/ymock/mock",
            RestfulServer.INSTANCE.port()
        );
    }

}
