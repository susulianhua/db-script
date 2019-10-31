package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.trigger.Trigger;
import com.xquant.database.config.trigger.Triggers;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.otherReturn.TriggerReturn;
import com.xquant.script.pojo.saveWithModuleName.TriggerWithModuleName;
import com.xquant.script.service.GetCorrespondFileUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/trigger")
public class TriggerController {

    @RequestMapping("/getTriggerInformation")
    @ResponseBody
    public NormalResponse getTriggerInformation(HttpServletRequest request){
        String triggerName = request.getParameter("triggerName");
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getTriggerFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Triggers.class);
        Triggers triggers = (Triggers) xStream.fromXML(file);
        TriggerReturn triggerReturn = new TriggerReturn();
        triggerReturn.setModuleName(moduleName);
        triggerReturn.setName(triggerName);
        for(Trigger trigger: triggers.getTriggers()){
            if(trigger.getName().equals(triggerName)){
                triggerReturn.setTitle(trigger.getTitle());
                triggerReturn.setDescription(trigger.getDescription());
                break;
            }
        }
        List<TriggerReturn> triggerReturnList = new ArrayList<TriggerReturn>();
        triggerReturnList.add(triggerReturn);
        return new NormalResponse(triggerReturnList, (long) triggerReturnList.size());
    }

    @RequestMapping("/getSqlBodyStore")
    @ResponseBody
    public NormalResponse getSqlBodyStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String triggerName = request.getParameter("triggerName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getTriggerFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Triggers.class);
        Triggers triggers = (Triggers) xStream.fromXML(file);
        for(Trigger trigger: triggers.getTriggers()){
            if(trigger.getName().equals(triggerName) &&
                    !CollectionUtils.isEmpty(trigger.getTriggerSqls())){
                return new NormalResponse(trigger.getTriggerSqls(),
                        (long) trigger.getTriggerSqls().size());
            }
        }
        return  new NormalResponse();
    }

    @RequestMapping("/saveTrigger")
    @ResponseBody
    public NormalResponse saveTrigger(@RequestBody TriggerWithModuleName triggerWithModuleName){
        String moduleName = triggerWithModuleName.getModuleName();
        Trigger trigger = triggerWithModuleName.getTrigger();
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getTriggerFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Triggers.class);
        Triggers triggers = (Triggers) xStream.fromXML(file);
        for(Trigger trigger1: triggers.getTriggers()){
            if(trigger1.getName().equals(trigger.getName())){
                triggers.getTriggers().remove(trigger1);
                triggers.getTriggers().add(trigger);
                break;
            }
        }
        String xml = xStream.toXML(triggers);
        UpdateMetaDataUtils.objectToFile(xml, file);
        return new NormalResponse();
    }
}
