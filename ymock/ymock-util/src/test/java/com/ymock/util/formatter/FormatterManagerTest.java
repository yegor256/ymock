package com.ymock.util.formatter;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

public class FormatterManagerTest extends TestCase {
    private FormatterManager formatterManager;

    @Before
    protected void setUp() throws Exception {
        formatterManager = FormatterManager.getInstance();
    }

    @Test
    public void testFormat() throws Exception {
        String s = formatterManager.fmt("group.format", "aaa");
        assertEquals(s, "aaaformatted");
    }

    @Test
    public void testFormatFormatterDoesntExist() throws Exception {
        String s = formatterManager.fmt("group.aaa", "aaa");
        assertEquals(s, "aaa");
    }

}
