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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Mock version of {@link SocketImpl}.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
final class SMSocket extends SocketImpl {

    /**
     * Regex for connections.
     */
    private final transient Pattern pattern;

    /**
     * Output stream.
     */
    private transient OutputStream output;

    /**
     * Input stream.
     */
    private transient InputStream input;

    /**
     * Public ctor.
     * @param regex What hosts do we match?
     */
    public SMSocketImpl(final Pattern ptrn) {
        super();
        this.pattern = ptrn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(final String host, final int port) {
        if (this.pattern.matches(host)) {
            final YMockClient client = new YMockClient(
                String.format("com.ymock.mock.socket:%s", host)
            );
            final DataBuffer buffer = new YMockBuffer(client);
            this.input = new SMInputStream(buffer);
            this.output = new SMInputStream(buffer);
        } else {
            this.connect(host, port);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>We are overriding the default implementation of {@link Socket},
     * in order to mock its real behavior. Instead of writing to socket
     * we're writing to {@link DataBuffer}.
     */
    @Override
    public OutputStream getOutputStream() {
        OutputStream stream;
        if (this.output == null) {
            stream = super.getOutputStream();
        } else {
            stream = this.output;
        }
        return stream;
    }

    /**
     * {@inheritDoc}
     *
     * <p>We are overriding the default implementation of {@link Socket},
     * in order to mock its real behavior. Instead of writing to socket
     * we're reading from {@link DataBuffer}.
     */
    @Override
    public InputStream getInputStream() {
        InputStream stream;
        if (this.input == null) {
            stream = super.getInputStream();
        } else {
            stream = this.input;
        }
        return stream;
    }

}
