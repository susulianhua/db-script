package org.tinygroup.flowbasiccomponent.test;

import org.tinygroup.flowbasiccomponent.CodeConvert;

public class DemoCodeConvert implements CodeConvert {

    public String convert(String source) {
        if ("00001".equals(source)) {
            return "90001";
        } else if ("00002".equals(source)) {
            return "90002";
        } else {
            return "90003";
        }
    }

    public String getType() {
        return "demo";
    }

}
