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

public class BSMWheelBrakesASN extends ASN1Object {
    private ASN1Integer unusedBits;
    private DEROctetString bytes;

    public BSMWheelBrakesASN(BSMWheelBrakes bsmWheelBrakes) {
        unusedBits = new ASN1Integer(bsmWheelBrakes.getUnusedBits());
        bytes = new DEROctetString(bsmWheelBrakes.getBytes());
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(unusedBits);
        vector.add(bytes);
        return new DERSequence(vector);
    }
}
