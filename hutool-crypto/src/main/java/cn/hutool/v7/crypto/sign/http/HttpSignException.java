package cn.hutool.v7.crypto.sign.http;

import cn.hutool.v7.crypto.CryptoException;

/**
 * HTTP签名异常。
 *
 * @author mumu
 * @since 7.0.0
 */
public class HttpSignException extends CryptoException {
	private static final long serialVersionUID = 1L;

	private final HttpSignErrorCode errorCode;

	/**
	 * 构造。
	 *
	 * @param errorCode 错误码
	 * @param message   错误消息
	 */
	public HttpSignException(final HttpSignErrorCode errorCode, final String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * 构造。
	 *
	 * @param errorCode 错误码
	 * @param throwable 原始异常
	 */
	public HttpSignException(final HttpSignErrorCode errorCode, final Throwable throwable) {
		super(throwable);
		this.errorCode = errorCode;
	}

	/**
	 * 获取错误码。
	 *
	 * @return 错误码
	 */
	public HttpSignErrorCode getErrorCode() {
		return errorCode;
	}
}
