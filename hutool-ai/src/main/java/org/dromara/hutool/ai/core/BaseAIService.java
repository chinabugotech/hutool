package org.dromara.hutool.ai.core;

import org.dromara.hutool.ai.AIException;
import org.dromara.hutool.http.HttpGlobalConfig;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.Method;

import java.util.Map;

/**
 * 基础AIService，包含基公共参数和公共方法
 *
 * @author elichow
 * @since 6.0.0
 */
public class BaseAIService {

	protected final AIConfig config;

	public BaseAIService(AIConfig config) {
		this.config = config;
	}

	protected Response sendGet(String endpoint) {
		//链式构建请求
		try {
			//设置超时
			HttpGlobalConfig.setTimeout(3000);
			return HttpUtil.createRequest(config.getApiUrl() + endpoint, Method.GET)
				.header(HeaderName.ACCEPT, "application/json")
				.header(HeaderName.AUTHORIZATION, "Bearer " + config.getApiKey())
				.send();
		} catch (AIException e) {
			throw new AIException("Failed to send GET request: " + e.getMessage(), e);
		}
	}

	protected Response sendPost(String endpoint, String paramJson) {
		//链式构建请求
		try {
			//设置超时
			HttpGlobalConfig.setTimeout(3000);
			return HttpUtil.createRequest(config.getApiUrl() + endpoint, Method.POST)
				.header(HeaderName.CONTENT_TYPE, "application/json")
				.header(HeaderName.ACCEPT, "application/json")
				.header(HeaderName.AUTHORIZATION, "Bearer " + config.getApiKey())
				.body(paramJson)
				.send();
		} catch (AIException e) {
			throw new AIException("Failed to send POST request：" + e.getMessage(), e);
		}

	}

	protected Response sendFormData(String endpoint, Map<String, Object> paramMap) {
		//链式构建请求
		try {
			//设置超时
			HttpGlobalConfig.setTimeout(3000);
			return HttpUtil.createPost(config.getApiUrl() + endpoint)
				//form表单中有file对象会自动将文件编码为 multipart/form-data 格式。不需要设置
//				.header(HeaderName.CONTENT_TYPE, "multipart/form-data")
				.header(HeaderName.AUTHORIZATION, "Bearer " + config.getApiKey())
				.form(paramMap)
				.send();
		} catch (AIException e) {
			throw new AIException("Failed to send POST request：" + e.getMessage(), e);
		}
	}
}
