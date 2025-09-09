/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

package cn.hutool.v7.db.sql;

import java.sql.ResultSet;

/**
 * 获取结果集的移动方向
 *
 * @author looly
 * @since 7.0.0
 */
public enum FetchDirection {

	/**
	 * 默认方向
	 */
	FORWARD(ResultSet.FETCH_FORWARD),
	/**
	 * 反向
	 */
	REVERSE(ResultSet.FETCH_REVERSE),
	/**
	 * 不确定方向
	 */
	UNKNOWN(ResultSet.FETCH_UNKNOWN);

	/**
	 * 获取值
	 */
	private final int value;

	FetchDirection(int value) {
		this.value = value;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public int getValue() {
		return value;
	}
}
