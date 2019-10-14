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
package org.tinygroup.jdbctemplatedslsession;

import junit.framework.TestCase;

import java.beans.PropertyDescriptor;
import java.util.Map;

public class MappedClassTest extends TestCase {

    public void testMappedClass() {
        BaseMappedClass<User> mappedClass = new BaseMappedClass<MappedClassTest.User>(User.class);
        Map<String, PropertyDescriptor> mappedFields = mappedClass.getMappedFields();
        PropertyDescriptor id = mappedFields.get("id");
        assertNotNull(id);
        PropertyDescriptor nickName = mappedFields.get("nickname");
        assertNotNull(nickName);
        nickName = mappedFields.get("nick_name");
        assertNotNull(nickName);
        PropertyDescriptor nickName2 = mappedFields.get("nickname2");
        assertNotNull(nickName2);
        nickName2 = mappedFields.get("nick_name2");
        assertNotNull(nickName2);
        PropertyDescriptor iphoneQQ = mappedFields.get("iphoneqq");
        assertNotNull(iphoneQQ);
        iphoneQQ = mappedFields.get("iphone_q_q");
        assertNotNull(iphoneQQ);
    }

    class User {
        private int id;
        private String nickName;
        private String nickName2;
        private String iphoneQQ;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getNickName2() {
            return nickName2;
        }

        public void setNickName2(String nickName2) {
            this.nickName2 = nickName2;
        }

        public String getIphoneQQ() {
            return iphoneQQ;
        }

        public void setIphoneQQ(String iphoneQQ) {
            this.iphoneQQ = iphoneQQ;
        }

    }
}
