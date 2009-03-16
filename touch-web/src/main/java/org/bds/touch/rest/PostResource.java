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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PostResource extends Resource {
	public PostResource(Context context, Request request, Response response) {
		super(context, request, response);
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}
	
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		DomRepresentation repr = null;
		XhtmlBuilder builder = new XhtmlBuilder();
		builder.setTitle("Post details");
		if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
			try {				
				String postId = (String)getRequest().getAttributes().get("postId");
				Post post = ((ChatApplication)getApplication()).getPostDao().findPostById(Integer.parseInt(postId));
				
				repr = builder.buildXhtml();
				Document document = repr.getDocument();
				Element body = (Element)document.getElementsByTagName("body").item(0);
				Element dlElem = builder.addNewElement(body, "dl");
				
				builder.addPair(dlElem, "chat", String.valueOf(post.getChatId()));
				builder.addPair(dlElem, "text", String.valueOf(post.getText()));
				builder.addPair(dlElem, "dir", String.valueOf(post.getDirection()));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return repr;
	}}
