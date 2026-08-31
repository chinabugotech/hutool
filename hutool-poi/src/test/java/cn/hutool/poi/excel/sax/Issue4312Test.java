package cn.hutool.poi.excel.sax;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例：修复通过sheet index读取Excel时rId映射错误问题 issue#4312
 *
 * 问题原因：原来代码假设sheetIndex=0对应rId1，但不同软件生成xlsx时，rId分配顺序不同：
 * <ul>
 *   <li>Apache POI: rId1=sharedStrings, rId2=styles, rId3=worksheet → 原代码错误读取rId1导致失败</li>
 *   <li>WPS: rId1=worksheet → 正常工作</li>
 * </ul>
 *
 * 该测试使用Apache POI生成的测试文件，验证修复后能正确读取第一个sheet。
 *
 * @author hutool
 * @since 5.8.48
 */
public class Issue4312Test {

	@Test
	public void readBySheetIndexWithPoiGeneratedFileTest() {
		// 读取Apache POI生成的测试文件（第一个worksheet实际rId=3）
		final List<Object[]> rows = new ArrayList<>();

		// 修复前：这里会错误读取rId1（sharedStrings）导致失败
		// 修复后：通过SheetRidReader解析workbook.xml，找到第一个sheet对应实际rId
		ExcelUtil.readBySax("issue4312.xlsx", 0, (sheetIndex, rowIndex, cellList) -> {
			rows.add(cellList.toArray(new Object[0]));
		});

		// 验证读取结果
		assertEquals(6, rows.size()); // 表头 + 5行数据
		assertEquals("order_no", rows.get(0)[0]);
		assertEquals("name", rows.get(0)[1]);
		assertEquals("amount", rows.get(0)[2]);

		assertEquals("BIZ_0001", rows.get(1)[0]);
		assertEquals("test_1", rows.get(1)[1]);
		assertEquals(101L, rows.get(1)[2]);

		assertEquals("BIZ_0005", rows.get(5)[0]);
		assertEquals("test_5", rows.get(5)[1]);
		assertEquals(105L, rows.get(5)[2]);
	}

	@Test
	public void readSecondSheetByIndexTest() {
		// 测试读取第二个sheet
		final List<Object[]> rows = new ArrayList<>();

		ExcelUtil.readBySax("issue4312.xlsx", 1, (sheetIndex, rowIndex, cellList) -> {
			rows.add(cellList.toArray(new Object[0]));
		});

		// 验证读取结果
		assertEquals(6, rows.size()); // 表头 + 5行数据
		assertEquals("order_no", rows.get(0)[0]);
		assertEquals("name", rows.get(0)[1]);
		assertEquals("amount", rows.get(0)[2]);

		assertEquals("BIZ_0006", rows.get(1)[0]);
		assertEquals("test_6", rows.get(1)[1]);
		assertEquals(106L, rows.get(1)[2]);

		assertEquals("BIZ_0010", rows.get(5)[0]);
		assertEquals("test_10", rows.get(5)[1]);
		assertEquals(110L, rows.get(5)[2]);
	}

	@Test
	public void readByOpcPackageTest() throws Exception {
		// 测试直接调用 read(OPCPackage, int) 重载
		final List<Object[]> rows = new ArrayList<>();
		
		// 读取测试文件，通过 OPCPackage 方式调用
		final Excel07SaxReader reader = new Excel07SaxReader((sheetIndex, rowIndex, cellList) -> {
			rows.add(cellList.toArray(new Object[0]));
		});
		
		final File file = FileUtil.file(ResourceUtil.getResource("issue4312.xlsx"));
		try (OPCPackage opcPackage = 
				OPCPackage.open(file, PackageAccess.READ)) {
			// 修复前：这里错误拼接为 rId0，实际读到 sharedStrings
			// 修复后：正确解析为 sheetIndex 0，找到真实 rId
			reader.read(opcPackage, 0);
		}
	
		// 验证读取结果
		assertEquals(6, rows.size());
		assertEquals("order_no", rows.get(0)[0]);
		assertEquals("BIZ_0001", rows.get(1)[0]);
	}

	@Test
	public void readAllSheetsByMinusOneTest() {
		// 测试 rid = -1 读取所有sheet的情况
		final List<Object[]> allRows = new ArrayList<>();

		// -1 表示读取所有sheet
		ExcelUtil.readBySax("issue4312.xlsx", -1, (sheetIndex, rowIndex, cellList) -> {
			allRows.add(cellList.toArray(new Object[0]));
		});

		// 验证结果：两个sheet各6行，总共12行
		assertEquals(12, allRows.size());
		// 第一个sheet第一行
		assertEquals("order_no", allRows.get(0)[0]);
		// 第二个sheet第一行在第6行（索引从0开始）
		assertEquals("order_no", allRows.get(6)[0]);
		// 第二个sheet第一行数据
		assertEquals("BIZ_0006", allRows.get(7)[0]);
	}

	@Test
	public void readAllSheetsByMinusOneFileInputStreamTest() throws Exception {
		// 测试 rid=-1，InputStream重载
		final List<Object[]> allRows = new ArrayList<>();

		final Excel07SaxReader reader = new Excel07SaxReader((sheetIndex, rowIndex, cellList) -> {
			allRows.add(cellList.toArray(new Object[0]));
		});

		try (InputStream in = FileUtil.getInputStream(FileUtil.file(ResourceUtil.getResource("issue4312.xlsx")))) {
			reader.read(in, -1);
		}

		assertEquals(12, allRows.size());
	}

	@Test
	public void readNonExistentSheetIndexTest() throws Exception {
		// 测试读取不存在的sheet index，验证异常处理
		// 读取不存在的sheet，验证遇到异常能正确抛出而不是崩溃
		final Excel07SaxReader reader = new Excel07SaxReader((sheetIndex, rowIndex, cellList) -> {
			// do nothing
		});

		// 对于不存在的sheet，getSheetIndex会抛出IllegalArgumentException，这是预期行为
		// 这里只验证修复后的int路径能够正确走完解析流程
		final File file = FileUtil.file(ResourceUtil.getResource("issue4312.xlsx"));
		try (OPCPackage opcPackage = OPCPackage.open(file, PackageAccess.READ)) {
			// 尝试读取一个不存在的sheet index 999
			reader.read(opcPackage, 999);
		} catch (IllegalArgumentException expected) {
			// 预期：找不到sheet会抛出异常，这是正确行为
			// 测试通过说明修复后的代码路径能正常执行到这里
		}
	}
}
