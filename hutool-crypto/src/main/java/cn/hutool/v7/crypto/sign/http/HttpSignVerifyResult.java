package cn.hutool.v7.crypto.sign.http;

/**
 * HTTP验签结果。
 *
 * @author mumu
 * @since 7.0.0
 */
public class HttpSignVerifyResult {

	/**
	 * 是否验签成功。
	 */
	private final boolean success;
	/**
	 * 验签失败错误码。
	 */
	private final HttpSignErrorCode errorCode;
	/**
	 * 验签失败消息。
	 */
	private final String message;
	/**
	 * 规范请求，失败时仅在签名不匹配等需要排查场景返回。
	 */
	private final CanonicalRequest canonicalRequest;
	/**
	 * 待签名字符串。
	 */
	private final String stringToSign;

	private HttpSignVerifyResult(final boolean success, final HttpSignErrorCode errorCode, final String message,
								 final CanonicalRequest canonicalRequest, final String stringToSign) {
		this.success = success;
		this.errorCode = errorCode;
		this.message = message;
		this.canonicalRequest = canonicalRequest;
		this.stringToSign = stringToSign;
	}

	/**
	 * 创建成功结果。
	 *
	 * @param canonicalRequest 规范请求
	 * @param stringToSign     待签名字符串
	 * @return 验签结果
	 */
	public static HttpSignVerifyResult success(final CanonicalRequest canonicalRequest, final String stringToSign) {
		return new HttpSignVerifyResult(true, null, null, canonicalRequest, stringToSign);
	}

	/**
	 * 创建失败结果。
	 *
	 * @param errorCode 错误码
	 * @param message   错误消息
	 * @return 验签结果
	 */
	public static HttpSignVerifyResult fail(final HttpSignErrorCode errorCode, final String message) {
		return new HttpSignVerifyResult(false, errorCode, message, null, null);
	}

	/**
	 * 创建带规范请求信息的失败结果。
	 *
	 * @param errorCode        错误码
	 * @param message          错误消息
	 * @param canonicalRequest 规范请求
	 * @param stringToSign     待签名字符串
	 * @return 验签结果
	 */
	public static HttpSignVerifyResult fail(final HttpSignErrorCode errorCode, final String message,
											final CanonicalRequest canonicalRequest, final String stringToSign) {
		return new HttpSignVerifyResult(false, errorCode, message, canonicalRequest, stringToSign);
	}

	/**
	 * 判断是否验签成功。
	 *
	 * @return 是否成功
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 获取错误码。
	 *
	 * @return 错误码
	 */
	public HttpSignErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * 获取错误消息。
	 *
	 * @return 错误消息
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 获取规范请求。
	 *
	 * @return 规范请求
	 */
	public CanonicalRequest getCanonicalRequest() {
		return canonicalRequest;
	}

	/**
	 * 获取待签名字符串。
	 *
	 * @return 待签名字符串
	 */
	public String getStringToSign() {
		return stringToSign;
	}
}
