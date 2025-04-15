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

package cn.hutool.v7.log.engine.jdk;

import java.io.InputStream;
import java.util.logging.LogManager;

import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.io.resource.ResourceUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.log.AbsLogEngine;
import cn.hutool.v7.log.Log;

/**
 * JDK日志工厂类
 * <a href="http://java.sun.com/javase/6/docs/technotes/guides/logging/index.html">java.util.logging</a> log.
 *
 * @author Looly
 */
public class JdkLogEngine extends AbsLogEngine {

	/**
	 * 构造
	 */
	public JdkLogEngine() {
		super("JDK Logging");
		readConfig();
	}

	@Override
	public Log createLog(final String name) {
		return new JdkLog(name);
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		return new JdkLog(clazz);
	}

	/**
	 * 读取ClassPath下的logging.properties配置文件
	 */
	private void readConfig() {
		//避免循环引用，Log初始化的时候不使用相关工具类
		final InputStream in = ResourceUtil.getStreamSafe("logging.properties");
		if (null == in) {
			System.err.println("[WARN] Can not find [logging.properties], use [%JRE_HOME%/lib/logging.properties] as default!");
			return;
		}

		try {
			LogManager.getLogManager().readConfiguration(in);
		} catch (final Exception e) {
			Console.error(e, "Read [logging.properties] from classpath error!");
			try {
				LogManager.getLogManager().readConfiguration();
			} catch (final Exception e1) {
				Console.error(e, "Read [logging.properties] from [%JRE_HOME%/lib/logging.properties] error!");
			}
		} finally {
			IoUtil.closeQuietly(in);
		}
	}
}
