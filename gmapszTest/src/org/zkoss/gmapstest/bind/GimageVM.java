package org.zkoss.gmapstest.bind;

public class GimageVM {
	private String _id;
	private String _src;
	private double _nelat;
	private double _nelng;
	private double _swlat;
	private double _swlng;

	public void setId (String id) {
		_id = id;
	}
	public String getId () {
		return _id;
	}
	public void setSrc (String src) {
		_src = src;
	}
	public String getSrc () {
		return _src;
	}
	public void setNelat (double nelat) {
		_nelat = nelat;
	}
	public double getNelat () {
		return _nelat;
	}
	public void setNelng (double nelng) {
		_nelng = nelng;
	}
	public double getNelng () {
		return _nelng;
	}
	public void setSwlat (double swlat) {
		_swlat = swlat;
	}
	public double getSwlat () {
		return _swlat;
	}
	public void setSwlng (double swlng) {
		_swlng = swlng;
	}
	public double getSwlng () {
		return _swlng;
	}
}
