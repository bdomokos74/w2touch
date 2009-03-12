package org.bds.touch.rest;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.bds.touch.model.User;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserListResource extends Resource implements XhtmlInvoker<User> {

	public UserListResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;

		XhtmlListBuilder<User> builder = new XhtmlListBuilder<User>(this);
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			try {
				repr = builder.buildXhtml();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return repr;
	}

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		Form form = new Form(entity);
		System.out.println("form="+form);
		String name = form.getFirstValue("name");
		String pw = form.getFirstValue("pw");

		if (name == null) {
			generateErrorRepresentation("User " + name + " already exists.",
					"1", getResponse());
		}

		User user = ((ChatApplication) getApplication()).getUserDao()
				.createUser(name, pw);

		getResponse().setStatus(Status.SUCCESS_CREATED);
		Representation rep = new StringRepresentation("Item created "+user.getId(),
				MediaType.TEXT_PLAIN);
		rep.setIdentifier(getRequest().getResourceRef().getIdentifier() + "/"
				+ user.getId());
		getResponse().setEntity(rep);

	}

	private void generateErrorRepresentation(String errorMessage,
			String errorCode, Response response) {
		// This is an error
		response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		// Generate the output representation
		try {
			DomRepresentation representation = new DomRepresentation(
					MediaType.TEXT_XML);
			// Generate a DOM document representing the list of
			// items.
			Document d = representation.getDocument();

			Element eltError = d.createElement("error");

			Element eltCode = d.createElement("code");
			eltCode.appendChild(d.createTextNode(errorCode));
			eltError.appendChild(eltCode);

			Element eltMessage = d.createElement("message");
			eltMessage.appendChild(d.createTextNode(errorMessage));
			eltError.appendChild(eltMessage);

			response.setEntity(representation);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getTitle() {
		return "User list";
	}

	public String getLink(User u) {
		Reference baseRef = getRequest().getResourceRef().getBaseRef();
		return baseRef + "/" + u.getId();
	}

	public List<User> getList() {
		List<User> persistedUsers = ((ChatApplication) getApplication())
				.getUserDao().findUsers();
		return persistedUsers;
	}

	public String getText(User u) {
		return u.getName();
	}

}
