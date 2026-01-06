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

package cn.hutool.v7.log.engine.console;

import cn.hutool.v7.log.AbsLogEngine;
import cn.hutool.v7.log.Log;

/**
 * 利用System.out.println()打印日志
 * @author Looly
 *
 */
public class ConsoleLogEngine extends AbsLogEngine {

	/**
	 * 构造
	 */
	public ConsoleLogEngine() {
		super("Hutool Console Logging");
	}

	@Override
	public Log createLog(final String name) {
		return new ConsoleLog(name);
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		return new ConsoleLog(clazz);
	}

}
