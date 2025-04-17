package cn.hutool.v7.http.server.engine.jetty;

import cn.hutool.v7.http.server.handler.ServerResponse;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Response;

import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Jetty响应对象
 *
 * @author looly
 */
public class JettyResponse implements ServerResponse {

	private Charset charset;
	private final Response response;

	/**
	 * 构造
	 *
	 * @param response Jetty响应对象
	 */
	public JettyResponse(Response response) {
		this.response = response;
	}

	@Override
	public JettyResponse setStatus(int statusCode) {
		response.setStatus(statusCode);
		return this;
	}

	@Override
	public JettyResponse setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

	@Override
	public JettyResponse addHeader(String header, String value) {
		response.getHeaders().add(header, value);
		return this;
	}

	@Override
	public JettyResponse setHeader(String header, String value) {
		response.getHeaders().put(header, value);
		return this;
	}

	@Override
	public OutputStream getOutputStream() {
		return Content.Sink.asOutputStream(response);
	}
}
