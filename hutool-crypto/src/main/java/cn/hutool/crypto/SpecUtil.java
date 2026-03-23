package cn.hutool.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Element;

import javax.crypto.spec.*;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;

/**
 * 规范相关工具类，用于生成密钥规范、参数规范等快捷方法。
 * <ul>
 *     <li>{@link KeySpec}: 密钥规范</li>
 *     <li>{@link java.security.spec.AlgorithmParameterSpec}: 参数规范</li>
 * </ul>
 *
 * @author Looly
 * @since 5.8.41
 */
public class SpecUtil {

	/**
	 * 根据算法创建{@link KeySpec}
	 * <ul>
	 *     <li>DESede: {@link DESedeKeySpec}</li>
	 *     <li>DES   : {@link DESedeKeySpec}</li>
	 *     <li>其它  : {@link SecretKeySpec}</li>
	 * </ul>
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @return {@link KeySpec}
	 */
	public static KeySpec createKeySpec(final String algorithm, byte[] key) {
		try {
			if (algorithm.startsWith("DESede")) {
				if (null == key) {
					key = RandomUtil.randomBytes(24);
				}
				// DESede兼容
				return new DESedeKeySpec(key);
			} else if (algorithm.startsWith("DES")) {
				if (null == key) {
					key = RandomUtil.randomBytes(8);
				}
				return new DESKeySpec(key);
			}
		} catch (final InvalidKeyException e) {
			throw new CryptoException(e);
		}

		return new SecretKeySpec(key, algorithm);
	}

	/**
	 * 创建{@link PBEKeySpec}<br>
	 * PBE算法没有密钥的概念，密钥在其它对称加密算法中是经过算法计算得出来的，PBE算法则是使用口令替代了密钥。
	 *
	 * @param password 口令
	 * @return {@link PBEKeySpec}
	 */
	public static PBEKeySpec createPBEKeySpec(char[] password) {
		if (null == password) {
			password = RandomUtil.randomStringLower(32).toCharArray();
		}
		return new PBEKeySpec(password);
	}

	/**
	 * 创建{@link PBEParameterSpec}
	 *
	 * @param salt           加盐值
	 * @param iterationCount 摘要次数
	 * @return {@link PBEParameterSpec}
	 */
	public static PBEParameterSpec createPBEParameterSpec(final byte[] salt, final int iterationCount) {
		return new PBEParameterSpec(salt, iterationCount);
	}

	/**
	 * 将XML格式的密钥参数转化为{@link RSAPrivateCrtKeySpec}，XML为C#生成格式，类似于：
	 * <pre>{@code
	 * <RSAKeyValue>
	 *     <Modulus>xx</Modulus>
	 *     <Exponent>xx</Exponent>
	 *     <P>xxxxxxxxx</P>
	 *     <Q>xxxxxxxxx</Q>
	 *     <DP>xxxxxxxx</DP>
	 *     <DQ>xxxxxxxx</DQ>
	 *     <InverseQ>xx</InverseQ>
	 *     <D>xxxxxxxxx</D>
	 * </RSAKeyValue>
	 * }</pre>
	 *
	 * @param xml xml格式密钥字符串
	 * @return {@link RSAPrivateCrtKeySpec}
	 */
	public static RSAPrivateCrtKeySpec xmlToRSAPrivateCrtKeySpec(final String xml) {
		// 1. 解析XML
		final Element rootElement = XmlUtil.getRootElement(XmlUtil.parseXml(xml));

		// 2. 提取各个字段
		final String modulusB64 = XmlUtil.elementText(rootElement, "Modulus");
		final String exponentB64 = XmlUtil.elementText(rootElement, "Exponent");
		final String pB64 = XmlUtil.elementText(rootElement, "P");
		final String qB64 = XmlUtil.elementText(rootElement, "Q");
		final String dpB64 = XmlUtil.elementText(rootElement, "DP");
		final String dqB64 = XmlUtil.elementText(rootElement, "DQ");
		final String inverseQB64 = XmlUtil.elementText(rootElement, "InverseQ");
		final String dB64 = XmlUtil.elementText(rootElement, "D");

		// 3. Base64解码
		final byte[] modulus = Base64.decode(modulusB64);
		final byte[] publicExponent = Base64.decode(exponentB64);
		final byte[] privateExponent = Base64.decode(dB64);
		final byte[] primeP = Base64.decode(pB64);
		final byte[] primeQ = Base64.decode(qB64);
		final byte[] primeExponentP = Base64.decode(dpB64);
		final byte[] primeExponentQ = Base64.decode(dqB64);
		final byte[] crtCoefficient = Base64.decode(inverseQB64);

		// 4. 创建RSAPrivateCrtKeySpec
		return new RSAPrivateCrtKeySpec(
			new BigInteger(1, modulus),
			new BigInteger(1, publicExponent),
			new BigInteger(1, privateExponent),
			new BigInteger(1, primeP),
			new BigInteger(1, primeQ),
			new BigInteger(1, primeExponentP),
			new BigInteger(1, primeExponentQ),
			new BigInteger(1, crtCoefficient)
		);
	}
}
