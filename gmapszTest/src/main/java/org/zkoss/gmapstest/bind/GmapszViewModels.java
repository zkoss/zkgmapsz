package org.zkoss.gmapstest.bind;

import java.util.*;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.gmaps.Ginfo;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.event.InfoChangeEvent;
import org.zkoss.gmaps.event.MapDropEvent;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.gmaps.event.MapMoveEvent;
import org.zkoss.gmaps.event.MapZoomEvent;
import org.zkoss.zk.ui.event.Event;

public class GmapszViewModels {
	private GmapsVM _mapsvm = new GmapsVM();
	private GmarkersVM _markersvmOne = null;
	private GmarkersVM _markersvmTwo = null;
	private GpolylineVM _polylinevm = null;
	private GpolygonVM _polygonvm = null;
	private GinfoVM _infovm = null;
	private GimageVM _imagevm = null;
	private GcircleVM _circlevm = null;
	private Map<String, GmarkersVM> _markersvmMap = new HashMap<String, GmarkersVM>();

	private boolean _controlvisibleOne = true;
	private boolean _controlvisibleTwo;
	private boolean _controlvisibleThree;
	private boolean _controlvisibleFour;
	private boolean _controlvisibleFive;
	private boolean _controlvisibleSix;
	private boolean _controlvisibleSeven;
	
	public GmapsVM getMapsvm () {
		return _mapsvm;
	}
	public GmarkersVM getFirstMarkersvm () {
		if (_markersvmOne == null) {
			_markersvmOne = new GmarkersVM();
			_markersvmOne.setId("firstMarker");
			_markersvmOne.setLat(23);
			_markersvmOne.setLng(121);
			_markersvmOne.setMinzoom(9);
			_markersvmOne.setMaxzoom(11);
			_markersvmOne.setIconImage("/img/Briefcase-16x16.png");
			_markersvmOne.setIconAnchorX(0);
			_markersvmOne.setIconAnchorY(0);
			_markersvmOne.setIconHeight(50);
			_markersvmOne.setIconWidth(50);
			_markersvmOne.setIconShadow("/img/QuestionmarkButton-16x16.png");
			_markersvmOne.setIconShadowHeight(80);
			_markersvmOne.setIconShadowWidth(80);
			_markersvmOne.setContent("test test one");
			_markersvmOne.setOpen(true);
			_markersvmOne.setDraggingEnabled(true);
			_markersvmMap.put("firstMarker", _markersvmOne);
		}
		return _markersvmOne;
	}
	public GmarkersVM getSecondMarkersvm () {
		if (_markersvmTwo == null) {
			_markersvmTwo = new GmarkersVM();
			_markersvmTwo.setId("secondMarker");
			_markersvmTwo.setLat(23.1);
			_markersvmTwo.setLng(121.1);
			_markersvmTwo.setContent("test test second marker");
			_markersvmMap.put("secondMarker", _markersvmTwo);
		}
		return _markersvmTwo;
	}
	public GpolylineVM getPolylinevm () {
		if (_polylinevm == null) {
			_polylinevm = new GpolylineVM();
			_polylinevm.setId("firstPolyline");
			_polylinevm.setColor("#FF0000");
			_polylinevm.setOpacity(100);
			_polylinevm.setWeight(1);
			_polylinevm.setPoints("37.42838786,-122.13998795,1,37.43561240,-122.13277816,1,37.42416187,-122.11441040,1,37.42157162,-122.12007522,1,37.41734524,-122.12316513,1,37.42838786,-122.13998795,1");
		}
		return _polylinevm;
	}
	public GpolygonVM getPolygonvm () {
		if (_polygonvm == null) {
			_polygonvm = new GpolygonVM();
			_polygonvm.setId("firstPolygon");
			_polygonvm.setColor("#FF0000");
			_polygonvm.setOpacity(20);
			_polygonvm.setWeight(1);
			_polygonvm.setOutline(true);
			_polygonvm.setFill(false);
			_polygonvm.setFillColor("#336633");
			_polygonvm.setFillOpacity(20);
			_polygonvm.setPoints("37.43838786,-122.14998795,3,37.44561240,-122.14277816,3,37.43416187,-122.12441040,3,37.43157162,-122.13007522,3,37.42734524,-122.13316513,3");
 		}
		return _polygonvm;
	}
	public GinfoVM getInfovm () {
		if (_infovm == null) {
			_infovm = new GinfoVM();
			_infovm.setId("firstInfo");
			_infovm.setLat(37.42838786);
			_infovm.setLng(-122.13998795);
			_infovm.setContent("Hello, <a href=\"http://www.zkoss.org\">ZK</a>.");
		}
		return _infovm;
	}
	public GimageVM getImagevm () {
		if (_imagevm == null) {
			_imagevm = new GimageVM();
			_imagevm.setId("firstImage");
			_imagevm.setSrc("/img/newIcon.png");
			_imagevm.setNelat(37.44033195);
			_imagevm.setNelng(-122.03191986);
			_imagevm.setSwlat(37.38215478);
			_imagevm.setSwlng(-122.09273453);
		}
		return _imagevm;
	}
	public GcircleVM getCirclevm () {
		if (_circlevm == null)
			_circlevm = new GcircleVM();
		return _circlevm;
	}
	@Command @NotifyChange("mapsvm")
	public void changeMapType() {
		
	}
	@Command @NotifyChange({"mmeData", "firstMarkersvm", "secondMarkersvm"})
	public void mapMouseEvent(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		MapMouseEvent event = (MapMouseEvent)ctx.getTriggerEvent();
		_mapsvm.setMmeData(event);
		if ("onMapClick".equals(event.getName()) && (event.getReference() instanceof Gmarker)) {
			GmarkersVM vm = _markersvmMap.get(((Gmarker)event.getReference()).getId());
			if (!vm.getOpen())
				vm.setOpen(true);
		}
	}

