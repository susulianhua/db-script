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
package org.tinygroup.servicehttpchannel;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Hession {
    public static byte[] serialize(Object obj) throws IOException {
        if (obj == null) throw new NullPointerException();
        SerializerFactory serializerFactory = new SerializerFactory();
        serializerFactory.addFactory(new BigDecimalSerializerFactory());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        ho.setSerializerFactory(serializerFactory);
        ho.writeObject(obj);
        return os.toByteArray();
    }

    public static Object deserialize(byte[] by) throws IOException {
        if (by == null) throw new NullPointerException();
        SerializerFactory serializerFactory = new SerializerFactory();
        serializerFactory.addFactory(new BigDecimalSerializerFactory());
        ByteArrayInputStream is = new ByteArrayInputStream(by);
        HessianInput hi = new HessianInput(is);
        hi.setSerializerFactory(serializerFactory);
        return hi.readObject();
    }
}
