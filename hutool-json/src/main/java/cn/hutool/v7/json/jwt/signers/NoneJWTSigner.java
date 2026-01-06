/*
 * Copyright (c) 2013-2026 Hutool Team.
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

package cn.hutool.v7.json.jwt.signers;

import cn.hutool.v7.core.text.StrUtil;

/**
 * 无需签名的JWT签名器
 *
 * @author Looly
 * @since 5.7.0
 */
public class NoneJWTSigner implements JWTSigner {

	/**
	 * 定义一个常量ID_NONE，表示没有ID的情况
	 */
	public static final String ID_NONE = "none";

	/**
	 * 创建一个NoneJWTSigner实例，用于处理没有签名的JWT
	 */
	public static NoneJWTSigner NONE = new NoneJWTSigner();

	/**
	 * 判断给定的算法是否为无签名的算法
	 *
	 * @param alg 算法
	 * @return 如果是无签名的算法，则返回true；否则返回false
	 */
	public static boolean isNone(final String alg) {
		return StrUtil.isBlank( alg) || StrUtil.equalsIgnoreCase(alg, ID_NONE);
	}


	@Override
	public String sign(final String headerBase64, final String payloadBase64) {
		return StrUtil.EMPTY;
	}

	@Override
	public boolean verify(final String headerBase64, final String payloadBase64, final String signBase64) {
		return StrUtil.isEmpty(signBase64);
	}

	@Override
	public String getAlgorithm() {
		return ID_NONE;
	}
}
