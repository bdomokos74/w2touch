package org.bds.touch.rest;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.bds.touch.model.Chat;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ChatListResource extends Resource implements XhtmlCallback<Chat> {

	public ChatListResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	public List<Chat> getList() {
		int userId = getUserId();
		List<Chat> persistedChats = ((ChatApplication) getApplication()).getChatDao().findAllChatByUserId(userId);
		return persistedChats;
	}

	public String getTitle() {
		return "Chat List";
	}
	
	private int getUserId() {
		return Integer.parseInt((String)getRequest().getAttributes().get("userId"));
	}

	public String getText(Chat t) {
		return t.getChatName();
	}
	public String getLink(Chat t) {
		Reference baseRef = getBaseUrl();
		return baseRef + "/" + t.getId();
	}

	private Reference getBaseUrl() {
		return getRequest().getResourceRef().getBaseRef();
	}

	public Element buildHeaderPart(XhtmlBuilder builder) {
		Document doc = builder.getDoc();
		Element pElement = doc.createElement("p");
		pElement.setAttribute("class", "user");
		Element aElement = doc.createElement("a");
		pElement.appendChild(aElement);
		aElement.setAttribute("href", trimLastPart(getBaseUrl().toString())); 
		aElement.setTextContent("User");
		return pElement;
	}
	
	public Element buildItemPart(XhtmlBuilder builder, Chat c) {
		Element liElement = builder.getDoc().createElement("li");
		Element aElement = builder.addNewElement(liElement, "a");
		aElement.setAttribute("href", getLink(c));
		aElement.setTextContent(getText(c));
		return liElement;
	}
	
	private String trimLastPart(String baseRef) {
		int lastSlash = baseRef.lastIndexOf('/');
		return baseRef.substring(0, lastSlash);
	}

	public String getResourceClass() {
		return "chats";
	}
	
	// The REST operations
	
	/**
	 * 
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;

		XhtmlListBuilder<Chat> builder = new XhtmlListBuilder<Chat>(this);
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
		String userId = form.getFirstValue("userId");
		String chatName = form.getFirstValue("chatName");
		String partyName = form.getFirstValue("partyName");

		if (userId == null || chatName == null || partyName == null ||
				userId.trim().equals("")||chatName.trim().equals("")||partyName.trim().equals("")) {
			XhtmlBuilder builder = new XhtmlBuilder();
			getResponse().setEntity(builder.buildErrorRepr(1, "Missing parameters"));
			return;
		}

		int id = Integer.parseInt(userId);
		Chat chat = ((ChatApplication) getApplication()).getChatDao()
				.createChat(chatName, id, partyName);

		getResponse().setStatus(Status.SUCCESS_CREATED);
		
		Representation rep = new StringRepresentation("Item created "+chat.getId()+"\n",
				MediaType.TEXT_PLAIN);
		rep.setIdentifier(getRequest().getResourceRef().getIdentifier() + "/"+ chat.getId());
		getResponse().setEntity(rep);

	}

}
