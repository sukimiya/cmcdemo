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
import io.e2x.utils.LangUtils;
import lombok.Data;

@JsonView
@Data
public class BSMFrame {

    public BSMFrame(String id,LocationWGS84 locations,String thetoken){
        this.id = tremByte(id.getBytes(),8);
        this.rawid = id;
        this.pos = locations;
        this.token = tremByte(thetoken.getBytes(),4);
        this.rawtoken = thetoken;
        initDatas();
    }
    private void initDatas(){
        this.plateNo = new byte[]{49, 50, 51, 52};
        this.secMark = (short) System.currentTimeMillis();
        if(this.secMark<0)this.secMark += 32768;
        accuracy = new BSMVector("a20m","elev_000_01");
        transmission = "neutral";
        speed = 80;
        heading = 38;
        angle = 121;
        motionCfd = new BSMMotionCfd("prec0_01ms","prec01deg","prec0_02deg");
        accelSet = new BSMAccelSet(1202,315,120,343);
        byte[] brekeBytes =new byte[1];
        brekeBytes[0]= 64;
        brakes = new BSMBrakes("on","engaged",new BSMWheelBrakes(3,brekeBytes));
        size = new BSMVehicleSize(3,6,2);
        vehicleClass = new BSMVehicleClass(123);
    }
    public BSMFrame(int msgCnt, byte[] id, byte[] plateNo, int secMark, LocationWGS84 pos, BSMVector accuracy, String transmission, int speed, int heading, int angle, BSMMotionCfd motionCfd, BSMAccelSet accelSet, BSMBrakes brakes, BSMVehicleSize size, BSMVehicleClass vehicleClass, byte[] token) {
        this.msgCnt = msgCnt;
        this.id = id;
        this.plateNo = plateNo;
        this.secMark = secMark;
        this.pos = pos;
        this.accuracy = accuracy;
        this.transmission = transmission;
        this.speed = speed;
        this.heading = heading;
        this.angle = angle;
        this.motionCfd = motionCfd;
        this.accelSet = accelSet;
        this.brakes = brakes;
        this.size = size;
        this.vehicleClass = vehicleClass;
        this.token = token;
    }

    @JsonProperty(value = "msgCnt",index = 0)
    public int getMsgCnt() {
        return msgCnt;
    }
    public BSMFrame addMsgCnt(){
        msgCnt++;
        return this;
    }

    public void setMsgCnt(int msgCnt) {
        this.msgCnt = msgCnt;
    }
    @JsonProperty(value = "id",index = 1)
    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }
    @JsonProperty(value = "plateNo",index = 2)
    public byte[] getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(byte[] plateNo) {
        this.plateNo = plateNo;
    }
    @JsonProperty(value = "secMark",index = 3)
    public int getSecMark() {
        return secMark;
    }

    public void setSecMark(int secMark) {
        this.secMark = secMark;
    }
    @JsonView(LocationWGS84.class)
    @JsonProperty(value = "pos",index = 4)
    public LocationWGS84 getPos() {
        return pos;
    }

    public void setPos(LocationWGS84 pos) {
        this.pos = pos;
    }
    @JsonView(BSMVector.class)
    @JsonProperty(value = "accuracy",index = 5)
    public BSMVector getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(BSMVector accuracy) {
        this.accuracy = accuracy;
    }
    @JsonProperty(value = "transmission",index = 6)
    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    @JsonProperty(value = "speed",index = 7)
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    @JsonProperty(value = "heading",index = 8)
    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }
    @JsonProperty(value = "angle",index = 9)
    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
    @JsonView(BSMMotionCfd.class)
    @JsonProperty(value = "motionCfd",index = 10)
    public BSMMotionCfd getMotionCfd() {
        return motionCfd;
    }

    public void setMotionCfd(BSMMotionCfd motionCfd) {
        this.motionCfd = motionCfd;
    }
    @JsonView(BSMAccelSet.class)
    @JsonProperty(value = "accelSet",index = 11)
    public BSMAccelSet getAccelSet() {
        return accelSet;
    }

    public void setAccelSet(BSMAccelSet accelSet) {
        this.accelSet = accelSet;
    }
    @JsonView(BSMBrakes.class)
    @JsonProperty(value = "brakes",index = 12)
    public BSMBrakes getBrakes() {
        return brakes;
    }

    public void setBrakes(BSMBrakes brakes) {
        this.brakes = brakes;
    }
    @JsonView(BSMVehicleSize.class)
    @JsonProperty(value = "size",index = 13)
    public BSMVehicleSize getSize() {
        return size;
    }

    public void setSize(BSMVehicleSize size) {
        this.size = size;
    }
    @JsonView(BSMVehicleClass.class)
    @JsonProperty(value = "vehicleClass",index = 14)
    public BSMVehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(BSMVehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }
    @JsonProperty(value = "token",index = 15)
    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    private static int msgCnt;
    private byte[] id;

    @JsonProperty(required = false,access = JsonProperty.Access.WRITE_ONLY)
    public String getRawid() {
        return rawid;
    }
    @JsonProperty(required = false,access = JsonProperty.Access.WRITE_ONLY)
    public String getRawtoken() {
        return rawtoken;
    }

    private String rawid;
    private byte[] plateNo;
    private int secMark;
    private LocationWGS84 pos;
    private BSMVector accuracy;
    private String transmission;
    private int speed;
    private int heading;
    private int angle;
    private BSMMotionCfd motionCfd;
    private BSMAccelSet accelSet;
    private BSMBrakes brakes;
    private BSMVehicleSize size;
    private BSMVehicleClass vehicleClass;
    private byte[] token;
    private String rawtoken;

    private byte[] tremByte(byte[] ori,int maxLengh){
        byte[] rs = new byte[maxLengh];
        int max = ori.length;
        int n = maxLengh-1;
        for(int i=max-maxLengh;i<max;i++){
            if(i<0){
                rs[n] = 0;
            }else{
                rs[n] = ori[i];
            }
            n--;

        }
        return rs;
    }
}
