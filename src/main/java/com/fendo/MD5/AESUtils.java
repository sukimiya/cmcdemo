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

package com.fendo.MD5;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.e2x.logger.Logger;
import org.apache.commons.lang3.StringUtils;


/**
 * @Title: AESUtils.java
 * @Package com.fendo.com.MD5
 * @Description: TODO
 * @author fendo
 * @date 2017年9月11日 下午5:48:17
 * @version V1.0
 */
public class AESUtils {

    enum RandomBit{
        B128(128),
        B256(256)
        ;

        private int bit = 128;
        public int getBit(){return bit;}
        RandomBit(int bit) {
            this.bit = bit;
        }
    }
    enum KeyAlgorithm{
        AES("AES"),	//Key generator for use with the AES algorithm.
        ARCFOUR("ARCFOUR")	,//Key generator for use with the ARCFOUR (RC4) algorithm.
        Blowfish("Blowfish")	,//Key generator for use with the Blowfish algorithm.
        DES("Blowfish")	,//Key generator for use with the DES algorithm.
        DESede("Blowfish")	,//Key generator for use with the DESede (triple-DES) algorithm.
        HmacMD5("Blowfish")	,//Key generator for use with the HmacMD5 algorithm.
        HmacSHA1("HmacSHA1") ,//HmacSHA256 HmacSHA384 HmacSHA512	Keys generator for use with the various flavors of the HmacSHA algorithms.
        RC2("HmacSHA1")	//Key generator for use with the RC2 algorithm.
        ;

        public String getAlgorithm() {
            return algorithm;
        }

        private String algorithm;

        KeyAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }
    }

    enum Algorithm{
        AES("AES"),
        DES("DES"),
        AES_CBC_P("AES/CBC/PKCS5Padding"),
        AES_ECB_NP("AES/ECB/NoPadding"),
        AES_ECB_P("AES/ECB/PKCS5Padding"),
        DES_CBC_NP("DES/CBC/NoPadding"),
        DES_CBC_P("DES/CBC/PKCS5Padding"),
        DES_ECB_NP("DES/ECB/NoPadding"),
        DES_ECB_P("DES/ECB/PKCS5Padding"),
        DESEDE_CBC_NP("DESede/CBC/NoPadding"),
        DESEDE_CBC_P("DESede/CBC/PKCS5Padding"),
        DESEDE_ECB_NP("DESede/ECB/NoPadding"),
        DESEDE_ECB_P("DESede/ECB/PKCS5Padding"),
        RSA_ECB_P("RSA/ECB/PKCS1Padding"),
        RSA_ECB_OAEP1_MGF1PADDING("RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),
        RSA_ECB_OAEP256_MGF1PADDING("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");


        private String algorithm;
        public String getAlgorithm(){return algorithm;}
        private Algorithm(String ag){
            algorithm = ag;
        }
    }

    private static final String charset = "utf-8";

    private static final String encodeRules = "volvo cars";

    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据ecnodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static Algorithm algorithm = Algorithm.AES_ECB_P;
    public static String AESEncode(String content,String decryptKey) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance(KeyAlgorithm.AES.getAlgorithm());
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(RandomBit.B128.getBit(),  new SecureRandom(decryptKey.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, KeyAlgorithm.AES.getAlgorithm());
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes(charset);
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode = new String(Base64.getEncoder().encode(byte_AES));
            //11.将字符串返回
            return AES_encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //如果有错就返加nulll
        return null;
    }



    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        return aesEncrypt(content.getBytes(),encryptKey);
    }
    public static byte[] aesEncrypt(byte [] content,String encryptKey) throws Exception{
        KeyGenerator kgen = KeyGenerator.getInstance(KeyAlgorithm.AES.getAlgorithm());
        kgen.init(RandomBit.B128.getBit(), new SecureRandom(encryptKey.getBytes()));

        Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), KeyAlgorithm.AES.getAlgorithm()));

        return cipher.doFinal(content);
    }
    /**
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String AESDecode(String content, String decryptKey) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance(KeyAlgorithm.AES.getAlgorithm());
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(RandomBit.B128.getBit(), new SecureRandom(decryptKey.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, KeyAlgorithm.AES.getAlgorithm());
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte[] byte_content = Base64.getDecoder().decode(content);
            /*
             * 解密
             */
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, charset);
            return AES_decode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        //如果有错就返加nulll
        return null;
    }


    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        Logger logger = new Logger(AESUtils.class);
        logger.info("AESUtils::aesDecryptByBytes ["+encryptBytes.toString()+"] with "+algorithm.getAlgorithm());
        KeyGenerator kgen = KeyGenerator.getInstance(KeyAlgorithm.AES.getAlgorithm());
        kgen.init(RandomBit.B128.getBit(), new SecureRandom(decryptKey.getBytes()));

        Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), KeyAlgorithm.AES.getAlgorithm()));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * base 64 加密
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base 64 解密
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : Base64.getDecoder().decode(base64Code);
    }

    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }
}
