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

package cn.hutool.v7.crypto.digest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BCryptTest {

	@Test
	public void checkpwTest(){
		assertFalse(BCrypt.checkpw("xxx",
				"$2a$2a$10$e4lBTlZ019KhuAFyqAlgB.Jxc6cM66GwkSR/5/xXNQuHUItPLyhzy"));
	}

	@Test
	public void hashpwShortSaltThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> BCrypt.hashpw("password", "$"));
		assertThrows(IllegalArgumentException.class, () -> BCrypt.hashpw("password", "$2"));
		assertThrows(IllegalArgumentException.class, () -> BCrypt.hashpw("password", "$2a"));
		assertThrows(IllegalArgumentException.class, () -> BCrypt.hashpw("password", "$2a$"));
		assertThrows(IllegalArgumentException.class, () -> BCrypt.hashpw("password", "$2a$10$"));
	}
}
