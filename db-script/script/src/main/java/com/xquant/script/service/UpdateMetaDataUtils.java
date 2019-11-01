package com.xquant.script.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.procedure.Procedure;
import com.xquant.database.config.procedure.Procedures;
import com.xquant.database.config.sequence.Sequence;
import com.xquant.database.config.sequence.Sequences;
import com.xquant.database.config.table.Table;
import com.xquant.database.config.table.Tables;
import com.xquant.database.config.trigger.Trigger;
import com.xquant.database.config.trigger.Triggers;
import com.xquant.database.config.view.View;
import com.xquant.database.config.view.Views;
import com.xquant.dialectfunction.DialectFunction;
import com.xquant.dialectfunction.DialectFunctions;
import com.xquant.metadata.config.bizdatatype.BusinessTypes;
import com.xquant.metadata.config.stdfield.StandardFields;
import com.xquant.script.pojo.module.*;
import com.xquant.script.pojo.module.ProcedureName;
import com.xquant.script.pojo.module.TableName;
import com.xquant.script.pojo.module.TriggerName;
import com.xquant.script.pojo.module.ViewName;
import org.apache.commons.lang.StringUtils;
import java.io.*;

public class UpdateMetaDataUtils {
    public static void addTableInTable(File file, String tableName){
        XStream xStream= new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = (Tables) xStream.fromXML(file);
        Table table = new Table();
        table.setId(tableName);
        tables.getTableList().add(table);
        String xml = xStream.toXML(tables);
        objectToFile(xml, file);
    }

    public static void addProcedureInProcedure(File file, String procedureName){
       XStream xStream = new XStream();
       xStream.processAnnotations(Procedures.class);
       Procedures procedures = (Procedures) xStream.fromXML(file);
       Procedure procedure = new Procedure();
       procedure.setName(procedureName);
       procedures.getProcedureList().add(procedure);
       String xml = xStream.toXML(procedures);
       objectToFile(xml, file);
    }

