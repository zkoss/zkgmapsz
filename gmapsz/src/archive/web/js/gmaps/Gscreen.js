/* Gscreen.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Aug 18 11:21:14     2009, Created by henrichen
		
	Used with ZK 5.0 and later
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
/**
 * Screen image which will not move with the maps that can be overlay on the Google Maps.
 */
gmaps.Gscreen = zk.$extends(gmaps.Goverlay, {
	$define: {
		src: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the X coordinate (in px or percentage of the maps size,
		 * left-bottom corner as the origin) of the screen the image is going 
		 * to be placed.
		 * @return String the X coordinate in px or percentage
		 */
		/**
		 * Sets the X coordinate (in px or percentage of the maps size,
		 * left-bottom corner as the origin) of the screen the image is going 
		 * to be placed.
		 * @param String screenX the X coordinate in px or percentage
		 */
		screenX: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the Y coordinate (in px or percentage of the maps size,
		 * left-bottom corner as the origin) of the screen the image is going 
		 * to be placed.
		 * @return String the Y coordinate in px or percentage
		 */
		/**
		 * Sets the Y coordinate (in px or percentage of the maps size,
		 * left-bottom corner as the origin) of the screen the image is going 
		 * to be placed.
		 * @param String screenY the Y coordinate in px or percentage
		 */
		screenY: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the X offset (in px or percentage of the maps size) to 
		 * move left the image.
		 * 
		 * @return String the X offset (in px or percentage of the maps size) to 
		 * move left the image.
		 */
		/**
		 * Sets the X offset (in px or percentage of the maps size) to 
		 * move left the image.
		 * 
		 * @param String offsetX the X offset (in px or percentage of the maps size) to 
		 * move left the image.
		 */
		offsetX: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the Y offset (in px or percentage of the maps size) to 
		 * move left the image.
		 * 
		 * @return String the Y offset (in px or percentage of the maps size) to 
		 * move left the image.
		 */
		/**
		 * Sets the Y offset (in px or percentage of the maps size) to 
		 * move left the image.
		 * 
		 * @param String offsetY the Y offset (in px or percentage of the maps size) to 
		 * move left the image.
		 */
		offsetY: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the image width in px or percentage of the maps size.
		 *
		 * @return String the width (in px or percentage of the maps size) of 
		 * the image.
		 */
		/**
		 * Set the image width in px or percentage of the maps size.
		 *
		 * @param String width the width (in px or percentage of the maps size) of 
		 * the image.
		 */
		width: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the image height in px or percentage of the maps size.
		 * @return String the height (in px or percentage of the maps size) of 
		 * the image.
		 */
		/**
		 * Sets the image height in px or percentage of the maps size.
		 * @param String height the height (in px or percentage of the maps size) of 
		 * the image.
		 */
		height: function(s) {
			this.rebindMapitem_();
		}
	},
	initMapitem_: function() {
		var sxy = this._topoint(this._screenX, this._screenY),
			oxy = this._topoint(this._offsetX, this._offsetY),
			sz = this._tosize(this._width, this._height),
			gscreen = new GScreenOverlay(this._src, sxy, oxy, sz);
		
		gscreen._wgt = this;
		this.mapitem_ = gscreen;
	},
	//private
	_tospoint: function(x) {
		var ix, xu;
		if (!x.endsWith("%")) {
			if (x.endsWith("px"))
				x = x.substring(0, x.length() - 2);
			ix = parseInt(x);
			xu = "pixels";
		} else {
			ix = parseFloat(x) / 100;
			xu = "fraction";
		}
		return [ix, xu];
	},
	_topoint: function(x, y) {
		var xp = this._tospoint(x),
			yp = this._tospoint(y);
		return new GScreenPoint(xp[0], yp[0], xp[1], yp[1]);
	},
	_tosize: function(w, h) {
		var wp = this._tospoint(w),
			hp = this._tospoint(h);
		return new GScreenSize(wp[0], hp[0], wp[1], hp[1]);
	},
	setRerender_: function(info) {
		this._src = info.src;
		this._screenX = info.screenX;
		this._screenY = info.screenY;
		this._offsetX = info.offsetX;
		this._offsetY = info.offsetY;
		this._width = info.width;
		this._height = info.height;
		
		this.rebindMapitem_();
	}
});