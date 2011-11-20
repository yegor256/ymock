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
package com.ymock.client;

import com.ymock.commons.YMockException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link HttpConnector}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class HttpConnectorTest {

    /**
     * Simple call to server.
     * @throws Exception If some problem inside
     */
    @Test
    public void testSimpleCallToServer() throws Exception {
        final String message = "some text";
        final Connector connector = new HttpConnector(
            this.client(message, HttpStatus.SC_OK)
        );
        final String response = connector.call("something...");
        MatcherAssert.assertThat(response, Matchers.equalTo(message));
    }

    /**
     * Simple call with error response.
     * @throws Exception If some problem inside
     */
    @Test(expected = YMockException.class)
    public void testCallWithErrorResponse() throws Exception {
        final Connector connector = new HttpConnector(
            this.client("msg", HttpStatus.SC_BAD_REQUEST)
        );
        connector.call("some request, ignored");
    }

    /**
     * Simple call with exception.
     * @throws Exception If some problem inside
     */
    @Test(expected = YMockException.class)
    public void testCallWithIOException() throws Exception {
        final HttpClient client = Mockito.mock(HttpClient.class);
        final ClientConnectionManager mgr =
            Mockito.mock(ClientConnectionManager.class);
        Mockito.doReturn(mgr).when(client).getConnectionManager();
        Mockito.doThrow(new java.io.IOException("test")).when(client)
            .execute(Mockito.any(HttpUriRequest.class));
        new HttpConnector(client).call("simple text");
    }

    /**
     * Simple call with real socket.
     * @throws Exception If some problem inside
     */
    @Test(expected = YMockException.class)
    public void testCallWithRealSocket() throws Exception {
        final Connector connector = new HttpConnector();
        connector.call("doesn't matter what");
    }

    /**
     * Test two consequetive calls.
     * @throws Exception If some problem inside
     */
    @Test
    public void testTwoConsequetiveCalls() throws Exception {
        final Connector connector = new HttpConnector();
        try {
            connector.call("first request");
        } catch (YMockException ex) {
            // swallow it
            MatcherAssert.assertThat(
                ex,
                Matchers.instanceOf(YMockException.class)
            );
        }
        try {
            connector.call("second request");
        } catch (YMockException ex) {
            // swallow it
            MatcherAssert.assertThat(
                ex,
                Matchers.instanceOf(YMockException.class)
            );
        }
    }

    /**
     * Create client.
     * @param msg The message to return
     * @param code The code
     * @return The client
     * @throws Exception If some problem inside
     */
    private HttpClient client(final String msg, final Integer code)
        throws Exception {
        final HttpClient client = Mockito.mock(HttpClient.class);
        final ClientConnectionManager mgr =
            Mockito.mock(ClientConnectionManager.class);
        Mockito.doReturn(mgr).when(client).getConnectionManager();
        final HttpResponse response = Mockito.mock(HttpResponse.class);
        Mockito.doReturn(response).when(client)
            .execute(Mockito.any(HttpUriRequest.class));
        final HttpEntity entity = Mockito.mock(HttpEntity.class);
        Mockito.doReturn(entity).when(response).getEntity();
        Mockito.doReturn(IOUtils.toInputStream(msg)).when(entity).getContent();
        final StatusLine line = Mockito.mock(StatusLine.class);
        Mockito.doReturn(line).when(response).getStatusLine();
        Mockito.doReturn(code).when(line).getStatusCode();
        return client;
    }

}
