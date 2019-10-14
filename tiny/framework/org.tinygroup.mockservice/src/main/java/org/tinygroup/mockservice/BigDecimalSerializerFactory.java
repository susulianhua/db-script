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
package org.tinygroup.mockservice;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

import java.math.BigDecimal;

public class BigDecimalSerializerFactory extends AbstractSerializerFactory {
    private BigDecimalSerializer bigDecimalSerializer = new BigDecimalSerializer();
    private BigDecimalDeserializer bigDecimalDeserializer = new BigDecimalDeserializer();

    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        if (BigDecimal.class.isAssignableFrom(cl)) {
            return bigDecimalSerializer;
        }
        return null;
    }

    @Override
    public Deserializer getDeserializer(Class cl)
            throws HessianProtocolException {
        if (BigDecimal.class.isAssignableFrom(cl)) {
            return bigDecimalDeserializer;
        }
        return null;
    }
}
