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

import cn.hutool.v7.core.text.StrUtil;

/**
 * JDK相关工具类，包括判断JDK版本等<br>
 * 工具部分方法来自fastjson2的JDKUtils
 *
 * @author fastjson, Looly
 */
public class JdkUtil {
	/**
	 * JDK版本
	 */
	public static final int JVM_VERSION;
	/**
	 * 是否大于等于JDK25
	 */
	public static final boolean IS_AT_LEAST_JDK25;

	/**
	 * 是否Android环境
	 */
	public static final boolean IS_ANDROID;
	/**
	 * 是否OPENJ9环境
	 */
	public static final boolean IS_OPENJ9;

	/**
	 * 是否GraalVM Native Image环境
	 */
	public static final boolean IS_GRAALVM_NATIVE;

	static {
		// JVM版本
		JVM_VERSION = _getJvmVersion();
		IS_AT_LEAST_JDK25 = JVM_VERSION >= 25;

		// JVM名称
		final String jvmName = _getJvmName();
		IS_ANDROID = jvmName.equals("Dalvik");
		IS_OPENJ9 = jvmName.contains("OpenJ9");

		// GraalVM
		IS_GRAALVM_NATIVE = null != System.getProperty("org.graalvm.nativeimage.imagecode");
	}

	/**
	 * 获取JVM名称
	 *
	 * @return JVM名称
	 */
	private static String _getJvmName() {
		return SystemUtil.getQuietly("java.vm.name");
	}

	/**
	 * 根据{@code java.specification.version}属性值，获取版本号<br>
	 * 默认17
	 *
	 * @return 版本号
	 */
	private static int _getJvmVersion() {
		int jvmVersion = 17;

		String javaSpecVer = SystemUtil.getQuietly("java.specification.version");
		if (StrUtil.isNotBlank(javaSpecVer)) {
			if (javaSpecVer.startsWith("1.")) {
				javaSpecVer = javaSpecVer.substring(2);
			}
			if (javaSpecVer.indexOf('.') == -1) {
				jvmVersion = Integer.parseInt(javaSpecVer);
			}
		}

		return jvmVersion;
	}
}
