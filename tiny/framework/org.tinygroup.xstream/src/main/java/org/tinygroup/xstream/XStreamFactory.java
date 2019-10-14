/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.xstream;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.commons.tools.ClassFiledUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.xstream.config.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoguo
 */
public final class XStreamFactory {

    private static Map<String, XStream> xstreamMap = new HashMap<String, XStream>();

    private XStreamFactory() {
    }

    public static XStream getXStream() {
        return getXStream("");
    }

    public static XStream getXStream(String key) {
        String xstreamKey = key;
        if (key == null) {
            xstreamKey = "";
        }
        XStream xstream = xstreamMap.get(xstreamKey);
        if (xstream == null) {
            xstream = newXStream(null);
            xstreamMap.put(xstreamKey, xstream);
        }
        return xstream;
    }

    public static XStream getXStream(String key, ClassLoader classLoader) {
        String xstreamKey = key;
        if (key == null) {
            xstreamKey = "";
        }
        XStream xstream = xstreamMap.get(xstreamKey);
        if (xstream == null) {
            xstream = newXStream(classLoader);
            xstreamMap.put(xstreamKey, xstream);
        }
        return xstream;
    }

    private static XStream newXStream(ClassLoader classLoader) {
        XStream xstream = new XStream();
        if (classLoader != null) {
            xstream.setClassLoader(classLoader);
        } else {
            xstream.setClassLoader(XStreamFactory.class.getClassLoader());
        }
        xstream.autodetectAnnotations(true);
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.processAnnotations(XStreamConfiguration.class);
        return xstream;
    }

    public static void clear() {
        xstreamMap.clear();
    }

    public static void parse(XStreamConfiguration xstreamConfiguration)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        XStream xStream = getXStream(xstreamConfiguration.getPackageName());
        loadAnnotationClass(xStream, xstreamConfiguration);
        if (xstreamConfiguration.getxStreamClassAliases() != null) {
            processClassAliases(xStream,
                    xstreamConfiguration.getxStreamClassAliases());
        }
    }

    private static void loadAnnotationClass(XStream xStream,
                                            XStreamConfiguration xstreamConfiguration)
            throws ClassNotFoundException {
        if (xstreamConfiguration.getxStreamAnnotationClasses() != null) {
            for (XStreamAnnotationClass annotationClass : xstreamConfiguration
                    .getxStreamAnnotationClasses()) {
                xStream.processAnnotations(Class.forName(annotationClass
                        .getClassName()));
            }
        }
    }

    private static void processClassAliases(XStream xStream,
                                            XStreamClassAliases classAliases) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        if (classAliases.getClassAliases() != null) {
            for (XStreamClassAlias classAlias : classAliases.getClassAliases()) {
                Class<?> clazz = Class.forName(classAlias.getType());
                xStream.alias(classAlias.getAliasName(), clazz);
                processClassAlias(xStream, classAlias, clazz);
            }
        }
    }

    private static void processClassAlias(XStream xStream,
                                          XStreamClassAlias classAlias, Class<?> clazz)
            throws InstantiationException, IllegalAccessException {
        List<XStreamClassPropertyAlias> list = classAlias.getList();
        if (list == null || list.isEmpty()) {
            return;
        }
        for (XStreamClassPropertyAlias propertyAlias : list) {
            processClassProperty(xStream, propertyAlias, clazz);
        }
    }

    private static void processClassProperty(XStream xStream, XStreamClassPropertyAlias propertyAlias, Class<?> clazz) {
        String propertyName = propertyAlias.getPropertyName();
        Field field = ClassFiledUtil.getDeclaredFieldWithParent(clazz, propertyName);
        String aliasName = propertyAlias.getAliasName();
        boolean asTttribute = propertyAlias.isAsTttribute();
        boolean implicit = propertyAlias.isImplicit();
        boolean omit = propertyAlias.isOmit();
        if (omit) {
            xStream.omitField(clazz, propertyName);
        }
        if (implicit && field != null) {
            if (field.getType().isArray()) {
                xStream.addImplicitArray(clazz, propertyName);
            } else if (ClassFiledUtil.implmentInterface(field.getType(),
                    Collection.class)) {
                xStream.addImplicitCollection(clazz, propertyName);
            }
        }
        if (!StringUtil.isBlank(aliasName)) {
            if (!asTttribute) {
                xStream.aliasField(aliasName, clazz, propertyName);
            } else {
                xStream.aliasAttribute(clazz, propertyName, aliasName);
            }

        }

    }
//
//	private static void processPropertyOmit(XStream xStream,
//			XStreamClassAlias classAlias, Class<?> clazz) {
//		for (XStreamPropertyOmit propertyOmit : classAlias.getPropertyOmits()) {
//			xStream.omitField(clazz, propertyOmit.getAttributeName());
//		}
//	}
//	private static void processPropertyImplicit(XStream xStream,
//			XStreamClassAlias classAlias, Class<?> clazz)
//			throws InstantiationException, IllegalAccessException {
//		
//		for (XStreamPropertyImplicit propertyImplicit : classAlias
//				.getPropertyImplicits()) {
//			String name = propertyImplicit.getAttributeName();
//			Field field = ClassFiledUtil.getDeclaredFieldWithParent(clazz, name);
//			if(field == null){
//				continue;
//			}
//			if(field.getType().isArray()){
//				xStream.addImplicitArray(clazz, name);
//			}else if(ClassFiledUtil.implmentInterface(field.getType(),
//                    Collection.class)){
//				xStream.addImplicitCollection(clazz, name);
//			}
//		}
//	}
//	private static void processPropertyAlias(XStream xStream,
//			XStreamClassAlias classAlias, Class<?> clazz) {
//		for (XStreamPropertyAlias propertyAlias : classAlias.getProperAliases()) {
//			xStream.aliasAttribute(clazz, propertyAlias.getAttributeName(),
//					propertyAlias.getAliasName());
//		}
//	}
}
