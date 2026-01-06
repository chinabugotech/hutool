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

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.lang.builder.Builder;
import cn.hutool.v7.core.util.ObjUtil;

import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能打印配置器：根据打印机能力动态构建 PrintRequestAttributeSet
 * <p>
 * 使用示例：
 * <pre><@code
 * PrintService printer = findPrinter("MyPrinter");
 * PrintRequestAttributeSet attrs = new SmartPrintConfigurator(printer)
 *     .copies(2)
 *     .a4Paper()
 *     .landscape()
 *     .duplex()
 *     .monochrome()
 *     .collated()
 *     .highQuality()
 *     .build();
 * }</pre>
 */
public class PrintAttributeBuilder implements Builder<PrintRequestAttributeSet> {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 创建打印属性构建器
	 *
	 * @param printer 打印服务对象
	 * @return 打印属性构建器实例
	 */
	public static PrintAttributeBuilder of(final PrintService printer) {
		return new PrintAttributeBuilder(printer);
	}

	private final PrintService printer;
	private final HashPrintRequestAttributeSet attributes;
	private final Set<Class<? extends Attribute>> supportedAttrs;

	// 缓存支持的属性值（提升性能）
	private final Map<Class<? extends Attribute>, Object> supportedValuesCache = new HashMap<>();

	/**
	 * 构造
	 *
	 * @param printer 打印机对象，不能为空
	 */
	@SuppressWarnings("unchecked")
	public PrintAttributeBuilder(final PrintService printer) {
		this.printer = Objects.requireNonNull(printer, "printer 不能为空");
		this.attributes = new HashPrintRequestAttributeSet();
		this.supportedAttrs = Arrays.stream(printer.getSupportedAttributeCategories())
			.filter(Objects::nonNull)
			.filter(Attribute.class::isAssignableFrom)
			.map(c -> (Class<? extends Attribute>) c) // 安全转型
			.collect(Collectors.toUnmodifiableSet());
	}

	// ------------------ 配置方法（链式调用） ------------------

	/**
	 * 设置份数（Copies）— 所有打印机均支持
	 *
	 * @param n 份数，至少为 1
	 * @return this，用于链式调用
	 */
	public PrintAttributeBuilder copies(final int n) {
		attributes.add(new Copies(Math.max(1, n)));
		return this;
	}

	/**
	 * 设置作业名
	 *
	 * @param name 作业名，可为空字符串但不应为null
	 * @return this，用于链式调用
	 */
	public PrintAttributeBuilder jobName(final String name) {
		attributes.add(new JobName(name, null));
		return this;
	}

	/**
	 * 强制 A4纸（210 mm × 297 mm）
	 *
	 * @return this，用于链式调用
	 */
	public PrintAttributeBuilder a4Paper() {
		return mediaSize(MediaSizeName.ISO_A4);
	}

	/**
	 * 强制 A3纸（297 mm × 420 mm）（若不支持则尝试 Letter 或忽略）
	 *
	 * @return this，用于链式调用
	 */
	public PrintAttributeBuilder a3Paper() {
		return mediaSize(MediaSizeName.ISO_A3);
	}

	/**
	 * 设置纸张类型
	 * <pre>{@code
	 *  A0: 841 mm × 1189 mm
	 *  A1: 594 mm × 841 mm
	 *  A2: 420 mm × 594 mm
	 *  A3: 297 mm × 420 mm
	 *  A4: 210 mm × 297 mm
	 *  A5: 148 mm × 210 mm
	 *  A6: 105 mm × 148 mm
	 *  A7: 74 mm × 105 mm
	 *  A8: 52 mm × 74 mm
	 *  A9: 37 mm × 52 mm
	 *  A10: 26 mm × 37 mm
	 * }</pre>
	 *
	 * @param mediaSizeName {@link MediaSizeName}
	 * @return this，用于链式调用
	 */
	public PrintAttributeBuilder mediaSize(final MediaSizeName mediaSizeName) {
		if (support(MediaSizeName.class)) {
			final MediaSizeName[] sizes = getSupportedValues(MediaSizeName.class);
			if (ArrayUtil.contains(sizes, mediaSizeName)) {
				attributes.add(mediaSizeName);
			}
		}
		return this;
	}

	/**
	 * 设置横向打印方向
	 * <p>
	 * 如果打印机不支持横向打印，将输出警告信息并保持默认的纵向打印方向
	 * </p>
	 *
	 * @return 当前PrintAttributeBuilder实例，支持链式调用
	 */
	public PrintAttributeBuilder landscape() {
		if (support(OrientationRequested.class)) {
			addAttribute(OrientationRequested.LANDSCAPE);
		}
		return this;
	}

	/**
	 * 设置纵向打印模式（默认，通常无需显式调用）
	 * <p>
	 * 将打印方向设置为纵向模式，这是大多数打印任务的默认方向。
	 * 如果打印机支持该方向，则会在打印属性中添加 PORTRAIT 方向设置。
	 * </p>
	 *
	 * @return 当前 PrintAttributeBuilder 实例，支持链式调用
	 */
	public PrintAttributeBuilder portrait() {
		if (support(OrientationRequested.class)) {
			addAttribute(OrientationRequested.PORTRAIT);
		}
		return this;
	}

	/**
	 * 双面打印（若支持则启用，否则自动降级为单面）
	 *
	 * @return this
	 */
	public PrintAttributeBuilder duplex() {
		if (support(Sides.class)) {
			final Sides[] sides = getSupportedValues(Sides.class);
			if (ArrayUtil.contains(sides, Sides.DUPLEX)) {
				attributes.add(Sides.DUPLEX);
			} else if (ArrayUtil.contains(sides, Sides.TUMBLE)) {
				attributes.add(Sides.TUMBLE);
			} else {
				attributes.add(Sides.ONE_SIDED);
			}
		} else {
			attributes.add(Sides.ONE_SIDED);
		}
		return this;
	}

