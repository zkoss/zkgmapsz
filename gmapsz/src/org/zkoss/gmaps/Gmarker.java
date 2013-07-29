/* Gmarker.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Oct 20 12:11:20     2006, Created by henrichen
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.gmaps.event.MapDropEvent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.impl.XulElement;

/**
 * The gmarker of the Gooogle Maps. Since 2.0_6 we support associated icon with the GMarker 
 * (see all getIconXxx() and setIconXxx() methods).
 *
 * @author henrichen
 */
public class Gmarker extends Ginfo {
	private static final long serialVersionUID = 200807041527L;
	public static final int ZOOM_LIMIT = 19; 
	private String _iconImage;
	private String _iconShadow;
	private int _iconWidth = -100;
	private int _iconHeight = -100;
	private int _iconShadowWidth = -100;
	private int _iconShadowHeight = -100;
	private int _iconAnchorX = -100;
	private int _iconAnchorY = -100;
	private int _iconInfoAnchorX = -100;
	private int _iconInfoAnchorY = -100;
	private String _iconPrintImage;
	private String _iconMozPrintImage;
	private String _iconPrintShadow;
	private String _iconTransparent;
	private String _iconImageMap;
	private int _iconMaxHeight = -100; 
	private String _iconDragCrossImage;
	private int _iconDragCrossWidth = -100;
	private int _iconDragCrossHeight = -100;
	private int _iconDragCrossAnchorX = -100;
	private int _iconDragCrossAnchorY = -100;
	private int _maxzoom = ZOOM_LIMIT; //visible zoom level default to 19
	private int _minzoom = 0; //visible zoom level default to 0
	private boolean _draggingEnabled;
	private String _tooltiptext; 

	public Gmarker() {
	}
	public Gmarker(String content) {
        super(content);
	}
	public Gmarker(String content, double lat, double lng) {
        super(content, lat, lng);
	}

	/**
	 * Sets whether this Gmarker is enabled to drag "INSIDE" the maps; default is true.
	 * Note that this property takes precedence than {#link #setDraggable}.
	 * If you want to drag Gmarker as other ZK components (that is, you can
	 * drag the Gmarker "OUTSIDE" the maps), you have to set this property 
	 * to "false".
	 * @param b whether enable dragging this Gmarker inside the maps.
	 * @since 2.0_9
	 */
	public void setDraggingEnabled(boolean b) {
		if (_draggingEnabled != b) {
			_draggingEnabled = b;
			smartUpdate("draggingEnabled", b);
		}
	}

	/** Returns whether this Gmarker is enabled to drag "INSIDE" the maps; default is true.
	 * Note that this property takes precedence. If you want to drag Gmarker 
	 * as other ZK components (that is, you can drag the Gmarker "OUTSIDE" 
	 * the maps), you have to set this property to "false".
	 * @return whether this Gmarker is enabled to drag "INSIDE" the maps.
	 * @since 2.0_9
	 */
	public boolean isDraggingEnabled() {
		return _draggingEnabled;
	}

