package org.bds.touch.rest;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.bds.touch.db.ServiceLocator;
import org.bds.touch.model.User;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.w3c.dom.Element;

public class UserListResource extends AbstractResource implements XhtmlCallback<Object,User> {
	
	public UserListResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public String getTitle() {
		return "User list";
	}

	public List<User> getUserList() {
		List<User> persistedUsers = ServiceLocator.getUserDao().findUsers();
		return persistedUsers;
	}
		
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;

		XhtmlBuilder<Object, User> builder = new XhtmlBuilder<Object, User>(this);
		builder.setChildren(getUserList());
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
			XhtmlBuilder<Object, User> builder = new XhtmlBuilder<Object, User>(this);
			getResponse().setEntity(builder.buildErrorRepr(1, "Missing parameters"));
			return;
		}

		User user = ServiceLocator.getUserDao().createUser(name, pw);

		getResponse().setStatus(Status.SUCCESS_CREATED);
		Representation rep = new StringRepresentation("Item created "+user.getId()+"\n",
				MediaType.TEXT_PLAIN);
		rep.setIdentifier(getRequest().getResourceRef().getIdentifier() + "/"
				+ user.getId());
		getResponse().setEntity(rep);

	}

	public Element buildHeaderPart(XhtmlBuilder<Object, User> builder, Object obj) {
		return null;
	}

	public Element buildItemPart(XhtmlBuilder<Object, User> builder, User u) {
		Element pElement = builder.createElement("p");
		pElement.setAttribute("class", "user");
		Element aElement = builder.createElement("a");
		pElement.appendChild(aElement);
		aElement.setAttribute("href", getBaseUrl().toString()+"/"+u.getId() ); 
		aElement.setTextContent(u.getName());
		return pElement;
	}
}
