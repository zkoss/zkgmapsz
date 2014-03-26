package org.zkoss.gmapstest.bind;

public class GpolygonVM {
	private String _id;
	private boolean _fill;
	private boolean _outline;
	private int _opacity;
	private int _weight;
	private String _color;
	private int _fillOpacity;
	private String _fillColor;
	private String _points;

	public void setId (String id) {
		_id = id;
	}
	public String getId () {
		return _id;
	}
	public void setFill (boolean fill) {
		_fill = fill;
	}
	public boolean getFill () {
		return _fill;
	}
	public void setOutline (boolean outline) {
		_outline = outline;
	}
	public boolean getOutline () {
		return _outline;
	}
	public void setOpacity (int opacity) {
		_opacity = opacity;
	}
	public int getOpacity () {
		return _opacity;
	}
	public void setWeight (int weight) {
		_weight = weight;
	}
	public int getWeight () {
		return _weight;
	}
	public void setColor (String color) {
		_color = color;
	}
	public String getColor () {
		return _color;
	}
	public void setFillOpacity (int fillOpacity) {
		_fillOpacity = fillOpacity;
	}
	public int getFillOpacity () {
		return _fillOpacity;
	}
	public void setFillColor (String fillColor) {
		_fillColor = fillColor;
	}
	public String getFillColor () {
		return _fillColor;
	}
	public void setPoints (String points) {
		_points = points;
	}
	public String getPoints () {
		return _points;
	}
}