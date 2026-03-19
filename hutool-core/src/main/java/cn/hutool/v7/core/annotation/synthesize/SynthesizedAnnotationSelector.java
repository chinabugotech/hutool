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

/**
 * 注解选择器，指定两个注解，选择其中一个返回。<br>
 * 该接口用于在{@link SynthesizedAggregateAnnotation}中用于从一批相同的注解对象中筛选最终用于合成注解对象。
 *
 * @author huangchengxing
 */
@FunctionalInterface
public interface SynthesizedAnnotationSelector {

	/**
	 * 返回距离根对象更近的注解，当距离一样时优先返回旧注解
	 */
	SynthesizedAnnotationSelector NEAREST_AND_OLDEST_PRIORITY = new NearestAndOldestPrioritySelector();

	/**
	 * 返回距离根对象更近的注解，当距离一样时优先返回新注解
	 */
	SynthesizedAnnotationSelector NEAREST_AND_NEWEST_PRIORITY = new NearestAndNewestPrioritySelector();

	/**
	 * 返回距离根对象更远的注解，当距离一样时优先返回旧注解
	 */
	SynthesizedAnnotationSelector FARTHEST_AND_OLDEST_PRIORITY = new FarthestAndOldestPrioritySelector();

	/**
	 * 返回距离根对象更远的注解，当距离一样时优先返回新注解
	 */
	SynthesizedAnnotationSelector FARTHEST_AND_NEWEST_PRIORITY = new FarthestAndNewestPrioritySelector();

	/**
	 * 比较两个被合成的注解，选择其中的一个并返回
	 *
	 * @param <T>           复合注解类型
	 * @param oldAnnotation 已存在的注解，该参数不允许为空
	 * @param newAnnotation 新获取的注解，该参数不允许为空
	 * @return 被合成的注解
	 */
	<T extends SynthesizedAnnotation> T choose(T oldAnnotation, T newAnnotation);

	/**
	 * 返回距离根对象更近的注解，当距离一样时优先返回旧注解
	 */
	class NearestAndOldestPrioritySelector implements SynthesizedAnnotationSelector {
		@Override
		public <T extends SynthesizedAnnotation> T choose(final T oldAnnotation, final T newAnnotation) {
			return Hierarchical.Selector.NEAREST_AND_OLDEST_PRIORITY.choose(oldAnnotation, newAnnotation);
		}
	}

	/**
	 * 返回距离根对象更近的注解，当距离一样时优先返回新注解
	 */
	class NearestAndNewestPrioritySelector implements SynthesizedAnnotationSelector {
		@Override
		public <T extends SynthesizedAnnotation> T choose(final T oldAnnotation, final T newAnnotation) {
			return Hierarchical.Selector.NEAREST_AND_NEWEST_PRIORITY.choose(oldAnnotation, newAnnotation);
		}
	}

	/**
	 * 返回距离根对象更远的注解，当距离一样时优先返回旧注解
	 */
	class FarthestAndOldestPrioritySelector implements SynthesizedAnnotationSelector {
		@Override
		public <T extends SynthesizedAnnotation> T choose(final T oldAnnotation, final T newAnnotation) {
			return Hierarchical.Selector.FARTHEST_AND_OLDEST_PRIORITY.choose(oldAnnotation, newAnnotation);
		}
	}

	/**
	 * 返回距离根对象更远的注解，当距离一样时优先返回新注解
	 */
	class FarthestAndNewestPrioritySelector implements SynthesizedAnnotationSelector {
		@Override
		public <T extends SynthesizedAnnotation> T choose(final T oldAnnotation, final T newAnnotation) {
			return Hierarchical.Selector.FARTHEST_AND_NEWEST_PRIORITY.choose(oldAnnotation, newAnnotation);
		}
	}

}
