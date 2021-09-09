/* Gpolyline.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Aug 9, 2007 4:38:11 PM, Created by henrichen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/

package org.zkoss.gmaps;

import java.util.*;

import org.zkoss.gmaps.event.PathChangeEvent;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONAware;
import org.zkoss.json.JSONObject;
import org.zkoss.lang.Objects;
import org.zkoss.lang.Strings;
import org.zkoss.util.CollectionsX;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.impl.XulElement;
/**
 * Google Maps support Gpolyline.
 * 
 * @author henrichen
 * @since 2.0_6
 */
public class Gpolyline extends XulElement implements Mapitem {
	private static final long serialVersionUID = 200807041530L;
	protected List<LatLng> _path = new ArrayList<>();
	private String _strpath;
	private String _color = "#808080"; //default to dark gray
	private int _weight = 5;  //default to 5
	private int _opacity = 50; //default to 50/100
	private boolean _pathEncoded = true;
	protected String _encodedPolyline; //cached value
	private boolean _editable = false;

	static {
		addClientEvent(Gpolyline.class, "onPathChange", CE_IMPORTANT|CE_DUPLICATE_IGNORE);
	}

	public Gpolyline() {
	}
	/**
	 * Add polyline axix point.
	 * <p>Note the polyline "level" does not correspond directly to a zoom level but they are related. 
	 * More accurately, the numLevels parameter divides up the existing zoom levels (currently 18) 
	 * into groups of zoom levels and polyline level is confined by the numLevels. For example, the
	 * default numLevels value is 4, so the polyline level can be 0 to 3 only. The Google maps zoom 
	 * levels are grouped together into the following 4 levels (determined by numLevels):</p>
	 * <table>
	 * <th><td>Polyline Level</td><td>Zoom Levels</td></th>
	 * <tr><td>Level 0</td><td>Zoom levels 0-5</td></tr>
	 * <tr><td>Level 1</td><td>Zoom levels 6-10</td></tr>
	 * <tr><td>Level 2</td><td>Zoom levels 11-14</td></tr>
	 * <tr><td>Level 3</td><td>Zoom levels 15-18</td></tr>
	 * </table>
	 * <p>An polyline level value of 3 in this case would ensure that a point appears in all zoom level, 
	 * while a level of 0 will ignore points specified beyond zoom level 5. The endpoints of a polyline 
	 * should often be set to the largest encoded level, to ensure that the endpoints will appear in 
	 * all zoom levels and the line is drawn correctly.</p>
	 * <p>A general rule is that whenever the level of the end point is greater or equal to the current Gmaps level,
	 * that end point is appeared; otherwise, it should be skipped.</p> 
	 * 
	 * @param lat the latitude
	 * @param lng the longitude
	 * @param level the polyline level(default to 0 - 3)
	 * @deprecated As of release 3.0.2, replaced with {@link #addPath(LatLng)} instead.
	 */
	public void addPoint(double lat, double lng, int level) {
		if (lat > 90.0 || lat < -90.0) {
			throw new IllegalArgumentException("latitude must be from -90 ~ +90: " +lat);
		}
		if (lng > 180.0 || lng < -180.0) {
			throw new IllegalArgumentException("longtitude must be from -180 ~ +180: "+lng);
		}
		addPath(new LatLng(lat, lng));
	}
	
	/**
	 * Add polyline axix point.
	 * @param latLng the latitude and longitude of the point
	 * @since 3.0.2
	 */
	public void addPath(LatLng latLng) {
		_path.add(latLng);
		clearCacheAndSmartUpdatePth();
	}
	
	/**
	 * Set polyline in lat,lng,level tuple(double, double, int); 
	 * e.g. "lat1,lng1,level1,lat2,lng2,level2,lat3,lng3,level3, ...".
	 * @param points
	 * @deprecated As of release 3.0.2, replaced with {@link #setPath(String)} instead.
	 */
	public void setPoints(String points) {
		if (Strings.isEmpty(points)) {
			setPath("");
		} else {
			String[] arr = points.split(",");
			int length = arr.length;
			final StringBuilder sb = new StringBuilder();
			for(int i = 0; i < length; sb.append(","), i += 3) {
				sb.append(arr[i]).append(",").append(arr[i+1]);
			}
			setPath(sb.toString());
		}
	}
	
	/**
	 * Sets ordered sequence of coordinates of the Polyline.
	 * e.g. "lat1,lng1,lat2,lng2,lat3,lng3, ...".
	 * @param path the path of the Polyline.
	 * @since 3.0.2
	 */
	public void setPath(String path) {
		if (!Objects.equals(path, _strpath)) {
			_strpath = path;
			if (path != null) {
				_path.clear();
				final Collection<String> list = CollectionsX.parse(null, path, ',');
				for(final Iterator<String> it = list.iterator(); it.hasNext();) {
					double lat = Double.parseDouble(it.next());
					double lng = Double.parseDouble(it.next());
					_path.add(new LatLng(lat, lng));
				}
			}
			clearCacheAndSmartUpdatePth();
		}
	}

