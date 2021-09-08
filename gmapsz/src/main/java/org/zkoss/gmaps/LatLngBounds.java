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

import org.zkoss.json.JSONAware;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * A rectangle bound in geographical coordinates used by Google Maps.
 * 
 * @author RaymondChao
 * @since 3.0.2
 */
public class LatLngBounds implements JSONAware {
	final LatLng _southWest, _northEast;
	
	/**
	 * Constructs a rectangle bound from the points at its south-west and north-east corners.
	 * @param southWest the south-west corner.
	 * @param northEast the north-east corner.
	 */
	public LatLngBounds(@Nonnull LatLng southWest, @Nonnull LatLng northEast) {
		if(southWest == null || northEast == null) {
			throw new IllegalArgumentException("southWest/northEast cannot be NULL");
		}
		_southWest = southWest;
		_northEast = northEast;
	}
	
	public LatLng getNorthEast() {
		return _northEast;
	}
	
	public LatLng getSouthWest() {
		return _southWest;
	}

	@Override
	public String toJSONString() {
		return "{southWest:" + _southWest.toJSONString() + ",northEast:" + _northEast.toJSONString() + "}";
	}

	public String toLatLngBoundsLiteral() {
		return "{south:" + _southWest.getLatitude() +
				",west:" + _southWest.getLongitude() +
				",north:" + _northEast.getLatitude() +
				",east:" + _northEast.getLongitude() + "}";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LatLngBounds that = (LatLngBounds) o;
		return _southWest.equals(that._southWest) && _northEast.equals(that._northEast);
	}

	@Override
	public int hashCode() {
		return Objects.hash(_southWest, _northEast);
	}
}
