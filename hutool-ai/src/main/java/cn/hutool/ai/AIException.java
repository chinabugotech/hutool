package cn.hutool.ai;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 异常处理类
 */
public class AIException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AIException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public AIException(String message) {
		super(message);
	}

	public AIException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public AIException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public AIException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public AIException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
