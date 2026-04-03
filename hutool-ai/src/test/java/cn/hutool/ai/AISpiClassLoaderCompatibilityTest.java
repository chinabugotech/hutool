package cn.hutool.ai;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AISpiClassLoaderCompatibilityTest {

	@Test
	void aiServiceFactoryFallsBackToModuleClassLoaderWhenContextClassLoaderCannotSeeSpi() throws Exception {
		try (final URLClassLoader isolatedLoader = new URLClassLoader(currentClasspathUrls(), null);
			 final URLClassLoader hiddenContextLoader = new URLClassLoader(new URL[0], null)) {
			final Thread thread = Thread.currentThread();
			final ClassLoader originalContextLoader = thread.getContextClassLoader();
			try {
				thread.setContextClassLoader(hiddenContextLoader);

				final Class<?> builderClass = Class.forName("cn.hutool.ai.core.AIConfigBuilder", true, isolatedLoader);
				final Object builder = builderClass.getConstructor(String.class).newInstance("openai");
				builderClass.getMethod("setApiKey", String.class).invoke(builder, "test-key");
				final Object config = builderClass.getMethod("build").invoke(builder);

				final Class<?> factoryClass = Class.forName("cn.hutool.ai.AIServiceFactory", true, isolatedLoader);
				final Class<?> aiConfigClass = Class.forName("cn.hutool.ai.core.AIConfig", true, isolatedLoader);
				final Method getAIService = factoryClass.getMethod("getAIService", aiConfigClass);
				final Object service = getAIService.invoke(null, config);

				assertNotNull(service);
				assertEquals("cn.hutool.ai.model.openai.OpenaiServiceImpl", service.getClass().getName());
			} finally {
				thread.setContextClassLoader(originalContextLoader);
			}
		}
	}

	@Test
	void aiConfigRegistryFallsBackToModuleClassLoaderWhenContextClassLoaderCannotSeeSpi() throws Exception {
		try (final URLClassLoader isolatedLoader = new URLClassLoader(currentClasspathUrls(), null);
			 final URLClassLoader hiddenContextLoader = new URLClassLoader(new URL[0], null)) {
			final Thread thread = Thread.currentThread();
			final ClassLoader originalContextLoader = thread.getContextClassLoader();
			try {
				thread.setContextClassLoader(hiddenContextLoader);

				final Class<?> registryClass = Class.forName("cn.hutool.ai.core.AIConfigRegistry", true, isolatedLoader);
				final Method getConfigClass = registryClass.getMethod("getConfigClass", String.class);
				final Class<?> configClass = (Class<?>) getConfigClass.invoke(null, "openai");

				assertNotNull(configClass);
				assertEquals("cn.hutool.ai.model.openai.OpenaiConfig", configClass.getName());
			} finally {
				thread.setContextClassLoader(originalContextLoader);
			}
		}
	}

	private static URL[] currentClasspathUrls() throws Exception {
		return Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator))
				.filter(path -> false == path.isEmpty())
				.flatMap(AISpiClassLoaderCompatibilityTest::toUrlStream)
				.toArray(URL[]::new);
	}

	private static Stream<URL> toUrlStream(final String path) {
		try {
			return Stream.of(Paths.get(path).toUri().toURL());
		} catch (final Exception e) {
			throw new IllegalStateException("Failed to convert classpath entry to URL: " + path, e);
		}
	}
}
