/* Gcircle.java
{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Mar 2, 2012 10:38:11 AM, Created by benbai
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/

package org.zkoss.gmaps;

import java.util.Map;

import org.zkoss.gmaps.event.CenterChangeEvent;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.impl.XulElement;
/**
 * Google Maps support Gcircle.
 * 
 * @author benbai
 * @since 3.0.0
 */
public class Gcircle extends XulElement implements Mapitem {
	private static final long serialVersionUID = 7919622962923909687L;
	private LatLng _center = new LatLng(0, 0);
	private boolean _clickable = true; // to match google map default value
	private boolean _editable;
	private String _fillColor;
	private int _fillOpacity = -1; // -1 denotes default, not render it
	private double _radius = 200; // default radius
	private String _strokeColor;
	private int _strokeOpacity = -1; // -1 denotes default, not render it
	private int _strokeWeight = -1; // -1 denotes default, not render it
	private boolean _circleVisible = true; // to match google map default value
	private int _circleZIndex = -1; // -1 denotes default, not render it
	
	/** get the latitude of the center of the circle.
	 * @return double the latitude of the center of the circle.
	 */
	public double getLat () {
		return _center.getLatitude();
	}
	/** set the latitude of the center of the circle.
	 * @param lat the latitude of the center of the circle.
	 */
	public void setLat (double lat) {
		if (!Objects.equals(lat, _center.getLatitude())) {
			setCenter(new LatLng(lat, _center.getLongitude()));
		}
	}

	/** get the longitude of the center of the circle.
	 * @return double the longitude of the center of the circle.
	 */
	public double getLng () {
		return _center.getLongitude();
	}
	/** set the longitude of the center of the circle.
	 * @param lng the longitude of the center of the circle.
	 */
	public void setLng (double lng) {
		if (!Objects.equals(lng, _center.getLongitude())) {
			setCenter(new LatLng(_center.getLatitude(), lng));
		}
	}
	
	/** Sets the Circle's center.
	 * @param center the center of the circle.
	 * @Since 3.0.2
	 */
	public void setCenter(LatLng center) {
		if (!_center.equals(center)) {
			_center = center;
			smartUpdate("center", GmapsUtil.latLngToArray(center));
		}
	}
	
	public LatLng getCenter() {
		return _center;
	}

	/** Returns whether this Gcircle is clickable.
	 * @return whether this Gcircle is clickable.
	 */
	public boolean isClickable () {
		return _clickable;
	}
	/** Sets whether this Gcircle is clickable; default to true.
	 * @param clickable whether enable click the Gcircle.
	 */
	public void setClickable (boolean clickable) {
		if (_clickable != clickable) {
			_clickable = clickable;
			smartUpdate("clickable", _clickable);
		}
	}

	/** Returns whether can edit the Gcircle by mouse; default to false.
	 * @return whether can edit the Gcircle by mouse; default to false.
	 */
	public boolean isEditable () {
		return _editable;
	}
	/** Sets whether can edit the Gcircle by mouse; default to false.
	 * @param editable whether enable edit the Gcircle by mouse.
	 */
	public void setEditable (boolean editable) {
		if (_editable != editable) {
			_editable = editable;
			smartUpdate("editable", _editable);
		}
	}

	/** Returns the fill color of this Gcircle.
	 * @return String the fill color of this Gcircle.
	 */
	public String getFillColor () {
		return _fillColor;
	}
	/** Sets the fill color of this Gcircle.
	 * @param fillColor the fill color of this Gcircle.
	 */
	public void setFillColor (String fillColor) {
		if (!Objects.equals(_fillColor, fillColor)) {
			_fillColor = fillColor;
			smartUpdate("fillColor", _fillColor);
		}
	}

	/** Returns the opacity of the fill color of this Gcircle.
	 * @return int the opacity of the fill color of this Gcircle.
	 */
	public int getFillOpacity() {
		return _fillOpacity;
	}
	/** Sets the opacity of the fill color of this Gcircle.
	 * @param fillOpacity the opacity of the fill color of this Gcircle,
	 * should between 0 and 100.
	 */
	public void setFillOpacity(int fillOpacity) {
		if (fillOpacity < 0 || fillOpacity > 100) {
			throw new UiException("Fill opacity must between 0 to 100 (inclusive): "+fillOpacity);
		}
		if (_fillOpacity != fillOpacity) {
			_fillOpacity = fillOpacity;
			smartUpdate("fillOpacity", _fillOpacity);
		}
	}

	/** Returns the radius of this Gcircle.
	 * @return double the radius of this Gcircle.
	 */
	public double getRadius () {
		return _radius;
	}
	/** Sets the radius of this Gcircle; default to 200.
	 * @param radius the radius of this Gcircle.
	 */
	public void setRadius (double radius) {
		if (radius < 0) {
			throw new UiException("radius must greater then or equal to 0: "+radius);
		}
		if (_radius != radius) {
			_radius = radius;
			smartUpdate("radius", _radius);
		}
	}

