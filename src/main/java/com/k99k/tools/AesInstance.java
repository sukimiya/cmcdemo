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

package com.k99k.tools;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES instance with one key and one iv for preformance.
 * mode: AES/CBC/PKCS5Padding
 * text input encoding: utf-8
 * text output encoding: base64
 *
 */
public class AesInstance {

    	private AesInstance(byte[] key,byte[] iv) throws Exception {
			this.encCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			this.decCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			IvParameterSpec ivc =  new IvParameterSpec(iv);
			this.encCipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivc);
			this.decCipher.init(Cipher.DECRYPT_MODE, skeySpec, ivc);
    	}
    	private Cipher encCipher =  null;
    	private Cipher decCipher =  null;
    	
    	public byte[] encBytes(byte[] srcBytes) throws Exception{
    		byte[] encrypted = encCipher.doFinal(srcBytes);
			return encrypted;
    	}
    	public byte[] decBytes(byte[] srcBytes) throws Exception{
    		byte[] decrypted = decCipher.doFinal(srcBytes);
			return decrypted;
    	}
    	public String encText(String srcStr) throws Exception {
			byte[] srcBytes = srcStr.getBytes("utf-8");
			byte[] encrypted = encBytes(srcBytes);
			return com.k99k.tools.Base64.encode(encrypted);
	  	}
    	public String decText(String srcStr) throws Exception {
			byte[] srcBytes = com.k99k.tools.Base64.decode(srcStr);
			byte[] decrypted = decBytes(srcBytes);
			return new String(decrypted,"utf-8");
	  	}
    	public static final AesInstance getInstance(byte[] key,byte[] iv) throws Exception{
    		AesInstance sub = new AesInstance(key,iv);
	    	return sub;
	    }
  }
    
   
   