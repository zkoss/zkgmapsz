/* Gscreen.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Wed Jul 9 9:13:36     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.gmaps.event.MapDropEvent;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.impl.Utils;
import org.zkoss.zul.impl.XulElement;

/**
 * A rectangular image on the Gmaps whose position remains fixed on the screen 
 * even you move the maps. It can be used in showing logos, heads-up display, etc.
 * @author henrichen
 * @see Gimage
 * @deprecated As of release 3.0.2, Google Map v3 do not support GScreenOverlay.
 */
public class Gscreen extends XulElement implements Mapitem {
	private static final long serialVersionUID = 200807021855L;
	protected String _screenX = "0";
	protected String _screenY = "0";
	protected String _offsetX = "0";
	protected String _offsetY = "0";
	protected String _width = "50";
	protected String _height = "50";

	/** image source */
	private String _src;
	/** The image. If not null, _src is generated automatically. */
	private org.zkoss.image.Image _image;
	/** Count the version of {@link #_image}. */
	private int _imgver;

	public Gscreen() {
	}

	/**
	 * Google Screen image (GScreenOverlay).
	 * @param src the image source
	 * @param screenX the X coordinate (in px or percentage of the maps size, 
	 * left-bottom corner) of the screen the image is going to be placed.
	 * @param screenY the Y coordinate (in px or percentage of the maps size, 
	 * left-bottom corner) of the screen the image is going to be placed.
	 * @param offsetX the X offset (in px or percentage of the maps size) to 
	 * move left the image. 
	 * @param offsetY the Y offset (in px or percentage of the maps size) to 
	 * move down the image.
	 * left-bottom corner of the image to be shown. 
	 * @param width the width (in px or percentage of the maps size) of 
	 * the image.
	 * @param height the height (in px or percentage of the maps size) of 
	 * the image.
	 */
	public Gscreen(String src, String screenX, String screenY, String offsetX, String offsetY, String width, String height) {
		setSrc(src);
		setScreenX(screenX);
		setScreenY(screenY);
		setOffsetX(offsetX);
		setOffsetY(offsetY);
		setWidth(width);
		setHeight(height);
	}
	
	/**
	 * Returns the X coordinate (in px or percentage of the maps size,
	 * left-bottom corner as the origin) of the screen the image is going 
	 * to be placed.
	 * @return the screen Y coordinate
	 */
	public String getScreenX() {
		if (_screenX == null) {
			throw new NullPointerException("screenX");
		}
		return _screenX;
	}

	/**
	 * Sets the X coordinate (in px or percentage of the maps size,
	 * left-bottom corner as the origin) of the screen the image is going 
	 * to be placed.
	 * @param screenX the X coordinate in px or percentage
	 */
	public void setScreenX(String screenX) {
		if (screenX == null) {
			throw new NullPointerException("screenX");
		}
		screenX = screenX.trim();
		if (!Objects.equals(screenX, _screenX)) {
			validate(screenX, "screenX");
			_screenX = screenX;
			smartRerender();
		}
	}

	/**
	 * Returns the Y coordinate (in px or percentage of the maps size,
	 * left-bottom corner as the origin) of the screen the image is going 
	 * to be placed.
	 * @return the screen Y coordinate 
	 */
	public String getScreenY() {
		if (_screenY == null) {
			throw new NullPointerException("screenY");
		}
		return _screenY;
	}

	/**
	 * Sets the Y coordinate (in px or percentage of the maps size,
	 * left-bottom corner as the origin) of the screen the image is going 
	 * to be placed.
	 * @param screenY the Y coordinate in px or percentage
	 */
	public void setScreenY(String screenY) {
		if (screenY == null) {
			throw new NullPointerException("screenY");
		}
		screenY = screenY.trim();
		if (!Objects.equals(screenY, _screenY)) {
			validate(screenY, "screenY");
			_screenY = screenY;
			smartRerender();
		}
	}

	/**
	 * Returns the X offset (in px or percentage of the maps size) to 
	 * move left the image.
	 * 
	 * @return the X offset (in px or percentage of the maps size) to 
	 * move left the image.
	 */
	public String getOffsetX() {
		if (_offsetX == null) {
			throw new NullPointerException("offsetX");
		}
		return _offsetX;
	}

	/**
	 * Sets the X offset (in px or percentage of the maps size) to 
	 * move left the image.
	 * 
	 * @param offsetX the X offset (in px or percentage of the maps size) to 
	 * move left the image.
	 */
	public void setOffsetX(String offsetX) {
		if (offsetX == null) {
			throw new NullPointerException("offsetX");
		}
		offsetX = offsetX.trim();
		if (!Objects.equals(offsetX, _offsetX)) {
			validate(offsetX, "offsetX");
			_offsetX = offsetX;
			smartRerender();
		}
	}

	/**
	 * Returns the Y offset (in px or percentage of the maps size) to 
	 * move down the image.
	 * 
	 * @return the Y offset (in px or percentage of the maps size) to 
	 * move down the image.
	 */
	public String getOffsetY() {
		if (_offsetY == null) {
			throw new NullPointerException("offsetY");
		}
		return _offsetY;
	}

