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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.LoggerFactory;

/**
 * Test case for {@link Logger}.
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Logger.class, LoggerFactory.class })
public final class LoggerTest {

    /**
     * The this.logger of SLF4J.
     */
    private transient org.slf4j.Logger logger;

    /**
     * Prepare this.logger factory.
     */
    @Before
    public void mockLoggerFactory() {
        PowerMockito.mockStatic(LoggerFactory.class);
        this.logger = Mockito.mock(org.slf4j.Logger.class);
        Mockito.doReturn(true).when(this.logger).isTraceEnabled();
        Mockito.doReturn(true).when(this.logger).isDebugEnabled();
        Mockito.when(LoggerFactory.getLogger(Mockito.any(Class.class)))
            .thenReturn(this.logger);
    }

    /**
     * Test it.
     * @throws Exception If something goes wrong
     */
    @Test
    public void testDetectionOfLogger() throws Exception {
        Logger.debug(this, "%[list]s, %d", new String[] {"foo"}, 1);
        Mockito.verify(this.logger).debug("[\"foo\"], 1");
    }

    /**
     * Test it.
     * @throws Exception If something goes wrong
     */
    @Test
    public void testDetectionOfStaticSource() throws Exception {
        Logger.info(LoggerTest.class, "sum: %.2f", 1d);
        Mockito.verify(this.logger).info("sum: 1.00");
    }

    /**
     * Test it.
     * @throws Exception If something goes wrong
     */
    @Test
    public void testSettingOfLoggingLevel() throws Exception {
        Logger.trace(this, "hello");
        Mockito.verify(this.logger).trace(Mockito.anyString());
        Logger.warn(this, "%s + %s", "alex", "mary");
        Mockito.verify(this.logger).warn("alex + mary");
        Logger.error(this, "%[type]s", "test");
        Mockito.verify(this.logger).error("java.lang.String");
    }

    /**
     * Test it.
     * @throws Exception If something goes wrong
     */
    @Test
    public void testIsTraceEnabledMethod() throws Exception {
        Mockito.when(this.logger.isTraceEnabled()).thenReturn(true);
        MatcherAssert.assertThat(
            Logger.isTraceEnabled(this),
            Matchers.is(true)
        );
        Mockito.verify(this.logger).isTraceEnabled();
    }

    /**
     * Test it.
     * @throws Exception If something goes wrong
     */
    @Test
    public void testIsDebugEnabledMethod() throws Exception {
        Mockito.when(this.logger.isDebugEnabled()).thenReturn(false);
        MatcherAssert.assertThat(
            Logger.isDebugEnabled(this),
            Matchers.is(false)
        );
        Mockito.verify(this.logger).isDebugEnabled();
    }

}
