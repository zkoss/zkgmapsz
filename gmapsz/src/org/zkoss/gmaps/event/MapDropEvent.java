/* MapDropEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Jul 17 18:36:10     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;

/**
 * When drag and drop on a {@link org.zkoss.gmaps.Gmaps} or 
 * {@link org.zkoss.gmaps.Gmarker}; this event was sent to the dropped 
 * component.
 *  
 * @author henrichen
 * @since 2.0_9
 * @see MapMouseEvent
 */
public class MapDropEvent extends DropEvent {
	private final double _lat, _lng;
	private final int _clientX, _clientY;

	/** Constructs a drop event.
	 * @param dragged The component being dragged and drop to {@link #getTarget}.
	 */
	public MapDropEvent(String name, Component target, Component dragged, 
		double lat, double lng, int x, int y, int clientX, int clientY, int keys) {
		super(name, target, dragged, x, y, keys);
		_clientX = clientX;
		_clientY = clientY;
		_lat = lat;
		_lng = lng;
	}
	
	/** Returns the latitude of the clicked position.
	 */
	public double getLat() {
		return _lat;
	}
	
	/** Returns the longitude of the clicked position.
	 */
	public double getLng() {
		return _lng;
	}
	
	/** Get the x in pixels related to the browser client
	 */
	public final int getClientX() {
		return _clientX;
	}
	
	/** Get the y in pixels related to the browser client
	 */
	public final int getClientY() {
		return _clientY;
	}
}
