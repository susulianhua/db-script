package org.tinygroup.service.test.classpublisher.configtest;

import junit.framework.TestCase;
import org.tinygroup.event.Parameter;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.service.ServiceProxy;
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
public class ConfigTest extends TestCase {

    public void testServiceConfig() {
        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        ServiceProviderInterface provider = new ServiceProviderImpl();
        provider.setServiceRegistory(serviceRegistry);
        TestClassServiceProcessor processor = new TestClassServiceProcessor();
        processor.setClassPath("org.tinygroup.service.test.classpublisher.service.TestServiceImpl");
        processor.setFileResolver(new FileResolverImpl());
        processor.setProvider(provider);
        XmlNode node = new XmlNode("service-class-path");
        XmlNode subNode = new XmlNode("class-path");
        subNode.setContent("org.tinygroup.service.test.classpublisher.service.TestService");
        node.getSubNodes().add(subNode);
        processor.config(node, null);

        ServiceRegistry registry = provider.getServiceRegistory();
        Collection<ServiceRegistryItem> serviceRegistryItems = registry.getServiceRegistryItems();
        assertEquals(6, serviceRegistryItems.size());

        for (ServiceRegistryItem item : serviceRegistryItems) {
            System.out.println("=========================================");
            System.out.println("serviceId=========" + item.getServiceId());
            System.out.println("localName=========" + item.getLocalName());

            ServiceProxy service = (ServiceProxy) item.getService();
            System.out.println("methodName=========" + service.getMethodName());
            if ("stringService".equals(service.getMethodName())) {
                assertEquals(1, service.getInputParameters().size());
                System.out.println("入参列表===============");
                for (Parameter inputParam : service.getInputParameters()) {
                    System.out.println("paramName=========" + inputParam.getName());
                    assertEquals("name", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertFalse(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("java.lang.String", inputParam.getType());
                    assertNull(inputParam.getCollectionType());
                }

                Parameter result = service.getOutputParameter();
                assertNotNull(result);
                System.out.println("出参===============");
                System.out.println("resultName=========" + result.getName());
                System.out.println("resultType=========" + result.getType());
                assertEquals("java.lang.String", result.getType());
                System.out.println("resultArray=========" + result.isArray());
                assertFalse(result.isArray());
                assertNull(result.getCollectionType());

                System.out.println("ItemParam列表===============");
                for (Parameter p : item.getParameters()) {
                    System.out.println("paramName=========" + p.getName());
                    assertEquals("name", p.getName());
                    System.out.println("paramArray=========" + p.isArray());
                    assertFalse(p.isArray());
                    System.out.println("paramType=========" + p.getType());
                    assertEquals("java.lang.String", p.getType());
                    assertNull(p.getCollectionType());
                }

                assertEquals(1, item.getResults().size());
                Parameter itemResults = item.getResults().get(0);
                System.out.println("出参===============");
                System.out.println("resultName=========" + itemResults.getName());
                System.out.println("resultType=========" + itemResults.getType());
                assertEquals("java.lang.String", itemResults.getType());
                System.out.println("resultArray=========" + itemResults.isArray());
                assertFalse(itemResults.isArray());
                assertNull(itemResults.getCollectionType());
            } else if ("listService".equals(service.getMethodName())) {
                assertEquals(1, service.getInputParameters().size());
                System.out.println("入参列表===============");
                for (Parameter inputParam : service.getInputParameters()) {
                    System.out.println("name=========" + inputParam.getName());
                    assertEquals("list", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertFalse(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("java.lang.String", inputParam.getType());
                    System.out.println("collectionType=========" + inputParam.getCollectionType());
                    assertEquals("java.util.List", inputParam.getCollectionType());
                }

                Parameter result = service.getOutputParameter();
                assertNotNull(result);
                System.out.println("出参===============");
                System.out.println("resultName=========" + result.getName());
                System.out.println("resultType=========" + result.getType());
                assertEquals("java.lang.String", result.getType());
                System.out.println("resultArray=========" + result.isArray());
                assertFalse(result.isArray());
                System.out.println("collectionType=========" + result.getCollectionType());
                assertEquals("java.util.List", result.getCollectionType());

                System.out.println("ItemParam列表===============");
                for (Parameter inputParam : item.getParameters()) {
                    System.out.println("name=========" + inputParam.getName());
                    assertEquals("list", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertFalse(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("java.lang.String", inputParam.getType());
                    System.out.println("collectionType=========" + inputParam.getCollectionType());
                    assertEquals("java.util.List", inputParam.getCollectionType());
                }

                assertEquals(1, item.getResults().size());
                Parameter itemResults = item.getResults().get(0);
                System.out.println("出参===============");
                System.out.println("resultName=========" + itemResults.getName());
                System.out.println("resultType=========" + itemResults.getType());
                assertEquals("java.lang.String", itemResults.getType());
                System.out.println("resultArray=========" + itemResults.isArray());
                assertFalse(itemResults.isArray());
                System.out.println("collectionType=========" + itemResults.getCollectionType());
                assertEquals("java.util.List", result.getCollectionType());
            } else if ("arrayService".equals(service.getMethodName())) {
                assertEquals(1, service.getInputParameters().size());
                System.out.println("入参列表===============");
                for (Parameter inputParam : service.getInputParameters()) {
                    System.out.println("name=========" + inputParam.getName());
                    assertEquals("strs", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertTrue(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("java.lang.String", inputParam.getType());
                    assertNull(inputParam.getCollectionType());
                }

                Parameter result = service.getOutputParameter();
                assertNotNull(result);
                System.out.println("出参===============");
                System.out.println("resultName=========" + result.getName());
                System.out.println("resultType=========" + result.getType());
                assertEquals("java.lang.String", result.getType());
                System.out.println("resultArray=========" + result.isArray());
                assertTrue(result.isArray());
                assertNull(result.getCollectionType());

                System.out.println("ItemParam列表===============");
                for (Parameter inputParam : item.getParameters()) {
                    System.out.println("name=========" + inputParam.getName());
                    assertEquals("strs", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertTrue(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("java.lang.String", inputParam.getType());
                    assertNull(inputParam.getCollectionType());
                }

                assertEquals(1, item.getResults().size());
                Parameter itemResults = item.getResults().get(0);
                System.out.println("出参===============");
                System.out.println("resultName=========" + itemResults.getName());
                System.out.println("resultType=========" + itemResults.getType());
                assertEquals("java.lang.String", itemResults.getType());
                System.out.println("resultArray=========" + itemResults.isArray());
                assertTrue(itemResults.isArray());
                assertNull(itemResults.getCollectionType());
            } else if ("userService".equals(service.getMethodName())) {
                assertEquals(1, service.getInputParameters().size());
                System.out.println("入参列表===============");
                for (Parameter inputParam : service.getInputParameters()) {
                    System.out.println("paramName=========" + inputParam.getName());
                    assertEquals("user", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertFalse(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("org.tinygroup.service.test.classpublisher.TestUser", inputParam.getType());
                    assertNull(inputParam.getCollectionType());
                }

                Parameter result = service.getOutputParameter();
                assertNotNull(result);
                System.out.println("出参===============");
                System.out.println("resultName=========" + result.getName());
                System.out.println("resultType=========" + result.getType());
                assertEquals("org.tinygroup.service.test.classpublisher.TestUser", result.getType());
                System.out.println("resultArray=========" + result.isArray());
                assertFalse(result.isArray());
                assertNull(result.getCollectionType());

                System.out.println("ItemParam列表===============");
                for (Parameter p : item.getParameters()) {
                    System.out.println("paramName=========" + p.getName());
                    assertEquals("user", p.getName());
                    System.out.println("paramArray=========" + p.isArray());
                    assertFalse(p.isArray());
                    System.out.println("paramType=========" + p.getType());
                    assertEquals("org.tinygroup.service.test.classpublisher.TestUser", p.getType());
                    assertNull(p.getCollectionType());
                }

                assertEquals(1, item.getResults().size());
                Parameter itemResults = item.getResults().get(0);
                System.out.println("出参===============");
                System.out.println("resultName=========" + itemResults.getName());
                System.out.println("resultType=========" + itemResults.getType());
                assertEquals("org.tinygroup.service.test.classpublisher.TestUser", itemResults.getType());
                System.out.println("resultArray=========" + itemResults.isArray());
                assertFalse(itemResults.isArray());
                assertNull(itemResults.getCollectionType());
            } else if ("listUserService".equals(service.getMethodName())) {
                assertEquals(1, service.getInputParameters().size());
                System.out.println("入参列表===============");
                for (Parameter inputParam : service.getInputParameters()) {
                    System.out.println("name=========" + inputParam.getName());
                    assertEquals("userList", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertFalse(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("org.tinygroup.service.test.classpublisher.TestUser", inputParam.getType());
                    System.out.println("collectionType=========" + inputParam.getCollectionType());
                    assertEquals("java.util.List", inputParam.getCollectionType());
                }

                Parameter result = service.getOutputParameter();
                assertNotNull(result);
                System.out.println("出参===============");
                System.out.println("resultName=========" + result.getName());
                System.out.println("resultType=========" + result.getType());
                assertEquals("org.tinygroup.service.test.classpublisher.TestUser", result.getType());
                System.out.println("resultArray=========" + result.isArray());
                assertFalse(result.isArray());
                System.out.println("collectionType=========" + result.getCollectionType());
                assertEquals("java.util.List", result.getCollectionType());

                System.out.println("ItemParam列表===============");
                for (Parameter inputParam : item.getParameters()) {
                    System.out.println("name=========" + inputParam.getName());
                    assertEquals("userList", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertFalse(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("org.tinygroup.service.test.classpublisher.TestUser", inputParam.getType());
                    System.out.println("collectionType=========" + inputParam.getCollectionType());
                    assertEquals("java.util.List", inputParam.getCollectionType());
                }

                assertEquals(1, item.getResults().size());
                Parameter itemResults = item.getResults().get(0);
                System.out.println("出参===============");
                System.out.println("resultName=========" + itemResults.getName());
                System.out.println("resultType=========" + itemResults.getType());
                assertEquals("org.tinygroup.service.test.classpublisher.TestUser", itemResults.getType());
                System.out.println("resultArray=========" + itemResults.isArray());
                assertFalse(itemResults.isArray());
                System.out.println("collectionType=========" + itemResults.getCollectionType());
                assertEquals("java.util.List", result.getCollectionType());
            } else if ("arrayUserService".equals(service.getMethodName())) {
                assertEquals(1, service.getInputParameters().size());
                System.out.println("入参列表===============");
                for (Parameter inputParam : service.getInputParameters()) {
                    System.out.println("name=========" + inputParam.getName());
                    assertEquals("users", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertTrue(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("org.tinygroup.service.test.classpublisher.TestUser", inputParam.getType());
                    assertNull(inputParam.getCollectionType());
                }

                Parameter result = service.getOutputParameter();
                assertNotNull(result);
                System.out.println("出参===============");
                System.out.println("resultName=========" + result.getName());
                System.out.println("resultType=========" + result.getType());
                assertEquals("org.tinygroup.service.test.classpublisher.TestUser", result.getType());
                System.out.println("resultArray=========" + result.isArray());
                assertTrue(result.isArray());
                assertNull(result.getCollectionType());

                System.out.println("ItemParam列表===============");
                for (Parameter inputParam : item.getParameters()) {
                    System.out.println("name=========" + inputParam.getName());
                    assertEquals("users", inputParam.getName());
                    System.out.println("paramArray=========" + inputParam.isArray());
                    assertTrue(inputParam.isArray());
                    System.out.println("paramType=========" + inputParam.getType());
                    assertEquals("org.tinygroup.service.test.classpublisher.TestUser", inputParam.getType());
                    assertNull(inputParam.getCollectionType());
                }

                assertEquals(1, item.getResults().size());
                Parameter itemResults = item.getResults().get(0);
                System.out.println("出参===============");
                System.out.println("resultName=========" + itemResults.getName());
                System.out.println("resultType=========" + itemResults.getType());
                assertEquals("org.tinygroup.service.test.classpublisher.TestUser", itemResults.getType());
                System.out.println("resultArray=========" + itemResults.isArray());
                assertTrue(itemResults.isArray());
                assertNull(itemResults.getCollectionType());
            }
            System.out.println("=========================================");
        }
    }
}
