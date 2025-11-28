/*
 * Copyright (c) 2013-2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.poi.excel.sax;

import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.poi.POIException;

import java.io.File;
import java.io.InputStream;

/**
 * Sax方式读取Excel接口，提供一些共用方法
 *
 * @param <T> 子对象类型，用于标记返回值this
 * @author Looly
 * @since 3.2.0
 */
public interface ExcelSaxReader<T> {

	/**
	 * sheet r:Id前缀
	 */
	String RID_PREFIX = "rId";
	/**
	 * sheet name前缀
	 */
	String SHEET_NAME_PREFIX = "sheetName:";

	/**
	 * 开始从文件中读取Excel
	 *
	 * @param file               Excel文件
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名称，规则如下：
	 *                           <ul>
	 *                             <li>如果为-1，处理所有编号的sheet</li>
	 *                             <li>如果为rId开头，例如rId1，表示读取指定编号的sheet，从1计数，即rId1表示第一个sheet</li>
	 *                             <li>如果为sheet名称，例如sheet1，直接读取名车给对应sheet</li>
	 *                             <li>如果为纯数字，在03中表示index，从0开始，07中表示sheet id，从1开始</li>
	 *                           </ul>
	 * @return this
	 * @throws POIException POI异常
	 */
	T read(File file, String idOrRidOrSheetName) throws POIException;

	/**
	 * 开始从流中读取Excel，读取结束后并不关闭流
	 *
	 * @param in                 Excel流
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名称，规则如下：
	 *                           <ul>
	 *                             <li>如果为-1，处理所有编号的sheet</li>
	 *                             <li>如果为rId开头，例如rId1，表示读取指定编号的sheet，从1计数，即rId1表示第一个sheet</li>
	 *                             <li>如果为sheet名称，例如sheet1，直接读取名车给对应sheet</li>
	 *                             <li>如果为纯数字，在03中表示index，从0开始，07中表示sheet id，从1开始</li>
	 *                           </ul>
	 * @return this
	 * @throws POIException POI异常
	 */
	T read(InputStream in, String idOrRidOrSheetName) throws POIException;

	/**
	 * 开始从路径中读取Excel，读取所有sheet
	 *
	 * @param path Excel文件路径，如果是相对路径，则相对classpath
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final String path) throws POIException {
		return read(FileUtil.file(path));
	}

	/**
	 * 开始从文件中读取Excel，读取所有sheet
	 *
	 * @param file Excel文件
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final File file) throws POIException {
		return read(file, -1);
	}

	/**
	 * 开始从流中读取Excel，读取所有sheet，读取结束后并不关闭流
	 *
	 * @param in Excel包流
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final InputStream in) throws POIException {
		return read(in, -1);
	}

	/**
	 * 开始从路径中读取Excel
	 *
	 * @param path    文件路径，如果是相对路径，则相对classpath
	 * @param idOrRid Excel中的sheet id或者rid编号，rid必须加rId前缀，例如rId1，如果为-1处理所有编号的sheet
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final String path, final int idOrRid) throws POIException {
		return read(FileUtil.file(path), idOrRid);
	}

	/**
	 * 开始读取Excel
	 *
	 * @param path               文件路径
	 * @param idOrRidOrSheetName Excel中的sheet id或者rid编号或sheet名称，规则如下：
	 *                           <ul>
	 *                             <li>如果为-1，处理所有编号的sheet</li>
	 *                             <li>如果为rId开头，例如rId1，表示读取指定编号的sheet，从1计数，即rId1表示第一个sheet</li>
	 *                             <li>如果为sheet名称，例如sheet1，直接读取名车给对应sheet</li>
	 *                             <li>如果为纯数字，在03中表示index，从0开始，07中表示sheet id，从1开始</li>
	 *                           </ul>
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final String path, final String idOrRidOrSheetName) throws POIException {
		return read(FileUtil.file(path), idOrRidOrSheetName);
	}

	/**
	 * 开始从文件中读取Excel
	 *
	 * @param file    Excel文件
	 * @param idOrRid Excel中的sheet id或者rid编号，规则如下：
	 *                           <ul>
	 *                             <li>如果为-1，处理所有编号的sheet</li>
	 *                             <li>如果为纯数字，在03中表示index，从0开始，07中表示sheet id，从1开始</li>
	 *                           </ul>
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final File file, final int idOrRid) throws POIException {
		return read(file, String.valueOf(idOrRid));
	}

	/**
	 * 开始从流中读取Excel，读取结束后并不关闭流
	 *
	 * @param in      Excel流
	 * @param idOrRid Excel中的sheet id或者rid编号，规则如下：
	 *                           <ul>
	 *                             <li>如果为-1，处理所有编号的sheet</li>
	 *                             <li>如果为纯数字，在03中表示index，从0开始，07中表示sheet id，从1开始</li>
	 *                           </ul>
	 * @return this
	 * @throws POIException POI异常
	 */
	default T read(final InputStream in, final int idOrRid) throws POIException {
		return read(in, String.valueOf(idOrRid));
	}
}