	@Command @NotifyChange({"mdeData", "firstMarkersvm", "secondMarkersvm"})
	public void mapDropEvent(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		MapDropEvent event = (MapDropEvent)ctx.getTriggerEvent();
		_mapsvm.setMdeData(event);
		if (event.getDragged() instanceof Gmarker) {
			GmarkersVM vm = _markersvmMap.get(((Gmarker)event.getDragged()).getId());
			vm.setLat(event.getLat());
			vm.setLng(event.getLng());
		}
	}
	@Command @NotifyChange("mzeData")
	public void mapZoomEvent(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		_mapsvm.setMzeData((MapZoomEvent)ctx.getTriggerEvent());
	}
	@Command @NotifyChange({"miceData", "firstMarkersvm", "secondMarkersvm", "infovm"})
	public void infoChangeEvent(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
		InfoChangeEvent event = (InfoChangeEvent)ctx.getTriggerEvent();
		_mapsvm.setMiceData(event);
		Ginfo info = event.getInfo();
		if (info instanceof Gmarker)
			_markersvmMap.get(info.getId()).setOpen(false);
		else
			_infovm.setOpen(false);
	}
	public Map getMmeData () {
		return _mapsvm.getMmeData();
	}

	public Map getMdeData () {
		return _mapsvm.getMdeData();
	}
	public Map getMzeData () {
		return _mapsvm.getMzeData();
	}
	public Map getMiceData () {
		return _mapsvm.getMiceData();
	}

	@NotifyChange({"controlvisibleOne", "controlvisibleTwo", "controlvisibleThree", "controlvisibleFour", "controlvisibleFive", "controlvisibleSix", "controlvisibleSeven"})
	public void setControlvisibleOne (boolean controlvisibleOne) {
		clearAllVisible();
		_controlvisibleOne = controlvisibleOne;
	}
	public boolean getControlvisibleOne () {
		return _controlvisibleOne;
	}
	@NotifyChange({"controlvisibleOne", "controlvisibleTwo", "controlvisibleThree", "controlvisibleFour", "controlvisibleFive", "controlvisibleSix", "controlvisibleSeven"})
	public void setControlvisibleTwo (boolean controlvisibleTwo) {
		clearAllVisible();
		_controlvisibleTwo = controlvisibleTwo;
	}
	public boolean getControlvisibleTwo () {
		return _controlvisibleTwo;
	}
	@NotifyChange({"controlvisibleOne", "controlvisibleTwo", "controlvisibleThree", "controlvisibleFour", "controlvisibleFive", "controlvisibleSix", "controlvisibleSeven"})
	public void setControlvisibleThree (boolean controlvisibleThree) {
		clearAllVisible();
		_controlvisibleThree = controlvisibleThree;
	}
	public boolean getControlvisibleThree () {
		return _controlvisibleThree;
	}
	@NotifyChange({"controlvisibleOne", "controlvisibleTwo", "controlvisibleThree", "controlvisibleFour", "controlvisibleFive", "controlvisibleSix", "controlvisibleSeven"})
	public void setControlvisibleFour (boolean controlvisibleFour) {
		clearAllVisible();
		_controlvisibleFour = controlvisibleFour;
	}
	public boolean getControlvisibleFour () {
		return _controlvisibleFour;
	}
	@NotifyChange({"controlvisibleOne", "controlvisibleTwo", "controlvisibleThree", "controlvisibleFour", "controlvisibleFive", "controlvisibleSix", "controlvisibleSeven"})
	public void setControlvisibleFive (boolean controlvisibleFive) {
		clearAllVisible();
		_controlvisibleFive = controlvisibleFive;
	}
	public boolean getControlvisibleFive () {
		return _controlvisibleFive;
	}
	@NotifyChange({"controlvisibleOne", "controlvisibleTwo", "controlvisibleThree", "controlvisibleFour", "controlvisibleFive", "controlvisibleSix", "controlvisibleSeven"})
	public void setControlvisibleSix (boolean controlvisibleSix) {
		clearAllVisible();
		_controlvisibleSix = controlvisibleSix;
	}
	public boolean getControlvisibleSix () {
		return _controlvisibleSix;
	}
	@NotifyChange({"controlvisibleOne", "controlvisibleTwo", "controlvisibleThree", "controlvisibleFour", "controlvisibleFive", "controlvisibleSix", "controlvisibleSeven"})
	public void setControlvisibleSeven (boolean controlvisibleSeven) {
		clearAllVisible();
		_controlvisibleSeven = controlvisibleSeven;
	}
	public boolean getControlvisibleSeven () {
		return _controlvisibleSeven;
	}
	private void clearAllVisible () {
		_controlvisibleOne = _controlvisibleTwo = _controlvisibleThree = _controlvisibleFour = _controlvisibleFive = _controlvisibleSix = _controlvisibleSeven = false;
	}
}
