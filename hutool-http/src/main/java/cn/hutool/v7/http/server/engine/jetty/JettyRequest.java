package cn.hutool.v7.http.server.engine.jetty;

import cn.hutool.v7.http.server.handler.ServerRequest;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Request;

import java.io.InputStream;

/**
 * Jetty请求对象包装
 *
 * @author looly
 */
public class JettyRequest implements ServerRequest {

	private final Request request;

	/**
	 * 构造
	 *
	 * @param request Jetty请求对象
	 */
	public JettyRequest(Request request) {
		this.request = request;
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public String getPath() {
		return Request.getPathInContext(request);
	}

	@Override
	public String getQuery() {
		return request.getHttpURI().getQuery();
	}

	@Override
	public String getHeader(String name) {
		return request.getHeaders().get(name);
	}

	/**
	 * 获取所有请求头
	 *
	 * @return 请求头
	 */
	public HttpFields getHeaders() {
		return request.getHeaders();
	}

	@Override
	public InputStream getBodyStream() {
		return Content.Source.asInputStream(request);
	}
}
