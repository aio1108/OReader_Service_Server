package tw.com.useful.test;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.junit.Assert;
import org.junit.Test;

import tw.com.useful.runner.util.LsUtil;
import tw.com.useful.runner.util.RmJDomUtil;
import junit.framework.TestCase;

public class LsUtilsTest extends TestCase {
	
	@Test
	public void testTransform(){
		String xmlpath = "/Users/chenwei/Documents/DS_Home/action_xsl/test.xml";
		Document doc;
		try {
			doc = RmJDomUtil.buildXml(new File(xmlpath));
			final String transformedXML = LsUtil.transform(doc, "/Users/chenwei/Documents/DS_Home/action_xsl/business_indicator.xsl");
			String test = transformedXML;
		} catch (JDOMException e) {
			Assert.fail();
		} catch (IOException e) {
			Assert.fail();
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
