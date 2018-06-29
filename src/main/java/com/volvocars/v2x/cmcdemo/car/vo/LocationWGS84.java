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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volvocars.v2x.cmcdemo.car.CarLocationUtils;

public class LocationWGS84 {
    public LocationWGS84() {
    }
    public LocationWGS84(LocationWGS84 locationWGS84){
        setAs(locationWGS84);
    }
    private LocationWGS84 setAs(LocationWGS84 locationWGS84){
        this.latitude = locationWGS84.latitude;
        this.longitude = locationWGS84.longitude;
        return this;
    }
    public LocationWGS84(Location location){
         setAs(CarLocationUtils.globalTowgs84(location));
    }

    @JsonProperty(value = "long_")
    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    private long longitude = 1203139968;

    @JsonProperty(value = "lat")
    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    private long latitude = 314806592;

    @JsonProperty(value = "elevation")
    public long getElevation() {
        return elevation;
    }

    public void setElevation(long elevation) {
        this.elevation = elevation;
    }

    private long elevation;

}
