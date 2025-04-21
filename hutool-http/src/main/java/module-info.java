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
module hutool.http {
	exports cn.hutool.v7.http.meta;
	exports cn.hutool.v7.http.client;
	exports cn.hutool.v7.http;

	requires jdk.httpserver;
	requires hutool.log;
	requires org.apache.httpcomponents.httpclient;
	requires org.apache.httpcomponents.httpcore;
	requires hutool.core;
	requires org.apache.httpcomponents.core5.httpcore5;
	requires org.apache.httpcomponents.client5.httpclient5;
	requires okhttp3;
	requires org.eclipse.jetty.server;
	requires smart.http.server;
	requires undertow.core;
	requires jakarta.xml.soap;
	requires okio;
	requires smart.http.common;
	requires org.apache.tomcat.embed.core;

	requires aio.pro;

	opens cn.hutool.v7.http.client.engine;
	opens cn.hutool.v7.http.client.engine.httpclient5;
	opens cn.hutool.v7.http.client.engine.httpclient4;
	opens cn.hutool.v7.http.client.engine.okhttp;
	opens cn.hutool.v7.http.client.engine.jdk;

}
