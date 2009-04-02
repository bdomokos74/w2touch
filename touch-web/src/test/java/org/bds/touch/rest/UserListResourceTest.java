package org.bds.touch.rest;

import static junit.framework.Assert.*;

import org.bds.touch.db.ServiceLocator;
import org.bds.touch.model.User;
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

public class UserListResourceTest {
	TransformHelper testHelper = TransformHelper.getInstance();
	
	@Before
	public void before() {
		ServiceLocator.setContext("/org/bds/touch/db/userlistresource-test-context.xml");	
	}
	
	@Test
	public void testRepresent_1() throws Exception {
		UserListResource userListResource = (UserListResource) ServiceLocator.getAppContext().getBean("userlistresource_1");
		DomRepresentation repr = (DomRepresentation)userListResource.represent(new Variant(MediaType.TEXT_XML));
		String result = testHelper.indentXml(repr.getDocument());
		assertEquals(testHelper.getExpectedString("/org/bds/touch/rest/userlistresource_expected_1.txt"), result);
	}
	
	@Test
	public void testAcceptRepresentation() throws Exception {
		UserListResource userListResource = (UserListResource) ServiceLocator.getAppContext().getBean("userlistresource_1");
		userListResource.setFormFactory(new FormFactory() {
			public Form getForm(Representation repr) {
				Form f = new Form();
				f.add(new Parameter("name", "uname_test"));
				f.add(new Parameter("pw", "ppp"));
				return f;
			}
		});
		userListResource.acceptRepresentation(Representation.createEmpty());
		User user = ServiceLocator.getUserDao().findUserByName("uname_test");
		assertNotNull(user);
		assertEquals(Status.SUCCESS_CREATED, userListResource.getResponse().getStatus());
		assertEquals("http://www.test.org/users/3", userListResource.getResponse().getEntity().getIdentifier().toString());
	}
}
