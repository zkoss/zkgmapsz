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

import org.zkoss.json.JSONObject;

/**
 * A rectangle bound in geographical coordinates used by Google Maps.
 * 
 * @author RaymondChao
 * @since 3.0.2
 */
public class LatLngBounds extends JSONObject {
	final LatLng _southWest, _northEast;
	
	/**
	 * Constructs a rectangle bound from the points at its south-west and north-east corners.
	 * @param southWest the south-west corner.
	 * @param northEast the north-east corner.
	 */
	public LatLngBounds(LatLng southWest, LatLng northEast) {
		this.put("southWest", southWest);
		this.put("northEast", northEast);
		_southWest = southWest;
		_northEast = northEast;
	}
	
	/**
	 * Returns the point at the north-east corner of the bound.
	 * @return the point at the north-east corner of the bound.
	 */
	public LatLng getNorthEast() {
		return _northEast;
	}
	
	/**
	 * Returns the point at the south-west corner of the bound.
	 * @return the point at the south-west corner of the bound.
	 */
	public LatLng getSouthWest() {
		return _southWest;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if(obj== null || getClass() != obj.getClass())
			return false;
		LatLngBounds other = (LatLngBounds) obj;
	    if ((_northEast == null && other.getNorthEast() != null) ||
	    		(_southWest == null && other.getSouthWest() != null))
	    	return false;
	    
	    return _northEast.equals(other.getNorthEast()) && _southWest.equals(other.getSouthWest());
	}
	
    public int hashCode() {
        return (_southWest != null ? _southWest.hashCode() : 0) * 31 +
				(_northEast != null ? getNorthEast().hashCode() : 0);
    }
}