	/** Returns the stroke color of this Gcircle.
	 * @return String the stroke color of this Gcircle.
	 */
	public String getStrokeColor () {
		return _strokeColor;
	}
	/** Sets the stroke color of this Gcircle.
	 * @param strokeColor the stroke color of this Gcircle.
	 */
	public void setStrokeColor (String strokeColor) {
		if (!Objects.equals(_strokeColor, strokeColor)) {
			_strokeColor = strokeColor;
			smartUpdate("strokeColor", _strokeColor);
		}
	}

	/** Returns the stroke opacity of this Gcircle.
	 * @return int the stroke opacity of this Gcircle.
	 */
	public int getStrokeOpacity() {
		return _strokeOpacity;
	}
	/** Sets the stroke opacity of this Gcircle.
	 * @param strokeOpacity the stroke opacity of this Gcircle,
	 * should between 0 and 100.
	 */
	public void setStrokeOpacity(int strokeOpacity) {
		if (strokeOpacity < 0 || strokeOpacity > 100) {
			throw new UiException("Stroke opacity must between 0 to 100 (inclusive): "+strokeOpacity);
		}
		if (_strokeOpacity != strokeOpacity) {
			_strokeOpacity = strokeOpacity;
			smartUpdate("strokeOpacity", _strokeOpacity);
		}
	}

	/** Returns the stroke weight of this Gcircle.
	 * @return int the stroke weight of this Gcircle.
	 */
	public int getStrokeWeight() {
		return _strokeWeight;
	}
	/** Sets the stroke weight of this Gcircle.
	 * @param strokeWeight the stroke weight of this Gcircle.
	 */
	public void setStrokeWeight(int strokeWeight) {
		if (strokeWeight < 0) {
			throw new UiException("strokeWeight must greater then or equal to 0: "+strokeWeight);
		}
		if (_strokeWeight != strokeWeight) {
			_strokeWeight = strokeWeight;
			smartUpdate("strokeWeight", _strokeWeight);
		}
	}

	/** Returns whether this Gcircle is visible.
	 * @return boolean whether this Gcircle is visible.
	 */
	public boolean isCircleVisible () {
		return _circleVisible;
	}
	/** Sets whether this Gcircle is visible; default to true.
	 * @param circleVisible whether this Gcircle is visible.
	 */
	public void setCircleVisible (boolean circleVisible) {
		if (_circleVisible != circleVisible) {
			_circleVisible = circleVisible;
			smartUpdate("circleVisible", _circleVisible);
		}
	}

	/** Returns the zIndex of this Gcircle.
	 * @return int the zIndex of this Gcircle.
	 */
	public int getCircleZIndex() {
		return _circleZIndex;
	}
	/** Sets the zIndex of this Gcircle.
	 * @param zIndex the zIndex of this Gcircle.
	 */
	public void setCircleZIndex(int circleZIndex) {
		if (circleZIndex < 0) {
			throw new UiException("circleZIndex must greater then or equal to 0: "+circleZIndex);
		}
		if (_circleZIndex != circleZIndex) {
			_circleZIndex = circleZIndex;
			smartUpdate("circleZIndex", _circleZIndex);
		}
	}

	//super
	public boolean isChildable() {
		return false;
	}
	
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		renderer.render("center",  GmapsUtil.latLngToArray(getCenter()));
		if (!_clickable)
			renderer.render("clickable", _clickable);
		render(renderer, "editable", _editable);
		render(renderer, "fillColor", _fillColor);
		if (_fillOpacity > -1)
			renderer.render("fillOpacity", _fillOpacity);
		if (_radius > -1)
			renderer.render("radius", _radius);
		render(renderer, "strokeColor", _strokeColor);
		if (_strokeOpacity > -1)
			renderer.render("strokeOpacity", _strokeOpacity);
		if (_radius != 200)
			renderer.render("strokeWeight", _strokeWeight);
		if (!_circleVisible)
			renderer.render("circleVisible", _circleVisible);
		if (_circleZIndex > -1)
			renderer.render("circleZIndex", _circleZIndex);
	}
	/** Processes an AU request.
	 *
	 * <p>Default: in addition to what are handled by {@link XulElement#service},
	 * it also handles onSelect.
	 * @since 5.0.0
	 */
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals("onCenterChange")) {
			final CenterChangeEvent evt = CenterChangeEvent.getCenterChangeEvent(request);
			_center = evt.getCenter();
			Events.postEvent(evt);
		} else if (cmd.equals("onRadiusChange")) {
			final Map data = request.getData();
			_radius = ((Number)data.get("radius")).doubleValue();
			Events.postEvent(new Event("onRadiusChange", this, data));
		} else
			super.service(request, everError);
	}
	//register the Gcircle related event
	static {
		addClientEvent(Gcircle.class, "onCenterChange", CE_DUPLICATE_IGNORE);
		addClientEvent(Gcircle.class, "onRadiusChange", CE_DUPLICATE_IGNORE);
	}
}
