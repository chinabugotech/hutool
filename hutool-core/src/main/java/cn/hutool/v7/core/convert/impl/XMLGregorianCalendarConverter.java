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
import cn.hutool.v7.core.convert.ConvertException;
import cn.hutool.v7.core.date.DateException;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.text.StrUtil;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serial;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期转换器
 *
 * @author Looly
 */
public class XMLGregorianCalendarConverter extends AbstractConverter {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 日期格式化
	 */
	private String format;
	/**
	 * {@link DatatypeFactory}
	 */
	private final DatatypeFactory datatypeFactory;

	/**
	 * 构造
	 */
	public XMLGregorianCalendarConverter() {
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (final DatatypeConfigurationException e) {
			throw new DateException(e);
		}
	}

	/**
	 * 获取日期格式
	 *
	 * @return 设置日期格式
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 设置日期格式
	 *
	 * @param format 日期格式
	 */
	public void setFormat(final String format) {
		this.format = format;
	}

	@Override
	protected XMLGregorianCalendar convertInternal(final Class<?> targetClass, final Object value) {
		if (value instanceof GregorianCalendar) {
			return datatypeFactory.newXMLGregorianCalendar((GregorianCalendar) value);
		}

		final GregorianCalendar gregorianCalendar = new GregorianCalendar();
		// Handle Date
		if (value instanceof Date) {
			gregorianCalendar.setTime((Date) value);
		} else if (value instanceof final Calendar calendar) {
			gregorianCalendar.setTimeZone(calendar.getTimeZone());
			gregorianCalendar.setFirstDayOfWeek(calendar.getFirstDayOfWeek());
			gregorianCalendar.setLenient(calendar.isLenient());
			gregorianCalendar.setTimeInMillis(calendar.getTimeInMillis());
		} else if (value instanceof Long) {
			gregorianCalendar.setTimeInMillis((Long) value);
		} else {
			final String valueStr = convertToStr(value);
			final Date date = StrUtil.isBlank(format) ? DateUtil.parse(valueStr) : DateUtil.parse(valueStr, format);
			if (null == date) {
				throw new ConvertException("Unsupported date value: " + value);
			}
			gregorianCalendar.setTime(date);
		}
		return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
	}

}
