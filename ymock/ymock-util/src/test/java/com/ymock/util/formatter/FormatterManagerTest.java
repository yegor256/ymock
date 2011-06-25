package com.ymock.util.formatter;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class FormatterManagerTest extends TestCase {
    private FormatterManager formatterManager;

    @Before
    protected void setUp() throws Exception {
        formatterManager = FormatterManager.getInstance();
    }

    @Test
    public void testFormat() throws Exception {
        String s = formatterManager.format("testFormatterKey", "aaa");
        assertEquals(s, "aaaformatted");
    }

    @Test
    public void testFormatFormatterDoesntExist() throws Exception {
        String s = formatterManager.format("notExistantKey", "aaa");
        assertEquals(s, "aaa");
    }

}
