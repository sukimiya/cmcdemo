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

import org.bouncycastle.asn1.*;

import java.io.IOException;

public class BSMFrameASN extends ASN1Object {

    private ASN1Integer msgCnt;
    private DEROctetString id;
    private DEROctetString plateNo;
    private ASN1Integer secMark;
    private ASN1Object pos;
    private ASN1Object accuracy;
    private DEROctetString transmission;
    private ASN1Integer speed;
    private ASN1Integer heading;
    private ASN1Integer angle;
    private ASN1Object motionCfd;
    private ASN1Object accelSet;
    private ASN1Object brakes;
    private ASN1Object size;
    private ASN1Object vehicleClass;
    private DEROctetString token;

    public BSMFrameASN(BSMFrame bsmFrame) throws IOException {
        this.msgCnt = new ASN1Integer(bsmFrame.getMsgCnt());
        this.id =new DEROctetString(bsmFrame.getToken());
        this.plateNo =new DEROctetString(bsmFrame.getPlateNo());
        this.secMark = new ASN1Integer(bsmFrame.getSecMark());
        this.pos = new BSMPosASN(bsmFrame.getPos());
        this.accuracy = new BSMVectorASN(bsmFrame.getAccuracy());
        this.transmission =new DEROctetString(bsmFrame.getTransmission().getBytes());
        this.speed = new ASN1Integer(bsmFrame.getSpeed());
        this.heading = new ASN1Integer(bsmFrame.getHeading());
        this.angle = new ASN1Integer(bsmFrame.getAngle());
        this.motionCfd = new BSMMotionCfdASN(bsmFrame.getMotionCfd());
        this.accelSet = new BSMAccelSetASN(bsmFrame.getAccelSet());
        this.brakes = new BSMBrakesASN(bsmFrame.getBrakes());
        this.size = new BSMVehicleSizeASN(bsmFrame.getSize());
        this.vehicleClass = new BSMVehicleClassASN(bsmFrame.getVehicleClass());
        this.token = new DEROctetString(bsmFrame.getToken());
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(msgCnt);
        vector.add(id);
        vector.add(plateNo);
        vector.add(secMark);
        vector.add(pos);
        vector.add(accuracy);
        vector.add(transmission);
        vector.add(speed);
        vector.add(heading);
        vector.add(angle);
        vector.add(motionCfd);
        vector.add(accelSet);
        vector.add(brakes);
        vector.add(size);
        vector.add(vehicleClass);
        vector.add(token);
        return new DERSequence(vector);
    }
}
