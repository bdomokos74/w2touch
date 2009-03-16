package org.bds.touch.rest;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.ResourceException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class XhtmlBuilder {

	protected DOMImplementation domImplementation;
	protected DocumentType docType;

	private String title = "";
	protected Document doc;

	public XhtmlBuilder() {
		// TODO avoid creating it all the time
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		domImplementation = documentBuilder.getDOMImplementation();
		docType = domImplementation.createDocumentType("html",
				"-//W3C//DTD XHTML 1.1//EN",
				"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd");
	}

	public void setTitle(String title) {
		this.title = title;
	}

	protected Element addNewElement(Element element, String name, String text) {
		Element newElement = addNewElement(element, name);
		newElement.setTextContent(text);
		return newElement;
	}

	protected Element addNewElement(Element element, String name) {
		Element newElement = doc.createElement(name);
		element.appendChild(newElement);
		return newElement;
	}

	protected DomRepresentation createXhtmlDocument()
			throws ParserConfigurationException {
		DomRepresentation repr;
		doc = domImplementation.createDocument("http://www.w3.org/1999/xhtml",
				"html", docType);
		repr = new DomRepresentation(MediaType.TEXT_XML, doc);
		return repr;
	}

	protected Element buildHeadAndBody(Element docElement, String title) {
		Element head = addNewElement(docElement, "head");

		addNewElement(head, "title", title);
		Element body = addNewElement(docElement, "body");
		return body;
	}

	public void addPair(Element dlElem, String key, String value) {
		Element dtElem = addNewElement(dlElem, "dt");
		dtElem.setTextContent(key);
		Element ddElem = addNewElement(dlElem, "dd");
		ddElem.setTextContent(value);
	}

	public DomRepresentation buildXhtml() throws ParserConfigurationException,
			IOException {
		DomRepresentation xhtmlDoc = createXhtmlDocument();
		doc = xhtmlDoc.getDocument();
		Element docElement = doc.getDocumentElement();
		buildHeadAndBody(docElement, title);
		return xhtmlDoc;
	}

	public DomRepresentation buildErrorRepr(int errorCode, String errorMessage) throws ResourceException {
		DomRepresentation xhtmlDoc = null;
		try {
			xhtmlDoc = createXhtmlDocument();
			doc = xhtmlDoc.getDocument();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Can't generate error repr.");
		}
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

		return xhtmlDoc;
	}

	public Document getDoc() {
		return doc;
	}
}