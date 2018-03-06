/* MapTypeChangeEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Apr 16, 2009 9:52:08 AM, Created by henrichen
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/

package org.zkoss.gmaps.event;

import java.util.Map;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents a Google Maps {@link org.zkoss.gmaps.Gmaps} related event which
 * is triggered whenever the map type is changed.
 *
 * @author henrichen
 *
 */
public class MapTypeChangeEvent extends Event {
	private final String _type;

	public static final MapTypeChangeEvent getMapTypeChangeEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();
		if (data == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
					new Object[] {data, request});
		final String type = (String)data.get("type");
		return new MapTypeChangeEvent(request.getCommand(), comp, type);
	}
	
	/** Constructs a Google Maps map type relevant event.
	 */
	public MapTypeChangeEvent(String name, Component target, String type) {
		super(name, target);
		_type = type;
	}
	/** Returns the map type name of the Google Map after map type changed.
	 */
	public final String getType() {
		return _type;
	}
}
