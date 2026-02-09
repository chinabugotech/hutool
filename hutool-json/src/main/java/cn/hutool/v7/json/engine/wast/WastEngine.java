/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.json.engine.wast;

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.ObjUtil;
import cn.hutool.v7.json.engine.AbstractJSONEngine;
import cn.hutool.v7.json.engine.JSONEngineConfig;
import io.github.wycst.wast.common.reflect.GenericParameterizedType;
import io.github.wycst.wast.json.JSON;
import io.github.wycst.wast.json.JSONConfig;
import io.github.wycst.wast.json.JSONReader;

import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Wast引擎实现
 *
 * @author Looly
 * @since 7.0.0
 */
public class WastEngine extends AbstractJSONEngine {

	private JSONConfig wastConfig;

	/**
	 * 构造
	 */
	public WastEngine() {
		// issue#IABWBL JDK8下，在IDEA旗舰版加载Spring boot插件时，启动应用不会检查字段类是否存在
		// 此处构造时调用下这个类，以便触发类是否存在的检查
		Assert.notNull(JSON.class);
	}

	@Override
	public void serialize(final Object bean, final OutputStream out) {
		initEngine();
		JSON.writeJsonTo(bean, out, this.wastConfig);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(final Reader reader, final Object type) {
		initEngine();
		// https://github.com/wycst/wast/issues/3
		final JSONReader jsonReader = new JSONReader(reader);
		return (T) jsonReader.readAsResult(GenericParameterizedType.of((Type) type));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T fromJsonString(final String jsonStr, final Object type) {
		initEngine();
		return (T) JSON.parse(jsonStr, GenericParameterizedType.of((Type) type));
	}

	@Override
	protected void reset() {
		wastConfig = null;
	}

	@Override
	protected void initEngine() {
		if(null != this.wastConfig){
			return;
		}

		this.config = ObjUtil.defaultIfNull(this.config, JSONEngineConfig::of);
		final JSONEngineConfig config = ObjUtil.defaultIfNull(this.config, JSONEngineConfig::of);
		this.wastConfig = JSONConfig.of();
		this.wastConfig.setFormatOut(config.isPrettyPrint());

		final String dateFormat = config.getDateFormat();
		if(StrUtil.isNotBlank(dateFormat)){
			this.wastConfig.setDateFormat(true);
			this.wastConfig.setDateFormatPattern(dateFormat);
		}
	}
}
