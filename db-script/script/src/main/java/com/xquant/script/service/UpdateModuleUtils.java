package com.xquant.script.service;

import com.thoughtworks.xstream.XStream;
import com.xquant.dialectfunction.DialectFunctions;
import com.xquant.script.pojo.module.*;
import com.xquant.script.pojo.module.ProcedureName;
import com.xquant.script.pojo.module.TriggerName;
import com.xquant.script.pojo.module.ViewName;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;

public class UpdateModuleUtils {
    public static UpdateMetaDataUtils updateMetaDataUtils = new UpdateMetaDataUtils();

    public static void addModule(File file, String newModuleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        Module module = new Module();
        module.setId(newModuleName);
        modules.getModuleList().add(module);
        file.delete();
        String xml = xStream.toXML(modules);
        updateMetaDataUtils.objectToFile(xml, file);
    }

    public static void deleteModule(File file, String moduleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                modules.getModuleList().remove(module);
                break;
            }
        }
        file.delete();
        String xml = xStream.toXML(modules);
        updateMetaDataUtils.objectToFile(xml, file);
    }

    public static void addTableInModule(File file, String tableName, String moduleName) throws Exception{
       XStream xStream = new XStream();
       xStream.processAnnotations(Modules.class);
       Modules modules = (Modules) xStream.fromXML(file);
       for(Module module: modules.getModuleList()){
           if(module.getId().equals(moduleName)){
               TableName tableName1 = new TableName();
               tableName1.setName(tableName);
               module.getTablelist().add(tableName1);
               break;
           }
       }
       String xml = xStream.toXML(modules);
       UpdateMetaDataUtils.objectToFile(xml, file);
    }

    public static void addProcedureInModule(File file, String procedureName, String moduleName){
        ProcedureName procedureNameInModule = new ProcedureName();
        procedureNameInModule.setName(procedureName);
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                module.getProcedureNameList().add(procedureNameInModule);
                break;
            };
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, file);
    }

    public static void addViewInModule(File file, String viewName, String moduleName){
        ViewName viewName1 = new ViewName();
        viewName1.setName(viewName);
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                module.getViewNameList().add(viewName1);
                break;
            };
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, file);
    }

    public static void addTriggerInModule(File moduleFile, String triggerName,String moduleName){
        TriggerName triggerName1 = new TriggerName();
        triggerName1.setName(triggerName);
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(moduleFile);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                module.getTriggerNameList().add(triggerName1);
                break;
            };
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, moduleFile);
    }

    public static void addSequenceInModule(File moduleFile,String sequenceName, String moduleName){
        SequenceName sequenceName1 = new SequenceName();
        sequenceName1.setName(sequenceName);
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(moduleFile);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                module.getSequenceNameList().add(sequenceName1);
                break;
            };
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, moduleFile);
    }

    public static void addFunctionInModule(File moduleFile,String functionName, String moduleName){
        FunctionName functionName1 = new FunctionName();
        functionName1.setName(functionName);
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(moduleFile);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                module.getFunctionNameList().add(functionName1);
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, moduleFile);
    }

    public static void deleteTableInModule(File file,String tableName, String moduleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                for(TableName table: module.getTablelist()){
                    if(table.getName().equals(tableName)){
                        module.getTablelist().remove(table);
                        break;
                    }
                }
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, file);
    }

    public static void deleteProcedureInModule(File file, String procedureName, String moduleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                for(ProcedureName procedure: module.getProcedureNameList()){
                    if(procedure.getName().equals(procedureName)){
                        module.getProcedureNameList().remove(procedure);
                        break;
                    }
                }
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, file);
    }

    public static void deleteViewInModule(File file, String viewName, String moduleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                for(ViewName viewName1: module.getViewNameList()){
                    if(viewName1.getName().equals(viewName)){
                        module.getViewNameList().remove(viewName1);
                        break;
                    }
                }
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, file);
    }

    public static void deleteTriggerInModule(File moduleFile, String triggerName, String moduleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(moduleFile);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName) && !CollectionUtils.isEmpty(module.getTriggerNameList())){
                for(TriggerName triggerName1: module.getTriggerNameList()){
                    if(triggerName1.getName().equals(triggerName)){
                        module.getTriggerNameList().remove(triggerName1);
                        break;
                    }
                }
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, moduleFile);
    }

    public static void deleteSequenceInModule(File moduleFile, String sequenceName, String moduleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(moduleFile);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName) && !CollectionUtils.isEmpty(module.getSequenceNameList())){
                for(SequenceName sequenceName1: module.getSequenceNameList()){
                    if(sequenceName1.getName().equals(sequenceName)){
                        module.getSequenceNameList().remove(sequenceName1);
                        break;
                    }
                }
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, moduleFile);
    }

    public static void deleteFunctionInModule(File moduleFile, String functionName, String moduleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(moduleFile);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName) && !CollectionUtils.isEmpty(module.getFunctionNameList())){
                for(FunctionName functionName1: module.getFunctionNameList()){
                    if(functionName1.getName().equals(functionName)){
                        module.getFunctionNameList().remove(functionName1);
                        break;
                    }
                }
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, moduleFile);
    }

    public static String  addOtherInModule(File file, String curText, String moduleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                if(curText.equals("标准字段") && StringUtils.isEmpty(module.getStandardfield())) {
                    module.setStandardfield(moduleName);
                    curText = "stdfield";
                }
                else if(curText.equals("业务类型") && StringUtils.isEmpty(module.getBusinessType())){
                    module.setBusinessType(moduleName);
                    curText = "bizdatatype";
                }
            }
        }
        String xml = xStream.toXML(modules);
        updateMetaDataUtils.objectToFile(xml, file);
        return curText;
    }

    public static String deleteOtherInModule(File file, String curText, String fileName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        xStream.autodetectAnnotations(true);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(fileName)){
                if(curText.equals("标准字段")) {
                    module.setStandardfield(null);
                    curText = "stdfield";
                }
                else if(curText.equals("业务类型")) {
                    module.setBusinessType(null);
                    curText = "bizdatatype";
                }
            }
        }
        String xml = xStream.toXML(modules);
        updateMetaDataUtils.objectToFile(xml, file);
        return curText;
    }
}

