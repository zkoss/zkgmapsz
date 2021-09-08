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
	This program is distributed under GPL Version 3.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
/**
 * Polyline that can be overlay on the Google Maps.
 */
gmaps.Gpolyline = zk.$extends(gmaps.Goverlay, {
	_pathEncoded: true,
	$define: {
		/**
		 * Returns the encoded path for this Gpolyline.
		 * @return String path is the encoded points.
		 */
		/**
		 * Sets the encoded path for this Gpolyline.
		 * @param String p is the encoded points.
		 */
		path: function(p) {
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
		},
		/** Returns whether can edit the Polyline by mouse; default to false.
		 * @return whether can edit the Polyline by mouse; default to false.
		 */
		/** Sets whether can edit the Polyline by mouse; default to false.
		 * @param b whether enable edit the Polyline by mouse.
		 */
		editable: function (b) {
			var polyline = this.mapitem_;
			if (this.mapitem_) {
				polyline.setEditable(b);
			}
		},
		visible: function(b) {
			var polyline = this.mapitem_;
			if (this.mapitem_) {
				polyline.setVisible(b);
			}
		},
		pathEncoded: function() {}
	},
	initMapitem_: function() {
		var decodedPath = this._path;
		if(this._pathEncoded) {
			decodedPath = google.maps.geometry.encoding.decodePath(decodedPath);
		}

		var polyOptions = {
			editable: this._editable,
			strokeColor: this._color,
			strokeOpacity: this._opacity,
			strokeWeight: this._weight,
			visible: this._visible
		},
		gpolyline = new google.maps.Polyline(polyOptions);
		var path = gpolyline.setPath(decodedPath),
			wgt = this;

		gpolyline._wgt = this;
		this.mapitem_ = gpolyline;
	},
	_initListeners: function() {
		var gpolyline = this.mapitem_;
		if (gpolyline) {
			var wgt = this,
				path = gpolyline.getPath();
	        this._insertAt = google.maps.event.addListener(path, "insert_at", _zkf = function() {wgt._updatePath();});
	        this._removeAt = google.maps.event.addListener(path, "remove_at", _zkf);
	        this._setAt = google.maps.event.addListener(path, "set_at", _zkf);
		}
	},
	_clearListeners: function() {
		if (this._insertAt) {
			google.maps.event.removeListener(this._insertAt);
			this._insertAt = null;
		}
		if (this._removeAt) {
			google.maps.event.removeListener(this._removeAt);
			this._removeAt = null;
		}
		if (this._setAt) {
			google.maps.event.removeListener(this._setAt);
			this._setAt = null;
		}
	},
	_updatePath: function() {
		var gpolyline = this.mapitem_;
		if (gpolyline) {
			if(this._pathEncoded) {
				var encodedPath = google.maps.geometry.encoding.encodePath(gpolyline.getPath());
				this._path = encodedPath;
			} else {
				this._path = gpolyline.getPath().getArray().map(function (latlng) {
					return latlng.toJSON();
				});
			}
			this.fireOnPathChange();
		}
	},
	/** Fires the onChange event.
	 * If the widget is created at the server, the event will be sent
	 * to the server too.
	 * @param Map opts [optional] the options. Refer to {@link zk.Event#opts}
	 * @since 3.0.2
	 */
	fireOnPathChange: function (opts) {
		this.fire('onPathChange', {path: this._path}, opts);
	},
	prepareRerender_: function(info) {
		this._path = info.path;
		this._numLevelsAndZoomFactor = info.numLevelsAndZoomFactor;
		this._color = info.color;
		this._weight = info.weight;
		this._opacity = info.opacity;
		this._editable = info.editable;
	},
	setRerender_: function(info) {
		this.prepareRerender_(info);
		this.rebindMapitem_();
	}
});