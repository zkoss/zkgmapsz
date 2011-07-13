/* MapOpenEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Aug 12 9:08:56     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.OpenEvent;

/**
 * @author henrichen
 *
 */
public class MapOpenEvent extends OpenEvent {
	double _lat;
	double _lng;
	
	/** Constructs an onOpen event for a context menu, a tooltip or a popup.
	 *
	 * @param target the component being opened
	 * @param open whether an open
	 * @param ref the component that causes target to be opened.
	 * @param lat the latitude the context menu open on the Gmaps
	 * @param lng the longitude the context menu open on the Gmaps
	 */
	public MapOpenEvent(String name, Component target, boolean open,
	Component ref, Object value, double lat, double lng) {
		super(name, target, open, ref);
		_lat = lat;
		_lng = lng;
	}
	
	public double getLat() {
		return _lat;
	}
	
	public double getLng() {
		return _lng;
	}
}
