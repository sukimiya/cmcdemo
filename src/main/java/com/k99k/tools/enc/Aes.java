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

package com.k99k.tools.enc;

import com.volvocars.v2x.service.common.util.MD5Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES static function for different key and iv
 * mode: AES/CBC/PKCS5Padding
 * text input encoding: utf-8
 * text output encoding: base64
 *
 */
public class Aes {

	private Aes() {
	}

//	public static final String bytesPrint(byte[] in) {
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < in.length; i++) {
//			sb.append(in[i]).append(",");
//		}
//		String out = sb.toString();
//		System.out.println(out);
//		return out;
//	}

	public static final byte[] encBytes(byte[] srcBytes, byte[] key,
			byte[] newIv) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec iv = new IvParameterSpec(newIv);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(srcBytes);
		return encrypted;
	}

	public static final String encText(String sSrc, byte[] key, byte[] newIv)
			throws Exception {
		byte[] srcBytes = sSrc.getBytes("utf-8");
		byte[] encrypted = encBytes(srcBytes, key, newIv);
		return com.k99k.tools.Base64.encode(encrypted);
	}

	public static final byte[] decBytes(byte[] srcBytes, byte[] key,
			byte[] newIv) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec iv = new IvParameterSpec(newIv);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(srcBytes);
		return encrypted;
	}

	public static final String decText(String sSrc, byte[] key, byte[] newIv)
			throws Exception {
		byte[] srcBytes = com.k99k.tools.Base64.decode(sSrc);
		byte[] decrypted = decBytes(srcBytes, key, newIv);
		return new String(decrypted, "utf-8");
	}
//	public static void main(String[] args) {
//		String s = "1/ON3mrESomcD9RlW9qkfQ==";
//		String ks = "0065766033703042"+"D8-C7-71-4B-7A-4C"+"20180703121614887";
//		String kss = MD5Utils.md5_16(ks);
//		byte[] key = kss.getBytes();
//		byte[] ivk = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//
//		try {
////			String enc = encText(s, key, ivk);
////			System.out.println(enc);
//			String dec = decText(s, key, ivk);
//			System.out.println(dec);
//
//			// If there is only one key and one iv, use AesInstance for better performance,but generally the iv need change everyTime
//
//			// AesInstance ai = AesInstance.getInstance(key, ivk);
//			// enc = ai.encText(s);
//			// System.out.println(enc);
//			// dec = ai.decText(enc);
//			// System.out.println(dec);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
}
