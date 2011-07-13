/* Gimage.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Aug 18 11:40:36     2009, Created by henrichen
		
	Used with ZK 5.0 and later
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 3.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
/**
 * Image that can be overlay on Google Maps.
 */
gmaps.Gimage = zk.$extends(gmaps.Goverlay, {
	$define: {
		/**
		 * Returns the source url associated with this Gimage.
		 * @return String
		 */
		/**
		 * Sets the source url associated with this Gimage.
		 * @param String s the source url associated with this Gimage.
		 */
		src: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the bounded south west latitude.
		 * @return double the bounded south west latitude.
		 */
		/**
		 * Sets the bounded south west latitude.
		 * @param double f the bounded south west latitude.
		 */
		swlat: function(f) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the bounded south west longitude.
		 * @return double the bounded south west longitude.
		 */ 
		/**
		 * Sets the bounded south west longitude.
		 * @param double f the bounded south west longitude.
		 */
		swlng: function(f) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the bounded north east latitude.
		 * @return double the bounded north east latitude.
		 */
		/**
		 * Sets the bounded north east latitude.
		 * @param double f the bounded north east latitude.
		 */
		nelat: function(f) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the bounded north east longitude.
		 * @return double the bounded north east longitude.
		 */
		/**
		 * Sets the bounded north east longitude.
		 * @param double f the bounded north east longitude.
		 */
		nelng: function(f) {
			this.rebindMapitem_();
		}
	},
	initMapitem_: function() {
		var sw = new google.maps.LatLng(this._swlat, this._swlng),
			ne = new google.maps.LatLng(this._nelat, this._nelng),
			bound = new google.maps.LatLngBounds(sw, ne),
			gground = new google.maps.GroundOverlay(this._src, bound);
		gground._wgt = this;
		this.mapitem_ = gground;
	},
	setRerender_: function(info) {
		
		this._src = info.src;
		this._swlat = info.swlat;
		this._swlng = info.swlng;
		this._nelat = info.nelat;
		this._nelng = info.nelng;
		
		this.rebindMapitem_();
	}
});