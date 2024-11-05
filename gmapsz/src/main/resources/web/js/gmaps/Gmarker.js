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
gmaps.Gmarker = zk.$extends(gmaps.Ginfo, {
	$define: {
		/** Returns the anchor point of the info window.
		 * @return double[] anchor[0] is latitude; anchor[1] is longitude.
		 */
		/** set the anchor point of the info window.
		 * @param double[] anchor anchor[0] is latitude; anchor[1] is longitude.
		 */
		anchor: function(c) {
			if (this.mapitem_) {
				var oldPoint = this.mapitem_.getPosition(),
					newPoint = new google.maps.LatLng(c.latitude, c.longitude);
				this.mapitem_.setPosition(newPoint);
				if (this.parent) {
					//this.parent._mm.onMarkerMoved_(this.mapitem_, oldPoint, newPoint); 
				}
			}
		},
		/**
		 * Returns the foreground image URL of the icon.
		 * @return String the iconImage URL
		 */
		/**
		 * Sets the foreground image URL of the icon. No operation if a null value.
		 * @param String iconImage the iconImage URL to set
		 */
		iconImage: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the shadow image URL of the icon.
		 * @return String the iconShadow image URL
		 */
		/**
		 * Sets the shadow image URL of the icon. No operation if a null value.
		 * @param String iconShadow the iconShadow URL to set
		 */
		iconShadow: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the icon size of this Gmarker.
		 * @return int[] size[0] is width, size[1] is height.
		 */
		/**
		 * Sets the icon size of this Gmarker.
		 * @param int[] size size[0] is width, size[1] is height.
		 */
		iconSize: function(d) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the icon shadow size of this Gmarker.
		 * @return int[] size[0] is width, size[1] is height.
		 */
		/**
		 * Sets the icon shadow size of this Gmarker.
		 * @param int[] size size[0] is width, size[1] is height.
		 */
		iconShadowSize: function(d) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the pixel coordinate relative to the top left corner of the icon 
		 * image at which this icon is anchored to the map. No operation if a value 
		 * less than -100.
		 * @return int[] xy[0] is x pixel coordinate; xy[1] is y pixel coordinate
		 */
		/**
		 * Sets the pixel coordinate relative to the top left corner of the icon 
		 * image at which this icon is anchored to the map. No operation if a value 
		 * less than -100.
		 * @param int[] xy xy[0] is x pixel coordinate; xy[1] is y pixel coordinate
		 */
		iconAnchor: function(c) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the pixel coordinate relative to the top left corner of the icon image 
		 * at which the info window is anchored to this icon. No operation if a value 
		 * less than -100.
		 * @return int[] xy[0] is x pixel coordinate; xy[1] is y pixel coordinate
		 */
		/**
		 * Sets the pixel coordinate relative to the top left corner of the icon image 
		 * at which the info window is anchored to this icon. No operation if a value 
		 * less than -100.
		 * @param int[] xy xy[0] is x pixel coordinate; xy[1] is y pixel coordinate
		 */
		iconInfoAnchor: function(c) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the URL of the foreground icon image used for printed maps. It must be the same size 
		 * as the main icon image given by image. 
		 * No operation if a null value.  
		 * @return String the iconPrintImage URL
		 */
		/**
		 * Sets the URL of the foreground icon image used for printed maps. It must be the same size 
		 * as the main icon image given by image. 
		 * No operation if a null value.  
		 * @param String iconPrintImage the iconPrintImage URL
		 */
		iconPrintImage: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the URL of the foreground icon image used for printed maps in Firefox/Mozilla. 
		 * It must be the same size as the main icon image given by image. 
		 * No operation if a null value. 
		 * @return String the iconMozPrintImage URL
		 */
		/**
		 * Sets the URL of the foreground icon image used for printed maps in Firefox/Mozilla. 
		 * It must be the same size as the main icon image given by image. 
		 * No operation if a null value. 
		 * @param String iconMozPrintImage the iconMozPrintImage URL
		 */
		iconMozPrintImage: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the URL of the shadow image used for printed maps. It should be a 
		 * GIF image since most browsers cannot print PNG images. No operation 
		 * if a null value.
		 * @return String the iconPrintShadow URL
		 */
		/**
		 * Sets the URL of the shadow image used for printed maps. It should be a 
		 * GIF image since most browsers cannot print PNG images. No operation 
		 * if a null value.
		 * @param String iconPrintShadow the iconPrintShadow URL
		 */
		iconPrintShadow: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the URL of a virtually transparent version of the foreground 
		 * icon image used to capture click events in Internet Explorer. This 
		 * image should be a 24-bit PNG version of the main icon image with 1% 
		 * opacity, but the same shape and size as the main icon. No operation 
		 * if a null value.
		 * @return String iconTransparent the iconTransparent URL
		 */
		/**
		 * Sets the URL of a virtually transparent version of the foreground 
		 * icon image used to capture click events in Internet Explorer. This 
		 * image should be a 24-bit PNG version of the main icon image with 1% 
		 * opacity, but the same shape and size as the main icon. No operation 
		 * if a null value.
		 * @param String iconTransparent the iconTransparent URL
		 */
		iconTransparent: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns an comma delimited integers representing the x/y coordinates of the image map we 
		 * should use to specify the click-able part of the icon image in browsers other than 
		 * Internet Explorer. No operation if a null value. 
		 * @return String an comma delimited integers representing the x/y coordinates of the image map we 
		 * should use to specify the click-able part of the icon image
		 */
		/**
		 * Sets an comma delimited integers representing the x/y coordinates of the image map we 
		 * should use to specify the click-able part of the icon image in browsers other than 
		 * Internet Explorer. No operation if a null value. 
		 * @param String iconImageMap an comma delimited integers representing the x/y coordinates of the image map we 
		 * should use to specify the click-able part of the icon image
		 */
		iconImageMap: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the distance pixels in which a marker will visually "rise" vertically when dragged. 
		 * @return int the distance pixels in which a marker will visually "rise" vertically when dragged. 
		 */
		/**
		 * Sets the distance pixels in which a marker will visually "rise" vertically when dragged. 
		 * No operation if a value less than 0.
		 * @param int iconMaxHeight the distance pixels in which a marker will visually "rise" vertically when dragged. 
		 */
		iconMaxHeight: function(i) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the cross image URL when an icon is dragged. No operation if a null value.
		 * @return String the iconDragCrossImage URL
		 */
		/**
		 * Sets the cross image URL when an icon is dragged. No operation if a null value.
		 * @param String iconDragCrossImage the iconDragCrossImage URL
		 */
		iconDragCrossImage: function(s) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the pixel size of the cross image when an icon is dragged. 
		 * No operation if a value less than 0. 
		 * @return int[] size[0] is width; size[1] is height.
		 */
		/**
		 * Sets the pixel size of the cross image when an icon is dragged. 
		 * No operation if a value less than 0. 
		 * @param int[] size size[0] is width; size[1] is height.
		 */
		iconDragCrossSize: function(d) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the pixel coordinate offsets (relative to the iconAnchor) of the 
		 * cross image when an icon is dragged. No operation if a value less than -100.
		 * @param int[] xy[0] is the x coordinate, xy[1] is the y coordinate
		 */
		/**
		 * Sets the pixel coordinate offsets (relative to the iconAnchor) of the 
		 * cross image when an icon is dragged. No operation if a value less than -100.
		 * @param int[] xy xy[0] is the x coordinate, xy[1] is the y coordinate
		 */
		iconDragCrossAnchor: function(c) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the maximum visible zoom level of this Gmarker.
		 * @return int maximum visible zoom level
		 */
		/**
		 * Sets the maximum visible zoom level of this Gmarker.
		 * @param int zoom maximum visible zoom level
		 */
		maxzoom: function(i) {
			this.rebindMapitem_();
		},
		/**
		 * Returns the minimum visible zoom level of this Gmarker (default to 0).
		 * @return int minimum visible zoom level
		 */
		/**
		 * Sets the minmum visible zoom level of this Gmarker  (default to 0).
		 * @param int zoom minimum visible zoom level
		 */
		minzoom: function(i) {
			this.rebindMapitem_();
		},
		/**
		 * Returns whether this Gmarker is enabled to drag "INSIDE" the maps; default is false.
		 * Note that this property takes precedence than {#link #setDraggable}.
		 * If you want to drag Gmarker as other ZK widgets (that is, you can
		 * drag the Gmarker "OUTSIDE" the maps), you have to set this property 
		 * to "false".
		 * @return boolean whether enable dragging this Gmarker inside the maps.
		 */
		/**
		 * Sets whether this Gmarker is enabled to drag "INSIDE" the maps; default is false.
		 * Note that this property takes precedence than {#link #setDraggable}.
		 * If you want to drag Gmarker as other ZK widgets (that is, you can
		 * drag the Gmarker "OUTSIDE" the maps), you have to set this property 
		 * to "false".
		 * @param boolean b whether enable dragging this Gmarker inside the maps.
		 */
		draggingEnabled: function(b) {
			this._initDraggable();
		},
		draggable: function(v) {
			this._initDraggable();
		},
		/** Sets the Tooltiptext of the anchor point.
		 */
		/** Gets the Tooltiptext of the anchor point.
		 */
		tooltiptext: function(t) {
			this.rebindMapitem_();
		},
		visible: function(b) {
			var marker = this.mapitem_;
			if (this.mapitem_) {
				marker.setVisible(b);
			}
		}
	},
	gmarker: function() {
		return this.mapitem_;
	},
	bindMapitem_: function() {
		if (!this.mapitem_) {
			this.initMapitem_();
			
			//binding if exists Gmaps
			if (this.parent) {
				var parent = this.parent,
					maxzoom = this._maxzoom;
				// Issue 28: Limit correspond to Gmaps max zoom level.
				//parent._mm.addMarker(this.mapitem_, this._minzoom, (maxzoom < 0 || maxzoom > parent._maxzoom) ? parent._maxzoom : maxzoom);
				// open at begining, no panTo
				


				if (this._open) parent.openInfo(this);
			}
		}
	},
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
	unbindMapitem_: function() {
		var gmarker = this.mapitem_;
		if (gmarker) {
			var parent = this.parent;
			if (parent) {
				if (this._infowindow) {
					var curZoom = parent.getZoom(),
						maxzoom = this.getMaxzoom();
					// Issue 28: Limit correspond to Gmaps max zoom level.
					if (maxzoom < 0 || maxzoom > parent._maxzoom)
						maxzoom = parent._maxzoom;
					if (curZoom < this.getMinzoom() || curZoom > maxzoom)
						this.parent.closeInfo(this);
				}
			}
			gmarker._wgt = null;
			this.mapitem_ = null;
		}
	},
	getDragNode: function() {
		return this._icon;
	},
	getDragOptions_: function() {
		return {handle: this._area};
	},
	initDrag_: function() { //must !isDraggingEnabled() to initDrag_
		if (!this._draggingEnabled && this.mapitem_ && this._icon) { 
			this.$supers(gmaps.Gmarker, 'initDrag_', arguments);
		}
	},
	doRightClick_: function (evt) {
		//Google Maps API will not bubble up the right-click-on-gmarker
		//domEvent to container(Gmaps#_gmaps) so we add own listener
		//to handle such cases. 
		//@see Gmarker#_initListeners

		//calling this to correct the context submenu not auto closed issue
		zk.Widget.mimicMouseDown_(this);
		
		//when right-click-on-gmarker, evt.data will be undefined.
		//so context#onOpen and doMapRightClick not working properly
		evt.target = this;
		evt.opts = {};
		this.$supers(gmaps.Gmarker, 'doRightClick_', arguments);
	},
	//private//
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
	},
	_doDragstart: function(latlng) {
		this._dragstarted = latlng; //start dragging this gmarker, store original latlng
	},
	_doPreDragend: function(evt) {
		//@see Gmaker.bindMapitem_
		//tricky! 
		//When dragging a marker inside the map and drop on the map,
		//prepare zk.Event to fire onMapDrop in _doDragend
		//then the #_doDragend method
		if (this._dragstarted)
			this._zkEvt = evt;
		//@see #_doDragend
	},
	_doDragend: function(latlng) {
		if (this._dragstarted) {
			//update the MarkerManager managing info to new point
			//this.parent._mm.onMarkerMoved_(this.mapitem_, this._dragstarted, latlng); 
			this._dragstarted = null;
		}

		//When dragging a marker inside the map and drop on the map
		//#_doPreDragend was called first, then this method(in some special 
		//case, it will miss.  DON'T know how to solve this yet.)
		//so we mainly try to get the keys of the event only, 
		var data = this._zkEvt ? this._zkEvt.data : {which:1}, //for keys only
			opts = this._zkEvt ? this._zkEvt.opts : {},
			domEvent = this._zkEvt ? this._zkEvt.domEvent : {},
			xy = this.parent.latlngToXY(this.parent,latlng),
			pageXY = gmaps.Gmaps.xyToPageXY(this.parent, xy.x, xy.y),
			nlat = latlng.lat(),
			nlng = latlng.lng(),
			//add information prepared by the _doPreDragend
			data = zk.copy(data, {lat:nlat,lng:nlng,dragged:this,x:xy.x,y:xy.y,pageX:pageXY[0],pageY:pageXY[1]});

		this._zkEvt = null;
		this.parent.fireX(new zk.Event(this.parent, 'onMapDrop', data, opts, domEvent));
		
		//after onMapDrop event, an unwanted onMapClick will be fired, shall block it
		//@see Gmaps#doClick_
		
	},
	_initListeners: function() {
		var gmarker = this.mapitem_,
			gmarkerwgt = this;

		this.$supers(gmaps.Gmarker, '_initListeners', arguments);
		this._dragstart = google.maps.event.addListener(gmarker, "dragstart", function() {gmarkerwgt._doDragstart(gmarkerwgt.mapitem_.getPosition());});
		this._dragend = google.maps.event.addListener(gmarker, "dragend", function() {gmarkerwgt._doDragend(gmarkerwgt.mapitem_.getPosition());});
		// simply trigger gmaps doDoubleClick with self event
		this._doubleClick = google.maps.event.addListener(gmarker, "dblclick", function(event) {event.target = gmarkerwgt; gmarkerwgt.parent.doDoubleClick_(event);});
		this._rightclick = google.maps.event.addListener(gmarker, "rightclick", function(evt) {gmarkerwgt.doRightClick_(evt);});
		// B65-Gmapsz-36: add mouseover and mouseout listener to show/hide tooltip
		this._mouseover = google.maps.event.addListener(gmarker, "mouseover", function(evt) {gmarkerwgt.doTooltipOver_(evt);});
		this._mouseout = google.maps.event.addListener(gmarker, "mouseout", function(evt) {gmarkerwgt.doTooltipOut_(evt);});
		if (this._area) {
			//Google Maps API will not bubble up the right-click-on-gmarker
			//and double-click-on-gmarker, so we add own dom listener here to 
			//handle such cases.   
			//Gmarker#doDoubleClick_ listens to area's ondblclick event
			this.domListen_(this._area, "ondblclick", "doDoubleClick_");
			//Gmarker#doRightClick_ listens to area's oncontextmenu event
			this.domListen_(this.$n(), "oncontextmenu", "doRightClick_");
			//Gmarker#_doPreDragend listens to area's onmouseup event
			this.domListen_(this._area, "onmouseup", "_doPreDragend");
			//Gmarker#_doPreDragend listens to area's onmouseout event
			this.domListen_(this._area, "onmouseout", "_doPreDragend");
		}
	},
	_clearListeners: function() {
		this.$supers(gmaps.Gmarker, '_clearListeners', arguments);
		if (this._dragstart) {
			google.maps.event.removeListener(this._dragstart);
			this._dragstart = null;
		}
		if (this._dragend) {
			google.maps.event.removeListener(this._dragend);
			this._dragend = null;
		}

		if (this._doubleClick) {
			google.maps.event.removeListener(this._doubleClick);
			this._doubleClick = null;
		}

		if (this._rightclick) {
			google.maps.event.removeListener(this._rightclick);
			this._rightclick = null;
		}
		
		if (this._mouseover) {
			google.maps.event.removeListener(this._mouseover);
			this._mouseover = null;
		}
		
		if (this._mouseout) {
			google.maps.event.removeListener(this._mouseout);
			this._mouseout = null;
		}

		if (this._area) {
			this.domUnlisten_(this._area, "ondblclick", "doDoubleClick_");
			this.domUnlisten_(this._area, "oncontextmenu", "doRightClick_");
			this.domUnlisten_(this._area, "onmouseup", "_doPreDragend");
			this.domUnlisten_(this._area, "onmouseout", "_doPreDragend");
		}
	},
	setRerender_: function(info) {
		this._minzoom = info.minzoom;
		this._maxzoom = info.maxzoom;
		if (info.iconShadow) this._iconShadow = info.iconShadow;
		if (info.iconSize) this._iconSize = info.iconSize;
		if (info.iconShadowSize) this._iconShadowSize = info.iconShadowSize;
		if (info.iconAnchor) this._iconAnchor = info.iconAnchor;
		if (info.iconInfoAnchor) this._iconInfoAnchor = info.iconInfoAnchor;
		if (info.iconPrintImage) this._iconPrintImage = info.iconPrintImage;
		if (info.iconMozPrintImage) this._iconMozPrintImage = info.iconMozPrintImage;
		if (info.iconPrintShadow) this._iconPrintShadow = info.iconPrintShadow;
		if (info.iconTransparent) this._iconTransparent = info.iconTransparent;
		if (info.iconImageMap) this._iconImageMap = info.iconImageMap;
		if (info.iconMaxHeight) this._iconMaxHeight = info.iconMaxHeight;
		if (info.iconDragCrossImage) this._iconDragCrossImage = info.iconDragCrossImage;
		if (info.iconDragCrossSize) this._iconDragCrossSize = info.iconDragCrossSize;
		if (info.iconDragCrossAnchor) this._iconDragCrossAnchor = info.iconDragCrossAnchor;
		if (info.tooltiptext) this._tooltiptext = info.tooltiptext;
		
		this.rebindMapitem_();
	},
	removeHTML_: function (n) {
		// B70-Gmapsz-44: do nothing, no dom element here.
	},
	// B65-Gmapsz-36: Returns gmaps dom element.
	$n: function (subId) {
		var maps = this.gmaps();
		return maps != null ? maps.getDiv() : null;
	},
	// B65-Gmapsz-36: only support 'at_pointer' and 'after_pointer' position.
	_parsePopParams: function (txt, event) {
		var params = this.$supers(gmaps.Gmarker, '_parsePopParams', arguments);
		if (params.position && params.position != 'at_pointer' && params.position != 'after_pointer')
			params.position = 'at_pointer';
		return params;
	}
});