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

package com.volvocars.v2x.cmcdemo.car.vo;

import com.volvocars.v2x.cmcdemo.car.CarLocationUtils;

public class Location {
    public Location(){

    }
    public Location(long longitude, long latitude, long height) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.height = height;
    }
    public Location(Location location){
        setAs(location);
    }
    public Location(LocationWGS84 locationWGS84){
        setAs(CarLocationUtils.wgs84ToGlobal(locationWGS84));
    }
    public Location setAs(Location location){
        this.latitude = location.latitude;
        this.longitude = location.longitude;
        this.height = location.height;
        return this;
    }
    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    private long longitude;
    private long latitude;

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    private long height;

}