	/**
	 * 强制单面（覆盖可能的默认双面）
	 *
	 * @return this，用于链式调用
	 */
	public PrintAttributeBuilder oneSided() {
		return addAttribute(Sides.ONE_SIDED);
	}

	/**
	 * 黑白打印（若支持彩色/黑白切换；否则保留默认）
	 *
	 * @return this，用于链式调用。
	 */
	public PrintAttributeBuilder monochrome() {
		if (support(Chromaticity.class)) {
			doAddAttribute(Chromaticity.MONOCHROME);
		}
		return this;
	}

	/**
	 * 彩色打印（若支持）
	 *
	 * @return this，用于链式调用。
	 */
	public PrintAttributeBuilder color() {
		if (support(Chromaticity.class)) {
			doAddAttribute(Chromaticity.COLOR);
		}
		return this;
	}

	/**
	 * 分套打印（collate）
	 *
	 * @return this，用于链式调用。
	 */
	public PrintAttributeBuilder collated() {
		if (support(SheetCollate.class)) {
			doAddAttribute(SheetCollate.COLLATED);
		}
		return this;
	}

	/**
	 * 不分套
	 *
	 * @return this，用于链式调用。
	 */
	public PrintAttributeBuilder uncollated() {
		if (support(SheetCollate.class)) {
			doAddAttribute(SheetCollate.UNCOLLATED);
		}
		return this;
	}

	/**
	 * 高质量打印（若支持）
	 *
	 * @return this，用于链式调用。
	 */
	public PrintAttributeBuilder highQuality() {
		if (support(PrintQuality.class)) {
			doAddAttribute(PrintQuality.HIGH);
		}
		return this;
	}

	/**
	 * 草稿模式（省墨）
	 *
	 * @return this，用于链式调用。
	 */
	public PrintAttributeBuilder draftMode() {
		if (support(PrintQuality.class)) {
			doAddAttribute(PrintQuality.DRAFT);
		}
		return this;
	}

	/**
	 * 指定进纸托盘为手动进纸
	 *
	 * @return this，用于链式调用。
	 */
	public PrintAttributeBuilder manualTray() {
		if (support(MediaTray.class)) {
			doAddAttribute(MediaTray.MANUAL);
		}
		return this;
	}

	/**
	 * 设置页码范围
	 *
	 * @param from 开始页码（包含）
	 * @param to   结束页码（包含）
	 * @return this，用于链式调用。
	 */
	public PrintAttributeBuilder pageRange(final int from, final int to) {
		// PageRanges 通常都支持
		attributes.add(new PageRanges(Math.max(1, from), Math.max(from, to)));
		return this;
	}

	/**
	 * 添加打印属性
	 *
	 * @param attr 要添加的打印属性对象
	 * @return this，用于链式调用
	 */
	public PrintAttributeBuilder addAttribute(final Attribute attr) {
		doAddAttribute(attr);
		return this;
	}

	/**
	 * 构建最终的属性集
	 */
	public PrintRequestAttributeSet build() {
		return ObjUtil.clone(this.attributes);
	}

	/**
	 * 打印当前配置摘要（调试用）
	 */
	public void printSummary() {
		Console.log("🖨️ 打印配置摘要 [打印机: " + printer.getName() + "]");
		for (final Attribute attr : attributes.toArray()) {
			Console.log("  • " + attr.getCategory().getSimpleName() + " = " + attr);
		}
	}

	@Override
	public String toString() {
		return "PrintAttributeBuilder{" +
			"printer=" + printer.getName() +
			", attributes=" + Arrays.toString(attributes.toArray()) +
			'}';
	}

	/**
	 * 快速获取支持的能力摘要
	 */
	public void printCapabilities() {
		Console.log("🔍 打印机能力检测: " + printer.getName());
		for (final Class<? extends Attribute> c : this.supportedAttrs) {
			final Object vals = printer.getSupportedAttributeValues(c, null, null);
			if (vals instanceof final Attribute[] arr) {
				Console.log("  ✅ " + c.getSimpleName() + ": " + Arrays.toString(arr));
			} else {
				Console.log("  ✅ " + c.getSimpleName() + " (值类型: " + (vals == null ? "null" : vals.getClass().getSimpleName()) + ")");
			}
		}
	}

	/**
	 * 添加打印属性
	 *
	 * @param attr 要添加的打印属性对象
	 * @return {@code true}表示支持属性，添加成功，否则返回{@code false}
	 */
	private boolean doAddAttribute(final Attribute attr) {
		if (supportsValue(attr)) {
			attributes.add(attr);
			return true;
		}
		return false;
	}

	private boolean support(final Class<? extends Attribute> attrClass) {
		return supportedAttrs.contains(attrClass);
	}

	/**
	 * 判断是否支持某个属性
	 *
	 * @param attrClass 属性类
	 * @param <T>       属性类
	 * @return 是否支持
	 */
	@SuppressWarnings("unchecked")
	private <T extends Attribute> T[] getSupportedValues(final Class<T> attrClass) {
		return (T[]) supportedValuesCache.computeIfAbsent(attrClass, k ->
			printer.getSupportedAttributeValues(k, null, null));
	}

	/**
	 * 判断是否支持某个属性值
	 *
	 * @param value 属性值
	 * @return 是否支持
	 */
	private boolean supportsValue(final Attribute value) {
		final Object vals = printer.getSupportedAttributeValues(
			value.getCategory(), null, null);
		if (vals instanceof final Attribute[] arr) {
			return ArrayUtil.contains(arr, value);
		}
		return false;
	}
}