	/**
	 * Returns the x pixel coordinate relative to the top left corner of the icon image at which this 
	 * icon is anchored to the map.
	 * @return x pixel coordinate relative to the top left corner of the icon image
	 * @since 2.0_6
	 */
	public int getIconAnchorX() {
		return _iconAnchorX;
	}
	/**
	 * Sets the x pixel coordinate relative to the top left corner of the icon image at which this 
	 * icon is anchored to the map. No operation if a value less than -100.
	 * @param iconAnchorX x pixel coordinate relative to the top left corner of the icon image
	 * @since 2.0_6
	 */
	public void setIconAnchorX(int iconAnchorX) {
		if (_iconAnchorX != iconAnchorX && iconAnchorX > -100) {
			_iconAnchorX = iconAnchorX;
			smartRerender();
		}
	}
	/**
	 * Returns the y pixel coordinate relative to the top left corner of the icon image at which this 
	 * icon is anchored to the map.
	 * @return the y pixel coordinate relative to the top left corner of the icon image
	 * @since 2.0_6
	 */
	public int getIconAnchorY() {
		return _iconAnchorY;
	}
	/**
	 * Sets the y pixel coordinate relative to the top left corner of the icon image at which this 
	 * icon is anchored to the map. No operation if a value less than -100. 
	 * @param iconAnchorY y pixel coordinate relative to the top left corner of the icon image
	 * @since 2.0_6
	 */
	public void setIconAnchorY(int iconAnchorY) {
		if (_iconAnchorY != iconAnchorY && iconAnchorY > -100) {
			_iconAnchorY = iconAnchorY;
			smartRerender();
		}
	}
	private int[] getIconAnchor() {
		return (getIconAnchorX() <= -100 || getIconAnchorY() <= -100) ? 
				null : new int[] {getIconAnchorX(), getIconAnchorY()};
	}
	/**
	 * Returns the x pixel coordinate offsets (relative to the iconAnchor) of the cross image when 
	 * an icon is dragged.
	 * @return the iconDragCrossAnchorX
	 * @since 2.0_6
	 */
	public int getIconDragCrossAnchorX() {
		return _iconDragCrossAnchorX;
	}
	/**
	 * Sets the x pixel coordinate offsets (relative to the iconAnchor) of the cross image when 
	 * an icon is dragged. No operation if a value less than -100.
	 * @param iconDragCrossAnchorX the iconDragCrossAnchorX to set
	 * @since 2.0_6
	 */
	public void setIconDragCrossAnchorX(int iconDragCrossAnchorX) {
		if (_iconDragCrossAnchorX != iconDragCrossAnchorX && iconDragCrossAnchorX > -100) {
			_iconDragCrossAnchorX = iconDragCrossAnchorX;
			smartRerender();
		}
	}
	/**
	 * Returns the y pixel coordinate offsets (relative to the iconAnchor) of the cross image when 
	 * an icon is dragged. 
	 * @return the iconDragCrossAnchorX
	 * @since 2.0_6
	 */
	public int getIconDragCrossAnchorY() {
		return _iconDragCrossAnchorY;
	}
	/**
	 * Sets the y pixel coordinate offsets (relative to the iconAnchor) of the cross image when 
	 * an icon is dragged. No operation if a value less than -100.
	 * @param iconDragCrossAnchorY the iconDragCrossAnchorY to set
	 * @since 2.0_6
	 */
	public void setIconDragCrossAnchorY(int iconDragCrossAnchorY) {
		if (_iconDragCrossAnchorY != iconDragCrossAnchorY && iconDragCrossAnchorY > -100) {
			_iconDragCrossAnchorY = iconDragCrossAnchorY;
			smartRerender();
		}
	}
	private int[] getIconDragCrossAnchor() {
		return (getIconDragCrossAnchorX() < 0 || getIconDragCrossAnchorY() < 0) ? 
				null : new int[] {getIconDragCrossAnchorX(),getIconDragCrossAnchorY()};
	}
	/**
	 * Returns the cross image URL when an icon is dragged.
	 * @return the iconDragCrossImage
	 * @since 2.0_6
	 */
	public String getIconDragCrossImage() {
		return _iconDragCrossImage;
	}
	/**
	 * Sets the cross image URL when an icon is dragged. No operation if a null value.
	 * @param iconDragCrossImage the iconDragCrossImage to set
	 * @since 2.0_6
	 */
	public void setIconDragCrossImage(String iconDragCrossImage) {
		if (iconDragCrossImage != null && !iconDragCrossImage.equals(_iconDragCrossImage)) {
			_iconDragCrossImage = iconDragCrossImage;
			smartRerender();
		}
	}
	/**
	 * Returns the pixel height of the cross image when an icon is dragged. 
	 * @return the iconDragCrossHeight
	 * @since 2.0_6
	 */
	public int getIconDragCrossHeight() {
		return _iconDragCrossHeight;
	}
	/**
	 * Sets the pixel height of the cross image when an icon is dragged. 
	 * No operation if a  value less than 0. 
	 * @param iconDragCrossHeight the iconDragCrossHeight to set
	 * @since 2.0_6
	 */
	public void setIconDragCrossHeight(int iconDragCrossHeight) {
		if (_iconDragCrossHeight != iconDragCrossHeight && iconDragCrossHeight >= 0) {
			_iconDragCrossHeight = iconDragCrossHeight;
			smartRerender();
		}
	}
	/**
	 * Returns the pixel width of the cross image when an icon is dragged.
	 * @return the iconDragCrossWidth
	 * @since 2.0_6
	 */
	public int getIconDragCrossWidth() {
		return _iconDragCrossWidth;
	}
	/**
	 * Sets the pixel width of the cross image when an icon is dragged. 
	 * No operation if a value less than 0. 
	 * @param iconDragCrossWidth the iconDragCrossWidth to set
	 * @since 2.0_6
	 */
	public void setIconDragCrossWidth(int iconDragCrossWidth) {
		if (_iconDragCrossWidth != iconDragCrossWidth && iconDragCrossWidth >= 0) {
			_iconDragCrossWidth = iconDragCrossWidth;
			smartRerender();
		}
	}
	private int[] getIconDragCrossSize() {
		return (getIconDragCrossWidth() < 0 || getIconDragCrossHeight() < 0) ? 
				null : new int[] {getIconDragCrossWidth(),getIconDragCrossHeight()};
	}
	/**
	 * Returns the foreground image URL of the icon.
	 * @return the iconImage URL
	 * @since 2.0_6
	 */
	public String getIconImage() {
		return _iconImage;
	}
	/**
	 * Sets the foreground image URL of the icon. No operation if a null value.
	 * @param iconImage the iconImage URL to set
	 * @since 2.0_6
	 */
	public void setIconImage(String iconImage) {
		if (iconImage != null && !iconImage.equals(_iconImage)) {
			_iconImage = iconImage;
			smartUpdate("iconImage", encodeURL(iconImage));
		}
	}
	/**
	 * Return a comma delimitered integers representing the x/y coordinates of the image map we 
	 * should use to specify the clickable part of the icon image in browsers other than 
	 * Internet Explorer.
	 * @return the iconImageMap
	 * @since 2.0_6
	 */
	public String getIconImageMap() {
		return _iconImageMap;
	}
	/**
	 * Sets an comma delimited integers representing the x/y coordinates of the image map we 
	 * should use to specify the click-able part of the icon image in browsers other than 
	 * Internet Explorer. No operation if a null value. 
	 * @param iconImageMap the iconImageMap to set
	 * @since 2.0_6
	 */
	public void setIconImageMap(String iconImageMap) {
		if (iconImageMap != null && !iconImageMap.equals(_iconImageMap)) {
			_iconImageMap = iconImageMap;
			smartRerender();
		}
	}
	/**
	 * Returns the x pixel coordinate relative to the top left corner of the icon image at which the info 
	 * window is anchored to this icon.
	 * @return the iconInfoAnchorX
	 * @since 2.0_6
	 */
	public int getIconInfoAnchorX() {
		return _iconInfoAnchorX;
	}
	/**
	 * Sets the x pixel coordinate relative to the top left corner of the icon image at which the info 
	 * window is anchored to this icon. No operation if a value less than -100.
	 * @param iconInfoAnchorX the iconInfoAnchorX to set
	 * @since 2.0_6
	 */
	public void setIconInfoAnchorX(int iconInfoAnchorX) {
		if (_iconInfoAnchorX != iconInfoAnchorX && iconInfoAnchorX > -100) {
			_iconInfoAnchorX = iconInfoAnchorX;
			smartRerender();
		}
	}
	/**
	 * Returns the y pixel coordinate relative to the top left corner of the icon image at which the info 
	 * window is anchored to this icon.
	 * @return the iconInfoAnchorY
	 * @since 2.0_6
	 */
	public int getIconInfoAnchorY() {
		return _iconInfoAnchorY;
	}
	/**
	 * Sets the y pixel coordinate relative to the top left corner of the icon image at which the info 
	 * window is anchored to this icon. No operation if a value less than -100.
	 * @param iconInfoAnchorY the iconInfoAnchorY to set
	 * @since 2.0_6
	 */
	public void setIconInfoAnchorY(int iconInfoAnchorY) {
		if (_iconInfoAnchorY != iconInfoAnchorY && iconInfoAnchorY > -100) {
			_iconInfoAnchorY = iconInfoAnchorY;
			smartRerender();
		}
	}
	private int[] getIconInfoAnchor() {
		return (getIconInfoAnchorX() < 0 || getIconInfoAnchorY() < 0) ? 
				null : new int[] {getIconInfoAnchorX(), getIconInfoAnchorY()};
	}
	/**
	 * Returns the distance pixels in which a marker will visually "rise" vertically when dragged. 
	 * 
	 * @return the iconMaxHeight
	 * @since 2.0_6
	 */
	public int getIconMaxHeight() {
		return _iconMaxHeight;
	}
	/**
	 * Sets the distance pixels in which a marker will visually "rise" vertically when dragged. 
	 * No operation if a value less than 0.
	 * @param iconMaxHeight the iconMaxHeight to set
	 * @since 2.0_6
	 */
	public void setIconMaxHeight(int iconMaxHeight) {
		if (_iconMaxHeight != iconMaxHeight && iconMaxHeight >= 0) {
			_iconMaxHeight = iconMaxHeight;
			smartRerender();
		}
		
	}
	/**
	 * Returns the URL of the foreground icon image used for printed maps in Firefox/Mozilla. 
	 * It must be the same size as the main icon image given by image.
	 * @return the iconMozPrintImage
	 * @since 2.0_6
	 */
	public String getIconMozPrintImage() {
		return _iconMozPrintImage;
	}
	/**
	 * Sets the URL of the foreground icon image used for printed maps in Firefox/Mozilla. 
	 * It must be the same size as the main icon image given by image. 
	 * No operation if a null value. 
	 * @param iconMozPrintImage the iconMozPrintImage to set
	 * @since 2.0_6
	 */
	public void setIconMozPrintImage(String iconMozPrintImage) {
		if (iconMozPrintImage != null && !iconMozPrintImage.equals(_iconMozPrintImage)) {
			_iconMozPrintImage = iconMozPrintImage;
			smartRerender();
		}
	}
	/**
	 * Returns the URL of the foreground icon image used for printed maps. It must be the same 
	 * size as the main icon image given by image.
	 * @return the iconPrintImage
	 * @since 2.0_6
	 */
	public String getIconPrintImage() {
		return _iconPrintImage;
	}
	/**
	 * Sets the URL of the foreground icon image used for printed maps. It must be the same size 
	 * as the main icon image given by image. 
	 * No operation if a null value.  
	 * @param iconPrintImage the iconPrintImage to set
	 * @since 2.0_6
	 */
	public void setIconPrintImage(String iconPrintImage) {
		if (iconPrintImage != null && !iconPrintImage.equals(_iconPrintImage)) {
			_iconPrintImage = iconPrintImage;
			smartRerender();
		}
	}
	/**
	 * Returns the URL of the shadow image used for printed maps. It should be a GIF image since most 
	 * browsers cannot print PNG images.
	 * @return the iconPrintShadow
	 * @since 2.0_6
	 */
	public String getIconPrintShadow() {
		return _iconPrintShadow;
	}
	/**
	 * Sets the URL of the shadow image used for printed maps. It should be a GIF image since most 
	 * browsers cannot print PNG images. No operation if a null value.
	 * @param iconPrintShadow the iconPrintShadow to set
	 * @since 2.0_6
	 */
	public void setIconPrintShadow(String iconPrintShadow) {
		if (iconPrintShadow != null && !iconPrintShadow.equals(_iconPrintShadow)) {
			_iconPrintShadow = iconPrintShadow;
			smartRerender();
		}
	}
	/**
	 * Returns the shadow image URL of the icon.
	 * @return the iconShadow
	 * @since 2.0_6
	 */
	public String getIconShadow() {
		return _iconShadow;
	}
	/**
	 * Sets the shadow image URL of the icon. No operation if a null value.
	 * @param iconShadow the iconShadow to set
	 * @since 2.0_6
	 */
	public void setIconShadow(String iconShadow) {
		if (iconShadow != null && !iconShadow.equals(_iconShadow)) {
			_iconShadow = iconShadow;
			smartRerender();
		}
	}
	/**
	 * Returns the pixel height of the shadow image.
	 * @return the iconShadowHeight
	 * @since 2.0_6
	 */
	public int getIconShadowHeight() {
		return _iconShadowHeight;
	}
	/**
	 * Sets the pixel height of the shadow image. No operation if a value less than 0.
	 * @param iconShadowHeight the iconShadowHeight to set
	 * @since 2.0_6
	 */
	public void setIconShadowHeight(int iconShadowHeight) {
		if (_iconShadowHeight != iconShadowHeight && iconShadowHeight >= 0) {
			_iconShadowHeight = iconShadowHeight;
			smartRerender();
		}
	}
	/**
	 * Return the pixel width of the shadow image.
	 * @return the iconShadowWidth
	 * @since 2.0_6
	 */
	public int getIconShadowWidth() {
		return _iconShadowWidth;
	}
	/**
	 * Sets the pixel width of the shadow image. No operation if a value less than 0.
	 * @param iconShadowWidth the iconShadowWidth to set
	 * @since 2.0_6
	 */
	public void setIconShadowWidth(int iconShadowWidth) {
		if (_iconShadowWidth != iconShadowWidth && iconShadowWidth >=0) {
			_iconShadowWidth = iconShadowWidth;
			smartRerender();
		}
	}
	private int[] getIconShadowSize() {
		return (getIconShadowWidth() < 0 || getIconShadowHeight() < 0) ? 
				null : new int[] {getIconShadowWidth(), getIconShadowHeight()};
	}
	/**
	 * Returns the pixel height of the foreground image of the icon.
	 * @return the iconHeight
	 * @since 2.0_6
	 */
	public int getIconHeight() {
		return _iconHeight;
	}
	/**
	 * Sets the pixel height of the foreground image of the icon. 
	 * No operation if a value less than 0.
	 * @param iconHeight the iconHeight to set
	 * @since 2.0_9
	 */
	public void setIconHeight(int iconHeight) {
		if (_iconHeight != iconHeight && iconHeight >= 0) {
			_iconHeight = iconHeight;
			smartRerender();
		}
	}
	
