package org.bds.touch.rest;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;

public abstract class AbstractResource extends Resource {
	interface FormFactory {
		Form getForm(Representation repr);
	}
	private FormFactory defaultFormFactory = new FormFactory() {
		public Form getForm(Representation repr) {
			return new Form(repr);
		}
	};

	AbstractResource(Context context, Request request, Response response) {
		super(context, request, response);
	}

	protected String trimLastPart(String baseRef) {
		int lastSlash = baseRef.lastIndexOf('/');
		return baseRef.substring(0, lastSlash);
	}

	protected String getBaseUrl() {
		return getRequest().getResourceRef().getBaseRef().toString();
	}

	protected String getUserName() {
		return getAttribute("userName");
	}

	protected String getAttribute(String attrName) {
		return (String)getRequest().getAttributes().get(attrName);
	}

	public Form transformRepresentation(Representation repr) {
		return defaultFormFactory.getForm(repr);
	}
	
	void setFormFactory(FormFactory factory) {
		defaultFormFactory = factory;
	}
}
