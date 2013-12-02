/* Gpolygon.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Mon Aug 17 18:30:42     2009, Created by henrichen
		
	Used with ZK 5.0 and later
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
/**
 * Polygon that can be overlay on the Google Maps.
 */
gmaps.Gpolygon = zk.$extends(gmaps.Gpolyline, {
	$define: {
		/**
		 * Returns fill color in form of #RRGGBB, default to #808080.
		 * @return String color the fill color
		 */
		/**
		 * Sets fill color in form of #RRGGBB, default to #808080.
		 * @param String color the fill color
		 */
		fillColor: function(s) {
			this.rebindMapitem_();
		},
		/** Returns true to draw the outline of this polygon with provided color {@link #getColor}.
		 * @return boolean true to draw the outline of this polygon
		 */
		/** Sets true to draw the outline of this polygon with provided color {@link #getColor}.
		 * @param boolean b true to draw the outline of this polygon
		 */
		outline: function(b) {
			this.rebindMapitem_();
		},
		/** Returns true to fill the interier of this polygon with the provided
		 * fill color {@link #getFillColor}.
		 * @return boolean true to fill the interier of this polygon
		 */
		/** Sets true to fill the interier of this polygon with the provided
		 * fill color {@link #getFillColor}.
		 * @param boolean b true to fill the interier of this polygon
		 */
		fill: function(b) {
			this.rebindMapitem_();
		},
		/**
		 * Returns fill opacity from 0 (transparent) to 100 (solid), default to 50.
		 * @return int the fill opacity level 
		 */
		/**
		 * Sets fill opacity from 0 (transparent) to 100 (solid), default to 50.
		 * @param int op the fill opacity level 
		 */
		fillOpacity: function(f) {
			this.rebindMapitem_();
		}
	},
	initMapitem_: function() {
		var decodedPath = google.maps.geometry.encoding.decodePath(this._pointsAndLevels[0]),
			opt = {
					editable: this._editable,
					paths: decodedPath,
					strokeColor: this._color,
					strokeOpacity: this._outline? this._opacity : 0,
					strokeWeight: this._weight,
					fillColor: this._fillColor,
					fillOpacity: this._fill? this._fillOpacity : 0,
					visible: this._visible
				},
			gpolygon = new google.maps.Polygon(opt);

		var wgt = this;
		gpolygon._wgt = this;
		this.mapitem_ = gpolygon;
	},
	setRerender_: function(info) {
		this.$supers(gmaps.Gpolygon, 'prepareRerender_', arguments);
		this._editable = info.editable;
		this._fillColor = info.fillColor;
		this._outline = info.outline;
		this._fill = info.fill;
		this._fillOpacity = info.fillOpacity;
		
		this.rebindMapitem_();
	}
});
