package cn.hutool.ai.core;

import cn.hutool.ai.AIException;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.Map;

/**
 * 基础AIService，包含基公共参数和公共方法
 *
 * @author elichow
 * @since 5.8.37
 */
public class BaseAIService {

	protected final AIConfig config;

	public BaseAIService(AIConfig config) {
		this.config = config;
	}

	protected HttpResponse sendGet(String endpoint) {
		//链式构建请求
		try {
			return HttpRequest.get(config.getApiUrl() + endpoint)
				.header(Header.ACCEPT, "application/json")
				.header(Header.AUTHORIZATION, "Bearer " + config.getApiKey())
				.timeout(60000)//超时，毫秒
				.execute();
		} catch (AIException e) {
			throw new AIException("Failed to send GET request: " + e.getMessage(), e);
		}
	}

	protected HttpResponse sendPost(String endpoint, String paramJson) {
		//链式构建请求
		try {
			return HttpRequest.post(config.getApiUrl() + endpoint)
				.header(Header.CONTENT_TYPE, "application/json")//头信息，多个头信息多次调用此方法即可
				.header(Header.ACCEPT, "application/json")
				.header(Header.AUTHORIZATION, "Bearer " + config.getApiKey())
				.body(paramJson)//表单内容
				.timeout(60000)//超时，毫秒
				.execute();
		} catch (AIException e) {
			throw new AIException("Failed to send POST request：" + e.getMessage(), e);
		}

	}

	protected HttpResponse sendFormData(String endpoint, Map<String, Object> paramMap) {
		//链式构建请求
		try {
			return HttpRequest.post(config.getApiUrl() + endpoint)
				.header(Header.CONTENT_TYPE, "multipart/form-data")//头信息，多个头信息多次调用此方法即可
				.header(Header.ACCEPT, "application/json")
				.header(Header.AUTHORIZATION, "Bearer " + config.getApiKey())
				.form(paramMap)
				.timeout(60000)//超时，毫秒
				.execute();
		} catch (AIException e) {
			throw new AIException("Failed to send POST request：" + e.getMessage(), e);
		}
	}
}
