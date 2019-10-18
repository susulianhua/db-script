package com.xquant.script.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.module.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/tree")
public class TreeController {

    @RequestMapping("/module")
    @ResponseBody
    public  NormalResponse modul(HttpServletRequest request){
        int id = 1;
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        filePath = filePath.substring(1,filePath.length() - 51);
        filePath = filePath + "/db-script/script/src/main/resources/xml/module.xml";
        File file = new File(filePath);
        XStream xstream = new XStream();
        xstream.processAnnotations(Modules.class);
        Modules modules = (Modules) xstream.fromXML(file);
        JSONObject jsontotal = new JSONObject();
        JSONArray jsonArraytotal = new JSONArray();
        for(Module module: modules.getModuleList()){
            JSONArray jsonArraytable = new JSONArray();
            JSONArray jsonArraymodule = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            if(module.getTablelist()!= null){
                for(TableName tableName: module.getTablelist()){
                    JSONObject json = new JSONObject();
                    json.put("text",tableName.getName());
                    json.put("leaf", true);
                    json.put("id", id++);
                    jsonArraytable.add(json);
                }
                JSONObject jsontable = new JSONObject();
                jsontable.put("text","表");
                jsontable.put("children",jsonArraytable);
                jsontable.put("id", id++);
                jsonArraymodule.add(jsontable);
            }else{
                JSONObject jsontable = new JSONObject();
                jsontable.put("text", "表");
                jsontable.put("leaf", false);
                jsontable.put("id", id++);
                jsonArraymodule.add(jsontable);
            }
            if(module.getStandardfield() != null) {
                JSONObject jsonstdfield = new JSONObject();
                jsonstdfield.put("text", "标准字段");
                jsonstdfield.put("leaf", true);
                jsonstdfield.put("id", id++);
                jsonArraymodule.add(jsonstdfield);
            }
            if(module.getView() != null){
                JSONObject jsonview = new JSONObject();
                jsonview.put("text", "视图");
                jsonview.put("leaf", true);
                jsonview.put("id", id++);
                jsonArraymodule.add(jsonview);
            }
            if(module.getTrigger() != null){
                JSONObject jsontrigger = new JSONObject();
                jsontrigger.put("text", "触发器");
                jsontrigger.put("leaf", true);
                jsontrigger.put("id", id++);
                jsonArraymodule.add(jsontrigger);
            }
            jsonObject.put("text","模块(" + module.getId() + ")");
            jsonObject.put("id", id++);
            jsonObject.put("children", jsonArraymodule);
            jsonArraytotal.add(jsonObject);
        }
        jsontotal.put("data",jsonArraytotal);
        return new NormalResponse(jsonArraytotal, (long) jsonArraytotal.size());

    }

    @RequestMapping("/addtable")
    @ResponseBody
    public NormalResponse addtable(HttpServletRequest request) throws Exception{
        String tableName = request.getParameter("tableName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        filePath = filePath.substring(1,filePath.length() - 51);
        filePath = filePath + "/db-script/script/src/main/resources/xml/module.xml";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        File file = new File(filePath);
        Document document = db.parse(file);
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("module");
        for(int i = 0; i < nodeList.getLength(); i++){
            if(nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(moduleName)){
                Element element = document.createElement("tablename");
                element.setAttribute("name",tableName);

                root.getElementsByTagName("module").item(i).appendChild(element);
                TransformerFactory tff = TransformerFactory.newInstance();
                Transformer tf = tff.newTransformer();
                tf.setOutputProperty(OutputKeys.INDENT, "yes");
                tf.transform(new DOMSource(document), new StreamResult(file));
            }
        }
        return new NormalResponse();
    }

    @RequestMapping("/deleteTable")
    @ResponseBody
    public NormalResponse deleteTable(HttpServletRequest request) throws Exception{
        String tableName = request.getParameter("tableName");
        String FileName = request.getParameter("FileName");
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        filePath = filePath.substring(1,filePath.length() - 51);
        String tableFilePath = filePath + "/db-script/script/src/main/resources/xml/" + FileName + "/" + FileName + ".table.xml";
        File file = new File(tableFilePath);
        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf = tff.newTransformer();
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

        //删除module.xml中对应表格
        String modulePath = filePath + "/db-script/script/src/main/resources/xml/" + "module.xml";
        File moduleFile = new File(modulePath);
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document moduleDocument = documentBuilder.parse(moduleFile);
        Element moduleRoot = moduleDocument.getDocumentElement();
        NodeList moduleNodeList = moduleRoot.getElementsByTagName("module");
        System.out.println("filename:" + FileName);
        for(int i = 0; i <  moduleNodeList.getLength(); i++){
            if(moduleNodeList.item(i).getAttributes().getNamedItem("id").equals(FileName)){
                Element cruModule = (Element) moduleNodeList.item(i);
                System.out.println("1111111111");
                NodeList tableNodeList = cruModule.getElementsByTagName("tablename");
                for(int j = 0; j < tableNodeList.getLength(); j++){
                    if(tableNodeList.item(j).getAttributes().getNamedItem("name").getNodeValue().equals(tableName)){
                        moduleNodeList.item(i).removeChild(tableNodeList.item(j));
                        System.out.println("22222222");
                    }
                }
                tf.transform(new DOMSource(moduleDocument), new StreamResult(moduleFile));
                break;
            }
        }
        return new NormalResponse();

    }

}
