/* MapClickEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Jan 19 16:08:51     2007, Created by henrichen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps.event;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.zk.ui.Component;

/**
 * A generic mouse event for Maps engine such as {@link Gmaps}.
 * @author henrichen
 * @since 2.0_9
 */
public class MapMouseEvent extends MapClickEvent {
	/**
	 * Constructs a Google Maps click event.
	 * @param name the event name
	 * @param target the event target (should be a Maps component such as {@link Gmaps}.
	 * @param ref the reference component that triggers this event.
	 * @param lat the latitude clicked on
	 * @param lng the longitude clicked on
	 * @param x the x clicked on related to the Maps.
	 * @param y the y clicked on related to the Maps.
	 * @param clientX the x clicked on related to the browser client.
	 * @param clientY the y clicked on related to the browser client.
	 * @param keys a combination of {@link #CTRL_KEY}, {@link #SHIFT_KEY}
	 * and {@link #ALT_KEY}.
	 */
	public MapMouseEvent(String name, Component target, Component ref, 
		double lat, double lng, int x, int y, int clientX, int clientY, int keys) {
		super(name, target, ref, lat, lng, x, y, clientX, clientY, keys);
	}
}
