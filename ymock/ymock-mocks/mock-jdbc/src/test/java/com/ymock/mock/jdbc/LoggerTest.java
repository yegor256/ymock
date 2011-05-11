/**
 * @version $Id$
 */
package com.ymock;

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
        this.savedLevel = org.apache.log4j.Logger.getLogger("com.ymock").getLevel();
        org.apache.log4j.Logger.getLogger("com.ymock").setLevel(Level.TRACE);
    }

    @After
    public void dettachAppender() {
        org.apache.log4j.Logger.getRootLogger().removeAppender(this.appender);
        this.appender = null;
        org.apache.log4j.Logger.getLogger("com.ymock").setLevel(this.savedLevel);
    }

    @Test
    public void testValidatesCorrectDetectionOfLogger() throws Exception {
        Logger.debug(this, "test message, ignore it");
        assertEquals("com.ymock.LoggerTest", this.appender.event.getLoggerName());
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
        String pkg = "com.ymock";
        Level level = org.apache.log4j.Logger.getLogger(pkg).getLevel();
        org.apache.log4j.Logger.getLogger(pkg).setLevel(Level.DEBUG);
        boolean enabled = Logger.isDebugEnabled(this);
        org.apache.log4j.Logger.getLogger(pkg).setLevel(level);
        assertTrue("Should be TRUE", enabled);
    }

    @Test
    public void testIsTraceEnabledMethod() throws Exception {
        String pkg = "com.ymock";
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
        assertEquals("com.ymock.LoggerTest$InnerClass", this.appender.event.getLoggerName());
    }

}