	/**
	 * Sets polyline in list
	 * @param path the List of LatLng
	 * @since 3.0.2
	 */
	public void setPath(List<LatLng> path) {
		if(!Objects.equals(_path, path)) {
			_path = path;
			clearCacheAndSmartUpdatePth();
		}
	}
	
	/**
	 * Returns the polyline path in list
	 * @return the List of LatLng
	 * @since 3.0.2
	 */
	public List<LatLng> getPath() {
		return _path;
	}

	private void clearCacheAndSmartUpdatePth() {
		_encodedPolyline = null;
		if(this._pathEncoded) {
			smartUpdate("path", getEncodedPolyline());
		} else {
			smartUpdate("path", pathToLatLngLiteralArrayJson);
		}
	}

	public String getEncodedPolyline() {
		if (_encodedPolyline == null) {
			int lat = 0;
			int lng = 0;
			final StringBuilder sb = new StringBuilder(_path.size()*4);
			for (LatLng latLng : _path) {
				final int tlat = e5(latLng.getLatitude());
				final int tlng = e5(latLng.getLongitude());
				sb.append(encodeLatLng(tlat - lat)).append(encodeLatLng(tlng - lng));
				lat = tlat;
				lng = tlng;
			}
			_encodedPolyline = sb.length() == 0 ? "??" : sb.toString();
		}
		return _encodedPolyline;
	}
	
	protected int e5(double db) {
		return (int) Math.floor(db * 100000);
	}

	protected static String encodeLatLng(int e5) {
		boolean sign = e5 < 0;
		e5 <<=1; //shift left
		if (sign) e5 ^= 0xffffffff;
		return encodeInt(e5);
	}
	
	protected static String encodeInt(int x) {
		final StringBuilder sb = new StringBuilder(6);
		do {
			int chunk = (x & 0x1f);
			x >>>= 5;
			if (x != 0) {
				chunk |= 0x20;
			}
			chunk += 63;
			sb.append((char)chunk);
		} while(x != 0);
		return sb.toString();
	}
	
	/**
	 * Set encoded path to polyline and decode path to 
	 * e.g. "_p~iF~ps|U_ulLnnqC_mqNvxq`@".
	 * @param encodedPolyline the encoded path of Polyline. 
	 * @since 3.0.2
	 */
	public void setEncodedPolylineByClient(String encodedPolyline) {
		if (!Objects.equals(encodedPolyline, _encodedPolyline)) {
			_encodedPolyline = encodedPolyline;
			decodePolyline(_encodedPolyline);
		}
	}
	
	protected void decodePolyline(String encodedPolyline) {
		Double[] values = getDecodeLatLngs(encodedPolyline);
		int length = values.length;
		_path.clear();
		if (length > 2) {
			_path.add(new LatLng(values[0], values[1]));
			for (int i = 2; i < length - 1; i += 2) {
				final double preLat = (_path.get(i/2 - 1)).getLatitude();
				final double preLng = (_path.get(i/2 - 1)).getLongitude();
				_path.add(new LatLng(values[i] + preLat, values[i+1] + preLng));
			}
		}
	}
	
	protected static Double[] getDecodeLatLngs(String encodedPolyline) {
		int length = encodedPolyline.length();
		int index = 0, value = 0, shift = 0;
		List<Double> decodeLatLngs = new ArrayList<>();
		while (index < length) {
			int chunk = ((int) encodedPolyline.charAt(index ++)) - 63;
			value |= (chunk & 0x1f) << shift;
			shift += 5;
			if ((chunk & 0x20) == 0) {
				decodeLatLngs.add(decodeInt(value));
				value = 0;
				shift = 0;
			}
		}
		return decodeLatLngs.toArray(new Double[0]);
	}
	
	protected static Double decodeInt(int x) {
		boolean sign = ((x & 1) != 0);
		double value = sign ? ~(x >> 1) : (x >> 1);
		return new Double(value / 1e5);
	}
	
	/**
	 * Returns the line color in form of #RRGGBB, default to #808080.
	 * @return the color
	 */
	public String getColor() {
		return _color;
	}

	/**
	 * Sets the line color in form of #RRGGBB, default to #808080.
	 * @param color the color to set
	 */
	public void setColor(String color) {
		if (color == null) color = "#808080";
		if (!color.equals(_color)) {
			this._color = color;
			smartRerender();
		}
	}

	/**
	 * Returns the line weight(width) 1 - 10, default to 5.
	 * @return the line weight
	 */
	public int getWeight() {
		return _weight;
	}

	/**
	 * Sets the line weight(width) 1 - 10, default to 5.
	 * @param weight the line weight
	 */
	public void setWeight(int weight) {
		if (weight != _weight) {
			this._weight = weight;
			smartRerender();
		}
	}
	
