package cn.hutool.core.io;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class Issue3846Test {
	@Test
	@Disabled
	void readBytesTest() {
		final StopWatch stopWatch = DateUtil.createStopWatch();
		stopWatch.start();
		final String filePath = "d:/test/issue3846.data";
		final byte[] bytes = IoUtil.readBytes(ResourceUtil.getStream(filePath), false);
		stopWatch.stop();
		Console.log(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
	}
}
