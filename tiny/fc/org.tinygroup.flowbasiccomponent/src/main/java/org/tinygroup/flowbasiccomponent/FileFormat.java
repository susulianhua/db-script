package org.tinygroup.flowbasiccomponent;


public interface FileFormat {

    public String getType();

    public <T> String formatObject(T t);

    public Object formatFile(String filePath, String classPath);

    public String getEncoding();
}
