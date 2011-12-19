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
import java.io.InputStream;

/**
 * Mock version of {@link InputStream}.
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 * @see <a href="http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">HTTP</a>
 * @see <a href="http://tools.ietf.org/html/rfc2616">RFC2616</a>
 */
final class SMInputStream extends InputStream {

    /**
     * The data bridge.
     * @see #read()
     */
    private final transient DataBuffer bridge;

    /**
     * The message to return.
     * @see #read()
     */
    private final transient StringBuilder message = new StringBuilder();

    /**
     * End of file was JUST returned, and we should return "-1" this
     * amount of times, in response to {@link #read()}.
     * @see #read()
     */
    private transient int eof;

    /**
     * Public ctor.
     * @param bdg The data bridge to use
     * @see MockSocket#getOutputStream()
     */
    @SuppressWarnings("PMD.CallSuperInConstructor")
    public SMInputStream(final DataBuffer bdg) {
        this.bridge = bdg;
    }

    /**
     * {@inheritDoc}
     * @todo #7 This implementation is very rough and not elegant.
     *  In the future would be nice to refactor the method.
     * @checkstyle ReturnCount (20 lines)
     * @checkstyle NestedIfDepth (20 lines)
     */
    @Override
    @SuppressWarnings("PMD.OnlyOneReturn")
    public int read() throws IOException {
        if (this.message.length() == 0) {
            if (this.eof == 0) {
                this.eof = 2;
                this.message.append(this.bridge.receive());
                if (this.message.length() == 0) {
                    return -1;
                }
            } else {
                this.eof -= 1;
                return -1;
            }
        }
        final int data = (int) this.message.charAt(0);
        this.message.deleteCharAt(0);
        return data;
    }

}