	/**
	 * Sets the pixel height of the foreground image of the icon. 
	 * No operation if a value less than 0.
	 * @param iconHeight the iconSizeHeight to set
	 * @since 2.0_6
	 * @deprecated use {@link #setIconHeight(int)} instead
	 */
	public void setIconSizeHeight(int iconHeight) {
		setIconHeight(iconHeight);
	}
	/**
	 * Returns the pixel width of the foreground image of the icon.
	 * @return the iconWidth
	 * @since 2.0_6
	 */
	public int getIconWidth() {
		return _iconWidth;
	}
	/**
	 * Sets the pixel width of the foreground image of the icon. 
	 * No operation if a value less than 0.
	 * @param iconWidth the icon width in pixel to set
	 * @since 2.0_6
	 */
	public void setIconWidth(int iconWidth) {
		if (_iconWidth != iconWidth && iconWidth >=0) {
			_iconWidth = iconWidth;
			smartRerender();
		}
	}
	private int[] getIconSize() {
		return (getIconWidth() < 0 || getIconHeight() < 0) ? 
				null : new int[] {getIconWidth(), getIconHeight()};
	}
	/**
	 * Returns the URL of a virtually transparent version of the foreground 
	 * icon image used to capture click events in Internet Explorer. This image should 
	 * be a 24-bit PNG version of the main icon image with 1% opacity, but the same 
	 * shape and size as the main icon.
	 * @return the iconTransparent
	 * @since 2.0_6
	 */
	public String getIconTransparent() {
		return _iconTransparent;
	}
	/**
	 * Sets the URL of a virtually transparent version of the foreground 
	 * icon image used to capture click events in Internet Explorer. This image should 
	 * be a 24-bit PNG version of the main icon image with 1% opacity, but the same 
	 * shape and size as the main icon. No operation if a null value.
	 * @param iconTransparent the iconTransparent to set
	 * @since 2.0_6
	 */
	public void setIconTransparent(String iconTransparent) {
		if (iconTransparent != null && !iconTransparent.equals(_iconTransparent)) {
			_iconTransparent = iconTransparent;
			smartRerender();
		}
	}

