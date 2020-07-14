package com.pagoda.framework.core.util.crypto;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 암호화 유틸리티
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public class CipherAES {

	// private String KEY = "F82799142FA202C1";
	// private String TRANSFORM = "AES/ECB/PKCS5Padding";

	private final String key;
	private final String transform;

	/**
	 * 생성자.
	 *
	 * @param transform
	 *            암호화 알고리즘
	 * @param key
	 *            암호화 키
	 */
	public CipherAES(String transform, String key) {
		this.transform = transform;
		this.key = key;
	}

	/**
	 * 암호화.
	 *
	 * @param str
	 *            대상
	 * @return 암호화 된 값.
	 * @throws Exception
	 *             Exception
	 */
	public String encryption(String str) throws Exception {
		return encrypt(str);
	}

	/**
	 * 복호화.
	 *
	 * @param str
	 *            대상
	 * @return 복호화 된 값.
	 * @throws Exception
	 *             Exception
	 */
	public String decryption(String str) throws Exception {
		return decrypt(str);
	}

	/**
	 * 암호화.
	 *
	 * @param plainText
	 *            원본 문자열
	 * @return 암호화 문자열
	 * @throws Exception
	 *             Exception
	 */
	private String encrypt(String plainText) throws Exception {
		if (null != plainText) {
			// 128bit AES 키 생성기
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);

			// keySpac 생성
			byte[] raw = key.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

			// 암호화 모듈 인스턴스 생성 및 초기화
			Cipher cipher = Cipher.getInstance(transform);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

			// 암호화
			byte[] encrypted = cipher.doFinal(plainText.getBytes());
			return asHex(encrypted);
		} else {
			return "";
		}
	}

	/**
	 * 복호화.
	 *
	 * @param cipherText
	 *            암호화된 문자열
	 * @return 복호화된 문자열
	 * @throws Exception
	 *             Exception
	 */
	private String decrypt(String cipherText) throws Exception {
		if (null != cipherText) {
			// 128bit 키 생성기
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);

			// keySpac 생성
			byte[] raw = key.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

			// 암호화 모듈 인스턴스 생성 및 초기화
			Cipher cipher = Cipher.getInstance(transform);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);

			// 복호화
			byte[] original = cipher.doFinal(fromString(cipherText));
			String originalString = new String(original);
			return originalString;
		} else {
			return "";
		}
	}

	/**
	 * byte 배열을 Hex 문자열로 반환.
	 *
	 * @param buf
	 *            byte 배열
	 * @return String
	 */
	private static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if ((buf[i] & 0xff) < 0x10) {
				strbuf.append("0");
			}

			strbuf.append(Long.toString(buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	/**
	 * hex 문자열을 byte 배열로 반환.
	 *
	 * @param hex
	 *            String
	 * @return byte[]
	 */
	private byte[] fromString(String hex) {
		int len = hex.length();
		byte[] buf = new byte[((len + 1) / 2)];

		int i = 0, j = 0;
		if (len % 2 != 0) {
			buf[j++] = (byte) fromDigit(hex.charAt(i++));
		}

		while (i < len) {
			buf[j++] = (byte) ((fromDigit(hex.charAt(i++)) << 4) | fromDigit(hex.charAt(i++)));
		}
		return buf;
	}

	/**
	 * char를 Hex 번호로 반환.
	 *
	 * @param ch
	 *            char
	 * @return int
	 */
	private int fromDigit(char ch) {
		if (ch >= '0' && ch <= '9') {
			return ch - '0';
		}
		if (ch >= 'A' && ch <= 'F') {
			return ch - 'A' + 10;
		}
		if (ch >= 'a' && ch <= 'f') {
			return ch - 'a' + 10;
		}

		throw new IllegalArgumentException("invalid hex digit '" + ch + "'");
	}

}
