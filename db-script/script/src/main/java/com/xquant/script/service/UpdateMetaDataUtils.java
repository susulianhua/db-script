package com.xquant.script.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.procedure.Procedure;
import com.xquant.database.config.procedure.Procedures;
import com.xquant.database.config.trigger.Trigger;
import com.xquant.database.config.trigger.Triggers;
import com.xquant.database.config.view.View;
import com.xquant.database.config.view.Views;
import com.xquant.script.pojo.module.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class UpdateMetaDataUtils {
    public static void addTableInTable(File file, String tableName) throws  Exception{
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = db.parse(file);
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("table");

        Element element = document.createElement("table");
        element.setAttribute("id", tableName);
        root.appendChild(element);
        tf.transform(new DOMSource(document), new StreamResult(file));
    }

    public static void addProcedureInProcedure(File file, String procedureName){
       XStream xStream = new XStream();
       xStream.processAnnotations(Procedures.class);
       Procedures procedures = (Procedures) xStream.fromXML(file);
       Procedure procedure = new Procedure();
       procedure.setName(procedureName);
       procedures.getProcedureList().add(procedure);
       String xml = xStream.toXML(procedures);
       classToFile(xml, file);
    }

    public static void addViewInView(File file, String viewName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Views.class);
        Views views = (Views) xStream.fromXML(file);
        View view = new View();
        view.setId(viewName);
        views.getViewTableList().add(view);
        String xml = xStream.toXML(views);
        classToFile(xml, file);
    }

    public static void addTriggerInTrigger(File file, String triggerName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Triggers.class);
        Triggers triggers = (Triggers) xStream.fromXML(file);
        Trigger trigger = new Trigger();
        trigger.setName(triggerName);
        triggers.getTriggers().add(trigger);
        String xml = xStream.toXML(triggers);
        classToFile(xml, file);
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
        classToFile(xml, file);
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
        classToFile(xml, triggerFile);
    }

    public static void deleteProcedureInProcedure(File file, String procedureName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Procedures.class);
        Procedures procedures = (Procedures) xStream.fromXML(file);
        for(Procedure procedure: procedures.getProcedureList()){
            if(procedure.getName().equals(procedureName)){
                procedures.getProcedureList().remove(procedure);
                break;
            }
        }
        String xml = xStream.toXML(procedures);
        classToFile(xml, file);
    }

    public static void deleteTableInTable(File file, String tableName) throws  Exception{
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");

        //删除table.xml中对应表格
        Document document = db.parse(file);
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("table");
        for(int i = 0; i < nodeList.getLength(); i++){
            if(nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(tableName)){
                root.removeChild(nodeList.item(i));
                tf.transform(new DOMSource(document), new StreamResult(file));
                break;
            }
        }
    }

    public static void createJson(JSONArray jsonArrayTotal, Modules modules){
        for(Module module: modules.getModuleList()){
            JSONArray jsonArrayTable = new JSONArray();
            JSONArray jsonArrayProcedure = new JSONArray();
            JSONArray jsonArrayModule = new JSONArray();
            JSONArray jsonArrayTrigger = new JSONArray();
            JSONArray jsonArrayView = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            if(module.getTablelist()!= null){
                for(TableName tableName: module.getTablelist()){
                    JSONObject json = new JSONObject();
                    json.put("text",tableName.getName());
                    json.put("leaf", true);
                    jsonArrayTable.add(json);
                }
            }
            JSONObject jsonTable = new JSONObject();
            jsonTable.put("text","表");
            jsonTable.put("children",jsonArrayTable);
            jsonArrayModule.add(jsonTable);

            if(!CollectionUtils.isEmpty(module.getProcedureNameInModuleList())){
                for(ProcedureNameInModule procedureNameInModule: module.getProcedureNameInModuleList()){
                    JSONObject json = new JSONObject();
                    json.put("text", procedureNameInModule.getName());
                    json.put("leaf", true);
                    jsonArrayProcedure.add(json);
                }
            }
            JSONObject jsonProcedure = new JSONObject();
            jsonProcedure.put("text", "存储过程");
            jsonProcedure.put("children", jsonArrayProcedure);
            jsonArrayModule.add(jsonProcedure);

            if(!CollectionUtils.isEmpty(module.getViewNameList())){
                for(ViewName viewName: module.getViewNameList()){
                    JSONObject json = new JSONObject();
                    json.put("text", viewName.getName());
                    json.put("leaf", true);
                    jsonArrayView.add(json);
                }
            }
            JSONObject jsonView = new JSONObject();
            jsonView.put("text", "视图");
            jsonView.put("children", jsonArrayView);
            jsonArrayModule.add(jsonView);

            if(!CollectionUtils.isEmpty(module.getTriggerNameList())){
                for(TriggerName triggerName: module.getTriggerNameList()){
                    JSONObject json = new JSONObject();
                    json.put("text", triggerName.getName());
                    json.put("leaf", true);
                    jsonArrayTrigger.add(json);
                }
            }
            JSONObject jsonTrigger = new JSONObject();
            jsonTrigger.put("text", "触发器");
            jsonTrigger.put("children", jsonArrayTrigger);
            jsonArrayModule.add(jsonTrigger);

            if(StringUtils.isEmpty(module.getBusinessType())){
                JSONObject jsonBusinessType = new JSONObject();
                jsonBusinessType.put("text", "业务类型");
                jsonBusinessType.put("leaf", true);
                jsonArrayModule.add(jsonBusinessType);
            }
            if(module.getStandardfield() != null) {
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

    public static void classToFile(String xml, File file){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream, "utf-8");
            BufferedWriter writer = new BufferedWriter(osw);
            writer.write("");
            writer.write(xml);
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
