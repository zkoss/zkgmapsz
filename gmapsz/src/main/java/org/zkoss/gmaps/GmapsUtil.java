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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;
import org.zkoss.json.parser.ParseException;
import org.zkoss.zk.ui.UiException;

/**
 * Utility class regrding {@link Gmaps}.
 * @author henrichen
 * @since 2.0_10
 */
public class GmapsUtil {
	private static String GOOGLE_WEB_SERVICE_GEOCODE_JSON = "https://maps.googleapis.com/maps/api/geocode/json?";
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
	/** Returns the bounds per the given center latitude, longitude, 
	 * view port width, view port height, and zoomLevel.
	 * 
	 * @param latLng the center latitude and longitude
	 * @param width the view port width
	 * @param height the view port height
	 * @param zoomLevel the zoom level
	 * @return bounds of the given center 
	 */
	public static LatLngBounds getBounds(LatLng latLng, int width, int height, int zoomLevel) {
		final int base = 256 << zoomLevel; //zoomLevel 0 -> 256, 1 -> 512

		final int[] centerxy = latlngToXy(latLng.getLatitude(), latLng.getLongitude(), zoomLevel);
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
		
		return new LatLngBounds(new LatLng(sw[0], sw[1]), new LatLng(ne[0], ne[1]));
	}
	/**
	 * Get Geocode Service Response by the given address
	 * @param address String, the given address
	 * @param sensor boolean, the value of Google Map sensor property
	 * @param language String, the language code
	 * @param apiKey Google Api Key
	 * @return StringBuilder, contains the response content
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @since 3.0.0
	 */
	public static StringBuilder getGeocodeJsonResult(String address, boolean sensor, String language, String apiKey)
		throws ParseException, UnsupportedEncodingException, MalformedURLException, IOException {
		// build the url path for request
		String path = GOOGLE_WEB_SERVICE_GEOCODE_JSON
				+ "address="+java.net.URLEncoder.encode(address, "UTF-8")
				+ "&sensor="+sensor
				+ "&language="+language
				+ (apiKey != null ? ("&key=" + apiKey) : "");
		StringBuilder sb = getResponse(path);
		return sb;
	}
	/**
	 * Get Geocode Service Response by the given Lat/Lng
	 * @param lat double, the given latitude
	 * @param lng double, the given longitude
	 * @param sensor boolean, the value of Google Map sensor property
	 * @param language String, the language code
	 * @param apiKey Google Api Key
	 * @return StringBuilder, contains the response content
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @since 3.0.0
	 */
	public static StringBuilder getGeocodeJsonResult(double lat, double lng, boolean sensor, String language, String apiKey)
		throws ParseException, UnsupportedEncodingException, MalformedURLException, IOException {
		// build the url path for request
		String path = GOOGLE_WEB_SERVICE_GEOCODE_JSON
				+"latlng="+lat+","+lng
				+"&sensor="+sensor
				+"&language="+language
				+ (apiKey != null ? ("&key=" + apiKey) : "");
		StringBuilder sb = getResponse(path);
		return sb;
	}
	/**
	 * Get Latitude/Longitude by given address
	 * @param address String, the given address
	 * @param sensor boolean, the value of Google Map sensor property
	 * @param language String, the language code
	 * @param apiKey Google Api Key
	 * @return latlng double array, latlng[0] contains the Latitude, latlng[1] contains the Longitude
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @since 3.0.0
	 */
	public static double[] getLatlngByAddress(String address, boolean sensor, String language, String apiKey)
		throws ParseException, UnsupportedEncodingException, MalformedURLException, IOException {
		StringBuilder sb = getGeocodeJsonResult(address, sensor, language, apiKey);
		double[] latlng = new double[2];

		JSONArray results = (JSONArray)((JSONObject)new JSONParser().parse(sb.toString())).get("results");
		JSONObject location = (JSONObject)((JSONObject)((JSONObject)results.get(0)).get("geometry")).get("location");
		latlng[0] = ((Double)location.get("lat")).doubleValue();
		latlng[1] = ((Double)location.get("lng")).doubleValue();
		return latlng;
	}
	/**
	 * Get Bounds by given address
	 * @param address String, the given address
	 * @param sensor boolean, the value of Google Map sensor property
	 * @param language String, the language code
	 * @param apiKey Google Api Key
	 * @return double[] with double[0] swlat, double[1] swlng, double[2] nelat, double[3] nelng
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @since 3.0.0
	 */
	public static double[] getBoundsByAddress(String address, boolean sensor, String language, String apiKey)
		throws ParseException, UnsupportedEncodingException, MalformedURLException, IOException {
		StringBuilder sb = getGeocodeJsonResult(address, sensor, language, apiKey);
		double[] bounds = new double[4];
	
		JSONArray results = (JSONArray)((JSONObject)new JSONParser().parse(sb.toString())).get("results");
		JSONObject boundsInfo = (JSONObject)((JSONObject)((JSONObject)results.get(0)).get("geometry")).get("bounds");
		bounds[0] = ((Double)((JSONObject)boundsInfo.get("southwest")).get("lat")).doubleValue();
		bounds[1] = ((Double)((JSONObject)boundsInfo.get("southwest")).get("lng")).doubleValue();
		bounds[2] = ((Double)((JSONObject)boundsInfo.get("northeast")).get("lat")).doubleValue();
		bounds[3] = ((Double)((JSONObject)boundsInfo.get("northeast")).get("lng")).doubleValue();
	
		return bounds;
	}
	/**
	 * Get address by given Latitude/Longitude
	 * @param lat double, the given latitude
	 * @param lng double, the given longitude
	 * @param sensor boolean, the value of Google Map sensor property
	 * @param language String, the language code
	 * @param apiKey Google Api Key
	 * @return String, the address
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @since 3.0.0
	 */
	public static String getAddressByLatlng(double lat, double lng, boolean sensor, String language, String apiKey)
		throws ParseException, UnsupportedEncodingException, MalformedURLException, IOException {
		StringBuilder sb = getGeocodeJsonResult(lat, lng, sensor, language, apiKey);
		String address = null;
		
		JSONArray results = (JSONArray)((JSONObject)new JSONParser().parse(sb.toString())).get("results");
		address = (String)((JSONObject)results.get(1)).get("formatted_address");
		return address;
	}
	private static StringBuilder getResponse(String path) throws MalformedURLException, IOException{
		java.net.URL url = new java.net.URL(path);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-agent", "Mozilla/5.0");

        conn.setRequestProperty("Accept-Charset", "UTF-8"); // encoding
        conn.setReadTimeout(10000);// timeout limit
        conn.connect();// connect
        int status = conn.getResponseCode();

        switch (status) {
            case java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT://504 timeout
                break;
            case java.net.HttpURLConnection.HTTP_FORBIDDEN://403 forbidden
                break;
            case java.net.HttpURLConnection.HTTP_INTERNAL_ERROR://500 server error
                break;
            case java.net.HttpURLConnection.HTTP_NOT_FOUND://404 not exist
                break;
            case java.net.HttpURLConnection.HTTP_OK: // ok
                InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF-8");

                int ch;
                StringBuilder sb = new StringBuilder("");
                while((ch = reader.read())!= -1){
                    sb.append((char)ch);
                }
                return sb;
        }
        return null;
    }
}
