/* Gmaps.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Oct 12 16:35:20     2006, Created by henrichen
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.zkoss.gmaps.event.InfoChangeEvent;
import org.zkoss.gmaps.event.MapDataEvent;
import org.zkoss.gmaps.event.MapDataListener;
import org.zkoss.gmaps.event.MapDropEvent;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.gmaps.event.MapMoveEvent;
import org.zkoss.gmaps.event.MapTypeChangeEvent;
import org.zkoss.gmaps.event.MapZoomEvent;
import org.zkoss.json.JSONObject;
import org.zkoss.lang.Objects;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.impl.XulElement;


/**
 * The component used to represent Maps based on the
 * <a href="https://developers.google.com/maps/documentation/javascript/">Google Maps JS API</a>
 *
 * @author henrichen
 * @version $Revision: 1.6 $ $Date: 2006/03/31 08:38:55 $
 */
public class Gmaps extends XulElement {
	private static final long serialVersionUID = 200807040842L;
	private static final String KEY = "key";
	private static final String CLIENT = "client";
	private static final String CHANNEL = "channel";
	private static final String LIBRARIES = "libraries";
	private static final String LANGUAGE = "language";
	private static final String SENSOR = "sensor";
	private static final String REGION = "region";
	private transient Ginfo _oneinfo; //the only one Ginfo child of this Gmaps.
    //TODO: GMap should support multiple GInfo in the future.
	private transient Ginfo _info; //current opened info window, null means none is open.

	private LatLng _center = new LatLng(37.4419, -122.1419);
	private LatLngBounds _bounds = new LatLngBounds(
								new LatLng(37.418026932311111, -122.1933746338),
								new LatLng(37.4657298516, -122.0903778076));
	private int _zoom = 13;
	private boolean _large;
	private boolean _small;
	private boolean _type = true;
	private boolean _smallZoom = true;
	private boolean _pan = true; // PanControl
	private boolean _scale;
	private boolean _overview;
	
	private boolean _normal = true;
	private boolean _satellite;
	private boolean _hybrid = true;
	private boolean _physical = true;
	
	private String _mapType = "normal";
	private boolean _enableDragging = true;
	private boolean _continuousZoom;
	private boolean _doubleClickZoom = true;
	private boolean _scrollWheelZoom = true;
	private boolean _enableGoogleBar;
	private String _baseDomain;
	private JSONObject _gmapsApiConfigParams = new JSONObject(); 
	{
		_gmapsApiConfigParams.put(LIBRARIES, "geometry");
	}
	private String _protocol;
	
	private MapModel _model;
	private MapitemRenderer _renderer;
	private MapDataListener _dataListener;
	private EventListener<Event> _moveListener; //used by MapModel live data
	private Map<Object, Mapitem> _dataMap = new HashMap<Object, Mapitem>(64); //data object -> Mapitem
	private Component _selected;

	private String _version = "3";

	/** Sets the center of the Google Maps.
	 * @param lat latitude of the Google Maps center
	 * @param lng longitude of the Google Maps center
	 */
	public void setCenter(double lat, double lng) {
		setCenter(new LatLng(lat, lng));
	}
	
	/** Sets the center of the Google Maps.
	 * @param center the Google Maps center
	 * @since 3.0.2
	 */
	public void setCenter(LatLng center) {
		if (center == null) {
			throw new NullPointerException("center");
		}
		if (!Objects.equals(_center, center)) {
			_center = center;
			smartUpdate("center", center);
		}
	}
	
	/** Returns the current center of the Google Maps.
	 * @return center the Google Maps center
	 * @since 3.0.2
	 */
	public LatLng getCenter() {
		return _center;
	}
	
	/** Sets the <a href="https://developers.google.com/maps/documentation/javascript/get-api-key#client-id">'client ID' Gmaps API parameter</a>.
	 * @param client client ID of the Google Maps
	 * @since 3.0.2
	 */
	public void setClient(String client) {
		setGmapsApiConfigParam(CLIENT, client);
	}
	
	/** Returns the <a href="https://developers.google.com/maps/documentation/javascript/get-api-key#client-id">'channel' Gmaps API parameter</a>.
	 * @return channel of the Google Maps API
	 * @since 3.0.5
	 */
	public String getClient() {
		return getGmapsApiConfigParam(CLIENT);
	}
	
	/** Returns the <a href="https://developers.google.com/maps/documentation/javascript/get-api-key#clientID-features">'client ID' Gmaps API parameter</a>.
	 * @return client ID of the Google Maps API
	 * @since 3.0.2
	 */
	public String getChannel() {
		return getGmapsApiConfigParam(CHANNEL);
	}

	/** Sets the <a href="https://developers.google.com/maps/documentation/javascript/get-api-key#clientID-features">'channel' Gmaps API parameter</a>.
	 * @param channel channel parameter of Google Maps API
	 * @since 3.0.5
	 */
	public void setChannel(String channel) {
		setGmapsApiConfigParam(CHANNEL, channel);
	}
	
	/** 
	 * Sets the <a href="https://developers.google.com/maps/documentation/javascript/get-api-key">Gmaps 'API key' parameter</a>.
	 * Alternatively you can set the <a href="https://www.zkoss.org/wiki/ZK_Component_Reference/Diagrams_and_Reports/Gmaps#Example">global JS variable 'zk.googleAPIkey'</a>
	 * @param key Gmaps API key of the Google Maps
	 * @since 3.0.5
	 */
	public void setKey(String key) {
		setGmapsApiConfigParam(KEY, key);
	}
	
	/** 
	 * Get the <a href="https://developers.google.com/maps/documentation/javascript/get-api-key">Gmaps 'API key' parameter</a>.
	 * Alternatively you can set the <a href="https://www.zkoss.org/wiki/ZK_Component_Reference/Diagrams_and_Reports/Gmaps#Example">global JS variable 'zk.googleAPIkey'</a>
	 * @return Gmaps API key of the Google Maps
	 * @since 3.0.5
	 */
	public String getKey() {
		return getGmapsApiConfigParam(KEY);
	}

