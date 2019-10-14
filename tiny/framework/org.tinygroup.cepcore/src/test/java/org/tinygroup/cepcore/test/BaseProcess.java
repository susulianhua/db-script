package org.tinygroup.cepcore.test;

public class BaseProcess {
    private static String value = "";

    public static String getValue() {
        String result = value;
        value = "";
        return result;
    }

    protected void addValue(String index) {
        value = value + index;
    }
}
