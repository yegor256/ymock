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
package com.ymock.util.formatter;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 */
public class FormatterManagerTest {

    private static final String STRING_TO_FORMAT = "aaa";
    private static final String FORMATTED = "formatted";

    private FormatterManager formatterManager;

    @Before
    public final void setUp() throws Exception {
        this.formatterManager = FormatterManager.getInstance();
    }

    @Test
    public final void testFormat() throws Exception {
        final String s = this.formatterManager.fmt("group.format",
            FormatterManagerTest.STRING_TO_FORMAT);
        assertThat(s, equalTo(FormatterManagerTest.STRING_TO_FORMAT
            + FormatterManagerTest.FORMATTED));
        assertThat(s, equalTo(FormatterManagerTest.STRING_TO_FORMAT
            + FormatterManagerTest.FORMATTED));
    }

    @Test
    public final void testFormatFormatterDoesntExist() throws Exception {
        final String s = this.formatterManager.fmt("group.aaa",
            FormatterManagerTest.STRING_TO_FORMAT);
        assertThat(s, equalTo(FormatterManagerTest.STRING_TO_FORMAT));
    }

}
