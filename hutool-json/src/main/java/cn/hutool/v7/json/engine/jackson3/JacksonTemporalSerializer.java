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

package cn.hutool.v7.json.engine.jackson3;

import cn.hutool.v7.core.date.TimeUtil;
import cn.hutool.v7.core.text.StrUtil;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

import java.time.temporal.TemporalAccessor;

/**
 * Jackson3处理{@link TemporalAccessor}相关对象序列化描述
 *
 * @author Looly
 * @since 7.0.0
 */
public class JacksonTemporalSerializer extends StdSerializer<TemporalAccessor> {

	/**
	 * 日期格式
	 */
	private final String format;

	/**
	 * 构造
	 *
	 * @param format 日期格式，null表示使用时间戳
	 */
	public JacksonTemporalSerializer(final String format) {
		super(TemporalAccessor.class);
		this.format = format;
	}

	@Override
	public void serialize(final TemporalAccessor value, final JsonGenerator gen, final SerializationContext context) {
		if (StrUtil.isEmpty(this.format)) {
			final long epochMilli = TimeUtil.toEpochMilli(value);
			gen.writeNumber(epochMilli);
		} else {
			gen.writeString(TimeUtil.format(value, this.format));
		}
	}
}
