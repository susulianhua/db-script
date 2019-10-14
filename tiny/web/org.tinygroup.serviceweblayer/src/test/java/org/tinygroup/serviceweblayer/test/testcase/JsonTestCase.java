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
package org.tinygroup.serviceweblayer.test.testcase;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.serviceweblayer.processor.JsonServiceTinyProcessor;
import org.tinygroup.tinyrunner.Runner;

public class JsonTestCase extends TestCase {

    public void testJson() {
        Runner.init(null, null);
        JsonServiceTinyProcessor processor = BeanContainerFactory
                .getBeanContainer(JsonTestCase.class.getClassLoader()).getBean(
                        JsonServiceTinyProcessor.BEAN_NAME);
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceChar\"},\"body\":{\"i\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceCharArray\"},\"body\":{\"i\":[\"a\",\"b\"]}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceCharacter\"},\"body\":{\"i\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceCharacterArray\"},\"body\":{\"i\":[\"a\",\"b\"]}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceCharacterList\"},\"body\":{\"i\":[\"a\",\"b\"]}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceInt\"},\"body\":{\"i\":1}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceIntArray\"},\"body\":{\"i\":[1,2,3,4],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceBoolean\"},\"body\":{\"i\":true,\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceBooleanArray\"},\"body\":{\"i\":[true,false,true,false],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceInteger\"},\"body\":{\"i\":1,\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceIntegerArray\"},\"body\":{\"i\":[1,2,3,4],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceIntegerList\"},\"body\":{\"i\":[1,2,3,4],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicWrapperServiceBoolean\"},\"body\":{\"i\":true,\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicWrapperServiceBooleanList\"},\"body\":{\"i\":[true,false,true,false],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"objectServiceGrade\"},\"body\":{\"grade\":{\"name\":\"a\",\"age\":1},\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"objectServiceGradeList\"},\"body\":{\"grade\":[{\"name\":\"a\",\"age\":1},{\"name\":\"b\",\"age\":2},{\"name\":\"c\",\"age\":3}],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"objectServiceGradeArray\"},\"body\":{\"grade\":[{\"name\":\"a\",\"age\":1},{\"name\":\"b\",\"age\":2},{\"name\":\"c\",\"age\":3}],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceByte\"},\"body\":{\"b\":85}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceByteArray\"},\"body\":{\"bs\":[85,6]}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceWrapperByte\"},\"body\":{\"b\":85}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceWrapperByteArray\"},\"body\":{\"bs\":[85,6]}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceWrapperByteList\"},\"body\":{\"bs\":[85,6]}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceFloat\"},\"body\":{\"f\":1.2}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceFloatArray\"},\"body\":{\"f\":[1.2,1.3]}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceWrapperFloat\"},\"body\":{\"f\":1.2}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceWrapperFloatArray\"},\"body\":{\"f\":[1.2,1.3]}}");
        process(processor, "{\"head\":{\"serviceid\":\"basicServiceWrapperFloatList\"},\"body\":{\"f\":[1.2,1.3]}}");
        process(processor, "{\"head\":{\"serviceid\":\"objectServiceSchool\"},\"body\":{\"school\":{\"str\":\"abc\",\"listStr\":[{\"str\":\"a\",\"str\":\"b\"}],\"grade\":{\"name\":\"a\",\"age\":1},\"gradeList\":[{\"name\":\"b\",\"age\":2},{\"name\":\"c\",\"age\":3}],\"gradeArray\":[{\"name\":\"d\",\"age\":4},{\"name\":\"e\",\"age\":5}]}}}");
        process(processor, "{\"head\":{\"serviceid\":\"objectServiceSchoolList\"},\"body\":{\"school\":[{\"str\":\"abc\",\"listStr\":[{\"str\":\"b\"}],\"grade\":{\"name\":\"a\",\"age\": 1},\"gradeList\": [{\"name\": \"b\",\"age\": 2},{\"name\": \"c\",\"age\": 3}],\"gradeArray\": [{\"name\": \"d\",\"age\": 4},{\"name\": \"e\",\"age\": 5}]},{\"str\": \"abc\",\"listStr\": [{\"str\": \"b\"}],\"grade\": {\"name\": \"a\",\"age\": 1},\"gradeList\": [{\"name\": \"b\",\"age\": 2},{\"name\": \"c\",\"age\": 3}],\"gradeArray\": [{\"name\": \"d\",\"age\": 4},{\"name\": \"e\",\"age\": 5}]}]}}");
        process(processor, "{\"head\":{\"serviceid\":\"objectServiceSchoolArray\"},\"body\":{\"school\":[{\"str\":\"abc\",\"listStr\":[{\"str\":\"b\"}],\"grade\":{\"name\":\"a\",\"age\": 1},\"gradeList\": [{\"name\": \"b\",\"age\": 2},{\"name\": \"c\",\"age\": 3}],\"gradeArray\": [{\"name\": \"d\",\"age\": 4},{\"name\": \"e\",\"age\": 5}]},{\"str\": \"abc\",\"listStr\": [{\"str\": \"b\"}],\"grade\": {\"name\": \"a\",\"age\": 1},\"gradeList\": [{\"name\": \"b\",\"age\": 2},{\"name\": \"c\",\"age\": 3}],\"gradeArray\": [{\"name\": \"d\",\"age\": 4},{\"name\": \"e\",\"age\": 5}]}]}}");
        assertTrue(true);
    }

    private void process(JsonServiceTinyProcessor processor, String json) {
        System.out.println("request:");
        System.out.println("		" + json);
        String result = processor.process(json);
        System.out.println("result:");
        System.out.println("		" + result);
        assertFalse(result.indexOf("交易成功") == -1);
    }
}
