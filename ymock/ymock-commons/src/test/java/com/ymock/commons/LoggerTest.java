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
package com.ymock.commons;

import org.junit.*;
import static org.junit.Assert.*;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;

public class LoggerTest {

    private static class MockAppender extends AppenderSkeleton {
        public LoggingEvent event;
        @Override
        public void append(LoggingEvent evt) {
            this.event = evt;
        }
        @Override
        public boolean requiresLayout() {
            return false;
        }
        @Override
        public void close() {
        }
        @Override
        public Level getThreshold() {
            return Level.ALL;
        }
    }

    private MockAppender appender;

    private Level savedLevel;

    @Before
    public void attachAppender() {
        this.appender = new MockAppender();
        org.apache.log4j.Logger.getRootLogger().addAppender(this.appender);
        this.savedLevel = org.apache.log4j.Logger.getLogger("com.ymock.commons").getLevel();
        org.apache.log4j.Logger.getLogger("com.ymock.commons").setLevel(Level.TRACE);
    }

    @After
    public void dettachAppender() {
        org.apache.log4j.Logger.getRootLogger().removeAppender(this.appender);
        this.appender = null;
        org.apache.log4j.Logger.getLogger("com.ymock.commons").setLevel(this.savedLevel);
    }

    @Test
    public void testValidatesCorrectDetectionOfLogger() throws Exception {
        Logger.debug(this, "test message, ignore it");
        assertEquals("com.ymock.commons.LoggerTest", this.appender.event.getLoggerName());
    }

    @Test
    public void testValidatesCorrectPassingOfMessageToLogger() throws Exception {
        String msg = "ignore this message";
        Logger.debug(this, msg);
        assertTrue(((String) this.appender.event.getMessage()).contains(msg));
    }

    @Test
    public void testValidatesCorrectPassingOfMessageToLoggerWithStaticSource() throws Exception {
        String msg = "ignore this message";
        Logger.debug(LoggerTest.class, msg);
        assertTrue(((String) this.appender.event.getMessage()).contains(msg));
    }

    @Test
    public void testValidatesCorrectLoggingLevel() throws Exception {
        String msg = "ignore this message";
        Logger.debug(this, msg);
        assertEquals(Level.DEBUG, this.appender.event.getLevel());
        Logger.info(this, msg);
        assertEquals(Level.INFO, this.appender.event.getLevel());
        Logger.warn(this, msg);
        assertEquals(Level.WARN, this.appender.event.getLevel());
        Logger.error(this, msg);
        assertEquals(Level.ERROR, this.appender.event.getLevel());
        Logger.trace(this, msg);
        assertEquals(Level.TRACE, this.appender.event.getLevel());
    }

    @Test
    public void testExperimentsWithVarArgs() throws Exception {
        Logger.debug(this, "Log testing: we found %d files", 5);
        assertTrue(((String) this.appender.event.getMessage()).contains("5 files"));
        Logger.debug(
            this,
            "Log testing: my name is '%s', I'm %d years old",
            "John Doe",
            55
        );
        assertTrue(((String) this.appender.event.getMessage()).contains("55 years old"));
    }

    @Test
    public void testIsDebugEnabledMethod() throws Exception {
        String pkg = "com.ymock.commons";
        Level level = org.apache.log4j.Logger.getLogger(pkg).getLevel();
        org.apache.log4j.Logger.getLogger(pkg).setLevel(Level.DEBUG);
        boolean enabled = Logger.isDebugEnabled(this);
        org.apache.log4j.Logger.getLogger(pkg).setLevel(level);
        assertTrue("Should be TRUE", enabled);
    }

    @Test
    public void testIsTraceEnabledMethod() throws Exception {
        String pkg = "com.ymock.commons";
        Level level = org.apache.log4j.Logger.getLogger(pkg).getLevel();
        org.apache.log4j.Logger.getLogger(pkg).setLevel(Level.TRACE);
        boolean enabled = Logger.isTraceEnabled(this);
        org.apache.log4j.Logger.getLogger(pkg).setLevel(level);
        assertTrue("Should be TRUE", enabled);
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
        assertEquals("com.ymock.commons.LoggerTest$InnerClass", this.appender.event.getLoggerName());
    }

}
