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

import org.junit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public class SMInputStreamTest {

    @Test
    public void testSimulatesStreamReading() throws Exception {
        this.read("simple text\r\nagain\n\nmore");
    }

    @Test
    public void testReadingOfEmptyString() throws Exception {
        this.read("");
    }

    private void read(final String text) throws Exception {
        final InputStream stream = new SMInputStream(new SimpleBridge(text));
        assertThat(IOUtils.toString(stream), equalTo(text));
        // let's try again, should work
        assertThat(IOUtils.toString(stream), equalTo(text));
    }

    private static class SimpleBridge implements DataBridge {
        private final String message;
        public SimpleBridge(final String msg) {
            this.message = msg;
        }
        @Override
        public void send(final String message) {
            // ignore it
        }
        @Override
        public String receive() {
            return this.message;
        }
    }


}
