/*
 * Copyright (c) 2013-2026 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hutool.v7.json.engine.jackson3;

import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.lang.wrapper.Wrapper;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.ObjUtil;
import cn.hutool.v7.json.JSONException;
import cn.hutool.v7.json.engine.AbstractJSONEngine;
import cn.hutool.v7.json.engine.JSONEngineConfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.json.JsonMapper;

import java.io.OutputStream;
import java.io.Reader;

;

/**
 * Jackson引擎
 *
 * @author Looly
 */
public class Jackson3Engine extends AbstractJSONEngine implements Wrapper<JsonMapper> {

	private JsonMapper mapper;

	/**
	 * 构造
	 */
	public Jackson3Engine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(JsonMapper.class);
	}

	/**
	 * 获取Jackson的{@link JsonMapper}对象
	 *
	 * @return {@link JsonMapper}对象
	 */
	@Override
	public JsonMapper getRaw() {
		initEngine();
		return mapper;
	}

	@Override
	public void serialize(final Object bean, final OutputStream out) {
		initEngine();
		mapper.writeValue(out, bean);
	}

	@Override
	public String toJsonString(final Object bean) {
		initEngine();
		return mapper.writeValueAsString(bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		if (type instanceof Class) {
			return mapper.readValue(reader, (Class<T>) type);
		} else if (type instanceof TypeReference) {
			return mapper.readValue(reader, (TypeReference<T>) type);
		} else if (type instanceof JavaType) {
			return mapper.readValue(reader, (JavaType) type);
		}

		throw new JSONException("Unsupported type: {}", type.getClass());
	}

	@Override
	protected void reset() {
		this.mapper = null;
	}

	@Override
	protected void initEngine() {
		if (null != this.mapper) {
			return;
		}

		// 先构建 JsonFactory，开启 JsonReadFeature
		final JsonFactory jsonFactory = JsonFactory.builder()
			// 允许出现单引号
			.enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
			// 允许没有引号的字段名(非标准)
			.enable(JsonReadFeature.ALLOW_UNQUOTED_PROPERTY_NAMES)
			.build();

		final MapperBuilder<JsonMapper, JsonMapper.Builder> builder = JsonMapper.builder(jsonFactory);

		// 关闭按字母排序字段，恢复与jackson2保持一致
		builder.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
		// 空Bean默认转为{}，jackson3中默认关闭
		//builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		// 自定义配置
		final JSONEngineConfig config = ObjUtil.defaultIfNull(this.config, JSONEngineConfig::of);
		if (config.isPrettyPrint()) {
			builder.enable(SerializationFeature.INDENT_OUTPUT);
		}
		final String dateFormat = config.getDateFormat();
		// 自定义模块，添加Hutool中自身实现的JSON对象的序列化支持
		builder.addModule(new HutoolModule(dateFormat));
		if (StrUtil.isNotEmpty(dateFormat)) {
			builder.defaultDateFormat(DateUtil.newSimpleFormat(dateFormat));
		} else {
			// 默认序列化时间戳,和Jackson2保持一致
			builder.enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);
		}
		if (config.isIgnoreNullValue()) {
			builder.changeDefaultPropertyInclusion(incl -> incl
				.withValueInclusion(JsonInclude.Include.NON_NULL)
				// 容器内部元素（Map、集合里的null）也一并排除，和Jackson2行为对齐
				.withContentInclusion(JsonInclude.Include.NON_NULL));
		}

		this.mapper = builder.build();
	}
}
