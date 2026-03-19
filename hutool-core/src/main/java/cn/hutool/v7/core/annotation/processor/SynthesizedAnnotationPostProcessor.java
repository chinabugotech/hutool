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

package cn.hutool.v7.core.annotation.processor;

import cn.hutool.v7.core.annotation.Alias;
import cn.hutool.v7.core.annotation.Link;
import cn.hutool.v7.core.annotation.synthesize.AnnotationSynthesizer;
import cn.hutool.v7.core.annotation.synthesize.SynthesizedAggregateAnnotation;
import cn.hutool.v7.core.annotation.synthesize.SynthesizedAnnotation;
import cn.hutool.v7.core.comparator.CompareUtil;

import java.util.Comparator;

/**
 * <p>被合成注解后置处理器，用于在{@link SynthesizedAggregateAnnotation}加载完所有待合成注解后，
 * 再对加载好的{@link SynthesizedAnnotation}进行后置处理。<br>
 * 当多个{@link SynthesizedAnnotationPostProcessor}需要一起执行时，将按照{@link #order()}的返回值进行排序，
 * 该值更小的处理器将被优先执行。
 *
 * <p>该接口存在多个实现类，调用者应当保证在任何时候，对一批后置处理器的调用顺序都符合：
 * <ul>
 *     <li>{@link AliasAnnotationPostProcessor}；</li>
 *     <li>{@link MirrorLinkAnnotationPostProcessor}；</li>
 *     <li>{@link AliasLinkAnnotationPostProcessor}；</li>
 *     <li>其他后置处理器；</li>
 * </ul>
 *
 * @author huangchengxing
 * @see AliasAnnotationPostProcessor
 * @see MirrorLinkAnnotationPostProcessor
 * @see AliasLinkAnnotationPostProcessor
 */
public interface SynthesizedAnnotationPostProcessor extends Comparable<SynthesizedAnnotationPostProcessor> {

	/**
	 * 属性上带有{@link Alias}的注解对象的后置处理器
	 */
	AliasAnnotationPostProcessor ALIAS_ANNOTATION_POST_PROCESSOR = new AliasAnnotationPostProcessor();

	/**
	 * 属性上带有{@link Link}，且与其他注解的属性存在镜像关系的注解对象的后置处理器
	 */
	MirrorLinkAnnotationPostProcessor MIRROR_LINK_ANNOTATION_POST_PROCESSOR = new MirrorLinkAnnotationPostProcessor();

	/**
	 * 属性上带有{@link Link}，且与其他注解的属性存在别名关系的注解对象的后置处理器
	 */
	AliasLinkAnnotationPostProcessor ALIAS_LINK_ANNOTATION_POST_PROCESSOR = new AliasLinkAnnotationPostProcessor();

	/**
	 * 在一组后置处理器中被调用的顺序，越小越靠前
	 *
	 * @return 排序值
	 */
	default int order() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 比较两个后置处理器的{@link #order()}返回值
	 *
	 * @param o 比较对象
	 * @return 大小
	 */
	@Override
	default int compareTo(final SynthesizedAnnotationPostProcessor o) {
		return CompareUtil.compare(this, o, Comparator.comparing(SynthesizedAnnotationPostProcessor::order));
	}

	/**
	 * 给定指定被合成注解与其所属的合成注解聚合器实例，经过处理后返回最终
	 *
	 * @param synthesizedAnnotation 合成的注解
	 * @param synthesizer           注解合成器
	 */
	void process(SynthesizedAnnotation synthesizedAnnotation, AnnotationSynthesizer synthesizer);

}
