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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link PasswordFormatter}.
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 * @todo #25! Provide implementation of PasswordFormatter, write javadoc
 */
public class PasswordFormatterTest {

    /**
     * Instance of the class to test.
     */
    private final PasswordFormatter fmtr = new PasswordFormatter();

    /**
     * NULL value should be formatted properly.
     */
    @Test
    public final void testFormatNullValue() {
        MatcherAssert.assertThat(
            this.fmtr.format(null),
            Matchers.equalTo("NULL")
        );
    }

    /**
     * Normal text should be escaped.
     */
    @Test
    @org.junit.Ignore
    public final void testNormalTextFormatting() {
        MatcherAssert.assertThat(
            this.fmtr.format("alpha beta gamma"),
            Matchers.equalTo("\"a***a\"")
        );
        MatcherAssert.assertThat(
            this.fmtr.format("a"),
            Matchers.equalTo("\"a***a\"")
        );
        MatcherAssert.assertThat(
            this.fmtr.format("ab"),
            Matchers.equalTo("\"a***b\"")
        );
    }

    /**
     * Empty text should be escaped.
     */
    @Test
    @org.junit.Ignore
    public final void testEmptyTextFormatting() {
        MatcherAssert.assertThat(
            this.fmtr.format(""),
            Matchers.equalTo("\"\"")
        );
    }

}