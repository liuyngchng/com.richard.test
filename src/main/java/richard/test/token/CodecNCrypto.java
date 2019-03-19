package richard.test.token;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;
/*
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.KeyParameter;
*/

public class CodecNCrypto {
	
	/**
	 * @return an UUID String
	 */
	public static String UUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Encode a String to base64
	 * 
	 * @param value
	 *            The plain String
	 * @return The base64 encoded String
	 */
	public static String encodeBASE64(String value) {
		try {
			return new String(Base64.encodeBase64(value.getBytes("utf-8")));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Encode binary data to base64
	 * 
	 * @param value
	 *            The binary data
	 * @return The base64 encoded String
	 */
	public static String encodeBASE64(byte[] value) {
		return new String(Base64.encodeBase64(value));
	}

	/**
	 * Decode a base64 value
	 * 
	 * @param value
	 *            The base64 encoded String
	 * @return decoded binary data
	 */
	public static byte[] decodeBASE64(String value) {
		try {
			return Base64.decodeBase64(value.getBytes("utf-8"));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Build an hexadecimal MD5 hash for a String
	 * 
	 * @param value
	 *            The String to hash
	 * @return An hexadecimal Hash
	 */
	public static String hexMD5(String value) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(value.getBytes("utf-8"));
			byte[] digest = messageDigest.digest();
			return byteToHexString(digest);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Build an hexadecimal SHA1 hash for a String
	 * 
	 * @param value
	 *            The String to hash
	 * @return An hexadecimal Hash
	 */
	public static String hexSHA1(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(value.getBytes("utf-8"));
			byte[] digest = md.digest();
			return byteToHexString(digest);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static String encryptAES(String value, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");  
            kgen.init(128, new SecureRandom(password.getBytes("utf-8")));  
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
			Cipher cipher = Cipher.getInstance("AES");
			SecretKeySpec securekey = new SecretKeySpec(enCodeFormat, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
			return byteToHexString(cipher.doFinal(value.getBytes("utf-8")));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static String decryptAES(String value, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	        kgen.init(128, new SecureRandom(password.getBytes("utf-8")));  
	        SecretKey secretKey = kgen.generateKey();  
	        byte[] enCodeFormat = secretKey.getEncoded();
			Cipher cipher = Cipher.getInstance("AES");
			SecretKeySpec securekey = new SecretKeySpec(enCodeFormat, "AES");
			cipher.init(Cipher.DECRYPT_MODE, securekey);
			return new String(cipher.doFinal(hexStringToByte(value)), "utf-8");
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}	
	
	/**
	 * Write a byte array as hexadecimal String.
	 */
	public static String byteToHexString(byte[] bytes) {
		return Hex.encodeHexString(bytes);
	}

	/**
	 * Transform an hexadecimal String to a byte array.
	 */
	public static byte[] hexStringToByte(String hexString) {
		try {
			return Hex.decodeHex(hexString.toCharArray());
		} catch (DecoderException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/*
	 * Javascript escape & unescape
	 */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
	
	public final static int murmur32(String s){
		return MurmurHash.hash32(s);
	}
	
	public final static long murmur64(String s){
		return MurmurHash.hash64(s);
	}
	
	public final static long murmur64(byte[] b){
		return MurmurHash.hash(b);
	}
	
	public static class MurmurHash {
		/**
		 * Hashes bytes in an array.
		 * 
		 * @param data
		 *            The bytes to hash.
		 * @param seed
		 *            The seed for the hash.
		 * @return The 32 bit hash of the bytes in question.
		 */
		public static int hash(byte[] data, int seed) {
			return hash(ByteBuffer.wrap(data), seed);
		}

		/**
		 * Hashes bytes in part of an array.
		 * 
		 * @param data
		 *            The data to hash.
		 * @param offset
		 *            Where to start munging.
		 * @param length
		 *            How many bytes to process.
		 * @param seed
		 *            The seed to start with.
		 * @return The 32-bit hash of the data in question.
		 */
		public static int hash(byte[] data, int offset, int length, int seed) {
			return hash(ByteBuffer.wrap(data, offset, length), seed);
		}

		/**
		 * Hashes the bytes in a buffer from the current position to the limit.
		 * 
		 * @param buf
		 *            The bytes to hash.
		 * @param seed
		 *            The seed for the hash.
		 * @return The 32 bit murmur hash of the bytes in the buffer.
		 */
		public static int hash(ByteBuffer buf, int seed) {
			// save byte order for later restoration
			ByteOrder byteOrder = buf.order();
			buf.order(ByteOrder.LITTLE_ENDIAN);

			int m = 0x5bd1e995;
			int r = 24;

			int h = seed ^ buf.remaining();

			int k;
			while (buf.remaining() >= 4) {
				k = buf.getInt();

				k *= m;
				k ^= k >>> r;
				k *= m;

				h *= m;
				h ^= k;
			}

			if (buf.remaining() > 0) {
				ByteBuffer finish = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
				// for big-endian version, use this first:
				// finish.position(4-buf.remaining());
				finish.put(buf).rewind();
				h ^= finish.getInt();
				h *= m;
			}

			h ^= h >>> 13;
			h *= m;
			h ^= h >>> 15;

			buf.order(byteOrder);
			return h;
		}

		public static long hash64A(byte[] data, int seed) {
			return hash64A(ByteBuffer.wrap(data), seed);
		}

		public static long hash64A(byte[] data, int offset, int length, int seed) {
			return hash64A(ByteBuffer.wrap(data, offset, length), seed);
		}

		public static long hash64A(ByteBuffer buf, int seed) {
			ByteOrder byteOrder = buf.order();
			buf.order(ByteOrder.LITTLE_ENDIAN);

			long m = 0xc6a4a7935bd1e995L;
			int r = 47;

			long h = seed ^ (buf.remaining() * m);

			long k;
			while (buf.remaining() >= 8) {
				k = buf.getLong();

				k *= m;
				k ^= k >>> r;
				k *= m;

				h ^= k;
				h *= m;
			}

			if (buf.remaining() > 0) {
				ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
				// for big-endian version, do this first:
				// finish.position(8-buf.remaining());
				finish.put(buf).rewind();
				h ^= finish.getLong();
				h *= m;
			}

			h ^= h >>> r;
			h *= m;
			h ^= h >>> r;

			buf.order(byteOrder);
			return h;
		}

		public static long hash(byte[] key) {
			return hash64A(key, 0x1234ABCD);
		}

		public static long hash64(String key) {
			try {
				return hash((key == null?"":key).getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				return 0L;
			}
		}
		
		public static int hash32(String key) {
			try {
				return hash((key == null?"":key).getBytes("utf-8"), 0x1234ABCD);
			} catch (UnsupportedEncodingException e) {
				return 0;
			}
		}
	}
	
//	public static byte[] encryptRijndael256(String key, String content) {
//		try{
//			byte[] kData = key.getBytes("utf-8");
//	
//			int padSize = 0;
//			// 16,24,32
//			int keySize = 16;
//	
//			if (kData.length > 16 && kData.length <= 32) {
//				for (int i = 1; i < 5; i++) {
//					keySize += (i << 3);
//					if (keySize > kData.length) {
//						break;
//					}
//				}
//				if ((padSize = kData.length % keySize) > 0) {
//					padSize = keySize - padSize;
//					byte[] temp = kData;
//					kData = new byte[kData.length + padSize];
//					System.arraycopy(temp, 0, kData, 0, temp.length);
//					for (int i = 0; i < padSize; i++) {
//						kData[temp.length + i] = 0;
//					}
//				}
//			} else if (kData.length <= 16) {
//				if ((padSize = kData.length % 16) > 0) {
//					padSize = 16 - padSize;
//					byte[] temp = kData;
//					kData = new byte[kData.length + padSize];
//					System.arraycopy(temp, 0, kData, 0, temp.length);
//					for (int i = 0; i < padSize; i++) {
//						kData[temp.length + i] = 0;
//					}
//				}
//			} else /*if (kData.length > 32)*/ {
//				byte[] temp = kData;
//				kData = new byte[32];
//				System.arraycopy(temp, 0, kData, 0, 32);
//			}
//	
//			int blockSize = 32;
//	
//			byte[] data = content.getBytes("utf-8");
//	
//			if ((padSize = data.length % blockSize) > 0) {
//				padSize = blockSize - padSize;
//				byte[] temp = data;
//				data = new byte[data.length + padSize];
//				System.arraycopy(temp, 0, data, 0, temp.length);
//				for (int i = 0; i < padSize; i++) {
//					data[temp.length + i] = 0;
//				}
//			}
//			
//			RijndaelEngine rijndaelEngine = new RijndaelEngine(256);
//			KeyParameter keyParam = new KeyParameter(kData);
//			rijndaelEngine.init(true, keyParam); // false == decrypt
//			PaddedBufferedBlockCipher bbc = new PaddedBufferedBlockCipher(
//					rijndaelEngine, new ZeroBytePadding());
//			byte[] encryptedBytes = new byte[bbc.getOutputSize(data.length)]; 
//			int processed = bbc.processBytes(data, 0, data.length, encryptedBytes,
//					0);
//			bbc.doFinal(encryptedBytes, processed);
//			return encryptedBytes;
//		}catch(Exception e){
//			throw new RuntimeException(e);
//		}
//	}
	
//	public static byte[] decryptRijndael256(String key, byte[] content) {
//		try{
//			byte[] kData = key.getBytes("utf-8");
//	
//			int padSize = 0;
//			// 16,24,32
//			int keySize = 16;
//	
//			if (kData.length > 16 && kData.length <= 32) {
//				for (int i = 1; i < 5; i++) {
//					keySize += (i << 3);
//					if (keySize > kData.length) {
//						break;
//					}
//				}
//				if ((padSize = kData.length % keySize) > 0) {
//					padSize = keySize - padSize;
//					byte[] temp = kData;
//					kData = new byte[kData.length + padSize];
//					System.arraycopy(temp, 0, kData, 0, temp.length);
//					for (int i = 0; i < padSize; i++) {
//						kData[temp.length + i] = 0;
//					}
//				}
//			} else if (kData.length <= 16) {
//				if ((padSize = kData.length % 16) > 0) {
//					padSize = 16 - padSize;
//					byte[] temp = kData;
//					kData = new byte[kData.length + padSize];
//					System.arraycopy(temp, 0, kData, 0, temp.length);
//					for (int i = 0; i < padSize; i++) {
//						kData[temp.length + i] = 0;
//					}
//				}
//			} else /*if (kData.length > 32)*/ {
//				byte[] temp = kData;
//				kData = new byte[32];
//				System.arraycopy(temp, 0, kData, 0, 32);
//			}
//			
//			RijndaelEngine rijndaelEngine = new RijndaelEngine(256);
//			KeyParameter keyParam = new KeyParameter(kData);
//			rijndaelEngine.init(false, keyParam); // false == decrypt
//			PaddedBufferedBlockCipher bbc = new PaddedBufferedBlockCipher(
//					rijndaelEngine, new ZeroBytePadding());
//			byte[] decryptedBytes = new byte[bbc.getOutputSize(content.length)]; 
//			int processed = bbc.processBytes(content, 0, content.length, decryptedBytes,
//					0);
//			bbc.doFinal(decryptedBytes, processed);
//			
//			int length = 0;
//			for(int i = decryptedBytes.length - 1; i != 0 ; i--){
//				length = i + 1;
//				if(decryptedBytes[i] != 0)
//					break;
//			}
//			if(length < decryptedBytes.length){
//				byte[] temp = decryptedBytes;
//				decryptedBytes = new byte[length];
//				System.arraycopy(temp, 0, decryptedBytes, 0, length);
//			}
//			return decryptedBytes;
//		}catch(Exception e){
//			throw new RuntimeException(e);
//		}
//	}
	
	public static void main(String[] args) {
		long now = System.currentTimeMillis();
		SecureRandom random = new SecureRandom();
		String ts = String.valueOf(now);
		String nonce = String.valueOf(random.nextInt(Integer.MAX_VALUE));
		StringBuilder str2Sign = new StringBuilder();
		
		str2Sign.append("JhwFvZZSD6muv").append(nonce).append(ts);
		
		System.out.println(nonce);
		System.out.println(ts);
		System.out.println(hexSHA1(str2Sign.toString()));
	}
}
