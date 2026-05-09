package cn.hutool.core.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.map.MapUtil;

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
 * 见：https://blog.csdn.net/u010430304/article/details/54601302
 * </p>
 *
 * @author looly
 * @since 5.7.7
 */
public class JNDIUtil {

	/**
	 * 创建{@link InitialDirContext}
	 * 建议在应用启动时设置系统属性（禁用远程 codebase 加载）
	 * <pre>{@code
	 *   System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "false");
	 *   System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "false");
	 * }</pre>
	 *
	 * @param environment 环境参数，如@{code java.naming.factory.initial}和{@code java.naming.provider.url}，{@code null}表示无参数
	 * @return {@link InitialDirContext}
	 */
	public static InitialDirContext createInitialDirContext(Map<String, String> environment) {
		try {
			if (MapUtil.isEmpty(environment)) {
				return new InitialDirContext();
			}

			// issue#4249 修复JNDI注入漏洞
			validateEnvironment(environment);

			return new InitialDirContext(Convert.convert(Hashtable.class, environment));
		} catch (NamingException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 创建{@link InitialContext}<br>
	 * 建议在应用启动时设置系统属性（禁用远程 codebase 加载）
	 * <pre>{@code
	 *   System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "false");
	 *   System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "false");
	 * }</pre>
	 *
	 * @param environment 环境参数，如@{code java.naming.factory.initial}和{@code java.naming.provider.url}，{@code null}表示无参数
	 * @return {@link InitialContext}
	 */
	public static InitialContext createInitialContext(Map<String, String> environment) {
		try {
			if (MapUtil.isEmpty(environment)) {
				return new InitialContext();
			}

			// issue#4249 修复JNDI注入漏洞
			validateEnvironment(environment);

			return new InitialContext(Convert.convert(Hashtable.class, environment));
		} catch (NamingException e) {
			throw new UtilException(e);
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
	public static Attributes getAttributes(String uri, String... attrIds) {
		try {
			return createInitialDirContext(null).getAttributes(uri, attrIds);
		} catch (NamingException e) {
			throw new UtilException(e);
		}
	}

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
	private static Map<String, String> validateEnvironment(Map<String, String> environment) {
		if (MapUtil.isNotEmpty(environment)) {
			// 检查 PROVIDER_URL
			String providerUrl = environment.get("java.naming.provider.url");
			if (StrUtil.isNotBlank(providerUrl) && !isSafeProtocol(providerUrl)) {
				throw new UtilException("JNDI protocol not allowed: " + providerUrl);
			}

			// 检查 INITIAL_CONTEXT_FACTORY
			String factory = environment.get("java.naming.factory.initial");
			if (StrUtil.isNotBlank(factory)) {
				// 只允许安全的工厂类
				if (!factory.startsWith("com.sun.jndi.dns.") &&
					!factory.startsWith("com.sun.jndi.ldap.") &&
					!factory.startsWith("com.sun.jndi.rmi.")) {
					throw new UtilException("JNDI factory not allowed: " + factory);
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
	private static boolean isSafeProtocol(String url, String... allowedProtocols) {
		if (StrUtil.isBlank(url)) {
			return false;
		}

		List<String> protocols = (allowedProtocols != null && allowedProtocols.length > 0)
			? Arrays.asList(allowedProtocols)
			: SAFE_PROTOCOLS;

		String lowerUrl = url.toLowerCase();
		for (String protocol : protocols) {
			if (lowerUrl.startsWith(protocol.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
