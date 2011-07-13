/* GpolygonDefault.java

 {{IS_NOTE
 Purpose:
 
 Description:
 
 History:
 Dec 20, 2007 19:09:13 , Created by henrichen
 }}IS_NOTE

 Copyright (C) 2007 Potix Corporation. All Rights Reserved.

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

import org.zkoss.gmaps.Gpolygon;

/*
 * {@link Gpolygon}'s default mold.
 * 
 * @author henrichen
 * 
 * @since 2.0_7
 */
public class GpolygonDefault extends GpolylineDefault {
	protected String getType() {
		return "gmapsz.gmaps.Ggon";
	}
}
