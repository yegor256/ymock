package com.ymock.util.formatter;

import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expect;

public class FormatterManagerTest extends TestCase {
    private IMocksControl control;
    private FormatterManager formatterManager;

    @Before
    protected void setUp() throws Exception {
        control = createControl();
        formatterManager = FormatterManager.getInstance();
    }

    @Test
    public void testRegisterFormatter() throws Exception {
        Formatter formatter = control.createMock(Formatter.class);

        formatterManager.registerFormatter("key", formatter);

        expect(formatter.format("aaa")).andReturn("bbb");

        control.replay();
        String s = formatterManager.format("key", "aaa");
        control.verify();

        assertEquals(s, "bbb");
    }

    @Test
    public void testUnregisterFormatter() throws Exception {
        Formatter formatter = control.createMock(Formatter.class);

        formatterManager.registerFormatter("key", formatter);
        formatterManager.unregisterFormatter("key");

        String s = formatterManager.format("key", "aaa");

        assertEquals(s, "aaa");
    }

    @Test
    public void testFormat() throws Exception {
        Formatter formatter = control.createMock(Formatter.class);

        formatterManager.registerFormatter("key", formatter);

        expect(formatter.format("aaa")).andReturn("bbb");

        control.replay();
        String s = formatterManager.format("key", "aaa");
        control.verify();

        assertEquals(s, "bbb");
    }

    @Test
    public void testFormatFormatterDoesntExist() throws Exception {
        String s = formatterManager.format("key", "aaa");
        assertEquals(s, "aaa");
    }

    @After
    public void tearDown(){
        formatterManager.unregisterFormatter("key");
    }
}
