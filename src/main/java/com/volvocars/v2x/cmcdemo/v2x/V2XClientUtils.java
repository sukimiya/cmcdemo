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

package com.volvocars.v2x.cmcdemo.v2x;

import com.fendo.MD5.AESUtils;
import com.volvocars.v2x.cmcdemo.car.vo.CarVO;
import com.volvocars.v2x.cmcdemo.v2x.msg.AuthResp;
import com.volvocars.v2x.cmcdemo.v2x.vo.CarAuthorizationVO;
import com.volvocars.v2x.service.common.util.MD5Utils;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class V2XClientUtils {

    /**利用MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    public static String EncoderByMd5(String str) throws IOException {
        String encoderString;
        //确定计算方法
        encoderString = MD5Utils.md5MySQL32(str);
        return encoderString.toUpperCase();
    }
    public static String EncoderByMD516(String str) throws IOException{
        System.out.println("MD5Utils->Encoder: "+str);
        return MD5Utils.md5MySQL16(str).toUpperCase();
    }
    private static final SimpleDateFormat dataformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static Date fromV2XNetDateFormat(String dateStr) throws ParseException {
        return dataformat.parse(dateStr);
    }
    public static String formatID(String id){
        String pinEncode=String.format("%1$16s", id);
        pinEncode =  pinEncode.replaceAll(" ", "0");//用0补足16位
        return pinEncode;
    }
    /**
     * AES加密字符串
     *
     * @param content
     *            需要被加密的字符串
     * @param password
     *            加密需要的密码
     * @return 密文
     */
    public static byte[] encrypt(byte[] content, String password ,String...options) throws Exception {
        return AESUtils.aesEncrypt(content,password);
    }
    public static String decode(byte[] encryptContent, String password, String...options) throws Exception{
        return AESUtils.aesDecryptByBytes(encryptContent,password);
    }
    private static SimpleDateFormat v2xdataformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static String preseDateToLongString(Date time){
        return v2xdataformat.format(time);
    }
    public static Date preseLongStringToDate(String s) throws ParseException {
        return v2xdataformat.parse(s);
    }

    public static CarAuthorizationVO fromAuthResult(AuthResp authResp,String id,String mac) throws Exception {
        CarAuthorizationVO carAuthorizationVO;
        carAuthorizationVO = new CarAuthorizationVO(id,authResp.getToken(),
                preseLongStringToDate(authResp.getTokenvalidtime()).getTime(),authResp.getKey());
        String md5token = EncoderByMD516(id+mac+authResp.getTokenvalidtime());
        byte[] base64token = Base64.getDecoder().decode(authResp.getToken());
        //carAuthorizationVO.accessToken = decode(base64token,md5token);
        String tokenStr = authResp.getToken();
        byte[] tokenbyte = tokenStr.getBytes();
        carAuthorizationVO.accessToken = AESUtils.AESDecode(tokenStr,md5token);
        String md5key = EncoderByMD516(id+mac+authResp.getKeyvalidtime());
        byte[] base64key = Base64.getDecoder().decode(authResp.getKey());
        String keyStr = authResp.getKey();
        byte[] keyByte = keyStr.getBytes();
        //carAuthorizationVO.key = decode(base64key,md5key);
        carAuthorizationVO.key = AESUtils.AESDecode(keyStr,md5key);
        carAuthorizationVO.expired = fromV2XNetDateFormat(authResp.getTokenvalidtime()).getTime();
        return carAuthorizationVO;

    }
}
