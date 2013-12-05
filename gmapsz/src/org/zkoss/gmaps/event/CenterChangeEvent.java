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

import org.zkoss.gmaps.LatLng;

/**
 * Represents a Google Maps {@link org.zkoss.gmaps.Gmaps} related event which 
 * is triggered whenever the center is moved.
 * 
 * @author benbai
 * @since 3.0.0
 */
public class CenterChangeEvent extends Event {
	private final LatLng _center, _oldCenter;

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
		return new CenterChangeEvent(request.getCommand(), comp, new LatLng(lat, lng), new LatLng(oldlat, oldlng));
	}
	
	/** Constructs a Google Maps center change event.
	 */
	public CenterChangeEvent(String name, Component target, LatLng center, LatLng oldCenter) {
		super(name, target);
		_center = center;
		_oldCenter = oldCenter;
	}

	/** Constructs a Google Maps center change event.
	 */
	public CenterChangeEvent(String name, Component target, double lat, double lng, double oldlat, double oldlng) {
		this(name, target, new LatLng(lat, lng), new LatLng(oldlat, oldlng));
	}
	
	/** Returns the latitude of the center after moved.
	 * @deprecated As of release 3.0.2, use {@link #getCenter()} instead.
	 */
	public double getLat() {
		return _center.getLatitude();
	}
	
	/** Returns the longitude of the center after moved.
	 * @deprecated As of release 3.0.2, use {@link #getCenter()} instead.
	 */
	public double getLng() {
		return _center.getLongitude();
	}
	
	/** Returns the latitude of the center before moved.
	 * @deprecated As of release 3.0.2, replaced with {@link #getOldCenter()} instead.
	 */
	public double getOldLat() {
		return _oldCenter.getLatitude();
	}
	
	/** Returns the longitude of the center before moved.
	 * @deprecated As of release 3.0.2, replaced with {@link #getOldCenter()} instead.
	 */
	public double getOldLng() {
		return _oldCenter.getLongitude();
	}
	
	/** Returns the center after moved.
	 *  @return center the center after moved.
	 *  @since 3.0.2
	 */
	public LatLng getCenter() {
		return _center;
	}
	
	/** Returns the center before moved.
	 *  @return center the center before moved.
	 *  @since 3.0.2
	 */
	public LatLng getOldCenter() {
		return _oldCenter;
	}
}
