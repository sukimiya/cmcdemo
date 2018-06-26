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

public class BSMMotionCfd {
    private String speedCfd;
    private String headingCfd;

    public BSMMotionCfd(String speedCfd, String headingCfd, String steerCfd) {
        this.speedCfd = speedCfd;
        this.headingCfd = headingCfd;
        this.steerCfd = steerCfd;
    }

    public String getSpeedCfd() {
        return speedCfd;
    }

    public void setSpeedCfd(String speedCfd) {
        this.speedCfd = speedCfd;
    }

    public String getHeadingCfd() {
        return headingCfd;
    }

    public void setHeadingCfd(String headingCfd) {
        this.headingCfd = headingCfd;
    }

    public String getSteerCfd() {
        return steerCfd;
    }

    public void setSteerCfd(String steerCfd) {
        this.steerCfd = steerCfd;
    }

    private String steerCfd;
}
