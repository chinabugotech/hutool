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

package cn.hutool.v7.http.client.engine.httpclient4;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import cn.hutool.v7.core.lang.wrapper.SimpleWrapper;
import cn.hutool.v7.http.HttpException;
import cn.hutool.v7.http.client.cookie.CookieSpi;
import cn.hutool.v7.http.client.cookie.CookieStoreSpi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Apache HttpClient4的Cookie存储器实现
 *
 * @author Looly
 * @since 6.0.0
 */
public class HttpClient4CookieStore extends SimpleWrapper<CookieStoreSpi> implements CookieStore {

	/**
	 * 构造
	 *
	 * @param raw Cookie存储器
	 */
	public HttpClient4CookieStore(final CookieStoreSpi raw) {
		super(raw);
	}

	@Override
	public void addCookie(final Cookie cookie) {
		final URI uri;
		try {
			uri = new URI("http", cookie.getDomain(), cookie.getPath(), null);
		} catch (final URISyntaxException e) {
			throw new HttpException(e);
		}

		this.raw.add(uri, new HttpClient4Cookie(cookie));
	}

	@Override
	public List<Cookie> getCookies() {
		final List<CookieSpi> cookies = this.raw.getCookies();
		final List<Cookie> result = new ArrayList<>(cookies.size());
		for (final CookieSpi cookie : cookies) {
			result.add(((HttpClient4Cookie) cookie).getRaw());
		}
		return result;
	}

	@Override
	public boolean clearExpired(final Date date) {
		// get时检查过期
		this.raw.getCookies();
		return true;
	}

	@Override
	public void clear() {
		this.raw.clear();
	}
}
