/* Ginfo.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Aug 04 11:50:41     2009, Created by henrichen
		
	Used with ZK 5.0 and later
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
/**
 * Info window that can be opened on Google Maps.
 */
gmaps.Ginfo = zk.$extends(gmaps.Goverlay, {
	$define: {
		/** Returns the anchor point of the info window.
		 * @return double[] anchor[0] is latitude; anchor[1] is longitude.
		 */
		/** set the anchor point of the info window.
		 * @param double[] anchor anchor[0] is latitude; anchor[1] is longitude.
		 */
		anchor: function(c) {
			if (this.parent)
				this.parent._changeInfoPosition(c, this);
		},
		/**
		 * Returns the info window content(text or HTML).
		 * @return String
		 */
		/**
		 * Sets the info window content(text or HTML).
		 * @param String s the info window content(text or HTML).
		 */
		content: function(s) {
			if (this.parent)
				this.parent._changeInfoContent(s, this);
		},
		/**
		 * Returns whether this info window is currently opened.
		 * @return boolean
		 */
		/**
		 * Sets whether this info window is opened.
		 * @param boolean b whether this info window is opened.
		 */
		open: function(b) {
			if (this.parent) {
				if (b) this.parent.openInfo(this);
				else this.parent.closeInfo(this);
			}
		}
	},
	clearOpen_: function() {
		this._open = false;
	},
	removeHTML_: function (n) {
		if (this._open && this._infowindow)
			this._infowindow.close();
	},
	bindMapitem_: function() {
		if (this.parent && this._open)
			this.parent.openInfo(this);
	},
	unbindMapitem_: function() {
		if (this.parent && this == this.parent._curInfo)
			this.parent.closeInfo();
	}
});