/* Gpolyline.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Aug 05 15:20:11     2009, Created by henrichen
		
	Used with ZK 5.0 and later
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
/**
 * Polyline that can be overlay on the Google Maps.
 */
gmaps.Gcircle = zk.$extends(gmaps.Goverlay, {
	_clickable: true,
	_fillOpacity: 30,
	$define: {
		center: function (c) {
			var circle = this.mapitem_;
			if (circle)
				circle.setCenter(new google.maps.LatLng(c[0],c[1]));
		},
		clickable: function (b) {
			this._updateCircle ('clickable', b);
		},
		editable: function (b) {
			this._updateCircle ('editable', b);
		},
		fillColor: function (v) {
			this._updateCircle ('fillColor', v);
		},
		fillOpacity: function (v) {
			this._updateCircle ('fillOpacity', v/100);
		}
	},
	initMapitem_: function() {
		var	gcircle = new google.maps.Circle(this.getCircleOptions());

		gcircle._wgt = this;
		this.mapitem_ = gcircle;
	},
	getCircleOptions: function (prop, val) {
		var circleOption;
		if (!this._circleOption) {
			var _center = this._center,
				latlng = new google.maps.LatLng(this.parent.getCenter()[0], this.parent.getCenter()[1]);
			this._circleOption =
				{center: latlng,
					radius: 100,
					editable: this._editable,
					clickable: this._clickable,
					fillColor: this._fillColor,
					fillOpacity: this._fillOpacity/100};
		}
		circleOption = this._circleOption;
		if (prop)
			circleOption[prop] = val;
		return circleOption;
	},
	_updateCircle: function (prop, val) {
		var circle = this.mapitem_;
		if (circle)
			circle.setOptions(this.getCircleOptions(prop, val));
	}
});