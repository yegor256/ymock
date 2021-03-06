/**
 * Copyright (c) 2011-2012, yMock.com
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
import com.ymock.commons.YMockException;
import java.io.IOException;

/**
 * Bridge between this component and {@link YMockClient}.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
final class YMockBridge implements DataBridge {

    /**
     * YMock client's name.
     */
    public static final String NAME = "com.ymock.mock.socket";

    /**
     * YMock client.
     */
    private static final YMockClient CLIENT =
        new YMockClient(YMockBridge.NAME);

    /**
     * The response to return to {@link #receive()}.
     */
    private transient String response;

    /**
     * Response is ready?
     */
    private transient boolean ready;

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final String message) throws IOException {
        try {
            this.response = YMockBridge.CLIENT.call(message);
            this.ready = true;
        } catch (YMockException ex) {
            throw new java.io.IOException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String receive() {
        if (!this.ready) {
            throw new IllegalStateException("Nothing to return");
        }
        final String msg = this.response;
        this.ready = false;
        return msg;
    }

}