    public static void addViewInView(File file, String viewName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Views.class);
        Views views = (Views) xStream.fromXML(file);
        View view = new View();
        view.setId(viewName);
        views.getViewTableList().add(view);
        String xml = xStream.toXML(views);
        objectToFile(xml, file);
    }

    public static void addTriggerInTrigger(File file, String triggerName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Triggers.class);
        Triggers triggers = (Triggers) xStream.fromXML(file);
        Trigger trigger = new Trigger();
        trigger.setName(triggerName);
        triggers.getTriggers().add(trigger);
        String xml = xStream.toXML(triggers);
        objectToFile(xml, file);
    }

    public static void addSequenceInSequence(File file, String sequenceName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Sequences.class);
        Sequences sequences = (Sequences) xStream.fromXML(file);
        Sequence sequence = new Sequence();
        sequence.setName(sequenceName);
        sequences.getSequences().add(sequence);
        String xml = xStream.toXML(sequences);
        objectToFile(xml, file);
    }

    public static void addFunctionInFunction(File file, String functionName){
        XStream xStream = new XStream();
        xStream.processAnnotations(DialectFunctions.class);
        DialectFunctions dialectFunctions = (DialectFunctions) xStream.fromXML(file);
        DialectFunction dialectFunction = new DialectFunction();
        dialectFunction.setName(functionName);
        dialectFunctions.getFunctions().add(dialectFunction);
        String xml = xStream.toXML(dialectFunctions);
        objectToFile(xml, file);

    }

    public static void deleteViewInView(File file, String viewName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Views.class);
        Views views = (Views) xStream.fromXML(file);
        for(View view: views.getViewTableList()){
            if(view.getId().equals(viewName)){
                views.getViewTableList().remove(view);
                break;
            }
        }
        String xml = xStream.toXML(views);
        objectToFile(xml, file);
    }

    public static void deleteTriggerInTrigger(File triggerFile, String triggerName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Triggers.class);
        Triggers triggers = (Triggers) xStream.fromXML(triggerFile);
        for(Trigger trigger: triggers.getTriggers()){
            if(trigger.getName().equals(triggerName)){
                triggers.getTriggers().remove(trigger);
                break;
            }
        }
        String xml = xStream.toXML(triggers);
        objectToFile(xml, triggerFile);
    }

    public static void deleteSequenceInSequence(File sequenceFile, String sequenceName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Sequences.class);
        Sequences sequences = (Sequences) xStream.fromXML(sequenceFile);
        for(Sequence sequence: sequences.getSequences()){
            if(sequence.getName().equals(sequenceName)){
                sequences.getSequences().remove(sequence);
                break;
            }
        }
        String xml = xStream.toXML(sequences);
        objectToFile(xml, sequenceFile);
    }

    public static void deleteFunctionInFunction(File file, String functionName){
        XStream xStream = new XStream();
        xStream.processAnnotations(DialectFunctions.class);
        DialectFunctions dialectFunctions = (DialectFunctions) xStream.fromXML(file);
        for(DialectFunction dialectFunction: dialectFunctions.getFunctions()){
            if(dialectFunction.getName().equals(functionName)){
                dialectFunctions.getFunctions().remove(dialectFunction);
                break;
            }
        }
        String xml = xStream.toXML(dialectFunctions);
        objectToFile(xml, file);
    }

    public static void deleteProcedureInProcedure(File file, String procedureName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Procedures.class);
        Procedures procedures = (Procedures) xStream.fromXML(file);
        System.out.println(procedures.getProcedureList().size());
        for(Procedure procedure: procedures.getProcedureList()){
            if(procedure.getName().equals(procedureName)){
                procedures.getProcedureList().remove(procedure);
                break;
            }
        }
        String xml = xStream.toXML(procedures);
        objectToFile(xml, file);
    }

    public static void deleteTableInTable(File file, String tableName) throws  Exception{
        XStream xStream = new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = (Tables) xStream.fromXML(file);
        for(Table table: tables.getTableList()){
            if(table.getId().equals(tableName)){
                tables.getTableList().remove(table);
                break;
            }
        }
        String xml = xStream.toXML(tables);
        objectToFile(xml, file);
    }

    public static void createJson(JSONArray jsonArrayTotal, Modules modules){
        for(Module module: modules.getModuleList()){
            JSONArray jsonArrayTable = new JSONArray();
            JSONArray jsonArrayProcedure = new JSONArray();
            JSONArray jsonArrayModule = new JSONArray();
            JSONArray jsonArrayTrigger = new JSONArray();
            JSONArray jsonArrayView = new JSONArray();
            JSONArray jsonArraySequence = new JSONArray();
            JSONArray jsonArrayFunction = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            if(module.getTablelist()!= null){
                for(TableName tableName: module.getTablelist()){
                    JSONObject json = new JSONObject();
                    json.put("text",tableName.getName());
                    json.put("leaf", true);
                    jsonArrayTable.add(json);
                }
                JSONObject jsonTable = new JSONObject();
                jsonTable.put("text","表");
                jsonTable.put("children",jsonArrayTable);
                jsonArrayModule.add(jsonTable);
            }

            if(module.getProcedureNameList() != null){
                for(ProcedureName procedureName : module.getProcedureNameList()){
                    JSONObject json = new JSONObject();
                    json.put("text", procedureName.getName());
                    json.put("leaf", true);
                    jsonArrayProcedure.add(json);
                }
                JSONObject jsonProcedure = new JSONObject();
                jsonProcedure.put("text", "存储过程");
                jsonProcedure.put("children", jsonArrayProcedure);
                jsonArrayModule.add(jsonProcedure);

            }

            if(module.getViewNameList() != null){
                for(ViewName viewName: module.getViewNameList()){
                    JSONObject json = new JSONObject();
                    json.put("text", viewName.getName());
                    json.put("leaf", true);
                    jsonArrayView.add(json);
                }
                JSONObject jsonView = new JSONObject();
                jsonView.put("text", "视图");
                jsonView.put("children", jsonArrayView);
                jsonArrayModule.add(jsonView);
            }

            if(module.getTriggerNameList() != null){
                for(TriggerName triggerName: module.getTriggerNameList()){
                    JSONObject json = new JSONObject();
                    json.put("text", triggerName.getName());
                    json.put("leaf", true);
                    jsonArrayTrigger.add(json);
                }
                JSONObject jsonTrigger = new JSONObject();
                jsonTrigger.put("text", "触发器");
                jsonTrigger.put("children", jsonArrayTrigger);
                jsonArrayModule.add(jsonTrigger);
            }

            if(module.getSequenceNameList() != null){
                for(SequenceName sequenceName: module.getSequenceNameList()) {
                    JSONObject json = new JSONObject();
                    json.put("text", sequenceName.getName());
                    json.put("leaf", true);
                    jsonArraySequence.add(json);
                }
                JSONObject jsonSequence = new JSONObject();
                jsonSequence.put("text", "序列");
                jsonSequence.put("children", jsonArraySequence);
                jsonArrayModule.add(jsonSequence);
            }
            if(module.getFunctionNameList() != null){
                for(FunctionName functionName: module.getFunctionNameList()) {
                    JSONObject json = new JSONObject();
                    json.put("text", functionName.getName() );
                    json.put("leaf", true);
                    jsonArrayFunction.add(json);
                }
                JSONObject jsonFunction = new JSONObject();
                jsonFunction.put("text", "函数");
                jsonFunction.put("children", jsonArrayFunction);
                jsonArrayModule.add(jsonFunction);
            }

            if(!StringUtils.isEmpty(module.getBusinessType())){
                JSONObject jsonBusinessType = new JSONObject();
                jsonBusinessType.put("text", "业务类型");
                jsonBusinessType.put("leaf", true);
                jsonArrayModule.add(jsonBusinessType);
            }
            if(!StringUtils.isEmpty(module.getStandardfield())) {
                JSONObject jsonStdfield = new JSONObject();
                jsonStdfield.put("text", "标准字段");
                jsonStdfield.put("leaf", true);
                jsonArrayModule.add(jsonStdfield);
            }
            jsonObject.put("text","模块(" + module.getId() + ")");
            jsonObject.put("children", jsonArrayModule);
            jsonArrayTotal.add(jsonObject);
        }
    }

    public static void addOtherInDetail(String moduleName, String curText, String filePath){
        filePath = filePath.substring(1, filePath.length() - 51);
        String otherFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + "." + curText + ".xml";
        File file = new File(otherFilePath);
        try {
            if(!file.exists()) file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        XStream xStream = new XStream();
        if(curText.equals("stdfield")){
            xStream.processAnnotations(StandardFields.class);
            StandardFields standardFields = new StandardFields();
            standardFields.setPackageName(moduleName);
            String xml = xStream.toXML(standardFields);
            UpdateMetaDataUtils.objectToFile(xml, file);
        }
        else if(curText.equals("bizdatatype")){
            xStream.processAnnotations(BusinessTypes.class);
            BusinessTypes businessTypes = new BusinessTypes();
            businessTypes.setPackageName(moduleName);
            String xml = xStream.toXML(businessTypes);
            UpdateMetaDataUtils.objectToFile(xml, file);
        }
    }

    public static void createMetaDataXmlFile(String moduleName, String fileName, String filePath){
        addOtherInDetail(moduleName,fileName,filePath);
        filePath = filePath.substring(1, filePath.length() - 51);
        String otherFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + "." + fileName + ".xml";
        File file = new File(otherFilePath);
        Object object = getObjectTypeByFileName(fileName);
        XStream xStream = new XStream();
        xStream.processAnnotations(object.getClass());
        String xml = xStream.toXML(object);
        objectToFile(xml, file);
    }

    public static Object getObjectTypeByFileName(String fileName){
        if(fileName.equals("table")) return new Tables();
        else if(fileName.equals("view")) return new Views();
        else if(fileName.equals("procedure")) return new Procedures();
        else if(fileName.equals("sequence")) return new Sequences();
        else if(fileName.equals("trigger")) return new Triggers();
        else return new DialectFunctions();
    }

    public static void deleteOtherInDetail(String fileName, String curText, String filePath){
        filePath = filePath.substring(1,filePath.length() - 51);
        String otherFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + fileName + "/" + fileName + "." + curText + ".xml";
        File file = new File(otherFilePath);
        if(file.exists()){
            file.delete();
        }
    }

    public static void objectToFile(String xml, File file){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream, "utf-8");
            BufferedWriter writer = new BufferedWriter(osw);
            writer.write("");
            writer.write(xml);
            writer.flush();
            writer.close();
        }catch (IOException e){
            System.out.println("hhhhhh");
            e.printStackTrace();
        }
    }


}
