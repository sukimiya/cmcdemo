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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonView
public class AuthResp {
    @JsonProperty("result")
    public int getResult() {
        return result;
    }
    @JsonProperty("result")
    public void setResult(int result) {
        this.result = result;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }
    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }
    @JsonProperty("key")
    public String getKey() {
        return key;
    }
    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }
    @JsonProperty("tokenvalidtime")
    public String getTokenvalidtime() {
        return tokenvalidtime;
    }
    @JsonProperty("tokenvalidtime")
    public void setTokenvalidtime(String tokenvalidtime) {
        this.tokenvalidtime = tokenvalidtime;
    }

    private int result;
    private String token;
    private String key;
    private String tokenvalidtime;
    @JsonProperty("keyvalidtime")
    public String getKeyvalidtime() {
        return keyvalidtime;
    }
    @JsonProperty("keyvalidtime")
    public void setKeyvalidtime(String keyvalidtime) {
        this.keyvalidtime = keyvalidtime;
    }

    private String keyvalidtime;
    @JsonProperty("timestamp")
    private String reportTime;

    public AuthResp(int result,String timestamp){
        this.result = result;
        this.reportTime = timestamp;
    }
    public AuthResp(int result, String token, String key, String tokenvalidtime) {
        this.result = result;
        this.token = token;
        this.key = key;
        this.tokenvalidtime = tokenvalidtime;
    }
}
