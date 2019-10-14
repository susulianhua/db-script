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
package org.tinygroup.message.email;

import org.tinygroup.message.*;

import javax.mail.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by luoguo on 2014/4/21.
 */
public class EmailMessageSendServiceTest {
    public static void main(String[] args) throws IOException, MessageException {
        MessageManager<EmailMessageAccount, EmailMessageSender, EmailMessageReceiver, EmailMessage> messageManager = new MessageManagerDefault();
        EmailMessageAccount account = new EmailMessageAccount();
        account.setHost("smtp.126.com");
        account.setUsername("tinygroup@126.com");
        account.setPassword("T336syhd");
        EmailMessageSendService sendService = new EmailMessageSendService();
        EmailMessageSender messageSender = new EmailMessageSender();
        messageSender.setDisplayName("Ump消息平台");
        messageSender.setEmail("tinygroup@126.com");
        EmailMessageReceiver messageReceiver = new EmailMessageReceiver();
        messageReceiver.setDisplayName("码农");
        messageReceiver.setEmail("jayzch@qq.com");
        EmailMessageReceiver messageReceiver2 = new EmailMessageReceiver();
        messageReceiver2.setDisplayName("码农2");
        messageReceiver2.setEmail("incareer@qq.com");
        messageReceiver.setType(Message.RecipientType.CC);
        messageReceiver2.setType(Message.RecipientType.CC);
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setSubject("高效快22速");
        emailMessage.setContent("中华人民共和国");
//        EmailAccessory accessory = new EmailAccessory(new File("D:/RUNNING.txt"));
//        emailMessage.getAccessories().add(accessory);
        messageManager.setMessageAccount(account);
        MessageReceiveService messageReceiveService = new EmailMessageReceiveService();
        MessageSendService messageSendService = new EmailMessageSendService();
        messageManager.setMessageReceiveService(messageReceiveService);
        messageManager.setMessageSendService(messageSendService);
        Collection<EmailMessageReceiver> receivers = new ArrayList<EmailMessageReceiver>();
        receivers.add(messageReceiver);
        receivers.add(messageReceiver2);
        messageManager.setMessageSendService(sendService);
        messageManager.sendMessage(messageSender, receivers, emailMessage);
    }


}
