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

package cn.hutool.v7.core.xml;

import cn.hutool.v7.core.io.file.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathConstants;
import java.io.File;

public class IssueIASB22Test {
	@Test
	@Disabled
	void getByPathTest() {
		final File xmlFile = FileUtil.file("D:/test/pom.xml");
		final Document docResult = XmlUtil.readXml(xmlFile);
		final Object value = XPathUtil.getByXPath("//project/artifactId", docResult, XPathConstants.STRING,
			new UniversalNamespaceCache(docResult, false));
		Assertions.assertTrue(value.toString().isEmpty());
	}
}
