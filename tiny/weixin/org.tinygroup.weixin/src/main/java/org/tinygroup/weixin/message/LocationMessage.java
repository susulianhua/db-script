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
package org.tinygroup.weixin.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.weixin.util.WeiXinMessageMode;

/**
 * 地理位置消息
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class LocationMessage extends CommonMessage {

    @XStreamAlias("Location_X")
    private double locationX;
    @XStreamAlias("Location_Y")
    private double locationY;
    @XStreamAlias("Scale")
    private int scale;
    @XStreamAlias("Label")
    private String label;

    public LocationMessage() {
        setMsgType(WeiXinMessageMode.LOCATION.getType());
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
