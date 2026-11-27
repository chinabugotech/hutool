package cn.hutool.core.bean;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.util.*;

/**
 * Bean路径表达式，用于获取多层嵌套Bean中的字段值或Bean对象<br>
 * 根据给定的表达式，查找Bean中对应的属性值对象。 表达式分为两种：
 * <ol>
 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
 * </ol>
 * <p>
 * 表达式栗子：
 *
 * <pre>
 * persion
 * persion.name
 * persons[3]
 * person.friends[5].name
 * ['person']['friends'][5]['name']
 * </pre>
 *
 * @author Looly
 * @since 4.0.6
 */
public class BeanPath implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 表达式边界符号数组
	 */
	private static final char[] EXP_CHARS = {CharUtil.DOT, CharUtil.BRACKET_START, CharUtil.BRACKET_END};

	private boolean isStartWith = false;
	protected List<String> patternParts;

	/**
	 * 解析Bean路径表达式为Bean模式<br>
	 * Bean表达式，用于获取多层嵌套Bean中的字段值或Bean对象<br>
	 * 根据给定的表达式，查找Bean中对应的属性值对象。 表达式分为两种：
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
	 * 表达式栗子：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * ['person']['friends'][5]['name']
	 * </pre>
	 *
	 * @param expression 表达式
	 * @return BeanPath
	 */
	public static BeanPath create(final String expression) {
		return new BeanPath(expression);
	}

	/**
	 * 构造
	 *
	 * @param expression 表达式
	 */
	public BeanPath(final String expression) {
		init(expression);
	}

	/**
	 * 获取表达式解析后的分段列表
	 *
	 * @return 表达式分段列表
	 */
	public List<String> getPatternParts() {
		return this.patternParts;
	}

	/**
	 * 获取Bean中对应表达式的值
	 *
	 * @param bean Bean对象或Map或List等
	 * @return 值，如果对应值不存在，则返回null
	 */
	public Object get(final Object bean) {
		return get(this.patternParts, bean, this.patternParts.size(), false);
	}

	/**
	 * 设置表达式指定位置（或field对应）的值<br>
	 * 若表达式指向一个List则设置其坐标对应位置的值，若指向Map则put对应key的值，Bean则设置字段的值<br>
	 * 注意：
	 *
	 * <pre>
	 * 1. 如果为List，如果下标不大于List长度，则替换原有值，否则追加值
	 * 2. 如果为数组，如果下标不大于数组长度，则替换原有值，否则追加值
	 * </pre>
	 *
	 * @param bean  Bean、Map或List
	 * @param value 值
	 */
	public void set(final Object bean, final Object value) {
		set(bean, this.patternParts, lastIsNumber(this.patternParts), value, canCreateParents(bean));
	}

	@Override
	public String toString() {
		return this.patternParts.toString();
	}

	//region Private Methods

	// 投影会生成临时集合，类名前缀可能重定向到根对象，两者都需要重新从根定位父节点。
	private boolean canCreateParents(Object bean) {
		for (String part : patternParts) {
			if ("*".equals(part) || part.indexOf(':') >= 0 || part.indexOf(',') >= 0 ||
					(!isStartWith && BeanUtil.isMatchName(bean, part, true))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置表达式指定位置（或field对应）的值<br>
	 * 若表达式指向一个List则设置其坐标对应位置的值，若指向Map则put对应key的值，Bean则设置字段的值<br>
	 * 注意：
	 *
	 * <pre>
	 * 1. 如果为List，如果下标不大于List长度，则替换原有值，否则追加值
	 * 2. 如果为数组，如果下标不大于数组长度，则替换原有值，否则追加值
	 * </pre>
	 *
	 * @param bean           Bean、Map或List
	 * @param patternParts   表达式块列表
	 * @param nextNumberPart 下一个路径段是否为下标
	 * @param createParents   是否允许沿单值路径补齐父节点
	 * @param value          值
	 */
	private void set(Object bean, List<String> patternParts, boolean nextNumberPart, Object value, boolean createParents) {
		Object subBean = this.get(patternParts, bean, patternParts.size() - 1, createParents);
		if (null == subBean) {
			// 当前节点是空，则先创建父节点
			final List<String> parentParts = getParentParts(patternParts);
			this.set(bean, parentParts, lastIsNumber(parentParts), nextNumberPart ? new ArrayList<>() : new HashMap<>(), createParents);
			//set中有可能做过转换，因此此处重新获取bean
			subBean = this.get(patternParts, bean, patternParts.size() - 1, createParents);
		}

		final Object newSubBean = BeanUtil.setFieldValue(subBean, patternParts.get(patternParts.size() - 1), value);
		if(newSubBean != subBean){
			// 对象变更，重新加入
			this.set(bean, getParentParts(patternParts), nextNumberPart, newSubBean, createParents);
		}
	}

	/**
	 * 判断path列表中末尾的标记是否为数字
	 *
	 * @param patternParts path列表
	 * @return 是否为数字
	 */
	private static boolean lastIsNumber(List<String> patternParts) {
		return isIndexPart(patternParts.get(patternParts.size() - 1));
	}

	private static boolean isIndexPart(String part) {
		if (StrUtil.isEmpty(part)) {
			return false;
		}
		// 属性名最为常见，避免为每个非数字名称在 parseInt 中创建异常。
		final char first = part.charAt(0);
		return (first == '+' || first == '-' || Character.isDigit(first)) && NumberUtil.isInteger(part);
	}

	/**
	 * 获取父级路径列表
	 *
	 * @param patternParts 路径列表
	 * @return 父级路径列表
	 */
	private static List<String> getParentParts(List<String> patternParts) {
		return patternParts.subList(0, patternParts.size() - 1);
	}

	/**
	 * 获取Bean中对应表达式的值
	 *
	 * @param patternParts 表达式分段列表
	 * @param bean         Bean对象或Map或List等
	 * @param length       需要访问的路径段个数
	 * @param createMissing 是否按需补齐写操作的中间节点
	 * @return 值，如果对应值不存在，则返回null
	 */
	private Object get(final List<String> patternParts, final Object bean, final int length, boolean createMissing) {
		Object subBean = bean;
		boolean isFirst = true;
		String patternPart;
		for (int i = 0; i < length; i++) {
			patternPart = patternParts.get(i);
			
			// 支持通配符 * 语法，用于获取集合或数组中所有元素的指定字段
			if ("*".equals(patternPart)) {
				// 如果当前对象是集合或数组，且后面还有路径部分，则展开处理
				if (i < length - 1 && (subBean instanceof Collection || ArrayUtil.isArray(subBean))) {
					return getByWildcard(subBean, patternParts, i + 1, length);
				}
				// 如果 * 是最后一个部分，直接返回当前集合/数组
				return subBean;
			}
			
			final Object parent = subBean;
			if (createMissing && (parent instanceof Collection || ArrayUtil.isArray(parent)) && !isIndexPart(patternPart)) {
				// 对整个集合取属性也会产生投影，写入必须保留原有的定位过程。
				createMissing = false;
			}
			subBean = getFieldValue(parent, patternPart);
			if (null == subBean) {
				// 支持表达式的第一个对象为Bean本身（若用户定义表达式$开头，则不做此操作）
				if (isFirst && false == this.isStartWith && BeanUtil.isMatchName(bean, patternPart, true)) {
					subBean = bean;
					isFirst = false;
				} else if (createMissing && parent != null && !ArrayUtil.isArray(parent)) {
					// 在当前父节点直接补齐下一层，重新读取以取得字段转换或自定义 put 后的实际对象。
					final Object child = isIndexPart(patternParts.get(i + 1)) ? new ArrayList<>() : new HashMap<>();
					BeanUtil.setFieldValue(parent, patternPart, child);
					subBean = getFieldValue(parent, patternPart);
					if (subBean == null) {
						return null;
					}
				} else {
					return null;
				}
			}
		}
		return subBean;
	}

	/**
	 * 处理通配符 * 的情况，对集合或数组中的每个元素应用剩余路径
	 *
	 * @param collectionOrArray 集合或数组对象
	 * @param patternParts      完整的表达式分段列表
	 * @param startIndex        剩余路径的起始索引
	 * @param endIndex          路径的结束索引
	 * @return 结果列表
	 */
	private Object getByWildcard(final Object collectionOrArray, final List<String> patternParts, 
	                              final int startIndex, final int endIndex) {
		final List<Object> results = new ArrayList<>();
		
		// 获取剩余的路径部分
		final List<String> remainingParts = patternParts.subList(startIndex, endIndex);
		
		if (collectionOrArray instanceof Collection) {
			// 处理集合
			for (Object item : (Collection<?>) collectionOrArray) {
				if (item != null) {
					final Object value = get(remainingParts, item, remainingParts.size(), false);
					if (value != null) {
						results.add(value);
					}
				}
			}
		} else if (ArrayUtil.isArray(collectionOrArray)) {
			// 处理数组
			final int arrayLength = ArrayUtil.length(collectionOrArray);
			for (int i = 0; i < arrayLength; i++) {
				final Object item = ArrayUtil.get(collectionOrArray, i);
				if (item != null) {
					final Object value = get(remainingParts, item, remainingParts.size(), false);
					if (value != null) {
						results.add(value);
					}
				}
			}
		}
		
		return results.isEmpty() ? null : results;
	}

	@SuppressWarnings("unchecked")
	private static Object getFieldValue(final Object bean, final String expression) {
		if (StrUtil.isBlank(expression)) {
			return null;
		}

		if (StrUtil.contains(expression, ':')) {
			// [start:end:step] 模式
			final List<String> parts = StrUtil.splitTrim(expression, ':');
			final int start = Integer.parseInt(parts.get(0));
			final int end = Integer.parseInt(parts.get(1));
			int step = 1;
			if (3 == parts.size()) {
				step = Integer.parseInt(parts.get(2));
			}
			if (bean instanceof Collection) {
				return CollUtil.sub((Collection<?>) bean, start, end, step);
			} else if (ArrayUtil.isArray(bean)) {
				return ArrayUtil.sub(bean, start, end, step);
			}
		} else if (StrUtil.contains(expression, ',')) {
			// [num0,num1,num2...]模式或者['key0','key1']模式
			final List<String> keys = StrUtil.splitTrim(expression, ',');
			if (bean instanceof Collection) {
				return CollUtil.getAny((Collection<?>) bean, Convert.convert(int[].class, keys));
			} else if (ArrayUtil.isArray(bean)) {
				return ArrayUtil.getAny(bean, Convert.convert(int[].class, keys));
			} else {
				final String[] unWrappedKeys = new String[keys.size()];
				for (int i = 0; i < unWrappedKeys.length; i++) {
					unWrappedKeys[i] = StrUtil.unWrap(keys.get(i), '\'');
				}
				if (bean instanceof Map) {
					// 只支持String为key的Map
					return MapUtil.getAny((Map<String, ?>) bean, unWrappedKeys);
				} else {
					final Map<String, Object> map = BeanUtil.beanToMap(bean);
					return MapUtil.getAny(map, unWrappedKeys);
				}
			}
		} else {
			// 数字或普通字符串
			return BeanUtil.getFieldValue(bean, expression);
		}

		return null;
	}

	/**
	 * 初始化
	 *
	 * @param expression 表达式
	 */
	private void init(final String expression) {
		final List<String> localPatternParts = new ArrayList<>();
		final int length = expression.length();

		final StringBuilder builder = new StringBuilder();
		char c;
		boolean isNumStart = false;// 下标标识符开始
		boolean isInWrap = false; //标识是否在引号内
		for (int i = 0; i < length; i++) {
			c = expression.charAt(i);
			if (0 == i && '$' == c) {
				// 忽略开头的$符，表示当前对象
				isStartWith = true;
				continue;
			}

			if ('\'' == c) {
				// 结束
				isInWrap = (false == isInWrap);
				continue;
			}

			if (false == isInWrap && ArrayUtil.contains(EXP_CHARS, c)) {
				// 处理边界符号
				if (CharUtil.BRACKET_END == c) {
					// 中括号（数字下标）结束
					if (false == isNumStart) {
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find ']' but no '[' !", expression, i));
					}
					isNumStart = false;
					// 中括号结束加入下标
				} else {
					if (isNumStart) {
						// 非结束中括号情况下发现起始中括号报错（中括号未关闭）
						throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, i));
					} else if (CharUtil.BRACKET_START == c) {
						// 数字下标开始
						isNumStart = true;
					}
					// 每一个边界符之前的表达式是一个完整的KEY，开始处理KEY
				}
				if (builder.length() > 0) {
					localPatternParts.add(builder.toString());
				}
				builder.setLength(0);
			} else {
				// 非边界符号，追加字符
				builder.append(c);
			}
		}

		// 末尾边界符检查
		if (isNumStart) {
			throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, length - 1));
		} else {
			if (builder.length() > 0) {
				localPatternParts.add(builder.toString());
			}
		}

		// 不可变List
		this.patternParts = ListUtil.unmodifiable(localPatternParts);
	}
	//endregion
}
