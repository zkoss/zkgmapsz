/* Gimage.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Aug 7, 2008 1:04:23 PM, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import org.zkoss.zk.ui.UiException;

/**
 * Utility class regrding {@link Gmaps}.
 * @author henrichen
 * @since 2.0_10
 */
public class GmapsUtil {
	/** Convert the latitude and longitude to x, y pixels. This is based on the Mercator 
	 * Projection (check http://en.wikipedia.org/wiki/Mercator_projection) as used
	 * in Google Maps
	 * @param lat the latitude in degree
	 * @param lng the longitude in degree
	 * @param zoomLevel the zoom level of the map
	 * @return int[] with int[0] the x and int[1] the y
	 */
	public static int[] latlngToXy(double lat, double lng, int zoomLevel) {
		if (lat > 90.0 || lat < -90.0)
			throw new UiException("latitude must be in the range of -90 degree ~ 90 degree");
		if (lng > 180.0 || lng < -180.0)
			throw new UiException("longitude must be in the range of -180 degree ~ 180 degree");
		
		//No reason, mimic the behavior of Google Maps
		if (lat > 89.2) lat = 89.2;
		else if (lat < -89.2) lat = -89.2;
		
		final int[] out = new int[2];
		final int base = 256 << zoomLevel;
		final int zero = base >> 1;
		out[0] = (int) (base * lng / 360.0) + zero ; //x
		out[1] = zero - (int) Math.round(Math.log(Math.tan(Math.PI/4.0 + 0.5 * lat * Math.PI / 180.0)) * zero / Math.PI); //y
		return out;
	}
	/** Convert the x, y pixels to latitude and longitude. This is based on the Mercator 
	 * Projection (check http://en.wikipedia.org/wiki/Mercator_projection) as used
	 * in Google Maps. 
	 * @param x the x in pixels
	 * @param y the y in pixels
	 * @param zoomLevel the zoom level of the map
	 * @return double[] with double[0] the latitude and double[1] the longitude
	 */
	public static double[] xyToLatlng(int x, int y, int zoomLevel) {
		final int base = 256 << zoomLevel; //zoomLevel 0 -> 256, 1 -> 512
		final int zero = base >> 1;
		final double[] out = new double[2];
		//360 degree of lng == base
		out[1] = (x - zero) * 360.0 / base; //longitude
		if (out[1] > 180) out[1] -= 360.0;
		else if (out[1] < -180) out[1] += 360.0;
		
		//90 degree of lat == Pi == half base of y
		out[0] = Math.atan(Math.exp((zero - y) * Math.PI / zero)) * 360.0 / Math.PI - 90.0; //latitude 
		
		return out;
	}
	/** Returns the bounds per the given center latitude, longitude, 
	 * view port width, view port height, and zoomLevel.
	 * 
	 * @param lat the center latitude
	 * @param lng the center longitude
	 * @param width the view port width
	 * @param height the view port height
	 * @param zoomLevel the zoom level
	 * @return double[] with double[0] swlat, double[1] swlng, double[2] nelat, double[3] nelng
	 */
	public static double[] getBounds(double lat, double lng, int width, int height, int zoomLevel) {
		final int base = 256 << zoomLevel; //zoomLevel 0 -> 256, 1 -> 512

		final int[] centerxy = latlngToXy(lat, lng, zoomLevel);
		int westx = centerxy[0] - width / 2;
		if (westx < 0) westx = 0;
		int eastx = westx + width;
		if (eastx > base) eastx = base;
		int northy = centerxy[1] - height / 2;
		int southy = northy + height;
		final double[] sw = xyToLatlng(westx, southy, zoomLevel);
		final double[] ne = xyToLatlng(eastx, northy, zoomLevel);
		final double[] out = new double[4];
		out[0] = sw[0];
		out[1] = sw[1];
		out[2] = ne[0];
		out[3] = ne[1];
		
		return out;
	}
}
