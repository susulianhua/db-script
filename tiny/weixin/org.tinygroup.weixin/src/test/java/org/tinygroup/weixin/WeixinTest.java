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
package org.tinygroup.weixin;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.weixin.common.UrlConfig;
import org.tinygroup.weixin.event.*;
import org.tinygroup.weixin.impl.WeiXinContextDefault;
import org.tinygroup.weixin.message.*;
import org.tinygroup.weixin.util.WeiXinParserUtil;

import java.io.File;

public class WeixinTest extends TestCase {

    private WeiXinSender sender;

    protected void setUp() throws Exception {
        AbstractTestUtil.init(null, false);
        sender = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean(WeiXinSender.DEFAULT_BEAN_NAME);
    }

    //验证初始化
    public void testInit() {
        UrlConfig config = sender.getWeiXinManager().getUrl(WeiXinSender.CONNECTOR_URL_KEY);
        assertNotNull(config);
    }

    //验证加密消息
    public void testEncryptMessage() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/EncryptMessage.xml"), "utf-8");
        EncryptMessage message = parse(xml);
        assertNotNull(message);
        assertEquals(message.getToUserName(), "toUser");
    }

    //验证文本消息
    public void testTextMessage() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/TextMessage.xml"), "utf-8");
        TextMessage message = parse(xml);
        assertNotNull(message);
        assertEquals(message.getMsgType(), "text");
        assertEquals(message.getMsgId(), 1234567890123456L);
    }

    //验证图片消息
    public void testImageMessage() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/ImageMessage.xml"), "utf-8");
        ImageMessage message = parse(xml);
        assertNotNull(message);
        assertEquals(message.getMsgType(), "image");
        assertEquals(message.getPicUrl(), "this is a url");
    }

    //验证语音消息
    public void testVoiceMessage() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/VoiceMessage.xml"), "utf-8");
        VoiceMessage message = parse(xml);
        assertNotNull(message);
        assertEquals(message.getMsgType(), "voice");
        assertEquals(message.getFormat(), "Format");
    }

    //验证视频消息
    public void testVideoMessage() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/VideoMessage.xml"), "utf-8");
        VideoMessage message = parse(xml);
        assertNotNull(message);
        assertEquals(message.getMsgType(), "video");
        assertEquals(message.getThumbMediaId(), "thumb_media_id");
    }

    //验证小视频消息
    public void testShortVideoMessage() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/ShortVideoMessage.xml"), "utf-8");
        ShortVideoMessage message = parse(xml);
        assertNotNull(message);
        assertEquals(message.getMsgType(), "shortvideo");
        assertEquals(message.getThumbMediaId(), "thumb_media_id");
    }

    //验证地理位置消息
    public void testLocationMessage() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/LocationMessage.xml"), "utf-8");
        LocationMessage message = parse(xml);
        assertNotNull(message);
        assertEquals(message.getMsgType(), "location");
        assertEquals(message.getLocationX(), 23.134521D);
        assertEquals(message.getLocationY(), 113.358803D);
    }

    //验证链接消息
    public void testLinkMessage() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/LinkMessage.xml"), "utf-8");
        LinkMessage message = parse(xml);
        assertNotNull(message);
        assertEquals(message.getMsgType(), "link");
        assertEquals(message.getTitle(), "公众平台官网链接");
    }

    //验证关注事件
    public void testSubscribeEvent() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/SubscribeEvent.xml"), "utf-8");
        SubscribeEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getEvent(), "subscribe");
    }


    //验证关注事件(用户未关注时，进行关注后的事件推送)
    public void testSubscribeEvent2() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/SubscribeEvent2.xml"), "utf-8");
        SubscribeEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getEvent(), "subscribe");
        assertEquals(event.getTicket(), "TICKET");
    }

    //验证关注事件(用户已关注时的事件推送 )
    public void testScanEvent() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/ScanEvent.xml"), "utf-8");
        ScanEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getEvent(), "SCAN");
        assertEquals(event.getEventKey(), "SCENE_VALUE");
    }

    //验证取消关注事件
    public void testUnSubscribeEvent() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/UnSubscribeEvent.xml"), "utf-8");
        UnSubscribeEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getEvent(), "unsubscribe");
    }

    //上报地理位置事件
    public void testLocationEvent() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/LocationEvent.xml"), "utf-8");
        LocationEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getEvent(), "LOCATION");
        assertEquals(event.getLatitude(), 23.137466d);
        assertEquals(event.getLongitude(), 113.352425d);
        assertEquals(event.getPrecision(), 119.385040d);
    }

    //自定义菜单事件(点击菜单拉取消息时的事件推送)
    public void testClickEvent() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/ClickEvent.xml"), "utf-8");
        ClickEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getEvent(), "CLICK");
        assertEquals(event.getEventKey(), "EVENTKEY");
    }

    //自定义菜单事件(点击菜单跳转链接时的事件推送)
    public void testViewEvent() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/ViewEvent.xml"), "utf-8");
        ViewEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getEvent(), "VIEW");
        assertEquals(event.getEventKey(), "www.qq.com");
    }

    public void testScanAlertEvent() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/ScanAlertEvent.xml"), "utf-8");
        ScanAlertEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getScanCodeInfo().getScanType(), "qrcode");
    }

    public void testScanPushEvent() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/ScanPushEvent.xml"), "utf-8");
        ScanPushEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getScanCodeInfo().getScanType(), "qrcode");
    }

    public void testSelectLocationEvent() throws Exception {
        String xml = FileUtil.readFileContent(new File("src/test/resources/SelectLocationEvent.xml"), "utf-8");
        SelectLocationEvent event = parse(xml);
        assertNotNull(event);
        assertEquals(event.getSendLocationInfo().getLocationX(), 30.289776d);
    }

    private <T> T parse(String xml) {
        return WeiXinParserUtil.parse(xml, new WeiXinContextDefault(), WeiXinConvertMode.RECEIVE);
    }
}
