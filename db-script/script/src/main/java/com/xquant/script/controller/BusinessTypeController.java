package com.xquant.script.controller;


import com.thoughtworks.xstream.XStream;
import com.xquant.metadata.config.PlaceholderValue;
import com.xquant.metadata.config.bizdatatype.BusinessType;
import com.xquant.metadata.config.bizdatatype.BusinessTypes;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.module.PlaceHolderValueReturn;
import com.xquant.script.service.FileFromXmlUtils;
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

@RequestMapping("/businessType")
@Controller
public class BusinessTypeController {

    @RequestMapping("/getBusinessTypeStore")
    @ResponseBody
    public NormalResponse getBusinessTypeStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getBusinessTypeFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(BusinessTypes.class);
        BusinessTypes businessTypes = (BusinessTypes) xStream.fromXML(file);
        List<BusinessType> businessTypeList = businessTypes.getBusinessTypeList();
        return new NormalResponse(businessTypeList, (long) businessTypeList.size());
    }

    @RequestMapping("/getPlaceHolderValue")
    @ResponseBody
    public NormalResponse getPlaceHolderValue(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getBusinessTypeFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(BusinessTypes.class);
        BusinessTypes businessTypes = (BusinessTypes) xStream.fromXML(file);
        List<PlaceHolderValueReturn> phvReturnsList = new ArrayList<PlaceHolderValueReturn>();
        for(BusinessType businessType: businessTypes.getBusinessTypeList()){
            if(!CollectionUtils.isEmpty(businessType.getPlaceholderValueList())){
                for(PlaceholderValue placeholderValue: businessType.getPlaceholderValueList()){
                    PlaceHolderValueReturn placeHolderValueReturn = new PlaceHolderValueReturn();
                    placeHolderValueReturn.setBusinessId(businessType.getId());
                    placeHolderValueReturn.setName(placeholderValue.getName());
                    placeHolderValueReturn.setValue(placeholderValue.getValue());
                   phvReturnsList.add(placeHolderValueReturn);
                }
            }
        }
        return new NormalResponse(phvReturnsList, (long) phvReturnsList.size());
    }

    @RequestMapping("/saveBusinessType")
    @ResponseBody
    public NormalResponse saveBusinessType(@RequestBody BusinessTypes businessTypes){
        String moduleName = businessTypes.getPackageName();
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = FileFromXmlUtils.getBusinessTypeFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(BusinessTypes.class);
        String xml = xStream.toXML(businessTypes);
        UpdateMetaDataUtils.classToFile(xml, file);
        return  new NormalResponse();
    }
}
