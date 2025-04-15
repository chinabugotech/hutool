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

package cn.hutool.v7.http.client.engine.httpclient4;

import org.apache.http.cookie.Cookie;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.lang.wrapper.SimpleWrapper;
import cn.hutool.v7.http.client.cookie.CookieSpi;

import java.time.Instant;

/**
 * HttpClient4 Cookie实现
 *
 * @author Looly
 * @since 6.0.0
 */
public class HttpClient4Cookie extends SimpleWrapper<Cookie> implements CookieSpi {

	/**
	 * 构造
	 *
	 * @param raw 原始对象
	 */
	public HttpClient4Cookie(final Cookie raw) {
		super(raw);
	}

	@Override
	public String getName() {
		return raw.getName();
	}

	@Override
	public String getValue() {
		return raw.getValue();
	}

	@Override
	public boolean isSecure() {
		return raw.isSecure();
	}

	@Override
	public boolean isHttpOnly() {
		return false;
	}

	@Override
	public boolean isHostOnly() {
		return false;
	}

	@Override
	public String getDomain() {
		return raw.getDomain();
	}

	@Override
	public String getPath() {
		return raw.getPath();
	}

	@Override
	public boolean isExpired(final Instant now) {
		return raw.isExpired(DateUtil.date(now));
	}

	@Override
	public String getAttribute(final String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPersistent() {
		return raw.isPersistent();
	}

	@Override
	public String toString() {
		return raw.toString();
	}
}
