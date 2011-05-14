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

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.junit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class HttpConnectorTest {

    @Test
    public void testSimpleCallToServer() throws Exception {
        final String message = "some text";
        final Connector connector = new HttpConnector(
            this.client(message, HttpStatus.SC_OK)
        );
        final String response = connector.call("something...");
        assertThat(response, equalTo(message));
    }

    @Test(expected = YMockException.class)
    public void testCallWithErrorResponse() throws Exception {
        final Connector connector = new HttpConnector(
            this.client("msg", HttpStatus.SC_BAD_REQUEST)
        );
        connector.call("doesn't matter what");
    }

    @Test(expected = YMockException.class)
    public void testCallWithIOException() throws Exception {
        final HttpClient client = mock(HttpClient.class);
        final ClientConnectionManager mgr = mock(ClientConnectionManager.class);
        doReturn(mgr).when(client).getConnectionManager();
        doThrow(new java.io.IOException("test")).when(client).execute(
            (HttpUriRequest) anyObject()
        );
        new HttpConnector(client).call("simple text");
    }

    private HttpClient client(final String msg, final Integer code)
        throws Exception {
        final HttpClient client = mock(HttpClient.class);
        final ClientConnectionManager mgr = mock(ClientConnectionManager.class);
        doReturn(mgr).when(client).getConnectionManager();
        final HttpResponse response = mock(HttpResponse.class);
        doReturn(response).when(client).execute((HttpUriRequest) anyObject());
        final HttpEntity entity = mock(HttpEntity.class);
        doReturn(entity).when(response).getEntity();
        doReturn(IOUtils.toInputStream(msg)).when(entity).getContent();
        final StatusLine line = mock(StatusLine.class);
        doReturn(line).when(response).getStatusLine();
        doReturn(code).when(line).getStatusCode();
        return client;
    }

}
