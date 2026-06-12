package cn.hutool.extra.expression;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.expression.engine.jexl.JexlEngine;
import cn.hutool.extra.expression.engine.jfireel.JfireELEngine;
import cn.hutool.extra.expression.engine.mvel.MvelEngine;
import cn.hutool.extra.expression.engine.qlexpress.QLExpressEngine;
import cn.hutool.extra.expression.engine.rhino.RhinoEngine;
import cn.hutool.extra.expression.engine.spel.SpELEngine;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ExpressionUtilTest {

	@Test
	public void evalTest(){
		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = ExpressionUtil.eval("a-(b-c)", dict);
		assertEquals(-143.8, (double)eval, 0);
	}

	@Test
	public void jexlTest(){
		final ExpressionEngine engine = new JexlEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("a-(b-c)", dict, null);
		assertEquals(-143.8, (double)eval, 0);
	}

	@Test
	public void jexlScriptTest(){
		final ExpressionEngine engine = new JexlEngine();

		final String exps2="if(a>0){return 100;}";
		final Map<String,Object> map2=new HashMap<>();
		map2.put("a", 1);
		final Object eval1 = engine.eval(exps2, map2, null);
		assertEquals(100, eval1);
	}

	@Test
	public void mvelTest(){
		final ExpressionEngine engine = new MvelEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("a-(b-c)", dict, null);
		assertEquals(-143.8, (double)eval, 0);
	}

	@Test
	public void jfireELTest(){
		final ExpressionEngine engine = new JfireELEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("a-(b-c)", dict, null);
		assertEquals(-143.8, (double)eval, 0);
	}

	@Test
	public void spELTest(){
		final ExpressionEngine engine = new SpELEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("#a-(#b-#c)", dict, null);
		assertEquals(-143.8, (double)eval, 0);
	}

	@Test
	public void rhinoTest(){
		final ExpressionEngine engine = new RhinoEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("a-(b-c)", dict, null);
		assertEquals(-143.8, (double)eval, 0);
	}

	@Test
	public void qlExpressTest(){
		final ExpressionEngine engine = new QLExpressEngine();

		final Dict dict = Dict.of()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("a-(b-c)", dict, null);
		assertEquals(-143.8, (double)eval, 0);
	}

	@Test
	public void qlExpressSecurityTest(){
		// https://github.com/chinabugotech/hutool/issues/4270
		// 默认黑名单模式下，高危的构造方法与native库加载等不能通过表达式调用
		final ExpressionEngine engine = new QLExpressEngine();
		final Dict context = Dict.of();

		assertThrows(ExpressionException.class,
				() -> engine.eval("new java.io.FileOutputStream(\"/tmp/hutool_test\")", context, null));
		assertThrows(ExpressionException.class,
				() -> engine.eval("System.load(\"/tmp/hutool_test.so\")", context, null));
		assertThrows(ExpressionException.class,
				() -> engine.eval("System.setProperty(\"k\", \"v\")", context, null));
		assertThrows(ExpressionException.class,
				() -> engine.eval("new java.io.RandomAccessFile(\"/tmp/hutool_test\", \"rw\")", context, null));

		// 普通表达式不受影响
		assertEquals(7, engine.eval("1+2*3", context, null));
	}

}
