package com.holcim.altimetrik.android.utilities;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class Decryption {
	private static final String seed = "Qv562CK635sE1QdR8ThbYwBEgN39fJZd";
	private static final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";
	private static SecretKeySpec secretKeySpec; 
	private static final byte [] iv = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	private static final IvParameterSpec IV = new IvParameterSpec(iv);
	private static byte[] byteKey;
	
	private static void init(){
		byteKey=null;				
		try {
			byteKey = (seed).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		secretKeySpec = new SecretKeySpec(byteKey, "AES");
	}
	
	public static String decryptKey(String hex){
		init();
		byte[] ciphertext = new BigInteger(hex,16).toByteArray();
		String decrypted = new String( decrypt(CIPHERMODEPADDING, secretKeySpec, IV, ciphertext) );		
		return decrypted;
	}
	
	private static byte [] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) {
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.DECRYPT_MODE, sk, IV);
			return c.doFinal(ciphertext);
		} catch (NoSuchAlgorithmException nsae) {
			Log.e("AESCrypto", "no cipher getinstance support for "+cmp );
		} catch (NoSuchPaddingException nspe) {
			Log.e("AESCrypto", "no cipher getinstance support for padding " + cmp );
		} catch (InvalidKeyException e) {
			Log.e("AESCrypto", "invalid key exception" );
		} catch (InvalidAlgorithmParameterException e) {
			Log.e("AESCrypto", "invalid algorithm parameter exception" );
		} catch (IllegalBlockSizeException e) {
			Log.e("AESCrypto", "illegal block size exception" );
		} catch (BadPaddingException e) {
			Log.e("AESCrypto", "bad padding exception" );
		}
		return null;
	}
}
