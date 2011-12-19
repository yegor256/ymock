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

import com.ymock.client.YMockClient;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.nio.channels.SocketChannel;
import java.util.regex.Pattern;

/**
 * Mock version of {@link SocketImpl}.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
final class SMSocketImpl extends SocketImpl {

    /**
     * Regex for connections.
     */
    private final transient Pattern pattern;

    /**
     * Real socket.
     */
    private transient Socket socket;

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
    public void accept(final SocketImpl sckt) throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available() throws IOException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(final InetAddress host, final int port)
        throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(final InetAddress host, final int port)
        throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(final SocketAddress pnt, final int timeout)
        throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(final String host, final int port) throws IOException {
        if (this.pattern.matcher(host).matches()) {
            final YMockClient client = new YMockClient(
                String.format("com.ymock.mock.socket:%s", host)
            );
            final DataBuffer buffer = new YMockBuffer(client);
            this.input = new SMInputStream(buffer);
            this.output = new SMOutputStream(buffer);
        } else {
            this.socket = new Socket(host, port);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final boolean stream) throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileDescriptor getFileDescriptor() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InetAddress getInetAddress() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * <p>We are overriding the default implementation of {@link Socket},
     * in order to mock its real behavior. Instead of writing to socket
     * we're reading from {@link DataBuffer}.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream stream;
        if (this.input == null) {
            stream = this.socket.getInputStream();
        } else {
            stream = this.input;
        }
        return stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLocalPort() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOption(final int opt) throws SocketException {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * <p>We are overriding the default implementation of {@link Socket},
     * in order to mock its real behavior. Instead of writing to socket
     * we're writing to {@link DataBuffer}.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        OutputStream stream;
        if (this.output == null) {
            stream = this.socket.getOutputStream();
        } else {
            stream = this.output;
        }
        return stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listen(final int backlog) throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendUrgentData(final int data) throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOption(final int opt, final Object obj)
        throws SocketException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPerformancePreferences(final int time,
        final int latency, final int bandwidth) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownInput() throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownOutput() throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsUrgentData() {
        return false;
    }

}
