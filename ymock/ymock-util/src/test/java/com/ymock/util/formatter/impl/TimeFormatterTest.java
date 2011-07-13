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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @todo #25! Provide implementation, write javadoc
 * @author Marina kosenko
 */
@PrepareForTest(System.class)
public class TimeFormatterTest {

    /**
     * half a second
     */
    private static final long NANO_HALF_SEC = 500000000L;

    /**
     * half a second
     */
    private static final long MILLI_HALF_SEC = 500L;

    /**
     * second * 2 + second * 60 * 5
     */
    private static final long CURRENT_TIME_NANO = 302000000000L;

    /**
     * second * 1 + second * 60 * 2
     */
    private static final long TEST_TIME_NANO = 121000000000L;

    /**
     * second * 2 + second * 60 * 5 + second * 60 * 60 * 45
     */
    private static final long CURRENT_TIME_DAYS_NANO = 162302000000000L;

    /**
     * second * 2 + second * 60 * 5
     */
    private static final long CURRENT_TIME_MILLI = 302000L;

    /**
     * second * 1 + second * 60 * 2
     */
    private static final long TEST_TIME_MILLI = 121000L;

    /**
     * second * 2 + second * 60 * 5 + second * 60 * 60 * 45
     */
    private static final long CURRENT_TIME_DAYS_MILLI = 162302000L;

    /**
     * result time with hours
     */
    private static final String RESULT_TIME_HOUR = "45:03:01";

    /**
     * result time
     */
    private static final String RESULT_TIME = "03:01";

    /**
     * result time with decimal seconds
     */
    private static final String RESULT_TIME_DECIMAL = "03:00.5";

    private TimeFormatter timeFormatter;

    @Before
    public final void setUp() throws Exception {
        this.timeFormatter = new TimeFormatter();
    }

    @Test
    public final void testFormatFake() {
        this.timeFormatter.formatMilli(0L);
        this.timeFormatter.formatNano(0L);
    }

    @Test
    @Ignore
    public final void testFormatNano() {
        PowerMockito.mockStatic(System.class);
        Mockito.when(System.nanoTime()).thenReturn(this.CURRENT_TIME_NANO);
        final String formatted = this.timeFormatter
            .formatNano(this.TEST_TIME_NANO);
        assertThat(formatted, equalTo(this.RESULT_TIME));
    }

    @Test
    @Ignore
    public final void testFormatNanoHours() {
        Mockito.when(System.nanoTime()).thenReturn(this.CURRENT_TIME_DAYS_NANO);
        final String formatted = this.timeFormatter
            .formatNano(this.TEST_TIME_NANO);
        assertThat(formatted, equalTo(this.RESULT_TIME_HOUR));
    }

    @Test
    @Ignore
    public final void testFormatNanoDecimal() {
        Mockito.when(System.nanoTime()).thenReturn(this.CURRENT_TIME_NANO);
        final String formatted = this.timeFormatter
            .formatNano(this.TEST_TIME_NANO + this.NANO_HALF_SEC);
        assertThat(formatted, equalTo(this.RESULT_TIME_DECIMAL));
    }

    @Test
    @Ignore
    public final void testFormatMilli() {
        PowerMockito.mockStatic(System.class);
        Mockito.when(System.nanoTime()).thenReturn(this.CURRENT_TIME_MILLI);
        final String formatted = this.timeFormatter
            .formatMilli(this.TEST_TIME_MILLI);
        assertThat(formatted, equalTo(this.RESULT_TIME));
    }

    @Test
    @Ignore
    public final void testFormatMilliHours() {
        Mockito.when(System.nanoTime())
            .thenReturn(this.CURRENT_TIME_DAYS_MILLI);
        final String formatted = this.timeFormatter
            .formatMilli(this.TEST_TIME_MILLI);
        assertThat(formatted, equalTo(this.RESULT_TIME_HOUR));
    }

    @Test
    @Ignore
    public final void testFormatMilliDecimal() {
        Mockito.when(System.nanoTime()).thenReturn(this.CURRENT_TIME_MILLI);
        final String formatted = this.timeFormatter
            .formatMilli(this.TEST_TIME_MILLI + this.MILLI_HALF_SEC);
        assertThat(formatted, equalTo(this.RESULT_TIME_DECIMAL));
    }

}
