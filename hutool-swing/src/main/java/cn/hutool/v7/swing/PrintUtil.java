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

import cn.hutool.v7.core.text.StrUtil;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * 打印机工具类
 *
 * @author Looly
 * @since 7.0.0
 */
public class PrintUtil {

	/**
	 * 获取所有可用的打印机
	 *
	 * @return 打印机数组
	 */
	public static PrintService[] getPrinters() {
		return PrinterJob.lookupPrintServices();
	}

	/**
	 * 获取默认打印机
	 *
	 * @return 默认打印机
	 */
	public static PrintService getDefaultPrinter() {
		return PrinterJob.getPrinterJob().getPrintService();
	}

	/**
	 * 根据名称获取打印机
	 *
	 * @param printerName 打印机名称
	 * @return 对应的打印机，未找到返回null
	 */
	public static PrintService getPrinter(final String printerName) {
		final PrintService[] printers = getPrinters();
		for (final PrintService printer : printers) {
			if (printer.getName().equals(printerName)) {
				return printer;
			}
		}
		return null;
	}

	/**
	 * 检查打印机是否可用
	 *
	 * @param printer 打印机
	 * @return 是否可用
	 */
	public static boolean isPrinterAvailable(final PrintService printer) {
		try {
			final PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintService(printer);
			return true;
		} catch (final PrinterException e) {
			return false;
		}
	}

	/**
	 * 打印内容(使用默认打印机)
	 *
	 * @param printable 可打印内容
	 */
	public static void print(final Printable printable) {
		print(printable, null, null);
	}

	/**
	 * 打印内容
	 *
	 * @param printable   可打印内容
	 * @param printerName 打印机名称(可选)
	 * @param attributes  打印属性(可选)
	 */
	public static void print(final Printable printable, final String printerName, PrintRequestAttributeSet attributes) {
		try {
			final PrinterJob job = PrinterJob.getPrinterJob();

			if (StrUtil.isNotEmpty(printerName)) {
				final PrintService printer = getPrinter(printerName);
				if (printer != null) {
					job.setPrintService(printer);
				}
			}

			if (attributes == null) {
				attributes = new HashPrintRequestAttributeSet();
				// 打印纸张大小
				attributes.add(MediaSizeName.ISO_A4);
				// 打印方向
				attributes.add(OrientationRequested.PORTRAIT);
				// 打印质量
				attributes.add(PrintQuality.NORMAL);
				// 打印颜色
				attributes.add(Chromaticity.COLOR);
				// 打印份数
				attributes.add(new Copies(1));
			}

			job.setPrintable(printable);
			job.print(attributes);
		} catch (final PrinterException e) {
			throw new SwingException(e);
		}
	}
}
