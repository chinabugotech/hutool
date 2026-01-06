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

package cn.hutool.v7.swing.captcha;

import cn.hutool.v7.core.math.Calculator;
import cn.hutool.v7.swing.captcha.generator.MathGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeneratorTest {

	@Test
	public void mathGeneratorTest() {
		final MathGenerator mathGenerator = new MathGenerator();
		for (int i = 0; i < 1000; i++) {
			mathGenerator.verify(mathGenerator.generate(), "0");
		}

		final MathGenerator mathGenerator1 = new MathGenerator(2, false);
		for (int i = 0; i < 1000; i++) {
			final String generate = mathGenerator1.generate();
			Assertions.assertTrue(Calculator.conversion(generate) >= 0);
		}
	}
}
