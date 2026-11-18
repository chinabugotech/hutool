package cn.hutool.core.img.gif;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GifDecoderTest {

	@Test
	public void readPreservesUrlPathCaseTest() throws IOException {
		final byte[] gif = Base64.getDecoder().decode("R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==");
		final HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		server.createContext("/", exchange -> {
			if ("/UpperCase.gif".equals(exchange.getRequestURI().getPath())) {
				exchange.sendResponseHeaders(200, gif.length);
				try (OutputStream out = exchange.getResponseBody()) {
					out.write(gif);
				}
			} else {
				exchange.sendResponseHeaders(404, -1);
				exchange.close();
			}
		});
		server.start();
		try {
			final String url = "http://127.0.0.1:" + server.getAddress().getPort() + "/UpperCase.gif";
			final GifDecoder decoder = new GifDecoder();
			assertEquals(GifDecoder.STATUS_OK, decoder.read(url));
			assertEquals(1, decoder.getFrameCount());
		} finally {
			server.stop(0);
		}
	}
}
