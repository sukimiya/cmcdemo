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
import com.volvocars.v2x.cmcdemo.v2x.vo.CarBSMUploadEvent;
import com.volvocars.v2x.service.common.util.MD5Utils;
import io.e2x.logger.Logger;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

public class V2XClientNet implements ApplicationListener<UDPClientRawMsgEvent>{

    private Mono<? extends NettyContext> clientContext;
    private UdpOutbound clientOut;
    private UdpInbound clientIn;
    private BSMSentSubscriber sentSubscriber = new BSMSentSubscriber();
    private UdpClient udpClient;
    private OioEventLoopGroup channels = new OioEventLoopGroup();
    @Autowired
    public V2XClientServer v2XClientServer;

    @Autowired
    private ApplicationContext publisher;

    private TcpClient bsmServer;
    private static final Charset ASCII = Charset.forName("utf-8");

    private InetSocketAddress host;

    //
    public V2XClientNet(String host,int port) {
        InetSocketAddress address = new InetSocketAddress(host,port);
        this.host = address;
        clientContext = UdpClient.create(builder ->
            builder.port(this.host.getPort()).host(this.host.getHostString())
                    .channelGroup(new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE))
                    .build()
        ).newHandler(this::inOutBoundHandler);
        clientContext.subscribe();
        //udpconnection.inbound().receive();

//        UdpServer.create().host("localhost").port(28120).bind().block().inbound()
//        .receive().asByteArray().subscribe(bytes -> {
//            try {
//                String decoders = AESUtils.aesDecryptByBytes(bytes,v2XClientServer.getV2XAuth().key);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }
    private Mono<Void> inOutBoundHandler(UdpInbound udpInbound, UdpOutbound udpOutbound){
        clientOut = udpOutbound;
        clientIn = udpInbound;
        clientOut.options(NettyPipeline.SendOptions::flushOnEach);
        return Mono.empty();
    }
    private UdpOutbound send(byte[] bytes,UdpInbound udpInbound, UdpOutbound udpOutbound){
        clientOut = udpOutbound;
        clientIn = udpInbound;
        clientOut.join(host.getAddress());
        return (UdpOutbound) clientOut.sendByteArray(Mono.just(bytes));
    }

    public void send(BSMFrame frame) throws InterruptedException {
        CarBSMUploadEvent carBSMUploadEvent = new CarBSMUploadEvent(frame,v2XClientServer.getV2XAuth().key);
        carBSMUploadEvent.serialize();
        Logger logger = new Logger(this);
        clientOut.sendByteArray(Mono.just(carBSMUploadEvent.getOutputstream())).subscribe(sentSubscriber);
    }

    @Override
    public void onApplicationEvent(UDPClientRawMsgEvent udpClientRawMsgEvent) {

    }
}
class BSMSentSubscriber implements Subscriber<Void>{
    Logger logger = new Logger(this);
    @Override
    public void onSubscribe(Subscription subscription) {

    }

    @Override
    public void onNext(Void aVoid) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {
        logger.debug("BSM Sent Complete["+System.currentTimeMillis()+"]");
    }
}