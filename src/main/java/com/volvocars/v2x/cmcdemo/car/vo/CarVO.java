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

import com.volvocars.v2x.cmcdemo.v2x.vo.CarAuthorizationVO;
import lombok.Data;

@Data
public class CarVO {
    private String ain;
    private String eth0_mac;
    private CarStatusVO status;

    public CarVO(String ain, String eth0_mac, CarStatusVO status) {
        this.ain = ain;
        this.eth0_mac = eth0_mac;
        this.status = status;
    }

    public String getAin() {
        return ain;
    }

    public void setAin(String ain) {
        this.ain = ain;
    }

    public String getEth0_mac() {
        return eth0_mac;
    }

    public void setEth0_mac(String eth0_mac) {
        this.eth0_mac = eth0_mac;
    }

    public CarStatusVO getStatus() {
        return status;
    }

    public void setStatus(CarStatusVO status) {
        this.status = status;
    }
}
