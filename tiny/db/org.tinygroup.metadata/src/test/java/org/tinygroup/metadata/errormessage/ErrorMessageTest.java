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
package org.tinygroup.metadata.errormessage;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.metadata.config.errormessage.ErrorMessage;
import org.tinygroup.metadata.config.errormessage.ErrorMessages;
import org.tinygroup.xstream.XStreamFactory;

import java.util.ArrayList;
import java.util.List;

public class ErrorMessageTest {
    public static void main(String[] args) {
        ErrorMessages errorMessages = new ErrorMessages();
        errorMessages.setPackageName("aa");
        List<ErrorMessage> errorInfoList = new ArrayList<ErrorMessage>();
        ErrorMessage errorInfo = new ErrorMessage();
        errorInfo.setErrorId("aa");
        errorInfo.setErrorMessageI18nKey("111");
        errorInfo.setResolutionI18nKey("aa");
        errorInfoList.add(errorInfo);
        errorMessages.setErrorMessageList(errorInfoList);
        XStream stream = XStreamFactory.getXStream();
        stream.autodetectAnnotations(true);
        stream.processAnnotations(ErrorMessages.class);
        System.out.println(stream.toXML(errorMessages));
    }
}
