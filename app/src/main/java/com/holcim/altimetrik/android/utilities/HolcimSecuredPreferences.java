package com.holcim.altimetrik.android.utilities;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

public class HolcimSecuredPreferences {
	
	public static class SecurePreferencesException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5559946106660988627L;

		public SecurePreferencesException(Throwable e) {
			super(e);
		}

	}

	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	private static final String SECRET_KEY_HASH_TRANSFORMATION = "SHA-256";
	private static final String CHARSET = "UTF-8";

	private final boolean encryptKeys;
	private final Cipher writer;
	private final Cipher reader;
	private final Cipher keyWriter;
	private final SharedPreferences preferences;

	/**
	 * This will initialize an instance of the SecurePreferences class
	 * @param context your current context.
	 * @param preferenceName name of preferences file (preferenceName.xml)
	 * @param secureKey the key used for encryption, finding a good key scheme is hard.
	 * Hardcoding your key in the application is bad, but better than plaintext preferences. Having the user enter the key upon application launch is a safe(r) alternative, but annoying to the user.
	 * @param encryptKeys settings this to false will only encrypt the values,
	 * true will encrypt both values and keys. Keys can contain a lot of information about
	 * the plaintext value of the value which can be used to decipher the value.
	 * @throws SecurePreferencesException
	 */
	public HolcimSecuredPreferences(Context context, String preferenceName, String secureKey, boolean encryptKeys) throws SecurePreferencesException {
		try {
			this.writer = Cipher.getInstance(TRANSFORMATION);
			this.reader = Cipher.getInstance(TRANSFORMATION);
			this.keyWriter = Cipher.getInstance(KEY_TRANSFORMATION);

			initCiphers(secureKey);

			this.preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

			this.encryptKeys = encryptKeys;
		}
		catch (GeneralSecurityException e) {
			throw new SecurePreferencesException(e);
		}
		catch (UnsupportedEncodingException e) {
			throw new SecurePreferencesException(e);
		}
	}
	
	public HolcimSecuredPreferences(Context context, String secureKey, boolean encryptKeys) throws SecurePreferencesException {
		try {
			this.writer = Cipher.getInstance(TRANSFORMATION);
			this.reader = Cipher.getInstance(TRANSFORMATION);
			this.keyWriter = Cipher.getInstance(KEY_TRANSFORMATION);

			initCiphers(secureKey);

			this.preferences = PreferenceManager.getDefaultSharedPreferences(context);

			this.encryptKeys = encryptKeys;
		}
		catch (GeneralSecurityException e) {
			throw new SecurePreferencesException(e);
		}
		catch (UnsupportedEncodingException e) {
			throw new SecurePreferencesException(e);
		}
	}

	protected void initCiphers(String secureKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException,
	InvalidAlgorithmParameterException {
		IvParameterSpec ivSpec = getIv();
		SecretKeySpec secretKey = getSecretKey(secureKey);

		writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		reader.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
		keyWriter.init(Cipher.ENCRYPT_MODE, secretKey);
	}

	protected IvParameterSpec getIv() {
		byte[] iv = new byte[writer.getBlockSize()];
		System.arraycopy("fldsjfodasjifudslfjdsaofshaufihadsf".getBytes(), 0, iv, 0, writer.getBlockSize());
		return new IvParameterSpec(iv);
	}

	protected SecretKeySpec getSecretKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] keyBytes = createKeyBytes(key);
		return new SecretKeySpec(keyBytes, TRANSFORMATION);
	}

	protected byte[] createKeyBytes(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(SECRET_KEY_HASH_TRANSFORMATION);
		md.reset();
		byte[] keyBytes = md.digest(key.getBytes(CHARSET));
		return keyBytes;
	}

	public void put(String key, String value) {
		if (value == null) {
			preferences.edit().remove(toKey(key)).commit();
		}
		else {
			putValue(toKey(key), value);
		}
	}

	public boolean containsKey(String key) {
		return preferences.contains(toKey(key));
	}

	public void removeValue(String key) {
		preferences.edit().remove(toKey(key)).commit();
	}

	public String getString(String key) throws SecurePreferencesException {
		if (preferences.contains(toKey(key))) {
			String securedEncodedValue = preferences.getString(toKey(key), "");
			return decrypt(securedEncodedValue);
		}
		return null;
	}

	public void clear() {
		preferences.edit().clear().commit();
	}

	private String toKey(String key) {
		if (encryptKeys)
			return encrypt(key, keyWriter);
		else return key;
	}

	private void putValue(String key, String value) throws SecurePreferencesException {
		String secureValueEncoded = encrypt(value, writer);

		preferences.edit().putString(key, secureValueEncoded).commit();
	}

	protected String encrypt(String value, Cipher writer) throws SecurePreferencesException {
		byte[] secureValue;
		try {
			secureValue = convert(writer, value.getBytes(CHARSET));
		}
		catch (UnsupportedEncodingException e) {
			throw new SecurePreferencesException(e);
		}
		String secureValueEncoded = Base64.encodeToString(secureValue, Base64.NO_WRAP);
		return secureValueEncoded;
	}

	protected String decrypt(String securedEncodedValue) {
		byte[] securedValue = Base64.decode(securedEncodedValue, Base64.NO_WRAP);
		byte[] value = convert(reader, securedValue);
		try {
			return new String(value, CHARSET);
		}
		catch (UnsupportedEncodingException e) {
			throw new SecurePreferencesException(e);
		}
	}

	private static byte[] convert(Cipher cipher, byte[] bs) throws SecurePreferencesException {
		try {
			return cipher.doFinal(bs);
		}
		catch (Exception e) {
			throw new SecurePreferencesException(e);
		}
	}
}
