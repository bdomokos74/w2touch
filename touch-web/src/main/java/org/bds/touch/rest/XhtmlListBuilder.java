package org.bds.touch.rest;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Element;

public class XhtmlListBuilder<T> extends XhtmlBuilder {

	private final XhtmlCallback<T> callback;

	public XhtmlListBuilder(XhtmlCallback<T> callback) {
		super();
		this.callback = callback;
	}

	public DomRepresentation buildXhtml() throws ParserConfigurationException,
			IOException {

		DomRepresentation repr = createXhtmlDocument();
		Element docElement = doc.getDocumentElement();
		Element body = buildHeadAndBody(docElement, callback.getTitle());

		Element headerElement = callback.buildHeaderPart(this);
		if(headerElement!=null)
			body.appendChild(headerElement);
			
		Element ulElement = addNewElement(body, "ul");
		ulElement.setAttribute("class", callback.getResourceClass());

		List<T> list = callback.getList();
		for (T element : list) {
			ulElement.appendChild(callback.buildItemPart(this, element));
		}
		return repr;
	}
}
