package com.xquant.script.controller.treeController;

import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.service.GetCorrespondFileUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import com.xquant.script.service.UpdateModuleUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
@RequestMapping("/addInTree")
public class AddInTreeController {

    /** 在module.xml及对应table.xml中生成添加对应表格信息*/
    @RequestMapping("/addTable")
    @ResponseBody
    public NormalResponse addTable(HttpServletRequest request) throws Exception{
        String tableName = request.getParameter("metadataName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateModuleUtils.addTableInModule(moduleFile, tableName, moduleName);
        return new NormalResponse();
    }

    @RequestMapping("/addProcedure")
    @ResponseBody
    public NormalResponse addProcedure(HttpServletRequest request){
        String procedureName = request.getParameter("metadataName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateModuleUtils.addProcedureInModule(moduleFile, procedureName, moduleName);
        return new NormalResponse();
    }

    @RequestMapping("/addView")
    @ResponseBody
    public NormalResponse addView(HttpServletRequest request){
        String viewName = request.getParameter("metadataName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateModuleUtils.addViewInModule(moduleFile, viewName, moduleName);
        return  new NormalResponse();
    }

    @RequestMapping("/addTrigger")
    @ResponseBody
    public NormalResponse addTrigger(HttpServletRequest request){
        String triggerName = request.getParameter("metadataName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateModuleUtils.addTriggerInModule(moduleFile, triggerName, moduleName);
        return  new NormalResponse();
    }

    @RequestMapping("/addSequence")
    @ResponseBody
    public NormalResponse addSequence(HttpServletRequest request){
        String sequenceName = request.getParameter("metadataName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateModuleUtils.addSequenceInModule(moduleFile, sequenceName, moduleName);
        return new NormalResponse();

    }

    @RequestMapping("/addFunction")
    @ResponseBody
    public NormalResponse addFunction(HttpServletRequest request){
        String functionName = request.getParameter("metadataName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        UpdateModuleUtils.addFunctionInModule(moduleFile, functionName, moduleName);
        return new NormalResponse();

    }
}
