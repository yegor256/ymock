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
package com.ymock.util;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link RuntimeProblem}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class RuntimeProblemTest {

    /**
     * Simple text used in all test methods.
     */
    private static final String REASON = "some text";

    /**
     * Test it.
     */
    @Test
    public void testValidatesExceptionClass() {
        final Throwable ex = RuntimeProblem.make(this.REASON);
        MatcherAssert.assertThat(
            ex,
            Matchers.instanceOf(RuntimeProblem.class)
        );
        MatcherAssert.assertThat(
            ex.getMessage(),
            Matchers.containsString(this.REASON)
        );
    }

    /**
     * Test it.
     */
    @Test
    public void testValidatesExceptionClassViaThrowableCause() {
        final Throwable ex = RuntimeProblem.make(
            new IllegalStateException(this.REASON)
        );
        MatcherAssert.assertThat(
            ex,
            Matchers.instanceOf(RuntimeProblem.class)
        );
        MatcherAssert.assertThat(
            ex.getMessage(),
            Matchers.containsString(this.REASON)
        );
    }

    /**
     * Test it.
     */
    @Test
    public void testValidatesExceptionClassViaThrowableCauseAndString() {
        final Throwable ex = RuntimeProblem.make(
            this.REASON,
            new IllegalStateException()
        );
        MatcherAssert.assertThat(
            ex,
            Matchers.instanceOf(RuntimeProblem.class)
        );
        MatcherAssert.assertThat(
            ex.getMessage(),
            Matchers.containsString(this.REASON)
        );
    }

    /**
     * Test it.
     */
    @Test
    public void testValidatesExceptionClassViaVarargString() {
        final Throwable ex = RuntimeProblem.make("number %d", 3);
        MatcherAssert.assertThat(
            ex,
            Matchers.instanceOf(RuntimeProblem.class)
        );
        MatcherAssert.assertThat(
            ex.getMessage(),
            Matchers.equalTo("number 3")
        );
    }

    /**
     * Trying to create an object with incorrect arguments.
     * @todo #24:1 The test doesn't work because functionality is not
     *  implemented yet. We should implement proper exception
     *  catching inside RuntimeProblem class. This test method
     *  should be enabled
     *  as soon as the functionality is implemented. It should NOT
     *  be altered, just enabled (Ignore annotation to be removed).
     */
    @org.junit.Ignore
    @Test
    public void testProblemCreationWithInvalidArguments() {
        MatcherAssert.assertThat(
            RuntimeProblem.make("data %d").getMessage(),
            Matchers.equalTo(
                // @checkstyle LineLength (1 line)
                "number %d (java.util.MissingFormatArgumentException: Format specifier 'd')"
            )
        );
        MatcherAssert.assertThat(
            RuntimeProblem.make("num %d", "test").getMessage(),
            Matchers.equalTo(
                // @checkstyle LineLength (1 line)
                "num %d (java.util.IllegalFormatConversionException: d != java.lang.String)"
            )
        );
        MatcherAssert.assertThat(
            RuntimeProblem.make("number %1z", "text").getMessage(),
            Matchers.equalTo(
                // @checkstyle LineLength (1 line)
                "number %1z (java.util.UnknownFormatConversionException: Conversion = 'z')"
            )
        );
        final Object obj = Mockito.mock(Object.class);
        Mockito.doThrow(new IllegalArgumentException("oops.."))
            .when(obj).toString();
        MatcherAssert.assertThat(
            RuntimeProblem.make("text %s", obj).getMessage(),
            Matchers.equalTo(
                "number %s (java.lang.IllegalArgumentException: oops..)"
            )
        );
    }

}
