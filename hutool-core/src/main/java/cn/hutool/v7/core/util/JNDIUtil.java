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

package cn.hutool.v7.core.util;

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.convert.ConvertUtil;
import cn.hutool.v7.core.exception.HutoolException;
import cn.hutool.v7.core.exception.ValidateException;
import cn.hutool.v7.core.map.MapUtil;
import cn.hutool.v7.core.text.StrUtil;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * JNDI工具类<br>
 * JNDI是Java Naming and Directory Interface（JAVA命名和目录接口）的英文简写，<br>
 * 它是为JAVA应用程序提供命名和目录访问服务的API（Application Programing Interface，应用程序编程接口）。
 *
 * <p>
 * 见：<a href="https://blog.csdn.net/u010430304/article/details/54601302">https://blog.csdn.net/u010430304/article/details/54601302</a>
 * </p>
 *
 * @author loolY
 * @since 5.7.7
 */
public class JNDIUtil {

	/**
	 * 创建{@link InitialDirContext}
	 *
	 * @param environment 环境参数，{@code null}表示无参数
	 * @param allowedProtocols 允许的协议列表，{@code null}或空表示使用默认安全协议
	 *  @return {@link InitialDirContext}
	 */
	public static InitialDirContext createInitialDirContext(final Map<String, String> environment, final String... allowedProtocols) {
		try {
			if (MapUtil.isEmpty(environment)) {
				return new InitialDirContext();
			}

			// issue#4249 修复JNDI注入漏洞
			validateEnvironment(environment, allowedProtocols);

			return new InitialDirContext(ConvertUtil.convert(Hashtable.class, environment));
		} catch (final NamingException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 创建{@link InitialContext}
	 *
	 * @param environment 环境参数，{@code null}表示无参数
	 * @param allowedProtocols 允许的协议列表，{@code null}或空表示使用默认安全协议
	 * @return {@link InitialContext}
	 */
	public static InitialContext createInitialContext(final Map<String, String> environment, final String... allowedProtocols) {
		try {
			if (MapUtil.isEmpty(environment)) {
				return new InitialContext();
			}

			// issue#4249 修复JNDI注入漏洞
			validateEnvironment(environment, allowedProtocols);

			return new InitialContext(ConvertUtil.convert(Hashtable.class, environment));
		} catch (final NamingException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 获取指定容器环境的对象的属性<br>
	 * 如获取DNS属性，则URI为类似：dns:hutool.cn
	 *
	 * @param uri     URI字符串，格式为[scheme:][name]/[domain]
	 * @param attrIds 需要获取的属性ID名称
	 * @return {@link Attributes}
	 */
	public static Attributes getAttributes(final String uri, final String... attrIds) {
		try {
			return createInitialDirContext(null).getAttributes(uri, attrIds);
		} catch (final NamingException e) {
			throw new HutoolException(e);
		}
	}

	/**
	 * 默认安全的协议列表
	 */
	private static final List<String> SAFE_PROTOCOLS = Arrays.asList(
		"java:",
		"dns:"
	);

	/**
	 * 验证并过滤environment中的危险属性
	 *
	 * @param environment 原始环境参数
	 * @return 过滤后的环境参数
	 */
	private static Map<String, String> validateEnvironment(final Map<String, String> environment, final String... allowedProtocols) {
		if (MapUtil.isNotEmpty(environment)) {
			// 检查 PROVIDER_URL
			final String providerUrl = environment.get("java.naming.provider.url");
			if (StrUtil.isNotBlank(providerUrl) && !isSafeProtocol(providerUrl, allowedProtocols)) {
				throw new ValidateException("JNDI protocol not allowed: " + providerUrl);
			}

			// 检查 INITIAL_CONTEXT_FACTORY
			final String factory = environment.get("java.naming.factory.initial");
			if (StrUtil.isNotBlank(factory)) {
				// 只允许安全的工厂类
				if (!factory.startsWith("com.sun.jndi.dns.") &&
					!factory.startsWith("com.sun.jndi.ldap.") &&
					!factory.startsWith("com.sun.jndi.rmi.")) {
					throw new ValidateException("JNDI factory not allowed: " + factory);
				}
			}
		}

		return environment;
	}

	/**
	 * 检查URL是否在协议白名单内
	 *
	 * @param url              要检查的URL
	 * @param allowedProtocols 允许的协议列表，{@code null}或空表示使用默认安全协议
	 * @return 是否安全
	 */
	private static boolean isSafeProtocol(final String url, final String... allowedProtocols) {
		if (StrUtil.isBlank(url)) {
			return false;
		}

		final List<String> protocols = ArrayUtil.isNotEmpty(allowedProtocols)
			? Arrays.asList(allowedProtocols)
			: SAFE_PROTOCOLS;

		final String lowerUrl = url.toLowerCase();
		for (final String protocol : protocols) {
			if (lowerUrl.startsWith(protocol.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
