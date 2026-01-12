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

package cn.hutool.v7.extra.management;

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.regex.ReUtil;
import cn.hutool.v7.core.util.SystemUtil;

import java.io.Serial;
import java.io.Serializable;

/**
 * 代表Java Implementation的信息。
 *
 * @see ManagementUtil#getJavaInfo()  使用方式
 */
public class JavaInfo implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private static final String version = SystemUtil.get("java.version", false);
	private final float versionFloat = getJavaVersionAsFloat();
	private final int versionInt = getJavaVersionAsInt();
	private final String vendor = SystemUtil.get("java.vendor", false);
	private final String vendorUrl = SystemUtil.get("java.vendor.url", false);

	private final boolean IS_JAVA_17 = getJavaVersionMatches("17");
	private final boolean IS_JAVA_18 = getJavaVersionMatches("18");
	private final boolean IS_JAVA_19 = getJavaVersionMatches("19");
	private final boolean IS_JAVA_20 = getJavaVersionMatches("20");
	private final boolean IS_JAVA_21 = getJavaVersionMatches("21");
	private final boolean IS_JAVA_22 = getJavaVersionMatches("22");
	private final boolean IS_JAVA_23 = getJavaVersionMatches("23");
	private final boolean IS_JAVA_24 = getJavaVersionMatches("24");
	private final boolean IS_JAVA_25 = getJavaVersionMatches("25");

	/**
	 * 取得当前Java impl.的版本（取自系统属性：{@code java.version}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "1.4.2"}
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 * @since Java 1.1
	 */
	public final String getVersion() {
		return version;
	}

	/**
	 * 取得当前Java impl.的版本（取自系统属性：{@code java.version}）。
	 *
	 * <p>
	 * 例如：
	 *
	 * <ul>
	 * <li>JDK 1.2：{@code 1.2f}。</li>
	 * <li>JDK 1.3.1：{@code 1.31f}</li>
	 * </ul>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code 0}。
	 */
	public final float getVersionFloat() {
		return versionFloat;
	}

	/**
	 * 取得当前Java impl.的版本（取自系统属性：{@code java.version}），java10及其之后的版本返回值为4位。
	 *
	 * <p>
	 * 例如：
	 *
	 * <ul>
	 * <li>JDK 1.2：{@code 120}。</li>
	 * <li>JDK 1.3.1：{@code 131}</li>
	 * <li>JDK 11.0.2：{@code 1102}</li>
	 * </ul>
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code 0}。
	 * @since Java 1.1
	 */
	public final int getVersionInt() {
		return versionInt;
	}

	/**
	 * 返回1位整型的java版本，（取自系统属性：{@code java.version}）如：7、8、11、15、17、18，返回1位，java10及其之后的版本返回值为2位
	 * <ul>
	 *     <li>JDK 1.8.0_211：{@code 8}</li>
	 *     <li>JDK 11.0.2：{@code 11}</li>
	 *     <li>JDK 13.0.11：{@code 13}</li>
	 *     <li>JDK 15.0.7：{@code 15}</li>
	 *     <li>JDK 17.0.3：{@code 17}</li>
	 *     <li>JDK 18.0.1.1：{@code 18}</li>
	 * </ul>
	 *
	 * @return 版本
	 * @author dazer
	 * @since 7.0.0
	 */
	public final int getVersionIntSimple() {
		if (version == null) {
			return 0;
		}
		if (version.startsWith("1.")) {
			return Integer.parseInt(version.split("\\.")[1]);
		}
		return Integer.parseInt(version.split("\\.")[0]);
	}

	/**
	 * 取得当前Java impl.的版本的{@code float}值。
	 *
	 * @return Java版本的<code>float</code>值或{@code 0}
	 */
	private float getJavaVersionAsFloat() {
		if (version == null) {
			return 0f;
		}

		String str = version;

		str = ReUtil.get("^[0-9]{1,2}(\\.[0-9]{1,2})?", str, 0);

		return Float.parseFloat(str);
	}

	/**
	 * 取得当前Java impl.的版本的{@code int}值。
	 *
	 * @return Java版本的<code>int</code>值或{@code 0}
	 */
	private int getJavaVersionAsInt() {
		if (version == null) {
			return 0;
		}

		final String javaVersion = ReUtil.get("^[0-9]{1,2}(\\.[0-9]{1,2}){0,2}", version, 0);

		final String[] split = javaVersion.split("\\.");
		String result = ArrayUtil.join(split, "");

		//保证java10及其之后的版本返回的值为4位
		if (split[0].length() > 1) {
			result = (result + "0000").substring(0, 4);
		}

		return Integer.parseInt(result);
	}

	/**
	 * 取得当前Java impl.的厂商（取自系统属性：{@code java.vendor}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "Sun Microsystems Inc."}
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 * @since Java 1.1
	 */
	public final String getVendor() {
		return vendor;
	}

	/**
	 * 取得当前Java impl.的厂商网站的URL（取自系统属性：{@code java.vendor.url}）。
	 *
	 * <p>
	 * 例如Sun JDK 1.4.2：{@code "<a href="http://java.sun.com/">http://java.sun.com/</a>"}
	 *
	 * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回{@code null}。
	 * @since Java 1.1
	 */
	public final String getVendorURL() {
		return vendorUrl;
	}

	/**
	 * 当前java的版本是否为Java17。
	 *
	 * @return 是否版本17
	 */
	public final boolean isJava17() {
		return IS_JAVA_17;
	}

	/**
	 * 当前java的版本是否为Java18。
	 *
	 * @return 是否版本18
	 */
	public final boolean isJava18() {
		return IS_JAVA_18;
	}

	/**
	 * 当前java的版本是否为Java19。
	 *
	 * @return 是否版本19
	 */
	public final boolean isJava19() {
		return IS_JAVA_19;
	}

	/**
	 * 当前java的版本是否为Java20。
	 *
	 * @return 是否版本21
	 */
	public final boolean isJava20() {
		return IS_JAVA_20;
	}

	/**
	 * 当前java的版本是否为Java21。
	 *
	 * @return 是否版本21
	 */
	public final boolean isJava21() {
		return IS_JAVA_21;
	}

	/**
	 * 当前java的版本是否为Java22。
	 *
	 * @return 是否版本22
	 */
	public final boolean isJava22() {
		return IS_JAVA_22;
	}

	/**
	 * 当前java的版本是否为Java23。
	 *
	 * @return 是否版本23
	 */
	public final boolean isJava23() {
		return IS_JAVA_23;
	}

	/**
	 * 当前java的版本是否为Java24。
	 *
	 * @return 是否版本24
	 */
	public final boolean isJava24() {
		return IS_JAVA_24;
	}

	/**
	 * 当前java的版本是否为Java25。
	 *
	 * @return 是否版本25
	 */
	public final boolean isJava25() {
		return IS_JAVA_25;
	}

	/**
	 * 匹配当前Java的版本。
	 *
	 * @param versionPrefix Java版本前缀
	 * @return 如果版本匹配，则返回{@code true}
	 */
	private boolean getJavaVersionMatches(final String versionPrefix) {
		if (version == null) {
			return false;
		}

		return version.startsWith(versionPrefix);
	}

	/**
	 * 判定当前Java的版本是否大于等于指定的版本号，例如：
	 * <ul>
	 * 	<li>测试JDK 1.2：{@code isJavaVersionAtLeast(1.2f)}</li>
	 * 	<li>测试JDK 1.2.1：{@code isJavaVersionAtLeast(1.31f)}</li>
	 * </ul>
	 *
	 * @param requiredVersion 需要的版本
	 * @return 如果当前Java版本大于或等于指定的版本，则返回{@code true}
	 */
	public final boolean isJavaVersionAtLeast(final float requiredVersion) {
		return getVersionFloat() >= requiredVersion;
	}

	/**
	 * 判定当前Java的版本是否大于等于指定的版本号，例如：
	 * <ul>
	 * 	<li>测试JDK 1.2：{@code isJavaVersionAtLeast(120)}</li>
	 * 	<li>测试JDK 1.2.1：{@code isJavaVersionAtLeast(131)}</li>
	 * </ul>
	 *
	 * @param requiredVersion 需要的版本
	 * @return 如果当前Java版本大于或等于指定的版本，则返回{@code true}
	 */
	public final boolean isJavaVersionAtLeast(final int requiredVersion) {
		return getVersionInt() >= requiredVersion;
	}

	/**
	 * 将Java Implementation的信息转换成字符串。
	 *
	 * @return JVM impl.的字符串表示
	 */
	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		ManagementUtil.append(builder, "Java Version:    ", getVersion());
		ManagementUtil.append(builder, "Java Vendor:     ", getVendor());
		ManagementUtil.append(builder, "Java Vendor URL: ", getVendorURL());

		return builder.toString();
	}
}
