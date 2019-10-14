package org.tinygroup.service.test.classpublisher.configtest;

import junit.framework.TestCase;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.service.impl.ServiceProviderImpl;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;
import org.tinygroup.service.registry.impl.ServiceRegistryImpl;
import org.tinygroup.service.test.classpublisher.TestClassServiceProcessor;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by qiucn on 2018/3/19.
 */
public class SameNameTest extends TestCase {

    public void testSameNameConfig() {
        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        ServiceProviderInterface provider = new ServiceProviderImpl();
        provider.setServiceRegistory(serviceRegistry);
        TestClassServiceProcessor processor = new TestClassServiceProcessor();
        processor.setClassPath("org.tinygroup.service.test.classpublisher.service.SameNameServiceImpl");
        processor.setFileResolver(new FileResolverImpl());
        processor.setProvider(provider);
        XmlNode node = new XmlNode("service-class-path");
        XmlNode subNode = new XmlNode("class-path");
        subNode.setContent("org.tinygroup.service.test.classpublisher.service.SameNameService");
        node.getSubNodes().add(subNode);
        processor.config(node, null);

        ServiceRegistry registry = provider.getServiceRegistory();
        Collection<ServiceRegistryItem> serviceRegistryItems = registry.getServiceRegistryItems();
        assertEquals(2, serviceRegistryItems.size());
        List<ServiceRegistryItem> list = new ArrayList<ServiceRegistryItem>(serviceRegistryItems);
        ServiceRegistryItem item1 = list.get(0);
        ServiceRegistryItem item2 = list.get(1);

        String serviceId1 = item1.getServiceId();
        String serviceId2 = item2.getServiceId();
        assertNotSame(serviceId1,serviceId2);
        if(serviceId1.length()>serviceId2.length()){
            assertEquals(serviceId1.substring(0,serviceId2.length()),serviceId2);
        }else{
            assertEquals(serviceId2.substring(0,serviceId1.length()),serviceId1);
        }

    }
}
