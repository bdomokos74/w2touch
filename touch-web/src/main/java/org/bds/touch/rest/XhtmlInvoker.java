package org.bds.touch.rest;

import java.util.List;

public interface XhtmlInvoker<T> {

	String getTitle();
	List<T> getList();
	String getLink(T t);
    String getText(T t);

}
