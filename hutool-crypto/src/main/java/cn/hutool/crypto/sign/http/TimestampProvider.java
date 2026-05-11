package cn.hutool.crypto.sign.http;

import cn.hutool.core.date.DateUtil;

/**
 * 时间戳提供器，返回Unix epoch seconds。
 *
 * @author mumu
 * @since 5.8.45
 */
public interface TimestampProvider {

	/**
	 * 系统时间戳提供器。
	 */
	TimestampProvider SYSTEM = DateUtil::currentSeconds;

	/**
	 * 获取当前时间戳，单位秒。
	 *
	 * @return 当前时间戳
	 */
	long currentTimestamp();
}
