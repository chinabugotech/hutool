package cn.hutool.extra.expression.engine.rhino;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.expression.ExpressionEngine;
import cn.hutool.extra.expression.ExpressionException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Collection;
import java.util.Map;

/**
 * rhino引擎封装<br>
 * 见：https://github.com/mozilla/rhino
 *
 * @author lzpeng
 * @since 5.5.2
 */
public class RhinoEngine implements ExpressionEngine {

	static {
		checkEngineExist(Context.class);
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

		final Context ctx = Context.enter();
		final Scriptable scope = ctx.initStandardObjects();
		if (MapUtil.isNotEmpty(context)) {
			context.forEach((key, value)->{
				// 将java对象转为js对象后放置于JS的作用域中
				ScriptableObject.putProperty(scope, key, Context.javaToJS(value, scope));
			});
		}
		final Object result = ctx.evaluateString(scope, expression, "rhino.js", 1, null);
		Context.exit();
		return result;
	}

	private static void checkEngineExist(Class<?> clazz){
		// do nothing
	}
}
