package com.xquant.script.controller;


import com.alibaba.fastjson.JSONArray;
import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.table.Tables;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.module.*;
import com.xquant.script.service.FileFromXmlUtils;
import com.xquant.script.service.UpdateXmlUtils;
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
        UpdateXmlUtils.createJson(jsonArrayTotal, modules);
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
        UpdateXmlUtils.classToFile(xml, tableFile);
        UpdateXmlUtils.addModule(file, newModuleName);
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
        UpdateXmlUtils.deleteModule(file, moduleName);

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
        UpdateXmlUtils.addTableInModule(moduleFile, tableName, moduleName);
        UpdateXmlUtils.addTableInTable(file, tableName);
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
        UpdateXmlUtils.deleteTableInTable(file, tableName);

        //删除module.xml中对应表格
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        UpdateXmlUtils.deleteTableInModule(moduleFile, tableName, FileName);
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
        UpdateXmlUtils.addProcedureInModule(moduleFile, procedureName, moduleName);
        UpdateXmlUtils.addProcedureInProcedure(file, procedureName);
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
        UpdateXmlUtils.deleteProcedureInProcedure(file, procedureName);

        //删除module.xml中对应表格
        File moduleFile = FileFromXmlUtils.getModuleFile(filePath);
        UpdateXmlUtils.deleteProcedureInModule(moduleFile, procedureName, moduleName);
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
        curText = UpdateXmlUtils.addOtherInModule(file, curText, moduleName);
        UpdateXmlUtils.addOtherInDetail(moduleName, curText, filePath);
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
        curText = UpdateXmlUtils.deleteOtherInModule(file, curText, fileName);
        System.out.println("curTextdelete" + curText);
        UpdateXmlUtils.deleteOtherInDetail(fileName, curText, filePath);
        return new NormalResponse();
    }
}
