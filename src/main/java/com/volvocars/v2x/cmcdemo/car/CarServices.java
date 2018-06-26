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

package com.volvocars.v2x.cmcdemo.car;

import com.volvocars.v2x.cmcdemo.car.vo.BSMFrame;
import com.volvocars.v2x.cmcdemo.v2x.V2XClientNet;
import com.volvocars.v2x.cmcdemo.v2x.V2XClientServer;
import org.springframework.beans.factory.annotation.Autowired;

public class CarServices {

    @Autowired
    public V2XClientServer clientServer;
    @Autowired
    public V2XClientNet clientNet;

    public void login(String carid,String mac){
        try {
            clientServer.loginCarById(carid,mac);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void uploadBSM(BSMFrame theframe) throws InterruptedException {
        clientNet.send(theframe);
    }
}
