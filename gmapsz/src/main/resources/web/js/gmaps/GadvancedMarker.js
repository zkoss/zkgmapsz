/* Gmarker.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Aug 04 17:08:50     2009, Created by henrichen
		
	Used with ZK 5.0 and later
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
/**
 * The gmarker that can be overlay on the Google Maps.
 */
gmaps.Gmarker = zk.$extends(gmaps.Gmarker, {
	initMapitem_: function() {
		var gmaps = this.parent,
			iimg = this._iconImage,
			isdw = this._iconShadow,
			isz = this._iconSize,
			isdwsz = this._iconShadowSize,
			ianch = this._iconAnchor,
			anch = this._anchor,
			tooltiptext = this._tooltiptext,
			visible = this._visible,
			opts = {};
			opts.map = this.gmaps();
		if(anch) opts.position = new google.maps.LatLng(anch.latitude, anch.longitude);
		if (iimg) {
			var markerImage = new google.maps.MarkerImage(iimg,
				null,
				null,
				ianch? new google.maps.Point(ianch[0], ianch[1]) : null,
				isz? new google.maps.Size(isz[0], isz[1]) : null
			);
			opts.icon = markerImage;
		}
		if (isdw) {
			var markerImage = new google.maps.MarkerImage(isdw,
				null,
				null,
				ianch? new google.maps.Point(ianch[0], ianch[1]) : null,
				isdwsz? new google.maps.Size(isdwsz[0], isdwsz[1]) : null
			);
			opts.shadow = markerImage;
		}
		if (tooltiptext) {
			opts.title = tooltiptext;
		}
		//opts.visible = visible;
		gmarker = new google.maps.marker.AdvancedMarkerElement(opts);
		gmarker._wgt = this;
		this.mapitem_ = gmarker;
		this._initDraggable();  

	},
	_initDraggable: function() {
		if (this.mapitem_) {
			this.mapitem_.draggable = this._draggingEnabled;
			// if draggingEnabled then set gmarker can be dragged in map,
			// and cleanDrag
			// else if draggable then initDrag
			if (this._draggingEnabled)
				this.cleanDrag_();
			else if (this._draggable && this._draggable != 'false') //drag inside maps
				this.initDrag_();
			else
				this.cleanDrag_();
		}
	}
});