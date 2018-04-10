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
	This program is distributed under GPL Version 2.0 in the hope that
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
		 * Returns the bounded of this Gimage.
		 * @return the bounded of this Gimage.
		 * @since 3.0.2
		 */
		/**
		 * Sets the bounded of this Gimage.
		 * @param b the bounded of this Gimage.
		 * @since 3.0.2
		 */
		bounds: function(b) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the bounded south west latitude.
		 * @return double the bounded south west latitude.
		 * @deprecated As of release 3.0.2, replaced with {@link Gimage#getBounds()} instead.
		 */
		/**
		 * Sets the bounded south west latitude.
		 * @param double f the bounded south west latitude.
		 * @deprecated As of release 3.0.2, replaced with {@link Gimage#setBounds()} instead.
		 */
		swlat: function(f) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the bounded south west longitude.
		 * @return double the bounded south west longitude.
		 * @deprecated As of release 3.0.2, replaced with {@link Gimage#getBounds()} instead.
		 */ 
		/**
		 * Sets the bounded south west longitude.
		 * @param double f the bounded south west longitude.
		 * @deprecated As of release 3.0.2, replaced with {@link Gimage#setBounds()} instead.
		 */
		swlng: function(f) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the bounded north east latitude.
		 * @return double the bounded north east latitude.
		 * @deprecated As of release 3.0.2, replaced with {@link Gimage#getBounds()} instead.
		 */
		/**
		 * Sets the bounded north east latitude.
		 * @param double f the bounded north east latitude.
		 * @deprecated As of release 3.0.2, replaced with {@link Gimage#setBounds()} instead.
		 */
		nelat: function(f) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the bounded north east longitude.
		 * @return double the bounded north east longitude.
		 * @deprecated As of release 3.0.2, replaced with {@link Gimage#getBounds()} instead.
		 */
		/**
		 * Sets the bounded north east longitude.
		 * @param double f the bounded north east longitude.
		 * @deprecated As of release 3.0.2, replaced with {@link Gimage#setBounds()} instead.
		 */
		nelng: function(f) {
			this.rebindMapitem_();
		}
	},
	initMapitem_: function() {
		var b = this._bounds,
			bounds = b != null ? new google.maps.LatLngBounds(
					new google.maps.LatLng(b.southWest.latitude, b.southWest.longitude),
					new google.maps.LatLng(b.northEast.latitude, b.northEast.longitude)) : null,
					gground = new google.maps.GroundOverlay(this._src, bounds);
		gground._wgt = this;
		this.mapitem_ = gground;
	},
	setRerender_: function(info) {
		
		this._src = info.src;
		this._bounds = info.bounds;
		
		this.rebindMapitem_();
	}
});