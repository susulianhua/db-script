package org.tinygroup.service.classpublisher;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiucn on 2018/3/27.
 */
@XStreamAlias("service-class-path")
public class SerivceClassPath {

    @XStreamImplicit(itemFieldName = "class-path")
    private List<String> classPaths;

    public List<String> getClassPaths() {
        if(classPaths == null){
            classPaths = new ArrayList<String>();
        }
        return classPaths;
    }

    public void setClassPaths(List<String> classPaths) {
        this.classPaths = classPaths;
    }
}
