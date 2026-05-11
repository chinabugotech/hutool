package cn.hutool.v7.crypto.sign.http;

import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.util.CharsetUtil;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * HTTP签名请求，是签名核心模块使用的轻量请求模型。
 * <p>
 * 此对象为可变对象，不保证并发修改安全。调用方应在单线程中构造完成后再传入签名或验签流程。
 *
 * @author mumu
 * @since 7.0.0
 */
public class HttpSignRequest {

	private String method;
	private String path;
	private final List<QueryParam> queryParamList = new ArrayList<>();
	private final Map<String, List<String>> headerMap = new LinkedHashMap<>();
	private byte[] bodyBytes;

	/**
	 * 创建请求。
	 *
	 * @return 请求
	 */
	public static HttpSignRequest create() {
		return new HttpSignRequest();
	}

	/**
	 * 获取HTTP方法。
	 *
	 * @return HTTP方法
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 设置HTTP方法。
	 *
	 * @param method HTTP方法
	 * @return this
	 */
	public HttpSignRequest setMethod(final String method) {
		this.method = method;
		return this;
	}

	/**
	 * 获取请求路径。
	 *
	 * @return 请求路径
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 设置请求路径。
	 *
	 * @param path 请求路径
	 * @return this
	 */
	public HttpSignRequest setPath(final String path) {
		this.path = path;
		return this;
	}

	/**
	 * 获取查询参数列表。
	 *
	 * @return 查询参数列表
	 */
	public List<QueryParam> getQueryParams() {
		return Collections.unmodifiableList(queryParamList);
	}

	/**
	 * 增加查询参数。
	 *
	 * @param name  参数名
	 * @param value 参数值，{@code null}按空字符串处理
	 * @return this
	 */
	public HttpSignRequest addQueryParam(final String name, final Object value) {
		if (value instanceof Iterable) {
			for (final Object item : (Iterable<?>) value) {
				addQueryParam(name, item);
			}
			return this;
		}
		if (null != value && value.getClass().isArray()) {
			final int length = Array.getLength(value);
			for (int i = 0; i < length; i++) {
				addQueryParam(name, Array.get(value, i));
			}
			return this;
		}
		this.queryParamList.add(new QueryParam(name, null == value ? "" : String.valueOf(value)));
		return this;
	}

