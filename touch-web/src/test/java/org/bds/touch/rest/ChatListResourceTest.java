package org.bds.touch.rest;

import static junit.framework.Assert.*;

import org.bds.touch.db.ServiceLocator;
import org.bds.touch.model.Chat;
import org.bds.touch.rest.AbstractResource.FormFactory;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;

public class ChatListResourceTest {
	TransformHelper testHelper = TransformHelper.getInstance();
	
	@Before
	public void before() {
		ServiceLocator.setContext("/org/bds/touch/db/chatlistresource-test-context.xml");	
	}
	
	@Test
	public void testRepresent() throws Exception {
		ChatListResource userResource = (ChatListResource) ServiceLocator.getAppContext().getBean("chatlistresource_1");
		DomRepresentation repr = (DomRepresentation)userResource.represent(new Variant(MediaType.TEXT_XML));
		String result = testHelper.indentXml(repr.getDocument());
		assertEquals(testHelper.getExpectedString("/org/bds/touch/rest/chatlistresource_expected_1.txt"), result);
	}

	@Test
	public void testAcceptRepresentation() throws Exception {
		ChatListResource chatListResource = (ChatListResource) ServiceLocator.getAppContext().getBean("chatlistresource_1");
		chatListResource.setFormFactory(new FormFactory() {
			public Form getForm(Representation repr) {
				Form form = new Form();
				form.add(new Parameter("chatName", "new_chat_1"));
				form.add(new Parameter("partyName", "party_1"));
				return form;
			}
		});
		chatListResource.acceptRepresentation(Representation.createEmpty());
		
		Chat chat = ServiceLocator.getChatDao().findChatById(4);
		assertNotNull(chat);
		assertEquals("new_chat_1", chat.getChatName());
	}
}

