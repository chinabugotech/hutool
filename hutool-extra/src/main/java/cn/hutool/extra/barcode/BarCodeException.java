package cn.hutool.extra.barcode;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 条形码异常
 *
 * @author Libai
 * @since 5.8.38
 */
public class BarCodeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BarCodeException(Throwable e) {
		super(ExceptionUtil.getMessage(e), e);
	}

	public BarCodeException(String message) {
		super(message);
	}

	public BarCodeException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public BarCodeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BarCodeException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public BarCodeException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
