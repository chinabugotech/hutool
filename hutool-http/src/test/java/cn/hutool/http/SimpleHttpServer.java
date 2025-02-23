package cn.hutool.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个简单的http服务器示例代码；
 * 用于打印出接收到的请求头和简单的post-json的请求体数据来测试
 *
 * @author ZhangWeinan
 * @date 2025/2/23 15:32
 */
public class SimpleHttpServer {

	public static void main(String[] args) throws IOException {
		int port = 8080;
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Server started on port " + port);

		while (true) {
			Socket clientSocket = serverSocket.accept();
			new Thread(() -> handleClient(clientSocket)).start();
		}
	}

	private static void handleClient(Socket clientSocket) {
		try (BufferedReader in = new BufferedReader(
			new InputStreamReader(clientSocket.getInputStream()));
			 OutputStream out = clientSocket.getOutputStream()) {
			String requestLine = in.readLine();
			if (requestLine == null) return;

			String[] parts = requestLine.split(" ");
			String method = parts[0];

			Map<String, String> headers = new HashMap<>();
			String line;
			System.out.println("\n=== Headers ===");
			while (!(line = in.readLine()).isEmpty()) {
				int colonIndex = line.indexOf(':');
				if (colonIndex > 0) {
					String key = line.substring(0, colonIndex).trim();
					String value = line.substring(colonIndex + 1).trim();
					headers.put(key, value);
					System.out.println(key + ": " + value);
				}
			}
			if ("POST".equalsIgnoreCase(method)) {
				int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
				if (contentLength > 0) {
					char[] body = new char[contentLength];
					in.read(body, 0, contentLength);
					String jsonBody = new String(body);
					System.out.println("\n=== JSON Body ===");
					System.out.println(jsonBody);
				}
			}
			String response = "HTTP/1.1 200 OK\r\n" +
				"Content-Type: text/plain\r\n" +
				"Content-Length: 16\r\n\r\n" +
				"Request received";
			out.write(response.getBytes(StandardCharsets.UTF_8));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
