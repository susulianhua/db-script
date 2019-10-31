package com.xquant.script.controller.treeController;


import com.alibaba.fastjson.JSONArray;
import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.table.Tables;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.module.*;
import com.xquant.script.service.GetCorrespondFileUtils;
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
        File file = GetCorrespondFileUtils.getModuleFile(filePath);
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
        File file = GetCorrespondFileUtils.getModuleFile(filePath);
        File moduleDirFile = GetCorrespondFileUtils.getModuleDirFile(filePath, newModuleName);
        moduleDirFile.mkdirs();
        UpdateModuleUtils.addModule(file, newModuleName);
        return new NormalResponse();
    }

    @RequestMapping("/deleteModule")
    @ResponseBody
    public NormalResponse deleteModule(HttpServletRequest request){
        String moduleDeplayName = request.getParameter("moduleName");
        String moduleName = moduleDeplayName.substring(3, moduleDeplayName.length() - 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getModuleFile(filePath);
        String folder = filePath.substring(1,filePath.length() - 51) +
                "/db-script/script/src/main/resources/xml/" + moduleName;
        File dirFile= new File(folder);
        try{
            FileUtils.deleteDirectory(dirFile);
        }catch (IOException e){
            e.printStackTrace();
        }
        UpdateModuleUtils.deleteModule(file, moduleName);

        return new NormalResponse();
    }


    /**在module.xml生成对应信息，并生成对应标准字段，业务类型xml文件*/
    @RequestMapping("/addOther")
    @ResponseBody
    public NormalResponse addOther(HttpServletRequest request){
        String moduleAll = request.getParameter("moduleName");
        String curText = request.getParameter("otherName");
        String moduleName = moduleAll.substring(3,moduleAll.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getModuleFile(filePath);
        curText = UpdateModuleUtils.addOtherInModule(file, curText, moduleName);
        UpdateMetaDataUtils.addOtherInDetail(moduleName, curText, filePath);
        return new NormalResponse();

    }

    @RequestMapping("/addFileName")
    @ResponseBody
    public NormalResponse addFileName(HttpServletRequest request){
        String moduleAll = request.getParameter("moduleName");
        String moduleName = moduleAll.substring(3, moduleAll.length() - 1);
        String fileName = GetCorrespondFileUtils.transformMetadataToEnglish(request.getParameter("otherName"));
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(moduleFile);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                module.setList(fileName);
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, moduleFile);
        UpdateMetaDataUtils.createMetaDataXmlFile(moduleName,fileName,filePath);
        return new NormalResponse();
    }

    /**在module.xml删除对应信息，并删除对应视图、触发器等xml文件*/
    @RequestMapping("/deleteOther")
    @ResponseBody
    public NormalResponse deleteOther(HttpServletRequest request){
        String fileName = request.getParameter("FileName");
        String curText = request.getParameter("curText");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getModuleFile(filePath);
        curText = UpdateModuleUtils.deleteOtherInModule(file, curText, fileName);
        UpdateMetaDataUtils.deleteOtherInDetail(fileName, curText, filePath);
        return new NormalResponse();
    }


}