	/**
	 * 设置查询参数并清空已有查询参数。
	 *
	 * @param queryParams 查询参数
	 * @return this
	 */
	public HttpSignRequest setQueryParams(final Map<String, ?> queryParams) {
		this.queryParamList.clear();
		if (null != queryParams) {
			for (final Map.Entry<String, ?> entry : queryParams.entrySet()) {
				addQueryParam(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}

	/**
	 * 获取Header映射。
	 *
	 * @return Header映射
	 */
	public Map<String, List<String>> getHeaders() {
		final Map<String, List<String>> headers = new LinkedHashMap<>();
		for (final Map.Entry<String, List<String>> entry : this.headerMap.entrySet()) {
			headers.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
		}
		return Collections.unmodifiableMap(headers);
	}

	/**
	 * 设置Header并清空已有Header。
	 *
	 * @param headers Header映射
	 * @return this
	 */
	public HttpSignRequest setHeaders(final Map<String, ? extends Collection<String>> headers) {
		this.headerMap.clear();
		if (null != headers) {
			for (final Map.Entry<String, ? extends Collection<String>> entry : headers.entrySet()) {
				if (null != entry.getValue()) {
					for (final String value : entry.getValue()) {
						addHeader(entry.getKey(), value);
					}
				}
			}
		}
		return this;
	}

	/**
	 * 设置单值Header并清空已有Header。
	 *
	 * @param headers Header映射
	 * @return this
	 */
	public HttpSignRequest setHeaderMap(final Map<String, String> headers) {
		this.headerMap.clear();
		if (null != headers) {
			for (final Map.Entry<String, String> entry : headers.entrySet()) {
				setHeader(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}

	/**
	 * 设置Header，替换同名Header已有值。
	 *
	 * @param name  Header名称
	 * @param value Header值
	 * @return this
	 */
	public HttpSignRequest setHeader(final String name, final String value) {
		removeHeader(name);
		return addHeader(name, value);
	}

	/**
	 * 增加Header值。
	 *
	 * @param name  Header名称
	 * @param value Header值
	 * @return this
	 */
	public HttpSignRequest addHeader(final String name, final String value) {
		if (null == name) {
			return this;
		}
		List<String> headerValues = this.headerMap.get(name);
		if (null == headerValues) {
			headerValues = new ArrayList<>();
			this.headerMap.put(name, headerValues);
		}
		headerValues.add(null == value ? "" : value);
		return this;
	}

	/**
	 * 获取Header首个值。
	 *
	 * @param name Header名称
	 * @return Header值
	 */
	public String getHeader(final String name) {
		final List<String> values = getHeaderValues(name);
		return ArrayUtil.isEmpty(values) ? null : values.get(0);
	}

	/**
	 * 获取Header值列表。
	 *
	 * @param name Header名称
	 * @return Header值列表
	 */
	public List<String> getHeaderValues(final String name) {
		if (null == name) {
			return null;
		}
		for (final Map.Entry<String, List<String>> entry : this.headerMap.entrySet()) {
			if (name.equalsIgnoreCase(entry.getKey())) {
				return Collections.unmodifiableList(entry.getValue());
			}
		}
		return null;
	}

	/**
	 * 获取请求体原始字节。
	 *
	 * @return 请求体原始字节
	 */
	public byte[] getBodyBytes() {
		return null == bodyBytes ? null : bodyBytes.clone();
	}

	/**
	 * 设置请求体原始字节。
	 *
	 * @param bodyBytes 请求体原始字节
	 * @return this
	 */
	public HttpSignRequest setBodyBytes(final byte[] bodyBytes) {
		this.bodyBytes = null == bodyBytes ? null : bodyBytes.clone();
		return this;
	}

	/**
	 * 设置UTF-8字符串请求体。
	 *
	 * @param body 请求体字符串
	 * @return this
	 */
	public HttpSignRequest setBody(final String body) {
		return setBody(body, CharsetUtil.UTF_8);
	}

	/**
	 * 设置字符串请求体。
	 *
	 * @param body    请求体字符串
	 * @param charset 字符集
	 * @return this
	 */
	public HttpSignRequest setBody(final String body, final Charset charset) {
		this.bodyBytes = null == body ? null : body.getBytes(null == charset ? CharsetUtil.UTF_8 : charset);
		return this;
	}

	/**
	 * 复制请求。
	 *
	 * @return 请求副本
	 */
	public HttpSignRequest copy() {
		final HttpSignRequest request = create()
			.setMethod(this.method)
			.setPath(this.path)
			.setBodyBytes(this.bodyBytes);
		for (final QueryParam param : this.queryParamList) {
			request.queryParamList.add(param);
		}
		for (final Map.Entry<String, List<String>> entry : this.headerMap.entrySet()) {
			for (final String value : entry.getValue()) {
				request.addHeader(entry.getKey(), value);
			}
		}
		return request;
	}

	private void removeHeader(final String name) {
		if (null == name) {
			return;
		}
		final List<String> removeNames = new ArrayList<>();
		for (final String existsName : this.headerMap.keySet()) {
			if (name.equalsIgnoreCase(existsName)) {
				removeNames.add(existsName);
			}
		}
		for (final String removeName : removeNames) {
			this.headerMap.remove(removeName);
		}
	}

	/**
	 * 查询参数。
	 */
	public static class QueryParam {
		private final String name;
		private final String value;

		/**
		 * 构造查询参数。
		 *
		 * @param name  参数名
		 * @param value 参数值
		 */
		public QueryParam(final String name, final String value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * 获取参数名。
		 *
		 * @return 参数名
		 */
		public String getName() {
			return name;
		}

		/**
		 * 获取参数值。
		 *
		 * @return 参数值
		 */
		public String getValue() {
			return value;
		}
	}
}
