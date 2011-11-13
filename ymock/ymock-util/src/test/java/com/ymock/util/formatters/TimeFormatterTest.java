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
     * NULL should be formatted without problems.
     */
    @Test
    public final void testNullFormatting() {
        MatcherAssert.assertThat(
            this.fmtr.msec(null),
            Matchers.equalTo("NULL")
        );
    }

    /**
     * Nano-seconds should be convertable to text.
     */
    @Test
    @org.junit.Ignore
    public final void testFormatNano() {
        final double nano = 1000 * 1000 * 1000;
        MatcherAssert.assertThat(
            this.fmtr.nano(55.432 * nano),
            Matchers.equalTo("55.432sec")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(0.43 * nano),
            Matchers.equalTo("43ms")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(93 * 60 * nano),
            Matchers.equalTo("1:33hr")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(15.6 * 60 * nano),
            Matchers.equalTo("15min")
        );
    }

    /**
     * Milliseconds should be convertable to text.
     */
    @Test
    @org.junit.Ignore
    public final void testFormatNano() {
        final double milli = 1000 * 1000;
        MatcherAssert.assertThat(
            this.fmtr.nano(0.023 * milli),
            Matchers.equalTo("2.3ms")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(1.0001 * milli),
            Matchers.equalTo("1sec")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(100 * milli),
            Matchers.equalTo("1:40min")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(10 * 60 * 60 * milli),
            Matchers.equalTo("10hr")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(6 * 24 * 60 * 60 * milli),
            Matchers.equalTo("6days")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(3 * 7 * 24 * 60 * 60 * milli),
            Matchers.equalTo("3wks")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(5 * 30 * 24 * 60 * 60 * milli),
            Matchers.equalTo("5mo")
        );
        MatcherAssert.assertThat(
            this.fmtr.nano(3 * 12 * 30 * 24 * 60 * 60 * milli),
            Matchers.equalTo("3yrs")
        );
    }

}
