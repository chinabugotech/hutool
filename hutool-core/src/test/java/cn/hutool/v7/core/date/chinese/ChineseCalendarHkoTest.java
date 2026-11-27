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

package cn.hutool.v7.core.date.chinese;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Offline HKO reference dates; see the fixture README for coverage and known differences. */
@EnabledIfSystemProperty(named = "hutool.hko.enabled", matches = "true")
@ResourceLock("java.util.TimeZone.default")
public class ChineseCalendarHkoTest {

	@Test
	public void lunarGoldenDatesTest() throws IOException {
		final TimeZone original = TimeZone.getDefault();
		final List<String> differences = new ArrayList<>();
		LocalDate expectedDate = LocalDate.of(1901, 1, 1);
		int compared = 0;
		int outsideSupportedRange = 0;
		try {
			// The legacy lunar constructor uses the default time zone. Calendar dates in
			// this reference suite use UTC+8; time-zone independence is tested separately.
			TimeZone.setDefault(TimeZone.getTimeZone("GMT+08:00"));
			try (BufferedReader reader = reader("lunar.csv.gz")) {
				assertEquals("solarDate,lunarYear,lunarMonth,lunarDay,isLeapMonth", reader.readLine());
				String line;
				while ((line = reader.readLine()) != null) {
					final String[] fields = line.split(",");
					final LocalDate solar = LocalDate.parse(fields[0]);
					assertEquals(expectedDate, solar, "Reference dates must be continuous");
					expectedDate = expectedDate.plusDays(1);
					final int year = Integer.parseInt(fields[1]);
					final int month = Integer.parseInt(fields[2]);
					final int day = Integer.parseInt(fields[3]);
					final boolean leap = "1".equals(fields[4]);
					if (year > LunarInfo.MAX_YEAR) {
						assertEquals(2100, year);
						outsideSupportedRange++;
						continue;
					}
					compared++;
					final String expectedLunar = year + "/" + month + "/" + day + "/" + fields[4];
					final ChineseDate forward = new ChineseDate(solar);
					final String actualLunar = forward.getChineseYear() + "/"
						+ (forward.getMonth() - (forward.isLeapMonth() ? 1 : 0)) + "/"
						+ forward.getDay() + "/" + (forward.isLeapMonth() ? 1 : 0);
					if (!expectedLunar.equals(actualLunar)) {
						differences.add("forward," + solar + "," + expectedLunar + "," + actualLunar);
					}
					final ChineseDate reverse = new ChineseDate(year, month, day, leap);
					final String actualSolar = reverse.getGregorianYear() + "/"
						+ reverse.getGregorianMonthBase1() + "/" + reverse.getGregorianDay();
					final String expectedSolar = solar.getYear() + "/" + solar.getMonthValue() + "/" + solar.getDayOfMonth();
					if (!expectedSolar.equals(actualSolar)) {
						differences.add("reverse," + solar + "," + expectedLunar + "," + actualSolar);
					}
				}
			}
		} finally {
			TimeZone.setDefault(original);
		}
		assertEquals(LocalDate.of(2101, 1, 1), expectedDate);
		assertEquals(72723, compared);
		assertEquals(326, outsideSupportedRange);
		// Compare every differing value, rather than ignoring all dates in 2057.
		assertEquals(knownDifferences(), differences);
	}

	@Test
	public void solarTermGoldenDatesTest() throws IOException {
		int count = 0;
		try (BufferedReader reader = reader("solar-terms.csv.gz")) {
			assertEquals("solarDate,solarTermIndex,solarTermName", reader.readLine());
			String line;
			while ((line = reader.readLine()) != null) {
				final String[] fields = line.split(",");
				final LocalDate date = LocalDate.parse(fields[0]);
				final int index = Integer.parseInt(fields[1]);
				assertEquals(1901 + count / 24, date.getYear());
				assertEquals(count % 24, index);
				assertEquals(date.getDayOfMonth(), SolarTerms.getTerm(date.getYear(), index + 1), fields[0]);
				assertEquals(simplify(fields[2]), SolarTerms.getTerm(date), fields[0]);
				assertEquals("", SolarTerms.getTerm(date.minusDays(1)));
				assertEquals("", SolarTerms.getTerm(date.plusDays(1)));
				count++;
			}
		}
		assertEquals(4800, count);
	}

	private static List<String> knownDifferences() throws IOException {
		final List<String> lines = new ArrayList<>();
		try (BufferedReader reader = reader("known-differences.csv")) {
			assertEquals("kind,date,expected,actual", reader.readLine());
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		}
		return lines;
	}

	private static BufferedReader reader(final String name) throws IOException {
		final InputStream stream;
		if (name.endsWith(".gz")) {
			final String directory = System.getProperty("hutool.hko.dir", "target/hko-fixtures");
			try (InputStream input = new GZIPInputStream(Files.newInputStream(Paths.get(directory, name)));
				 ByteArrayOutputStream output = new ByteArrayOutputStream()) {
				final byte[] buffer = new byte[8192];
				int count;
				while ((count = input.read(buffer)) != -1) {
					output.write(buffer, 0, count);
				}
				final byte[] data = output.toByteArray();
				final String expectedHash = "lunar.csv.gz".equals(name)
					? "970e0b804f5924aded7ccad8ad6c8da2abb5780ed1cdac7f8957dc9b46cf95d9"
					: "4f12c658eed72fa07d273b2a1b30d81beac239b825d1d4de7d2267266f6cf138";
				assertEquals(expectedHash, sha256(data), "Review HKO source changes before updating the snapshot hash");
				stream = new ByteArrayInputStream(data);
			}
		} else {
			stream = ChineseCalendarHkoTest.class.getResourceAsStream("/date/chinese/hko/" + name);
			assertNotNull(stream, name);
		}
		return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
	}

	private static String sha256(final byte[] data) {
		try {
			final StringBuilder hex = new StringBuilder();
			for (final byte value : MessageDigest.getInstance("SHA-256").digest(data)) {
				hex.append(String.format("%02x", value & 0xff));
			}
			return hex.toString();
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	private static String simplify(final String name) {
		return name.replace('驚', '惊').replace('蟄', '蛰').replace('穀', '谷')
			.replace('滿', '满').replace('種', '种').replace('處', '处');
	}
}
