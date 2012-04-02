/* CenterChangeEvent.java
{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Mon Mar 05 14:51:13     2012, Created by benbai
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

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
 * Represents a Google Maps {@link org.zkoss.gmaps.*} related event which 
 * is triggered whenever the center is moved.
 * 
 * @author benbai
 * @since 3.0.0
 */
public class CenterChangeEvent extends Event {
	private final double _lat, _lng, _oldlat, _oldlng;

	/** Converts an AU request to a event.
	 * @since 5.0.0
	 */
	public static final CenterChangeEvent getCenterChangeEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();
		if (data == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
				new Object[] {data, request});

		final double lat = ((Number)data.get("lat")).doubleValue();
		final double lng = ((Number)data.get("lng")).doubleValue();
		final double oldlat = ((Number)data.get("oldlat")).doubleValue();
		final double oldlng = ((Number)data.get("oldlng")).doubleValue();
		return new CenterChangeEvent(request.getCommand(), comp, lat, lng, oldlat, oldlng);
	}

	/** Constructs a Google Maps center change event.
	 */
	public CenterChangeEvent(String name, Component target, double lat, double lng, double oldlat, double oldlng) {
		super(name, target);
		_lat = lat;
		_lng = lng;
		_oldlat = oldlat;
		_oldlng = oldlng;
	}
	/** Returns the latitude of the center after moved.
	 */
	public double getLat() {
		return _lat;
	}
	/** Returns the longitude of the center after moved.
	 */
	public double getLng() {
		return _lng;
	}
	/** Returns the latitude of the center before moved.
	 */
	public double getOldLat() {
		return _oldlat;
	}
	/** Returns the longitude of the center before moved.
	 */
	public double getOldLng() {
		return _oldlng;
	}
}
