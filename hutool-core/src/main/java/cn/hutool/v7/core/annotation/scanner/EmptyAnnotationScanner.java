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

package cn.hutool.v7.core.annotation.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 默认不扫描任何元素的扫描器
 *
 * @author huangchengxing
 */
public class EmptyAnnotationScanner implements AnnotationScanner {

	@Override
	public boolean support(final AnnotatedElement annotatedEle) {
		return true;
	}

	@Override
	public List<Annotation> getAnnotations(final AnnotatedElement annotatedEle) {
		return Collections.emptyList();
	}

	@Override
	public void scan(final BiConsumer<Integer, Annotation> consumer, final AnnotatedElement annotatedEle, final Predicate<Annotation> filter) {
		// do nothing
	}
}