	/**
	 * Sets an arbitrary additional url parameter/value to the request downloading the google maps api (https://maps.googleapis.com/maps/api/js?...).
	 * The purpose of this method is to support future introduced API parameters by google, not yet supported by ZK's gmaps java api.
	 * @param param
	 * @param value
	 */
	public void setGmapsApiConfigParam(String param, Object value) {
		if (!Objects.equals(getGmapsApiConfigParam(param), value)) {
			this._gmapsApiConfigParams.put(param, value);
			smartUpdate("gmapsApiConfig", this._gmapsApiConfigParams);
		}
	}

	/**
	 * Get the current value of a Gmaps API parameter set by {@link Gmaps#setGmapsApiConfigParam(String, Object)} 
	 * @param param 
	 * @return the value of the key
	 */
	@SuppressWarnings("unchecked")
	public <T> T getGmapsApiConfigParam(String param) {
		return (T)this._gmapsApiConfigParams.get(param);
	}
	
	/** Sets the current latitude of the Maps center.
	 * @param lat latitude of the Google Maps center
	 */
	public void setLat (double lat) {
		setCenter(new LatLng(lat, _center.getLongitude()));
	}
	
	/** Returns the current latitude of the Maps center.
	 * @return the current latitude of the Maps center.
	 */
	public double getLat() {
		return _center.getLatitude();
	}
	
	/** Sets the current longitude of the Maps center.
	 * @param lng the current longitude of the Maps center.
	 */
	public void setLng (double lng) {
		setCenter(new LatLng(_center.getLatitude(), lng));
	}
	
	/** Returns the currrent longitude of the Maps center.
	 * @return the currrent longitude of the Maps center.
	 */
	public double getLng() {
		return _center.getLongitude();
	}
	
	/** Sets the viewport to contain the given bounds.
	 * @param bounds the current viewport in Google Maps.
	 * @since 3.0.2
	 */
	public void fitBounds(LatLngBounds bounds) {
		if (bounds == null) {
			throw new NullPointerException("bounds");
		}
		if(!Objects.equals(_bounds, bounds)) {
			_bounds = bounds;
			syncModel();
			smartUpdate("bounds", bounds);
		}
	}
	
	/** Returns the current bounds of the Google Maps.
	 * @return bounds the current viewport in Google Maps.
	 * @since 3.0.2
	 */
	public LatLngBounds getBounds() {
		return _bounds;
	}
	
	/**
	 * Returns the bounded south west latitude.
	 * @return the bounded south west latitude.
	 * @since 2.0_8
	 * @deprecated As of release 3.0.2, replaced with {@link Gmaps#getSwlat()} instead.
	 */
	public double getSwLat() {
		return getSwlat();
	}
	
	/**
	 * Returns the bounded south west latitude.
	 * @return the bounded south west latitude.
	 * @since 3.0.2
	 */
	public double getSwlat() {
		return _bounds.getSouthWest().getLatitude();
	}
	
	/**
	 * Sets the bounded south west latitude.
	 * @param swlat south west latitude
	 * @since 3.0.2
	 */
	public void setSwlat(double swlat) {
		fitBounds(new LatLngBounds(new LatLng(swlat, _bounds.getSouthWest().getLongitude()), _bounds.getNorthEast()));
	}
	
	/**
	 * Returns the bounded south west longitude.
	 * @return the bounded south west longitude.
	 * @since 2.0_8
	 * @deprecated As of release 3.0.2, replaced with {@link Gmaps#getSwlng()} instead.
	 */
	public double getSwLng() {
		return getSwlng();
	}
	
	/**
	 * Returns the bounded south west longitude.
	 * @return the bounded south west longitude.
	 * @since 3.0.2
	 */
	public double getSwlng() {
		return _bounds.getSouthWest().getLongitude();
	}
	/**
	 * Sets the bounded south west longitude.
	 * @param swlng south west longitude
	 * @since 3.0.2
	 */
	public void setSwlng(double swlng) {
		fitBounds(new LatLngBounds(new LatLng(_bounds.getSouthWest().getLatitude(), swlng), _bounds.getNorthEast()));
	}
	/**
	 * Returns the bounded north east latitude.
	 * @return the bounded north east latitude.
	 * @since 2.0_8
	 * @deprecated As of release 3.0.2, replaced with {@link Gmaps#getNelat()} instead.
	 */
	public double getNeLat() {
		return getNelat();
	}
	/**
	 * Returns the bounded north east latitude.
	 * @return the bounded north east latitude.
	 * @since 3.0.2
	 */
	public double getNelat() {
		return _bounds.getNorthEast().getLatitude();
	}
	/**
	 * Sets the bounded north east latitude.
	 * @param nelat the bounded north east latitude.
	 * @since 3.0.2
	 */
	public void setNelat(double nelat) {
		fitBounds(new LatLngBounds(_bounds.getSouthWest(), new LatLng(nelat, _bounds.getNorthEast().getLongitude())));
	}
	/**
	 * Returns the bounded north east longitude.
	 * @return the bounded north east longitude.
	 * @since 2.0_8
	 * @deprecated As of release 3.0.2, replaced with {@link Gmaps#getNelng()} instead.
	 */
	public double getNeLng() {
		return getNelng();
	}
	/**
	 * Returns the bounded north east longitude.
	 * @return the bounded north east longitude.
	 * @since 3.0.2
	 */
	public double getNelng() {
		return _bounds.getNorthEast().getLongitude();
	}
	/**
	 * Sets the bounded north east longitude.
	 * @param nelng the bounded north east longitude.
	 * @since 3.0.2
	 */
	public void setNelng(double nelng) {
		fitBounds(new LatLngBounds(_bounds.getSouthWest(), new LatLng(_bounds.getNorthEast().getLatitude(), nelng)));
	}

