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

package cn.hutool.v7.core.convert.impl;

import cn.hutool.v7.core.convert.AbstractConverter;
import cn.hutool.v7.core.convert.MatcherConverter;
import cn.hutool.v7.core.date.ZoneUtil;

import java.io.Serial;
import java.lang.reflect.Type;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * TimeZone转换器
 * @author Looly
 *
 */
public class TimeZoneConverter extends AbstractConverter implements MatcherConverter {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final TimeZoneConverter INSTANCE = new TimeZoneConverter();

	@Override
	public boolean match(final Type targetType, final Class<?> rawType, final Object value) {
		return TimeZone.class.isAssignableFrom(rawType);
	}

	@Override
	protected TimeZone convertInternal(final Class<?> targetClass, final Object value) {
		if(value instanceof ZoneId){
			return ZoneUtil.toTimeZone((ZoneId) value);
		}
		return TimeZone.getTimeZone(convertToStr(value));
	}
}
