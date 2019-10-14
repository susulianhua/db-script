package org.tinygroup.service.test.classpublisher.inherittest;

import junit.framework.TestCase;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.service.impl.ServiceProviderImpl;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;
import org.tinygroup.service.registry.impl.ServiceRegistryImpl;
import org.tinygroup.service.test.classpublisher.TestClassServiceProcessor;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.Collection;

/**
 * Created by qiucn on 2018/3/19.
 */
public class InheritTest extends TestCase {

    public void testInherit() {
        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        ServiceProviderInterface provider = new ServiceProviderImpl();
        provider.setServiceRegistory(serviceRegistry);
        TestClassServiceProcessor processor = new TestClassServiceProcessor();
        processor.setClassPath("org.tinygroup.service.test.classpublisher.service.InheritServiceImpl");
        processor.setFileResolver(new FileResolverImpl());
        processor.setProvider(provider);
        XmlNode node = new XmlNode("service-class-path");
        XmlNode subNode = new XmlNode("class-path");
        subNode.setContent("org.tinygroup.service.test.classpublisher.service.InheritService");
        node.getSubNodes().add(subNode);
        processor.config(node, null);

        ServiceRegistry registry = provider.getServiceRegistory();
        Collection<ServiceRegistryItem> serviceRegistryItems = registry.getServiceRegistryItems();
        assertEquals(2, serviceRegistryItems.size());
    }
}
