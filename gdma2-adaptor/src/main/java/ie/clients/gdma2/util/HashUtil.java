package ie.clients.gdma2.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashUtil extends SecurityUtil {

	private static Logger logger = LoggerFactory.getLogger(HashUtil.class.getName());
	
	
	public static enum HashAlgorithm {
		MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256"), SHA384("SHA-384"), SHA512(
				"SHA-512");

		private final String algorithm;

		private HashAlgorithm(String code) {
			this.algorithm = code;
		}

		public String algorithm() {
			return algorithm;
		}
	}

	private static HashAlgorithm defaultHashAlgorithm = HashAlgorithm.SHA256;
	private static String defaultEncoding = "UTF-8";

	public static String hash(String clearData) {
		return hash(clearData, defaultHashAlgorithm, defaultEncoding);
	}

	public static String hash(String clearData, String encoding) {
		return hash(clearData, defaultHashAlgorithm, encoding);
	}

	public static String hash(String clearData, HashAlgorithm ha) {
		return hash(clearData, ha, defaultEncoding);
	}

	public static String hash(String clearData, HashAlgorithm ha,
			String encoding) {

		String result= byteToHex(hashDigest(clearData, ha, encoding));
//		logger.debug("ClearData: " + clearData + ", Hashed: " + result);
		return result;
	}

	public static byte[] hashDigest(String clearData, HashAlgorithm ha,
			String encoding) {
		try {
//			logger.debug("Hashing using algo: " + ha.algorithm() + ", encoding: " + encoding);
			MessageDigest digest = MessageDigest.getInstance(ha.algorithm());
			digest.reset();
			digest.update(clearData.getBytes(encoding));
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new byte[10];
	}

}
