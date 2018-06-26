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

import com.couchbase.client.deps.io.netty.channel.socket.DatagramPacket;
import com.volvocars.v2x.cmcdemo.events.UDPClientRawMsgEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class V2XClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)
            throws Exception {
        // TODO 不确定服务端是否有response 所以暂时先不用处理
        byte[] content = packet.content().array();
        String serverMessage = new String(content);
        applicationContext.publishEvent(new UDPClientRawMsgEvent(this,content));
        System.out.print("reserveServerResponse is: "+serverMessage);
    }


}