	/** Pan to the new center of the Google Maps.
	 * @param lat latitude of the Google Maps center
	 * @param lng longitude of the Google Maps center
	 */
	public void panTo(double lat, double lng) {
		panTo(new LatLng(lat, lng));
	}
	
	/** Pan to the new center of the Google Maps.
	 * @param center the Google Maps center
	 * @since 3.0.2
	 */
	public void panTo(LatLng center) {
		if (!_center.equals(center)) {
			_center = center;
			smartUpdate("panTo_", center);
		}
	}
	
	/** Sets zoom level.
	 * @param zoom the zoom level (0-18)
	 */
	public void setZoom(int zoom) {
		if (zoom != _zoom) {
			_zoom = zoom;
			smartUpdate("zoom", zoom);
		}
	}
	
	/** Returns the current zoom level.
	 * @return the current zoom level.
	 */
	public int getZoom() {
		return _zoom;
	}
	
	/** Sets whether show the large Google Maps Control.
	 * @param b true to show the large Google Maps Control.
	 */
	public void setShowLargeCtrl(boolean b) {
		if (_large == b) {
			return;
		}
		_large = b;
		if (b) {
			setShowSmallCtrl(false);
			setShowZoomCtrl(false);
		}
		smartUpdate("showLargeCtrl", b);
	}
	
	/** Returns whether show the large Google Maps Control.
	 * @return whether show the large Google Maps Control.
	 */
	public boolean isShowLargeCtrl() {
		return _large;
	}
	
	/** Sets whether show the small Google Maps Control.
	 * @param b true to show the small Google Maps Control.
	 */
	public void setShowSmallCtrl(boolean b) {
		if (_small == b) {
			return;
		}
		_small = b;
		if (b) {
			setShowLargeCtrl(false);
			setShowZoomCtrl(false);
		}
		smartUpdate("showSmallCtrl", b);		
	}
	
	/** Returns whether show the large Google Maps Control.
	 * @return whether show the large Google Maps Control.
	 */
	public boolean isShowSmallCtrl() {
		return _small;
	}
	
	/** Sets whether show the small zoom Google Maps Control.
	 * @param b true to show the small zoom Google Maps Control.
	 * @since 2.0_7
	 */
	public void setShowZoomCtrl(boolean b) {
		if (_smallZoom == b) {
			return;
		}
		_smallZoom = b;
		if (b) {
			setShowLargeCtrl(false);
			setShowSmallCtrl(false);
		}
		smartUpdate("showZoomCtrl", b);		
	}
	
	/** Returns whether show the small zoom Google Maps Control.
	 * @return whether show the small zoom Google Maps Control.
	 * @since 2.0_7
	 */
	public boolean isShowZoomCtrl() {
		return _smallZoom;
	}
	
	/** Sets whether show the Google Maps type Control.
	 * @param b true to show the Google Maps type Control.
	 */
	public void setShowTypeCtrl(boolean b) {
		if (_type == b) {
			return;
		}
		_type = b;
		smartUpdate("showTypeCtrl", b);
	}

	/** Returns whether show the Google Maps type Control.
	 * @return whether show the Google Maps type Control.
	 */
	public boolean isShowTypeCtrl() {
		return _type;
	}
	
	/** Sets whether show the Google Maps pan Control.
	 * @param b true to show the Google Maps pan Control.
	 * @since 3.0.1
	 */
	public void setShowPanCtrl(boolean b) {
		if (_pan == b) {
			return;
		}
		_pan = b;
		smartUpdate("showPanCtrl", b);
	}

	/** Returns whether show the Google Maps pan Control, default to false.
	 * @return whether show the Google Maps pan Control.
	 * @since 3.0.1
	 */
	public boolean isShowPanCtrl() {
		return _pan;
	}
	
	/** Sets whether show the Google Maps scale Control.
	 * @param b true to show the Google Maps scale Control.
	 * @since 2.0_7
	 */
	public void setShowScaleCtrl(boolean b) {
		if (_scale == b) {
			return;
		}
		_scale = b;
		smartUpdate("showScaleCtrl", b);
	}

	/** Returns whether show the Google Maps scale Control, default to false.
	 * @return whether show the Google Maps scale Control.
	 * @since 2.0_7
	 */
	public boolean isShowScaleCtrl() {
		return _scale;
	}
	
	/** Sets whether show the Google Maps overview Control, default to false.
	 * @param b whether show the Google Maps overview Control.
	 * @since 2.0_7
	 */
	public void setShowOverviewCtrl(boolean b) {
		if (_overview == b) {
			return;
		}
		_overview = b;
		smartUpdate("showOverviewCtrl", b);
	}

	/** Returns whether show the Google Maps overview Control, default to false.
	 * @return whether show the Google Maps overview Control.
	 * @since 2.0_7
	 */
	public boolean isShowOverviewCtrl() {
		return _overview;
	}

	/** Sets whether support normal map, default to true.
	 * @param b whether support normal map, default to true.
	 * @since 2.0_7
	 */
	public void setNormal(boolean b) {
		if (_normal != b) {
			if (!b && "normal".equals(_mapType)) {
				if (isHybrid()) setMapType("hybrid");
				else if (isSatellite()) setMapType("satellite");
				else if (isPhysical()) setMapType("physical");
				else return; //cannot do it if no more map!
			}
			_normal = b;
			smartUpdate("normal", b);
		}
	}
	
	/** Returns whether support normal map, default to true.
	 * @return whether support normal map.
	 * @since 2.0_7
	 */
	public boolean isNormal() {
		return _normal;
	}
	
	/** Sets whether support satellite map, default to true.
	 * @param b whether support satellite map, default to true.
	 * @since 2.0_7
	 */
	public void setSatellite(boolean b) {
		if (_satellite != b) {
			if (!b && "satellite".equals(_mapType)) {
				if (isNormal()) setMapType("normal");
				else if (isHybrid()) setMapType("hybrid");
				else if (isPhysical()) setMapType("physical");
				else return; //cannot do it if no more maps!
			}
			_satellite = b;
			smartUpdate("satellite", b);
		}
	}
	
