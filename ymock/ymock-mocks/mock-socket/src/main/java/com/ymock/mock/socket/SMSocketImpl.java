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
import com.ymock.util.Logger;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.util.regex.Pattern;

/**
 * Mock version of {@link SocketImpl}.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
@SuppressWarnings("PMD.TooManyMethods")
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
     * @param ptrn What hosts do we match?
     */
    public SMSocketImpl(final Pattern ptrn) {
        super();
        this.pattern = ptrn;
        Logger.debug(
            this,
            "#SMSocketImpl('%s'): instantiated",
            ptrn
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(final SocketImpl sckt) throws IOException {
        Logger.debug(
            this,
            "#accept('%s'): done",
            sckt.getClass().getName()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available() throws IOException {
        final int bytes = 0;
        Logger.debug(
            this,
            "#available(): returned %d",
            bytes
        );
        return bytes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(final InetAddress host, final int port)
        throws IOException {
        Logger.debug(
            this,
            "#bind('%s', %d): done",
            host,
            port
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        Logger.debug(
            this,
            "#close(): done"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(final InetAddress host, final int port)
        throws IOException {
        Logger.debug(
            this,
            "#connect('%s', %d): done with InetAddress",
            host,
            port
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(final SocketAddress pnt, final int timeout)
        throws IOException {
        Logger.debug(
            this,
            "#connect('%s', %d): done with SocketAddress",
            pnt,
            timeout
        );
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
        Logger.debug(
            this,
            "#connect('%s', %d): done",
            host,
            port
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final boolean stream) throws IOException {
        Logger.debug(
            this,
            "#create(%B): done",
            stream
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileDescriptor getFileDescriptor() {
        final FileDescriptor descriptor = this.fd;
        Logger.debug(
            this,
            "#getFileDescriptor(): returned %s",
            descriptor
        );
        return descriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InetAddress getInetAddress() {
        final InetAddress addr = this.address;
        Logger.debug(
            this,
            "#getInetAddress(): returned %s",
            addr
        );
        return addr;
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
        Logger.debug(
            this,
            "#getInputStream(): returned '%s'",
            stream.getClass().getName()
        );
        return stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLocalPort() {
        final int local = this.localport;
        Logger.debug(
            this,
            "#getLocalPort(): returned %d",
            local
        );
        return local;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOption(final int opt) throws SocketException {
        final Object obj = "nothing";
        Logger.debug(
            this,
            "#getOption(%d): returned %s",
            obj.getClass().getName()
        );
        return obj;
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
        Logger.debug(
            this,
            "#getOutputStream(): returned '%s'",
            stream.getClass().getName()
        );
        return stream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        final int prt = this.port;
        Logger.debug(
            this,
            "#getPort(): returned %d",
            prt
        );
        return prt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listen(final int backlog) throws IOException {
        Logger.debug(
            this,
            "#listen(%d): done",
            backlog
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendUrgentData(final int data) throws IOException {
        Logger.debug(
            this,
            "#sendUrgentData(%d): done",
            data
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOption(final int opt, final Object obj)
        throws SocketException {
        Logger.debug(
            this,
            "#setOption(%d, %s): done",
            opt,
            obj.getClass().getName()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPerformancePreferences(final int time,
        final int latency, final int bandwidth) {
        Logger.debug(
            this,
            "#setPerformancePreferences(%d, %d, %d): done",
            time,
            latency,
            bandwidth
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownInput() throws IOException {
        Logger.debug(
            this,
            "#shutdownInput(): done"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownOutput() throws IOException {
        Logger.debug(
            this,
            "#shutdownOutput(): done"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsUrgentData() {
        final boolean supports = false;
        Logger.debug(
            this,
            "#supportsUrgentData(): returned %B",
            supports
        );
        return supports;
    }

}
