package com.xquant.script.service;

import java.io.File;

public  class GetCorrespondFileUtils {
    private static GetCorrespondFileUtils getCorrespondFileUtils = new GetCorrespondFileUtils();
    public static GetCorrespondFileUtils getGetCorrespondFileUtils(){
        return getCorrespondFileUtils;
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
        String procedureFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + ".procedure.xml";
        File file = new File(procedureFilePath);
        return file;
    }

    public static File getViewFile(String moduleName, String filePath){
        filePath = filePath.substring(1, filePath.length() - 51);
        String viewFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + ".view.xml";
        File file = new File(viewFilePath);
        return file;
    }

    public static File getTriggerFile(String moduleName, String filePath){
        filePath = filePath.substring(1, filePath.length() - 51);
        String viewFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + ".trigger.xml";
        File file = new File(viewFilePath);
        return file;
    }

    public static File getSequenceFile(String moduleName, String filePath){
        filePath = filePath.substring(1, filePath.length() - 51);
        String sequenceFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + ".sequence.xml";
        File file = new File(sequenceFilePath);
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

    public static String transformMetadataToEnglish(String curText){
        if(curText.equals("表")) return "table";
        else if(curText.equals("视图")) return "view";
        else if(curText.equals("序列")) return "sequence";
        else if(curText.equals("触发器")) return "trigger";
        else  return "procedure";
    }

}
