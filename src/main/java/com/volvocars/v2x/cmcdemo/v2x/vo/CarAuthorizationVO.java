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

package com.volvocars.v2x.cmcdemo.v2x.vo;

import lombok.Data;

@Data
public class CarAuthorizationVO {
    public CarAuthorizationVO(String carid, String accessToken, long expired, String key) {
        this.carid = carid;
        this.accessToken = accessToken;
        this.expired = expired;
        this.key = key;
    }

    public String getCarid() {
        return carid;
    }

    public CarAuthorizationVO setCarid(String carid) {
        this.carid = carid;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public CarAuthorizationVO setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public long getExpired() {
        return expired;
    }

    public CarAuthorizationVO setExpired(long expired) {
        this.expired = expired;
        return this;
    }

    public String getKey() {
        return key;
    }

    public CarAuthorizationVO setKey(String key) {
        this.key = key;
        return this;
    }

    public String carid;
    public String accessToken;
    public long expired;
    public String key;

    public long getKeyExpired() {
        return keyExpired;
    }

    public long keyExpired;
}
