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

package cn.hutool.v7.swing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class PrintAttributeBuilderTest {
	private PrintService testPrinter;
	private Set<Class<? extends Attribute>> supportedAttributes;

	@BeforeEach
	@EnabledOnOs(OS.WINDOWS)
	void setUp() {
		supportedAttributes = new HashSet<>();
		testPrinter = PrintUtil.getDefaultPrinter();
	}

	@Test
	void testConstructorWithNullPrinter() {
		assertThrows(NullPointerException.class, () -> new PrintAttributeBuilder(null));
	}

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testCopies() {
		// 测试份数设置
		final PrintAttributeBuilder builder = new PrintAttributeBuilder(testPrinter);
		builder.copies(3);
		final PrintRequestAttributeSet attributes = builder.build();

		final Copies copies = (Copies) attributes.get(Copies.class);
		assertNotNull(copies);
		assertEquals(3, copies.getValue());
	}

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testCopiesWithInvalidValue() {
		// 测试无效份数（小于1）
		final PrintAttributeBuilder builder = new PrintAttributeBuilder(testPrinter);
		builder.copies(0);
		final PrintRequestAttributeSet attributes = builder.build();

		final Copies copies = (Copies) attributes.get(Copies.class);
		assertNotNull(copies);
		assertEquals(1, copies.getValue()); // 应该自动修正为1
	}

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testJobName() {
		final PrintAttributeBuilder builder = new PrintAttributeBuilder(testPrinter);
		builder.jobName("Test Job");
		final PrintRequestAttributeSet attributes = builder.build();

		final JobName jobName = (JobName) attributes.get(JobName.class);
		assertNotNull(jobName);
		assertEquals("Test Job", jobName.getValue());
	}

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testA4PaperWhenNotSupported() {
		// 确保不支持MediaSizeName
		supportedAttributes.clear();

		final PrintAttributeBuilder builder = new PrintAttributeBuilder(testPrinter);
		builder.a4Paper();
		final PrintRequestAttributeSet attributes = builder.build();

		assertNull(attributes.get(MediaSizeName.class));
	}

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testLandscapeWhenSupported() {
		supportedAttributes.add(OrientationRequested.class);

		final PrintAttributeBuilder builder = new PrintAttributeBuilder(testPrinter);
		builder.landscape();
		final PrintRequestAttributeSet attributes = builder.build();

		assertEquals(OrientationRequested.LANDSCAPE, attributes.get(OrientationRequested.class));
	}

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testMonochromeWhenSupported() {
		supportedAttributes.add(Chromaticity.class);

		final PrintAttributeBuilder builder = new PrintAttributeBuilder(testPrinter);
		builder.monochrome();
		final PrintRequestAttributeSet attributes = builder.build();

		assertEquals(Chromaticity.MONOCHROME, attributes.get(Chromaticity.class));
	}

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testPageRange() {
		final PrintAttributeBuilder builder = new PrintAttributeBuilder(testPrinter);
		builder.pageRange(3, 5);
		final PrintRequestAttributeSet attributes = builder.build();

		final PageRanges pageRanges = (PageRanges) attributes.get(PageRanges.class);
		assertNotNull(pageRanges);
		assertArrayEquals(new int[][]{{3, 5}}, pageRanges.getMembers());
	}

	@Test
	@EnabledOnOs(OS.WINDOWS)
	void testBuildReturnsClone() {
		final PrintAttributeBuilder builder = new PrintAttributeBuilder(testPrinter);
		builder.copies(2);
		final PrintRequestAttributeSet attributes1 = builder.build();
		final PrintRequestAttributeSet attributes2 = builder.build();

		assertNotSame(attributes1, attributes2);
		assertEquals(attributes1, attributes2);
	}

	@Test
	@Disabled
	@EnabledOnOs(OS.WINDOWS)
	void testColorWhenSupported() {
		supportedAttributes.add(Chromaticity.class);

		final PrintAttributeBuilder builder = new PrintAttributeBuilder(testPrinter);
		builder.color();
		final PrintRequestAttributeSet attributes = builder.build();

		assertEquals(Chromaticity.COLOR, attributes.get(Chromaticity.class));
	}
}
