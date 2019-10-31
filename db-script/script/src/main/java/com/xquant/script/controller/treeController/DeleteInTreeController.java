package com.xquant.script.controller.treeController;

import com.thoughtworks.xstream.XStream;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.module.Module;
import com.xquant.script.pojo.module.Modules;
import com.xquant.script.service.GetCorrespondFileUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import com.xquant.script.service.UpdateModuleUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
@RequestMapping("/deleteInTree")
public class DeleteInTreeController {

    /** 在module.xml及对应table.xml中生成删除对应表格信息*/
    @RequestMapping("/deleteTable")
    @ResponseBody
    public NormalResponse deleteTable(HttpServletRequest request) throws Exception{
        String tableName = request.getParameter("metadataName");
        String moduleName = request.getParameter("FileName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getTableFile(moduleName, filePath);
        //删除table.xml中对应表格
        UpdateMetaDataUtils.deleteTableInTable(file, tableName);

        //删除module.xml中对应表格
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateModuleUtils.deleteTableInModule(moduleFile, tableName, moduleName);
        return new NormalResponse();

    }

    @RequestMapping("/deleteProcedure")
    @ResponseBody
    public NormalResponse deleteProcedure(HttpServletRequest request){
        String procedureName = request.getParameter("metadataName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getProcedureFile(moduleName, filePath);
        UpdateMetaDataUtils.deleteProcedureInProcedure(file, procedureName);
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateModuleUtils.deleteProcedureInModule(moduleFile, procedureName, moduleName);
        return new NormalResponse();
    }

    @RequestMapping("/deleteView")
    @ResponseBody
    public NormalResponse deleteView(HttpServletRequest request){
        String viewName = request.getParameter("metadataName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getViewFile(moduleName, filePath);
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateMetaDataUtils.deleteViewInView(file, viewName);
        UpdateModuleUtils.deleteViewInModule(moduleFile, viewName, moduleName);
        return new NormalResponse();
    }

    @RequestMapping("/deleteTrigger")
    @ResponseBody
    public NormalResponse deleteTrigger(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String triggerName = request.getParameter("metadataName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File triggerFile = GetCorrespondFileUtils.getTriggerFile(moduleName, filePath);
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateMetaDataUtils.deleteTriggerInTrigger(triggerFile, triggerName);
        UpdateModuleUtils.deleteTriggerInModule(moduleFile, triggerName, moduleName);
        return new NormalResponse();
    }

    @RequestMapping("TableFile")
    @ResponseBody
    public NormalResponse deleteTableFile(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        String targetFile = filePath.substring(1,filePath.length() - 51) +
                "/db-script/script/src/main/resources/xml/" + moduleName + "/" + ".table.xml";
        File file = new File(targetFile);
        file.delete();
        XStream xStream = new XStream();
        xStream.processAnnotations(Modules.class);
        Modules modules = (Modules) xStream.fromXML(moduleFile);
        for(Module module: modules.getModuleList()){
            if(module.getId().equals(moduleName)){
                module.setTablelist(null);
                break;
            }
        }
        String xml = xStream.toXML(modules);
        UpdateMetaDataUtils.objectToFile(xml, moduleFile);
        return new NormalResponse();
    }
}
