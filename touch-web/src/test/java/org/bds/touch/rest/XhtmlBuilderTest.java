package org.bds.touch.rest;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Element;

public class XhtmlBuilderTest {
	TransformHelper testHelper = TransformHelper.getInstance();

	XhtmlCallback<Object,Object> cb_null = new XhtmlCallback<Object,Object>() {
		public Element buildHeaderPart(XhtmlBuilder<Object,Object> builder, Object header) {
			return null;
		}
		public Element buildItemPart(XhtmlBuilder<Object,Object> builder, Object item) {
			return null;
		}
	};
	XhtmlCallback<Object,Object> cb_test = new XhtmlCallback<Object,Object>() {
		public Element buildHeaderPart(XhtmlBuilder<Object,Object> builder, Object header) {
			return builder.createElement("HeadTag");
		}
		public Element buildItemPart(XhtmlBuilder<Object,Object> builder, Object item) {
			return builder.createElement("ChildTag");
		}
	};
	
	@BeforeClass
	public static void setup() {
	}
	
	@Test
	public void testXhtmlBuilder_simple() throws Exception {
		XhtmlBuilder<Object,Object> builder = new XhtmlBuilder<Object,Object>(cb_null);
		builder.setTitle("test title");
		DomRepresentation repr = builder.buildXhtml();		
		String result = testHelper.indentXml(repr.getDocument());
		assertEquals(
				testHelper.getExpectedString("/org/bds/touch/rest/xhtmlbuilder_expected_simple.txt"), result);
	}
	@Test
	public void testXhtmlBuilder_mainrep() throws Exception {
		XhtmlBuilder<Object,Object> builder = new XhtmlBuilder<Object,Object>(cb_test);
		builder.setTitle("test title");
		builder.setHeader(new Object());
		builder.setChildren(Arrays.asList(new Object(), new Object()));
		DomRepresentation repr = builder.buildXhtml();		
		String result = testHelper.indentXml(repr.getDocument());
		assertEquals(
				testHelper.getExpectedString("/org/bds/touch/rest/xhtmlbuilder_expected_children.txt"), result);
	}
	
	
}
