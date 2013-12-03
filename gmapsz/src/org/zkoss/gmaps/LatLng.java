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

import java.text.NumberFormat;

public class LatLng {
	private double _latitude, _longitude;
	private boolean _unbound;
	
	public LatLng(double latitude, double longitude) {
		this(latitude, longitude, false);
	}
	
	public LatLng(double latitude, double longitude, boolean unbound) {
		_latitude = latitude;
		_longitude = longitude;
		_unbound = unbound;
	}
	
	public double setLatitude() {
		return _latitude;
	}

	public double getLatitude() {
		return _latitude;
	}

	public double getLongitude() {
		return _longitude;
	}
	
	public boolean equals(LatLng other) {
		if (other == null)
			return false;
		return other.getLatitude() == _latitude && other.getLongitude() == _longitude;
	}
	
	public String toString() {
		return Double.toString(_latitude) + ", " + Double.toString(_longitude);
	}
	
	public String toUrlValue(int precision) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(precision);
		return numberFormat.format(_latitude) + "," + numberFormat.format(_longitude);
	}
	
}
