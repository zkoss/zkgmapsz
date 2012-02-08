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
	zk.gapi.loadAPIs = function(wgt, callback, msg, timeout) {
		var opts = {};
		opts['condition'] = function() {return window.google && window.google.load};
		opts['callback'] = function() {callback(); delete zk.gapi.LOADING;}
		opts['message'] = msg;
		if (!opts.condition()) {
			zk.gapi.waitUntil(wgt, opts);
			if (!zk.gapi.LOADING) { //avoid double loading Google Ajax APIs
				zk.gapi.LOADING = true;
				if (!opts.condition()) {
					if (!zk.googleAPIkey) {
						zk.loadScript('http://www.google.com/jsapi');
					}
					else 
						zk.loadScript('http://www.google.com/jsapi?key='+zk.googleAPIkey);
				}
			}
		} else {
			callback();
		}
	};
/**
 * The component used to represent
 * &lt;a href="http://www.google.com/apis/maps/"&gt;Google Maps&lt;/a&gt;
 */
gmaps.Gmaps = zk.$extends(zul.Widget, {
	_center: [37.4419, -122.1419],
	_zoom: 13,
	_mapType: 'normal',
	_normal: true,
	_hybrid: true,
	_physical: true,
	_showZoomCtrl: true,
	_showTypeCtrl: true,
	_doubleClickZoom: true,
	_scrollWheelZoom: true,
	_enableDragging: true,
	_version: '3',
	_libraries: 'geometry',
	
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
				var latLng = new google.maps.LatLng(c[0],c[1]);
				maps.setCenter(latLng);
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
	     * Returns whether your application is using a sensor (such as a GPS locator) 
	     * to determine the user's location. This is especially important for mobile 
	     * devices; default is false.
	     * @return boolean
	     */
	    /**
	     * Sets whether your application is using a sensor (such as a GPS locator) 
	     * to determine the user's location. This is especially important for mobile 
	     * devices; default is false.
	     * @param boolean sensor whether using a sensor to determine the user's location.
	     */
		sensor: null,
		/**
		 * Returns the preferred language code; default to null and means using
		 * browser's preferred language. You can check language code 
		 * <a href="http://spreadsheets.google.com/pub?key=p9pdwsai2hDMsLkXsoM05KQ&gid=1">here</a> 
		 * <p>By default Gmaps uses the browser's preferred language setting when 
		 * displaying textual information such as control names, copyright, and so
		 * one. Sets language code will make Gmaps to always use the specified
		 * language and ignore the browser's language setting.
		 * 
		 * @return String
		 */
		/**
		 * Sets the preferred language code; default to null and means using
		 * browser's preferred language. You can check language code 
		 * <a href="http://spreadsheets.google.com/pub?key=p9pdwsai2hDMsLkXsoM05KQ&gid=1">here</a> 
		 * <p>By default Gmaps uses the browser's preferred language setting when 
		 * displaying textual information such as control names, copyright, and so
		 * one. Sets language code will make Gmaps to always use the specified
		 * language and ignore the browser's language setting.
		 * 
		 * @param String language the preferred language code
		 */
		language: null,
		/**
		 * Returns the libraries to load; default to geometry and means
		 * load geometry library only, you can check libraries
		 * <a href="http://code.google.com/intl/zh-TW/apis/maps/documentation/javascript/libraries.html">here</a>
		 * <p> Use comma to separate different library
		 * 
		 * 
		 * @return the libraries to load.
		 * @since 3.0.0
		 */
		/**
		 * Sets the libraries to load; default to geometry and means using
		 * load geometry library only, you can check libraries
		 * <a href="http://code.google.com/intl/zh-TW/apis/maps/documentation/javascript/libraries.html">here</a>
		 * <p> Use comma to separate different library
		 * 
		 * @param libraries the libraries to load
		 * @since 3.0.0
		 */
		libraries:null
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
		        infWin.setPosition(new google.maps.LatLng(anch[0], anch[1]));
		        infWin.open(maps);
		        info._open = true;
			}
		    this._opening = false;
		}
	},
	setCloseInfo_: function(info) {
		var maps = this._gmaps;
		if (maps && info) {
			info._open = false;
			if (info._infowindow) {
				info._infowindow.close();
				var anch = info.getAnchor();
				if (info.$instanceof(gmaps.Gmarker) && !maps.getBounds().contains(new google.maps.LatLng(anch[0], anch[1]))) {
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
			var latLng = new google.maps.LatLng(c[0], c[1]);
			maps.panTo(latLng);
		}
	},
	bind_: function(dt, skipper, after) {
		var wgt = this;
		if (!window.google || !window.google.maps)
			zk.gapi.loadAPIs(wgt, function() {wgt._tryBind(dt, skipper, after)}, 'Loading Google Ajax APIs');
		else {
			var wgt = this,
				opts1 = [];
			opts1['condition'] = function() {return window.MarkerManager;};
			opts1['callback'] = function() {wgt._realBind(dt, skipper, after);};
			opts1['message'] = 'Loading Google Maps APIs';
			zk.gapi.waitUntil(wgt, opts1);
		}
	},
	_tryBind: function(dt, skipper, after) {
		if (!window.google || !window.google.maps) {
			if (!window.google.load || window.google.loader.LoadFailure) {
				var n = jq(this.uuid, zk)[0];
				n.innerHTML = gmaps.Gmaps.errormsg; 
				return;  //failed to load the Google AJAX APIs
			}
			var wgt = this,
				opts0 = {};
			opts0['condition'] = function() {return window.google.maps;};
			opts0['callback'] = function() {};
			opts0['message'] = 'Loading Google Maps APIs';
			if (!opts0.condition()) {
				zk.gapi.waitUntil(wgt, opts0);
				if (!gmaps.Gmaps.LOADING) { //avoid double loading Google Maps APIs
					gmaps.Gmaps.LOADING = true;
					if (!opts0.condition()) {
						var opts = ['sensor=',
						            this._sensor? this._sensor : 'false',
						            '&language=', this._language? this._language : '',
						            '&region=', this._baseDomain? this._baseDomain : '',
						            '&libraries=', this._libraries? this._libraries : ''].join('');
						google.load('maps', this._version,
								{
									other_params: opts,
									callback: function(){// load marker manager after map api loaded
										zk.loadScript(zk.ajaxURI('/web/js/gmaps/ext/markermanager.js', {desktop : this.desktop,au : true}));
										wgt._realBind(dt, skipper, after);
									}
								}
						); // load the maps api
					}
				}
			} else {
				opts0['condition'] = function() {return window.MarkerManager;};
				opts0['callback'] = function() {wgt._realBind(dt, skipper, after);};
				zk.gapi.waitUntil(wgt, opts0);
			}
		} else {
			var wgt = this,
				opts1 = [];
			opts1['condition'] = function() {return window.MarkerManager;};
			opts1['callback'] = function() {wgt._realBind(dt, skipper, after);};
			opts1['message'] = 'Loading Google Maps APIs';
			zk.gapi.waitUntil(wgt, opts1);
		}
	},
	_realBind: function(dt, skipper, after) {
		var n = jq(this.uuid, zk)[0];
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
			wgt._mm = new MarkerManager(maps, {trackMarkers: true});
			google.maps.event.addListener(wgt._mm, 'loaded', function(){
				wgt._mmLoaded = true;
	        });
		};
		opts0['message'] = 'Loading Marker Manager API';
		zk.gapi.waitUntil(wgt, opts0);

		// wait until marker manager is initialized
		opts1['condition'] = function() {return wgt._mmLoaded};
		opts1['callback'] = function() {
			wgt.overrideMarkermanager();
			//init listeners
			wgt._initListeners(n);
			wgt.$supers(gmaps.Gmaps, 'bind_', arguments); //calling down kid widgets to do binding
			//bug #2929253 map canvas partly broken when map was invisible
			//watch the global event onSize/onShow (must after $supers(gmaps.Gmaps, 'bind_', arguments)) 
			zWatch.listen({onSize: wgt, onShow: wgt});
			
			//Tricky!
			//IE will not fire onSize at the end, so we have to enforce a 
			//resize(true) to restore the center
			if (zk.ie) {
				setTimeout(function () {wgt._resize(true)}, 500);
			}
		};
		opts1['message'] = 'Initialize Marker Manager';
		zk.gapi.waitUntil(wgt, opts1);
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
		var wgt = evt.target;

		//calling this to correct the popup submenu not auto closed issue
		zk.Widget.mimicMouseDown_(wgt);

		if (wgt.$instanceof(gmaps.Gmarker) || wgt.$instanceof(gmaps.Gpolyline) || wgt.$instanceof(gmaps.Gpolygon)) {
			var latLng = evt.latLng? evt.latLng : new google.maps.LatLng(0, 0),
				xy = this._mm.projection_.fromLatLngToDivPixel(latLng, this._gmaps.getZoom()),
				pageXY = gmaps.Gmaps.xyToPageXY(this.parent, xy.x, xy.y),
				data = zk.copy(evt.data, {lat:latLng.lat(),lng:latLng.lng(),reference:wgt,x:xy.x,y:xy.y,pageX:pageXY[0],pageY:pageXY[1]}),
				opts = evt.opts;
			this.fireX(new zk.Event(this, 'onMapClick', data, opts, evt.domEvent));
			// do not select self
			if (wgt != this)
				this.fireX(new zk.Event(this, 'onSelect', {items:[wgt],reference:wgt}, opts, evt.domEvent));
			this.$supers(gmaps.Gmaps, 'doClick_', arguments);
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
	
		// if this is not real visible, the move should caused by calling api,
		// and map will move to wrong position,
		// do not get center value from map, just keep default.
		if (this.isRealVisible()) {
			var c = maps.getCenter();
			this._center = [c.lat(), c.lng()];
		}
		this.fireX(new zk.Event(this, 'onMapMove', {lat:this._center[0],lng:this._center[1],swlat:sw.lat(),swlng:sw.lng(),nelat:ne.lat(),nelng:ne.lng()}, {}, null));
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
			info._infowindow.setPosition(new google.maps.LatLng(c[0], c[1]));
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
			if (shallRestoreCenter)
				google.maps.event.trigger(maps, 'resize');
			
			if (shallRestoreCenter) {
				//@see #_doMoveEnd
				var latLng = new google.maps.LatLng(this._center[0],this._center[1]);
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
		this._doubleclick = google.maps.event.addListener(maps, 'dblclick', function(event) {wgt.doDoubleClick_(event)});
		this._zoomend = google.maps.event.addListener(maps, 'zoom_changed', function() {wgt._doZoomEnd()});
		// TODO , vary hard
		this._maptypechanged = google.maps.event.addListener(maps, 'maptypeid_changed', function() {wgt._doMapTypeChanged()});
	},
	_clearListeners: function() {
		if (this._moveend ) {
			google.maps.event.removeListener(this._moveend);
			this._moveend = null;
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
		this._gmaps = maps;
		this.setNormal(this._normal, {force:true}) //prepare map types
			.setHybrid(this._hybrid, {force:true})
			.setSatellite(this._satellite, {force:true})
			.setPhysical(this._physical, {force:true})
			.setMapType(this._mapType, {force:true}) //set initial map type
			.setShowTypeCtrl(this._showTypeCtrl, {force:true})
			.setShowZoomCtrl(this._showZoomCtrl, {force:true})
			.setShowScaleCtrl(this._showScaleCtrl, {force:true})
			.setShowOverviewCtrl(this._showOverviewCtrl, {force:true})
			.setDoubleClickZoom(this._doubleClickZoom, {force:true})
			.setScrollWheelZoom(this._scrollWheelZoom, {force:true})
			.setEnableDragging(this._enableDragging, {force:true})
			.setCenter(this._center, {force:true})
			.setZoom(this._zoom, {force:true});

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
	},
	_clearGmaps: function() {
		this._clearListeners();
		this._gmaps = this._lctrl = this._sctrl = this._tctrl = this._cctrl = this._octrl = this._mm = this._mmLoaded = null;
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
  	errormsg: '<p>To use <code>&lt;gmaps&gt;</code>, you have to specify the following statement in your page:</p>'
		+'<code>&lt;script content="zk.googleAPIkey='+"'key-assigned-by-google'"+'" /></code>' 
});
//register to be called when window.onunload. 
//jq(function(...)) tells to do this until html document is ready.
jq(function() {jq(window).unload(function(){/** 
 	* Issue 9: Javascript Error: GUnload is not defined
 	* TODO unload maps if unload API avaliable*/})});
})();