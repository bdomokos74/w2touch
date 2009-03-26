package org.bds.touch.rest;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.bds.touch.db.ServiceLocator;
import org.bds.touch.model.User;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserResource extends AbstractResource implements XhtmlCallback<User, Object> {

	public UserResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		
		// This representation has only one type of representation.
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	/**
	 * Returns a full representation for a given variant.
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;
		XhtmlBuilder<User, Object> builder = new XhtmlBuilder<User, Object>(this);
		builder.setTitle("User details");
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			try {				
				String userId = (String)getRequest().getAttributes().get("userId");
				User user= ServiceLocator.getUserDao().findUserById(Integer.parseInt(userId));
				builder.setHeader(user);
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
	public void delete() {
		String userId = (String) getRequest().getAttributes().get("userId");
		((ChatApplication)getApplication()).getUserDao().deleteUserById(Integer.parseInt(userId));
	}

	public Element buildHeaderPart(XhtmlBuilder<User, Object> builder,
			User user) {
		Element dlElem = builder.createElement("dl");
		builder.addPair(dlElem, "name", String.valueOf(user.getName()));
		builder.addPair(dlElem, "id", String.valueOf(user.getId()));
		return dlElem;
	}

	public Element buildItemPart(XhtmlBuilder<User, Object> builder, Object item) {
		return null;
	}
}
