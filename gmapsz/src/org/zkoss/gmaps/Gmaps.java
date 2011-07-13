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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.zkoss.gmaps.event.MapDataEvent;
import org.zkoss.gmaps.event.MapDataListener;
import org.zkoss.xml.HTMLs;
import org.zkoss.zk.au.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.Express;
import org.zkoss.zk.ui.ext.client.Selectable;
import org.zkoss.zul.impl.XulElement;


/**
 * The component used to represent
 * &lt;a href="http://www.google.com/apis/maps/"&gt;Google Maps&lt;/a&gt;
 *
 * @author henrichen
 * @version $Revision: 1.6 $ $Date: 2006/03/31 08:38:55 $
 */
public class Gmaps extends XulElement {
	private static final long serialVersionUID = 200807040842L;
	private transient Ginfo _oneinfo; //the only one Ginfo child of this Gmaps.
	private transient Ginfo _info; //current opened info window, null means none is open.

	private double _lat = 37.4419;
	private double _lng = -122.1419;
	private int _zoom = 13;
	private boolean _large;
	private boolean _small;
	private boolean _type;
	private boolean _smallZoom;
	private boolean _scale;
	private boolean _overview;
	
	private boolean _normal = true;
	private boolean _satellite = true;
	private boolean _hybrid = true;
	private boolean _physical;
	
	private String _mapType = "normal";
	private boolean _enableDragging = true;
	private boolean _continuousZoom;
	private boolean _doubleClickZoom;
	private boolean _scrollWheelZoom;
	private boolean _enableGoogleBar;
	
	private double _swlat = 37.418026932311111;
	private double _swlng = -122.1933746338;
	private double _nelat = 37.4657298516;
	private double _nelng = -122.0903778076;
	
	private MapModel _model;
	private MapitemRenderer _renderer;
	private MapDataListener _dataListener;
	private EventListener _moveListener; //used by MapModel live data
	private Map _dataMap = new HashMap(64); //data object -> Mapitem
	private Component _selected;

	/** Sets the center of the Google Maps.
	 * @param lat latitude of the Google Maps center
	 * @param lng longitude of the Google Maps center
	 */
	public void setCenter(double lat, double lng) {
		boolean update = false;
		if (lat != _lat) {
			_lat = lat;
			update = true;
		}
		if (lng != _lng) {
			_lng = lng;
			update = true;
		}

		if (update) {
			smartUpdate("z.center", getCenter());
		}
	}

	/** Sets the current latitude of the Maps center.
	 * @param lat latitude of the Google Maps center
	 */
	public void setLat(double lat) {
		if (lat != _lat) {
			_lat = lat;
			smartUpdate("z.center", getCenter());
		}
	}
	
	/** Returns the current latitude of the Maps center.
	 * @return the current latitude of the Maps center.
	 */
	public double getLat() {
		return _lat;
	}
	
	/** Sets the current longitude of the Maps center.
	 * @param lng the current longitude of the Maps center.
	 */
	public void setLng(double lng) {
		if (lng != _lng) {
			_lng = lng;
			smartUpdate("z.center", getCenter());
		}
	}
	
	/** Returns the currrent longitude of the Maps center.
	 * @return the currrent longitude of the Maps center.
	 */
	public double getLng() {
		return _lng;
	}
	
	/** Returns the Maps center in String form lat,lng; used by component developers
	 * only.
	 */
	private String getCenter() {
		return ""+_lat+","+_lng;
	}

	/**
	 * Returns the bounded south west latitude.
	 * @return the bounded south west latitude.
	 * @since 2.0_8
	 */
	public double getSwLat() {
		return _swlat;
	}
	/**
	 * Returns the bounded south west longitude.
	 * @return the bounded south west longitude.
	 * @since 2.0_8
	 */
	public double getSwLng() {
		return _swlng;
	}
	/**
	 * Returns the bounded north east latitude.
	 * @return the bounded north east latitude.
	 * @since 2.0_8
	 */
	public double getNeLat() {
		return _nelat;
	}
	/**
	 * Returns the bounded north east longitude.
	 * @return the bounded north east longitude.
	 * @since 2.0_8
	 */
	public double getNeLng() {
		return _nelng;
	}

	/** Pan to the new center of the Google Maps.
	 * @param lat latitude of the Google Maps center
	 * @param lng longitude of the Google Maps center
	 */
	public void panTo(double lat, double lng) {
		boolean update = false;
		if (lat != _lat) {
			_lat = lat;
			update = true;
		}
		if (lng != _lng) {
			_lng = lng;
			update = true;
		}
		
		if (update) {
			smartUpdate("z.panTo", getCenter());
		}
	}
	
