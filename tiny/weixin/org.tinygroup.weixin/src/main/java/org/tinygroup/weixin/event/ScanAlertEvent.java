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
package org.tinygroup.weixin.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.weixin.util.WeiXinEventMode;

@XStreamAlias("xml")
public class ScanAlertEvent extends CommonEvent {

    @XStreamAlias("EventKey")
    private String eventKey;
    @XStreamAlias("ScanCodeInfo")
    private ScanCodeInfo scanCodeInfo;

    public ScanAlertEvent() {
        super();
        setEvent(WeiXinEventMode.SCANCODE_WAITMSG.getEvent());
    }

    public ScanCodeInfo getScanCodeInfo() {
        return scanCodeInfo;
    }

    public void setScanCodeInfo(ScanCodeInfo scanCodeInfo) {
        this.scanCodeInfo = scanCodeInfo;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }


}
