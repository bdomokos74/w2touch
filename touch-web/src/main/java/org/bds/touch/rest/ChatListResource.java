package org.bds.touch.rest;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.bds.touch.db.ServiceLocator;
import org.bds.touch.model.Chat;
import org.bds.touch.model.User;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.w3c.dom.Element;

public class ChatListResource extends AbstractResource implements XhtmlCallback<User, Chat> {

	public ChatListResource(Context context, Request request, Response response) {
		super(context, request, response);
		setModifiable(true);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}
	
	public String getChatLink(Chat chat) {
		return getBaseUrl() + "/" + encodeUrl(chat.getChatName());
	}

	User getUser() {
		return ServiceLocator.getUserDao().findUserByName(getUserName());
	}
	List<Chat> getChatList() {
		List<Chat> persistedChats = ServiceLocator.getChatDao().findAllChatByUserName(getUserName());
		return persistedChats;
	}
	
	public Element buildHeaderPart(XhtmlBuilder<User,Chat> builder, User user) {
		Element dlElement = builder.createElement("dl");
		Element dtElement = builder.addNewElement(dlElement, "dt");
		dtElement.setTextContent("user");
		Element ddElement = builder.addNewElement(dlElement, "dd");
		Element aElement = builder.addNewElement(ddElement, "a");
		aElement.setTextContent(user.getName());
		aElement.setAttribute("class", "user");
		aElement.setAttribute("href", trimLastPart(getBaseUrl())); 
		return dlElement;
	}
	
	public Element buildItemPart(XhtmlBuilder<User, Chat> builder, Chat c) {
		Element ulElement = builder.createElement("ul");
		ulElement.setAttribute("class", "chats");
		Element liElement = builder.addNewElement(ulElement, "li");
		Element aElement = builder.addNewElement(liElement, "a");
		aElement.setTextContent(c.getChatName());
		aElement.setAttribute("class", "chat");
		aElement.setAttribute("href", getChatLink(c));
		return liElement;
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

		XhtmlBuilder<User,Chat> builder = new XhtmlBuilder<User, Chat>(this);
		builder.setTitle("Chat List");
		List<Chat> chatList = getChatList();
		builder.setHeader(getUser());
		builder.setChildren(chatList);
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
		Form form = transformRepresentation(entity);
		String chatName = form.getFirstValue("chatName");
		String partyName = form.getFirstValue("partyName");

		if ( chatName == null || partyName == null ||
				chatName.trim().equals("")||partyName.trim().equals("")) {
			XhtmlBuilder<User, Chat> builder = new XhtmlBuilder<User, Chat>(this);
			getResponse().setEntity(builder.buildErrorRepr(1, "Missing parameters"));
			return;
		}

		User user = ServiceLocator.getUserDao().findUserByName(getUserName());
		Chat chat = ServiceLocator.getChatDao().createChat(chatName, user.getId(), partyName);

		getResponse().setStatus(Status.SUCCESS_CREATED);
		
		
		XhtmlBuilder<User, Chat> builder = new XhtmlBuilder<User, Chat>(this);
		//rep.setIdentifier(getRequest().getResourceRef().getIdentifier() + "/"+ chat.getId());
		getResponse().setEntity(builder.buildResponseXmlRep("created", chat.getId(), getBaseUrl()+"/"+chat.getId()));

	}
}
