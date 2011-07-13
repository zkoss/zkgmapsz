/* GimageDefault.java

 {{IS_NOTE
 Purpose:
 
 Description:
 
 History:
 Jan 8, 2008 14:43:13 , Created by henrichen
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
import org.zkoss.zk.ui.render.ComponentRenderer;
import org.zkoss.zk.ui.render.SmartWriter;

import org.zkoss.gmaps.Gimage;

/*
 * {@link Gimage}'s default mold.
 * 
 * @author henrichen
 * 
 * @since 2.0_7
 */
public class GimageDefault extends AbstractGmapsRenderer {

	protected String getType() {
		return "gmapsz.gmaps.Ggnd";
	}
	protected String getContent(Component comp) {
		return "";
	}
}