	/** Returns whether support satellite map, default to true.
	 * @return whether support satellite map.
	 * @since 2.0_7
	 */
	public boolean isSatellite() {
		return _satellite;
	}
	
	/** Sets whether support hybrid map, default to true.
	 * @param b whether support hybrid map, default to true.
	 * @since 2.0_7
	 */
	public void setHybrid(boolean b) {
		if (_hybrid != b) {
			if (!b && "hybrid".equals(_mapType)) {
				if (isNormal()) setMapType("normal");
				else if (isSatellite()) setMapType("satellite");
				else if (isPhysical()) setMapType("physical");
				else return; //cannot do it if no more maps!
			}
			_hybrid = b;
			smartUpdate("hybrid", b);
		}
	}
	
	/** Returns whether support hybrid map, default to true.
	 * @return whether support hybrid map.
	 * @since 2.0_7
	 */
	public boolean isHybrid() {
		return _hybrid;
	}
	
	/** Sets whether support physical map, default to false.
	 * @param b whether support physical map, default to false.
	 * @since 2.0_7
	 */
	public void setPhysical(boolean b) {
		if (_physical != b) {
			if (!b && "physical".equals(_mapType)) {
				if (isNormal()) setMapType("normal");
				else if (isHybrid()) setMapType("hybrid");
				else if (isSatellite()) setMapType("satellite");
				else return; //cannot do it if no more maps!
			}
			_physical = b;
			smartUpdate("physical", b);
		}
	}
	
	/** Returns whether support physical map, default to false.
	 * @return whether support physical map, default to false.
	 * @since 2.0_7
	 */
	public boolean isPhysical() {
		return _physical;
	}
	
	/**
	 * Get the current Map Type.
	 * @return the current Map Type.
	 */
	public String getMapType() {
		return _mapType;
	}
	
	/** 
	 * Set the map type (normal, satellite, hybrid, physical), default is normal.
	 * This implementation always set since we don't know what the current MapType is in
	 * browser side.
	 * @param mapType (normal, satellite, hybrid, physical), default is normal.
	 */
	public void setMapType(String mapType) {
		if ("normal".equals(mapType)) {
			setNormal(true);
		} else if ("satellite".equals(mapType)) {
			setSatellite(true);
		} else if ("hybrid".equals(mapType)) {
			setHybrid(true);
		} else if ("physical".equals(mapType)) {
			setPhysical(true);
		} else {
			mapType = "normal";
			setNormal(true);
		}
		_mapType = mapType;
		smartUpdate("mapType", mapType);
	}

	/** Sets whether enable dragging maps by mouse, default to true.
	 * @param b true to enable dragging maps by mouse.
	 * @since 2.0_7
	 */
	public void setEnableDragging(boolean b) {
		if (_enableDragging != b) {
			_enableDragging = b;
			smartUpdate("enableDragging", b);
		}
	}
	
	/** Returns whether enable dragging maps by mouse, default to true.
	 * @return true to enable dragging maps by mouse.
	 * @since 2.0_7
	 */
	public boolean isEnableDragging() {
		return _enableDragging;
	}
	
	/** Sets whether enable continuous zoom effects, default to false.
	 * @param b true to enable continuous zoom effects.
	 * @since 2.0_7
	 */
	public void setContinuousZoom(boolean b) {
		if (_continuousZoom != b) {
			_continuousZoom = b;
			smartUpdate("continuousZoom", b);
		}
	}
	
	/** Returns whether enable continuous zoom effects, default to false.
	 * @return true to enable continuous zoom effects.
	 * @since 2.0_7
	 */
	public boolean isContinuousZoom() {
		return _continuousZoom;
	}
	
	/** Sets whether enable zoom in-out via mouse double click, default to false.
	 * @param b true to enable zoom in-out via mouse double clilck.
	 * @since 2.0_7
	 */
	public void setDoubleClickZoom(boolean b) {
		if (_doubleClickZoom != b) {
			_doubleClickZoom = b;
			smartUpdate("doubleClickZoom", b);
		}
	}
	
	/** Returns whether enable zoom in-out via mouse double click, default to false.
	 * @return true to enable zoom in-out via mouse double clilck.
	 * @since 2.0_7
	 */
	public boolean isDoubleClickZoom() {
		return _doubleClickZoom;
	}
	
	/** Sets whether enable zoom in-out via mouse scroll wheel, default to false.
	 * @param b true to enable zoom in-out via mouse scroll wheel.
	 * @since 2.0_7
	 */
	public void setScrollWheelZoom(boolean b) {
		if (_scrollWheelZoom != b) {
			_scrollWheelZoom = b;
			smartUpdate("scrollWheelZoom", b);
		}
	}
	
	/** Returns whether enable zoom in-out via mouse scroll wheel, default to false.
	 * @return true to enable zoom in-out via mouse scroll wheel.
	 * @since 2.0_7
	 */
	public boolean isScrollWheelZoom() {
		return _scrollWheelZoom;
	}

	/** Sets whether show the Google Search Bar on the Map, default to false.
	 * @param b true to show the Google Search Bar
	 * @since 2.0_7
	 */
	public void setEnableGoogleBar(boolean b) {
		if (_enableGoogleBar != b) {
			_enableGoogleBar = b;
			smartUpdate("enableGoogleBar", b);
		}
	}

	/** Returns whether show the Google Search Bar on the Map, default to false.
	 * @return true if show the Google Search Bar.
	 * @since 2.0_7
	 */
	public boolean isEnableGoogleBar() {
		return _enableGoogleBar;
	}
	

	/**
     * Returns whether your application is using a sensor (such as a GPS locator) 
     * to determine the user's location. This is especially important for mobile 
     * devices; default is false.
	 * @return whether your application is using a sensor.
	 * @since 2.0_50
	 * @deprecated As of 3.0.5 : see <a href="https://developers.google.com/maps/documentation/javascript/error-messages#sensor-not-required">SensorNotRequired</a>
	 */
	@Deprecated
    public boolean isSensor() {
		return getGmapsApiConfigParam(SENSOR);
	}

