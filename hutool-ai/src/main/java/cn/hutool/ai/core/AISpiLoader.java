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

package cn.hutool.ai.core;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ServiceLoaderUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * AI SPI 鍔犺浇宸ュ叿锛屽吋瀹?ContextClassLoader 鍜屾帴鍙ｅ畾涔夋墍鍦ㄧ殑 ClassLoader銆?
 *
 * @author Looly
 * @since 5.8.45
 */
public class AISpiLoader {

	private AISpiLoader() {
		// static utility class
	}

	/**
	 * 鎸夐『搴忓皾璇曞姞杞斤細褰撳墠绾跨▼ ContextClassLoader 浼樺厛锛屽叾娆℃槸 SPI 鎺ュ彛鎵€鍦?ClassLoader銆?
	 *
	 * @param type SPI 鎺ュ彛绫诲瀷
	 * @param <T>  SPI 瀹炵幇绫诲瀷
	 * @return 宸插姞杞界殑 SPI 瀹炵幇鍒楄〃
	 */
	public static <T> List<T> load(final Class<T> type) {
		final List<T> services = new ArrayList<>();
		final Set<ClassLoader> visited = Collections.newSetFromMap(new IdentityHashMap<ClassLoader, Boolean>());

		load(services, visited, type, ClassLoaderUtil.getContextClassLoader());
		load(services, visited, type, type.getClassLoader());

		return services;
	}

	private static <T> void load(final List<T> services, final Set<ClassLoader> visited,
								 final Class<T> type, final ClassLoader loader) {
		if (loader == null || false == visited.add(loader)) {
			return;
		}

		for (final T service : ServiceLoaderUtil.load(type, loader)) {
			services.add(service);
		}
	}
}
