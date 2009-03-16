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

public class ChatResource extends Resource implements XhtmlCallback<Post>{
	private static DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss.SSS"); 
	public ChatResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public Element buildHeaderPart(XhtmlBuilder builder) {
		String userId = (String) getRequest().getAttributes().get("chatId");
		int id = -1;
		try {
			id = Integer.parseInt(userId);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		Chat chat = ((ChatApplication) getApplication()).getChatDao().findChatById(id);
		
		Element dlElem = builder.getDoc().createElement("dl");
		builder.addPair(dlElem, "chatName", chat.getChatName());
		builder.addPair(dlElem, "otherName", chat.getOtherName());
		Date lastModified = chat.getLastModified();
		builder.addPair(dlElem, "lastModified", fmt.format(lastModified));
		return dlElem;
	}

	public Element buildItemPart(XhtmlBuilder builder, Post t) {
		Element liElement = builder.getDoc().createElement("li");
		Element aElement = builder.addNewElement(liElement, "a");
		aElement.setAttribute("href", getPostLink(t));
		aElement.setTextContent(t.getText());
		return liElement;
	}
	
	public String getTitle() {
		return "Chat";
	}

	public List<Post> getList() {
		List<Post> posts = ((ChatApplication) getApplication()).getPostDao().findAllPostsByChatId(getChatId());
		return posts;
	}

	private int getChatId() {
		return Integer.parseInt((String)getRequest().getAttributes().get("chatId"));
	}
	
	public String getResourceClass() {
		return "post";
	}

	public String getPostLink(Post p) {
		Reference baseRef = getRequest().getResourceRef().getBaseRef();
		return baseRef + "/" + p.getId();
	}

	

	/**
	 * Returns a full representation for a given variant.
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;
		XhtmlListBuilder<Post> builder = new XhtmlListBuilder<Post>(this);
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

	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		Form form = new Form(entity);
		String text = form.getFirstValue("text");
		String dir = form.getFirstValue("direction");

		if (text == null || dir == null) {
			XhtmlBuilder builder = new XhtmlBuilder();
			getResponse().setEntity(builder.buildErrorRepr(1, "Missing parameters"));
			return;
		}
		int idir = -1;
		try {
			idir = Integer.parseInt(dir);
		} catch (NumberFormatException e) {
			XhtmlBuilder builder = new XhtmlBuilder();
			getResponse().setEntity(builder.buildErrorRepr(1, "Invalid parameters"));
		}
		
		Post post = ((ChatApplication) getApplication()).getPostDao()
				.createPost(getChatId(), Post.Direction.getDirection(idir), text);

		getResponse().setStatus(Status.SUCCESS_CREATED);
		Representation rep = new StringRepresentation("Item created "+post.getId()+"\n",
				MediaType.TEXT_PLAIN);
		rep.setIdentifier(getRequest().getResourceRef().getIdentifier() + "/"
				+ post.getId());
		getResponse().setEntity(rep);

	}
	
	@Override
	public void delete() {
		String chatId = (String) getRequest().getAttributes().get("chatId");
		
		ChatDAO chatDao = ((ChatApplication) getApplication()).getChatDao();
		chatDao.delete(Integer.parseInt(chatId));
		
	}

}
