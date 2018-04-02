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

import java.util.Iterator;
import java.util.Map;

import org.zkoss.zk.ui.UiException;

/**
 * Google Maps support Gpolygon.
 * 
 * @author henrichen
 * @since 2.0_7
 */
public class Gpolygon extends Gpolyline {
	private static final long serialVersionUID = 200807091638L;
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
			smartRerender();
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
			smartRerender();
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
			smartRerender();
		}
	}
	
	/**
	 * Returns fill opacity from 0 (transparent) to 100 (solid), default to 50.
	 * @return fill opacity level
	 */
	public int getFillOpacity() {
		return _fillOpacity;
	}
	
	/**
	 * Sets fill opacity from 0 (transparent) to 100 (solid), default to 50.
	 * @param op the fill opacity level 
	 */
	public void setFillOpacity(int op) {
		if (op < 0 || op > 100) {
			throw new UiException("Fill opacity must be between 0 to 100 (inclusive): "+op);
		}
		if (_fillOpacity != op) {
			_fillOpacity = op;
			smartRerender();
		}
	}
	
	public String getEncodedPolyline() {
		if (_encodedPolyline == null) {
			int lat = 0;
			int lng = 0;
			final StringBuffer sb = new StringBuffer(_path.size()*4);
			int lat0 = 0;
			int lng0 = 0;
			int j = 0;
			for(final Iterator it = _path.iterator(); it.hasNext(); ++j) {
				LatLng latLng = (LatLng) it.next();
				final int tlat = e5(latLng.getLatitude());
				final int tlng = e5(latLng.getLongitude());
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
	
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		render(renderer, "fill", isFill());
		render(renderer, "outline", isOutline());
		render(renderer, "fillColor", getFillColor());
		render(renderer, "fillOpacity", new Double(getFillOpacity() / 100.0));
	}
	
	protected void prepareRerender(Map info) {
		super.prepareRerender(info);
		
		info.put("fill", Boolean.valueOf(isFill()));
		info.put("outline", Boolean.valueOf(isOutline()));
		info.put("fillColor", getFillColor());
		info.put("fillOpacity", new Double(getFillOpacity() / 100.0));
	}
}
