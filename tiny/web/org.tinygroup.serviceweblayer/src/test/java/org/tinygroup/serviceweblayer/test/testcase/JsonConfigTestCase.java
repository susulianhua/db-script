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
import org.tinygroup.serviceweblayer.processor.JsonServiceHeadConfigTinyProcessor;
import org.tinygroup.tinyrunner.Runner;

public class JsonConfigTestCase extends TestCase {

    public void testJson() {
        Runner.init(null, null);
        JsonServiceHeadConfigTinyProcessor processor = BeanContainerFactory
                .getBeanContainer(JsonConfigTestCase.class.getClassLoader()).getBean(
                        JsonServiceHeadConfigTinyProcessor.BEAN_NAME);
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceChar\"},\"bizhead\":{}},\"body\":{\"i\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceCharArray\"},\"bizhead\":{}},\"body\":{\"i\":[\"a\",\"b\"]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceCharacter\"},\"bizhead\":{}},\"body\":{\"i\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceCharacterArray\"},\"bizhead\":{}},\"body\":{\"i\":[\"a\",\"b\"]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceCharacterList\"},\"bizhead\":{}},\"body\":{\"i\":[\"a\",\"b\"]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceInt\"},\"bizhead\":{}},\"body\":{\"i\":1}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceIntArray\"},\"bizhead\":{}},\"body\":{\"i\":[1,2,3,4],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceBoolean\"},\"bizhead\":{}},\"body\":{\"i\":true,\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceBooleanArray\"},\"bizhead\":{}},\"body\":{\"i\":[true,false,true,false],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceInteger\"},\"bizhead\":{}},\"body\":{\"i\":1,\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceIntegerArray\"},\"bizhead\":{}},\"body\":{\"i\":[1,2,3,4],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceIntegerList\"},\"bizhead\":{}},\"body\":{\"i\":[1,2,3,4],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicWrapperServiceBoolean\"},\"bizhead\":{}},\"body\":{\"i\":true,\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicWrapperServiceBooleanList\"},\"bizhead\":{}},\"body\":{\"i\":[true,false,true,false],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"objectServiceGrade\"},\"bizhead\":{}},\"body\":{\"grade\":{\"name\":\"a\",\"age\":1},\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"objectServiceGradeList\"},\"bizhead\":{}},\"body\":{\"grade\":[{\"name\":\"a\",\"age\":1},{\"name\":\"b\",\"age\":2},{\"name\":\"c\",\"age\":3}],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"objectServiceGradeArray\"},\"bizhead\":{}},\"body\":{\"grade\":[{\"name\":\"a\",\"age\":1},{\"name\":\"b\",\"age\":2},{\"name\":\"c\",\"age\":3}],\"a\":\"a\"}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceByte\"},\"bizhead\":{}},\"body\":{\"b\":85}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceByteArray\"},\"bizhead\":{}},\"body\":{\"bs\":[85,6]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceWrapperByte\"},\"bizhead\":{}},\"body\":{\"b\":85}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceWrapperByteArray\"},\"bizhead\":{}},\"body\":{\"bs\":[85,6]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceWrapperByteList\"},\"bizhead\":{}},\"body\":{\"bs\":[85,6]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceFloat\"},\"bizhead\":{}},\"body\":{\"f\":1.2}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceFloatArray\"},\"bizhead\":{}},\"body\":{\"f\":[1.2,1.3]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceWrapperFloat\"},\"bizhead\":{}},\"body\":{\"f\":1.2}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceWrapperFloatArray\"},\"bizhead\":{}},\"body\":{\"f\":[1.2,1.3]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"basicServiceWrapperFloatList\"},\"bizhead\":{}},\"body\":{\"f\":[1.2,1.3]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"objectServiceSchool\"},\"bizhead\":{}},\"body\":{\"school\":{\"str\":\"abc\",\"listStr\":[{\"str\":\"a\",\"str\":\"b\"}],\"grade\":{\"name\":\"a\",\"age\":1},\"gradeList\":[{\"name\":\"b\",\"age\":2},{\"name\":\"c\",\"age\":3}],\"gradeArray\":[{\"name\":\"d\",\"age\":4},{\"name\":\"e\",\"age\":5}]}}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"objectServiceSchoolList\"},\"bizhead\":{}},\"body\":{\"school\":[{\"str\":\"abc\",\"listStr\":[{\"str\":\"b\"}],\"grade\":{\"name\":\"a\",\"age\": 1},\"gradeList\": [{\"name\": \"b\",\"age\": 2},{\"name\": \"c\",\"age\": 3}],\"gradeArray\": [{\"name\": \"d\",\"age\": 4},{\"name\": \"e\",\"age\": 5}]},{\"str\": \"abc\",\"listStr\": [{\"str\": \"b\"}],\"grade\": {\"name\": \"a\",\"age\": 1},\"gradeList\": [{\"name\": \"b\",\"age\": 2},{\"name\": \"c\",\"age\": 3}],\"gradeArray\": [{\"name\": \"d\",\"age\": 4},{\"name\": \"e\",\"age\": 5}]}]}}");
        process(processor, "{\"head\":{\"syshead\":{\"serviceid\":\"objectServiceSchoolArray\"},\"bizhead\":{}},\"body\":{\"school\":[{\"str\":\"abc\",\"listStr\":[{\"str\":\"b\"}],\"grade\":{\"name\":\"a\",\"age\": 1},\"gradeList\": [{\"name\": \"b\",\"age\": 2},{\"name\": \"c\",\"age\": 3}],\"gradeArray\": [{\"name\": \"d\",\"age\": 4},{\"name\": \"e\",\"age\": 5}]},{\"str\": \"abc\",\"listStr\": [{\"str\": \"b\"}],\"grade\": {\"name\": \"a\",\"age\": 1},\"gradeList\": [{\"name\": \"b\",\"age\": 2},{\"name\": \"c\",\"age\": 3}],\"gradeArray\": [{\"name\": \"d\",\"age\": 4},{\"name\": \"e\",\"age\": 5}]}]}}");
        assertTrue(true);
    }

    private void process(JsonServiceHeadConfigTinyProcessor processor, String json) {
        System.out.println("request:");
        System.out.println("		" + json);
        String result = processor.process(json);
        System.out.println("result:");
        System.out.println("		" + result);
    }
}
