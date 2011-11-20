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
package com.ymock.util.decors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

/**
 * Test case for {@link NanoDecor}.
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
@RunWith(Parameterized.class)
public class NanoDecorTest {

    /**
     * The value to test against.
     */
    private final long nano;

    /**
     * The text to expect as an output.
     */
    private final String text;

    /**
     * Formatting flas.
     */
    private final int flags;

    /**
     * Formatting width.
     */
    private final int width;

    /**
     * Formatting precision.
     */
    private final int precision;

    /**
     * Public ctor.
     * @param nan The amount of nanoseconds
     * @param txt Expected text
     * @param flgs Flags
     * @param wdt Width
     * @param prcs Precission
     */
    public NanoDecorTest(final long nan, final String txt,
        final int flgs, final int wdt, final int prcs) {
        this.nano = nan;
        this.text = txt;
        this.flags = flgs;
        this.width = wdt;
        this.precision = prcs;
    }

    /**
     * Params for this parametrized test.
     * @return Array of arrays of params for ctor
     */
    @Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(
            new Object[][] {
                { 234L, "0.234mcs", 0, 1, 1 },
                { 53111L, "53.11mcs  ", FormattableFlags.LEFT_JUSTIFY, 10, 2 },
                { 87090432L, "  87ms", 0, 6, 0 },
                { 6001001001L, "6.0010s", 0, 0, 4 },
                { 122001001001L, "  2MIN", FormattableFlags.UPPERCASE, 6, 0 },
                { 3789001001001L, "3789.00100s", 0, 0, 5 },
                { 3789002002002L, "63.2min", 0, 0, 1 },
                { 3789003003003L, "1hr", 0, 0, 0 },
                { 342000004004004L, "95hr", 0, 0, 0 },
            }
        );
    }

    /**
     * Zero should be formatted without problems.
     * @throws Exception If some problem inside
     */
    @Test
    @org.junit.Ignore
    public final void testDifferentFormats() throws Exception {
        final Formattable decor = new NanoDecor(this.nano);
        final Appendable dest = Mockito.mock(Appendable.class);
        final Formatter fmt = new Formatter(dest);
        decor.formatTo(fmt, this.flags, this.width, this.precision);
        Mockito.verify(dest).append(text);
    }

}
