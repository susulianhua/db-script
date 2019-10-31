package com.xquant.script.controller;

import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.view.View;
import com.xquant.database.config.view.Views;
import com.xquant.script.pojo.ReturnClass.NormalResponse;
import com.xquant.script.pojo.saveWithModuleName.ViewWithModuleName;
import com.xquant.script.pojo.otherReturn.RefViewIdReturn;
import com.xquant.script.pojo.otherReturn.ViewReturn;
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
@RequestMapping("/view")
public class ViewController {

    @RequestMapping("/getSqlBodyStore")
    @ResponseBody
    public NormalResponse getSqlBodyStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String viewName = request.getParameter("viewName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getViewFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Views.class);
        Views views = (Views) xStream.fromXML(file);
        for(View view: views.getViewTableList()){
            if(view.getId().equals(viewName) &&
                    !CollectionUtils.isEmpty(view.getSqlBodyList())){
                return new NormalResponse(view.getSqlBodyList(),
                        (long) view.getSqlBodyList().size());
            }
        }
        return  new NormalResponse();
    }

    @RequestMapping("/getViewInformation")
    @ResponseBody
    public NormalResponse getViewInformation(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String viewName = request.getParameter("viewName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getViewFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Views.class);
        Views views = (Views) xStream.fromXML(file);
        ViewReturn viewReturn = new ViewReturn();
        viewReturn.setId(viewName);
        viewReturn.setModuleName(moduleName);
        for(View view: views.getViewTableList()){
            if(view.getId().equals(viewName)){
                viewReturn.setName(view.getNameWithOutSchema());
                viewReturn.setDescription(view.getDescription());
                viewReturn.setTitle(view.getTitle());
                viewReturn.setSchema(view.getSchema());
            }
        }
        List<ViewReturn> viewReturnList = new ArrayList<ViewReturn>();
        viewReturnList.add(viewReturn);
        return new NormalResponse(viewReturnList, (long) viewReturnList.size());
    }

    @RequestMapping("/getRefViewIdStore")
    @ResponseBody
    public NormalResponse getRefViewIdStore(HttpServletRequest request){
        String moduleName = request.getParameter("moduleName");
        String viewName = request.getParameter("viewName");
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getViewFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Views.class);
        Views views = (Views) xStream.fromXML(file);
        List<RefViewIdReturn> refViewIdReturnList = new ArrayList<RefViewIdReturn>();
        for(View view: views.getViewTableList()){
            if(view.getId().equals(viewName) && view.getRefViewIds() != null){
                if(!CollectionUtils.isEmpty(view.getRefViewIds().getRefViewIdList())){
                    for(String string: view.getRefViewIds().getRefViewIdList()){
                        RefViewIdReturn refViewIdReturn = new RefViewIdReturn();
                        refViewIdReturn.setRefViewId(string);
                        refViewIdReturnList.add(refViewIdReturn);
                    }
                }
                break;
            }
        }
        return new NormalResponse(refViewIdReturnList, (long) refViewIdReturnList.size());
    }

    @RequestMapping("/saveView")
    @ResponseBody
    public NormalResponse saveView(@RequestBody ViewWithModuleName viewWithModuleName){
        String moduleName = viewWithModuleName.getModuleName();
        View view = viewWithModuleName.getView();
        String viewName = view.getId();
        String filePath = this.getClass().getClassLoader().getResource("/").getPath();
        File file = GetCorrespondFileUtils.getViewFile(moduleName, filePath);
        XStream xStream = new XStream();
        xStream.processAnnotations(Views.class);
        Views views = (Views) xStream.fromXML(file);
        for(View view1: views.getViewTableList()){
            if(view1.getId().equals(viewName)){
                views.getViewTableList().remove(view1);
                views.getViewTableList().add(view);
                break;
            }
        }
        String xml = xStream.toXML(views);
        UpdateMetaDataUtils.objectToFile(xml, file);
        return new NormalResponse();
    }
}
