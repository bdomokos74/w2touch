package org.bds.touch.rest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

public class TransformHelper {

	private static TransformHelper INSTANCE = new TransformHelper();

	private TransformHelper() {
	}

	public static TransformHelper getInstance() {
		return INSTANCE;
	}

	public String indentXml(Document doc) throws IOException {
		return doTransform(getResource("/indent-xml.xsl"), doc);
	}

	public String indentXml(String xml) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
		return doTransform(new StreamSource(getResource("/indent-xml.xsl")), new StreamSource(is));
	}
	
	private String doTransform(InputStream xsltLocationStream, Document xmlInput) {
		StreamSource transformation = new StreamSource(xsltLocationStream);
		DOMSource domSource = new DOMSource(xmlInput);
		return doTransform(transformation, domSource);
	}

	private String doTransform(Source transformation, Source domSource) {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		StringWriter result = new StringWriter();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer(transformation);
			transformer.transform(domSource, new StreamResult(result));
		} catch (Exception e) {
			throw new RuntimeException("Exception occured: " + e);
		}
		return result.toString().trim();
	}

	public InputStream getResource(String location) {
		return getClass().getResourceAsStream(location);
	}

	public String getExpectedString(String resourceLocation) throws IOException {
		InputStream expectedInputStream = getClass().getResourceAsStream(
				resourceLocation);
		BufferedReader is = new BufferedReader(new InputStreamReader(
				expectedInputStream));
		StringBuilder result = new StringBuilder();
		String line;
		while ((line = is.readLine()) != null) {
			result.append(line).append("\n");
		}
		if (result.length() > 0)
			result.deleteCharAt(result.length() - 1);
		expectedInputStream.close();
		return result.toString();
	}

}