	/** Sets zoom level.
	 * @param zoom the zoom level (0-18)
	 */
	public void setZoom(int zoom) {
		if (zoom != _zoom) {
			_zoom = zoom;
			smartUpdate("z.zoom", ""+_zoom);
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
		smartUpdate("z.lctrl", ""+b);
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
		smartUpdate("z.sctrl", ""+b);		
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
		smartUpdate("z.zctrl", ""+b);		
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
		smartUpdate("z.tctrl", ""+b);
	}

	/** Returns whether show the Google Maps type Control.
	 * @return whether show the Google Maps type Control.
	 */
	public boolean isShowTypeCtrl() {
		return _type;
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
		smartUpdate("z.cctrl", ""+b);
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
		smartUpdate("z.octrl", ""+b);
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
			smartUpdate("z.nmap", b);
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
			smartUpdate("z.smap", b);
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
			smartUpdate("z.hmap", b);
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
			smartUpdate("z.pmap", b);
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
		}
		_mapType = mapType;
		smartUpdate("z.mt", mapType);
	}

	/** Sets whether enable dragging maps by mouse, default to true.
	 * @param b true to enable dragging maps by mouse.
	 * @since 2.0_7
	 */
	public void setEnableDragging(boolean b) {
		if (_enableDragging != b) {
			_enableDragging = b;
			smartUpdate("z.dg", ""+b);
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
			smartUpdate("z.cz", ""+b);
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
			smartUpdate("z.dz", ""+b);
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
			smartUpdate("z.wz", ""+b);
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
			smartUpdate("z.gb", ""+b);
		}
	}

	/** Returns whether show the Google Search Bar on the Map, default to false.
	 * @return true if show the Google Search Bar.
	 * @since 2.0_7
	 */
	public boolean isEnableGoogleBar() {
		return _enableGoogleBar;
	}
	
    /** Open the specified Ginfo or Gmarker. The specified Ginfo must be child of this Gmaps.
	 * @param info the specified Ginfo or Gmarker.
     */
    public void openInfo(Ginfo info) {
        if (info != null) {
        	if (info != _info) {
		        if (info.getParent() != this) {
		            throw new UiException("The to be opened Ginfo or Gmarker must be child of this Gmaps!");
		        }
		        if (_info != null) {
		        	_info.setOpenByClient(false); //closeInfo() <-- no need to fire the command
		        }
		        _info = info;
		        smartUpdate("z.open", info.getUuid());
        	}
        } else {
            closeInfo();
        }
    }
    
