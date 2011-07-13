/* Gpolygon.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Dec 19, 2007 4:38:11 PM, Created by henrichen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/

package org.zkoss.gmaps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.zkoss.lang.Objects;
import org.zkoss.util.CollectionsX;
import org.zkoss.xml.HTMLs;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.UiException;

/**
 * Google Maps support Gpolygon.
 * 
 * @author henrichen
 * @since 2.0_7
 */
public class Gpolygon extends Gpolyline {
	private boolean _outline = true;
	private boolean _fill = true;
	private String _fillColor = "#808080"; //default to dark gray
	private int _fillOpacity = 50; //0~100
	
	public Gpolygon() {
	}

	/** Returns whether to draw the outline of this polygon with the provided
	 * color {@link #getColor}.
	 * @return whether to draw the outline of this polygon.
	 */
	public boolean isOutline() {
		return _outline;
	}

	/** Sets true to draw the outline of this polygon with provied color {@link #getColor}.
	 * @param b true to draw the outline of this polygon
	 */
	public void setOutline(boolean b) {
		if (_outline != b) {
			_outline = b;
			invalidate();
		}
	}
	
	/** Returns whether  to fill the interier of this polygon with the provided 
	 * fill color {@link #getFillColor}.
	 * @return whether to fill the interier of this polygon.
	 */
	public boolean isFill() {
		return _fill;
	}
	
	/** Sets true to fill the interier of this polygon with the provided
	 * fill color {@link #getFillColor}.
	 * @param b true to fill the interier of this polygon
	 */
	public void setFill(boolean b) {
		if (_fill != b) {
			_fill = b;
			invalidate();
		}
	}
	
	/**
	 * Returns fill color in form of #RRGGBB, default to #808080.
	 * @return the color
	 */
	public String getFillColor() {
		return _fillColor;
	}

	/**
	 * Sets fill color in form of #RRGGBB, default to #808080.
	 * @param color the color to set
	 */
	public void setFillColor(String color) {
		if (color == null) color = "#808080";
		if (!color.equals(_fillColor)) {
			this._fillColor = color;
			invalidate();
		}
	}
	
	/**
	 * Returns fill opacity from 0 (transparent) to 100 (solid), default to 50.
	 */
	public int getFillOpacity() {
		return _fillOpacity;
	}
	
	/**
	 * Sets fill opacity from 0 (transparent) to 100 (solid), default to 50.
	 */
	public void setFillOpacity(int op) {
		if (op < 0 || op > 100) {
			throw new UiException("Fill opacity must be between 0 to 100 (inclusive): "+op);
		}
		if (_fillOpacity != op) {
			_fillOpacity = op;
			invalidate();
		}
	}
	
	public String getEncodedPolyline() {
		if (_encodedPolyline == null) {
			int lat = 0;
			int lng = 0;
			final StringBuffer sb = new StringBuffer(_points.size()*4);
			int lat0 = 0;
			int lng0 = 0;
			int j = 0;
			for(final Iterator it = _points.iterator(); it.hasNext(); ++j) {
				Tuple tuple = (Tuple) it.next();
				final int tlat = e5(tuple.lat);
				final int tlng = e5(tuple.lng);
				if (j == 0) {
					lat0 = tlat;
					lng0 = tlng;
				}
				sb.append(encodeLatLng(tlat - lat)).append(encodeLatLng(tlng - lng));
				lat = tlat;
				lng = tlng;
			}
			//there is at least one points and last point is not the first point
			if (j > 0 && (lat != lat0 || lng != lng0)) {
				sb.append(encodeLatLng(lat0 - lat)).append(encodeLatLng(lng0 - lng));
			}
			_encodedPolyline = (j == 0) ? "??" : sb.toString();
		}
		return _encodedPolyline;
	}
	
	public String getEncodedLevels() {
		if (_encodedLevels == null) {
			final StringBuffer sb = new StringBuffer(_points.size()*2);
			int lat0 = 0;
			int lng0 = 0;
			int lvl0 = 0;
			int lat = 0;
			int lng = 0;
			int j = 0;
			for(final Iterator it = _points.iterator(); it.hasNext(); ++j) {
				Tuple tuple = (Tuple) it.next();
				final int tlat = e5(tuple.lat);
				final int tlng = e5(tuple.lng);
				if (j == 0) {
					lat0 = tlat;
					lng0 = tlng;
					lvl0 = tuple.level;
				}
				sb.append(encodeInt(tuple.level));
				lat = tlat;
				lng = tlng;
			}
			//there is at least one point and last point is not the first point
			if (j > 0 && (lat != lat0 || lng != lng0)) {
				sb.append(encodeInt(lvl0));
			}
			_encodedLevels = (j == 0) ? "?" : sb.toString();
		}
		return _encodedLevels;
	}
	
	/** Returns the HTML attributes for this tag.
	 * <p>Used only for component development, not for application developers.
	 */
	public String getOuterAttrs() {
		final String attrs = super.getOuterAttrs();
		final StringBuffer sb = new StringBuffer(128);
		if (attrs != null) {
			sb.append(attrs);
		}
		HTMLs.appendAttribute(sb, "z.fl", isFill());
		HTMLs.appendAttribute(sb, "z.ol", isOutline());
		HTMLs.appendAttribute(sb, "z.fcr", getFillColor());
		HTMLs.appendAttribute(sb, "z.op", ""+(getFillOpacity() / 100.0));
		return sb.toString();
	}
}
