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

import java.util.Map;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.Gpolygon;
import org.zkoss.gmaps.Gpolyline;
import org.zkoss.gmaps.LatLng;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.mesg.MZk;

/**
 * A generic mouse event for Maps engine such as {@link Gmaps}.
 * @author henrichen
 * @since 2.0_9
 */
public class MapMouseEvent extends MouseEvent {
	private final Component _ref;
	private final LatLng _latLng;

	/** Converts an AU request to a event.
	 * @since 5.0.0
	 */
	public static final MapMouseEvent getMapMouseEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();
		if (data == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
					new Object[] {data, request});

		final Component ref = request.getDesktop().getComponentByUuidIfAny((String)data.get("reference"));
		//bug #2930047 Gmaps Exception when set lat/lng to integer
		final double lat = ((Number)data.get("lat")).doubleValue();
		final double lng = ((Number)data.get("lng")).doubleValue();
		//bug #2167700 FF3 only. Exception when click on ZK Pet Shop Maps
		final Number xn = (Number) data.get("x");
		final Number yn = (Number) data.get("y");
		final Number pxn = (Number) data.get("pageX");
		final Number pyn = (Number) data.get("pageY");
		final int x = xn instanceof Double ? (int) Math.round(((Double)xn).doubleValue()) : xn.intValue();
		final int y = yn instanceof Double ? (int) Math.round(((Double)yn).doubleValue()) : yn.intValue();
		final int pageX = pxn instanceof Double ? (int) Math.round(((Double)pxn).doubleValue()) : pxn.intValue();
		final int pageY = pyn instanceof Double ? (int) Math.round(((Double)pyn).doubleValue()) : pyn.intValue();
		return new MapMouseEvent(request.getCommand(), comp, 
				ref, new LatLng(lat, lng), x, y, pageX, pageY, AuRequests.parseKeys(data));
	}
	
	
	/**
	 * Constructs a Google Maps click event.
	 * @param name the event name
	 * @param target the event target (should be a Maps component such as {@link Gmaps}.
	 * @param ref the reference component that triggers this event.
	 * @param latLng the latitude and longitude clicked on
	 * @param x the x clicked on related to the Maps.
	 * @param y the y clicked on related to the Maps.
	 * @param pageX the x clicked on related to the whole document.
	 * @param pageY the y clicked on related to the whole document.
	 * @param keys a combination of {@link #CTRL_KEY}, {@link #SHIFT_KEY}
	 * and {@link #ALT_KEY}.
	 */
	public MapMouseEvent(String name, Component target, Component ref,
			LatLng latLng, int x, int y, int pageX, int pageY, int keys) {
		super(name, target, x, y, pageX, pageY, keys);
		_ref = ref;
		_latLng = latLng;
	}
	
	/**
	 * Constructs a Google Maps click event.
	 * @param name the event name
	 * @param target the event target (should be a Maps component such as {@link Gmaps}.
	 * @param ref the reference component that triggers this event.
	 * @param lat the latitude clicked on
	 * @param lng the longitude clicked on
	 * @param x the x clicked on related to the Maps.
	 * @param y the y clicked on related to the Maps.
	 * @param pageX the x clicked on related to the whole document.
	 * @param pageY the y clicked on related to the whole document.
	 * @param keys a combination of {@link #CTRL_KEY}, {@link #SHIFT_KEY}
	 * and {@link #ALT_KEY}.
	 */
	public MapMouseEvent(String name, Component target, Component ref, 
		double lat, double lng, int x, int y, int pageX, int pageY, int keys) {
		this(name, target, ref, new LatLng(lat, lng), x, y, pageX, pageY, keys);
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
	
	/** Returns the latitude and longitude of the clicked position.
	 * @Since 3.0.2
	 */
	public LatLng getLatLng() {
		return _latLng;
	}
	
	/** Returns the latitude of the clicked position.
	 * @deprecated As of release 3.0.2, replaced with {@link MapMouseEvent#getLatLng()} instead.
	 */
	public double getLat() {
		return _latLng.getLatitude();
	}
	
	/** Returns the longitude of the clicked position.
	 * @deprecated As of release 3.0.2, replaced with {@link MapMouseEvent#getLatLng()} instead.
	 */
	public double getLng() {
		return _latLng.getLongitude();
	}
}
