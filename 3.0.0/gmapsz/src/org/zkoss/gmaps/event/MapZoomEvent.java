/* MapZoomEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Oct 13 14:51:13     2006, Created by henrichen
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

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
 * is triggered whenever the zoom level is changed.
 *
 * @author henrichen
 */
public class MapZoomEvent extends Event {
	private final int _zoom;

	/** Converts an AU request to a event.
	 * @since 5.0.0
	 */
	public static final MapZoomEvent getMapZoomEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();
		if (data == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
					new Object[] {data, request});

		final int zoom = ((Integer)data.get("zoom")).intValue();
		return new MapZoomEvent(request.getCommand(), comp, zoom);
	}
	
	/** Constructs a Google Maps zoom level relevant event.
	 */
	public MapZoomEvent(String name, Component target, int zoom) {
		super(name, target);
		_zoom = zoom;
	}
	/** Returns the zoom level of the Google Map after zoomed in or zoomed out.
	 */
	public final int getZoom() {
		return _zoom;
	}
}
