/* MapMoveEvent.java

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

import org.zkoss.gmaps.LatLng;
import org.zkoss.gmaps.LatLngBounds;

/**
 * Represents a Google Maps {@link org.zkoss.gmaps.Gmaps} related event which 
 * is triggered whenever the center is moved or zoom level is changed.
 * 
 * @author henrichen
 */
public class MapMoveEvent extends Event {
	private final LatLng _latLng;
	private final LatLngBounds _bounds;

	/** Converts an AU request to a event.
	 * @since 5.0.0
	 */
	public static final MapMoveEvent getMapMoveEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();
		if (data == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
				new Object[] {data, request});

		//bug #2930047 Gmaps Exception when set lat/lng to integer
		final double lat = ((Number)data.get("lat")).doubleValue();
		final double lng = ((Number)data.get("lng")).doubleValue();
		final double swlat = ((Number)data.get("swlat")).doubleValue();
		final double swlng = ((Number)data.get("swlng")).doubleValue();
		final double nelat = ((Number)data.get("nelat")).doubleValue();
		final double nelng = ((Number)data.get("nelng")).doubleValue();
		return new MapMoveEvent(request.getCommand(), comp, new LatLng(lat, lng),
				new LatLngBounds( new LatLng(swlat, swlng), new LatLng(nelat, nelng)));
	}

	/** Constructs a Google Maps moving relevant event.
	 */
	public MapMoveEvent(String name, Component target, LatLng latLng, LatLngBounds bounds) {
		super(name, target);
		_latLng = latLng;
		_bounds = bounds;
	}
	
	/** Constructs a Google Maps moving relevant event.
	 */
	public MapMoveEvent(String name, Component target, double lat, double lng, double swlat, double swlng, double nelat, double nelng) {
		this(name, target, new LatLng(lat, lng), new LatLngBounds( new LatLng(swlat, swlng), new LatLng(nelat, nelng)));
	}
	
	/** Returns the latitude and longitude of the Google Map center after moved.
	 * @since 3.0.2
	 */
	public LatLng getLatLng() {
		return _latLng;
	}
	
	/** Returns the latitude of the Google Map center after moved.
	 * @deprecated As of release 3.0.2, use {@link #getLatLng()} instead.
	 */
	public double getLat() {
		return _latLng.getLatitude();
	}
	
	/** Returns the longitude of the Google Map center after moved.
	 * @deprecated As of release 3.0.2, use {@link #getLatLng()} instead.
	 */
	public double getLng() {
		return _latLng.getLongitude();
	}
	
	/** Returns the bounds of the Google Map after moved.
	 * @since 3.0.2
	 */
	public LatLngBounds getBounds() {
		return _bounds;
	}
	
	/**
	 * Returns the bounded south west latitude after moved.
	 * @return the bounded south west latitude after moved.
	 * @since 2.0_8
	 * @deprecated As of release 3.0.2, use {@link #getBounds()} instead.
	 */
	public double getSwLat() {
		return _bounds.getSouthWest().getLatitude();
	}
	/**
	 * Returns the bounded south west longitude after moved.
	 * @return the bounded south west longitude after moved.
	 * @since 2.0_8
	 * @deprecated As of release 3.0.2, use {@link #getBounds()} instead.
	 */
	public double getSwLng() {
		return _bounds.getSouthWest().getLongitude();
	}
	/**
	 * Returns the bounded north east latitude after moved.
	 * @return the bounded north east latitude after moved.
	 * @since 2.0_8
	 * @deprecated As of release 3.0.2, use {@link #getBounds()} instead.
	 */
	public double getNeLat() {
		return _bounds.getNorthEast().getLatitude();
	}
	/**
	 * Returns the bounded north east longitude after moved.
	 * @return the bounded north east longitude after moved.
	 * @since 2.0_8
	 * @deprecated As of release 3.0.2, use {@link #getBounds()} instead.
	 */
	public double getNeLng() {
		return _bounds.getNorthEast().getLongitude();
	}
}
