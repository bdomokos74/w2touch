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
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.w3c.dom.Element;

public class PostResource extends AbstractResource implements
		XhtmlCallback<PostResource.ReqHolder, Object> {
	static class ReqHolder {
		private final Post post;
		private final Chat chat;
		public ReqHolder(Post post, Chat chat) {
			this.post = post;
			this.chat = chat;
		}
		public Post getPost() {
			return post;
		}
		public Chat getChat() {
			return chat;
		}
	}
	public PostResource(Context context, Request request, Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;
		String postId = getAttribute("postId");
		String userName = getUserName();

		Post post = ServiceLocator.getPostDao().findPostById(Integer.parseInt(postId));
		if(post==null)
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Post object not found.");
		
		Chat chat = ServiceLocator.getChatDao().findChatById(post.getChatId());
		User user = ServiceLocator.getUserDao().findUserById(chat.getOwnerId());
		if(!userName.equals(user.getName()))
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, ".");
			
		XhtmlBuilder<PostResource.ReqHolder, Object> builder = new XhtmlBuilder<PostResource.ReqHolder, Object>(this);
		builder.setTitle("Post details");
		builder.setHeader(new ReqHolder(post, chat));
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

	public Element buildHeaderPart(XhtmlBuilder<PostResource.ReqHolder, Object> builder,
			PostResource.ReqHolder holder) {
		Element dlElem = builder.createElement("dl");
		Element aElement = builder.createElement("a");
		aElement.setTextContent(holder.getChat().getChatName());
		aElement.setAttribute("href", trimLastPart(getBaseUrl()) );
		builder.addPair(dlElem, "chat", aElement);
		builder.addPair(dlElem, "text", String.valueOf(holder.getPost().getText()));
		builder.addPair(dlElem, "dir", String.valueOf(holder.getPost().getDirection()));
		return dlElem;
	}

	public Element buildItemPart(XhtmlBuilder<PostResource.ReqHolder, Object> builder, Object item) {
		return null;
	}
}
