/* Gmaps.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Jul 30 11:45:16     2009, Created by henrichen
		
	Used with ZK 5.0 and later
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
(function() {
/**
 * The component used to represent
 * &lt;a href="http://www.google.com/apis/maps/"&gt;Google Maps&lt;/a&gt;
 */
gmaps.Gmaps = zk.$extends(zul.Widget, {
	_center: {latitude: 37.4419, longitude: -122.1419},
	_zoom: 13,
	_mapType: 'normal',
	_normal: true,
	_hybrid: true,
	_physical: true,
	_showZoomCtrl: true,
	_showTypeCtrl: true,
	_showPanCtrl: true,
	_doubleClickZoom: true,
	_scrollWheelZoom: true,
	_enableDragging: true,
	_extraMapOptions: null,
	_version: '3',
	_gmapsApiConfigParams: {
		libraries: 'geometry'
	},
	
	$define: {
		/** 
		 * Returns the selected version of google map API v3.
		 * @return String
		 */
		/** 
		 * Set the selected version of google map API v3.
		 * @param String version.
		 */
		version: null,
		/** 
		 * Returns the center point of this Gmaps.
		 * @return double[]
		 */
		/** 
		 * Set the center point of this Gmaps.
		 * @param double[] center center[0] is latitude; center[1] is longitude.
		 */
		center: function(c) {
			var maps = this._gmaps;
			if (maps) {
				var latLng = new google.maps.LatLng(c.latitude,c.longitude);
				maps.setCenter(latLng);
			}
		},
		/** 
		 * Returns the viewport to contain the given bounds.
		 * @return double[]
		 */
		/** 
		 * Sets the viewport to contain the given bounds.
		 * @param double[] the bounds of this Gmaps.
		 */
		bounds: function(b) {
			var maps = this._gmaps;
			if (maps) {
				var bounds = b != null ? new google.maps.LatLngBounds(
						new google.maps.LatLng(b.southWest.latitude, b.southWest.longitude),
						new google.maps.LatLng(b.northEast.latitude, b.northEast.longitude)) : null;
				maps.fitBounds(bounds);
			}
		},
		/** 
		 * Returns the zoom level of this Gmaps.
		 * @return int
		 */
		/** 
		 * Set the zoom level of this Gmaps.
		 * @param int z the zoom level of this Gmaps.
		 */
		zoom: function(z) {
			var maps = this._gmaps;
			if (maps) maps.setZoom(z);
		},
		/** 
		 * Returns the maps type this Gmaps("normal", "satellite", "hybrid", "physical".
		 * @return String 
		 */
		/** 
		 * Sets the maps type this Gmaps("normal", "satellite", "hybrid", "physical".
		 * @param String t the maps type("normal", "satellite", "hybrid", "physical".
		 */
		mapType: function(t) {
			var maps = this._gmaps;
			if (maps) {
				var amid = google.maps.MapTypeId,
					type = amid.ROADMAP;
				switch (t) {
				default:
				case 'normal':
					type = amid.ROADMAP;
					break;
				case 'satellite':
					if (amid.SATELLITE) //china has no satellite map
						type = amid.SATELLITE;
					break;
				case 'hybrid':
					if (amid.HYBRID)
						type = amid.HYBRID;
					break;
				case 'physical':
					if (amid.TERRAIN)
						type = amid.TERRAIN;
					break;
				}
				var mopt = this.getMapOptions(),
					mtids = mopt.mapTypeControlOptions.mapTypeIds;
				if (mopt.mapTypeId != type) {
					for (var i = 0;i < mtids.length;i ++) {
						if (mtids[i] == type) { // is supported type
							mopt.mapTypeId = type;
							maps.setMapTypeId(type);
						}
					}
				}
			}
		},
		/** 
		 * Returns whether support normal map, default to true.
		 * @return boolean
		 */
		/** 
		 * Sets whether support normal map, default to true.
		 * @param boolean b whether support normal map, default to true.
		 */
		normal: function(b) {
			this._initMapType(b, 'normal');
		},
		/**
		 * Returns whether support satellite map, default to true.
		 * @return boolean
		 */
		/** 
		 * Sets whether support satellite map, default to true.
		 * @param boolean b whether support satellite map, default to true.
		 */
		satellite: function(b) {
			this._initMapType(b, 'satellite');
		},
		/** 
		 * Returns whether support hybrid map, default to true.
		 * @return boolean
		 */
		/** 
		 * Sets whether support hybrid map, default to true.
		 * @param boolean b whether support hybrid map, default to true.
		 */
		hybrid: function(b) {
			this._initMapType(b, 'hybrid');
		},
		/** 
		 * Returns whether support physical map, default to true.
		 * @return boolean
		 */
		/** 
		 * Sets whether support physical map, default to true.
		 * @param boolean b whether support physical map, default to true.
		 */
		physical: function(b) {
			this._initMapType(b, 'physical');
		},
		/** 
		 * Returns whether show the large Google Maps Control.
		 * @return boolean
		 */
		/** 
		 * Sets whether show the large Google Maps Control.
		 * @param boolean b true to show the large Google Maps Control.
		 */
		showLargeCtrl: function(b) {
			// currently not supported in v3
		},
		/** 
		 * Returns whether show the small Google Maps Control.
		 * @return boolean
		 */
		/** 
		 * Sets whether show the small Google Maps Control.
		 * @param boolean b true to show the small Google Maps Control.
		 */
		showSmallCtrl: function(b) {
			// currently not supported in v3
		},
		/** 
		 * Returns whether show the small zoom Google Maps Control.
		 * @return boolean
		 */
		/** 
		 * Sets whether show the small zoom Google Maps Control.
		 * @param boolean b true to show the small zoom Google Maps Control.
		 */
		showZoomCtrl: function(b) {
			var maps = this._gmaps;
			if (maps) {
				var opts = this.getMapOptions();
				opts.zoomControl = b;
				maps.setOptions(opts);
			}
		},
		/** 
		 * Returns whether show the Google Maps type Control.
		 * @return boolean
		 */
		/** 
		 * Sets whether show the Google Maps type Control.
		 * @param boolean b true to show the Google Maps type Control.
		 */
		showTypeCtrl: function(b) {
			var maps = this._gmaps;
			if (maps) {
				var opts = this.getMapOptions();
				opts.mapTypeControl = b;
				maps.setOptions(opts);
			}
		},
		/** 
		 * Returns whether show the Google Maps pan Control.
		 * @return boolean
		 */
		/** 
		 * Sets whether show the Google Maps pan Control.
		 * @param boolean b true to show the Google Maps pan Control.
		 */
		showPanCtrl: function(b) {
			var maps = this._gmaps;
			if (maps) {
				var opts = this.getMapOptions();
				opts.panControl = b;
				maps.setOptions(opts);
			}
		},
		/** 
		 * Returns whether show the Google Maps scale Control.
		 * @return boolean
		 */
		/** 
		 * Sets whether show the Google Maps scale Control.
		 * @param boolean b true to show the Google Maps scale Control.
		 */
		showScaleCtrl: function(b) {
			var maps = this._gmaps;
			if (maps) {
				var opts = this.getMapOptions();
				opts.scaleControl = b;
				maps.setOptions(opts);
			}
		},
		/** 
		 * Returns whether show the Google Maps overview Control, default to false.
		 * @return boolean
		 */
		/** 
		 * Sets whether show the Google Maps overview Control, default to false.
		 * @param boolean b whether show the Google Maps overview Control.
		 */
		showOverviewCtrl: function(b) {
			var maps = this._gmaps;
			if (maps) {
				var opts = this.getMapOptions();
				opts.overviewMapControl = b;
				maps.setOptions(opts);
			}
		},
		/** 
		 * Returns whether enable dragging maps by mouse, default to true.
		 * @return boolean
		 */
		/** 
		 * Sets whether enable dragging maps by mouse, default to true.
		 * @param boolean b true to enable dragging maps by mouse.
		 */
		enableDragging: function(b) {
			var maps = this._gmaps;
			if (maps) {
				var opts = this.getMapOptions();
				opts.draggable = b;
				maps.setOptions(opts);
			}
		},
		/** 
		 * Returns whether enable continuous zoom effects, default to false.
		 * @return boolean
		 */
		/** 
		 * Sets whether enable continuous zoom effects, default to false.
		 * @param boolean b true to enable continuous zoom effects.
		 */
		continuousZoom: function(b) {
			// currently not supported in v3
		},
		/** 
		 * Returns whether enable zoom in-out via mouse double click, default to false.
		 * @return boolean
		 */
		/** 
		 * Sets whether enable zoom in-out via mouse double click, default to false.
		 * @param boolean b true to enable zoom in-out via mouse double clilck.
		 */
		doubleClickZoom: function(b) {
			var maps = this._gmaps;
			if (maps) {
				var opts = this.getMapOptions();
				opts.disableDoubleClickZoom = !b;
				maps.setOptions(opts);
			}
		},
		/** 
		 * Returns whether enable zoom in-out via mouse scroll wheel, default to false.
		 * @return boolean
		 */
		/** 
		 * Sets whether enable zoom in-out via mouse scroll wheel, default to false.
		 * @param boolean b true to enable zoom in-out via mouse scroll wheel.
		 */
		scrollWheelZoom: function(b) {
			var maps = this._gmaps;
			if (maps) {
				var opts = this.getMapOptions();
				opts.scrollwheel = b;
				maps.setOptions(opts);
			}
		},
		/** 
		 * Returns whether show the Google Search Bar on the Map, default to false.
		 * @return boolean
		 */
		/** 
		 * Sets whether show the Google Search Bar on the Map, default to false.
		 * @param boolean b true to show the Google Search Bar
		 */
		enableGoogleBar: function(b) {
			// currently not supported in v3
		},
		/**
		 * @return Object the last value provided by setExtraMapOptions
		 * @see #setExtraMapOptions
		 * @since 3.1.0
		 */
		 /**
		 * Set additional map options for which no dedicated getter/setter exists (e.g. tilt, fullscreenControl ...)
		 * Adds/replaces mapOption properties of the provided object into the current mapOptions.
		 * It does not remove missing properties, instead you have to set null, [], {} or false respectively.
		 * The behavior is undefined if used with mapOptions which already have a dedicated getter/setter.
		 * @param Object extraOpts the map options to set/replace
		 * @since 3.1.0
		 */
		extraMapOptions: function(extraOpts) {
			var maps = this._gmaps;
			if (maps) {
				var opts = this.getMapOptions();
				zk.copy(opts, extraOpts);
				maps.setOptions(opts);
			}
		},
		/**
		 * Returns the base domain from which to load the Maps API. For example,
		 * you could load from "ditu.google.cn" with the "maps" module to get
		 * the Chinese version of the Maps API; null to use the default domain.
		 * @return String
		 */
		/**
		 * Sets the base domain from which to load the Maps API. For example,
		 * you could load from "ditu.google.cn" with the "maps" module to get
		 * the Chinese version of the Maps API; null to use the default domain.
		 * @param String baseDomain the base domain from which to load the Maps API
		 */
		baseDomain: null,
		/**
		 * Returns the protocol to load the Maps API.
		 * Currently support http for insecure connections
		 * and https for secure connections.
		 * @return the user specified protocol to load the Maps API.
		 * @since 3.0.0
		 */
		/**
		 * Sets the protocol to load the Maps API. 
		 * Currently support http for insecure connections
		 * and https for secure connections.
		 * @param protocol the protocol to load the Maps API
		 * @since 3.0.0
		 */
		protocol: null,
		
		/**
		 * Returns the google maps API parmeter configuration map.
		 * @return object containing the parameters load the Maps API with.
		 * @since 3.0.5
		 */
		/**
		 * Set the google maps API parmeter configuration map.
		 * @param protocol the protocol to load the Maps API
		 * @since 3.0.5
		 */
		gmapsApiConfigParams: null 
	},
	/**
	 * Add supported map type into this Gmaps("normal", "satellite", "hybrid", "physical").
	 * @param String maptype the supported map type.
	 * @see #setNormal
	 * @see #setSatellite
	 * @see #setHybrid
	 * @see #setPhysical
	 */
	addMapType: function(t) {
		var maps = this._gmaps;
		if (maps) {
			var mtid;
			switch (t) {
			case 'normal':
				mtid = google.maps.MapTypeId.ROADMAP;
				break;
			case 'satellite':
				mtid = google.maps.MapTypeId.SATELLITE;
				break;
			case 'hybrid':
				mtid = google.maps.MapTypeId.HYBRID;
				break;
			case 'physical':
				mtid = google.maps.MapTypeId.TERRAIN;
				break;
			default: return;
			}
			var mapopt = this.getMapOptions();
			// do nothing if already contain mtid
			var mtids = mapopt.mapTypeControlOptions.mapTypeIds;
			for(var i = 0;i < mtids.length;i ++) {
				if (mtids[i] == mtid)
					return;
			}
			mtids.push(mtid);
			maps.setOptions(mapopt);
		}
	},
	/**
	 * Remove supported map type from this Gmaps("normal", "satellite", "hybrid", "physical").
	 * @param String map type name
	 * @see #setNormal
	 * @see #setSatellite
	 * @see #setHybrid
	 * @see #setPhysical
	 */
	removeMapType: function(t) {
		var maps = this._gmaps;
		if (maps) {
			var mtid;
			switch (t) {
			case 'normal':
				mtid = google.maps.MapTypeId.ROADMAP;
				break;
			case 'satellite':
				mtid = google.maps.MapTypeId.SATELLITE;
				break;
			case 'hybrid':
				mtid = google.maps.MapTypeId.HYBRID;
				break;
			case 'physical':
				mtid = google.maps.MapTypeId.TERRAIN;
				break;
			default: return;
			}
			var mapopt = this.getMapOptions(),			
				mtids = mapopt.mapTypeControlOptions.mapTypeIds,
				idx = mtids.indexOf(mtid);
			// do remove contain mtid
			if (idx != -1) {
				mtids.splice(idx, 1);
			}
			maps.setOptions(mapopt); 
		}
	},
	/** 
	 * Pan this Gmaps to the specified center point.
	 * @param double[] center center[0] is latitude; center[1] is longitude.
	 */
	panTo: function(c) {
		this.setPanTo_(c);
	},
	/** 
	 * Open the specified info window.
	 * @param Ginfo info the info window.
	 */
	openInfo: function(info) {
		this.setOpenInfo_(info);
	},
	/** 
	 * Close the specified info window.
	 * @param Ginfo info the info window.
	 */
	closeInfo: function(info) {
		this.setCloseInfo_(info);
	},
	redraw: function(out) {
		out.push('<div', this.domAttrs_(), '></div>');
	},
	setOpenInfo_: function(info) {
		if (typeof info == 'string') {
			info = zk.Widget.$(info);
		}
		var maps = this._gmaps;
		// if maps is undifined, just skip and let info open it self
		if (maps && info) {
			var infocontent = info.getContent();
			if (!infocontent) return; //no contents, no way to open info window

			// get a new info window if no old window
			if (!info._infowindow) {
				info._infowindow = new google.maps.InfoWindow();
			}
			var wgt = this;
			info._infowindowclose = google.maps.event.addListener(info._infowindow, 'closeclick', function() {
				// do something while close button clicked here
				wgt.setCloseInfo_(info);
			});
			var infWin = info._infowindow;
			infWin.setContent(infocontent);

			this._opening = true;

			// remove close listener if it never be triggered
			if (info._closeListener) {
				google.maps.event.removeListener(info._closeListener);
				info._closeListener = null;
			}

			if (info.$instanceof(gmaps.Gmarker)) { //gmaps.Gmarker
				// icon info anchor, have not process it
				var iinfanch = info.getIconInfoAnchor();
				// markermanager will handle panto as need, no more panto
				infWin.open(maps, info.mapitem_);
				info._open = true;
			} else { //gmaps.Ginfo
				var anch = info.getAnchor();
				infWin.setPosition(new google.maps.LatLng(anch.latitude, anch.longitude));
				infWin.open(maps);
				info._open = true;
			}
			this._opening = false;
		}
	},
	setCloseInfo_: function(info) {
		if (typeof info == 'string') {
			info = zk.Widget.$(info);
		}
		var maps = this._gmaps;
		if (maps && info) {
			info._open = false;
			if (info._infowindow) {
				info._infowindow.close();
				var anch = info.getAnchor();
				if (info.$instanceof(gmaps.Gmarker) && !maps.getBounds().contains(new google.maps.LatLng(anch.latitude, anch.longitude))) {
					// if the info window is bind with a gmarker,
					// and its out of bounds and we close it,
					// it will be opened by markermanager after panto its position,
					// close it again at that time
					info._closeListener = google.maps.event.addListener(
							info._infowindow,
							'domready',
							function() {info._infowindow.close(); google.maps.event.removeListener(info._closeListener); info._infowindow = info._closeListener = null;}
					);
				} else
					info._infowindow = null;
				this._doInfoClose(info);
			}
		}
	},
	setPanTo_: function(c) {
		this._center = c;
		var maps = this._gmaps;
		if (maps) {
			var latLng = new google.maps.LatLng(c.latitude, c.longitude);
			maps.panTo(latLng);
		}
	},
	bind_: function(dt, skipper, after) {
		var wgt = this;
		//#3: call $supers first to keep consistency of zk's life cycle
		//but notice that all goverlay render should be called as callback function
		wgt.$supers(gmaps.Gmaps, 'bind_', arguments); //calling down kid widgets to do binding

		this._maskOpts = gmapsGapi.initMask(this, {message: 'Loading Google Maps APIs'});
		if (!window.google || !window.google.maps)
			gmapsGapi.loadAPIs(wgt, function() {wgt._tryBind(dt, skipper, after)}, 'Loading Google Ajax APIs');
		else {
			var opts1 = [];
			opts1['condition'] = function() {return window.MarkerManager && wgt.$n().offsetHeight;};
			opts1['callback'] = function() {wgt._realBind(dt, skipper, after);};
			gmapsGapi.waitUntil(wgt, opts1);
		}
	},
	_tryBind: function(dt, skipper, after) {
		var maskOpts;
		if (maskOpts = this._maskOpts) {
			if (maskOpts._mask && maskOpts._mask._opts) {
				maskOpts._mask._opts.anchor = this;
				maskOpts._mask.sync();
			} else {
				gmapsGapi.clearMask(this, maskOpts);
				this._maskOpts = gmapsGapi.initMask(this, {message: 'Loading Google Maps APIs'});
			}
		}

		if (!window.google || !window.google.maps) {
			if (window.google && (!window.google.load  || window.google.load.LoadFailure)) {
				var n = jq(this.uuid, zk)[0];
				n.innerHTML = gmaps.Gmaps.errormsg;
				return;  //failed to load the Google AJAX APIs
			}
			var wgt = this,
				opts0 = {};
			opts0['condition'] = function() {return window.google && window.google.maps;};
			opts0['callback'] = function() {};
			
			if (!opts0.condition()) {
				
				gmapsGapi.waitUntil(wgt, opts0);
				if (!gmaps.Gmaps.LOADING) { //avoid double loading Google Maps APIs
					gmaps.Gmaps.LOADING = true;
					if (!opts0.condition()) {
						if(!this._gmapsApiConfigParams.client && !this._gmapsApiConfigParams.key && zk.googleAPIkey) {
							this._gmapsApiConfigParams.key = zk.googleAPIkey;
						}
						var otherParams = jq.param(this._gmapsApiConfigParams);
						var optionalSettings = {
							other_params: otherParams,
							callback: function() {// load marker manager after map api loaded
								zk.loadScript(zk.ajaxURI('/web/js/gmaps/ext/markermanager.js', {desktop : this.desktop,au : true}));
								wgt._realBind(dt, skipper, after);
							}
						};
						if(this._baseDomain) {
							optionalSettings.base_domain = this._baseDomain;
						}
						google.load('maps', this._version, optionalSettings); // load the maps api
					}
				}
			} else {
				opts0['condition'] = function() {return window.MarkerManager;};
				opts0['callback'] = function() {wgt._realBind(dt, skipper, after);};
				gmapsGapi.waitUntil(wgt, opts0);
			}
		} else {
			var wgt = this,
				opts1 = [];
			opts1['condition'] = function() {return window.MarkerManager;};
			opts1['callback'] = function() {wgt._realBind(dt, skipper, after);};
			
			gmapsGapi.waitUntil(wgt, opts1);
		}
	},
	_realBind: function(dt, skipper, after) {
		var n = jq(this.uuid, zk)[0],
			maskOpts;
		if (maskOpts = this._maskOpts) {
			if (maskOpts._mask && maskOpts._mask._opts) {
				maskOpts._mask._opts.anchor = this;
				maskOpts._mask.sync();
			} else {
				gmapsGapi.clearMask(this, maskOpts);
				this._maskOpts = gmapsGapi.initMask(this, {message: 'Loading Google Maps APIs'});
			}
		}
		if ( (window.google == null) || (window.google.maps == null) ) {
			n.innerHTML = gmaps.Gmaps.errormsg;
			return; //failed to load the Google Maps APIs
		}
		this._initGmaps(n);

		// wait until markermanager loaded
		var opts0 = [],
			opts1 = [],
			wgt = this, // Issue 8: First gmarker fails when there are more than one gmap in a page
			maps = this._gmaps;
		opts0['condition'] = function() {return window.MarkerManager};
		opts0['callback'] = function() {
			
			setTimeout(function() {
			  var types = maps.mapTypes;

			// Issue 28: Find map max zoom level.
			  var mapsMaxZoom = 19;
			  for (var type in types ) {
				  if (typeof types.get(type) === 'object' && typeof types.get(type).maxZoom === 'number') {
					  var zoom = types.get(type).maxZoom;
				      if (zoom > mapsMaxZoom) {
				        mapsMaxZoom = zoom;
				      }
				  }
			  }
			wgt._maxzoom = mapsMaxZoom;
			wgt._mm = new MarkerManager(maps, {trackMarkers: true, maxZoom: mapsMaxZoom});
			google.maps.event.addListener(wgt._mm, 'loaded', function(){
				wgt._mmLoaded = true;
			});}, 0);
		};
		
		gmapsGapi.waitUntil(wgt, opts0);

		// wait until marker manager is initialized
		opts1['condition'] = function() {return wgt._mmLoaded};
		opts1['callback'] = function() {
			wgt.overrideMarkermanager();
			//init listeners
			wgt._initListeners(n);
			
			//bug #2929253 map canvas partly broken when map was invisible
			//watch the global event onSize/onShow (must after $supers(gmaps.Gmaps, 'bind_', arguments)) 
			zWatch.listen({onSize: wgt, onShow: wgt});
			// set the hflex/vflex again after bind
			if (wgt._hflex)
				wgt.setHflex(wgt._hflex, {force:true});
			if (wgt._vflex)
				wgt.setVflex(wgt._vflex, {force:true});
			
			//Tricky!
			//IE will not fire onSize at the end, so we have to enforce a 
			//resize(true) to restore the center
			if (zk.ie)
				setTimeout(function () {wgt._resize(true);}, 500);
		};
		
		gmapsGapi.waitUntil(wgt, opts1);
	},
	unbind_: function() { //detach or server invalidate()
		this.$supers(gmaps.Gmaps, 'unbind_', arguments);
		this._clearGmaps();
	},
	beforeParentChanged_: function(p) { //detach()
		this.$supers(gmaps.Gmaps, 'beforeParentChanged_', arguments);
		if (!p) this._clearGmaps();
	},
	//override dom event//
	doClick_: function(evt) {
		var wgt = evt.target,
			gmap = this;

		//calling this to correct the popup submenu not auto closed issue
		zk.Widget.mimicMouseDown_(wgt);
		// google map event
		if (evt.latLng) {
			var latLng = evt.latLng,
				xy = gmaps.Gmaps.latlngToXY(this, latLng),
				pageXY = gmaps.Gmaps.xyToPageXY(this.parent, xy.x, xy.y),
				data = zk.copy(evt.data, {lat:latLng.lat(),lng:latLng.lng(),reference:wgt,x:xy.x,y:xy.y,pageX:pageXY[0],pageY:pageXY[1]}),
				opts = evt.opts;
			// keep the data,
			// will modified by the widget event that triggered by mimicMouseDown_
			this._onMapClickData = data;
			// Gmaps do not have dom element in event.
			evt.domTarget = evt.domTarget || this.$n();
			// fire event later
			setTimeout (
				function () {
					gmap.fireX(new zk.Event(gmap, 'onMapClick', data, opts, evt.domEvent));
					delete gmap._onMapClickData;
			}, 0);
			// do not select self
			if (wgt != this)
				this.fireX(new zk.Event(this, 'onSelect', {items:[wgt],reference:wgt}, opts, evt.domEvent));

			this.$supers(gmaps.Gmaps, 'doClick_', arguments);
		} else { // widget event
			var edata = evt.data,
				data;
			// has google map data to update
			if (data = this._onMapClickData) {
				for (var key in evt.data) {
					// copy data if not exist
					if (!(key in data))
						data[key] = edata[key];
				}
			}
		}
	},
	doDoubleClick_: function (evt) {
		//Google Maps API will not bubble up the double-click-on-gmarker
		//domEvent to container(Gmaps#_gmaps) so we add own listener
		//to handle such cases. 
		//@see Gmarker#_initListeners
		if(!evt.latLng) return;
		var latlng = evt.latLng,
			xy = this._mm.projection_.fromLatLngToDivPixel(latlng, this._gmaps.getZoom());
		var	pageXY = gmaps.Gmaps.xyToPageXY(this, xy.x, xy.y),
			wgt = evt.target? evt.target : this,
			data ={};
		data = zk.copy(data, {lat:latlng.lat(),lng:latlng.lng(),reference:wgt,x:xy[0],y:xy[1],pageX:pageXY[0],pageY:pageXY[1]});
		// fake opts for fireX
		evt.opts = {};
		this.fireX(new zk.Event(this, 'onMapDoubleClick', data, null, 'ondblclick'));
		this.$supers(gmaps.Gmaps, 'doDoubleClick_', arguments);
	},
	doRightClick_: function (evt) {
		var data = {which:3},
			xy,
			latlng, 
			wgt = evt.target,
			pageXY; 
		// maybe from google event or dom event
		if (evt.latLng) {
			xy = this._mm.projection_.fromLatLngToDivPixel(evt.latLng, this._gmaps.getZoom());
			latlng = evt.latLng;
			pageXY = gmaps.Gmaps.xyToPageXY(this, xy.x, xy.y);
		} else {
			var pageX = evt.pageX,
				pageY = evt.pageY;
			xy = gmaps.Gmaps.pageXYToXY(this, pageX, pageY);
			latlng = new google.maps.LatLng(0, 0);
			pageXY = [pageX, pageY];
		}

		//Google Maps API will not bubble up the right-click-on-gmarker
		//domEvent to container(Gmaps#_gmaps) so we add own listener
		//to handle such cases. 
		//@see Gmarker#_initListeners

		//calling this to correct the context submenu not auto closed issue
		zk.Widget.mimicMouseDown_(wgt);
		
		data = zk.copy(data, {lat:latlng.lat(),lng:latlng.lng(),reference:wgt,x:xy[0],y:xy[1],pageX:pageXY[0],pageY:pageXY[1]});
		// fake opts for fireX
		this.fireX(new zk.Event(this, 'onMapRightClick', data, null, 'oncontextmenu'));
		this.$supers(gmaps.Gmaps, 'doRightClick_', arguments);
	},
	//private//
	_initMapType: function(b, t) {
		if (b)
			this.addMapType(t);
		else
			this.removeMapType(t);
	},
	_initMapitems: function() {
		var kid = this.firstChild;
		while (kid) {
			kid.bindMapitem_();
			kid = kid.nextSibling;
		}
	},
	_doMoveEnd: function() {
		var wgt = this;
		
		var maps = this._gmaps,
			b = maps.getBounds(),
			sw = b.getSouthWest(),
			ne = b.getNorthEast();
		this._centerRestored = null;
		// if this is not real visible, the move should caused by calling api,
		// and map will move to wrong position,
		// do not get center value from map, just keep default.
		if (this.isRealVisible()) {
			var c = maps.getCenter();
			this._center = {latitude: c.lat(), longitude: c.lng()};
		}
		this.fireX(new zk.Event(this, 'onMapMove', {lat:this._center.latitude,lng:this._center.longitude,swlat:sw.lat(),swlng:sw.lng(),nelat:ne.lat(),nelng:ne.lng()}, {}, null));
	},
	_doZoomEnd: function() {
		var maps = this._gmaps;
		this._zoom = maps.getZoom();
		this.fireX(new zk.Event(this, 'onMapZoom', {zoom:this._zoom}, {}, null));
	},
	_doMapTypeChanged: function() {
		var maps = this._gmaps,
			amid = google.maps.MapTypeId,
			type = this.getMapOptions().mapTypeId = maps.getMapTypeId();
		if (type) {
			switch (type) {
			default:
			case amid.ROADMAP:
				type = 'normal';
				break;
			case amid.SATELLITE:
				type = 'satellite';
				break;
			case amid.HYBRID:
				type = 'hybrid';
				break;
			case amid.TERRAIN:
				type = 'physical';
				break;
			}
			if (this._mapType != type) {
				this._mapType = type;
				this.fireX(new zk.Event(this, 'onMapTypeChange', {type:type}, {}, null));
			}
		}
	},
	//1. when end user click the x icon, or click outside the info window, close without condition
	//2. when programmer called setCloseInfo_ (directly, or indirectly from Gmarker#setOpen(false)), close without condition
	//3. when MarkerManager#removeOverlay_, _closing == true
	//4. when Gmarker was removed from the gmaps(MarkerManager#removeOverlay_ then Gmarker#unbindMapitem_).
	//5. when another Ginfo/Gmarker open (via API only), _opening == true
	_doInfoClose: function(ginfo) {
		if (ginfo) {
			ginfo.clearOpen_();
			this.fireX(new zk.Event(this, 'onInfoChange', {info: ginfo}, {}, null));
		}
	},
	_changeInfoPosition: function(c, info) {
		if (info._infowindow) {
			info._infowindow.setPosition(new google.maps.LatLng(c.latitude, c.longitude));
		}
	},
	_changeInfoContent: function(s, info) {
		if (info._infowindow) {
			info._infowindow.setContent(s);
		}
	},
	_resize: function(isshow) {
		var maps = this._gmaps; 
		if (maps && this.isRealVisible()) {
			//bug 2099729: in IE, gmap's container div height will not resize automatically
			var n = jq(this.uuid, zk)[0];
			if (zk.ie) { 
				var hgh = n.style.height;
				if (hgh.indexOf('%') >= 0) {
					n.style.height="";
					n.style.height=hgh;
				}
			}
			
			//Still has to restore the center if onSize event fired first
			var shallRestoreCenter = isshow || !this._centerRestored;
			// should trigger resize for browser maximized case
			google.maps.event.trigger(maps, 'resize');
			
			if (shallRestoreCenter) {
				//@see #_doMoveEnd
				var latLng = new google.maps.LatLng(this._center.latitude,this._center.longitude);
				maps.setCenter(latLng);
				this._centerRestored = true;
			} else {
				this._doMoveEnd(); //fire _doMoveEnd for maps.checkResize() case
			}
		}
	},
	_initListeners: function(n) {
		var maps = this._gmaps,
			wgt = this;
		this._moveend = google.maps.event.addListener(maps, 'idle', function() {wgt._mm.onMapMoveEnd_(); wgt._doMoveEnd();});
		this._click = google.maps.event.addListener(maps, 'click', function(event) {wgt.doClick_(new zk.Event(wgt, 'onClick', {latLng: event.latLng}))});
		this._doubleclick = google.maps.event.addListener(maps, 'dblclick', function(event) {wgt.doDoubleClick_(event)});
		this._zoomend = google.maps.event.addListener(maps, 'zoom_changed', function() {wgt._doZoomEnd()});
		this._maptypechanged = google.maps.event.addListener(maps, 'maptypeid_changed', function() {wgt._doMapTypeChanged()});
	},
	_clearListeners: function() {
		if (this._moveend ) {
			google.maps.event.removeListener(this._moveend);
			this._moveend = null;
		}
		if (this._click) {
			google.maps.event.removeListener(this._click);
			this._click = null;
		}

		if (this._doubleclick ) {
			google.maps.event.removeListener(this._doubleclick);
			this._doubleclick = null;
		}
		if (this._zoomend) {
			google.maps.event.removeListener(this._zoomend);
			this._zoomend = null;
		}

		if (this._infowindowclose) {
			google.maps.event.removeListener(this._infowindowclose);
			this._infowindowclose = null;
		}

		if (this._maptypechanged) {
			google.maps.event.removeListener(this._maptypechanged);
			this._maptypechanged = null;
		}
	},
	/**
	 * keep the map options so we can modify it later
	 */
	getMapOptions: function() {
		if (!this._mapOptions) {
			// default not support contain map type
			var mtids = [];
			this._mapOptions = {
				// used to be {type: G_PHYSICAL_MAP}
				mapTypeControlOptions: {mapTypeIds: mtids}
			};
		}
		return this._mapOptions;
	},
	_initGmaps: function(n) {
		var maps = new google.maps.Map(n, this.getMapOptions());
		if (this._maskOpts)
			gmapsGapi.clearMask(this, this._maskOpts);

		this._gmaps = maps;
		this.setNormal(this._normal, {force:true}) //prepare map types
			.setHybrid(this._hybrid, {force:true})
			.setSatellite(this._satellite, {force:true})
			.setPhysical(this._physical, {force:true})
			.setMapType(this._mapType, {force:true}) //set initial map type
			.setShowTypeCtrl(this._showTypeCtrl, {force:true})
			.setShowZoomCtrl(this._showZoomCtrl, {force:true})
			.setShowPanCtrl(this._showPanCtrl, {force:true})
			.setShowScaleCtrl(this._showScaleCtrl, {force:true})
			.setShowOverviewCtrl(this._showOverviewCtrl, {force:true})
			.setDoubleClickZoom(this._doubleClickZoom, {force:true})
			.setScrollWheelZoom(this._scrollWheelZoom, {force:true})
			.setEnableDragging(this._enableDragging, {force:true})
			.setCenter(this._center, {force:true})
			.setZoom(this._zoom, {force:true})
			.setExtraMapOptions(this._extraMapOptions, {force:true});
		if (this._bounds)
			this.setBounds(this._bounds, {force:true});
	},
	overrideMarkermanager: function() {
		var mm = this._mm; //markermanager
		mm.addOverlay_ = function (marker) {
			var markerwgt = marker._wgt; //Gmarker widget

			if (mm.show_) {
				marker.setMap(mm.map_);
				mm.shownMarkers_++;
				markerwgt._initListeners();
			}
		};
		mm.removeOverlay_ = function(marker) {
			var markerwgt = marker._wgt; //Gmarker widget

			marker.setMap(null);
			mm.shownMarkers_--;
			markerwgt._clearListeners();
		}
		this.overlayOverride = true;
	},
	_clearGmaps: function() {
		this._clearListeners();
		this._gmaps = this._lctrl = this._sctrl = this._tctrl = this._cctrl = this._octrl
			= this._mm = this._mmLoaded = this._centerRestored = this._onMapClickData 
			= this._maskOpts = null;
	},
	//zWatch//
	onSize: function() {
		this._resize(false);
	},
	onShow: function() {
		this._resize(true);
	}
},{//static
	//given Gmaps, pageXY, return relative xy as [x, y]
	pageXYToXY: function(gmaps, x, y) {
		var orgxy = zk(gmaps).revisedOffset();
		return [x - orgxy[0], y - orgxy[1]]; 
	},
	//given Gmaps, relative xy, return pageXY as [pageX, pageY]
	xyToPageXY: function(gmaps, x, y) {
		var orgxy = zk(gmaps).revisedOffset();
		return [x + orgxy[0], y + orgxy[1]];
	},
	// given Gmaps widget, latlng, return xy as [x, y]
	latlngToXY: function (wgt, latlng) {
		var gmaps = wgt._gmaps,
			zoom = gmaps.getZoom(),
			bounds = gmaps.getBounds(),
			nwLatlng = new google.maps.LatLng(bounds.getNorthEast().lat(),
							bounds.getSouthWest().lng()),
			projection = wgt._mm.projection_,
			nwXY = projection.fromLatLngToDivPixel(nwLatlng, zoom), // X, Y of North West of bounds
			evtXY = projection.fromLatLngToDivPixel(latlng, zoom); // X, Y of clicked point

		return {x: evtXY.x-nwXY.x, y: evtXY.y-nwXY.y};
	},
	errormsg: '<p>To use <code>&lt;gmaps&gt;</code>, you have to specify the following statement in your page:</p>'
		+'<code>&lt;script content="zk.googleAPIkey='+"'key-assigned-by-google'"+'" /></code>' 
});
//register to be called when window.onunload. 
//jq(function(...)) tells to do this until html document is ready.
jq(function() {jq(window).unload(function(){/** 
	* Issue 9: Javascript Error: GUnload is not defined
	* TODO unload maps if unload API avaliable*/})});
})();
