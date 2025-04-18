/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

/**
 *
 * @author choweli
 */
module hutool.log {
	exports cn.hutool.v7.log;
	exports cn.hutool.v7.log.level;
	exports cn.hutool.v7.log.engine.console;
	exports cn.hutool.v7.log.engine.log4j2;

	requires hutool.core;
	requires org.jboss.logging;
	requires org.apache.commons.logging;
	requires org.apache.logging.log4j;
	requires org.slf4j;
	requires tinylog;
	requires org.tinylog.api;
	requires java.logging;
	requires log4j;

}
