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

package cn.hutool.v7.http.client.body;

import cn.hutool.v7.core.io.resource.HttpResource;
import cn.hutool.v7.core.io.resource.StringResource;
import cn.hutool.v7.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MultipartBodyTest {

	@Test
	public void buildTest(){
		final Map<String, Object> form = new HashMap<>();
		form.put("pic1", "pic1 content");
		form.put("pic2", new HttpResource(
				new StringResource("pic2 content"), "text/plain"));
		form.put("pic3", new HttpResource(
				new StringResource("pic3 content", "pic3.jpg"), "image/jpeg"));

		final MultipartBody body = MultipartBody.of(form, CharsetUtil.UTF_8);

		Assertions.assertNotNull(body.toString());
//		Console.log(body);
	}
}
