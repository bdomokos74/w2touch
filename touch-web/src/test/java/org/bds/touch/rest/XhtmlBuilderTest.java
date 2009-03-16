package org.bds.touch.rest;

import static junit.framework.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.resource.DomRepresentation;

public class XhtmlBuilderTest {
	TransformHelper testHelper = TransformHelper.getInstance();

	@BeforeClass
	public static void setup() {
	}
	
	@Test
	public void testXhtmlBuilder_simple() throws Exception {
		XhtmlBuilder builder = new XhtmlBuilder();
		builder.setTitle("test title");
		DomRepresentation repr = builder.buildXhtml();		
		String result = testHelper.indentXml(repr.getDocument());
		assertEquals(
				testHelper.getExpectedString("/org/bds/touch/rest/xhtmlbuilder_expected_simple.txt"), result);
	}
}
