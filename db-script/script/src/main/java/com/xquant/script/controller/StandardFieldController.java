package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.metadata.config.bizdatatype.BusinessType;
import com.xquant.metadata.config.bizdatatype.BusinessTypes;
import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.config.stdfield.StandardFields;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.comboboxStoreRetrun.BusinessTypeIdStore;
import com.xquant.script.service.GetCorrespondFileUtils;
import com.xquant.script.service.UpdateMetaDataUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/standardField")
public class StandardFieldController {

    @RequestMapping("/getStdidStore")
    @ResponseBody
    public NormalResponse getStdidStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getStandardFieldFile(moduleName, filePath);
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
        File file = GetCorrespondFileUtils.getStandardFieldFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(StandardFields.class);
        String xml = xStream.toXML(standardFields);
        UpdateMetaDataUtils.objectToFile(xml, file);
        return new NormalResponse();
    }

    @RequestMapping("/getBusinessTypeId")
    @ResponseBody
    public NormalResponse getBusinessTypeId(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getBusinessTypeFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(BusinessTypes.class);
        List<BusinessTypeIdStore> businessTypeIdStoreList = new ArrayList<BusinessTypeIdStore>();
        BusinessTypes businessTypes = (BusinessTypes) xStream.fromXML(file);
        for(BusinessType businessType: businessTypes.getBusinessTypeList()){
            BusinessTypeIdStore businessTypeIdStore = new BusinessTypeIdStore();
            businessTypeIdStore.setTypeId(businessType.getTypeId());
            businessTypeIdStore.setName(businessType.getTypeId());
            businessTypeIdStoreList.add(businessTypeIdStore);
        }
        return new NormalResponse(businessTypeIdStoreList, (long) businessTypeIdStoreList.size());
    }
}
