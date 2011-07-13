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
package com.ymock.util.formatter.impl;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * @todo #25! Provide implementation, write javadoc
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 */
public class SizeFormatterTest {

    private static final String ERROR = "ERROR";
    private static final int N_5 = 5;

    private SizeFormatter sizeFormatter;

    @Before
    public final void setUp() throws Exception {
        this.sizeFormatter = new SizeFormatter();
    }

    @Test
    public final void testFormatFake() {
        this.sizeFormatter.format(null);
    }

    @Test
    @Ignore
    public final void testFormat() {
        final File testfile = mock(File.class);
        doReturn(this.N_5).when(testfile).length();
        final String formatted = this.sizeFormatter.format(testfile);
        assertThat(formatted, equalTo("5 bytes"));
    }

    @Test
    @Ignore
    public final void testFormatNull() {
        final String formatted = this.sizeFormatter.format(null);
        assertThat(formatted, equalTo(this.ERROR));
    }

    @Test
    @Ignore
    public final void testFormatEmpty() {
        final File testfile = new File("donotexist");
        final String formatted = this.sizeFormatter.format(testfile);
        assertThat(formatted, equalTo(this.ERROR));
    }
}
