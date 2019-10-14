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
package org.tinygroup.channel.test.protocol.user;

import org.tinygroup.channel.protocol.ProtocolTrans;
import org.tinygroup.channel.test.protocol.pojo.User;
import org.tinygroup.context.Context;

public class UserTrans implements ProtocolTrans<User, String, String, User> {

    public User transReqFromMessage(String reqMessage, Context context) {

        return User.parse(reqMessage);
    }

    public String transBizReq2ProReq(User bizRequest, Context context) {
        return bizRequest.toString();
    }

    public User transProReq2BizReq(String proRequest, Context context) {
        return User.parse(proRequest);
    }

    public User transProRes2BizRes(String proResponse, Context context) {
        return User.parse(proResponse);
    }

    public String transBizRes2ProRes(User bizResponse, Context context) {
        return bizResponse.toString();
    }

}
