package com.xquant.script.service;

import com.thoughtworks.xstream.XStream;
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
        updateMetaDataUtils.classToFile(xml, file);
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
        updateMetaDataUtils.classToFile(xml, file);
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

    public static void addProcedureInModule(File file, String procedureName, String moduleName){
        ProcedureNameInModule procedureNameInModule = new ProcedureNameInModule();
        procedureNameInModule.setName(procedureName);
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                module.getProcedureNameInModuleList().add(procedureNameInModule);
                break;
            };
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.classToFile(xml, file);
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
        UpdateMetaDataUtils.classToFile(xml, file);
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
        UpdateMetaDataUtils.classToFile(xml, moduleFile);
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

    public static void deleteProcedureInModule(File file, String procedureName, String moduleName){
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(file);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                for(ProcedureNameInModule procedureNameInModule: module.getProcedureNameInModuleList()){
                    if(procedureNameInModule.getName().equals(procedureName)){
                        module.getProcedureNameInModuleList().remove(procedureNameInModule);
                        break;
                    }
                }
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.classToFile(xml, file);
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
        UpdateMetaDataUtils.classToFile(xml, file);
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
        UpdateMetaDataUtils.classToFile(xml, moduleFile);
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
                else if(curText.equals("序列") && StringUtils.isEmpty(module.getSequence())){
                    module.setSequence(moduleName);
                    curText = "sequence";
                }
            }
        }
        String xml = xStream.toXML(modules);
        System.out.println("xml:" + xml);
        updateMetaDataUtils.classToFile(xml, file);
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
                else if(curText.equals("序列")) {
                    module.setSequence(null);
                    curText = "sequence";
                }
            }
        }
        String xml = xStream.toXML(modules);
        updateMetaDataUtils.classToFile(xml, file);
        return curText;
    }
}

