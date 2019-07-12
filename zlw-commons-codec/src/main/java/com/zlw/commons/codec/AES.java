package com.zlw.commons.codec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/*******************************************************************************
 * AES加解密算法
 * 
 * @update 
 * *******************************************************************************
 */

public class AES {

	private static Logger logger = LoggerFactory.getLogger(AES.class);
	
	public static String Encrypt(String sSrc,String key) {
		if (key == null) {
			logger.error("key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (key.length() != 16) {
			logger.error("Key长度不是16位");
			return null;
		}
		byte[] raw = key.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
			return byte2hex(encrypted).toLowerCase();
		} catch (Exception e) {
			logger.error("AES加密失败",e);
		} 
		return "";
		
	}

	public static String EncryptBase64(String sSrc,String key) throws UnsupportedEncodingException {
		if (key == null) {
			return null;
		}
		// 判断Key是否为16位
		if (key.length() != 16) {
			return null;
		}
		byte[] raw = key.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec( "0102030405060708".getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(encrypted));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return "";
		
	}
	
	
	
	// 解密
	public static String Decrypt(String sSrc,String key) {
		try {
			// 判断Key是否正确
			if (key == null) {
				logger.error("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (key.length() != 16) {
				logger.error("Key长度不是16位");
				return null;
			}
			byte[] raw = key.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = parseHexStr2Byte(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original,"UTF-8");
				return originalString;
			} catch (Exception e) {
				logger.warn(e.toString());
				return null;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			return null;
		}
	}

	// 解密
		public static String DecryptBase64(String sSrc,String key) {
			try {
				// 判断Key是否正确
				if (key == null) {
					System.out.print("Key为空null");
					return null;
				}
				// 判断Key是否为16位
				if (key.length() != 16) {
					logger.warn("Key长度不是16位");
					return null;
				}
				byte[] raw = key.getBytes("ASCII");
				SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
				byte[] encrypted1 = Base64.decodeBase64(sSrc);
				try {
					byte[] original = cipher.doFinal(encrypted1);
					String originalString = new String(original);
					return originalString;
				} catch (Exception e) {
					logger.warn(e.toString());
					return null;
				}
			} catch (Exception ex) {
				logger.warn(ex.toString());
				return null;
			}
		}
	
	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
					16);
		}
		return b;
	}

	// 十六进制字符串转字节数组
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	public static void main(String[] args) throws Exception {
        /*
        * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
        */
        String cKey = "msec818hangnan13";
        // 需要加密的字串
        String username ="c3NvdGVzdDY%3D";
        String password ="password";
        String oldpass ="oldpass";
        String newPass ="newpass";
        String mobileString = "13500135000";
        
        // 加密
        System.out.println("username="+AES.Encrypt(username, cKey));
        System.out.println("password="+AES.Encrypt(password, cKey));
        System.out.println("oldpass="+AES.Encrypt(oldpass, cKey));
        System.out.println("newPass="+AES.Encrypt(newPass, cKey));
        System.out.println("mobileString="+AES.Encrypt(mobileString, cKey));
        
   }
}