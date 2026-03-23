package cn.hutool.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.*;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SecureUtil类单元测试
 */
public class SecureUtilTest {

	private static final String TEST_CONTENT = "test中文";
	private static final String TEST_DATA = "test data";
	private static final byte[] TEST_KEY = RandomUtil.randomBytes(16);

	@Test
	public void getAlgorithmAfterWithTest() {
		String algorithm = SecureUtil.getAlgorithmAfterWith("SHA256withRSA");
		assertEquals("RSA", algorithm);

		algorithm = SecureUtil.getAlgorithmAfterWith("NONEwithECDSA");
		assertEquals("EC", algorithm);
	}

	@Test
	public void generateAlgorithmTest() {
		String algorithm = SecureUtil.generateAlgorithm(AsymmetricAlgorithm.RSA, DigestAlgorithm.SHA256);
		assertEquals("SHA256withRSA", algorithm);

		algorithm = SecureUtil.generateAlgorithm(AsymmetricAlgorithm.RSA, null);
		assertEquals("NONEwithRSA", algorithm);
	}

	@Test
	public void aesTest() {
		AES aes = SecureUtil.aes();
		assertNotNull(aes);

		AES aesWithKey = SecureUtil.aes(TEST_KEY);
		assertNotNull(aesWithKey);

		// 测试加密解密
		String encrypted = aesWithKey.encryptBase64(TEST_CONTENT);
		String decrypted = aesWithKey.decryptStr(encrypted, CharsetUtil.CHARSET_UTF_8);
		assertEquals(TEST_CONTENT, decrypted);
	}

	@Test
	public void desTest() {
		DES des = SecureUtil.des();
		assertNotNull(des);

		DES desWithKey = SecureUtil.des(RandomUtil.randomBytes(8));
		assertNotNull(desWithKey);

		// 测试加密解密
		String encrypted = desWithKey.encryptBase64(TEST_CONTENT);
		String decrypted = desWithKey.decryptStr(encrypted, CharsetUtil.CHARSET_UTF_8);
		assertEquals(TEST_CONTENT, decrypted);
	}