    /**
     * Sets whether your application is using a sensor (such as a GPS locator) 
     * to determine the user's location. This is especially important for mobile 
     * devices; default is false.
     * @param sensor whether using a sensor to determine the user's location.
	 * @since 2.0_50
	 * @deprecated As of 3.0.5 : see <a href="https://developers.google.com/maps/documentation/javascript/error-messages#sensor-not-required">SensorNotRequired</a>
     */
	@Deprecated
	public void setSensor(boolean sensor) {
		setGmapsApiConfigParam(SENSOR, sensor);
	}

	/**
	 * Returns the base domain from which to load the Maps API. For example, 
	 * you could load from "maps.google.cn " with the "maps" module to get 
	 * the Chinese version of the Maps API; null to use the default domain.<br/>
	 * <br/>
	 * As an alternative consider using the {@link Gmaps#setRegion(String)} for geocoding purposes
	 * (refer to: <a href="https://developers.google.com/maps/premium/faq#ssl_base_domain">FAQ SSL and Base Domain</a>).<br/>
	 * 
	 * @return the user specified base domain from which to load the Maps API.
	 * @since 2.0_50
	 */
	public String getBaseDomain() {
		return _baseDomain;
	}

	/**
	 * Sets the base domain from which to load the Maps API. For example, 
	 * you could load from "maps.google.cn" with the "maps" module to get 
	 * the Chinese version of the Maps API; null to use the default domain.<br/>
	 * <br/>
	 * As an alternative consider using the {@link Gmaps#setRegion(String)} for geocoding purposes
	 * (refer to: <a href="https://developers.google.com/maps/premium/faq#ssl_base_domain">FAQ SSL and Base Domain</a>).<br/>
	 * 
	 * @param baseDomain the base domain from which to load the Maps API
	 * @since 2.0_50
	 */
	public void setBaseDomain(String baseDomain) {
		if (!Objects.equals(_baseDomain, baseDomain)) {
			this._baseDomain = baseDomain;
			smartUpdate("baseDomain", baseDomain);
		}
	}	
	
	/**
	 * Get the <a href="https://developers.google.com/maps/documentation/javascript/localization#Region">gmaps api region parameter</a>
	 * @return the region parameter load the Gmaps API with
	 * @since 3.0.5
	 */
	public String getRegion() {
		return getGmapsApiConfigParam(REGION);
	}
	
	
	/**
	 * Set the <a href="https://developers.google.com/maps/documentation/javascript/localization#Region">Gmaps API region parameter</a>
	 * @param region the region parameter load the Gmaps API with
	 * @since 3.0.5
	 */
	public void setRegion(String region) {
		setGmapsApiConfigParam(REGION, region);
	}

	/**
	 * Returns the protocol to load the Maps API.
	 * Currently support http for insecure connections
	 * and https for secure connections.
	 * @return the user specified protocol to load the Maps API.
	 * @since 3.0.0
	 */
	public String getProtocol() {
		return _protocol;
	}

	/**
	 * Sets the protocol to load the Maps API. 
	 * Currently support http for insecure connections
	 * and https for secure connections.
	 * @param protocol the protocol to load the Maps API
	 * @since 3.0.0
	 */
	public void setProtocol(String protocol) {
		if (!Objects.equals(_protocol, protocol)) {
			this._protocol = protocol;
			smartUpdate("protocol", protocol);
		}
	}

	/**
	 * Returns the preferred language code
	 * (<a href="https://developers.google.com/maps/documentation/javascript/localization#Language">gmaps 
	 * API language parameter</a>); default to null and means using
	 * browser's preferred language. You can check language code 
	 * 
	 * @return the preferred language code specified developer.
	 * @since 2.0_50
	 */
	public String getLanguage() {
		return getGmapsApiConfigParam(LANGUAGE);
	}

	/**
	 * Sets the preferred language code 
	 * (<a href="https://developers.google.com/maps/documentation/javascript/localization#Language">gmaps
	 * API language parameter</a>); default to null and means using
	 * browser's preferred language. 
	 * <p>By default Gmaps uses the browser's preferred language setting when 
	 * displaying textual information such as control names, copyright, and so
	 * one. Sets language code will make Gmaps to always use the specified
	 * language and ignore the browser's language setting.
	 * 
	 * @param language the preferred language code
	 * @since 2.0_50
	 */
	public void setLanguage(String language) {
		setGmapsApiConfigParam(LANGUAGE, language);
	}
	/**
	 * Returns the libraries to load; defaults to 'geometry' and means
	 * load geometry library only, you can check libraries
	 * <a href="https://developers.google.com/maps/documentation/javascript/libraries">here</a>
	 * <p> Use comma to separate different library
	 * 
	 * @return the libraries to load.
	 * @since 3.0.0
	 */
	public String getLibraries () {
		return getGmapsApiConfigParam(LIBRARIES);
	}
	/**
	 * Sets the libraries to load; defaults to 'geometry' and means using
	 * load geometry library only, you can check libraries
	 * <a href="https://developers.google.com/maps/documentation/javascript/libraries">here</a>
	 * <p> Use comma to separate different library
	 * 
	 * @param libraries comma separated String of libraries to load
	 * @since 3.0.0
	 */
	public void setLibraries (String libraries) {
		setGmapsApiConfigParam(LIBRARIES, libraries);
	}

	/** 
	 * Returns the selected version of google map API v3.
	 * @return String
	 */
	public String getVersion() {
		return _version;
	}

	/** 
	 * Set the selected version of google map API v3.
	 * @param version the version of google map.
	 */
	public void setVersion(String version) {
		if (!Objects.equals(_version, version)) {
			this._version = version;
			smartUpdate("language", version);
		}
	}

