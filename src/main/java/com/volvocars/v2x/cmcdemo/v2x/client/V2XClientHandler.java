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

package com.volvocars.v2x.cmcdemo.v2x.client;

import com.volvocars.v2x.cmcdemo.events.UDPClientRawMsgEvent;
import com.volvocars.v2x.cmcdemo.v2x.V2XClientServer;
import com.volvocars.v2x.cmcdemo.v2x.V2XClientUtils;
import com.volvocars.v2x.cmcdemo.v2x.msg.V2XRowPackage;
import com.volvocars.v2x.cmcdemo.v2x.vo.CarAuthorizationVO;
import io.e2x.logger.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;

public class V2XClientHandler extends SimpleChannelInboundHandler<DatagramPacket> implements ApplicationListener<UDPClientRawMsgEvent> {

    Logger logger = new Logger(this);
    private V2XClientServer v2XClientServer;
    private ApplicationEventPublisher publisher;

    public V2XClientHandler() {
        super();
    }

    public V2XClientHandler(V2XClientServer server) {
        v2XClientServer = server;
        publisher = v2XClientServer.publisher;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        // TODO 不确定服务端是否有response 所以暂时先不用处理
        byte[] content = new byte[packet.content().readableBytes()];
        packet.content().getBytes(0,content);
        onApplicationEvent(new UDPClientRawMsgEvent(this,content));
    }

    @Override
    public void onApplicationEvent(UDPClientRawMsgEvent udpClientRawMsgEvent) {
        CarAuthorizationVO auth = v2XClientServer.getV2XAuth();
        try {
            //logger.info(new String(udpClientRawMsgEvent.getData()));
            String resultString  = V2XClientUtils.decode(udpClientRawMsgEvent.getData(),auth.key);
            V2XRowPackage rowPackage = new V2XRowPackage(resultString);
            logger.info(resultString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
