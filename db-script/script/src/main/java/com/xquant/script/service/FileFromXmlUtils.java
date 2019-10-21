package com.xquant.script.service;

import java.io.File;

public  class FileFromXmlUtils {
    private static FileFromXmlUtils fileFromXmlUtils = new FileFromXmlUtils();
    public static FileFromXmlUtils getFileFromXmlUtils(){
        return fileFromXmlUtils;
    }

    public static File getTableFile(String FileName, String filePath){
        filePath = filePath.substring(1,filePath.length() - 51);
        String tableFilePath = filePath + "/db-script/script/src/main/resources/xml/" + FileName + "/" + FileName + ".table.xml";
        File file = new File(tableFilePath);
        return file;
    }

    public static File getModuleFile(String filePath){
        filePath = filePath.substring(1,filePath.length() - 51);
        filePath = filePath + "/db-script/script/src/main/resources/xml/module.xml";
        File file = new File(filePath);
        return file;
    }

    public static File getModuleDirFile(String filePath, String moduleName){
        filePath = filePath.substring(1,filePath.length() - 51);
        filePath = filePath + "/db-script/script/src/main/resources/xml/" + moduleName;
        File file = new File(filePath);
        return file;
    }

}
