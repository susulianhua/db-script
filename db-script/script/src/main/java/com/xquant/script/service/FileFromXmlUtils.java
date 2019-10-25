package com.xquant.script.service;

import java.io.File;
import java.io.FileOutputStream;

public  class FileFromXmlUtils {
    private static FileFromXmlUtils fileFromXmlUtils = new FileFromXmlUtils();
    public static FileFromXmlUtils getFileFromXmlUtils(){
        return fileFromXmlUtils;
    }

    public static File getTableFile(String moduleName, String filePath){
        filePath = filePath.substring(1,filePath.length() - 51);
        String tableFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + ".table.xml";
        File file = new File(tableFilePath);
        return file;
    }

    public static File getStandardFieldFile(String moduleName, String filePath){
        filePath = filePath.substring(1,filePath.length() - 51);
        String standardFieldFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + ".stdfield.xml";
        File file = new File(standardFieldFilePath);
        return file;
    }

    public static File getBusinessTypeFile(String moduleName, String filePath){
        filePath = filePath.substring(1, filePath.length() - 51);
        String businessTypeFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + ".bizdatatype.xml";
        File file = new File(businessTypeFilePath);
        return file;
    }

    public static File getProcedureFile(String moduleName, String filePath){
        filePath = filePath.substring(1, filePath.length() - 51);
        String procedureFilePath = filePath + "/db-script/script/src/main/resourcces/xml"
                + moduleName + "/" + moduleName + ".procedure.xml";
        File file = new File(procedureFilePath);
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
