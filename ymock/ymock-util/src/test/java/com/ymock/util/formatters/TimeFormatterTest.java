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
 * Test case for {@link TimeFormatter}.
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 * @todo #25! Provide implementation of TimeFormatter, write javadoc
 */
public class TimeFormatterTest {

    /**
     * Object under test.
     */
    private TimeFormatter fmtr = new TimeFormatter();

    /**
     * Zero should be formatted without problems.
     */
    @Test
    @org.junit.Ignore
    public final void testZeroFormatting() {
        MatcherAssert.assertThat(
            this.fmtr.nano(0),
            Matchers.equalTo("0mks")
        );
        MatcherAssert.assertThat(
            this.fmtr.msec(0),
            Matchers.equalTo("0ms")
        );
    }

    /**
     * Nano-seconds should be convertable to text.
     * @checkstyle MagicNumber (30 lines)
     */
    @Test
    @org.junit.Ignore
    public final void testFormatNano() {
        final double sec = 1000 * 1000 * 1000;
        MatcherAssert.assertThat(
            this.fmtr.nano((long) (55.432 * sec)),
            Matchers.equalTo("55.432sec")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano((long) (0.43 * sec)),
            Matchers.equalTo("43ms")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano((long) (0.000051 * sec)),
            Matchers.equalTo("51mks")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano((long) (93 * 60 * sec)),
            Matchers.equalTo("1:33hr")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano((long) (15.6 * 60 * sec)),
            Matchers.equalTo("15min")
        );
    }

    /**
     * Milliseconds should be convertable to text.
     * @checkstyle MagicNumber (50 lines)
     */
    @Test
    @org.junit.Ignore
    public final void testFormatMillis() {
        final double sec = 1000 * 1000;
        MatcherAssert.assertThat(
            this.fmtr.msec((long) (0.0023 * sec)),
            Matchers.equalTo("2.3ms")
        );
        MatcherAssert.assertThat(
            this.fmtr.msec((long) (1.0001 * sec)),
            Matchers.equalTo("1sec")
        );
        MatcherAssert.assertThat(
            this.fmtr.msec((long) (100 * sec)),
            Matchers.equalTo("1:40min")
        );
        MatcherAssert.assertThat(
            this.fmtr.msec((long) (10 * 60 * 60 * sec)),
            Matchers.equalTo("10hr")
        );
        MatcherAssert.assertThat(
            this.fmtr.msec((long) (6 * 24 * 60 * 60 * sec)),
            Matchers.equalTo("6days")
        );
        MatcherAssert.assertThat(
            this.fmtr.msec((long) (3 * 7 * 24 * 60 * 60 * sec)),
            Matchers.equalTo("3wks")
        );
        MatcherAssert.assertThat(
            this.fmtr.msec((long) (5 * 30 * 24 * 60 * 60 * sec)),
            Matchers.equalTo("5mo")
        );
        MatcherAssert.assertThat(
            this.fmtr.msec((long) (3 * 12 * 30 * 24 * 60 * 60 * sec)),
            Matchers.equalTo("3yrs")
        );
    }

}
