package com.ymock.util.formatter;

@FormatGroup("group")
public class TestFormatter{

    @Format("format")
    public String format(String s) {
        return s.toString()+"formatted";
    }

}
