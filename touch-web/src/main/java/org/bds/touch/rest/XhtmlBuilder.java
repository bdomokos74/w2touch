package org.bds.touch.rest;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.data.MediaType;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class XhtmlBuilder {

	protected static DOMImplementation domImplementation;
	protected static DocumentType docType;
	private String title ="";
	protected Document doc;
	
	public XhtmlBuilder() {
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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

	public DomRepresentation createXhtmlDocument() throws ParserConfigurationException {
		DomRepresentation repr;
		doc = domImplementation.createDocument("http://www.w3.org/1999/xhtml", "html", docType);
		repr = new DomRepresentation(MediaType.TEXT_XML, doc);
		return repr;
	}

	protected Element buildHeadAndBody(Element docElement, String title) {
		Element head = addNewElement(docElement, "head");
	
		addNewElement(head, "title", title );
		Element body = addNewElement(docElement, "body");
		return body;
	}
	
	public void addPair(Element dlElem, String key, String value) {
		Element dtElem = addNewElement(dlElem, "dt");
		dtElem.setTextContent(key);
		Element ddElem = addNewElement(dlElem, "dd");
		ddElem.setTextContent(value);
	}

	public DomRepresentation buildXhtml() throws ParserConfigurationException, IOException {
		DomRepresentation xhtmlDoc = createXhtmlDocument();
		doc = xhtmlDoc.getDocument();
		Element docElement = doc.getDocumentElement();
		buildHeadAndBody(docElement, title);
		return xhtmlDoc;
	}
}