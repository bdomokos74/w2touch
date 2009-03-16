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

public class UserListResource extends Resource implements XhtmlCallback<User> {
	
	public UserListResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public String getTitle() {
		return "User list";
	}

	public String getUserLink(User u) {
		Reference baseRef = getRequest().getResourceRef().getBaseRef();
		return baseRef + "/" + u.getId();
	}

	public List<User> getList() {
		List<User> persistedUsers = ((ChatApplication) getApplication())
				.getUserDao().findUsers();
		return persistedUsers;
	}
	
	public Element buildHeaderPart(XhtmlBuilder builder) {
		return null;
	}

	public Element buildItemPart(XhtmlBuilder builder, User u) {
		Element liElement = builder.getDoc().createElement("li");
		Element aElement = builder.addNewElement(liElement, "a");
		aElement.setAttribute("href", getUserLink(u));
		aElement.setTextContent(u.getName());
		return liElement;
	}
	
	public String getResourceClass() {
		return "users";
	}
	
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;

		XhtmlListBuilder<User> builder = new XhtmlListBuilder<User>(this);
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			try {
				repr = builder.buildXhtml();
			} catch (ParserConfigurationException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage());
			} catch (IOException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e.getMessage());
			}
		}
		return repr;
	}

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		Form form = new Form(entity);
		String name = form.getFirstValue("name");
		String pw = form.getFirstValue("pw");

		if (name == null) {
			XhtmlBuilder builder = new XhtmlBuilder();
			getResponse().setEntity(builder.buildErrorRepr(1, "Missing parameters"));
			return;
		}

		User user = ((ChatApplication) getApplication()).getUserDao()
				.createUser(name, pw);

		getResponse().setStatus(Status.SUCCESS_CREATED);
		Representation rep = new StringRepresentation("Item created "+user.getId()+"\n",
				MediaType.TEXT_PLAIN);
		rep.setIdentifier(getRequest().getResourceRef().getIdentifier() + "/"
				+ user.getId());
		getResponse().setEntity(rep);

	}

	
}
