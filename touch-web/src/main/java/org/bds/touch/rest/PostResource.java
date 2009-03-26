package org.bds.touch.rest;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.bds.touch.model.Post;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.w3c.dom.Element;

public class PostResource extends Resource implements
		XhtmlCallback<Post, Object> {
	public PostResource(Context context, Request request, Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;
		String postId = (String) getRequest().getAttributes().get("postId");

		Post post = ((ChatApplication) getApplication()).getPostDao()
				.findPostById(Integer.parseInt(postId));

		XhtmlBuilder<Post, Object> builder = new XhtmlBuilder<Post, Object>(
				this);
		builder.setTitle("Post details");
		builder.setHeader(post);
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			try {

				repr = builder.buildXhtml();
				
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return repr;
	}

	public Element buildHeaderPart(XhtmlBuilder<Post, Object> builder,
			Post post) {
		Element dlElem = builder.createElement("dl");
		builder.addPair(dlElem, "chat", String.valueOf(post.getChatId()));
		builder.addPair(dlElem, "text", String.valueOf(post.getText()));
		builder.addPair(dlElem, "dir", String.valueOf(post.getDirection()));
		return dlElem;
	}

	public Element buildItemPart(XhtmlBuilder<Post, Object> builder, Object item) {
		// TODO Auto-generated method stub
		return null;
	}
}
