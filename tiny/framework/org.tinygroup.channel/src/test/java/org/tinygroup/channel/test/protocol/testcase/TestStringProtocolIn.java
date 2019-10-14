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
package org.tinygroup.channel.test.protocol.testcase;

import org.tinygroup.channel.protocol.impl.BaseProtocolIn;
import org.tinygroup.channel.test.protocol.ObjectProtocolInListener;
import org.tinygroup.channel.test.protocol.simple.StringProcess;
import org.tinygroup.channel.test.protocol.simple.StringProtocolInListener;
import org.tinygroup.channel.test.protocol.simple.StringProtocolInListener2;
import org.tinygroup.channel.test.protocol.simple.StringTrans;


public class TestStringProtocolIn {
    public static void main(String[] args) {
        BaseProtocolIn<String, String> in = new BaseProtocolIn<String, String>();
        in.setProtocolProcess(new StringProcess());
        in.setProtocolTrans(new StringTrans());
        in.addProtocolListener(new StringProtocolInListener());
        in.addProtocolListener(new StringProtocolInListener2());
        in.addProtocolListener(new ObjectProtocolInListener());
        System.out.println(in.received("hello"));
    }
}
