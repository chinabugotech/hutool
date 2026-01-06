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

package cn.hutool.v7.core.net;

import cn.hutool.v7.core.net.url.UrlUtil;
import cn.hutool.v7.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * URLUtil单元测试
 *
 * @author Looly
 *
 */
public class URLUtilTest {

	@Test
	public void normalizeTest() {
		// issue#I25MZL，多个/被允许
		String url = "http://www.hutool.cn//aaa/bbb";
		String normalize = UrlUtil.normalize(url);
		assertEquals("http://www.hutool.cn//aaa/bbb", normalize);

		url = "www.hutool.cn//aaa/bbb";
		normalize = UrlUtil.normalize(url);
		assertEquals("http://www.hutool.cn//aaa/bbb", normalize);
	}

	@Test
	public void normalizeTest2() {
		String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
		String normalize = UrlUtil.normalize(url);
		assertEquals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);

		url = "www.hutool.cn//aaa/bbb?a=1&b=2";
		normalize = UrlUtil.normalize(url);
		assertEquals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);
	}

	@Test
	public void normalizeTest3() {
		String url = "http://www.hutool.cn//aaa/\\bbb?a=1&b=2";
		String normalize = UrlUtil.normalize(url, true);
		assertEquals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);

		url = "www.hutool.cn//aaa/bbb?a=1&b=2";
		normalize = UrlUtil.normalize(url, true);
		assertEquals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);

		url = "\\/www.hutool.cn//aaa/bbb?a=1&b=2";
		normalize = UrlUtil.normalize(url, true);
		assertEquals("http://www.hutool.cn//aaa/bbb?a=1&b=2", normalize);
	}

	@Test
	public void normalizeIpv6Test() {
		final String url = "http://[fe80::8f8:2022:a603:d180]:9439";
		final String normalize = UrlUtil.normalize("http://[fe80::8f8:2022:a603:d180]:9439", true);
		assertEquals(url, normalize);
	}

	@Test
	public void formatTest() {
		final String url = "//www.hutool.cn//aaa/\\bbb?a=1&b=2";
		final String normalize = UrlUtil.normalize(url);
		assertEquals("http://www.hutool.cn//aaa//bbb?a=1&b=2", normalize);
	}

	@Test
	public void getHostTest() throws MalformedURLException {
		final String url = "https://www.hutool.cn//aaa/\\bbb?a=1&b=2";
		final String normalize = UrlUtil.normalize(url);
		final URI host = UrlUtil.getHost(new URL(normalize));
		assertEquals("https://www.hutool.cn", host.toString());
	}

	@Test
	public void getPathTest(){
		final String url = " http://www.aaa.bbb/search?scope=ccc&q=ddd";
		final String path = UrlUtil.getPath(url);
		assertEquals("/search", path);
	}

	@Test
	public void issue3676Test() {
		final String fileFullName = "/Uploads/20240601/aaaa.txt";
		final URI uri = UrlUtil.toURI(fileFullName);
		final URI resolve = uri.resolve(".");
		assertEquals("/Uploads/20240601/", resolve.toString());
	}

	@Test
	public void urlWithFormTest() {
		final Map<String, Object> param = new LinkedHashMap<>();
		param.put("AccessKeyId", "123");
		param.put("Action", "DescribeDomainRecords");
		param.put("Format", "date");
		param.put("DomainName", "lesper.cn"); // 域名地址
		param.put("SignatureMethod", "POST");
		param.put("SignatureNonce", "123");
		param.put("SignatureVersion", "4.3.1");
		param.put("Timestamp", 123432453);
		param.put("Version", "1.0");

		String urlWithForm = UrlUtil.urlWithForm("http://api.hutool.cn/login?type=aaa", param, CharsetUtil.UTF_8, false);
		Assertions.assertEquals(
			"http://api.hutool.cn/login?type=aaa&AccessKeyId=123&Action=DescribeDomainRecords&Format=date&DomainName=lesper.cn&SignatureMethod=POST&SignatureNonce=123&SignatureVersion=4.3.1&Timestamp=123432453&Version=1.0",
			urlWithForm);

		urlWithForm = UrlUtil.urlWithForm("http://api.hutool.cn/login?type=aaa", param, CharsetUtil.UTF_8, false);
		Assertions.assertEquals(
			"http://api.hutool.cn/login?type=aaa&AccessKeyId=123&Action=DescribeDomainRecords&Format=date&DomainName=lesper.cn&SignatureMethod=POST&SignatureNonce=123&SignatureVersion=4.3.1&Timestamp=123432453&Version=1.0",
			urlWithForm);
	}
}