	/**
	 * Returns the line opacity 0 - 100, default to 50.
	 * @return the opacity
	 * @since 2.0_7
	 */
	public int getOpacity() {
		return _opacity;
	}

	/**
	 * Sets the line opacity 0 - 100, default to 50.
	 * @param opacity the line opacity to set
	 * @since 2.0_7
	 */
	public void setOpacity(int opacity) {
		if (opacity < 0 || opacity > 100) {
			throw new UiException("Line opacity must be between 0 to 100 (inclusive): "+opacity);
		}
		if (opacity != _opacity) {
			this._opacity = opacity;
			smartRerender();
		}
	}

	/** Returns whether can edit the Gpolyline by mouse; default to false.
	 * @return whether can edit the Gpolyline by mouse; default to false.
	 * @since 3.0.2
	 */
	public boolean isEditable () {
		return _editable;
	}
	
	/** Sets whether can edit the Gpolyline by mouse; default to false.
	 * @param editable whether enable edit the Gpolyline by mouse.
	 * @since 3.0.2
	 */
	public void setEditable (boolean editable) {
		if (_editable != editable) {
			_editable = editable;
			smartUpdate("editable", _editable);
		}
	}

	/**
	 * @see #setPathEncoded(boolean)
	 * @return
	 */
	public boolean isPathEncoded() {
		return _pathEncoded;
	}

	/**
	 * <p>Toggle between <a href="https://developers.google.com/maps/documentation/utilities/polylinealgorithm">lossy polygon encoding (compression)</a>
	 * or raw <a href="https://developers.google.com/maps/documentation/javascript/reference/coordinates#LatLngLiteral">LatLngLiteral</a>s
	 * (for maximum precision).</p>
	 * <p>Especially when a polygon is editable (at high zoom levels) it is useful to preserve the exact coordinates without compression.</p>
	 * <p>For backwards compatibility the path encoding is enabled by default.</p>
	 *
	 * @param pathEncoded
	 * @since 3.3.0
	 */
	public void setPathEncoded(boolean pathEncoded) {
		if (_pathEncoded != pathEncoded) {
			_pathEncoded = pathEncoded;
			smartUpdate("pathEncoded", _pathEncoded);
		}
	}

	public void setPathByClient(JSONArray latLngLiteralArray) {
		this._path = latLngLiteralArrayToPath(latLngLiteralArray);
	}

	private JSONAware pathToLatLngLiteralArrayJson = new JSONAware() {
		@Override
		public String toJSONString() {
			List<LatLng> path = getPath();
			StringBuilder sb = new StringBuilder("[");
			for (LatLng latLng : path) {
				sb.append(latLng.toLatLngLiteral()).append(",");
			}
			sb.setCharAt(sb.length() - 1, ']');
			return sb.toString();
		}
	};

	private static List<LatLng> latLngLiteralArrayToPath(JSONArray jsonArray) {
		List<LatLng> path = new ArrayList<>(jsonArray.size());
		for(Object jsonObject : jsonArray) {
			path.add(LatLng.fromLatLngLiteral((JSONObject) jsonObject));
		}
		return path;
	}

	protected void prepareRerender(Map<String, Object> info) {
		info.put("path", this.isPathEncoded() ? getEncodedPolyline() : pathToLatLngLiteralArrayJson);
		info.put("color", getColor());
		info.put("weight", getWeight());
		info.put("opacity", getOpacity()/100.0);
		info.put("editable", isEditable());
		info.put("pathEncoded", isPathEncoded());
	}

	protected void smartRerender() {
		final Map<String, Object> info = new HashMap<>();
		prepareRerender(info);
		smartUpdate("rerender_", info);
	}
	
	//-- Component --//
	/** Default: not childable.
	 */
	public boolean isChildable() {
		return false;
	}
	
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);
		render(renderer, "path", isPathEncoded() ? getEncodedPolyline() : pathToLatLngLiteralArrayJson);
		render(renderer, "color", getColor());
		render(renderer, "weight", getWeight());
		render(renderer, "opacity", getOpacity() / 100.0);
		render(renderer, "editable", isEditable());
		if (!isPathEncoded()) {
			renderer.render("pathEncoded", isPathEncoded());
		}
	}
	/** Processes an AU request.
	 *
	 * <p>Default: in addition to what are handled by {@link org.zkoss.zk.ui.HtmlBasedComponent#service(AuRequest, boolean)}},
	 * it also handles onClick.
	 * @since 3.0.2
	 */
	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if ("onPathChange".equals(cmd)) {
			final PathChangeEvent evt = PathChangeEvent.getPathChangeEvent(request);
			if(evt.isPathEncoded()) {
				setEncodedPolylineByClient(evt.getEncodedPath());
			} else {
				setPathByClient(evt.getRawPath());
			}
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}
}
