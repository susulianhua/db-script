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
package org.tinygroup.weixintest;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.tinytestutil.AbstractTestUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.weixin.WeiXinConnector;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.common.ToServerMessage;
import org.tinygroup.weixin.impl.WeiXinContextDefault;
import org.tinygroup.weixin.result.AccessToken;
import org.tinygroup.weixinhttp.WeiXinHttpUpload;
import org.tinygroup.weixinmeterial.message.*;
import org.tinygroup.weixinmeterial.result.*;

/**
 * 素材上传测试
 *
 * @author yancheng11334
 */
public class MeterialTest {

    private static WeiXinConnector weiXinConnector;

    static {
        AbstractTestUtil.init(null, false);
        weiXinConnector = BeanContainerFactory.getBeanContainer(KfMessageTest.class.getClassLoader()).getBean("weiXinConnector");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        AccessToken token = weiXinConnector.getAccessToken();
        Assert.assertNotNull(token);

        testGetMeterialNums();
        testGetMeterialList();

        //testUploadTempImage();
        //testUploadTempThumb();
        //testUploadTempVoice();
        //testUploadTempVideo();

        //testUploadPermanentImage();
        //testUploadPermanentThumb();
        //testUploadPermanentVoice();
        testUploadPermanentVideo();
    }

    public static void testGetMeterialNums() {
        MaterialNumsResult result = send(new GetMaterialNumsMessage());
        Assert.assertTrue(result.getImageNums() > 0);
    }

    public static void testGetMeterialList() {
        NewsListMessage newsMessage = new NewsListMessage();
        newsMessage.setCount(10);
        NewsListResult result = send(newsMessage);
        Assert.assertTrue(result.getTotalNum() > 0);

        ImageListMessage iamgeMessage = new ImageListMessage();
        iamgeMessage.setCount(5);
        OtherListResult result2 = send(iamgeMessage);
        Assert.assertTrue(result2.getTotalNum() > 0);
    }

    /**
     * 上传临时图片
     */
    public static void testUploadTempImage() {
        FileObject file = VFS.resolveFile("src/test/resources/file/image.jpg");
        TempImageMessage upload = new TempImageMessage(file);
        TempImageResult result = upload(upload);
        Assert.assertTrue(result.getType().equals("image"));
        Assert.assertTrue(result.getMediaId() != null);
    }

    /**
     * 上传临时缩略图
     */
    public static void testUploadTempThumb() {
        FileObject file = VFS.resolveFile("src/test/resources/file/thumb.jpg");
        TempThumbMessage upload = new TempThumbMessage(file);
        TempThumbResult result = upload(upload);
        Assert.assertTrue(result.getType().equals("thumb"));
        Assert.assertTrue(result.getThumbMediaId() != null);
    }

    /**
     * 上传临时视频
     */
    public static void testUploadTempVideo() {
        FileObject file = VFS.resolveFile("src/test/resources/file/video.mp4");
        TempVideoMessage upload = new TempVideoMessage(file);
        TempVideoResult result = upload(upload);
        Assert.assertTrue(result.getType().equals("video"));
        Assert.assertTrue(result.getMediaId() != null);
    }

    /**
     * 上传临时语音
     */
    public static void testUploadTempVoice() {
        FileObject file = VFS.resolveFile("src/test/resources/file/voice.amr");
        TempVoiceMessage upload = new TempVoiceMessage(file);
        TempVoiceResult result = upload(upload);
        Assert.assertTrue(result.getType().equals("voice"));
        Assert.assertTrue(result.getMediaId() != null);
    }

    /**
     * 上传永久图片
     */
    public static void testUploadPermanentImage() {
        FileObject file = VFS.resolveFile("src/test/resources/file/image.jpg");
        PermanentImageMessage upload = new PermanentImageMessage(file);
        PermanentUrlResult result = upload(upload);
        Assert.assertTrue(result.getUrl() != null);
        Assert.assertTrue(result.getMediaId() != null);
    }

    /**
     * 上传永久缩略图
     */
    public static void testUploadPermanentThumb() {
        FileObject file = VFS.resolveFile("src/test/resources/file/thumb.jpg");
        PermanentThumbMessage upload = new PermanentThumbMessage(file);
        PermanentUrlResult result = upload(upload);
        Assert.assertTrue(result.getUrl() != null);
        Assert.assertTrue(result.getMediaId() != null);
    }

    /**
     * 上传永久语音
     */
    public static void testUploadPermanentVoice() {
        FileObject file = VFS.resolveFile("src/test/resources/file/voice.amr");
        PermanentVoiceMessage upload = new PermanentVoiceMessage(file);
        PermanentMediaIdResult result = upload(upload);
        Assert.assertTrue(result.getMediaId() != null);
    }

    /**
     * 上传永久视频
     */
    public static void testUploadPermanentVideo() {
        FileObject file = VFS.resolveFile("src/test/resources/file/video.mp4");
        PermanentVideoMessage upload = new PermanentVideoMessage(file, "视频", "宠物视频");
        PermanentMediaIdResult result = upload(upload);
        Assert.assertTrue(result.getMediaId() != null);
    }

    private static <OUTPUT> OUTPUT send(ToServerMessage message) {
        WeiXinContext context = new WeiXinContextDefault();
        context.put(WeiXinConnector.ACCESS_TOKEN, weiXinConnector.getAccessToken().getAccessToken());
        weiXinConnector.getWeiXinSender().send(message, context);
        return context.getOutput();
    }

    private static <OUTPUT> OUTPUT upload(WeiXinHttpUpload upload) {
        WeiXinContext context = new WeiXinContextDefault();
        context.put(WeiXinConnector.ACCESS_TOKEN, weiXinConnector.getAccessToken().getAccessToken());
        weiXinConnector.getWeiXinSender().upload(upload, context);
        return context.getOutput();
    }

}
