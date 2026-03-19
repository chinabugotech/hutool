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

package cn.hutool.v7.core.annotation.synthesize;

import java.lang.annotation.Annotation;

/**
 * 表示一组被聚合在一起的注解对象
 *
 * @author huangchengxing
 */
public interface AggregateAnnotation extends Annotation {

	/**
	 * 在聚合中是否存在的指定类型注解对象
	 *
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

	/**
	 * 获取聚合中的全部注解对象
	 *
	 * @return 注解对象
	 */
	Annotation[] getAnnotations();

}
