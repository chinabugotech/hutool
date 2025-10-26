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

package cn.hutool.v7.log.engine.commons;

import cn.hutool.v7.log.engine.log4j.Log4jLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serial;

/**
 * Apache Commons Logging for Log4j
 *
 * @author Looly
 */
public class ApacheCommonsLog4JLog extends Log4jLog {
	@Serial
	private static final long serialVersionUID = -6843151523380063975L;

	// ------------------------------------------------------------------------- Constructor

	/**
	 * 构造
	 *
	 * @param logger Logger
	 */
	public ApacheCommonsLog4JLog(final Logger logger) {
		super(logger);
	}

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public ApacheCommonsLog4JLog(final Class<?> clazz) {
		super(LogManager.getLogger(clazz));
	}

	/**
	 * 构造
	 *
	 * @param name 名称
	 */
	public ApacheCommonsLog4JLog(final String name) {
		super(LogManager.getLogger(name));
	}
}
