package org.bds.touch.rest;

import org.restlet.Context;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

public abstract class AbstractResource extends Resource {
	AbstractResource(Context context, Request request, Response response) {
		super(context, request, response);
	}

	protected String trimLastPart(String baseRef) {
		int lastSlash = baseRef.lastIndexOf('/');
		return baseRef.substring(0, lastSlash);
	}

	protected Reference getBaseUrl() {
		return getRequest().getResourceRef().getBaseRef();
	}
}
