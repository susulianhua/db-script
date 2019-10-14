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
package org.tinygroup.weixin.util;

/**
 * 微信事件枚举
 *
 * @author yancheng11334
 */
public enum WeiXinEventMode {

    /**
     * 用户订阅事件
     */
    SUBSCRIBE("subscribe"),
    /**
     * 退订事件
     */
    UNSUBSCRIBE("unsubscribe"),
    /**
     * 扫描事件
     */
    SCAN("SCAN"),
    /**
     * 自动上传位置
     */
    LOCATION("LOCATION"),
    /**
     * 点击事件
     */
    CLICK("CLICK"),
    /**
     * 跳转事件
     */
    VIEW("VIEW"),
    /**
     * 模板消息推送事件
     */
    TEMPLATE_SEND_JOB_FINISH("TEMPLATESENDJOBFINISH"),
    /**
     * 群发消息推送事件
     */
    MASS_SEND_JOB_FINISH("MASSSENDJOBFINISH"),
    /**
     * 扫码推事件
     */
    SCANCODE_PUSH("scancode_push"),
    /**
     * 扫码推事件且弹出“消息接收中”提示框
     */
    SCANCODE_WAITMSG("scancode_waitmsg"),
    /**
     * 弹出系统拍照发图
     */
    PIC_SYSPHOTO("pic_sysphoto"),
    /**
     * 弹出拍照或者相册发图
     */
    PIC_PHOTO_OR_ALBUM("pic_photo_or_album"),
    /**
     * 弹出微信相册发图器
     */
    PIC_WEIXIN("pic_weixin"),
    /**
     * 弹出地理位置选择器
     */
    LOCATION_SELECT("location_select"),
    MEDIA_ID("media_id"),
    VIEW_LIMITED("view_limited");

    private final String event;

    private WeiXinEventMode(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

}
