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

package cn.hutool.v7.core.comparator;

import cn.hutool.v7.core.collection.ListUtil;
import cn.hutool.v7.core.io.IoUtil;
import cn.hutool.v7.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class IssueI81N3HTest {

	@Test
	void sortTest() {
		final List<String> list = IoUtil.readUtf8Lines(ResourceUtil.getStream("IssueI81N3H.list"), new ArrayList<>());
		ListUtil.sort(list, VersionComparator.INSTANCE.reversed());
	}
}
