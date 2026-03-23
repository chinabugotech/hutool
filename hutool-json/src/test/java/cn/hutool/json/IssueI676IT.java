package cn.hutool.json;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.xml.JSONXMLSerializer;
import org.junit.jupiter.api.Test;

import static javax.xml.xpath.XPathConstants.STRING;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueI676IT {

	@Test
	public void parseXMLTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.readUtf8Str("IssueI676IT.json"));
		String xmlStr = JSONXMLSerializer.toXml(jsonObject, null, (String) null);
		String content = String.valueOf(XmlUtil.getByXPath("/page/orderItems[1]/content", XmlUtil.readXML(xmlStr), STRING));
		assertEquals("bar1", content);
	}
}
