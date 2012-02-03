package org.zkoss.gmapstest.bind;

public class GmarkersVM {
	private String _id;
	private double _lat;
	private double _lng;
	private int _minzoom;
	private int _maxzoom;
	private int _iconAnchorX;
	private int _iconAnchorY;
	private String _iconImage;
	private int _iconHeight;
	private int _iconWidth;
	private String _iconShadow;
	private int _iconShadowHeight;
	private int _iconShadowWidth;
	private String _content;
	private boolean _open;
	private boolean _draggingEnabled;

	public void setId (String id) {
		_id = id;
	}
	public String getId() {
		return _id;
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

	public void setMinzoom (int minzoom) {
		_minzoom = minzoom;
	}
	public int getMinzoom () {
		return _minzoom;
	}

	public void setMaxzoom (int maxzoom) {
		_maxzoom = maxzoom;
	}
	public int getMaxzoom () {
		return _maxzoom;
	}

	public void setIconAnchorX (int iconAnchorX) {
		_iconAnchorX = iconAnchorX;
	}
	public int getIconAnchorX () {
		return _iconAnchorX;
	}

	public void setIconAnchorY (int iconAnchorY) {
		_iconAnchorY = iconAnchorY;
	}
	public int getIconAnchorY () {
		return _iconAnchorY;
	}
	public void setIconImage (String iconImage) {
		_iconImage = iconImage;
	}
	public String getIconImage() {
		return _iconImage;
	}

	public void setIconHeight (int iconHeight) {
		_iconHeight = iconHeight;
	}
	public int getIconHeight () {
		return _iconHeight;
	}

	public void setIconWidth (int iconWidth) {
		_iconWidth = iconWidth;
	}
	public int getIconWidth () {
		return _iconWidth;
	}
	public void setIconShadow (String iconShadow) {
		_iconShadow = iconShadow;
	}
	public String getIconShadow() {
		return _iconShadow;
	}

	public void setIconShadowHeight (int iconShadowHeight) {
		_iconShadowHeight = iconShadowHeight;
	}
	public int getIconShadowHeight () {
		return _iconShadowHeight;
	}

	public void setIconShadowWidth (int iconShadowWidth) {
		_iconShadowWidth = iconShadowWidth;
	}
	public int getIconShadowWidth () {
		return _iconShadowWidth;
	}
	public void setContent (String content) {
		_content = content;
	}
	public String getContent() {
		return _content;
	}

	public void setOpen(boolean open) {
		_open = open;
	}
	public boolean getOpen () {
		return _open;
	}

	public void setDraggingEnabled(boolean draggingEnabled) {
		_draggingEnabled = draggingEnabled;
	}
	public boolean getDraggingEnabled () {
		return _draggingEnabled;
	}
}