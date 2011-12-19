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
package com.ymock.mock.socket;

import com.ymock.commons.YMockException;
import com.ymock.server.Response;
import com.ymock.server.YMockServer;
import com.ymock.server.matchers.RegexMatcher;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link YMockBuffer}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class YMockBufferTest {

    /**
     * YMockBuffer can send messages to YMockClient.
     * @throws Exception If something wrong inside
     */
    @Test
    public void sendsMessagesThroughClient() throws Exception {
        final Connector connector = Mockito.mock(Connector.class);
        final YMockClient client = new YMockClient("", connector);
        final DataBuffer buffer = new YMockBuffer(client);
        final String request = "some message to send";
        buffer.send(request);
        Mockito.verify(connector).send(request);
    }

    /**
     * YMockBuffer can read responses from YMockClient.
     * @throws Exception If something wrong inside
     */
    @Test
    public void receivesResponsesFromClient() throws Exception {
        final String request = "some request";
        final String response = "some response";
        final Connector connector = Mockito.mock(Connector.class);
        Mockito.doReturn(response).when(connector).call(request);
        final YMockClient client = new YMockClient("", connector);
        final DataBuffer buffer = new YMockBuffer(client);
        buffer.send(request);
        MatcherAssert.assertThat(
            buffer.receive(),
            Matchers.equalTo(response)
        );
    }

    /**
     * YMockBuffer can throw exception if buffer is empty.
     * @throws Exception If something wrong inside
     */
    @Test(expected = IOException.class)
    public void throwsExceptionWhenBufferIsEmpty() throws Exception {
        final Connector connector = Mockito.mock(Connector.class);
        final YMockClient client = new YMockClient("", connector);
        final DataBuffer buffer = new YMockBuffer(client);
        buffer.send("some text to send");
        buffer.receive();
        buffer.receive();
    }

    /**
     * YMockBuffer can throw IOException when there is a problem with client.
     * @throws Exception If something wrong inside
     */
    @Test(expected = IOException.class)
    public void throwsIoExceptionWhenYmockExceptionAppears() throws Exception {
        final String request = "some request with exception";
        final Connector connector = Mockito.mock(Connector.class);
        Mockito.doThrow(new IOException()).when(connector).call(request);
        final YMockClient client = new YMockClient("", connector);
        new YMockBuffer(client).send(request);
    }

}
