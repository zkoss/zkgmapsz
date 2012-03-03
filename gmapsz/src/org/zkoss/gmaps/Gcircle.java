/* Gpolyline.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Aug 9, 2007 4:38:11 PM, Created by henrichen
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.lang.Strings;
import org.zkoss.util.CollectionsX;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.impl.XulElement;
/**
 * Google Maps support Gpolyline.
 * 
 * @author henrichen
 * @since 2.0_6
 */
public class Gcircle extends XulElement implements Mapitem {
	private static final long serialVersionUID = 7919622962923909687L;
	private double _lat;
	private double _lng;
	public boolean _clickable = true;
	public boolean _editable;
	public String _fillColor;
	public int _fillOpacity = 30;
	
	public double getLat () {
		return _lat;
	}
	public void setLat (double lat) {
		if (_lat != lat) {
			_lat = lat;
			smartUpdate("center", getCenter());
		}
	}

	public double getLng () {
		return _lng;
	}
	public void setLng (double lng) {
		if (_lng != lng) {
			_lng = lng;
			smartUpdate("center", getCenter());
		}
	}
	/** Returns the Circle's center in double[] array where [0] is lat and [1] is lng;
	 * used by component developers only.
	 */
	private double[] getCenter() {
		return new double[] {_lat, _lng};
	}

	public boolean isClickable () {
		return _clickable;
	}
	public void setClickable (boolean clickable) {
		if (_clickable != clickable) {
			_clickable = clickable;
			smartUpdate("clickable", _clickable);
		}
	}

	public boolean isEditable () {
		return _editable;
	}
	public void setEditable (boolean editable) {
		if (_editable != editable) {
			_editable = editable;
			smartUpdate("editable", _editable);
		}
	}

	public String getFillColor () {
		return _fillColor;
	}
	public void setFillColor (String fillColor) {
		if (!Objects.equals(_fillColor, fillColor)) {
			_fillColor = fillColor;
			smartUpdate("fillColor", _fillColor);
		}
	}

	public int getFillOpacity() {
		return _fillOpacity;
	}
	
	public void setFillOpacity(int fillOpacity) {
		if (fillOpacity < 0 || fillOpacity > 100) {
			throw new UiException("Fill opacity must be between 0 to 100 (inclusive): "+fillOpacity);
		}
		if (_fillOpacity != fillOpacity) {
			_fillOpacity = fillOpacity;
			smartUpdate("fillOpacity", _fillOpacity);
		}
	}
	

	public boolean isChildable() {
		return false;
	}
	
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		if (_lat != 37.4419 || _lng != -122.1419)
			render(renderer, "center", getCenter());
		if (!_clickable)
			renderer.render("clickable", _clickable);
		if (_editable)
			renderer.render("editable", _editable);
		if (_fillOpacity != 30)
			renderer.render("fillOpacity", _fillOpacity);
	}
}
