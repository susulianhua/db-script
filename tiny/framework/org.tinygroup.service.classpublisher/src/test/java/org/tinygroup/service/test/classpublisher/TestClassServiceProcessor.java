package org.tinygroup.service.test.classpublisher;

import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.service.classpublisher.ClassServiceFileProcessor;
import org.tinygroup.service.config.ServiceComponent;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiucn on 2018/3/21.
 */
public class TestClassServiceProcessor extends ClassServiceFileProcessor {

    private String classPath;

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Object getServiceInstance(ServiceComponent component) {
        try {
            BeanContainer<?> container = BeanContainerFactory
                    .getBeanContainer(getFileResolver().getClassLoader());
            Class<?> clazz = Class.forName(classPath);
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void config(XmlNode config, XmlNode componentConfig) {
        NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(config);
        XmlNode pathNode = nameFilter.findNode("service-class-path");
        try {
            List<String> list = new ArrayList<String>();
            for (XmlNode node : pathNode.getSubNodes()) {
                list.add(node.getContent());
            }
            initService(list);
            this.loadService(getProvider().getServiceRegistory(), getFileResolver().getClassLoader());
        } catch (ServiceLoadException e) {
        }
    }
}