	/** Open the specified Ginfo or Gmarker. The specified Ginfo must be child of this Gmaps.
	 * @param info the specified Ginfo or Gmarker.
     */
    public void openInfo(Ginfo info) {
        if (info != null) {
	        if (info.getParent() != this) {
	            throw new UiException("The to be opened Ginfo or Gmarker must be child of this Gmaps!");
	        }
	        // do not need close old info
	        _info = info;
	        smartUpdate("openInfo_", info.getUuid());
        } else {
            closeInfo();
        }
    }
    
    /** Close the currently opened info window.
     */
    public void closeInfo() {
    	closeInfo(null);
    }
    
    /** Close the currently opened info window.
     */
    public void closeInfo(Ginfo info) {
    	_info = null;
        smartUpdate("closeInfo_", info.getUuid());
    }
    
	/** Returns the currently opened info window of this Google Maps (might be Gmarker or Ginfo).
	 * @return the currently opened Ginfo.
	 */
	public Ginfo getInfo() {
		return _info;
	}

	/**
	 * Returns the Maps model associated with this Maps, or null if this Maps
	 * is not associated with any Maps data model. 
	 * @return the Maps model associated with this Maps
	 * @since 2.0_9
	 */
	public MapModel getModel() {
		return _model;
	}
	
	/**
	 * Sets the model associated with this Maps. If a non-null model is
	 * assigned, no matter whether it is the same as the previous, it will
	 * always cause re-render.
	 * 
	 * @param model the model to associated, or null to dis-associate any
	 * privious model.
	 * @since 2.0_9
	 */
	public void setModel(MapModel model) {
		if (model != null) {
			if (_model != model) {
				if (_model != null) {
					_model.removeMapDataListener(_dataListener);
				}
				_model = model;
				initMapDataListener();
			}
			addOnMapMove();
			//always syncModel because it is easier for user to enforce reload
			syncModel();
			//since user might setModel and setRenderer separately or repeatly
			//we don't handle it right now until the event processing phase
			//such that we won't render the same set of data twice.
		} else {
			//20090721, Henri Chen: Shall not remove those kids not controlled by 
			//the _model.
			//trigger model clear to remove controlled kids
			onMapDataChange(new MapDataEvent(_model, MapDataEvent.CLEARED, null));
			_model.removeMapDataListener(_dataListener);
			removeOnMapMove();
			_model = null;
		}
	}
	private class UpdateBoundsListener implements EventListener<Event>, java.io.Serializable {
		private static final long serialVersionUID = 200808261207L;

		public void onEvent(Event evt) {
			//bounds not initiated yet(do it at server side)
			if (getSwlat() == 37.418026932311111)
				initBounds();
			syncModel();
		}
	}
	private void addOnMapMove() {
		if (_moveListener == null) {
			_moveListener = new UpdateBoundsListener();
			addEventListener(1000, "onMapMove", _moveListener);
		}
	}
	private void removeOnMapMove() {
		if (_moveListener != null) {
			removeEventListener("onMapMove", _moveListener);
		}
	}
	private void syncModel() {
		if (_model != null)
			onMapDataChange(new MapDataEvent(_model, MapDataEvent.BOUNDS_CHANGED, 
				_model.getItemsIn(getSwlat(), getSwlng(), getNelat(), getNelng(), getLat(), getLng(), _zoom)));
	}
	private void initMapDataListener() {
		if (_dataListener == null) {
			_dataListener = new MapDataListener() {
				public void onChange(MapDataEvent event) {
					onMapDataChange(event);
				}
			};
		}
		_model.addMapDataListener(_dataListener);
	}
	private void onMapDataChange(MapDataEvent event) {
		//when this is called, _model should not be null
		Ginfo info = null;
		switch(event.getType()) {
			case MapDataEvent.ADDED: {
				final MapitemRenderer renderer = getRealRenderer();
				for(final Iterator it = event.getItems().iterator(); it.hasNext();) {
					final Object data = it.next();
					final Mapitem mitem = renderer.newMapitem(data);
					if (info == null && mitem instanceof Ginfo && ((Ginfo)mitem).isOpen()) {
						info = (Ginfo) mitem;
					}
					_dataMap.put(data, mitem);
					appendChild((Component) mitem);
				}
				break;
			}
			case MapDataEvent.REMOVED: {
				for(final Iterator it = event.getItems().iterator(); it.hasNext();) {
					final Object data = it.next();
					final Mapitem mitem = (Mapitem) _dataMap.remove(data);
					if (mitem != null)
						removeChild((Component) mitem);
				}
				break;
			}
			case MapDataEvent.CONTENTS_CHANGED: {
				final MapitemRenderer renderer = getRealRenderer();
				final Collection items = event.getItems();
				for(final Iterator it = items.iterator(); it.hasNext();) {
					final Object data = it.next();
					if (_dataMap.containsKey(data)) {
						final Mapitem mitem = (Mapitem) _dataMap.remove(data);
						final boolean isopen = info == null && mitem instanceof Ginfo && ((Ginfo)mitem).isOpen();
						removeChild((Component) mitem);
						final Mapitem nitem = renderer.newMapitem(data);
						if (isopen) {
							info = (Ginfo) nitem;
						}
						_dataMap.put(data, nitem);
						appendChild((Component) nitem);
					}
				}
				break;
			}	
			case MapDataEvent.BOUNDS_CHANGED: {
				final MapitemRenderer renderer = getRealRenderer();
				final Collection items = event.getItems();
				for(final Iterator it = _dataMap.entrySet().iterator(); it.hasNext();) {
					final Entry entry = (Entry) it.next();
					final Object data = entry.getKey();
					final Mapitem mitem = (Mapitem) entry.getValue();
					if (!items.contains(data)) { //removed item
						it.remove();
						removeChild((Component) mitem);
					}
				}
				for(final Iterator it = items.iterator(); it.hasNext();) {
					final Object data = it.next();
					if (!_dataMap.containsKey(data)) {
						final Mapitem mitem = renderer.newMapitem(data);
						if (info == null && mitem instanceof Ginfo && ((Ginfo)mitem).isOpen()) {
							info = (Ginfo) mitem;
						}
						_dataMap.put(data, mitem);
						appendChild((Component) mitem);
					}
				}
				break;
			}
			case MapDataEvent.CLEARED: {
				//only Mapitem that is controlled by the model
				for(final Iterator it = _dataMap.entrySet().iterator(); it.hasNext();) {
					final Entry entry = (Entry) it.next();
					final Mapitem mitem = (Mapitem) entry.getValue();
					removeChild((Component) mitem);
				}
				//bug #2813930, Memory Leak in GMap
				_dataMap.clear();
				break;
			}
		}
		if (info != null && info != _info) {
			openInfo(info);
		}
	}
	private MapitemRenderer getRealRenderer() {
		return _renderer == null ? 
				new MapitemRenderer() {
					public Mapitem newMapitem(Object data) {
						return (Mapitem) data;
					}
				} : _renderer;
	}
	/**
	 * Returns the renderer to render given data object, or null if the default 
	 * renderer is used. Default renderer assume the given data object is a kind
	 * of {@link Mapitem}.
	 * 
	 * @return the renderer to render given data.
	 * @since 2.0_9
	 */
	public MapitemRenderer getItemRenderer() {
		return _renderer;
	}
	
