package org.bds.touch.rest;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.data.MediaType;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.ResourceException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class XhtmlBuilder<T, S> {

	protected DOMImplementation domImplementation;
	protected DocumentType docType;

	private DomRepresentation repr;
	private Document doc;

	private String title = "";
	private final XhtmlCallback<T, S> cb;
	private T header;
	private List<S> children;

	public XhtmlBuilder(XhtmlCallback<T, S> cb) {
		this.cb = cb;
		// TODO avoid creating it all the time
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			// TODO
			throw new RuntimeException();
		}
		domImplementation = documentBuilder.getDOMImplementation();
		docType = domImplementation.createDocumentType("html",
				"-//W3C//DTD XHTML 1.1//EN",
				"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd");
		
		try {
			createXhtmlDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			// TODO
			throw new RuntimeException();
		}
	}

	private void createXhtmlDocument() throws ParserConfigurationException {
		doc = domImplementation.createDocument("http://www.w3.org/1999/xhtml",
				"html", docType);
		repr = new DomRepresentation(MediaType.TEXT_XML, doc);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setHeader(T header) {
		this.header = header;

	}

	public void setChildren(List<S> children) {
		this.children = children;

	}

	public Element createElement(String tag) {
		return doc.createElement(tag);
	}
	
	public Element addNewElement(Element element, String name, String text) {
		Element newElement = addNewElement(element, name);
		newElement.setTextContent(text);
		return newElement;
	}

	public Element addNewElement(Element parentElement, String name) {
		Element newElement = doc.createElement(name);
		parentElement.appendChild(newElement);
		return newElement;
	}

	public void addPair(Element parentElement, String key, String value) {
		Element dtElem = addNewElement(parentElement, "dt");
		dtElem.setTextContent(key);
		Element ddElem = addNewElement(parentElement, "dd");
		ddElem.setTextContent(value);
	}

	public void addPair(Element parentElement, String key, Element value) {
		Element dtElem = addNewElement(parentElement, "dt");
		dtElem.setTextContent(key);
		Element ddElem = addNewElement(parentElement, "dd");
		ddElem.appendChild(value);
	}

	protected Element buildHeadAndBody(Element docElement, String title) {
		Element head = addNewElement(docElement, "head");

		addNewElement(head, "title", title);
		Element body = addNewElement(docElement, "body");
		return body;
	}

	public DomRepresentation buildXhtml() throws ParserConfigurationException,
			IOException {

		Element docElement = doc.getDocumentElement();
		Element body = buildHeadAndBody(docElement, title);

		Element headerElement = cb.buildHeaderPart(this, header);
		if (headerElement != null)
			body.appendChild(headerElement);

		if (children != null) {
			Element ulElement = addNewElement(body, "ul");

			for (S child : children) {
				ulElement.appendChild(cb.buildItemPart(this, child));
			}
		}
		return repr;
	}
	
	/*
	 * DomRepresentation repr = createXhtmlDocument(); Element docElement =
	 * doc.getDocumentElement(); Element body = buildHeadAndBody(docElement,
	 * callback.getTitle());
	 * 
	 * Element headerElement = callback.buildHeaderPart(this);
	 * if(headerElement!=null) body.appendChild(headerElement);
	 * 
	 * Element ulElement = addNewElement(body, "ul");
	 * ulElement.setAttribute("class", callback.getResourceClass());
	 * 
	 * List<T> list = callback.getList(); for (T element : list) {
	 * ulElement.appendChild(callback.buildItemPart(this, element)); } return
	 * repr;
	 */

	public DomRepresentation buildErrorRepr(int errorCode, String errorMessage)
			throws ResourceException {
		
		Element docElement = doc.getDocumentElement();
		Element body = buildHeadAndBody(docElement, "Error");

		Element eltError = doc.createElement("error");
		Element eltCode = doc.createElement("code");
		eltCode.setTextContent(String.valueOf(errorCode));
		eltError.appendChild(eltCode);

		Element eltMessage = doc.createElement("message");
		eltMessage.appendChild(doc.createTextNode(errorMessage));
		eltError.appendChild(eltMessage);
		body.appendChild(eltError);

		return repr;
	}
}