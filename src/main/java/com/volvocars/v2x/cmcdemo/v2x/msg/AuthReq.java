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

package com.volvocars.v2x.cmcdemo.v2x.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvocars.v2x.cmcdemo.v2x.V2XClientUtils;
import io.e2x.logger.Logger;
import io.e2x.utils.JsonUtils;
import org.slf4j.Marker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@JsonView
public class AuthReq{

    private Logger logger = new Logger(this);

    @JsonProperty("id")
    public String getId() {
        return id;
    }
    @JsonProperty("type")
    public int getType() {
        return type;
    }
    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }
    private String id;
    private String mac;

    private int type=0;

    public AuthReq(String id, String mac, int type) {
        this.id = V2XClientUtils.formatID(id);
        this.mac = mac;
        this.type = type;
        setTime();
        try {
            this.getHashInfo();

            logger.debug(this.getHashInfo());
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.isDebugEnabled();
    }

    @JsonProperty("hashInfo")
    public String getHashInfo() throws IOException {
        logger.info(id+mac+timestamp);
        hashInfo= V2XClientUtils.EncoderByMD516(id+mac+timestamp);
        logger.info(hashInfo);
        return hashInfo;
    }
    public AuthReq setTime(){
        Date thetime = new Date();
        thetime.setTime(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        timestamp = simpleDateFormat.format(thetime);
        return this;
    }
    private String hashInfo;

    private String timestamp;
    @Override
    public String toString() {
       return JsonUtils.object2Json(this);
    }
}