	/**
	 * 	Sets the renderer which is used to render each {@link Mapitem} if 
	 * {@link #getModel} is not null.
	 * <p>Note: changing a renderer will not cause the Maps to re-render. If
	 *  you want it to re-render, you could assign the same model again(i.e.,
	 *  setModel(getModel()) ) or fire an {@link MapDataEvent} event.
	 * @param renderer the renderer, or null to use the default.
	 * @since 2.0_9
	 */
	public void setItemRenderer(MapitemRenderer renderer) {
		_renderer = renderer;
	}

	/**
	 * Set the selected item (can be {@link Gmarker}, {@link Gpolyline}, or {@link Gpolygon}).
	 * @param item the item to be selected.
	 */
	public void setSelectedItem(Component item) {
		if (item == null) {
			_selected = null;
		} else {
			if (item.getParent() != this)
				throw new UiException("Not a child: "+item);
			_selected = item;
		}
	}
	
	/**
	 * Returns the selected item (can be {@link Gmarker}, {@link Gpolyline}, or {@link Gpolygon}).
	 * @return the selected item.
	 */
	public Component getSelectedItem() {
		return _selected;
	}
	
	//-- Component --//
	public boolean insertBefore(Component child, Component insertBefore) {
        if (!(child instanceof Mapitem)) {
            throw new UiException("Only Mapitem such as Ginfo, Gmarker, Gpolyline, Gpolygon, Gimage, Gscreen is allowed to be child of Gmaps: "+this+", "+child);
        }
		if (isGinfo(child)) { //so it is Ginfo
			if (_oneinfo != null && _oneinfo != child)
				throw new UiException("Only one Ginfo is allowed: "+this);
			_oneinfo = (Ginfo)child;
		}
		final boolean ret = super.insertBefore(child, insertBefore);
		if (child instanceof Ginfo && ((Ginfo)child).isOpen()) {
			openInfo((Ginfo)child);
		}
		return ret;
	}
	public void onChildRemoved(Component child) {
		if (isGinfo(child)) { //so it is Ginfo
            _oneinfo = null;
        }
		if (child == _info) { //the detached Ginfo is the currently opened Ginfo.
			//no need to call closeInfo() since removing
			//the child would close the info window automatically
            _info.setOpenByClient(false);
            _info = null;
		}
		super.onChildRemoved(child);
	}
	private boolean isGinfo(Component comp) {
		return (comp instanceof Ginfo) && ((Ginfo)comp).isGinfo();
	}
	//Cloneable//
	public Object clone() {
		final Gmaps clone = (Gmaps)super.clone();
		if (clone._oneinfo != null || clone._info != null) clone.afterUnmarshal();
		return clone;
	}
	private void afterUnmarshal() {
		for (Iterator it = getChildren().iterator(); it.hasNext();) {
			final Object child = it.next();
            if (isGinfo((Component)child)) { //so it is Ginfo
                _oneinfo = (Ginfo)child;
            }
			if (_info != null && ((Component)child).getUuid() == _info.getUuid()) {
				_info = (Ginfo)child;
				break;
			}
		}
	}
	
	/** used by the MapMoveEvent */
	/* package */ void setCenterByClient(LatLng center) {
		_center = center;
	}
	/* package */ void setZoomByClient(int zoom) {
		_zoom = zoom;
	}
	/* package */ void setMapTypeByClient(String type) {
		_mapType = type;
	}
    /** used by the InfoChangeEvent */
    /* package */ void setInfoByClient(Ginfo info) {
    	if (info != null) {
    		info.setOpenByClient(false);
    	}
    	else if (_info != null) {
    		_info.setOpenByClient(false);
    	}
        _info = info;
    }
	/* package */ void setBoundsByClient(LatLngBounds bounds) {
		_bounds = bounds;
	}

	private int width(String width) {
		width = width.trim();
		if (width.endsWith("%"))
			return 1280; //default to 1280
		else
			return stringToInt(width);
	}
	private int height(String height) {
		if (height.endsWith("%"))
			return 1024; //default to 1024
		else
			return stringToInt(height);
	}
	private int stringToInt(String str) {
		if (str.endsWith("px"))
			str = str.substring(0, str.length() - 2);
		else if (str.endsWith("pt")) {
			str = str.substring(0, str.length() - 2);
			return (int) (Integer.parseInt(str) * 1.3333);
		} 
		else if (str.endsWith("em")) {
			str = str.substring(0, str.length() - 2);
			return (int) (Integer.parseInt(str) * 13.3333);
		}
		return Integer.parseInt(str);
	}
	private void initBounds() {
		_bounds = GmapsUtil.getBounds(_center, width(getWidth()), height(getHeight()), _zoom);
	}

