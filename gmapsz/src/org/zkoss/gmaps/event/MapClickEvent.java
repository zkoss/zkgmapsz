/* MapClickEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Mon Jun 2 11:10:17     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps.event;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.Gpolygon;
import org.zkoss.gmaps.Gpolyline;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.MouseEvent;

/**
 * Represents a Google Maps {@link org.zkoss.gmaps.Gmaps} related event which 
 * is triggered whenever the map is clicked on.
 * 
 * @author henrichen
 */
public class MapClickEvent extends MouseEvent {
	private final Component _ref;
	private final double _lat, _lng;
	private final int _clientX, _clientY;

	/**
	 * Constructs a Google Maps click event.
	 * @param name the event name.
	 * @param target the event target (should be a Maps component such as {@link Gmaps}.
	 * @param x x coordinate related to target component.
	 * @param y y coordinate related to target component.  
	 * @param keys a combination of {@link #CTRL_KEY}, {@link #SHIFT_KEY}
	 * and {@link #ALT_KEY}.
	 * @param ref the reference component that triggers this event.
	 * @param lat the latitude clicked on
	 * @param lng the longitude clicked on
	 */
	public MapClickEvent(String name, Component target, Component ref, 
	double lat, double lng, int x, int y, int clientX, int clientY, int keys) { 
		super(name, target, x, y, keys);
		_ref = ref;
		_clientX = clientX;
		_clientY = clientY;
		_lat = lat;
		_lng = lng;
	}
	
	/**
	 * Constructs a Google Maps click event.
	 * @param name the event name
	 * @param target the event target (should be a Maps component such as {@link Gmaps}.
	 * @param gmarker the reference Gmarker that triggers this event.
	 * @param lat the latitude clicked on
	 * @param lng the longitude clicked on
	 * @deprecated
	 */
	public MapClickEvent(String name, Component target, Gmarker gmarker, double lat, double lng) {
		this(name, target, gmarker, lat, lng, 0, 0, 0, 0, 0);
	}

	/** Returns the clicked Gmarker or null if not click on a Gmarker.
	 * @see #getReference
	 */
	public Gmarker getGmarker() {
		final Component ref = getReference();
		return ref instanceof Gmarker ? (Gmarker) ref : null;
	}
	
	/** Returns the clicked Component ({@link Gmarker}, {@link Gpolyline}, {@link Gpolygon}) 
	 * or null if not click on a Component.
	 * @return the clicked Component ({@link Gmarker}, {@link Gpolyline}, {@link Gpolygon})
	 * @since 2.0_9
	 */
	public Component getReference() {
		return _ref;
	}
	
	/** Not appliable to this event; it always returns null. 
	 * @since 2.0_9 
	 */
	public String getArea() {
		return null;
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
	 * @since 2.0_9 
	 */
	public final int getClientX() {
		return _clientX;
	}
	
	/** Get the y in pixels related to the browser client
	 * @since 2.0_9 
	 */
	public final int getClientY() {
		return _clientY;
	}
}
