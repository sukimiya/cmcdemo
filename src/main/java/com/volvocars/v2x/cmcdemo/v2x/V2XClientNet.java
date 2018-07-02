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

import com.volvocars.v2x.cmcdemo.car.vo.BSMFrame;
import com.volvocars.v2x.cmcdemo.v2x.client.V2XClientHandler;
import com.volvocars.v2x.cmcdemo.v2x.vo.CarBSMUploadEvent;
import io.e2x.logger.Logger;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

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
        //channel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
    }
}