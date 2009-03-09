package org.bds.touch.rest;

import static org.restlet.data.MediaType.TEXT_XML;

import java.io.IOException;
import java.util.List;

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

public class UserListResource extends Resource {
	public UserListResource(Context context, Request request, Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			try {
				repr = new DomRepresentation(TEXT_XML);
				Document doc = repr.getDocument();
				Element usersElement = doc.createElement("users");
				doc.appendChild(usersElement);
				
				List<User> persistentUsers = ((ChatApplication)getApplication()).getUserDao().findUsers();
				for(User u : persistentUsers ) {
					Element userElement = doc.createElement("user");
					userElement.setAttribute("name", u.getName());
					userElement.setAttribute("uri", "http://localhost:8080/touch-web/resources/users/"+u.getName());
					usersElement.appendChild(userElement);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return repr;
	}
}