	/**
	 * Returns the maximum visible zoom level of this Gmarker (default to 17).
	 * @since 2.0_8
	 */
	public int getMaxzoom() {
		return _maxzoom;
	}
	
	/**
	 * Sets the maximum visible zoom level of this Gmarker (max 17).
	 * @since 2.0_8
	 */
	public void setMaxzoom(int lv) {
		if (lv > ZOOM_LIMIT || lv < 0) {
			throw new UiException("maxzoom level must be between 0 ~ "+ZOOM_LIMIT+". maxzoom: "+lv);
		}
		if (_maxzoom != lv) {
			_maxzoom = lv;
			smartRerender();
		}
	}
	
	/**
	 * Returns the minimum visible zoom level of this Gmarker (default to 0).
	 * @since 2.0_8
	 */
	public int getMinzoom() {
		return _minzoom;
	}
	
	/**
	 * Sets the minimum visible zoom level of this Gmarker (min 0).
	 * @since 2.0_8
	 */
	public void setMinzoom(int lv) {
		if (lv > ZOOM_LIMIT || lv < 0) {
			throw new UiException("maxzoom level must be between 0 ~ "+ZOOM_LIMIT+". maxzoom: "+lv);
		}
		if (_minzoom != lv) {
			_minzoom = lv;
			smartRerender();
		}
	}
	
