package org.bds.touch.rest;

import java.util.List;

import org.w3c.dom.Element;

public interface XhtmlCallback<T> {
	String getTitle();
	String getResourceClass();
	List<T> getList();
	Element buildHeaderPart(XhtmlBuilder builder);
	Element buildItemPart(XhtmlBuilder builder, T t);
	/*String getLink(T t);
    String getText(T t);*/
}
