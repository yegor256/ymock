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

import com.ymock.util.formatter.FormatterManager;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.LoggerFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Yegor Bugayenko (yegor@ymock.com)
 * @version $Id$
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class, LoggerFactory.class})
public final class LoggerTest {

    @Before
    public void mockLoggerFactory() {
        PowerMockito.mockStatic(LoggerFactory.class);
    }

    @Test
    public void testDetectionOfLogger() throws Exception {
        final org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        when(LoggerFactory.getLogger(this.getClass())).thenReturn(logger);
        Logger.debug(this, "number %d", 1L);
        verify(logger).debug("number 1");
    }

    @Test
    public void testDetectionOfStaticSource() throws Exception {
        final org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        when(LoggerFactory.getLogger(this.getClass())).thenReturn(logger);
        Logger.info(LoggerTest.class, "sum: %.2f", 1d);
        verify(logger).info("sum: 1.00");
    }

    @Test
    public void testSettingOfLoggingLevel() throws Exception {
        final org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        when(LoggerFactory.getLogger(this.getClass())).thenReturn(logger);
        Logger.trace(this, "hello");
        verify(logger).trace(anyString());
        Logger.warn(this, "%s + %s", "alex", "mary");
        verify(logger).warn("alex + mary");
        Logger.error(this, "%d + %d", 1, 1);
        verify(logger).error("1 + 1");
    }

    @Test
    public void testIsTraceEnabledMethod() throws Exception {
        final org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        when(LoggerFactory.getLogger(this.getClass())).thenReturn(logger);
        when(logger.isTraceEnabled()).thenReturn(true);
        assertThat(Logger.isTraceEnabled(this), is(true));
        verify(logger).isTraceEnabled();
    }

    @Test
    public void testIsDebugEnabledMethod() throws Exception {
        final org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
        when(LoggerFactory.getLogger(this.getClass())).thenReturn(logger);
        when(logger.isDebugEnabled()).thenReturn(false);
        assertThat(Logger.isDebugEnabled(this), is(false));
        verify(logger).isDebugEnabled();
    }

    // @Test
    // public void testFormatting() throws Exception {
    //     final org.slf4j.Logger logger = mock(org.slf4j.Logger.class);
    //     when(FormatterManager.getInstance()).thenReturn(logger);
    //     Logger.trace(this, "hello");
    //     verify(logger).trace(anyString());
    // }

}
