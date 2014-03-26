package org.zkoss.gmapstest.bind;

public class GpolylineVM {
	private String _id;
	private String _color;
	private int _opacity;
	private int _weight;
	private String _points;

	public void setId(String id) {
		_id = id;
	}
	public String getId () {
		return _id;
	}
	public void setColor(String color) {
		_color = color;
	}
	public String getColor () {
		return _color;
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
	public void setPoints(String points) {
		_points = points;
	}
	public String getPoints () {
		return _points;
	}
}
