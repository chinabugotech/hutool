package cn.hutool.extra.expression.engine.mvel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.expression.ExpressionEngine;
import cn.hutool.extra.expression.ExpressionException;
import org.mvel2.MVEL;

import java.util.Collection;
import java.util.Map;

/**
 * MVEL (MVFLEX Expression Language)引擎封装<br>
 * 见：https://github.com/mvel/mvel
 *
 * @since 5.5.0
 * @author looly
 */
public class MvelEngine implements ExpressionEngine {

	static {
		checkEngineExist(MVEL.class);
	}

	/**
	 * 构造
	 */
	public MvelEngine(){
	}

	@Override
	public Object eval(String expression, Map<String, Object> context, Collection<Class<?>> allowClassSet) {

		// issue#4249 检查context的value类型是否在白名单中，不在则抛出异常
		if(CollUtil.isNotEmpty(allowClassSet)){
			context.values().forEach(value -> {
				if(!allowClassSet.contains(value.getClass())){
					throw new ExpressionException("Value type [{}] is not in allowClassSet [{}]", value.getClass(), allowClassSet);
				}
			});
		}

		return MVEL.eval(expression, context);
	}

	private static void checkEngineExist(Class<?> clazz){
		// do nothing
	}
}
