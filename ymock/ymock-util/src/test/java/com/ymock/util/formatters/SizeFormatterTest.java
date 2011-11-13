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
package com.ymock.util.formatters;

import java.io.File;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link SizeFormatter}.
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @todo #25! Provide implementation of SizeFormatter, write javadoc
 */
public class SizeFormatterTest {

    /**
     * Object under test.
     */
    private final SizeFormatter fmtr = new SizeFormatter();

    /**
     * NULL should be formatted without problems.
     */
    @Test
    public final void testNullFormatting() {
        MatcherAssert.assertThat(
            this.fmtr.format(null),
            Matchers.equalTo("NULL")
        );
    }

    /**
     * File size should be formatted.
     */
    @Test
    @org.junit.Ignore
    public final void testFileSizeFormatting() {
        final File ffile = Mockito.mock(File.class);
        Mockito.doReturn(5).when(file).length();
        MatcherAssert.assertThat(
            this.fmtr.format(file),
            Matchers.equalTo("5bytes")
        );
        Mockito.doReturn(5.02 * 1024).when(file).length();
        MatcherAssert.assertThat(
            this.fmtr.format(file),
            Matchers.equalTo("5Kb")
        );
        Mockito.doReturn(5.2 * 1024 * 1024).when(file).length();
        MatcherAssert.assertThat(
            this.fmtr.format(file),
            Matchers.equalTo("5.2Mb")
        );
        Mockito.doReturn(1334.65 * 1024 * 1024 * 1024).when(file).length();
        MatcherAssert.assertThat(
            this.fmtr.format(file),
            Matchers.equalTo("1334.7Gb")
        );
    }

    /**
     * File size should be formatted.
     */
    @Test
    @org.junit.Ignore
    public final void testAbsentFileSizeFormatting() {
        final File file = Mockito.mock(File.class);
        Mockito.doThrow(new java.io.IOException("ouch")).when(file).length();
        MatcherAssert.assertThat(
            this.fmtr.format(file),
            Matchers.equalTo("(?:ouch)")
        );
    }

}
