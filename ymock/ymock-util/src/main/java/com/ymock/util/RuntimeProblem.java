/**
 * Copyright (c) 2011-2012, yMock.com
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

/**
 * Global creator of {@link RuntimeException} instances.
 *
 * <p>Use it like this in any class, and in any package:
 *
 * <pre>
 * package com.ymock.XXX;
 * import com.ymock.util.RuntimeProblem;
 * public class MyClass {
 *   public void foo(Integer num) {
 *     if (num > 2) {
 *       throw RuntimeProblem.make("%d is bigger than 2", num);
 *     }
 *   }
 * }
 * [...]
 * </pre>
 *
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
public final class RuntimeProblem extends RuntimeException {

    /**
     * Private ctor. It is private in order to avoid
     * a possibility to instantiate the class directly,
     * outside of factory method {@link #raise()}.
     * @param message The exception message
     * @see #make(String)
     */
    private RuntimeProblem(final String message) {
        super(message);
    }

    /**
     * Private ctor. It is private in order to avoid
     * a possibility to instantiate the class directly,
     * outside of factory method {@link #raise()}.
     * @param cause Previous problem
     * @see #make(Throwable)
     */
    private RuntimeProblem(final Throwable cause) {
        super(cause);
    }

    /**
     * Private ctor. It is private in order to avoid
     * a possibility to instantiate the class directly,
     * outside of factory method {@link #raise()}.
     * @param message The exception message
     * @param cause Previous problem
     * @see #make(String,Throwable)
     */
    private RuntimeProblem(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a custom exception class, using the params provided.
     * @param message The exception message
     * @param args Optional arguments for <tt>System.fmt()</tt>
     * @return The problem created
     */
    public static RuntimeProblem make(final String message,
        final Object... args) {
        return new RuntimeProblem(Logger.format(message, args));
    }

    /**
     * Create a custom exception class, using the params provided.
     * @param cause Previous problem
     * @return The problem created
     */
    public static RuntimeProblem make(final Throwable cause) {
        return new RuntimeProblem(cause);
    }

    /**
     * Create a custom exception class, using the params provided.
     * @param cause Previous problem
     * @param message The exception message
     * @param args Optional arguments for <tt>System.fmt()</tt>
     * @return The problem created
     */
    public static RuntimeProblem make(final Throwable cause,
        final String message, final Object... args) {
        return new RuntimeProblem(Logger.format(message, args), cause);
    }

}