	/** Sets the Tooltiptext of the anchor point.
	 */
	public void setTooltiptext(String tooltiptext) {
		if (tooltiptext != _tooltiptext) {
			_tooltiptext = tooltiptext;
			smartUpdate("tooltiptext", _tooltiptext);
		}
	}
	
	/** Gets the Tooltiptext of the anchor point.
	 */
	public String getTooltiptext() {
		return _tooltiptext;
	}

	private String encodeURL(String url) {
		final Desktop desktop = getDesktop();  
		return (desktop == null ? Executions.getCurrent() : desktop.getExecution()).encodeURL(url);
	}
	
	/*package*/ boolean isGinfo() {
		return false;
	}
	
	/** used by the MarkerDropEvent */
	/* package */ void setLatByClient(double lat) {
		_lat = lat;
	}
	/* package */ void setLngByClient(double lng) {
		_lng = lng;
	}
	
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		if (getIconImage() != null) {
			render(renderer, "iconImage", encodeURL(getIconImage()));
		}
		if (getIconShadow() != null) {
			render(renderer, "iconShadow", encodeURL(getIconShadow()));
		}
		if (getIconSize() != null) {
			render(renderer, "iconSize", getIconSize());
		}
		if (getIconShadowSize() != null) {
			render(renderer, "iconShadowSize", getIconShadowSize());
		}
		if (getIconAnchor() != null) {
			render(renderer, "iconAnchor", getIconAnchor());
		}
		if (getIconInfoAnchor() != null) {
			render(renderer, "iconInfoAnchor", getIconInfoAnchor());
		}
		if (getIconPrintImage() != null) {
			render(renderer, "iconPrintImage", encodeURL(getIconPrintImage()));
		}
		if (getIconMozPrintImage() != null) {
			render(renderer, "iconMozPrintImage", encodeURL(getIconMozPrintImage()));
		}
		if (getIconPrintShadow() != null) {
			render(renderer, "iconPrintShadow", encodeURL(getIconPrintShadow()));
		}
		if (getIconTransparent() != null) {
			render(renderer, "iconTransparent", encodeURL(getIconTransparent()));
		}
		if (getIconImageMap() != null) {
			render(renderer, "iconImageMap", getIconImageMap());
		}
		if (getIconMaxHeight() >= 0) {
			render(renderer, "iconMaxHeight", new Integer(getIconMaxHeight()));
		}
		if (getIconDragCrossImage() != null) {
			render(renderer, "iconDragCrossImage", encodeURL(getIconDragCrossImage()));
		}
		if (getIconDragCrossSize() != null) {
			render(renderer, "iconDragCrossSize", getIconDragCrossSize());
		}
		if (getIconDragCrossAnchor() != null) {
			render(renderer, "iconDragCrossAnchor", getIconDragCrossAnchor());
		}
		if (isDraggingEnabled()) {
			render(renderer, "draggingEnabled", true);
		}
		if (getTooltiptext() != null) {
			render(renderer, "tooltiptext", getTooltiptext());
		}
		render(renderer, "maxzoom", new Integer(getMaxzoom()));
		render(renderer, "minzoom", new Integer(getMinzoom()));
	}
	
	/** Processes an AU request.
	 *
	 * <p>Default: in addition to what are handled by {@link XulElement#service},
	 * it also handles onSelect.
	 * @since 5.0.0
	 */
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals("onMapDrop")) {
			final MapDropEvent evt = MapDropEvent.getMapDropEvent(request);
			setLatByClient(evt.getLat());
			setLngByClient(evt.getLng());
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}
	
	private void smartRerender() {
		final Map info = new HashMap();
		
		if (getIconShadow() != null) {
			info.put("iconShadow", encodeURL(getIconShadow()));
		}
		if (getIconSize() != null) {
			info.put("iconSize", getIconSize());
		}
		if (getIconShadowSize() != null) {
			info.put("iconShadowSize", getIconShadowSize());
		}
		if (getIconAnchor() != null) {
			info.put("iconAnchor", getIconAnchor());
		}
		if (getIconInfoAnchor() != null) {
			info.put("iconInfoAnchor", getIconInfoAnchor());
		}
		if (getIconPrintImage() != null) {
			info.put("iconPrintImage", encodeURL(getIconPrintImage()));
		}
		if (getIconMozPrintImage() != null) {
			info.put("iconMozPrintImage", encodeURL(getIconMozPrintImage()));
		}
		if (getIconPrintShadow() != null) {
			info.put("iconPrintShadow", encodeURL(getIconPrintShadow()));
		}
		if (getIconTransparent() != null) {
			info.put("iconTransparent", encodeURL(getIconTransparent()));
		}
		if (getIconImageMap() != null) {
			info.put("iconImageMap", getIconImageMap());
		}
		if (getIconMaxHeight() >= 0) {
			info.put("iconMaxHeight", new Integer(getIconMaxHeight()));
		}
		if (getIconDragCrossImage() != null) {
			info.put("iconDragCrossImage", encodeURL(getIconDragCrossImage()));
		}
		if (getIconDragCrossSize() != null) {
			info.put("iconDragCrossSize", getIconDragCrossSize());
		}
		if (getIconDragCrossAnchor() != null) {
			info.put("iconDragCrossAnchor", getIconDragCrossAnchor());
		}
		if (getTooltiptext() != null) {
			info.put("tooltiptext", getTooltiptext());
		}
		info.put("maxzoom", new Integer(getMaxzoom()));
		info.put("minzoom", new Integer(getMinzoom()));
		
		smartUpdate("rerender_", info);
	}

	//register the Gmaps related event
	static {
		addClientEvent(Gmarker.class, "onMapDrop", CE_DUPLICATE_IGNORE | CE_IMPORTANT);
	}
}
