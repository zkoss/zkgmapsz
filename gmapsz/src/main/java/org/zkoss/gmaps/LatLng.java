/* LatLng.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Dec 3 10:32:46     2013, Created by RaymondChao
}}IS_NOTE

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 3.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import java.util.Objects;

import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;

/**
 * A point in geographical coordinates represented by latitude and longitude.
 * 
 * @author RaymondChao
 * @since 3.0.2
 */
public class LatLng implements JSONAware {
	final private double _latitude, _longitude;
	
	public LatLng(double latitude, double longitude) {
		_latitude = latitude;
		_longitude = longitude;
	}

	public double getLatitude() {
		return _latitude;
	}
	
	public double getLongitude() {
		return _longitude;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LatLng latLng = (LatLng) o;
		return Double.compare(latLng._latitude, _latitude) == 0 && Double.compare(latLng._longitude, _longitude) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_latitude, _longitude);
	}

	public String toJSONString() {
		return "{latitude:" + _latitude  + ",longitude:" + _longitude + "}";
	}

	/**
	 * produces the JSON corresponding to the gmaps API
	 * <a href="https://developers.google.com/maps/documentation/javascript/reference/coordinates#LatLngLiteral">
	 *     google.maps.LatLngLiteral
	 * </a>
	 * @return google.maps.LatLngLiteral JSON String
	 */
	public String toLatLngLiteral() {
		return "{lat:" + _latitude  + ",lng:" + _longitude + "}";
	}

	/**
	 * build a LatLng object from a JSON object corresponding to the gmaps API
	 * <a href="https://developers.google.com/maps/documentation/javascript/reference/coordinates#LatLngLiteral">
	 *     google.maps.LatLngLiteral
	 * </a>
	 * @return latlng
	 */
	public static LatLng fromLatLngLiteral(JSONObject json) {
		return new LatLng(
				((Number)json.get("lat")).doubleValue(),
				((Number)json.get("lng")).doubleValue()
		);
	}
}
