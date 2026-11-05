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

import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONArray;
import cn.hutool.v7.json.JSONObject;
import cn.hutool.v7.json.JSONPrimitive;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

/**
 * Hutool JSON序列化器
 *
 * @author Looly
 * @since 7.0.0
 */
public class HutoolJSONSerializer extends StdSerializer<JSON> {

	/**
	 * 构造
	 */
	protected HutoolJSONSerializer() {
		super(JSON.class);
	}

	@Override
	public void serialize(final JSON json, final JsonGenerator jsonGenerator, final SerializationContext context) {
		if(json instanceof JSONPrimitive){
			jsonGenerator.writePOJO(((JSONPrimitive) json).getValue());
		}else if(json instanceof JSONObject){
			jsonGenerator.writeStartObject();
			json.asJSONObject().forEach(jsonGenerator::writePOJOProperty);
			jsonGenerator.writeEndObject();
		}else if(json instanceof JSONArray){
			jsonGenerator.writeStartArray();
			json.asJSONArray().forEach(jsonGenerator::writePOJO);
			jsonGenerator.writeEndArray();
		}
	}
}
