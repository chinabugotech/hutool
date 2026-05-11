package cn.hutool.v7.crypto.sign.http;

import cn.hutool.v7.core.date.DateUtil;

/**
 * 时间戳提供器，返回Unix epoch seconds。
 *
 * @author mumu
 * @since 7.0.0
 */
public interface TimestampProvider {

	/**
	 * 系统时间戳提供器。
	 */
	TimestampProvider SYSTEM = () -> DateUtil.currentSeconds();

	/**
	 * 获取当前时间戳，单位秒。
	 *
	 * @return 当前时间戳
	 */
	long currentTimestamp();
}
