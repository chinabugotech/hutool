package cn.hutool.v7.core.compiler;

import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.reflect.ConstructorUtil;
import cn.hutool.v7.core.compress.ZipUtil;

import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
/**
 * Java源码编译器测试
 *
 * @author lzpeng
 */
public class JavaSourceCompilerTest {

	/**
	 * 测试编译Java源码
	 */
	@Test
	public void testCompile() throws ClassNotFoundException {
		// 依赖A，编译B和C
		final File libFile = ZipUtil.zip(FileUtil.file("lib.jar"),
				new String[]{"A.class", "A$InnerClass.class"},
				new InputStream[]{
						FileUtil.getInputStream("test-compile/A.class"),
						FileUtil.getInputStream("test-compile/A$InnerClass.class")
				});
		final ClassLoader classLoader = CompilerUtil.getCompiler(null)
				.addSource(FileUtil.file("test-compile/B.java"))
				.addSource("C", FileUtil.readUtf8String("test-compile/C.java"))
				.addLibrary(libFile)
				.compile();
		final Class<?> clazz = classLoader.loadClass("C");
		final Object obj = ConstructorUtil.newInstance(clazz);
		Assertions.assertTrue(String.valueOf(obj).startsWith("C["));
	}

	@Test
	public void testErrorCompile() {
		Exception exception = null;
		try {
			CompilerUtil.getCompiler(null)
					.addSource(FileUtil.file("test-compile/ErrorClazz.java"))
					.compile();
		} catch (final Exception ex) {
			exception = ex;
		} finally {
			Assertions.assertTrue(exception instanceof CompilerException);
		}
	}
}
