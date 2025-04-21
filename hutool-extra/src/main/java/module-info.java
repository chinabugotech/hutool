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
module hutool.extra {
	requires spring.core;
	requires org.apache.commons.compress;
	requires emoji.java;
	requires org.apache.commons.net;
	requires ftpserver.core;
	requires ftplet.api;
	requires hutool.setting;
	requires hutool.log;
	requires hutool.core;
	requires jakarta.mail;
	requires java.management;
	requires com.github.oshi;
	requires kafka.clients;
	requires com.rabbitmq.client;
	requires rocketmq.client;
	requires rocketmq.common;
	requires bopomofo4j;
	requires pinyin;
	requires jpinyin;
	requires pinyin4j;
	requires TinyPinyin;
	requires spring.context;
	requires spring.beans;
	requires ganymed.ssh2;
	requires com.jcraft.jsch;
	requires org.apache.sshd.core;
	requires org.apache.sshd.common;
	requires mmseg4j.core;
	requires lucene.core;
	requires lucene.analyzers.smartcn;
	requires IKAnalyzer.lucene;
	requires jieba.analysis;
	requires jcseg.core;
	requires word;
	requires hanlp.portable;
	requires ansj.seg;
	requires mynlp;
	requires io.pebbletemplates;
	requires enjoy;
	requires wit.core;
	requires freemarker;
	requires thymeleaf;
	requires velocity.engine.core;
	requires gg.jte.runtime;
	requires gg.jte;
	requires jetbrick.template;
	requires jetbrick.commons;
	requires rythm.engine;
	requires beetl.core;
	requires jakarta.xml.bind;


}
