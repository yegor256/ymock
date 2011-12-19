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

import java.net.Socket;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link SMSocketImpl}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class SMSocketImplTest {

    /**
     * SMSocketImpl can catch HTTP sessions requests and responses.
     * @throws Exception If something wrong inside
     */
    @Test
    public void respondsToHttpSessionSimulation() throws Exception {
        // final DataBuffer buffer = new DataBufferMocker()
        //     .doReturn(".*POST.*", "200 OK")
        //     .mock();
        // final SocketImpl socket = new SMSocketImpl(".*");
        // final String message = "POST /index HTTP/1.0\r\n"
        //     + "Content-Length: " + this.REQUEST.length() + "\r\n"
        //     + "Content-Type: application/x-www-form-urlencoded\r\n\r\n"
        //     + this.REQUEST;
        // final String response = IOUtils.toString(socket.getInputStream());
        // IOUtils.write(message, socket.getOutputStream());
        // MatcherAssert.assertThat(response, Matchers.equalTo(this.RESPONSE));
    }

}
