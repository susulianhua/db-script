package com.xquant.script.controller;


import com.thoughtworks.xstream.XStream;
import com.xquant.metadata.config.PlaceholderValue;
import com.xquant.metadata.config.stddatatype.DialectType;
import com.xquant.metadata.config.stddatatype.StandardType;
import com.xquant.metadata.config.stddatatype.StandardTypes;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.service.GetCorrespondFileUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
@RequestMapping("/standardType")
public class StandardTypeController {

    @RequestMapping("/getDialectTypeStore")
    @ResponseBody
    public NormalResponse getDialectTypeStore(HttpServletRequest request){
        String standardTypeId = request.getParameter("standardTypeId");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getStandardTypeFile(filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(StandardTypes.class);
        StandardTypes standardTypes = (StandardTypes) xStream.fromXML(file);
        for(StandardType standardType: standardTypes.getStandardTypeList()){
            if(standardType.getId().equals(standardTypeId)){
                return new NormalResponse(standardType.getDialectTypeList(),
                        (long) standardType.getDialectTypeList().size());
            }
        }
        return new NormalResponse();
    }

    @RequestMapping("/getPlaceHolderStore")
    @ResponseBody
    public NormalResponse getPlaceHolderStore(HttpServletRequest request){
        String standardTypeId = request.getParameter("standardTypeId");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getStandardTypeFile(filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(StandardTypes.class);
        StandardTypes standardTypes = (StandardTypes) xStream.fromXML(file);
        for(StandardType standardType: standardTypes.getStandardTypeList()){
            if(standardType.getId().equals(standardTypeId)){
                return new NormalResponse(standardType.getPlaceholderList(),
                        (long) standardType.getPlaceholderList().size());
            }
        }
        return new NormalResponse();
    }

    @RequestMapping("/getPlaceholderValueStore")
    @ResponseBody
    public NormalResponse getPlaceholderValueStore(HttpServletRequest request){
        String standardTypeId = request.getParameter("standardTypeId");
        String languageType = request.getParameter("languageType");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getStandardTypeFile(filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(StandardTypes.class);
        StandardTypes standardTypes = (StandardTypes) xStream.fromXML(file);
        for(StandardType standardType: standardTypes.getStandardTypeList()){
            if(standardType.getId().equals(standardTypeId)){
                for(DialectType dialectType: standardType.getDialectTypeList()){
                    if(dialectType.getLanguage().equals(languageType) &&
                            !CollectionUtils.isEmpty(dialectType.getPlaceholderValueList())){
                        return new NormalResponse(dialectType.getPlaceholderValueList(),
                                (long) dialectType.getPlaceholderValueList().size());
                    }
                }
            }
        }
        return new NormalResponse();
    }
}
