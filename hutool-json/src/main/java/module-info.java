/*
 * Copyright (c) 2025 Hutool Team and hutool.cn
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

/**
 *
 * @author choweli
 */
module hutool.json {
	exports cn.hutool.v7.json;
	exports cn.hutool.v7.json.jwt;
	exports cn.hutool.v7.json.writer;
	exports cn.hutool.v7.json.engine;
	exports cn.hutool.v7.json.engine.gson;
	exports cn.hutool.v7.json.engine.moshi;
	exports cn.hutool.v7.json.engine.jackson;
	exports cn.hutool.v7.json.engine.fastjson;
	exports cn.hutool.v7.json.test.bean;
	exports cn.hutool.v7.json.test.bean.report;

	requires hutool.core;
	requires hutool.crypto;
	requires com.alibaba.fastjson2;
	requires com.google.gson;
	requires com.fasterxml.jackson.databind;
	requires com.squareup.moshi;
	requires okio;
	requires java.sql;

	opens cn.hutool.v7.json;
	opens cn.hutool.v7.json.jwt;
	opens cn.hutool.v7.json.engine;
	opens cn.hutool.v7.json.issues;
	opens cn.hutool.v7.json.issues.issueIVMD5;

}
