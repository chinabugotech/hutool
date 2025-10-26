/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.crypto.asymmetric;

/**
 * 签名算法类型<br>
 * see: <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Signature">Signature</a>
 *
 * @author Looly
 */
public enum SignAlgorithm {
	// The RSA signature algorithm
	/**
	 * RSA签名算法
	 */
	NONEwithRSA("NONEwithRSA"),

	// The MD2/MD5 with RSA Encryption signature algorithm
	/**
	 * MD2withMD5签名算法
	 */
	MD2withRSA("MD2withRSA"),
	/**
	 * MD5withRSA签名算法
	 */
	MD5withRSA("MD5withRSA"),

	// The signature algorithm with SHA-* and the RSA
	/**
	 * SHA1withRSA签名算法
	 */
	SHA1withRSA("SHA1withRSA"),
	/**
	 * SHA256withRSA签名算法
	 */
	SHA256withRSA("SHA256withRSA"),
	/**
	 * SHA384withRSA签名算法
	 */
	SHA384withRSA("SHA384withRSA"),
	/**
	 * SHA512withRSA签名算法
	 */
	SHA512withRSA("SHA512withRSA"),

	// The Digital Signature Algorithm
	/**
	 * DSA签名算法
	 */
	NONEwithDSA("NONEwithDSA"),
	// The DSA with SHA-1 signature algorithm
	/**
	 * DSAwithSHA1签名算法
	 */
	SHA1withDSA("SHA1withDSA"),

	// The ECDSA signature algorithms
	/**
	 * ECDSA签名算法
	 */
	NONEwithECDSA("NONEwithECDSA"),
	/**
	 * ECDSAwithSHA1签名算法
	 */
	SHA1withECDSA("SHA1withECDSA"),
	/**
	 * ECDSAwithSHA256签名算法
	 */
	SHA256withECDSA("SHA256withECDSA"),
	/**
	 * ECDSAwithSHA384签名算法
	 */
	SHA384withECDSA("SHA384withECDSA"),
	/**
	 * ECDSAwithSHA512签名算法
	 */
	SHA512withECDSA("SHA512withECDSA"),

	// 需要BC库加入支持
	/**
	 * SHA256withRSA/PSS签名算法
	 */
	SHA256withRSA_PSS("SHA256WithRSA/PSS"),
	/**
	 * SHA384withRSA/PSS签名算法
	 */
	SHA384withRSA_PSS("SHA384WithRSA/PSS"),
	/**
	 * SHA512withRSA/PSS签名算法
	 */
	SHA512withRSA_PSS("SHA512WithRSA/PSS");

	private final String value;

	/**
	 * 构造
	 *
	 * @param value 算法字符表示，区分大小写
	 */
	SignAlgorithm(final String value) {
		this.value = value;
	}

	/**
	 * 获取算法字符串表示，区分大小写
	 *
	 * @return 算法字符串表示
	 */
	public String getValue() {
		return this.value;
	}
}
