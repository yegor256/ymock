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
 * Mock version of {@link Socket}.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 * @todo #7 There should be some mechanism implemented in order to
 *  watch a real connection and protocol what's going on with
 *  it. Also extra logging should be added to this component. Now
 *  it is very silent.
 */
public final class SMSocket extends Socket {

    /**
     * Output stream.
     */
    private final transient OutputStream outputStream;

    /**
     * Input stream.
     */
    private final transient InputStream inputStream;

    /**
     * Public ctor.
     */
    @SuppressWarnings("PMD.CallSuperInConstructor")
    public SMSocket() {
        this(new YMockBridge());
    }

    /**
     * Public ctor.
     * @param bridge The dispatcher to use
     */
    @SuppressWarnings("PMD.CallSuperInConstructor")
    public SMSocket(final DataBridge bridge) {
        this.outputStream = new SMOutputStream(bridge);
        this.inputStream = new SMInputStream(bridge);
    }

    /**
     * {@inheritDoc}
     *
     * <p>We are overriding the default implementation of {@link Socket},
     * in order to mock its real behavior. Instead of writing to socket
     * we're writing to {@link DataBridge}.
     */
    @Override
    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    /**
     * {@inheritDoc}
     *
     * <p>We are overriding the default implementation of {@link Socket},
     * in order to mock its real behavior. Instead of writing to socket
     * we're reading from {@link DataBridge}.
     */
    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

}
