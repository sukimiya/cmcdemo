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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k99k.tools.enc.Aes;
import com.volvocars.v2x.cmcdemo.car.vo.BSMASNVO;
import com.volvocars.v2x.cmcdemo.car.vo.BSMFrame;
import com.volvocars.v2x.cmcdemo.repo.CarAuthRepository;
import com.volvocars.v2x.cmcdemo.v2x.V2XClientUtils;
import io.e2x.logger.Logger;
import io.e2x.utils.JsonUtils;
import org.asnlab.asndt.runtime.conv.AsnConverter;
import org.bouncycastle.asn1.BEROctetString;
import org.bouncycastle.util.encoders.Hex;
import reactor.core.publisher.Mono;

public class CarBSMUploadEvent {
    Logger logger = new Logger(this);
    public BSMFrame bsmFrame;
    public CarAuthorizationVO authorizationVO;

    private byte[] outputstream;
    public byte[] getOutputstream(){return outputstream;};
    public char[] getOutputChars(){
        String s = new String(outputstream);
        return s.toCharArray();
    }
    private String key;
    public CarBSMUploadEvent(BSMFrame bsmFrame,String key) {
        this.bsmFrame = bsmFrame;
        this.key = key;
    }

    public Mono<CarBSMUploadEvent> findAuthByID(String id, CarAuthRepository carAuthRepository){
        CarBSMUploadEvent thisevent = this;
        return carAuthRepository.findById(id).flatMap(carAuthorizationVO -> {
            thisevent.authorizationVO = carAuthorizationVO;
            return Mono.just(thisevent);
        });
    }
    public Mono<CarBSMUploadEvent> setBSMFrame(BSMFrame frame){
        this.bsmFrame = frame;
        return Mono.just(this);
    }

    public Mono<CarBSMUploadEvent> serialize(){
        BSMUploadEventPackage bsmUploadEventPackage = new BSMUploadEventPackage(0,bsmFrame);
        ObjectMapper mapper = new ObjectMapper();
        byte[] ivk = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        try {
            String json = JsonUtils.object2Json(bsmUploadEventPackage);
//            BSMASNVO bsmasnvo = new BSMASNVO(0,bsmFrame);
//            byte[] asn1 = bsmasnvo.toASN1Primitive().getEncoded();
            logger.info("CarBSMUploadEvent.serialize with key: "+key);
            AsnConverter asn1 =
            outputstream = Aes.encBytes(asn1,key.getBytes(),ivk);
            logger.info("out put steam:"+Hex.toHexString(outputstream).toUpperCase());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.just(this);
    }
}
