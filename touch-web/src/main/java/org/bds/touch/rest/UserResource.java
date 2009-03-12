package org.bds.touch.rest;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.bds.touch.model.User;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserResource extends Resource {

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
		XhtmlBuilder builder = new XhtmlBuilder();
		builder.setTitle("User details");
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			try {				
				String userId = (String)getRequest().getAttributes().get("userId");
				User user= ((ChatApplication)getApplication()).getUserDao().findUserById(Integer.parseInt(userId));
				
				repr = builder.buildXhtml();
				Document document = repr.getDocument();
				Element body = (Element)document.getElementsByTagName("body").item(0);
				Element dlElem = builder.addNewElement(body, "dl");
				
				builder.addPair(dlElem, "name", user.getName());
				builder.addPair(dlElem, "id", String.valueOf(user.getId()));
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
}
