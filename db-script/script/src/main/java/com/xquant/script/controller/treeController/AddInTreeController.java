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
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        File file = GetCorrespondFileUtils.getTableFile(moduleName, filePath);
        UpdateModuleUtils.addTableInModule(moduleFile, tableName, moduleName);
        UpdateMetaDataUtils.addTableInTable(file, tableName);
        return new NormalResponse();
    }

    @RequestMapping("/addProcedure")
    @ResponseBody
    public NormalResponse addProcedure(HttpServletRequest request){
        String procedureName = request.getParameter("metadataName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        File file = GetCorrespondFileUtils.getProcedureFile(moduleName, filePath);
        UpdateModuleUtils.addProcedureInModule(moduleFile, procedureName, moduleName);
        UpdateMetaDataUtils.addProcedureInProcedure(file, procedureName);
        return new NormalResponse();
    }

    @RequestMapping("/addView")
    @ResponseBody
    public NormalResponse addView(HttpServletRequest request){
        String viewName = request.getParameter("metadataName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        File file = GetCorrespondFileUtils.getViewFile(moduleName, filePath);
        UpdateModuleUtils.addViewInModule(moduleFile, viewName, moduleName);
        UpdateMetaDataUtils.addViewInView(file, viewName);
        return  new NormalResponse();
    }

    @RequestMapping("/addTrigger")
    @ResponseBody
    public NormalResponse addTrigger(HttpServletRequest request){
        String triggerName = request.getParameter("metadataName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        File file = GetCorrespondFileUtils.getTriggerFile(moduleName, filePath);
        UpdateModuleUtils.addTriggerInModule(moduleFile, triggerName, moduleName);
        UpdateMetaDataUtils.addTriggerInTrigger(file, triggerName);
        return  new NormalResponse();
    }

    @RequestMapping("/addSequence")
    @ResponseBody
    public NormalResponse addSequence(HttpServletRequest request){
        String sequenceName = request.getParameter("metadataName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        File file = GetCorrespondFileUtils.getSequenceFile(moduleName,filePath);
        UpdateModuleUtils.addSequenceInModule(moduleFile, sequenceName, moduleName);
        UpdateMetaDataUtils.addSequenceInSequence(file, sequenceName);
        return new NormalResponse();

    }

    @RequestMapping("/addFunction")
    @ResponseBody
    public NormalResponse addFunction(HttpServletRequest request){
        String functionName = request.getParameter("metadataName");
        String module = request.getParameter("moduleName");
        String moduleName = module.substring(3,module.length()- 1);
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File moduleFile = GetCorrespondFileUtils.getModuleFile(filePath);
        File file = GetCorrespondFileUtils.getFunctionFile(moduleName,filePath);
        UpdateModuleUtils.addFunctionInModule(moduleFile, functionName, moduleName);
        UpdateMetaDataUtils.addFunctionInFunction(file, functionName);
        return new NormalResponse();

    }
}
