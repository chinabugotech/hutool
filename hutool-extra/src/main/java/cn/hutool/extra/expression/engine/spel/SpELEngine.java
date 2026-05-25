package cn.hutool.extra.expression.engine.spel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.expression.ExpressionEngine;
import cn.hutool.extra.expression.ExpressionException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.util.Collection;
import java.util.Map;

/**
 * Spring-Expression引擎封装<br>
 * 见：https://github.com/spring-projects/spring-framework/tree/master/spring-expression
 *
 * @since 5.5.0
 * @author looly
 */
public class SpELEngine implements ExpressionEngine {

	private final ExpressionParser parser;

	/**
	 * 构造
	 */
	public SpELEngine(){
		parser = new SpelExpressionParser();
	}

	@Override
	public Object eval(String expression, Map<String, Object> context, Collection<Class<?>> allowClassSet) {
		// final EvaluationContext evaluationContext = new StandardEvaluationContext();

		// issue#4249 检查context的value类型是否在白名单中，不在则抛出异常
		if(CollUtil.isNotEmpty(allowClassSet)){
			context.values().forEach(value -> {
				if(!allowClassSet.contains(value.getClass())){
					throw new ExpressionException("Value type [{}] is not in allowClassSet [{}]", value.getClass(), allowClassSet);
				}
			});
		}

		EvaluationContext evaluationContext = SimpleEvaluationContext
			.forReadOnlyDataBinding()
			.withInstanceMethods() // 仅允许调用白名单类的实例方法
			.build();

		context.forEach(evaluationContext::setVariable);
		return parser.parseExpression(expression).getValue(evaluationContext);
	}
}
