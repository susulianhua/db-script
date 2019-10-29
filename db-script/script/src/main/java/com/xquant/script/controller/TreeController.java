package com.xquant.script.controller;


import com.alibaba.fastjson.JSONArray;
import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.table.Tables;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.module.*;
import com.xquant.script.service.FileFromXmlUtils;
import com.xquant.script.service.UpdateModuleUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/tree")
public class TreeController {
    /**生成树Store，拼接json*/
    @RequestMapping("/getModule")
    @ResponseBody
    public NormalResponse getModule(){
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getModuleFile(filePath);
        XStream xstream = new XStream();
        xstream.processAnnotations(Modules.class);
        Modules modules = (Modules) xstream.fromXML(file);
        JSONArray jsonArrayTotal = new JSONArray();
        UpdateMetaDataUtils.createJson(jsonArrayTotal, modules);
        return new NormalResponse(jsonArrayTotal, (long) jsonArrayTotal.size());
    }

    @RequestMapping("/addModule")
    @ResponseBody
    public NormalResponse addModule(HttpServletRequest request){
        String newModuleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getModuleFile(filePath);
        File moduleDirFile = FileFromXmlUtils.getModuleDirFile(filePath, newModuleName);
        moduleDirFile.mkdirs();
        File tableFile = FileFromXmlUtils.getTableFile(newModuleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Tables.class);
        Tables tables = new Tables();
        String xml = xStream.toXML(tables);
        UpdateMetaDataUtils.classToFile(xml, tableFile);
        UpdateModuleUtils.addModule(file, newModuleName);
        return new NormalResponse();
    }

    @RequestMapping("/deleteModule")
    @ResponseBody
    public NormalResponse deleteModule(HttpServletRequest request){
        String moduleDeplayName = request.getParameter("moduleName");
        String moduleName = moduleDeplayName.substring(3, moduleDeplayName.length() - 1);
        System.out.println("moduleName" + moduleName);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getModuleFile(filePath);
        String folder = filePath.substring(1,filePath.length() - 51) +
                "/db-script/script/src/main/resources/xml/" + moduleName;
        System.out.println("folder:" + folder);
        File dirFile= new File(folder);
        try{
            FileUtils.deleteDirectory(dirFile);
        }catch (IOException e){
            e.printStackTrace();
        }
        UpdateModuleUtils.deleteModule(file, moduleName);

        return new NormalResponse();
    }

    /** 在module.xml及对应table.xml中生成添加对应表格信息*/
    @RequestMapping("/addtable")
    @ResponseBody
    public NormalResponse addtable(HttpServletRequest request) throws Exception{
        String tableName = request.getParameter("tableName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        File file = FileFromXmlUtils.getTableFile(moduleName, filePath);
        UpdateModuleUtils.addTableInModule(moduleFile, tableName, moduleName);
        UpdateMetaDataUtils.addTableInTable(file, tableName);
        return new NormalResponse();
    }

    /** 在module.xml及对应table.xml中生成删除对应表格信息*/
    @RequestMapping("/deleteTable")
    @ResponseBody
    public NormalResponse deleteTable(HttpServletRequest request) throws Exception{
        String tableName = request.getParameter("tableName");
        String FileName = request.getParameter("FileName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getTableFile(FileName, filePath);
        //删除table.xml中对应表格
        UpdateMetaDataUtils.deleteTableInTable(file, tableName);

        //删除module.xml中对应表格
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        UpdateModuleUtils.deleteTableInModule(moduleFile, tableName, FileName);
        return new NormalResponse();

    }

    @RequestMapping("/addProcedure")
    @ResponseBody
    public NormalResponse addProcedure(HttpServletRequest request) throws Exception{
        String procedureName = request.getParameter("procedureName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        File file = FileFromXmlUtils.getProcedureFile(moduleName, filePath);
        UpdateModuleUtils.addProcedureInModule(moduleFile, procedureName, moduleName);
        UpdateMetaDataUtils.addProcedureInProcedure(file, procedureName);
        return new NormalResponse();
    }

    @RequestMapping("/deleteProcedure")
    @ResponseBody
    public NormalResponse deleteProcedure(HttpServletRequest request){
        String procedureName = request.getParameter("procedureName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getProcedureFile(moduleName, filePath);
        //删除table.xml中对应表格
        UpdateMetaDataUtils.deleteProcedureInProcedure(file, procedureName);
        //删除module.xml中对应表格
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        UpdateModuleUtils.deleteProcedureInModule(moduleFile, procedureName, moduleName);
        return new NormalResponse();
    }

    @RequestMapping("/addView")
    @ResponseBody
    public NormalResponse addView(HttpServletRequest request){
        String viewName = request.getParameter("viewName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        File file = FileFromXmlUtils.getViewFile(moduleName, filePath);
        UpdateModuleUtils.addViewInModule(moduleFile, viewName, moduleName);
        UpdateMetaDataUtils.addViewInView(file, viewName);
        return  new NormalResponse();
    }

    @RequestMapping("/deleteView")
    @ResponseBody
    public NormalResponse deleteView(HttpServletRequest request){
        String viewName = request.getParameter("viewName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getViewFile(moduleName, filePath);
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        UpdateMetaDataUtils.deleteViewInView(file, viewName);
        UpdateModuleUtils.deleteViewInModule(moduleFile, viewName, moduleName);
        return new NormalResponse();
    }

    /**在module.xml生成对应信息，并生成对应视图、触发器等xml文件*/
    @RequestMapping("/addOther")
    @ResponseBody
    public NormalResponse addOther(HttpServletRequest request){
        String moduleAll = request.getParameter("moduleName");
        String curText = request.getParameter("otherName");
        String moduleName = moduleAll.substring(3,moduleAll.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getModuleFile(filePath);
        curText = UpdateModuleUtils.addOtherInModule(file, curText, moduleName);
        UpdateMetaDataUtils.addOtherInDetail(moduleName, curText, filePath);
        return new NormalResponse();

    }

    /**在module.xml删除对应信息，并删除对应视图、触发器等xml文件*/
    @RequestMapping("/deleteOther")
    @ResponseBody
    public NormalResponse deleteOther(HttpServletRequest request){
        String fileName = request.getParameter("FileName");
        String curText = request.getParameter("curText");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getModuleFile(filePath);
        curText = UpdateModuleUtils.deleteOtherInModule(file, curText, fileName);
        System.out.println("curTextdelete" + curText);
        UpdateMetaDataUtils.deleteOtherInDetail(fileName, curText, filePath);
        return new NormalResponse();
    }

    @RequestMapping("/addTrigger")
    @ResponseBody
    public NormalResponse addTrigger(HttpServletRequest request){
        String triggerName = request.getParameter("triggerName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        File file = FileFromXmlUtils.getTriggerFile(moduleName, filePath);
        UpdateModuleUtils.addTriggerInModule(moduleFile, triggerName, moduleName);
        UpdateMetaDataUtils.addTriggerInTrigger(file, triggerName);
        return  new NormalResponse();
    }

    @RequestMapping("/deleteTrigger")
    @ResponseBody
    public NormalResponse deleteTrigger(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String triggerName = request.getParameter("triggerName");
        System.out.println("moduleName: " + moduleName);
        System.out.println("triggerName: " + triggerName);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File triggerFile = FileFromXmlUtils.getTriggerFile(moduleName, filePath);
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        UpdateMetaDataUtils.deleteTriggerInTrigger(triggerFile, triggerName);
        UpdateModuleUtils.deleteTriggerInModule(moduleFile, triggerName, moduleName);
        return new NormalResponse();
    }

}
