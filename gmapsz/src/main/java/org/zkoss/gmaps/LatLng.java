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

import org.zkoss.json.JSONObject;

/**
 * A point in geographical coordinates represented by latitude and longitude.
 * 
 * @author RaymondChao
 * @since 3.0.2
 */
public class LatLng extends JSONObject {
	final private double _latitude, _longitude;
	
	/**
	 * Construct a point with latitude and longitude.
	 * @param latitude the latitude of the point.
	 * @param longitude the longitude of the point.
	 */
	public LatLng(double latitude, double longitude) {
		this(latitude, longitude, false);
	}
	/**
	 * Construct a point with latitude and longitude.
	 * @param latitude the latitude of the point.
	 * @param longitude the longitude of the point.
	 * @param unbound the set to true if want to enable values outside of this range.
	 */
	public LatLng(double latitude, double longitude, boolean unbound) {
		this.put("latitude", new Double(latitude));
		this.put("longitude", new Double(longitude));
		// Not use yet.
		//this.put("unbound", unbound);
		_latitude = latitude;
		_longitude = longitude;
	}
	
	/**
	 * Returns the latitude in degrees.
	 */
	public double getLatitude() {
		return _latitude;
	}
	
	/**
	 * Returns the longitude in degrees.
	 */
	public double getLongitude() {
		return _longitude;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if(obj == null || getClass() != obj.getClass())
	        return false;
		LatLng other = (LatLng) obj;
		return _latitude == other.getLatitude() && _longitude == other.getLongitude();
	}
	
	public int hashCode() {
    	return Double.valueOf(_latitude).hashCode() * 31 + Double.valueOf(_longitude).hashCode();
	}
}
