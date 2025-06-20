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
