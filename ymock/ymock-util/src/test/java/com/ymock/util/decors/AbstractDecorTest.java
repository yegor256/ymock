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
 * Abstract test case for all decors in the package.
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public abstract class AbstractDecorTest {

    /**
     * The value to test against.
     */
    private final Object object;

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
     * @param obj The object
     * @param txt Expected text
     * @param flgs Flags
     * @param wdt Width
     * @param prcs Precission
     */
    public AbstractDecorTest(final Object obj, final String txt,
        final int flgs, final int wdt, final int prcs) {
        this.object = obj;
        this.text = txt;
        this.flags = flgs;
        this.width = wdt;
        this.precision = prcs;
    }

    /**
     * Zero should be formatted without problems.
     * @throws Exception If some problem inside
     */
    @Test
    public void testDifferentFormats() throws Exception {
        final Formattable decor = this.decor();
        final Appendable dest = Mockito.mock(Appendable.class);
        final Formatter fmt = new Formatter(dest);
        decor.formatTo(fmt, this.flags, this.width, this.precision);
        Mockito.verify(dest).append(this.text);
    }

    /**
     * Get decor with the object.
     */
    protected abstract Formattable decor();

    /**
     * Get object under test.
     * @return The object
     */
    protected final Object object() {
        return this.object();
    }

}
