package org.bds.touch.rest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.bds.touch.db.ChatDAO;
import org.bds.touch.model.Chat;
import org.bds.touch.model.Post;
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
import org.w3c.dom.Element;

public class ChatResource extends Resource implements XhtmlCallback<Chat,Post>{
	private static DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss.SSS"); 
	public ChatResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public Element buildHeaderPart(XhtmlBuilder<Chat,Post> builder, Chat chat) {
		System.out.println("ownerid="+getOwnerId()+" chatname="+getChatName());
		Element dlElem = builder.createElement("dl");
		builder.addPair(dlElem, "chatName", chat.getChatName());
		builder.addPair(dlElem, "otherName", chat.getOtherName());
		Date lastModified = chat.getLastModified();
		builder.addPair(dlElem, "lastModified", fmt.format(lastModified));
		return dlElem;
	}

	public Element buildItemPart(XhtmlBuilder<Chat,Post> builder, Post post) {
		Element liElement = builder.createElement("li");
		Element aElement = builder.addNewElement(liElement, "a");
		aElement.setAttribute("href", getPostLink(post));
		aElement.setTextContent(post.getText());
		return liElement;
	}

	private String getChatName() {
		return getAttribute("chatName");
	}
	private int getOwnerId() {
		return Integer.parseInt(getAttribute("userId"));
	}

	private String getAttribute(String attrName) {
		return (String)getRequest().getAttributes().get(attrName);
	}
	
	public String getResourceClass() {
		return "post";
	}

	public String getPostLink(Post p) {
		Reference baseRef = getRequest().getResourceRef().getBaseRef();
		return baseRef + "/" + p.getId();
	}

	

	/**
	 * Handle GET. Returns a full representation for a given variant.
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;
		Chat chat = getChatApplication().getChatDao().findChatByName(getOwnerId(), getChatName());
		if(chat == null) {
			throw new ResourceException(404);
		}
		
		XhtmlBuilder<Chat, Post> builder = new XhtmlBuilder<Chat,Post>(this);
		builder.setHeader(chat);
		List<Post> posts = (getChatApplication()).getPostDao().findAllPostsByOwnerIdAndChatName(getOwnerId(), getChatName());
		builder.setChildren(posts);
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			try {
				repr = builder.buildXhtml();
			} catch (ParserConfigurationException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e
						.getMessage());
			} catch (IOException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e
						.getMessage());
			}
		}

		return repr;
	}

	/**
	 * Handle POST requests, adds a chat post entry to this chat
	 */
	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		Form form = new Form(entity);
		String text = form.getFirstValue("text");
		String dir = form.getFirstValue("direction");

		if (text == null || dir == null) {
			XhtmlBuilder<Chat,Post> builder = new XhtmlBuilder<Chat,Post>(this);
			getResponse().setEntity(builder.buildErrorRepr(1, "Missing parameters"));
			return;
		}
		int idir = -1;
		try {
			idir = Integer.parseInt(dir);
		} catch (NumberFormatException e) {
			XhtmlBuilder<Chat,Post> builder = new XhtmlBuilder<Chat,Post>(this);
			getResponse().setEntity(builder.buildErrorRepr(1, "Invalid parameters"));
		}
		
		Chat chat = getChatApplication().getChatDao().findChatByName(getOwnerId(), getChatName());
		Post post = getChatApplication().getPostDao()
				.createPost(chat.getId(), Post.Direction.getDirection(idir), text);

		getResponse().setStatus(Status.SUCCESS_CREATED);
		Representation rep = new StringRepresentation("Item created "+post.getId()+"\n",
				MediaType.TEXT_PLAIN);
		rep.setIdentifier(getRequest().getResourceRef().getIdentifier() + "/"
				+ post.getId());
		getResponse().setEntity(rep);

	}

	/**
	 * PUT request, to create a new chat. Fails if the chat with that chat name already exists.
	 */
	@Override
	public void storeRepresentation(Representation entity)
			throws ResourceException {
		// TODO Auto-generated method stub
		super.storeRepresentation(entity);
	}
	
	
	private ChatApplication getChatApplication() {
		return (ChatApplication) getApplication();
	}
	
	@Override
	public void delete() {
		ChatDAO chatDao = getChatApplication().getChatDao();
		Chat chat = chatDao.findChatByName(getOwnerId(), getChatName());
		chatDao.delete(chat.getId());
		// TODO delete the posts as well, this will cause integrity violation
	}

}
