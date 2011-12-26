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

import com.ymock.server.YMockServer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.regex.Pattern;
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
     * SMSocketImpl can do the job end-to-end.
     * @throws Exception If something wrong inside
     */
    @Test
    @org.junit.Ignore
    public void forwardsTcpTrafficToYmockServer() throws Exception {
        final int port = 1;
        final String host = "www.google.com";
        final String response = "some text back";
        new YMockServer(String.format("com.ymock.mock.socket:%s", host))
            .when(".*", response);
        final SMSocketImpl impl = new SMSocketImpl(
            Pattern.compile(Pattern.quote(host)),
            new Socket()
        );
        impl.connect(host, port);
        final Socket socket = new Socket(impl) { };
        socket.connect(new InetSocketAddress(host, port));
        IOUtils.write("GET / HTTP/1.1", socket.getOutputStream());
        socket.getOutputStream().flush();
        MatcherAssert.assertThat(
            IOUtils.toString(socket.getInputStream()),
            Matchers.equalTo(response)
        );
    }

    /**
     * SMSocketImpl can pass the traffic that is not relevant.
     * @throws Exception If something wrong inside
     */
    @Test
    @org.junit.Ignore
    public void passesNonRelatedTcpTraffic() throws Exception {
        SMSocketImplFactory.INSTANCE.start();
        final ServerSocket server = new ServerSocket(0);
        final int port = server.getLocalPort();
        final Socket socket = new Socket("localhost", port);
        IOUtils.write("test", socket.getOutputStream());
        socket.getOutputStream().flush();
        server.close();
        SMSocketImplFactory.INSTANCE.stop();
    }

    /**
     * SMSocketImpl can work with HTTP protocol.
     * @throws Exception If something wrong inside
     */
    @Test
    public void mocksHttpProtocol() throws Exception {
        SMSocketImplFactory.INSTANCE.start().match("www.apple.com");
        final String content = "some text";
        MatcherAssert.assertThat(
            (String) new URL("http://www.apple.com/").getContent(),
            Matchers.equalTo(content)
        );
        SMSocketImplFactory.INSTANCE.stop();
    }

}
