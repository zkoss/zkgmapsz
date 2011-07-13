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

/**
 * Represents a Google Maps {@link org.zkoss.gmaps.Gmaps} related event which 
 * is triggered whenever the center is moved or zoom level is changed.
 * 
 * @author henrichen
 */
public class MapMoveEvent extends Event {
	private final double _lat, _lng, _swlat, _swlng, _nelat, _nelng;

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
		return new MapMoveEvent(request.getCommand(), comp, lat, lng, swlat, swlng, nelat, nelng);
	}

	/** Constructs a Google Maps moving relevant event.
	 */
	public MapMoveEvent(String name, Component target, double lat, double lng, double swlat, double swlng, double nelat, double nelng) {
		super(name, target);
		_lat = lat;
		_lng = lng;
		_swlat = swlat;
		_swlng = swlng;
		_nelat = nelat;
		_nelng = nelng;
	}
	/** Returns the latitude of the Google Map center after moved.
	 */
	public double getLat() {
		return _lat;
	}
	/** Returns the longitude of the Google Map center after moved.
	 */
	public double getLng() {
		return _lng;
	}
	/**
	 * Returns the bounded south west latitude after moved.
	 * @return the bounded south west latitude after moved.
	 * @since 2.0_8
	 */
	public double getSwLat() {
		return _swlat;
	}
	/**
	 * Returns the bounded south west longitude after moved.
	 * @return the bounded south west longitude after moved.
	 * @since 2.0_8
	 */
	public double getSwLng() {
		return _swlng;
	}
	/**
	 * Returns the bounded north east latitude after moved.
	 * @return the bounded north east latitude after moved.
	 * @since 2.0_8
	 */
	public double getNeLat() {
		return _nelat;
	}
	/**
	 * Returns the bounded north east longitude after moved.
	 * @return the bounded north east longitude after moved.
	 * @since 2.0_8
	 */
	public double getNeLng() {
		return _nelng;
	}
}
