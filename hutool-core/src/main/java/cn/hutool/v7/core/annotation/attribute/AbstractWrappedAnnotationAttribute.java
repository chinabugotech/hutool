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

package cn.hutool.v7.core.annotation.attribute;

import cn.hutool.v7.core.lang.Assert;
import cn.hutool.v7.core.util.ObjUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link WrappedAnnotationAttribute}的基本实现
 *
 * @author huangchengxing
 * @see ForceAliasedAnnotationAttribute
 * @see AliasedAnnotationAttribute
 * @see MirroredAnnotationAttribute
 */
public abstract class AbstractWrappedAnnotationAttribute implements WrappedAnnotationAttribute {

	protected final AnnotationAttribute original;
	protected final AnnotationAttribute linked;

	protected AbstractWrappedAnnotationAttribute(final AnnotationAttribute original, final AnnotationAttribute linked) {
		Assert.notNull(original, "target must not null");
		Assert.notNull(linked, "linked must not null");
		this.original = original;
		this.linked = linked;
	}

	@Override
	public AnnotationAttribute getOriginal() {
		return original;
	}

	@Override
	public AnnotationAttribute getLinked() {
		return linked;
	}

	@Override
	public AnnotationAttribute getNonWrappedOriginal() {
		AnnotationAttribute curr = null;
		AnnotationAttribute next = original;
		while (next != null) {
			curr = next;
			next = next.isWrapped() ? ((WrappedAnnotationAttribute) curr).getOriginal() : null;
		}
		return curr;
	}

	@Override
	public Collection<AnnotationAttribute> getAllLinkedNonWrappedAttributes() {
		final List<AnnotationAttribute> leafAttributes = new ArrayList<>();
		collectLeafAttribute(this, leafAttributes);
		return leafAttributes;
	}

	private void collectLeafAttribute(final AnnotationAttribute curr, final List<AnnotationAttribute> leafAttributes) {
		if (ObjUtil.isNull(curr)) {
			return;
		}
		if (!curr.isWrapped()) {
			leafAttributes.add(curr);
			return;
		}
		final WrappedAnnotationAttribute wrappedAttribute = (WrappedAnnotationAttribute) curr;
		collectLeafAttribute(wrappedAttribute.getOriginal(), leafAttributes);
		collectLeafAttribute(wrappedAttribute.getLinked(), leafAttributes);
	}

}
