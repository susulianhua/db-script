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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.ThreadDeathWatcher;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.nettyremote.Server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerImpl implements Server {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ServerImpl.class);
    // private ServerThread serverThread = new ServerThread();
//    private boolean start = false;
    private final AtomicBoolean start;
    private boolean startFailStop = false;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private int localPort;
    private ChannelFuture f;

    public ServerImpl(int localPort, boolean startFailStop) {
        this.localPort = localPort;
        this.startFailStop = startFailStop;
        this.start = new AtomicBoolean(false);

    }

    public void start() {
        LOGGER.logMessage(LogLevel.INFO, "启动服务端,端口:{1}", localPort);
        setStart(false);
        startRun();
        LOGGER.logMessage(LogLevel.INFO, "启动服务端完成,端口:{1}", localPort);
    }

    protected void init(ServerBootstrap b) {
        b.channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    public void initChannel(SocketChannel ch)
                            throws IOException {
                        ch.pipeline().addLast(
                                new ObjectDecoder(ClassResolvers
                                        .cacheDisabled(null)));
                        ch.pipeline().addLast("MessageEncoder",
                                new ObjectEncoder());
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });
    }

    private void bind() throws InterruptedException {

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        init(b);
        // 绑定端口，同步等待成功
        f = b.bind(localPort).sync();
        FastThreadLocal.removeAll();
    }

    public void stop() {
        LOGGER.logMessage(LogLevel.INFO, "关闭服务端");
        if (f != null) {
            try {
                f.channel().close();
                //.closeFuture();
            } catch (Exception e) {
                LOGGER.errorMessage("关闭服务端Channnel时发生异常,端口:{}", e, localPort);
            }

        }
        setStart(false);
        Future bg = null;
        try {
            bg = bossGroup.shutdownGracefully();
        } catch (Exception e) {
            LOGGER.errorMessage("关闭服务端时发生异常,端口:{}", e, localPort);
        }
        Future wg = null;
        try {
            wg = workerGroup.shutdownGracefully();
        } catch (Exception e) {
            LOGGER.errorMessage("关闭服务端时发生异常,端口:{}", e, localPort);
        }
        if (bg != null) {
            try {
                bg.await();
            } catch (InterruptedException ignore) {
                LOGGER.logMessage(LogLevel.INFO,
                        "等待EventLoopGroup shutdownGracefully中断");
            }
        }


        if (wg != null) {
            try {
                wg.await();
            } catch (InterruptedException ignore) {
                LOGGER.logMessage(LogLevel.INFO,
                        "等待EventLoopGroup shutdownGracefully中断");
            }
        }
        try {
            GlobalEventExecutor.INSTANCE.awaitInactivity(5, TimeUnit.SECONDS);
        } catch (InterruptedException e1) {
            LOGGER.logMessage(LogLevel.INFO,
                    "等待GlobalEventExecutor.INSTANCE.awaitInactivity中断");
        }
        try {
            if (ThreadDeathWatcher.awaitInactivity(5, TimeUnit.SECONDS)) {
                LOGGER.logMessage(LogLevel.INFO,
                        "Global event executor finished shutting down.");
            } else {
                LOGGER.logMessage(LogLevel.INFO,
                        "Global event executor failed to shut down.");
            }
        } catch (InterruptedException e) {
            LOGGER.logMessage(LogLevel.INFO,
                    "等待ThreadDeathWatcher.awaitInactivity中断");
        }
        FastThreadLocal.removeAll();
        FastThreadLocal.destroy();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        LOGGER.logMessage(LogLevel.INFO, "关闭服务端完成,端口:{}", localPort);
    }

    public void startRun() {
        if (this.start.compareAndSet(false, true)) {
            try {
                bind();
            } catch (Exception e) {
                LOGGER.errorMessage("服务端启动失败,端口:{}", e, localPort);
                stop();
                if (startFailStop) {
                    throw new RuntimeException("服务端启动失败,端口:" + localPort, e);
                }
            }
        }

    }

    public boolean isStart() {
        return start.get();
    }

    private void setStart(boolean start) {
        this.start.set(start);
    }

}
