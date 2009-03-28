package org.bds.touch.rest;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.bds.touch.db.ServiceLocator;
import org.bds.touch.model.Chat;
import org.bds.touch.model.Post;
import org.bds.touch.model.User;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.w3c.dom.Element;

public class PostResource extends AbstractResource implements
		XhtmlCallback<Post, Object> {
	public PostResource(Context context, Request request, Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;
		String postId = (String) getRequest().getAttributes().get("postId");
		String userName = (String) getRequest().getAttributes().get("userName");

		Post post = ServiceLocator.getPostDao().findPostById(Integer.parseInt(postId));
		if(post==null)
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Post object not found.");
		
		Chat chat = ServiceLocator.getChatDao().findChatById(post.getChatId());
		User user = ServiceLocator.getUserDao().findUserById(chat.getOwnerId());
		if(!userName.equals(user.getName()))
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, ".");
			
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
		Element aElement = builder.createElement("a");
		aElement.setAttribute("href", trimLastPart(getBaseUrl().toString()) );
		builder.addPair(dlElem, "chat", aElement);
		builder.addPair(dlElem, "text", String.valueOf(post.getText()));
		builder.addPair(dlElem, "dir", String.valueOf(post.getDirection()));
		return dlElem;
	}

	public Element buildItemPart(XhtmlBuilder<Post, Object> builder, Object item) {
		return null;
	}
}
