package org.bds.touch.rest;

import org.w3c.dom.Element;

public interface XhtmlCallback<T,S> {
	Element buildHeaderPart(XhtmlBuilder<T,S> builder, T header);
	Element buildItemPart(XhtmlBuilder<T,S> builder, S item);
}
