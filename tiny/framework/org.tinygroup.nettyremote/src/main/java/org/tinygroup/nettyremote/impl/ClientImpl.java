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
package org.tinygroup.nettyremote.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.FastThreadLocal;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.nettyremote.Client;
import org.tinygroup.nettyremote.DisconnectCallBack;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientImpl implements Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientImpl.class);
    private final AtomicBoolean ready;// 是否已完成连接
    private final AtomicBoolean start;// 是否已开始启动
    private final AtomicBoolean reConnect;// 连接断开后,是否需要进行重连
    private EventLoopGroup group = new NioEventLoopGroup();
    private int remotePort;// 需要连接的远程端口
    private String remoteHost;// 需要连接的远程地址
    private int reConnectInterval = 10; // 单位:秒
    private DisconnectCallBack callBack;
    private Thread connectThread = new Thread(new ConnectThread());
    private ChannelFuture future;

    public ClientImpl(int remotePort, String remoteHost, boolean reConnect) {
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
        this.reConnect = new AtomicBoolean(reConnect);
        this.ready = new AtomicBoolean(false);
        this.start = new AtomicBoolean(false);
    }

    public void start() {
        LOGGER.logMessage(LogLevel.INFO, "启动客户端线程连接服务端{0}:{1}", remoteHost, remotePort);
        connectThread.setDaemon(true);
        connectThread.start();


    }

    public void write(Object o) {
        if (o instanceof Event) {
            Event event = (Event) o;
            LOGGER.logMessage(LogLevel.DEBUG, "写出消息为:eventId:{},serviceId:{}", event.getEventId(),
                    event.getServiceRequest().getServiceId());
        }
        if (future == null || future.channel() == null) {
            throw new RuntimeException("连接尚未就绪");
        }
        if (!future.channel().isActive()) {
            throw new RuntimeException("连接未就绪或者已经被关闭");
        }
        ChannelFuture f = future.channel().writeAndFlush(o);
        if (f instanceof ChannelPromise) {
            ChannelPromise p = (ChannelPromise) f;
            try {
                p.await();
            } catch (InterruptedException e) {
                LOGGER.logMessage(LogLevel.WARN, "等待消息写出被中断");
            } finally {
                FastThreadLocal.removeAll();
            }
            if (p.isSuccess()) {
                LOGGER.logMessage(LogLevel.DEBUG, "消息写出状态：{}", p.isSuccess());
            } else {
                LOGGER.logMessage(LogLevel.WARN, "消息写出状态：false");
                throw new RuntimeException(p.cause());
            }

        }

    }

    private void connect(final int port, final String host) throws InterruptedException {
        LOGGER.logMessage(LogLevel.INFO, "开始连接服务端{0}:{1}", remoteHost, remotePort);
        // 配置客户端NIO线程组
        Bootstrap b = new Bootstrap();
        b.group(group).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
//		b.group(NettyRemoteUtil.group).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        init(b);
        // 发起异步连接操作
        try {
            if (reConnect.get()) {
                future = b.connect(host, port).addListener(new ReconnectFuture()).sync();
            } else {
                future = b.connect(host, port).addListener(new SimpleFuture()).sync();
            }
        } catch (Exception e) {
        }
        FastThreadLocal.removeAll();
    }

    protected void init(Bootstrap b) {
        b.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                ch.pipeline().addLast("MessageEncoder", new ObjectEncoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });

    }

    protected void beginStop() {
        reConnect.set(false);
    }

    public void stop() {
        LOGGER.logMessage(LogLevel.INFO, "关闭客户端");
        ready.set(false);
        reConnect.set(false);
        start.set(false);
        try {
            if (future != null && future.channel() != null) {
                future.channel().close();
            }
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.INFO, "等待future close时发生异常");
        }
        try {
            group.shutdownGracefully().await(3000);
        } catch (Exception e) {
            LOGGER.errorMessage("关闭Client时出错", e);
        }
        LOGGER.logMessage(LogLevel.INFO, "关闭客户端完成");
    }

    public boolean isReady() {
        if (future == null || future.channel() == null) {
            return false;
        }
        return ready.get();
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public void setCallBack(DisconnectCallBack callBack) {
        this.callBack = callBack;
    }

    public void disconnect() {
        if (ready.compareAndSet(true, false)) {
            LOGGER.logMessage(LogLevel.INFO, "服务端{0}:{1}已断开", remoteHost, remotePort);
        }
        if (reConnect.get()) {
            try {
                TimeUnit.SECONDS.sleep(getReConnectInterval());
            } catch (Exception e2) {
            }
        }
        if (reConnect.get()) {
            try {
                connect(remotePort, remoteHost);
            } catch (InterruptedException e) {
                LOGGER.logMessage(LogLevel.INFO, "与服务端{0}:{1}断开后重连发生异常", remoteHost, remotePort);
            }
            return;
        }
        if (callBack != null) {
            callBack.call();
        }
        FastThreadLocal.removeAll();
    }

    public void connected() {
        if (ready.compareAndSet(false, true)) {
            LOGGER.logMessage(LogLevel.INFO, "服务端{0}:{1}已连接", remoteHost, remotePort);
        }
        if (!start.get()) {
            LOGGER.logMessage(LogLevel.INFO, "并未开启与服务端{0}:{1}的连接逻辑，或者改连接已执行stop,再次stop", remoteHost, remotePort);
            stop();
            LOGGER.logMessage(LogLevel.INFO, "再次stop完成", remoteHost, remotePort);
        }
    }

    protected int getReConnectInterval() {
        return reConnectInterval;
    }

    class ConnectThread implements Runnable {
        public void run() {
            if (start.compareAndSet(false, true)) {
                try {
                    connect(remotePort, remoteHost);
                } catch (InterruptedException e) {
                }
            }

        }
    }

    class ReconnectFuture implements ChannelFutureListener {

        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                LOGGER.errorMessage("连接服务端{0}:{1}失败", future.cause(), remoteHost, remotePort);
                try {
                    TimeUnit.SECONDS.sleep(getReConnectInterval());
                } catch (Exception e2) {
                }
                if (reConnect.get()) {
                    connect(remotePort, remoteHost);
                }

            } else {
                connected();
            }

        }

    }

    class SimpleFuture implements ChannelFutureListener {

        public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
                LOGGER.errorMessage("连接服务端{0}:{1}发生失败", future.cause(), remoteHost, remotePort);
            } else {
                connected();
            }
        }

    }
}