	//-- ComponentCtrl --//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		if (!_center.equals(new LatLng(37.4419, -122.1419)))
			render(renderer, "center", _center);
		if (!_bounds.equals(new LatLngBounds(
				new LatLng(37.418026932311111, -122.1933746338),
				new LatLng(37.4657298516, -122.0903778076))))
			render(renderer, "bounds", _bounds);
		if (_zoom != 13)
			render(renderer, "zoom", new Integer(getZoom()));
		if (_large)
			render(renderer, "showLargeCtrl", isShowLargeCtrl());
		if (_small)
			render(renderer, "showSmallCtrl", isShowSmallCtrl());
		if (!_smallZoom)
			renderer.render("showZoomCtrl", isShowZoomCtrl());
		if (!_type)
			renderer.render("showTypeCtrl", isShowTypeCtrl());
		if (!_pan)
			renderer.render("showPanCtrl", isShowPanCtrl());
		if (_scale)
			renderer.render("showScaleCtrl", isShowScaleCtrl());
		if (_overview)
			renderer.render("showOverviewCtrl", isShowOverviewCtrl());
		if (!_enableDragging)
			renderer.render("enableDragging", isEnableDragging());
		if (_continuousZoom)
			render(renderer, "continuousZoom", isContinuousZoom());
		if (!_doubleClickZoom)
			renderer.render("doubleClickZoom", isDoubleClickZoom());
		if (!_scrollWheelZoom)
			renderer.render("scrollWheelZoom", isScrollWheelZoom());
		if (_enableGoogleBar)
			render(renderer, "enableGoogleBar", isEnableGoogleBar());
		if (!"normal".equals(_mapType))
			render(renderer, "mapType", getMapType());
		if (_satellite)
			renderer.render("satellite", isSatellite());
		if (!_hybrid)
			renderer.render("hybrid", isHybrid());
		if (!_physical)
			renderer.render("physical", isPhysical());
		if (!_normal)
			renderer.render("normal", isNormal());
		if (!"3".equals(_version))
			renderer.render("version", _version);
		if (!Strings.isBlank(_baseDomain))
			renderer.render("baseDomain", _baseDomain);
		if (!Strings.isBlank(_protocol))
			renderer.render("protocol", _protocol);
		if(!Objects.equals(_gmapsApiConfigParams, Collections.singletonMap(LIBRARIES, "geometry"))) {
			renderer.render("gmapsApiConfigParams", _gmapsApiConfigParams);
		}
	}

	/** Processes an AU request.
	 *
	 * <p>Default: in addition to what are handled by {@link XulElement#service},
	 * it also handles onSelect.
	 * @since 5.0.0
	 */
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals("onMapMove")) {
			final MapMoveEvent evt = MapMoveEvent.getMapMoveEvent(request);
			setCenterByClient(evt.getLatLng());
			setBoundsByClient(evt.getBounds());
			Events.postEvent(evt);
		} else if (cmd.equals("onMapZoom")) {
			final MapZoomEvent evt = MapZoomEvent.getMapZoomEvent(request);
			final int zoom = evt.getZoom();
			setZoomByClient(zoom);
			Events.postEvent(evt);
		} else if (cmd.equals("onInfoChange")) {
			final InfoChangeEvent evt = InfoChangeEvent.getInfoChangeEvent(request);
			setInfoByClient(evt.getInfo());
			Events.postEvent(evt);
		} else if (cmd.equals("onMapClick")
				|| cmd.equals("onMapDoubleClick")
				|| cmd.equals("onMapRightClick")) {
			final MapMouseEvent evt = MapMouseEvent.getMapMouseEvent(request);
			Events.postEvent(evt);
		} else if (cmd.equals(Events.ON_SELECT)) {
			SelectEvent evt = SelectEvent.getSelectEvent(request);
			Set selItems = evt.getSelectedItems();
			final Component mitem =  selItems == null || selItems.isEmpty() ? 
					null : (Component) selItems.iterator().next();
			setSelectedItem(mitem);
			Events.postEvent(evt);
		} else if (cmd.equals("onMapTypeChange")) {
			final MapTypeChangeEvent evt = MapTypeChangeEvent.getMapTypeChangeEvent(request);
			setMapTypeByClient(evt.getType());
			Events.postEvent(evt);
		} else if (cmd.equals("onMapDrop")) {
			final MapDropEvent evt = MapDropEvent.getMapDropEvent(request);
			final Component dragged = evt.getDragged();
			if (dragged instanceof Gmarker)
				((Gmarker) dragged).setAnchor(evt.getLatLng());
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}

	//register the Gmaps related event
	static {
		addClientEvent(Gmaps.class, "onMapMove", CE_DUPLICATE_IGNORE | CE_IMPORTANT | CE_NON_DEFERRABLE);
		addClientEvent(Gmaps.class, "onMapZoom", CE_DUPLICATE_IGNORE | CE_IMPORTANT | CE_NON_DEFERRABLE);
		addClientEvent(Gmaps.class, "onInfoChange", CE_DUPLICATE_IGNORE | CE_IMPORTANT | CE_NON_DEFERRABLE);
		addClientEvent(Gmaps.class, "onMapClick", CE_DUPLICATE_IGNORE);
		addClientEvent(Gmaps.class, "onMapDoubleClick", CE_DUPLICATE_IGNORE);
		addClientEvent(Gmaps.class, "onMapRightClick", CE_DUPLICATE_IGNORE);
		addClientEvent(Gmaps.class, "onMapDrop", CE_DUPLICATE_IGNORE);
		addClientEvent(Gmaps.class, "onMapTypeChange", CE_DUPLICATE_IGNORE);
		addClientEvent(Gmaps.class, Events.ON_SELECT, CE_DUPLICATE_IGNORE);
	}
}
