package org.bds.touch.rest;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Element;

public class XhtmlListBuilder<T> extends XhtmlBuilder {

	private final XhtmlInvoker<T> invoker;

	public XhtmlListBuilder(XhtmlInvoker<T> invoker) {
		super();
		this.invoker = invoker;
	}

	public DomRepresentation buildXhtml() throws ParserConfigurationException,
			IOException {

		DomRepresentation repr = createXhtmlDocument();
		Element docElement = doc.getDocumentElement();
		Element body = buildHeadAndBody(docElement, invoker.getTitle());

		Element ulElement = addNewElement(body, "ul");
		ulElement.setAttribute("class", "users");

		List<T> list = invoker.getList();
		for (T element : list) {
			Element liElement = addNewElement(ulElement, "li");
			Element aElement = addNewElement(liElement, "a");
			aElement.setAttribute("href", invoker.getLink(element));
			aElement.setTextContent(invoker.getText(element));
			System.out.println(element.toString());
		}
		return repr;
	}
}
