package richard.test.token;

import com.ning.http.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created with IntelliJ IDEA. User: srp Date: 14-3-18 Time: 下午5:00
 */
public class Token {
	public int tokenHash;
	public String tokenPrefix;
	public String deviceId;
	public String appId;
	public long tokenTime;
	public String userId;

	public Token(String appId, String userId, String deviceId) throws Exception {
		this.appId = appId;
		this.userId = userId;
		this.deviceId = getHex8(deviceId);
		this.tokenTime = System.currentTimeMillis();
	}

	public Token() {
	}

	public String toTokenString(byte[] secureKey) throws Exception {
		if (this.deviceId.contains("}|{")) {
			throw new Exception("deviceId-" + this.deviceId + " is invalid.");
		}
		String base64UserId = Base64.encode(this.userId.getBytes());
		String baseString = String.format("f}|{%s}|{%s}|{%d}|{%s", this.deviceId, this.appId,
				Long.valueOf(this.tokenTime), base64UserId);
		this.tokenHash = baseString.hashCode();
		baseString = this.tokenHash + "}|{" + baseString;
		return Base64.encode(DES.encrypt(secureKey, baseString));
	}

	public static Token fromTokenString(byte[] secureKey, String tokenString) throws Exception {
		Token token = new Token();
		try {
			String tokenStr = DES.decrypt(secureKey, Base64.decode(tokenString.trim()));

			fillingToken(tokenStr, token);
		} catch (Exception e) {
			throw new Exception(tokenString + " Error!", e);
		}

		if (verifyTokenHash(token)) {
			return token;
		}
		throw new Exception("Miss match token hash!");
	}

	private static boolean verifyTokenHash(Token token) {
		return token.tokenPrefix.equals("f");
	}

	/**
	 * Created with IntelliJ IDEA.WARN 可能有线程安全问题 WARN
	 * 如果加密的key被重置，目前只能重启，以后做reload User: srp Date: 14-3-20 Time: 下午2:31
	 */
	private static class DES {
		// DES算法要求有一个可信任的随机数源
		private static SecureRandom sr = new SecureRandom();
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
		private static SecretKeyFactory keyFactory;
		// 为我们选择的DES算法生成一个KeyGenerator对象
		private static KeyGenerator kg;

		static {
			try {
				keyFactory = SecretKeyFactory.getInstance("DES");
				kg = KeyGenerator.getInstance("DES");
				kg.init(sr);
			} catch (NoSuchAlgorithmException neverHappens) {
				neverHappens.printStackTrace();
			}
		}

		/**
		 * 生成一个key用于加密解密
		 * 
		 * @return
		 */
		public static byte[] generateKey() {
			// 生成密匙
			SecretKey key = kg.generateKey();
			// 获取密匙数据
			byte rawKeyData[] = key.getEncoded();
			return rawKeyData;
		}

		/**
		 * 加密方法
		 * 
		 * @param rawKeyData
		 * @param str
		 * @return
		 * @throws  Exception
		 */
		public static byte[] encrypt(byte rawKeyData[], String str) throws Exception {
			try {
				// 从原始密匙数据创建一个DESKeySpec对象
				DESKeySpec dks = new DESKeySpec(rawKeyData);
				SecretKey key = keyFactory.generateSecret(dks);
				// Cipher对象实际完成加密操作
				Cipher cipher = Cipher.getInstance("DES");
				// 用密匙初始化Cipher对象
				cipher.init(Cipher.ENCRYPT_MODE, key, sr);
				// 现在，获取数据并加密
				byte data[] = str.getBytes();
				// 正式执行加密操作
				byte[] encryptedData = cipher.doFinal(data);
				return encryptedData;
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}

		/**
		 * 解密方法
		 * 
		 * @param rawKeyData
		 * @param encryptedData
		 * @throws Exception
		 */
		public static String decrypt(byte rawKeyData[], byte[] encryptedData) throws Exception {

			// 从原始密匙数据创建一个DESKeySpec对象
			DESKeySpec dks = null;
			try {
				dks = new DESKeySpec(rawKeyData);
				SecretKey key = keyFactory.generateSecret(dks);
				// Cipher对象实际完成解密操作
				Cipher cipher = Cipher.getInstance("DES");
				// 用密匙初始化Cipher对象
				cipher.init(Cipher.DECRYPT_MODE, key, sr);
				// 正式执行解密操作
				byte decryptedData[] = cipher.doFinal(encryptedData);
				return new String(decryptedData);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}

		}
	}

	public static Token fromTokenString(String secureKey, String tokenString) throws Exception {
		Token token = new Token();
		try {
			String tokenStr = DES.decrypt(secureKey.getBytes("UTF-8"), Base64.decode(tokenString));
			fillingToken(tokenStr, token);
		} catch (Exception e) {
			throw new Exception(tokenString + " Error!", e);
		}

		if (verifyTokenHash(token)) {
			return token;
		}
		throw new Exception("Miss match token hash!");
	}

	public boolean checkDeviceId(String deviceId) throws Exception {

		if (this.deviceId.length() == 0) {
			return true;
		}

		if (!this.deviceId.startsWith("RV1:")) {
			return true;
		}
		String did = getHex8(deviceId);
		return this.deviceId.equals(did);
	}

	protected String getHex8(String deviceId) throws Exception {
		if (deviceId.length() == 0) {
			return "";
		}
		if (deviceId.contains("}|{")) {
			throw new Exception("deviceId-" + deviceId + " is invalid.");
		}
		String md532 = CodecNCrypto.hexMD5(deviceId);
		return ("RV1:" + md532.substring(11, 19));
	}

	private static void fillingToken(String tokenString, Token token) {
		String[] base = tokenString.split("\\}\\|\\{");
		token.tokenHash = (Integer.parseInt(base[0]));
		token.tokenPrefix = (base[1]);
		token.deviceId = base[2];
		
		if (base[3].equals("e5t4ouvpegsxa")) {
			token.appId = "mgb7ka1nmyp5q";
		}else if (base[3].equals("mgb7ka1nmyp5g")) {
			token.appId = "82hegw5u8mb94";
		}else{
			token.appId = (base[3]);
		}
			
		 
		token.tokenTime = (Long.parseLong(base[4]));
		token.userId = new String(Base64.decode(base[5]));
	}

	public static void main(String[] arg) throws Exception {
		Token t = Token.fromTokenString("k51hidwqkclib",
				"4qHy6VBgAdy5rYg7yd1Ck0FI5WQtHMVN%2bJdFdHE71j4619Ri%2bveFXLczSKDw0FzkboVo6gBHqu0%3d");
		System.out.println(t.appId);
		System.out.println(t.deviceId);
		System.out.println(t.checkDeviceId("xiaoqiaomonitor6A8"));
	}
}