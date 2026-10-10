/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.http.server.handler;

import cn.hutool.v7.http.meta.HttpStatus;

/**
 * 404状态处理器
 *
 * @author Looly
 */
public class NotFoundHandler implements HttpHandler {

	@Override
	public void handle(final ServerRequest request, final ServerResponse response) {
		response.setStatus(HttpStatus.HTTP_NOT_FOUND);
		response.write("404 Not Found !");
	}
}
