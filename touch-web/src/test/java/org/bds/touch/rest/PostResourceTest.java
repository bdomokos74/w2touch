package org.bds.touch.rest;

import static junit.framework.Assert.*;

import org.bds.touch.db.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

public class PostResourceTest {
	TransformHelper testHelper = TransformHelper.getInstance();
	
	@Before
	public void before() {
		ServiceLocator.setContext("/org/bds/touch/db/postresource-test-context.xml");	
	}
	
	@Test
	public void test_post_ok() throws Exception {
		PostResource postResource = (PostResource) ServiceLocator.getAppContext().getBean("postresource_OK");
		DomRepresentation repr = (DomRepresentation)postResource.represent(new Variant(MediaType.TEXT_XML));
		String result = testHelper.indentXml(repr.getDocument());
		assertEquals(testHelper.getExpectedString("/org/bds/touch/rest/postresource_expected_1.txt"), result);
	}
	@Test
	public void test_post_accessdenied() throws Exception {
		PostResource postResource = (PostResource) ServiceLocator.getAppContext().getBean("postresource_DENIED");
		try {
			DomRepresentation repr = (DomRepresentation)postResource.represent(new Variant(MediaType.TEXT_XML));
			fail("Access denied expected");
		} catch(ResourceException e) {
			assertEquals(Status.CLIENT_ERROR_UNAUTHORIZED, e.getStatus());
		}
	}
}
