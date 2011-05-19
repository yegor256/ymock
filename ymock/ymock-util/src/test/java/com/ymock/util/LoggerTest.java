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

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public final class LoggerTest {

    private static class MockAppender extends AppenderSkeleton {
        private LoggingEvent event;
        @Override
        public void append(final LoggingEvent evt) {
            this.event = evt;
        }
        @Override
        public boolean requiresLayout() {
            return false;
        }
        @Override
        public void close() {
            // intentionally empty
        }
        @Override
        public Level getThreshold() {
            return Level.ALL;
        }
    }

    private static final String PACKAGE = "com.ymock.util";

    private static final String MESSAGE = "test message, ignore it";

    private MockAppender appender;

    private Level saved;

    @Before
    public void attachAppender() {
        this.appender = new MockAppender();
        org.apache.log4j.Logger.getRootLogger().addAppender(this.appender);
        this.saved = org.apache.log4j.Logger.getLogger(this.PACKAGE).getLevel();
        org.apache.log4j.Logger.getLogger(this.PACKAGE).setLevel(Level.TRACE);
    }

    @After
    public void dettachAppender() {
        org.apache.log4j.Logger.getRootLogger().removeAppender(this.appender);
        this.appender = null;
        org.apache.log4j.Logger.getLogger(this.PACKAGE).setLevel(this.saved);
    }

    @Test
    public void testValidatesCorrectDetectionOfLogger() throws Exception {
        Logger.debug(this, this.MESSAGE);
        assertThat(
            this.getClass().getName(),
            equalTo(this.appender.event.getLoggerName())
        );
    }

    @Test
    public void testValidatesCorrectPassingOfMessageToLogger()
        throws Exception {
        Logger.debug(this, this.MESSAGE);
        assertThat(
            (String) this.appender.event.getMessage(),
            containsString(this.MESSAGE)
        );
    }

    @Test
    public void testValidatesCorrectPassingOfMessageToLoggerWithStaticSource()
        throws Exception {
        Logger.debug(LoggerTest.class, this.MESSAGE);
        assertThat(
            (String) this.appender.event.getMessage(),
            containsString(this.MESSAGE)
        );
    }

    @Test
    public void testValidatesCorrectLoggingLevel() throws Exception {
        Logger.debug(this, this.MESSAGE);
        assertThat(Level.DEBUG, equalTo(this.appender.event.getLevel()));
        Logger.info(this, this.MESSAGE);
        assertThat(Level.INFO, equalTo(this.appender.event.getLevel()));
        Logger.warn(this, this.MESSAGE);
        assertThat(Level.WARN, equalTo(this.appender.event.getLevel()));
        Logger.error(this, this.MESSAGE);
        assertThat(Level.ERROR, equalTo(this.appender.event.getLevel()));
        Logger.trace(this, this.MESSAGE);
        assertThat(Level.TRACE, equalTo(this.appender.event.getLevel()));
    }

    @Test
    public void testExperimentsWithVarArgs() throws Exception {
        Logger.debug(this, "Log testing: we found %d file", 1);
        assertThat(
            (String) this.appender.event.getMessage(),
            containsString("1 file")
        );
        Logger.debug(
            this,
            "Log testing: my name is '%s', I have %d son",
            "John Doe",
            1
        );
        assertThat(
            (String) this.appender.event.getMessage(),
            containsString("1 son")
        );
    }

    @Test
    public void testIsDebugEnabledMethod() throws Exception {
        final Level level = org.apache.log4j.Logger.getLogger(this.PACKAGE)
            .getLevel();
        org.apache.log4j.Logger.getLogger(this.PACKAGE).setLevel(Level.DEBUG);
        final boolean enabled = Logger.isDebugEnabled(this);
        org.apache.log4j.Logger.getLogger(this.PACKAGE).setLevel(level);
        assertThat(enabled, is(true));
    }

    @Test
    public void testIsTraceEnabledMethod() throws Exception {
        final Level level = org.apache.log4j.Logger.getLogger(this.PACKAGE)
            .getLevel();
        org.apache.log4j.Logger.getLogger(this.PACKAGE).setLevel(Level.TRACE);
        final boolean enabled = Logger.isTraceEnabled(this);
        org.apache.log4j.Logger.getLogger(this.PACKAGE).setLevel(level);
        assertThat(enabled, is(true));
    }

    private static class InnerClass {
        public void log() {
            this.innerLog();
        }
        private void innerLog() {
            Logger.info(this, "Inner log message");
        }
    }

    @Test
    public void testSendsLogMessageFromNestedClass() throws Exception {
        new InnerClass().log();
        assertThat(
            this.PACKAGE + ".LoggerTest$InnerClass",
            equalTo(this.appender.event.getLoggerName())
        );
    }

}
