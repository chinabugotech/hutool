package cn.hutool.extra.expression.engine.qlexpress;

import cn.hutool.extra.expression.ExpressionEngine;
import cn.hutool.extra.expression.ExpressionException;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.config.QLExpressRunStrategy;

import javax.naming.InitialContext;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * QLExpress引擎封装<br>
 * 见：https://github.com/alibaba/QLExpress
 *
 * @author looly
 * @since 5.8.9
 */
public class QLExpressEngine implements ExpressionEngine {

	private final ExpressRunner engine;

	/**
	 * 构造
	 */
	public QLExpressEngine() {
		engine = new ExpressRunner();

		// issue#3994@Github
		// Enforce blacklisting of high-risk method invocations
		QLExpressRunStrategy.setForbidInvokeSecurityRiskMethods(true);
		// Explicitly forbid JNDI lookup calls through InitialContext
		QLExpressRunStrategy.addSecurityRiskMethod(InitialContext.class, "doLookup");

		// The method blacklist above does not cover object creation, so also enforce the
		// constructor blacklist; otherwise new FileOutputStream(...) can drop a payload that
		// System.load then runs as native code
		QLExpressRunStrategy.setForbidInvokeSecurityRiskConstructors(true);
		// High-risk methods missing from the default blacklist
		QLExpressRunStrategy.addSecurityRiskMethod(System.class, "load");
		QLExpressRunStrategy.addSecurityRiskMethod(System.class, "loadLibrary");
		QLExpressRunStrategy.addSecurityRiskMethod(System.class, "setProperty");
		QLExpressRunStrategy.addSecurityRiskMethod(Runtime.class, "load");
		QLExpressRunStrategy.addSecurityRiskMethod(Runtime.class, "loadLibrary");
		// High-risk file read/write constructors
		QLExpressRunStrategy.addRiskSecureConstructor(FileOutputStream.class);
		QLExpressRunStrategy.addRiskSecureConstructor(FileWriter.class);
		QLExpressRunStrategy.addRiskSecureConstructor(RandomAccessFile.class);
		QLExpressRunStrategy.addRiskSecureConstructor(FileInputStream.class);
		QLExpressRunStrategy.addRiskSecureConstructor(FileReader.class);
	}

	@Override
	public Object eval(final String expression, final Map<String, Object> context, Collection<Class<?>> allowClassSet) {
		// issue#3994@Github
		if (null != allowClassSet) {
			for (Class<?> clazz : allowClassSet) {
				for (Method method : clazz.getDeclaredMethods()) {
					QLExpressRunStrategy.addSecureMethod(clazz, method.getName());
				}
			}
		}
		final DefaultContext<String, Object> defaultContext = new DefaultContext<>();
		defaultContext.putAll(context);
		try {
			return engine.execute(expression, defaultContext, null, true, false);
		} catch (final Exception e) {
			throw new ExpressionException(e);
		}
	}
}
