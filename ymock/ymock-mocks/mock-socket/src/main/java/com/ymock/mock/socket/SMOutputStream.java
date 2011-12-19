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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Mock version of {@link OutputStream}.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 * @see MockDispatcher#getOutputStream
 * @see <a href="http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">HTTP</a>
 * @see <a href="http://tools.ietf.org/html/rfc2616">RFC2616</a>
 */
final class SMOutputStream extends OutputStream {

    /**
     * The buffer.
     */
    private final transient DataBuffer buffer;

    /**
     * Recent message.
     * @see #write(int)
     */
    private final transient StringBuilder message = new StringBuilder();

    /**
     * Public ctor.
     * @param buf The buffer to use
     * @see SMSocketImpl#getOutputStream()
     */
    @SuppressWarnings("PMD.CallSuperInConstructor")
    public SMOutputStream(final DataBuffer buf) {
        this.buffer = buf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final int data) throws IOException {
        this.message.append((char) data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() throws IOException {
        this.buffer.send(this.message.toString());
        this.message.setLength(0);
    }

}
