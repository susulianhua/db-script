package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.config.stdfield.StandardFields;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.service.FileFromXmlUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/standardField")
public class StandardFieldController {

    @RequestMapping("/getStdidStore")
    @ResponseBody
    public NormalResponse getStdidStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getStandardFieldFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(StandardFields.class);
        StandardFields standardFields = (StandardFields) xStream.fromXML(file);
        List<StandardField> standardFieldList = standardFields.getStandardFieldList();
        return new NormalResponse(standardFieldList, (long) standardFieldList.size());
    }

    @RequestMapping("/standardFieldSave")
    @ResponseBody
    public NormalResponse saveStandardField(@RequestBody StandardFields standardFields){
        String moduleName = standardFields.getPackageName();
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getStandardFieldFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(StandardFields.class);
        String xml = xStream.toXML(standardFields);
        UpdateMetaDataUtils.classToFile(xml, file);
        return new NormalResponse();
    }
}
