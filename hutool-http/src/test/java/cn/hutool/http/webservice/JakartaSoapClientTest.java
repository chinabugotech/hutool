package cn.hutool.http.webservice;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * SOAP相关单元测试
 *
 * @author looly
 *
 */
public class JakartaSoapClientTest {

	@Test
	@Disabled
	public void requestTest() {
		JakartaSoapClient client = JakartaSoapClient.create("http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx")
		.setMethod("web:getCountryCityByIp", "http://WebXml.com.cn/")
		.setCharset(CharsetUtil.CHARSET_GBK)
		.setParam("theIpAddress", "218.21.240.106");

		Console.log(client.getMsgStr(true));

		Console.log(client.send(true));
	}

	@Test
	@Disabled
	public void requestForMessageTest() throws SOAPException {
		JakartaSoapClient client = JakartaSoapClient.create("http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx")
				.setMethod("web:getCountryCityByIp", "http://WebXml.com.cn/")
				.setParam("theIpAddress", "218.21.240.106");

		SOAPMessage message = client.sendForMessage();
		Console.log(message.getSOAPBody().getTextContent());
	}
}
