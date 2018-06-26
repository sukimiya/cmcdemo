package com.volvocars.v2x.service.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5算法帮助类
 *
 * @author ruanxiangbing
 * @version V1.0.0
 * @date [2018年3月27日]
 */
public final class MD5Utils
{
    /**
     * MD5算法
     */
    private static final String ALGORITHM_MD5 = "MD5";

    /**
     * UTF-8编码格式
     */
    private static final String CHARSET_ENCODER = "UTF-8";

    /**
     * 16进制字符
     */
    private static final char HEX_DIGIST[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F' };

    /**
     * 将原字符串转换为16位的MD5，转换失败报运行时异常
     * <br> 字母全部大写
     *
     * @param msg 待转换的字符串
     * @return 转换后字符串
     */
    public static String md5_16(String msg)
    {
        return md5_32(msg).substring(8, 24);
    }

    /**
     * 将原字符串转换为32位的MD5，转换失败报运行时异常
     * <br> 字母全部大写
     *
     * @param msg 待转换的字符串
     * @return 转换后字符串
     */
    public static String md5_32(String msg)
    {
        MessageDigest md = null;
        byte[] md5Byte = null;
        try
        {
            md = MessageDigest.getInstance(ALGORITHM_MD5);
            md5Byte = md.digest(msg.getBytes(CHARSET_ENCODER));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("encrypt the msg by md5 error.", e);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("encrypt the msg by md5 error.", e);
        }

        return byteToHexString(md5Byte);
    }

    /**
     * 将byte[]转换成十六进制的字符串
     *
     * @param data
     *            待转换的byte数据
     * @return 转换后的十六进制的字符串
     */
    private static String byteToHexString(byte[] data)
    {
        char str[] = new char[16 * 2];
        int k = 0;
        for (int i = 0; i < 16; i++)
        {
            byte byte0 = data[i];
            str[k++] = HEX_DIGIST[byte0 >>> 4 & 0xf];
            str[k++] = HEX_DIGIST[byte0 & 0xf];
        }

        return new String(str);
    }

    public static String md5MySQL16(String sourceStr){
        return md5MySQL32(sourceStr).substring(8,24);
    }
    public static String md5MySQL32(String sourceStr){
        try {
            byte[] c = sourceStr.getBytes();
            //生成md5加密算法照耀
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int j = 0; j < b.length; j++) {
                i = b[j];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }


            String md5_32 = buf.toString();		//32位加密
            String md5_16 = buf.toString().substring(8, 24);	//16位加密
            return md5_32;
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return "";
    }
}
