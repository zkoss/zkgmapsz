package org.zkoss.gmapstest.bind;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.InfoChangeEvent;
import org.zkoss.gmaps.event.MapDropEvent;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.gmaps.event.MapMoveEvent;
import org.zkoss.gmaps.event.MapZoomEvent;
import org.zkoss.zk.ui.Component;

public class GmapsVM {
	private Map<String, String> _mouseEventData = new HashMap<String, String>();
	private Map<String, String> _dropEventData = new HashMap<String, String>();
	private Map<String, String> _zoomEventData = new HashMap<String, String>();
	private Map<String, String> _infoChangeEventData = new HashMap<String, String>();

	private Component _selectedItem;

	private double _lat = 37.42838786;
	private double _lng = -122.13998795;
	private double _nelat;
	private double _nelng;
	private double _swlat;
	private double _swlng;
	private int _zoom = 11;
	private String _mapType = "physical";
	
	// current type
	private boolean _isNormal = false;
	private boolean _isHybrid = false;
	private boolean _isSatellite = false;
	private boolean _isPhysical = true;

	// map types
	private boolean _normalEnabled = false;
	private boolean _hybridEnabled = false;
	private boolean _satelliteEnabled = false;
	private boolean _physicalEnabled = true;

	private boolean _sensorEnabled = true;

	// controls
	private boolean _doubleClickZoom = false;
	private boolean _enableDragging = true;
	private boolean _scrollWheelZoom = false;
	private boolean _showZoomCtrl = false;
	private boolean _showTypeCtrl = true;
	private boolean _showOverviewCtrl = true;
	private boolean _showScaleCtrl = true;

