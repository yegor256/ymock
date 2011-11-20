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
import java.util.Formattable;
import java.util.Formatter;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link ObjectDecor}.
 * @author Marina Kosenko (marina.kosenko@gmail.com)
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public class ObjectDecorTest {

    /**
     * NULL should be formatted without problems.
     */
    @Test
    @org.junit.Ignore
    public final void testNullFormatting() {
        final Formattable decor = new ObjectDecor(null);
        final Formatter fmt = Mockito.mock(Formatter.class);
        decor.formatTo(fmt, 0, 1, 1);
        Mockito.verify(fmt).format("NULL");
    }

    /**
     * We get internal structure of an object and serialize it.
     */
    @Test
    @org.junit.Ignore
    public final void testFormatCollection() {
        final Formattable decor = new ObjectDecor(
            Arrays.asList(new Foo(1), new Foo(2))
        );
        final Formatter fmt = Mockito.mock(Formatter.class);
        decor.formatTo(fmt, 0, 1, 1);
        Mockito.verify(fmt).format("?");
    }

    private static final class Foo {
        /**
         * Internal field.
         */
        private final transient Integer field;
        /**
         * Public ctor.
         * @param val The value to set
         */
        public Foo(final Integer val) {
            this.field = val;
        }
        /**
         * Read field.
         * @return The value
         */
        public Integer getField() {
            return this.field;
        }
    }

}
