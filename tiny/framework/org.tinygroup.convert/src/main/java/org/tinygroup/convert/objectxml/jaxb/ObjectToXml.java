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
package org.tinygroup.convert.objectxml.jaxb;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class ObjectToXml<T> implements Converter<T, String> {
    private JAXBContext context;
    private boolean format;
    private Marshaller marshaller;

    public ObjectToXml(String className, boolean format) throws ConvertException {
        try {
            context = JAXBContext.newInstance(className);
            marshaller = context.createMarshaller();
            this.format = format;
        } catch (JAXBException e) {
            throw new ConvertException(e);
        }
    }

    public ObjectToXml(Class<T> clazz, boolean format) throws ConvertException {
        try {
            context = JAXBContext.newInstance(clazz);
            marshaller = context.createMarshaller();
            this.format = format;
        } catch (JAXBException e) {
            throw new ConvertException(e);
        }
    }

    public String convert(T inputData) throws ConvertException {

        StringWriter writer = new StringWriter();

        try {
            if (format) {
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                        Boolean.TRUE);
            }
            marshaller.marshal(inputData, writer);

        } catch (Exception e) {
            throw new ConvertException(e);
        }
        return writer.toString();
    }

}