	@Test
	public void md5Test() {
		// 测试MD5对象
		MD5 md5 = SecureUtil.md5();
		assertNotNull(md5);

		// 测试字符串MD5
		String md5Str = SecureUtil.md5(TEST_DATA);
		assertNotNull(md5Str);
		assertEquals(32, md5Str.length()); // MD5是32位十六进制字符串

		// 测试文件MD5
		try {
			File tempFile = File.createTempFile("test", ".txt");
			FileUtil.writeString(TEST_DATA, tempFile, CharsetUtil.CHARSET_UTF_8);
			String fileMd5 = SecureUtil.md5(tempFile);
			assertNotNull(fileMd5);
			assertEquals(32, fileMd5.length());
		} catch (Exception e) {
			fail("File MD5 test failed: " + e.getMessage());
		}

		// 测试InputStream MD5
		InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes());
		String streamMd5 = SecureUtil.md5(is);
		assertNotNull(streamMd5);
		assertEquals(32, streamMd5.length());
	}

	@Test
	public void sha1Test() {
		// 测试SHA1对象
		Digester sha1 = SecureUtil.sha1();
		assertNotNull(sha1);

		// 测试字符串SHA1
		String sha1Str = SecureUtil.sha1(TEST_DATA);
		assertNotNull(sha1Str);
		assertEquals(40, sha1Str.length()); // SHA1是40位十六进制字符串

		// 测试InputStream SHA1
		InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes());
		String streamSha1 = SecureUtil.sha1(is);
		assertNotNull(streamSha1);
		assertEquals(40, streamSha1.length());
	}

	@Test
	public void sha256Test() {
		// 测试SHA256对象
		Digester sha256 = SecureUtil.sha256();
		assertNotNull(sha256);

		// 测试字符串SHA256
		String sha256Str = SecureUtil.sha256(TEST_DATA);
		assertNotNull(sha256Str);
		assertEquals(64, sha256Str.length()); // SHA256是64位十六进制字符串

		// 测试InputStream SHA256
		InputStream is = new ByteArrayInputStream(TEST_DATA.getBytes());
		String streamSha256 = SecureUtil.sha256(is);
		assertNotNull(streamSha256);
		assertEquals(64, streamSha256.length());
	}

	@Test
	public void hmacSha1AndSha256KeyGenerationTest() {
		// 验证当传入null时，生成的密钥类型是否正确
		HMac hmacSha1 = SecureUtil.hmacSha1((byte[]) null);
		HMac hmacSha256 = SecureUtil.hmacSha256((byte[]) null);

		assertNotNull(hmacSha1);
		assertNotNull(hmacSha256);

		// 验证两个HMac对象使用不同的算法，结果长度也应不同
		String sha1Result = hmacSha1.digestHex(TEST_DATA);
		String sha256Result = hmacSha256.digestHex(TEST_DATA);

		assertEquals(40, sha1Result.length()); // SHA1 HMAC应为40字符
		assertEquals(64, sha256Result.length()); // SHA256 HMAC应为64字符
	}

	@Test
	public void hmacTest() {
		// 测试HMac对象生成
		HMac hmac = SecureUtil.hmac(HmacAlgorithm.HmacSHA256, TEST_KEY);
		assertNotNull(hmac);

		// 测试字符串密钥
		HMac hmac2 = SecureUtil.hmac(HmacAlgorithm.HmacMD5, "testkey");
		assertNotNull(hmac2);

		// 测试SecretKey
		SecretKey secretKey = new SecretKeySpec(TEST_KEY, "HmacSHA256");
		HMac hmac3 = SecureUtil.hmac(HmacAlgorithm.HmacSHA256, secretKey);
		assertNotNull(hmac3);
	}

	@Test
	public void hmacMd5Test() {
		HMac hmacMd5 = SecureUtil.hmacMd5();
		assertNotNull(hmacMd5);

		HMac hmacMd5WithKey = SecureUtil.hmacMd5("testkey");
		assertNotNull(hmacMd5WithKey);

		HMac hmacMd5WithBytes = SecureUtil.hmacMd5(TEST_KEY);
		assertNotNull(hmacMd5WithBytes);

		// 验证加密结果
		String result = hmacMd5WithKey.digestHex(TEST_DATA);
		assertNotNull(result);
		assertEquals(32, result.length()); // MD5 HMAC是32位十六进制字符串
	}

	@Test
	public void hmacSha1Test() {
		HMac hmacSha1 = SecureUtil.hmacSha1();
		assertNotNull(hmacSha1);

		HMac hmacSha1WithKey = SecureUtil.hmacSha1("testkey");
		assertNotNull(hmacSha1WithKey);

		HMac hmacSha1WithBytes = SecureUtil.hmacSha1(TEST_KEY);
		assertNotNull(hmacSha1WithBytes);

		// 验证加密结果
		String result = hmacSha1WithKey.digestHex(TEST_DATA);
		assertNotNull(result);
		assertEquals(40, result.length()); // SHA1 HMAC是40位十六进制字符串
	}

	@Test
	public void hmacSha256Test() {
		HMac hmacSha256 = SecureUtil.hmacSha256();
		assertNotNull(hmacSha256);

		HMac hmacSha256WithKey = SecureUtil.hmacSha256("testkey");
		assertNotNull(hmacSha256WithKey);

		HMac hmacSha256WithBytes = SecureUtil.hmacSha256(TEST_KEY);
		assertNotNull(hmacSha256WithBytes);

		// 验证加密结果
		String result = hmacSha256WithKey.digestHex(TEST_DATA);
		assertNotNull(result);
		assertEquals(64, result.length()); // SHA256 HMAC是64位十六进制字符串
	}

	@Test
	public void signTest() {
		// 测试生成签名对象
		Sign sign = SecureUtil.sign(SignAlgorithm.NONEwithRSA);
		assertNotNull(sign);

		// 测试使用密钥生成签名对象
		KeyPair keyPair = SecureUtil.generateKeyPair("RSA", 512);
		Sign sign2 = SecureUtil.sign(SignAlgorithm.SHA256withRSA, keyPair.getPrivate().getEncoded(),
			keyPair.getPublic().getEncoded());
		assertNotNull(sign2);

		// 测试签名功能
		byte[] signed = sign2.sign(TEST_DATA.getBytes());
		assertTrue(sign2.verify(TEST_DATA.getBytes(), signed));
	}

	@Test
	public void decodeTest() {
		// 测试Hex解码
		String hexStr = HexUtil.encodeHexStr(TEST_DATA.getBytes());
		byte[] decodedHex = SecureUtil.decode(hexStr);
		assertArrayEquals(TEST_DATA.getBytes(), decodedHex);

		// 测试Base64解码
		String base64Str = Base64.encode(TEST_DATA);
		byte[] decodedBase64 = SecureUtil.decode(base64Str);
		assertArrayEquals(TEST_DATA.getBytes(), decodedBase64);
	}
}
