package cn.hutool.v7.http;

import cn.hutool.v7.core.io.file.FileUtil;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.map.MapUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssueIDDHNSTest {

	@Test
	@Disabled
	void postTest(){
		final Map<String, Object> map = MapUtil.ofKvs(false,
			"file", FileUtil.file("bd.jpg"),
			"path", "/opt/B",
			"ojb", "{\"test\":\"test\"}"
		);

		final String post = HttpUtil.post("http://127.0.0.1:8080/upload", map);
		Console.log(post);
	}
}
