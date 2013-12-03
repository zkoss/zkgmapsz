/* LatLngBounds.java

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

public class LatLngBounds {
	private LatLng _northEast, _southWest;
	public LatLngBounds(LatLng southWest, LatLng northEast) {
		_southWest = southWest;
		_northEast = northEast;
	}
	
	public LatLng getNorthEast() {
		return _northEast;
	}
	
	public LatLng getSouthWest() {
		return _southWest;
	}
	
	public boolean equals(LatLngBounds other) {
		
	    if (other == null || (_northEast == null && other.getNorthEast() != null) ||
	    		(_southWest == null && other.getSouthWest() != null))
	    	return false;
	    
	    return _northEast.equals(other.getNorthEast()) && _southWest.equals(other.getSouthWest());
	}
	
	public String toString() {
		return _southWest.toString() + ", " + _northEast.toString();
	}
	
	public String toUrlValue(int precision) {
		return _southWest.toUrlValue(precision) + "," + _northEast.toUrlValue(precision);
	}
}
