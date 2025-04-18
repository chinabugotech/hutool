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
module hutool.core {
	exports cn.hutool.v7.core.exception;
	exports cn.hutool.v7.core.lang;
	exports cn.hutool.v7.core.lang.wrapper;
	exports cn.hutool.v7.core.text;
	exports cn.hutool.v7.core.io;
	exports cn.hutool.v7.core.io.file;
	exports cn.hutool.v7.core.io.stream;
	exports cn.hutool.v7.core.io.resource;
	exports cn.hutool.v7.core.util;
	exports cn.hutool.v7.core.array;
	exports cn.hutool.v7.core.codec.binary;
	exports cn.hutool.v7.core.thread;
	exports cn.hutool.v7.core.thread.lock;
	exports cn.hutool.v7.core.map;
	exports cn.hutool.v7.core.collection;
	exports cn.hutool.v7.core.spi;
	exports cn.hutool.v7.core.func;
	exports cn.hutool.v7.core.lang.caller;
	exports cn.hutool.v7.core.reflect;
	exports cn.hutool.v7.core.lang.ansi;
	exports cn.hutool.v7.core.date;
	exports cn.hutool.v7.core.lang.getter;
	exports cn.hutool.v7.core.text.split;
	exports cn.hutool.v7.core.bean.copier;
	exports cn.hutool.v7.core.bean.path;
	exports cn.hutool.v7.core.bean;
	exports cn.hutool.v7.core.net.url;
	exports cn.hutool.v7.core.io.watch;
	exports cn.hutool.v7.core.io.watch.watchers;
	exports cn.hutool.v7.core.convert;
	exports cn.hutool.v7.core.regex;
	exports cn.hutool.v7.core.map.concurrent;
	exports cn.hutool.v7.core.math;
	exports cn.hutool.v7.core.collection.set;
	exports cn.hutool.v7.core.collection.iter;
	exports cn.hutool.v7.core.reflect.method;
	exports cn.hutool.v7.core.lang.builder;
	exports cn.hutool.v7.core.lang.range;
	exports cn.hutool.v7.core.lang.page;
	exports cn.hutool.v7.core.classloader;
	exports cn.hutool.v7.core.pool;
	exports cn.hutool.v7.core.pool.partition;
	exports cn.hutool.v7.core.stream;
	exports cn.hutool.v7.core.lang.tuple;
	exports cn.hutool.v7.core.codec;
	exports cn.hutool.v7.core.net;
	exports cn.hutool.v7.core.map.reference;
	exports cn.hutool.v7.core.lang.mutable;
	exports cn.hutool.v7.core.lang.loader;
	exports cn.hutool.v7.core.comparator;
	exports cn.hutool.v7.core.date.format;
	exports cn.hutool.v7.core.lang.copier;
	exports cn.hutool.v7.core.convert.impl;
	exports cn.hutool.v7.core.bean.path.node;
	exports cn.hutool.v7.core.xml;
	exports cn.hutool.v7.core.reflect.kotlin;
	exports cn.hutool.v7.core.text.escape;
	exports cn.hutool.v7.core.annotation;
	exports cn.hutool.v7.core.map.multi;
	exports cn.hutool.v7.core.data.id;
	exports cn.hutool.v7.core.io.buffer;
	exports cn.hutool.v7.core.reflect.creator;
	exports cn.hutool.v7.core.compress;
	exports cn.hutool.v7.core.net.ssl;

	requires java.desktop;
	requires java.sql;
	requires java.management;
	requires java.compiler;
	requires java.naming;

}
