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

package cn.hutool.v7.extra.management;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.management.MBeanServer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.lang.management.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ManagementUtilTest {

	// ----- 测试常量字段 -----
	@Test
	public void testConstantsNotNull() {
		assertNotNull(ManagementUtil.SPECIFICATION_NAME);
		assertNotNull(ManagementUtil.VERSION);
		assertNotNull(ManagementUtil.SPECIFICATION_VERSION);
		assertNotNull(ManagementUtil.VENDOR);
		assertNotNull(ManagementUtil.SPECIFICATION_VENDOR);
		assertNotNull(ManagementUtil.VENDOR_URL);
		assertNotNull(ManagementUtil.HOME);
		assertNotNull(ManagementUtil.LIBRARY_PATH);
		assertNotNull(ManagementUtil.TMPDIR);
		assertNotNull(ManagementUtil.COMPILER);
		assertNotNull(ManagementUtil.EXT_DIRS);
		assertNotNull(ManagementUtil.VM_NAME);
		assertNotNull(ManagementUtil.VM_SPECIFICATION_NAME);
		assertNotNull(ManagementUtil.VM_VERSION);
		assertNotNull(ManagementUtil.VM_SPECIFICATION_VERSION);
		assertNotNull(ManagementUtil.VM_VENDOR);
		assertNotNull(ManagementUtil.VM_SPECIFICATION_VENDOR);
		assertNotNull(ManagementUtil.CLASS_VERSION);
		assertNotNull(ManagementUtil.CLASS_PATH);
		assertNotNull(ManagementUtil.OS_NAME);
		assertNotNull(ManagementUtil.OS_ARCH);
		assertNotNull(ManagementUtil.OS_VERSION);
		assertNotNull(ManagementUtil.FILE_SEPARATOR);
		assertNotNull(ManagementUtil.PATH_SEPARATOR);
		assertNotNull(ManagementUtil.LINE_SEPARATOR);
		assertNotNull(ManagementUtil.USER_NAME);
		assertNotNull(ManagementUtil.USER_HOME);
		assertNotNull(ManagementUtil.USER_DIR);
	}

	// ----- 测试进程相关方法 -----
	@Test
	public void testGetCurrentPID() {
		final long pid = ManagementUtil.getCurrentPID();
		assertTrue(pid > 0);
		assertTrue(pid < Long.MAX_VALUE);
	}

	// ----- 测试JMX Bean获取方法 -----
	@Test
	public void testGetClassLoadingMXBean() {
		final ClassLoadingMXBean bean = ManagementUtil.getClassLoadingMXBean();
		assertNotNull(bean);
		assertTrue(bean.getTotalLoadedClassCount() >= 0);
		assertTrue(bean.getLoadedClassCount() >= 0);
		assertTrue(bean.getUnloadedClassCount() >= 0);
	}

	@Test
	public void testGetMemoryMXBean() {
		final MemoryMXBean bean = ManagementUtil.getMemoryMXBean();
		assertNotNull(bean);
		assertNotNull(bean.getHeapMemoryUsage());
		assertNotNull(bean.getNonHeapMemoryUsage());
		assertTrue(bean.getObjectPendingFinalizationCount() >= 0);
	}

	@Test
	public void testGetThreadMXBean() {
		final ThreadMXBean bean = ManagementUtil.getThreadMXBean();
		assertNotNull(bean);
		assertTrue(bean.getThreadCount() > 0);
		assertTrue(bean.getPeakThreadCount() > 0);
		assertTrue(bean.getTotalStartedThreadCount() > 0);
	}

	@Test
	public void testGetRuntimeMXBean() {
		final RuntimeMXBean bean = ManagementUtil.getRuntimeMXBean();
		assertNotNull(bean);
		assertNotNull(bean.getName());
		assertNotNull(bean.getVmName());
		assertNotNull(bean.getVmVendor());
		assertNotNull(bean.getVmVersion());
		assertTrue(bean.getStartTime() > 0);
		assertTrue(bean.getUptime() >= 0);
	}

	@Test
	public void testGetCompilationMXBean() {
		final CompilationMXBean bean = ManagementUtil.getCompilationMXBean();
		// CompilationMXBean 在某些JVM中可能为null
		if (bean != null) {
			assertNotNull(bean.getName());
			assertTrue(bean.getTotalCompilationTime() >= 0);
		}
	}

	@Test
	public void testGetOperatingSystemMXBean() {
		final OperatingSystemMXBean bean = ManagementUtil.getOperatingSystemMXBean();
		assertNotNull(bean);
		assertNotNull(bean.getName());
		assertNotNull(bean.getArch());
		assertNotNull(bean.getVersion());
		assertTrue(bean.getAvailableProcessors() > 0);
		assertTrue(bean.getSystemLoadAverage() >= -1);
	}

	@Test
	public void testGetMemoryPoolMXBeans() {
		final List<MemoryPoolMXBean> beans = ManagementUtil.getMemoryPoolMXBeans();
		assertNotNull(beans);
		assertFalse(beans.isEmpty());

		for (final MemoryPoolMXBean bean : beans) {
			assertNotNull(bean.getName());
			assertNotNull(bean.getType());
			assertNotNull(bean.getUsage());
			assertNotNull(bean.getPeakUsage());
		}
	}

	@Test
	public void testGetMemoryManagerMXBeans() {
		final List<MemoryManagerMXBean> beans = ManagementUtil.getMemoryManagerMXBeans();
		assertNotNull(beans);
		assertFalse(beans.isEmpty());

		for (final MemoryManagerMXBean bean : beans) {
			assertNotNull(bean.getName());
			assertNotNull(bean.getMemoryPoolNames());
		}
	}

	@Test
	public void testGetGarbageCollectorMXBeans() {
		final List<GarbageCollectorMXBean> beans = ManagementUtil.getGarbageCollectorMXBeans();
		assertNotNull(beans);
		assertFalse(beans.isEmpty());

		for (final GarbageCollectorMXBean bean : beans) {
			assertNotNull(bean.getName());
			assertNotNull(bean.getMemoryPoolNames());
			assertTrue(bean.getCollectionCount() >= 0);
			assertTrue(bean.getCollectionTime() >= 0);
		}
	}

	@Test
	public void testGetMBeanServer() {
		final MBeanServer server = ManagementUtil.getMBeanServer();
		assertNotNull(server);
		assertTrue(server.getMBeanCount() >= 0);
		assertNotNull(server.getDefaultDomain());
	}

	@Test
	public void testRegisterMBeanWithException() {
		// 测试注册MBean时抛出异常的情况
		assertThrows(ManagementException.class, () -> {
			ManagementUtil.registerMBean(null, null);
		});
	}

	// ----- 测试信息获取方法 -----
	@Test
	public void testGetJvmSpecInfo() {
		final JvmSpecInfo info = ManagementUtil.getJvmSpecInfo();
		assertNotNull(info);
		// 这些值可能为null，取决于JVM实现
		assertNotNull(info.getName());
		assertNotNull(info.getVersion());
		assertNotNull(info.getVendor());
	}

	@Test
	public void testGetJvmInfo() {
		final JvmInfo info = ManagementUtil.getJvmInfo();
		assertNotNull(info);
		assertNotNull(info.getName());
		assertNotNull(info.getVersion());
		assertNotNull(info.getVendor());
		assertNotNull(info.getInfo());
	}

	@Test
	public void testGetJavaSpecInfo() {
		final JavaSpecInfo info = ManagementUtil.getJavaSpecInfo();
		assertNotNull(info);
		assertNotNull(info.getName());
		assertNotNull(info.getVersion());
		assertNotNull(info.getVendor());
	}

	@Test
	public void testGetJavaInfo() {
		final JavaInfo info = ManagementUtil.getJavaInfo();
		assertNotNull(info);
		assertNotNull(info.getVersion());
		assertNotNull(info.getVendor());
		assertNotNull(info.getVendorURL());
		assertTrue(info.getVersionFloat() >= 0);
		assertTrue(info.getVersionInt() >= 0);
		assertTrue(info.getVersionIntSimple() >= 0);
	}

	@Test
	public void testGetJavaRuntimeInfo() {
		final JavaRuntimeInfo info = ManagementUtil.getJavaRuntimeInfo();
		assertNotNull(info);
		assertNotNull(info.getName());
		assertNotNull(info.getVersion());
		assertNotNull(info.getHomeDir());
		assertNotNull(info.getClassPath());
		assertNotNull(info.getClassVersion());
		assertNotNull(info.getLibraryPath());

		// 测试数组方法
		assertNotNull(info.getClassPathArray());
		assertNotNull(info.getLibraryPathArray());
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Test
	public void testGetOsInfo() {
		final OsInfo info = ManagementUtil.getOsInfo();
		assertNotNull(info);
		assertNotNull(info.getArch());
		assertNotNull(info.getName());
		assertNotNull(info.getVersion());
		assertNotNull(info.getFileSeparator());
		assertNotNull(info.getLineSeparator());
		assertNotNull(info.getPathSeparator());

		// 测试OS类型判断方法
		// 这些方法返回布尔值，只需要检查没有异常
		assertDoesNotThrow(() -> {
			info.isAix();
			info.isHpUx();
			info.isIrix();
			info.isLinux();
			info.isMac();
			info.isMacOsX();
			info.isOs2();
			info.isSolaris();
			info.isSunOS();
			info.isWindows();
			info.isWindows2000();
			info.isWindows95();
			info.isWindows98();
			info.isWindowsME();
			info.isWindowsNT();
			info.isWindowsXP();
			info.isWindows7();
			info.isWindows8();
			info.isWindows8_1();
			info.isWindows10();
			info.isWindows11();
		});
	}

	@Test
	public void testGetUserInfo() {
		final UserInfo info = ManagementUtil.getUserInfo();
		assertNotNull(info);
		assertNotNull(info.getName());
		assertNotNull(info.getHomeDir());
		assertNotNull(info.getCurrentDir());
		assertNotNull(info.getTempDir());
		assertNotNull(info.getLanguage());
		assertNotNull(info.getCountry());

		// 验证临时目录以分隔符结尾
		assertTrue(info.getTempDir().endsWith(File.separator));
	}

	@Test
	public void testGetHostInfo() {
		final HostInfo info = ManagementUtil.getHostInfo();
		assertNotNull(info);
		// 主机名和地址可能为null
		assertNotNull(info.getName());
		assertNotNull(info.getAddress());
	}

	@Test
	public void testGetRuntimeInfo() {
		final RuntimeInfo info = ManagementUtil.getRuntimeInfo();
		assertNotNull(info);
		assertNotNull(info.getRuntime());
		assertTrue(info.getMaxMemory() > 0);
		assertTrue(info.getTotalMemory() > 0);
		assertTrue(info.getFreeMemory() >= 0);
		assertTrue(info.getUsableMemory() >= 0);
	}

	// ----- 测试内存相关方法 -----
	@Test
	public void testGetTotalMemory() {
		final long totalMemory = ManagementUtil.getTotalMemory();
		assertTrue(totalMemory > 0);
		assertTrue(totalMemory < Runtime.getRuntime().maxMemory());
	}

	@Test
	public void testGetFreeMemory() {
		final long freeMemory = ManagementUtil.getFreeMemory();
		assertTrue(freeMemory >= 0);
		assertTrue(freeMemory <= ManagementUtil.getTotalMemory());
	}

	@Test
	public void testGetMaxMemory() {
		final long maxMemory = ManagementUtil.getMaxMemory();
		assertTrue(maxMemory > 0);
		assertTrue(maxMemory >= ManagementUtil.getTotalMemory());
	}

	// ----- 测试线程相关方法 -----
	@SuppressWarnings("ConstantValue")
	@Test
	public void testGetTotalThreadCount() {
		final int threadCount = ManagementUtil.getTotalThreadCount();
		assertTrue(threadCount > 0);
		// 当前JVM应该至少有main线程和一些系统线程
		assertTrue(threadCount >= 1);
	}

	// ----- 测试dump方法 -----
	@Test
	@Disabled("避免在测试中输出到控制台")
	public void testDumpSystemInfo() {
		ManagementUtil.dumpSystemInfo();
	}

	@Test
	public void testDumpSystemInfoToWriter() {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintWriter writer = new PrintWriter(baos);

		ManagementUtil.dumpSystemInfo(writer);
		writer.flush();

		final String output = baos.toString();
		assertFalse(output.isEmpty());
		assertTrue(output.contains("Java Runtime Name"));
	}

	// ----- 边界值测试 -----
	@Test
	public void testMemoryBoundaryValues() {
		// 测试内存值边界
		final long total = ManagementUtil.getTotalMemory();
		final long free = ManagementUtil.getFreeMemory();
		final long max = ManagementUtil.getMaxMemory();

		// 验证内存关系
		assertTrue(free <= total);
		assertTrue(total <= max);
		assertTrue(max > 0);
	}

	@Test
	public void testThreadCountBoundary() {
		final int threadCount = ManagementUtil.getTotalThreadCount();
		assertTrue(threadCount > 0);
		// 最大线程数应该有合理的上限（比如小于10000）
		assertTrue(threadCount < 10000);
	}

	@Test
	public void testPIDBoundary() {
		final long pid = ManagementUtil.getCurrentPID();
		assertTrue(pid > 0);
		// PID应该有上限（操作系统限制）
		assertTrue(pid < 1000000);
	}
}