	/**
	 * Sets the Y offset (in px or percentage of the maps size) to 
	 * move left the image.
	 * 
	 * @param offsetY the Y offset (in px or percentage of the maps size) to 
	 * move left the image.
	 */
	public void setOffsetY(String offsetY) {
		if (offsetY == null) {
			throw new NullPointerException("offsetY");
		}
		offsetY = offsetY.trim();
		if (!Objects.equals(offsetY, _offsetY)) {
			validate(offsetY, "offsetY");
			_offsetY = offsetY;
			smartRerender();
		}
	}
	
	/**
	 * Set the image width in px or percentage of the maps size.
	 *
	 * @param width the width (in px or percentage of the maps size) of 
	 * the image.
	 */
	public void setWidth(String width) {
		if (width == null) {
			throw new NullPointerException("width");
		}
		width = width.trim();
		if (!Objects.equals(width, _width)) {
			validate(width, "width");
			_width = width;
			smartRerender();
		}
	}
	
	/**
	 * Returns the image width.
	 * @return the image width.
	 */
	public String getWidth() {
		return _width;
	}
	
	/**
	 * Sets the image height in px or percentage of the maps size.
	 * @param height the height (in px or percentage of the maps size) of 
	 * the image.
	 */
	public void setHeight(String height) {
		if (height == null) {
			throw new NullPointerException("height");
		}
		height = height.trim();
		if (!Objects.equals(height, _height)) {
			validate(height, "height");
			_height = height;
			smartRerender();
		}
	}

	/**
	 * Returns the image height.
	 * @return the image height.
	 */
	public String getHeight() {
		return _height;
	}

	/** Returns the source URI of the image.
	 * <p>Default: null.
	 */
	public String getSrc() {
		return _src;
	}
	/** Sets the source URI of the image.
	 *
	 * <p>If {@link #setContent} is ever called with non-null,
	 * it takes heigher priority than this method.
	 *
	 * @param src the URI of the image source
	 */
	public void setSrc(String src) {
		if (src != null && src.length() == 0)
			src = null;

		if (_image != null || !Objects.equals(_src, src)) {
			_src = src;
			_image = null;
			smartRerender();
		}
	}
	/** Sets the content directly.
	 * Default: null.
	 *
	 * @param image the image to display. If not null, it has higher
	 * priority than {@link #getSrc}.
	 */
	public void setContent(org.zkoss.image.Image image) {
		if (image != _image) {
			_image = image;
			if (_image != null) ++_imgver; //enforce browser to reload image
			smartRerender();
		}
	}
	/** Returns the content set by {@link #setContent}.
	 * <p>Note: it won't fetch what is set thru by {@link #setSrc}.
	 * It simply returns what is passed to {@link #setContent}.
	 */
	public org.zkoss.image.Image getContent() {
		return _image;
	}

	//-- Component --//
	/** Default: not childable.
	 */
	public boolean isChildable() {
		return false;
	}

	private void validate(String arg, String name) {
		try {
			String numstr = arg;
			if (arg.endsWith("px")) {
				numstr = arg.substring(0, arg.length() - 2); 
				double num = Double.parseDouble(numstr);
				int inum = (int) num;
				if (inum != num) {
					throw new UiException(name+" allow px, or % only: " + arg);
				}
			} else if (arg.endsWith("%")) {
				numstr = arg.substring(0, arg.length() - 1);
				Double.parseDouble(numstr);
			} else {
				double num = Double.parseDouble(numstr);
				int inum = (int) num;
				if (inum != num) {
					throw new UiException(name+" allows px, or % only: " + arg);
				}
			}
		} catch (NumberFormatException ex) {
			throw new UiException(name+" allow px, or % only: " + arg);
		}
	}
	
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		//render(renderer, "src", getEncodedURL());
		render(renderer, "screenX", getScreenX());
		render(renderer, "screenY", getScreenY());
		render(renderer, "offsetX", getOffsetX());
		render(renderer, "offsetY", getOffsetY());
		render(renderer, "width", getWidth());
		render(renderer, "height", getHeight());
	}
	
	/** Returns the encoded URL of the image (never null).
	 */
	/*
	private String getEncodedURL() {
		if (_image != null)
			return Utils.getDynamicMediaURI( //already encoded
				this, _imgver, "c/" + _image.getName(), _image.getFormat());

		final Desktop dt = getDesktop(); //it might not belong to any desktop
		return dt != null ? dt.getExecution()
			.encodeURL(_src != null ? _src: "~./img/spacer.gif"): "";
	}
	
	private class EncodedURL implements org.zkoss.zk.au.DeferredValue {
		public Object getValue() {
			return getEncodedURL();
		}
	}*/
	private void smartRerender() {
		final Map info = new HashMap();
		//info.put("src", new EncodedURL());
		info.put("screenX", getScreenX());
		info.put("screenY", getScreenY());
		info.put("offsetX", getOffsetX());
		info.put("offsetY", getOffsetY());
		info.put("width", getWidth());
		info.put("height", getHeight());
		
		smartUpdate("rerender_", info);
	}
}
