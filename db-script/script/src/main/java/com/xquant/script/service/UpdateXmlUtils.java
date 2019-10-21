package com.xquant.script.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.xquant.script.pojo.module.Module;
import com.xquant.script.pojo.module.Modules;
import com.xquant.script.pojo.module.TableName;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UpdateXmlUtils {
    public static UpdateXmlUtils updateXmlUtils = new UpdateXmlUtils();

    public static void addModule(File file, String newModuleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        Module module = new Module();
        module.setId(newModuleName);
        modules.getModuleList().add(module);
        file.delete();
        String xml = xStream.toXML(modules);
        updateXmlUtils.modulesToFile(xml, file);
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
        updateXmlUtils.modulesToFile(xml, file);
    }

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

    public static void addTableInModule(File file, String tableName, String moduleName) throws Exception{
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = db.parse(file);
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("module");

        for(int i = 0; i < nodeList.getLength(); i++){
            if(nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(moduleName)){
                Element element = document.createElement("tablename");
                element.setAttribute("name",tableName);
                root.getElementsByTagName("module").item(i).appendChild(element);
                Transformer tf = TransformerFactory.newInstance().newTransformer();
                tf.setOutputProperty(OutputKeys.INDENT, "yes");
                tf.transform(new DOMSource(document), new StreamResult(file));
            }
        }
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

    public static void deleteTableInModule(File file,String tableName, String FileName) throws Exception{
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Document document = db.parse(file);
        Element root = document.getDocumentElement();

        NodeList moduleNodeList = root.getElementsByTagName("module");
        for(int i = 0; i <  moduleNodeList.getLength(); i++){
            if(moduleNodeList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(FileName)){
                Element cruModule = (Element) moduleNodeList.item(i);
                NodeList tableNodeList = cruModule.getElementsByTagName("tablename");
                for(int j = 0; j < tableNodeList.getLength(); j++){
                    if(tableNodeList.item(j).getAttributes().getNamedItem("name").getNodeValue().equals(tableName)){
                        moduleNodeList.item(i).removeChild(tableNodeList.item(j));
                    }
                }
                tf.transform(new DOMSource(document), new StreamResult(file));
                break;
            }
        }
    }

    public static void createJson(JSONArray jsonArrayTotal, Modules modules){
        int id = 1;
        for(Module module: modules.getModuleList()){
            JSONArray jsonArrayTable = new JSONArray();
            JSONArray jsonArrayModule = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            if(module.getTablelist()!= null){
                for(TableName tableName: module.getTablelist()){
                    JSONObject json = new JSONObject();
                    json.put("text",tableName.getName());
                    json.put("leaf", true);
                    json.put("id", id++);
                    jsonArrayTable.add(json);
                }
            }
            JSONObject jsonTable = new JSONObject();
            jsonTable.put("text","表");
            jsonTable.put("children",jsonArrayTable);
            jsonTable.put("id", id++);
            jsonArrayModule.add(jsonTable);
            if(module.getStandardfield() != null) {
                JSONObject jsonStdfield = new JSONObject();
                jsonStdfield.put("text", "标准字段");
                jsonStdfield.put("leaf", true);
                jsonStdfield.put("id", id++);
                jsonArrayModule.add(jsonStdfield);
            }
            if(module.getView() != null){
                JSONObject jsonView = new JSONObject();
                jsonView.put("text", "视图");
                jsonView.put("leaf", true);
                jsonView.put("id", id++);
                jsonArrayModule.add(jsonView);
            }
            if(module.getTrigger() != null){
                JSONObject jsonTrigger = new JSONObject();
                jsonTrigger.put("text", "触发器");
                jsonTrigger.put("leaf", true);
                jsonTrigger.put("id", id++);
                jsonArrayModule.add(jsonTrigger);
            }
            if(module.getProcedure() != null){
                JSONObject jsonProcedure = new JSONObject();
                jsonProcedure.put("text", "存储过程");
                jsonProcedure.put("leaf", true);
                jsonProcedure.put("id", id++);
                jsonArrayModule.add(jsonProcedure);
            }
            jsonObject.put("text","模块(" + module.getId() + ")");
            jsonObject.put("id", id++);
            jsonObject.put("children", jsonArrayModule);
            jsonArrayTotal.add(jsonObject);
        }
    }

    public static void addOtherInDetail(String moduleName, String curText, String filePath){
        filePath = filePath.substring(1, filePath.length() - 51);
        String otherFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + moduleName + "/" + moduleName + "." + curText + ".xml";
        System.out.println("otherFilePath: " + otherFilePath);
        File file = new File(otherFilePath);
        try {
            if(!file.exists()) file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
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
                else if(curText.equals("视图") && StringUtils.isEmpty(module.getView())){
                    module.setView(moduleName);
                    curText = "view";
                }
                else if(curText.equals("触发器") && StringUtils.isEmpty(module.getTrigger())){
                    module.setTrigger(moduleName);
                    curText = "trigger";
                }
                else if(curText.equals("序列") && StringUtils.isEmpty(module.getSequence())){
                    module.setTrigger(moduleName);
                    curText = "sequence";
                }
                else if(curText.equals("存储过程") && StringUtils.isEmpty(module.getProcedure())){
                    module.setSequence(moduleName);
                    curText = "procedure";
                }
            }
        }
        String xml = xStream.toXML(modules);
        updateXmlUtils.modulesToFile(xml, file);
        return curText;
    }

    public static void deleteOtherInDetail(String fileName, String curText, String filePath){
        filePath = filePath.substring(1,filePath.length() - 51);
        String otherFilePath = filePath + "/db-script/script/src/main/resources/xml/"
                + fileName + "/" + fileName + "." + curText + ".xml";
        System.out.println("otherFilePath:" + otherFilePath);
        File file = new File(otherFilePath);
        if(file.exists()){
            file.delete();
        }
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
                else if(curText.equals("视图")) {
                    module.setView(null);
                    curText = "view";
                }
                else if(curText.equals("存储过程")) {
                    module.setProcedure(null);
                    curText = "procedure";
                }
                else if(curText.equals("触发器")) {
                    module.setTrigger(null);
                    curText = "trigger";
                }
                else if(curText.equals("序列")) {
                    module.setSequence(null);
                    curText = "sequence";
                }
            }
        }
        String xml = xStream.toXML(modules);
        updateXmlUtils.modulesToFile(xml, file);
        return curText;
    }

    public static void modulesToFile(String xml, File file){
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write("");
            writer.write(xml);
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
