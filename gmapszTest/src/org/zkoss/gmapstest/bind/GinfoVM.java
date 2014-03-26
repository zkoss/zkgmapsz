package org.zkoss.gmapstest.bind;

public class GinfoVM {
	private String _id;
	private boolean _open;
	private double _lat;
	private double _lng;
	private String _content;

	public void setId (String id) {
		_id = id;
	}
	public String getId () {
		return _id;
	}
	public void setOpen (boolean open) {
		_open = open;
	}
	public boolean getOpen () {
		return _open;
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
	public double getLng () {
		return _lng;
	}
	public void setContent (String content) {
		_content = content;
	}
	public String getContent () {
		return _content;
	}
}