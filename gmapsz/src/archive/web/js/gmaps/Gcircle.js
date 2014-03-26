/* Gcircle.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Mar 02 10:20:11     2012, Created by benbai
		
	Used with ZK 5.0 and later
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
/**
 * Circle that can be overlay on the Google Maps.
 * @since 3.0.0
 */
gmaps.Gcircle = zk.$extends(gmaps.Goverlay, {
	_clickable: true,
	_radius: 200,
	_circleVisible: true,
	$define: {
		/** Returns the Circle's center in double[] array where [0] is lat and [1] is lng;
		 */
		/** Sets the Circle's center in double[] array where [0] is lat and [1] is lng;
		 */
		center: function (c) {
			var circle = this.mapitem_;
			if (circle) {
				// not send event if not changed by drag
				this._centerChangeByServer = true;
				circle.setCenter(new google.maps.LatLng(c[0],c[1]));
				delete this._centerChangeByServer;
			}
		},
		/** Returns whether this Gcircle is clickable.
		 * @return whether this Gcircle is clickable.
		 */
		/** Sets whether this Gcircle is clickable; default to true.
		 * @param b whether enable click the Gcircle.
		 */
		clickable: function (b) {
			this._updateCircle ('clickable', b);
		},
		/** Returns whether can edit the Gcircle by mouse; default to false.
		 * @return whether can edit the Gcircle by mouse; default to false.
		 */
		/** Sets whether can edit the Gcircle by mouse; default to false.
		 * @param b whether enable edit the Gcircle by mouse.
		 */
		editable: function (b) {
			var circle = this.mapitem_;
			if (circle)
				circle.setEditable(b);
		},
		/** Returns the fill color of this Gcircle.
		 * @return String the fill color of this Gcircle.
		 */
		/** Sets the fill color of this Gcircle.
		 * @param v the fill color of this Gcircle.
		 */
		fillColor: function (v) {
			this._updateCircle ('fillColor', v);
		},
		/** Returns the opacity of the fill color of this Gcircle.
		 * @return int the opacity of the fill color of this Gcircle.
		 */
		/** Sets the opacity of the fill color of this Gcircle.
		 * @param v the opacity of the fill color of this Gcircle,
		 * should between 0 and 100.
		 */
		fillOpacity: function (v) {
			this._updateCircle ('fillOpacity', v/100);
		},
		/** Returns the radius of this Gcircle.
		 * @return double the radius of this Gcircle.
		 */
		/** Sets the radius of this Gcircle; default to 200.
		 * @param v the radius of this Gcircle.
		 */
		radius: function (v) {
			var circle = this.mapitem_;
			if (circle) {
				// not send event if not changed by drag
				this._radiusChangeByServer = true;
				circle.setRadius(v);
				delete this._radiusChangeByServer;
			}
		},
		/** Returns the stroke color of this Gcircle.
		 * @return String the stroke color of this Gcircle.
		 */
		/** Sets the stroke color of this Gcircle.
		 * @param v the stroke color of this Gcircle.
		 */
		strokeColor: function (v) {
			this._updateCircle ('strokeColor', v);
		},
		/** Returns the stroke opacity of this Gcircle.
		 * @return int the stroke opacity of this Gcircle.
		 */
		/** Sets the stroke opacity of this Gcircle.
		 * @param strokeOpacity the stroke opacity of this Gcircle,
		 * should between 0 and 100.
		 */
		strokeOpacity: function (v) {
			this._updateCircle ('strokeOpacity', v/100);
		},
		/** Returns the stroke weight of this Gcircle.
		 * @return int the stroke weight of this Gcircle.
		 */
		/** Sets the stroke weight of this Gcircle.
		 * @param v the stroke weight of this Gcircle.
		 */
		strokeWeight: function (v) {
			this._updateCircle ('strokeWeight', v);
		},
		/** Returns whether this Gcircle is visible.
		 * @return boolean whether this Gcircle is visible.
		 */
		/** Sets whether this Gcircle is visible; default to true.
		 * @param b whether this Gcircle is visible.
		 */
		circleVisible: function (b) {
			var circle = this.mapitem_;
			if (circle)
				circle.setVisible(b);
		},
		/** Returns the zIndex of this Gcircle.
		 * @return int the zIndex of this Gcircle.
		 */
		/** Sets the zIndex of this Gcircle.
		 * @param v the zIndex of this Gcircle.
		 */
		circleZIndex: function (v) {
			this._updateCircle ('zIndex', v);
		}
	},
	initMapitem_: function() {
		var	_center = this._center,
			latlng = new google.maps.LatLng(_center[0], _center[1]),
			gcircle = new google.maps.Circle(this.getCircleOptions());

		gcircle.setRadius(this._radius);
		gcircle.setCenter(latlng);
		gcircle.setVisible(this._circleVisible);
		gcircle.setEditable(this._editable);
		gcircle._wgt = this;
		this.mapitem_ = gcircle;
	},
	getCircleOptions: function (prop, val) {
		var circleOption = this._circleOption;
			
		if (!circleOption) {
			var circleZIndex = this._circleZIndex,
				fillColor = this._fillColor,
				fillOpacity = this._fillOpacity,
				strokeColor = this._strokeColor,
				strokeOpacity = this._strokeOpacity,
				strokeWeight = this._strokeWeight;
			this._circleOption =
				{clickable: this._clickable};
			circleOption = this._circleOption;
			if (circleZIndex)
				circleOption['zIndex'] = circleZIndex;
			if (fillColor)
				circleOption['fillColor'] = fillColor;
			if (fillOpacity)
				circleOption['fillOpacity'] = fillOpacity/100;
			if (strokeColor)
				circleOption['strokeColor'] = strokeColor;
			if (strokeOpacity)
				circleOption['strokeOpacity'] = strokeOpacity/100;
			if (strokeWeight)
				circleOption['strokeWeight'] = strokeWeight;
		}

		// override one property, used by setters
		if (prop)
			circleOption[prop] = val;
		return circleOption;
	},
	_updateCircle: function (prop, val) {
		var circle = this.mapitem_;
		if (circle)
			circle.setOptions(this.getCircleOptions(prop, val));
	},
	_initListeners: function() {
		var circle = this.mapitem_,
			wgt = this,
			gevt = google.maps.event;

		this._centerChanged = gevt.addListener(circle, "center_changed",
				function() {if (!wgt._centerChangeByServer) wgt._doCenterChanged(circle.getCenter());});
		this._radiusChanged = gevt.addListener(circle, "radius_changed",
				function() {if (!wgt._radiusChangeByServer) wgt._doRadiusChanged(circle.getRadius());});
		this._lClick = gevt.addListener(circle, "click",
				function(evt) {wgt._doClick(evt);});
		this._dblClick = gevt.addListener(circle, "dblclick",
				function(evt) {wgt._doDoubleClick(evt);});
		this._rClick = gevt.addListener(circle, "rightclick",
				function(evt) {wgt._doRightClick(evt);});
	},
	_doCenterChanged: function (center) {
		var oldCenter = this._center,
			nlat = center.lat(),
			nlng = center.lng(),
			data = {lat:nlat, lng:nlng, oldlat: oldCenter[0], oldlng: oldCenter[1]};
		// update center after data copied
		oldCenter[0] = nlat;
		oldCenter[1] = nlat;
		this.fire('onCenterChange', data, {});
	},
	_doRadiusChanged: function (radius) {
		var nradius = radius,
			data = {radius: nradius, oldRadius: this._radius};

		// update radius after data copied
		this._radius = radius;
		this.fire('onRadiusChange', data, {});
	},
	_doClick: function (evt) {
		this._fireClickEvent(evt.latLng, 'onClick');
		this.doClick_(new zk.Event(this, 'onClick', {latLng: evt.latLng}))
	},
	_doDoubleClick: function (evt) {
		this._fireClickEvent(evt.latLng, 'onDoubleClick');
	},
	_doRightClick: function (evt) {
		this._fireClickEvent(evt.latLng, 'onRightClick');
	},
	_fireClickEvent: function (latlng, evtnm) {
		var parent = this.parent,
			xy = gmaps.Gmaps.latlngToXY(parent, latlng),
			pageXY = gmaps.Gmaps.xyToPageXY(parent, xy.x, xy.y),
			data = {x:xy.x,y:xy.y,pageX:pageXY[0],pageY:pageXY[1]};
	
		this.fire(evtnm, data, {});
	},
	_clearListeners: function () {
		var gevt = google.maps.event;
		if (this._centerChanged) {
			gevt.removeListener(this._centerChanged);
			this._centerChanged = null;
		}
		if (this._radiusChanged) {
			gevt.removeListener(this._radiusChanged);
			this._radiusChanged = null;
		}
		if (this._lClick) {
			gevt.removeListener(this._lClick);
			this._lClick = null;
		}
		if (this._dblClick) {
			gevt.removeListener(this._dblClick);
			this._dblClick = null;
		}
		if (this._rClick) {
			gevt.removeListener(this._rClick);
			this._rClick = null;
		}
	}
});