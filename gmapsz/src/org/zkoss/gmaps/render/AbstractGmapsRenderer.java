/* AbstractGmapsRenderer.java

 {{IS_NOTE
 Purpose:
 
 Description:
 
 History:
 Jan 8, 2008 14:45:13 , Created by henrichen
 }}IS_NOTE

 Copyright (C) 2008 Potix Corporation. All Rights Reserved.

 {{IS_RIGHT
 This program is distributed under GPL Version 2.0 in the hope that
 it will be useful, but WITHOUT ANY WARRANTY.
 }}IS_RIGHT
 */
package org.zkoss.gmaps.render;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

import org.zkoss.zk.ui.render.ComponentRenderer;
import org.zkoss.zk.ui.render.SmartWriter;

import org.zkoss.gmaps.Gimage;

/*
 * Base implementation of Google Maps component renderer.
 * 
 * @author henrichen
 * 
 * @since 2.0_7
 */
abstract public class AbstractGmapsRenderer implements ComponentRenderer {
	abstract protected String getType();
	abstract protected String getContent(Component comp);
	
	public void render(Component comp, Writer out) throws IOException {
		final SmartWriter wh = new SmartWriter(out);
		final HtmlBasedComponent self = (HtmlBasedComponent) comp;
		final String uuid = self.getUuid();
		wh.write("<span id=\"").write(uuid).write("\"");
		wh.write(self.getOuterAttrs()).write(self.getInnerAttrs()).write(" z.type=\""+getType()+"\">");
		wh.write(getContent(comp)).write("</span>");
	}
}