	public Component getSelectedItem() {
		return _selectedItem;
	}
	public void setSelectedItem(Component selectedItem) {
		_selectedItem = selectedItem;
	}
	public String getMapId () {
		return "theMap";
	}
	public String getMapWidth () {
		return "400px";
	}
	public String getMapHeight () {
		return "400px";
	}
	public String getMapLang() {
		return "ja";
	}
	public String getBaseDomain() {
		return "GB";
	}
	public void setLat (double lat) {
		_lat = lat;
	}
	public double getLat () {
		return _lat;
	}
	public void setLng (double lng) {
		_lng = lng;
	}
	public double getLng() {
		return _lng;
	}
	public void setNeLat (double nelat) {
		_nelat = nelat;
	}
	public double getNeLat () {
		return _nelat;
	}
	public void setNeLng (double nelng) {
		_nelng = nelng;
	}
	public double getNeLng () {
		return _nelng;
	}
	public void setSwLat (double swlat) {
		_swlat = swlat;
	}
	public double getSwLat () {
		return _swlat;
	}
	public void setSwLng (double swlng) {
		_swlng = swlng;
	}
	public double getSwLng () {
		return _swlng;
	}
	public void setZoom (int zoom) {
		_zoom = zoom;
	}
	public int getZoom () {
		return _zoom;
	}
	public void setMapType (String mapType) {
		_mapType = mapType;
		_isNormal = _isHybrid = _isSatellite = _isPhysical = false;
		if ("normal".equals(mapType))
			_isNormal = true;
		else if ("hybrid".equals(mapType))
			_isHybrid = true;
		else if ("satellite".equals(mapType))
			_isSatellite = true;
		else if ("physical".equals(mapType))
			_isPhysical = true;
	}
	public String getMapType () {
		return _mapType;
	}
	public void setIsNormal(boolean isNormal) {
		if (_normalEnabled) {
			if (isNormal)
				setMapType("normal");
			_isNormal = isNormal;
		}
	}
	public boolean getIsNormal() {
		return _isNormal;
	}
	public void setIsHybrid(boolean isHybrid) {
		if (_hybridEnabled) {
			if (isHybrid)
				setMapType("hybrid");
			_isHybrid = isHybrid;
		}
	}
	public boolean getIsHybrid() {
		return _isHybrid;
	}
	public void setIsSatellite(boolean isSatellite) {
		if (_satelliteEnabled) {
			if (isSatellite)
				setMapType("satellite");
			_isSatellite = isSatellite;
		}
	}
	public boolean getIsSatellite() {
		return _isSatellite;
	}
	public void setIsPhysical(boolean isPhysical) {
		if (_physicalEnabled) {
			if (isPhysical)
				setMapType("physical");
			_isPhysical = isPhysical;
		}
	}
	public boolean getIsPhysical() {
		return _isPhysical;
	}
	public void setNormalEnabled (boolean normalEnabled) {
		_normalEnabled = normalEnabled;
	}
	public boolean getNormalEnabled () {
		return _normalEnabled;
	}
	public void setHybridEnabled (boolean hybridEnabled) {
		_hybridEnabled = hybridEnabled;
	}
	public boolean getHybridEnabled () {
		return _hybridEnabled;
	}
	public void setSatelliteEnabled (boolean satelliteEnabled) {
		_satelliteEnabled = satelliteEnabled;
	}
	public boolean getSatelliteEnabled () {
		return _satelliteEnabled;
	}
	public void setPhysicalEnabled (boolean physicalEnabled) {
		_physicalEnabled = physicalEnabled;
	}
	public boolean getPhysicalEnabled () {
		return _physicalEnabled;
	}
	public void setSensorEnabled (boolean sensorEnabled) {
		_sensorEnabled = sensorEnabled;
	}
	public boolean getSensorEnabled () {
		return _sensorEnabled;
	}
	public void setDoubleClickZoom (boolean doubleClickZoom) {
		_doubleClickZoom = doubleClickZoom;
	}
	public boolean getDoubleClickZoom () {
		return _doubleClickZoom;
	}
	public void setEnableDragging (boolean enableDragging) {
		_enableDragging = enableDragging;
	}
	public boolean getEnableDragging () {
		return _enableDragging;
	}
	public void setScrollWheelZoom (boolean scrollWheelZoom) {
		_scrollWheelZoom = scrollWheelZoom;
	}
	public boolean getScrollWheelZoom () {
		return _scrollWheelZoom;
	}
	public void setShowZoomCtrl (boolean showZoomCtrl) {
		_showZoomCtrl = showZoomCtrl;
	}
	public boolean getShowZoomCtrl () {
		return _showZoomCtrl;
	}
	public void setShowTypeCtrl (boolean showTypeCtrl) {
		_showTypeCtrl = showTypeCtrl;
	}
	public boolean getShowTypeCtrl () {
		return _showTypeCtrl;
	}
	public void setShowOverviewCtrl (boolean showOverviewCtrl) {
		_showOverviewCtrl = showOverviewCtrl;
	}
	public boolean getShowOverviewCtrl () {
		return _showOverviewCtrl;
	}
	public void setShowScaleCtrl (boolean showScaleCtrl) {
		_showScaleCtrl = showScaleCtrl;
	}
	public boolean getShowScaleCtrl () {
		return _showScaleCtrl;
	}
	public void setMmeData(MapMouseEvent event) {
		_mouseEventData.clear();
		_mouseEventData.put("mmeName", event.getName());
		_mouseEventData.put("mmeKeys", event.getKeys()+"");
		_mouseEventData.put("mmeRef", event.getReference().toString());
		_mouseEventData.put("mmeLat", event.getLat()+"");
		_mouseEventData.put("mmeLng", event.getLng()+"");
		_mouseEventData.put("mmeX", event.getX() + "");
		_mouseEventData.put("mmeY", event.getY() + "");
		_mouseEventData.put("mmePageX", event.getPageX() + "");
		_mouseEventData.put("mmePageY", event.getPageY() + "");
		_mouseEventData.put("mmeTarget", event.getTarget().toString());
	}
	public Map getMmeData () {
		return _mouseEventData;
	}
	public void setMdeData(MapDropEvent event) {
		_dropEventData.put("mdeName", event.getName());
		_dropEventData.put("mdeKeys", event.getKeys() + "");
		_dropEventData.put("mdeLat", event.getLat() + "");
		_dropEventData.put("mdeLng", event.getLng() + "");
		_dropEventData.put("mdeX", event.getX() + "");
		_dropEventData.put("mdeY", event.getY() + "");
		_dropEventData.put("mdePageY", event.getPageX() + "");
		_dropEventData.put("mdePageY", event.getPageY() + "");
		_dropEventData.put("mdeTarget", event.getTarget().toString());
	}
	public Map getMdeData () {
		return _dropEventData;
	}
	public void setMzeData(MapZoomEvent event) {
		_zoomEventData.put("mzeName", event.getName());
		_zoomEventData.put("mzeZoom", event.getZoom() + "");
		_zoomEventData.put("mzeTarget", event.getTarget().toString());
	}
	public Map getMzeData () {
		return _zoomEventData;
	}
	public void setMiceData(InfoChangeEvent event) {
		_infoChangeEventData.put("miceName", event.getName());
		_infoChangeEventData.put("miceTarget", event.getTarget().toString());
	}
	public Map getMiceData () {
		return _infoChangeEventData;
	}
}