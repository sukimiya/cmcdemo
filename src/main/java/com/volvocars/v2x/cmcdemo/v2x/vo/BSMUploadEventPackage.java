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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.volvocars.v2x.cmcdemo.car.vo.BSMFrame;

@JsonView
public class BSMUploadEventPackage {
    @JsonProperty("choiceID")
    public int choiceID=0;
    @JsonProperty("bsmFrame")
    public BSMFrame bsmFrame;

    public BSMUploadEventPackage(int choseID, BSMFrame bsmFrame) {
        this.choiceID = choseID;
        this.bsmFrame = bsmFrame;
    }
}
