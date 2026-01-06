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

package cn.hutool.v7.db;

import cn.hutool.v7.db.config.DbConfig;
import cn.hutool.v7.db.config.SettingConfigParser;
import cn.hutool.v7.setting.Setting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI9B98CTest {

	@Test
	void settingConfigParserTest() {
		final SettingConfigParser settingConfigParser = SettingConfigParser.of(new Setting("config/issueI9B98C.setting"));
		final DbConfig config = settingConfigParser.parse(null);
		Assertions.assertEquals("jdbc:sqlite:test.db", config.getUrl());
		Assertions.assertEquals("true", config.getConnProps().getProperty("remarks"));
	}
}
