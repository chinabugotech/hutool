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

package cn.hutool.v7.core.annotation.attribute;

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.reflect.method.MethodUtil;
import cn.hutool.v7.core.util.ObjUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * {@link AnnotationAttribute}的基本实现
 *
 * @author huangchengxing
 */
public class CacheableAnnotationAttribute implements AnnotationAttribute {

	private volatile boolean valueInvoked;
	private volatile Object value;

	private boolean defaultValueInvoked;
	private Object defaultValue;

	private final Annotation annotation;
	private final Method attribute;

	/**
	 * 创建一个缓存的{@link AnnotationAttribute}
	 *
	 * @param annotation 注解
	 * @param attribute  属性
	 */
	public CacheableAnnotationAttribute(final Annotation annotation, final Method attribute) {
		Assert.notNull(annotation, "annotation must not null");
		Assert.notNull(attribute, "attribute must not null");
		this.annotation = annotation;
		this.attribute = attribute;
		this.valueInvoked = false;
		this.defaultValueInvoked = false;
	}

	@Override
	public Annotation getAnnotation() {
		return this.annotation;
	}

	@Override
	public Method getAttribute() {
		return this.attribute;
	}

	@Override
	public Object getValue() {
		if (!valueInvoked) {
			synchronized (this) {
				if (!valueInvoked) {
					valueInvoked = true;
					value = MethodUtil.invoke(annotation, attribute);
				}
			}
		}
		return value;
	}

	@Override
	public boolean isValueEquivalentToDefaultValue() {
		if (!defaultValueInvoked) {
			defaultValue = attribute.getDefaultValue();
			defaultValueInvoked = true;
		}
		return ObjUtil.equals(getValue(), defaultValue);
	}

}
