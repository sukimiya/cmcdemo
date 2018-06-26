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
import com.fasterxml.jackson.annotation.JsonView;

public class BSMBrakes {
    public String brakePadel;
    public String traction;
    @JsonView(BSMWheelBrakes.class)
    @JsonProperty("wheelBrakes")
    public BSMWheelBrakes wheelBrakes;

    public BSMBrakes(String brakePadel, String traction, BSMWheelBrakes wheelBrakes) {
        this.brakePadel = brakePadel;
        this.traction = traction;
        this.wheelBrakes = wheelBrakes;
    }
}
