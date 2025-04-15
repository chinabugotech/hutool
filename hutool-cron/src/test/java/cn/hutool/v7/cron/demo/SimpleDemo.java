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

package cn.hutool.v7.cron.demo;

import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.cron.CronUtil;

public class SimpleDemo {
	public static void main(String[] args) {
		// 打开秒匹配
		CronUtil.setMatchSecond(true);
		// 添加任务
		CronUtil.schedule("*/2 * * * * *",
			() -> Console.log("Hutool task running!"));
		// 启动
		CronUtil.start();
	}
}
