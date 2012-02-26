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

import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link SMInputStream}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class SMInputStreamTest {

    /**
     * Test it.
     * @throws Exception If something wrong inside
     */
    @Test
    public void testSimulatesStreamReading() throws Exception {
        this.read("simple text\r\nagain\n\nmore");
    }

    /**
     * Test it.
     * @throws Exception If something wrong inside
     */
    @Test
    public void testReadingOfEmptyString() throws Exception {
        this.read("");
    }

    /**
     * Read text.
     * @param text The text
     * @throws Exception If something wrong inside
     */
    private void read(final String text) throws Exception {
        final InputStream stream = new SMInputStream(new SimpleBridge(text));
        MatcherAssert.assertThat(
            IOUtils.toString(stream),
            Matchers.equalTo(text)
        );
        MatcherAssert.assertThat(
            IOUtils.toString(stream),
            Matchers.equalTo(text)
        );
    }

    /**
     * Simple bridge.
     */
    private static class SimpleBridge implements DataBridge {
        /**
         * Message.
         */
        private final transient String message;
        /**
         * Public ctor.
         * @param msg The message
         */
        public SimpleBridge(final String msg) {
            this.message = msg;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void send(final String msg) {
            // ignore it
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String receive() {
            return this.message;
        }
    }

}