    /** Close the currently opened info window.
     */
    public void closeInfo() {
        if (_info != null) {
        	_info = null;
            smartUpdate("z.close", "");
        }
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
				} else {
					//20090721, Henri Chen: Shall not remove those original kids
					//getChildren().clear();
					//_dataMap.clear();
					smartUpdate("z.onMapMove", "true");
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
			//getChildren().clear();
			//_dataMap.clear();
			if (!Events.isListened(this, "onMapMove", true)) {
				smartUpdate("z.onMapMove", null);
			}
		}
	}
	private class UpdateBoundsListener implements EventListener, Express, java.io.Serializable {
		private static final long serialVersionUID = 200808261207L;

		public void onEvent(Event evt) {
			syncModel();
		}
	}
	private void addOnMapMove() {
		if (_moveListener == null) {
			_moveListener = new UpdateBoundsListener();
			addEventListener("onMapMove", _moveListener);
		}
	}
	private void removeOnMapMove() {
		if (_moveListener != null) {
			removeEventListener("onMapMove", _moveListener);
		}
	}
	private void syncModel() {
		//bounds not initiated yet(do it at server side)
		if (_swlat == 37.418026932311111) { 
			initBounds();
		}
		if (_model != null)
			onMapDataChange(new MapDataEvent(_model, MapDataEvent.BOUNDS_CHANGED, 
				_model.getItemsIn(_swlat, _swlng, _nelat, _nelng, _lat, _lng, _zoom)));
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
	
	/** Internal Use Only. Returns the HTML attributes for this tag.
	 * <p>Used only for component development, not for application developers.
	 */
	public String getOuterAttrs() {
		final String attrs = super.getOuterAttrs();
		final StringBuffer sb = new StringBuffer(64);
		if (attrs != null) {
			sb.append(attrs);
		}
		if (Events.isListened(this, "onMapMove", true)) {
			HTMLs.appendAttribute(sb, "z.onMapMove", "true");
		}
		if (Events.isListened(this, "onMapZoom", true)) {
			HTMLs.appendAttribute(sb, "z.onMapZoom", "true");
		}
		if (Events.isListened(this, "onInfoChange", true)) {
			HTMLs.appendAttribute(sb, "z.onInfoChange", "true");
		}
		if (Events.isListened(this, "onMapClick", true)) {
			HTMLs.appendAttribute(sb, "z.onMapClick", "true");
		}
		if (Events.isListened(this, "onMapDoubleClick", true)) {
			HTMLs.appendAttribute(sb, "z.onMapDoubleClick", "true");
		}
		if (Events.isListened(this, Events.ON_SELECT, true)) {
			HTMLs.appendAttribute(sb, "z."+Events.ON_SELECT, "true");
		}
		if (Events.isListened(this, "onMapRightClick", true)) {
			HTMLs.appendAttribute(sb, "z.onMapRightClick", "true");
		}
		if (Events.isListened(this, "onMapTypeChange", true)) {
			HTMLs.appendAttribute(sb, "z.onMapTypeChange", "true");
		}
		
		final StringBuffer ctrls = new StringBuffer(3);
		if (isShowLargeCtrl()) {
			ctrls.append("l");
		}
		if (isShowSmallCtrl()) {
			ctrls.append("s");
		} 
		if (isShowZoomCtrl()) {
			ctrls.append("z");
		}
		if (isShowTypeCtrl()) {
			ctrls.append("t");
		}
		if (isShowScaleCtrl()) {
			ctrls.append("c");
		} 
		if (isShowOverviewCtrl()) {
			ctrls.append("o");
		} 
		HTMLs.appendAttribute(sb, "z.init", getCenter()+","+_zoom+(ctrls.length() == 0 ? "" : (","+ctrls)));
		HTMLs.appendAttribute(sb, "z.mt", getMapType());
		HTMLs.appendAttribute(sb, "z.dg", isEnableDragging());
		HTMLs.appendAttribute(sb, "z.cz", isContinuousZoom());
		HTMLs.appendAttribute(sb, "z.dz", isDoubleClickZoom());
		HTMLs.appendAttribute(sb, "z.wz", isScrollWheelZoom());
		HTMLs.appendAttribute(sb, "z.gb", isEnableGoogleBar());
		
		HTMLs.appendAttribute(sb, "z.smap", isSatellite());
		HTMLs.appendAttribute(sb, "z.hmap", isHybrid());
		HTMLs.appendAttribute(sb, "z.pmap", isPhysical());
		HTMLs.appendAttribute(sb, "z.nmap", isNormal());

		return sb.toString();
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
			if (_info != null && ((Component)child).getId() == _info.getId()) {
				_info = (Ginfo)child;
				break;
			}
		}
	}
	
	/** used by the MapMoveEvent */
	/* package */ void setCenterByClient(double lat, double lng) {
		_lat = lat;
		_lng = lng;
	}
	/* package */ void setZoomByClient(int zoom) {
		_zoom = zoom;
	}
	/* package */ void setMapTypeByClient(String type) {
		_mapType = type;
	}
    /** used by the InfoChangeEvent */
    /* package */ void setInfoByClient(Ginfo info) {
    	if (info == null && _info != null) {
    		_info.setOpenByClient(false);
    	}
        _info = info;
    }
	/* package */ void setBoundsByClient(double swlat, double swlng, double nelat, double nelng) {
		_swlat = swlat;
		_swlng = swlng;
		_nelat = nelat;
		_nelng = nelng;
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
		final double[] bounds = GmapsUtil.getBounds(_lat, _lng, width(getWidth()), height(getHeight()), _zoom);
		_swlat = bounds[0];
		_swlng = bounds[1];
		_nelat = bounds[2];
		_nelng = bounds[3];
	}

	//-- ComponentCtrl --//
	protected Object newExtraCtrl() {
		return new ExtraCtrl();
	}
	/** A utility class to implement {@link #getExtraCtrl}.
	 * It is used only by component developers.
	 */
	protected class ExtraCtrl extends HtmlBasedComponent.ExtraCtrl
	implements Selectable {
		//-- Selectable --//
		public void selectItemsByClient(Set selItems) {
			final Component mitem =  selItems == null || selItems.isEmpty() ? 
				null : (Component) selItems.iterator().next();
			setSelectedItem(mitem);
		}

		public void clearSelectionByClient() {
			//do nothing
		}
	}

	//register the Gmaps related event
	static {	
		new MapMoveCommand("onMapMove", Command.IGNORE_OLD_EQUIV);
		new MapZoomCommand("onMapZoom", Command.IGNORE_OLD_EQUIV);
		new InfoChangeCommand("onInfoChange", Command.IGNORE_OLD_EQUIV);
		new MapClickCommand("onMapClick", Command.IGNORE_OLD_EQUIV);
		new MapDoubleClickCommand("onMapDoubleClick", Command.IGNORE_OLD_EQUIV);
		new MapRightClickCommand("onMapRightClick", Command.IGNORE_OLD_EQUIV);
		new MarkerDropCommand("onMarkerDrop", Command.IGNORE_OLD_EQUIV);
		new MapDropCommand("onMapDrop", Command.IGNORE_OLD_EQUIV);
		new MapOpenCommand("onMapOpen", Command.IGNORE_OLD_EQUIV);
		new MapTypeChangeCommand("onMapTypeChange", Command.IGNORE_OLD_EQUIV);
	}
}
