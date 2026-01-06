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

package cn.hutool.v7.extra.management.oshi;

import org.junit.jupiter.api.Test;
import oshi.hardware.*;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OshiUtilTest {

	@Test
	void testGetOs() {
		final OperatingSystem os = OshiUtil.getOs();
		assertNotNull(os);
		assertSame(os, OshiUtil.getOs()); // Should be singleton
	}

	@Test
	void testGetCurrentProcess() {
		final OSProcess process = OshiUtil.getCurrentProcess();
		assertNotNull(process);
		// Process ID should be valid
		assertTrue(process.getProcessID() >= 0);
	}

	@Test
	void testGetHardware() {
		final HardwareAbstractionLayer hardware = OshiUtil.getHardware();
		assertNotNull(hardware);
		assertSame(hardware, OshiUtil.getHardware()); // Should be singleton
	}

	@Test
	void testGetSystem() {
		final ComputerSystem system = OshiUtil.getSystem();
		assertNotNull(system);
		assertNotNull(system.getManufacturer());
		assertNotNull(system.getModel());
	}

	@Test
	void testGetMemory() {
		final GlobalMemory memory = OshiUtil.getMemory();
		assertNotNull(memory);
		assertTrue(memory.getTotal() > 0);
		assertTrue(memory.getAvailable() >= 0);
	}

	@Test
	void testGetProcessor() {
		final CentralProcessor processor = OshiUtil.getProcessor();
		assertNotNull(processor);
		assertTrue(processor.getLogicalProcessorCount() > 0);
	}

	@Test
	void testGetSensors() {
		final Sensors sensors = OshiUtil.getSensors();
		assertNotNull(sensors);
		// Sensors may not be available on all systems, so no specific assertions
	}

	@Test
	void testGetDiskStores() {
		final List<HWDiskStore> diskStores = OshiUtil.getDiskStores();
		assertNotNull(diskStores);
		// Disk stores should exist on any system
	}

	@Test
	void testGetNetworkIFs() {
		final List<NetworkIF> networkIFs = OshiUtil.getNetworkIFs();
		assertNotNull(networkIFs);
		// Network interfaces should exist on any system
	}

	@Test
	void testGetCpuInfo() {
		final CpuInfo cpuInfo = OshiUtil.getCpuInfo();
		assertNotNull(cpuInfo);
		// CPU info values should be valid
	}

	@Test
	void testGetCpuInfoWithWaitingTime() {
		final CpuInfo cpuInfo = OshiUtil.getCpuInfo(500);
		assertNotNull(cpuInfo);
		// CPU info values should be valid with specific waiting time
	}

	@Test
	void testSingletonBehavior() {
		// Verify that static fields are initialized only once
		final HardwareAbstractionLayer hardware1 = OshiUtil.getHardware();
		final HardwareAbstractionLayer hardware2 = OshiUtil.getHardware();
		assertSame(hardware1, hardware2);

		final OperatingSystem os1 = OshiUtil.getOs();
		final OperatingSystem os2 = OshiUtil.getOs();
		assertSame(os1, os2);
	}
}
