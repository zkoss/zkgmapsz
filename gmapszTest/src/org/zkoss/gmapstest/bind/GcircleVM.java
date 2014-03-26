package org.zkoss.gmapstest.bind;

public class GcircleVM {

	private String _id= "circle";
	private boolean _clickable = false;
	private boolean _editable = true;
	private String _fillColor = "red";
	private int _fillOpacity = 80;
	private double _radius = 1000.0;
	private String _strokeColor = "green";
	private int _strokeOpacity = 30;
	private int _strokeWeight = 3;
	private int _circleZIndex = 1;
	private double _lat = 37.43633195;
	private double _lng = -122.09073453;
	private boolean _circleVisible = true;

	public String getId() {
		return _id;
	}
	public void setId(String id) {
		_id = id;
	}

	public boolean getClickable () {
		return _clickable;
	}
	public void setClickable (boolean clickable) {
		_clickable = clickable;
	}

	public boolean getEditable () {
		return _editable;
	}
	public void setEditable (boolean editable) {
		_editable = editable;
	}

	public String getFillColor () {
		return _fillColor;
	}
	public void setFillColor (String fillColor) {
		_fillColor = fillColor;
	}

	public int getFillOpacity () {
		return _fillOpacity;
	}
	public void setFillOpacity (int fillOpacity) {
		_fillOpacity = fillOpacity;
	}

	public double getRadius () {
		return _radius;
	}
	public void setRadius (double radius) {
		_radius = radius;
	}

	public String getStrokeColor () {
		return _strokeColor;
	}
	public void setStrokeColor (String strokeColor) {
		_strokeColor = strokeColor;
	}

	public int getStrokeOpacity () {
		return _strokeOpacity;
	}
	public void setStrokeOpacity (int strokeOpacity) {
		_strokeOpacity = strokeOpacity;
	}

	public int getStrokeWeight () {
		return _strokeWeight;
	}
	public void setStrokeWeight (int strokeWeight) {
		_strokeWeight = strokeWeight;
	}

	public int getCircleZIndex () {
		return _circleZIndex;
	}
	public void setCircleZIndex (int circleZIndex) {
		_circleZIndex = circleZIndex;
	}

	public double getLat () {
		return _lat;
	}
	public void setLat (double lat) {
		_lat = lat;
	}

	public double getLng () {
		return _lng;
	}
	public void setLng (double lng) {
		_lng = lng;
	}

	public boolean getCircleVisible () {
		return _circleVisible;
	}
	public void setCircleVisible (boolean circleVisible) {
		_circleVisible = circleVisible;
	}
}
