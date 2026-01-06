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

package cn.hutool.v7.http.server.engine.sun.filter;

import com.sun.net.httpserver.Filter;
import cn.hutool.v7.http.server.engine.sun.SunServerRequest;
import cn.hutool.v7.http.server.engine.sun.SunServerResponse;

/**
 * 异常处理过滤器
 *
 * @author Looly
 */
public abstract class ExceptionFilter implements HttpFilter {

	@Override
	public void doFilter(final SunServerRequest req, final SunServerResponse res, final Filter.Chain chain) {
		try {
			chain.doFilter(req.getExchange());
		} catch (final Throwable e) {
			afterException(req, res, e);
		}
	}

	/**
	 * 异常之后的处理逻辑
	 *
	 * @param req {@link SunServerRequest}
	 * @param res {@link SunServerResponse}
	 * @param e   异常
	 */
	public abstract void afterException(final SunServerRequest req, final SunServerResponse res, final Throwable e);
}
