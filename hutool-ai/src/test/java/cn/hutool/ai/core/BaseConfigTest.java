package cn.hutool.ai.core;

import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;

import static org.junit.jupiter.api.Assertions.*;

class BaseConfigTest {

	@Test
	void setProxyShouldSetHasProxyTrue() {
		BaseConfig config = new BaseConfig();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080));
		config.setProxy(proxy);
		assertTrue(config.getHasProxy(), "setProxy should automatically set hasProxy to true");
		assertSame(proxy, config.getProxy());
	}

	@Test
	void setNullProxyShouldClearHasProxy() {
		BaseConfig config = new BaseConfig();
		config.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080)));
		config.setProxy(null);
		assertNull(config.getProxy());
		assertFalse(config.getHasProxy(), "setProxy(null) should clear hasProxy");
	}
}
