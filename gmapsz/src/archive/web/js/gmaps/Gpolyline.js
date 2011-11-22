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
gmaps.Gpolyline = zk.$extends(gmaps.Goverlay, {
	$define: {
		/**
		 * Returns the encoded points and levels for this Gpolyline.
		 * @return String[] info[0] is the encoded points; info[1] is the encoded visible level
		 */
		/**
		 * Sets the encoded points and levels for this Gpolyline.
		 * @param String[] info info[0] is the encoded points; info[1] is the encoded visible level
		 */
		pointsAndLevels: function(sa) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the number of polyline levels(0~19) and zoomFactor change per the numLevels.
		 * @return int[] numbers[0] is the number of levels; numbers[1] is zoomFactor change per the numLevels.
		 */
		/**
		 * Sets the number of polyline levels(0~19) and zoomFactor change per the numLevels.
		 * @param int[] numbers[] numbers[0] is the number of levels; numbers[1] is zoomFactor change per the numLevels.
		 */
		numLevelsAndZoomFactor: function(ia) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the line color in form of #RRGGBB, default to #808080.
		 * @return String color the color to set
		 */
		/**
		 * Sets the line color in form of #RRGGBB, default to #808080.
		 * @param String color the color to set
		 */
		color: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the line weight(width) 1 - 10, default to 5.
		 * @return int the line weight 
		 */
		/**
		 * Sets the line weight(width) 1 - 10, default to 5.
		 * @param int weight the line weight
		 */
		weight: function(i) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the line opacity 0 - 100, default to 50.
		 * @return int the line opacity
		 */
		/**
		 * Sets the line opacity 0 - 100, default to 50.
		 * @param int opacity the line opacity
		 */
		opacity: function(f) {
			this.rebindMapitem_();
		}
	},
	initMapitem_: function() {
		var decodedPath = google.maps.geometry.encoding.decodePath(this._pointsAndLevels[0]);

		var polyOptions = {
			strokeColor: this._color,
			strokeOpacity: this._opacity,
			strokeWeight: this._weight
		},
		gpolyline = new google.maps.Polyline(polyOptions);
		var path = gpolyline.setPath(decodedPath),
			wgt = this;

		gpolyline._wgt = this;
		this.mapitem_ = gpolyline;
	},
	prepareRerender_: function(info) {
		this._pointsAndLevels = info.pointsAndLevels;
		this._numLevelsAndZoomFactor = info.numLevelsAndZoomFactor;
		this._color = info.color;
		this._weight = info.weight;
		this._opacity = info.opacity;
	},
	setRerender_: function(info) {
		this.prepareRerender_(info);
		this.rebindMapitem_();
	}
});