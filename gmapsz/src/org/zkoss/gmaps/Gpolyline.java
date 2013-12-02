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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.zkoss.gmaps.event.PathChangeEvent;
import org.zkoss.lang.Objects;
import org.zkoss.util.CollectionsX;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
	protected List<Tuple> _points = new ArrayList<Tuple>(32);
	private String _strpoints;
	private String _color = "#808080"; //default to dark gray
	private int _weight = 5;  //default to 5
	private int _opacity = 50; //default to 50/100
	private int _numLevels = 4; //default to 4
	private int _zoomFactor = 32; //zoom factor between polyline levels (2^5 if _numLevels == 4).
	protected String _encodedPolyline; //cached value
	protected String _encodedLevels; //cached value
	private boolean _editable = false;
	private boolean _smartUpdatePoly; //whether post the smartUpdate event already?
	
	static {
		addClientEvent(Gpolyline.class, "onPathChange", CE_IMPORTANT|CE_DUPLICATE_IGNORE);
	}

	public Gpolyline() {
		addEventListener("onSmartUpdatePoly", new EventListener() {
			public void onEvent(Event evt) {
				_smartUpdatePoly = false; //reset
				smartRerender();
			}
		});
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
	 * @param lng the longtitude
	 * @param level the polyline level(default to 0 - 3)
	 */
	public void addPoint(double lat, double lng, int level) {
		if (lat > 90.0 || lat < -90.0) {
			throw new IllegalArgumentException("latitude must be from -90 ~ +90: "+lat);
		}
		if (lng > 180.0 || lng < -180.0) {
			throw new IllegalArgumentException("longtitude must be from -180 ~ +180: "+lng);
		}
		if (level < 0 || level >= _numLevels) {
			throw new IllegalArgumentException("level must be from 0 ~ " + (_numLevels - 1) + ": " + level);
		}
		_points.add(new Tuple(lat, lng, level));

		clearCacheAndSmartUpdate();
	}
	/**
	 * mark a flag to inform that this Gpolyline needs update.
	 */
	protected void smartUpdatePoly() {
		if (_smartUpdatePoly) { //already mark smart draw
			return;
		}
		_smartUpdatePoly = true;
		Events.postEvent("onSmartUpdatePoly", this, null);
	}
	
	private void clearCacheAndSmartUpdate() {
		_encodedPolyline = null; //cached value
		_encodedLevels = null; //cached value
		smartUpdatePoly();
	}
	/**
	 * Set polyline in lat,lng,level tuple(double, double, int); 
	 * e.g. "lat1,lng1,level1,lat2,lng2,level2,lat3,lng3,level3, ...".
	 * @param points
	 */
	public void setPoints(String points) {
		if (!Objects.equals(points, _strpoints)) {
			_strpoints = points;
			if (points != null) {
				final Collection list = CollectionsX.parse(null, points, ',');
				for(final Iterator it = list.iterator(); it.hasNext();) {
					double lat = Double.parseDouble((String)it.next());
					double lng = Double.parseDouble((String)it.next());
					int level = Integer.parseInt((String)it.next());
					addPoint(lat, lng, level);
				}
			}

			clearCacheAndSmartUpdate();
		}
	}

	private String[] getPointsAndLevels() {
		return new String[] {getEncodedPolyline(), getEncodedLevels()};
	}
	
	public String getEncodedPolyline() {
		if (_encodedPolyline == null) {
			int lat = 0;
			int lng = 0;
			final StringBuffer sb = new StringBuffer(_points.size()*4);
			for(final Iterator it = _points.iterator(); it.hasNext();) {
				Tuple tuple = (Tuple) it.next();
				final int tlat = e5(tuple.lat);
				final int tlng = e5(tuple.lng);
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
	
	public String getEncodedLevels() {
		if (_encodedLevels == null) {
			final StringBuffer sb = new StringBuffer(_points.size()*2);
			for(final Iterator it = _points.iterator(); it.hasNext();) {
				Tuple tuple = (Tuple) it.next();
				sb.append(encodeInt(tuple.level));
			}
			_encodedLevels = sb.length() == 0 ? "?" : sb.toString();
		}
		return _encodedLevels;
	}
	
	protected static String encodeLatLng(int e5) {
		boolean sign = e5 < 0;
		e5 <<=1; //shift left
		if (sign) e5 ^= 0xffffffff;
		return encodeInt(e5);
	}
	
	protected static String encodeInt(int x) {
		final StringBuffer sb = new StringBuffer(6);
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
	 * @param encodedPolyline
	 * @Since 3.0.2
	 */
	public void setEncodedPolylineByClient(String encodedPolyline) {
		if (!Objects.equals(encodedPolyline, _encodedPolyline)) {
			_encodedPolyline = encodedPolyline;
			_points = getDecodedPolyline(_encodedPolyline);
		}
	}
	
	protected List<Tuple> getDecodedPolyline(String encodedPolyline) {
		List<Tuple> points = new LinkedList<Tuple>();
		List<Double> values = decodeLatLngs(encodedPolyline); 
		int length = values.size();
		if (length > 2) {
			points.add(new Tuple(values.get(0),values.get(1), 3));
			for (int i = 2; i < length - 1; i += 2) {
				points.add(new Tuple(values.get(i) + points.get(i/2 - 1).lat, values.get(i + 1) + points.get(i/2 - 1).lng, 3));
			}
			return points;
		} else 
			return new ArrayList<Tuple>(32);
	}
	
	protected static List<Double> decodeLatLngs(String encodedPolyline) {
		int length = encodedPolyline.length();
		int index = 0, value = 0, shift = 0;
		List<Double> decodeLatLngs = new LinkedList<Double>();
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
		return decodeLatLngs;
	}
	
	protected static double decodeInt(int x) {
		boolean sign = ((x & 1) != 0);
		double value = sign ? ~(x >> 1) : (x >> 1);
		return value / 1e5;
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

	/**
	 * Returns the number of polyline levels.
	 * @return the number of polyline levels.
	 */
	public int getNumLevels() {
		return _numLevels;
	}
	
	/**
	 * Sets the number of polyline levels.
	 * @param numLevels the number of levels.
	 */
	public void setNumLevels(int numLevels) {
		if (numLevels < 1 || numLevels > 19) {
			throw new IllegalArgumentException("numLevels must be from 1 ~ 19: " + numLevels);
		}
		if (_numLevels != numLevels) {
			this._numLevels = numLevels;
			
			//calculate proper zoomFactor
			int factor = 19/numLevels;
			int mod = 19%numLevels;
			
			//if mod is larger than numLevels / 2
			_zoomFactor = 2 << ((mod > (numLevels >> 1)) ? (factor + 1) : factor);
			
			smartRerender();
		}
	}
	
	/** Returns whether can edit the Gpolyline by mouse; default to false.
	 * @return whether can edit the Gpolyline by mouse; default to false.
	 * @Sinec 3.0.2
	 */
	public boolean isEditable () {
		return _editable;
	}
	
	/** Sets whether can edit the Gpolyline by mouse; default to false.
	 * @param editable whether enable edit the Gpolyline by mouse.
	 * @Sinec 3.0.2
	 */
	public void setEditable (boolean editable) {
		if (_editable != editable) {
			_editable = editable;
			smartUpdate("editable", _editable);
		}
	}
	
	/**
	 * Returns the zoomFactor (zoomFactor change per the numLevels).
	 */
	public int getZoomFactor() {
		return _zoomFactor;
	}
	
	private int[] getNumLevelsAndZoomFactor() {
		return new int[] {getNumLevels(), getZoomFactor()};
	}

	protected static class Tuple implements java.io.Serializable {
		private static final long serialVersionUID = 200807041531L;
		public double lat;
		public double lng;
		public int level;
		public Tuple(double lat, double lng, int level) {
			this.lat = lat;
			this.lng = lng;
			this.level = level;
		}
	}
	
	protected void prepareRerender(Map info) {
		info.put("pointsAndLevels", getPointsAndLevels());
		info.put("numLevelsAndZoomFactor", getNumLevelsAndZoomFactor());
		info.put("color", getColor());
		info.put("weight", new Integer(getWeight()));
		info.put("opacity", new Double(getOpacity()/100.0));
		info.put("editable", isEditable());
	}

	protected void smartRerender() {
		final Map info = new HashMap();
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
		
		render(renderer, "pointsAndLevels", getPointsAndLevels());
		render(renderer, "numLevelsAndZoomFactor", getNumLevelsAndZoomFactor());
		render(renderer, "color", getColor());
		render(renderer, "weight", new Integer(getWeight()));
		render(renderer, "opacity", new Double(getOpacity()/100.0));
		render(renderer, "editable", isEditable());
	}
	/** Processes an AU request.
	 *
	 * <p>Default: in addition to what are handled by {@link XulElement#service},
	 * it also handles onClick.
	 * @since 3.0.2
	 */
	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if ("onPathChange".equals(cmd)) {
			final PathChangeEvent evt = PathChangeEvent.getPathChangeEvent(request);
			setEncodedPolylineByClient(evt.getPath());
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}
}
