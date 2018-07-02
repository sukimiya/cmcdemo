/*
 * cmcdemo
 * Copyright (C) 2018.  e2x.io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.volvocars.v2x.cmcdemo.v2x;

import com.fendo.MD5.AESUtils;
import com.volvocars.v2x.cmcdemo.car.vo.BSMFrame;
import com.volvocars.v2x.cmcdemo.events.UDPClientRawMsgEvent;
import com.volvocars.v2x.cmcdemo.v2x.client.V2XClientHandler;
import com.volvocars.v2x.cmcdemo.v2x.vo.CarBSMUploadEvent;
import com.volvocars.v2x.service.common.util.MD5Utils;
import io.e2x.logger.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.NettyPipeline;
import reactor.ipc.netty.channel.ContextHandler;
import reactor.ipc.netty.tcp.TcpClient;
import reactor.ipc.netty.udp.UdpClient;
import reactor.ipc.netty.udp.UdpInbound;
import reactor.ipc.netty.udp.UdpOutbound;

import java.net.InetSocketAddress;
import java.nio.channels.NetworkChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

public class V2XClientNet{

    public V2XClientServer v2XClientServer;

    private static final Charset ASCII = Charset.forName("utf-8");

    private InetSocketAddress host;

    private Channel channel;

    //
    public V2XClientNet(String host, int port, V2XClientServer v2XClientServer) {
        InetSocketAddress address = new InetSocketAddress(host,port);
        this.host = address;
        this.v2XClientServer = v2XClientServer;
        startInit();
    }
    private V2XClientChannelInitializer channelInitializer;
    public void startInit(){
        channelInitializer = new V2XClientChannelInitializer(v2XClientServer);
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .remoteAddress(host)
                .handler(channelInitializer);
        try {
            channel = bootstrap.bind(0).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void send(BSMFrame frame){
        CarBSMUploadEvent carBSMUploadEvent = new CarBSMUploadEvent(frame,v2XClientServer.getV2XAuth().key);
        carBSMUploadEvent.serialize();
        Logger logger = new Logger(this);
        send(carBSMUploadEvent.getOutputstream());
    }
    public void send(byte[] bytes){
        channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(bytes), host));
    }
}

class V2XClientChannelInitializer extends ChannelInitializer<NioDatagramChannel>{

    private V2XClientServer server;
    public V2XClientChannelInitializer(V2XClientServer v2XClientServer) {
        server = v2XClientServer;
    }

    @Override
    protected void initChannel(NioDatagramChannel channel) throws Exception {
        channel.pipeline().addLast(new V2XClientHandler(server));
        channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
    }
}