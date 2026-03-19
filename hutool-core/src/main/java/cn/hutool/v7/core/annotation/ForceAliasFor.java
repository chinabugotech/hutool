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

package cn.hutool.v7.core.annotation;

import java.lang.annotation.*;

/**
 * <p>{@link Link}的子注解。表示“原始属性”将强制作为“关联属性”的别名。效果等同于在“原始属性”上添加{@link Alias}注解，
 * 任何情况下，获取“关联属性”的值都将直接返回“原始属性”的值
 * <b>注意，该注解与{@link Link}、{@link AliasFor}或{@link MirrorFor}一起使用时，将只有被声明在最上面的注解会生效</b>
 *
 * @author huangchengxing
 * @see Link
 * @see RelationType#FORCE_ALIAS_FOR
 */
@Link(type = RelationType.FORCE_ALIAS_FOR)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface ForceAliasFor {

	/**
	 * 产生关联的注解类型，当不指定时，默认指注释的属性所在的类
	 *
	 * @return 关联注解类型
	 */
	@Link(annotation = Link.class, attribute = "annotation", type = RelationType.FORCE_ALIAS_FOR)
	Class<? extends Annotation> annotation() default Annotation.class;

	/**
	 * {@link #annotation()}指定注解中关联的属性
	 *
	 * @return 关联的属性
	 */
	@Link(annotation = Link.class, attribute = "attribute", type = RelationType.FORCE_ALIAS_FOR)
	String attribute() default "";
}
