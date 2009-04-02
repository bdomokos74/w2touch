package org.bds.touch.rest;

import static junit.framework.Assert.*;
import static org.junit.Assert.*;

import java.util.List;
import org.bds.touch.db.ServiceLocator;
import org.bds.touch.model.Post;
import org.bds.touch.rest.AbstractResource.FormFactory;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;

public class ChatResourceTest {
	TransformHelper testHelper = TransformHelper.getInstance();
	
	@Before
	public void before() {
		ServiceLocator.setContext("/org/bds/touch/db/chatresource-test-context.xml");	
	}
	
	@Test
	public void testRepresent() throws Exception {
		ChatResource chatResource = (ChatResource) ServiceLocator.getAppContext().getBean("chatresource_1");
		DomRepresentation repr = (DomRepresentation)chatResource.represent(new Variant(MediaType.TEXT_XML));
		String result = testHelper.indentXml(repr.getDocument());
		assertEquals(testHelper.getExpectedString("/org/bds/touch/rest/chatresource_expected_1.txt"), result);
	}
	
	@Test
	public void testAcceptRepresentation() throws Exception {
		ChatResource chatResource = (ChatResource) ServiceLocator.getAppContext().getBean("chatresource_2");
		chatResource.setFormFactory(new FormFactory() {
			public Form getForm(Representation repr) {
				Form form = new Form();
				form.add(new Parameter("dir", "0"));
				form.add(new Parameter("text", "hello1"));
				return form;
			}
		});
		chatResource.acceptRepresentation(Representation.createEmpty());
		List<Post> posts = ServiceLocator.getPostDao().findAllPostsByChatId(1);
		assertEquals(1, posts.size());
		assertEquals("hello1", posts.get(0).getText());
		assertEquals(Status.SUCCESS_CREATED.getCode(), chatResource.getResponse().getStatus().getCode());
	}
}
