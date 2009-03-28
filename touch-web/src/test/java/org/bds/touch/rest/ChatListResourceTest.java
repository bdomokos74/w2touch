package org.bds.touch.rest;

import static junit.framework.Assert.*;

import org.bds.touch.db.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Variant;

public class ChatListResourceTest {
	TransformHelper testHelper = TransformHelper.getInstance();
	
	@Before
	public void before() {
		ServiceLocator.setContext("/org/bds/touch/db/mocked-context.xml");	
	}
	
	@Test
	public void test_build_simple() throws Exception {
		ChatListResource userResource = (ChatListResource) ServiceLocator.getAppContext().getBean("chatlistresource_1");
		DomRepresentation repr = (DomRepresentation)userResource.represent(new Variant(MediaType.TEXT_XML));
		String result = testHelper.indentXml(repr.getDocument());
		assertEquals(testHelper.getExpectedString("/org/bds/touch/rest/chatlistresource_expected_1.txt"), result);
	}
}
