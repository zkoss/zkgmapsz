/* gmaps.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Oct 12 14:29:16     2006, Created by henrichen
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
/* Due to the Gmaps limitation that we cannot create script element dynamically
	So, it is user's job to declare <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=xxx"
	in a page that is not dynamically loaded.
zk.load(
	"http://maps.google.com/maps?file=api&v=2&key="+getZKAttr($e("zk_gmapsKey"),"key"),
	null,
	function () {
		return typeof GMap2 != "undefined";
	});
*/
//prototype backward compatible
if (!window.zPos)
	window.zPos = window.Position;


zk.load("ext.gmaps.markermanager");

function Gmaps_newMarkerManager(map, opt_opts) {
	//20080215, Henri Chen: When drag a gmarker outside,
	//the auto tracking of the MarkerManager is not good.
	//which will remove then add which cause the lost of the mouse
	//capture and the map will keep moving.
	var me = new MarkerManager(map, opt_opts);
	me.removeOverlay_ = function(marker) {
		if (!marker._oldcenter) { //not under dragging
			var elm = marker._elm;
			if (elm) {
				if (elm._dg)
					zkau.cleandrag(elm); //will clean elm._dg
				if (elm._dp) {
					if (zkau.cleandrop)
						zkau.cleandrop(elm._dp);
					elm._dp = null;
				}
			}
			//bug #2800271: ginfo dissapears when zoom level changed in gmap. 
			//MarkerManager detach the marker then attach back when gmaps 
			//zooming or moving that is the reason why the ginfo disappear
			var mp = $outer(map.getContainer());
			mp._zooming = true;
			
			me.map_.removeOverlay(marker);
			me.shownMarkers_--;
			marker._attach = null;
			marker._gmaps = null;
			marker._area = null;
			marker._gmimap = null;
		}
	};
	me.addOverlay_ = function(marker) {
		if (!marker._oldcenter) { //not under dragging
			var gmaps = me.map_;
			gmaps.addOverlay(marker);
			var pane1 = gmaps.getPane(G_MAP_MARKER_MOUSE_TARGET_PANE);
			//var pane2 = gmaps.getPane(G_MAP_MARKER_PANE);
			me.shownMarkers_++;
			marker._attach = true;
			marker._gmaps = gmaps;
			marker._area = pane1.lastChild;
			//20090515, Henri Chen: Tricky! remember "gmimap" in pane1
			//so later we can find the gmimap's index in pane1 and retrieve 
			//the real <img/> element in pane2 when doing initdrag
			marker._gmimap = pane1.lastChild; //<map id="gmimapX"/> in pane1 
			if (!marker._area.id || !marker._area.id.startsWith("mtgt_unnamed_"))
				marker._area = marker._area.firstChild 
			
			//handle drop
			var elm = marker._elm;
			var drop = getZKAttr(elm, "drop");
			if (drop) {
				Gmarker_doinitdrop(marker, elm, drop);
			}

			//check if the current info is the marker
			var mp = $outer(gmaps.getContainer());
			if (marker._open || mp._curInfo == elm) {
				marker._open = null;
				Gmaps_openInfo($outer(gmaps.getContainer()), elm);
			}
		}
	};

	return me;
}

function Goverlay_addListeners(overlay) {
	overlay._mouseover = GEvent.addListener(overlay, "mouseover", Goverlay_onmouseover);
	overlay._mouseout = GEvent.addListener(overlay, "mouseout", Goverlay_onmouseout);
}

function Goverlay_removeListeners(overlay) {
	if (overlay._mouseover) {
		GEvent.removeListener(overlay._mouseover);
		overlay._mouseover = null;
	}
	if (overlay._mouseout) {
		GEvent.removeListener(overlay._mouseout);
		overlay._mouseout = null;
	}
}

////
zkGmaps = {};

function Gscr_tospoint(x) {
	var ix;
	var xu;
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
}
function Gscr_topoint(x, y) {
	var xp = Gscr_tospoint(x);
	var yp = Gscr_tospoint(y);
	return new GScreenPoint(xp[0], yp[0], xp[1], yp[1]);
}
function Gscr_tosize(w, h) {
	var wp = Gscr_tospoint(w);
	var hp = Gscr_tospoint(h);
	return new GScreenSize(wp[0], hp[0], wp[1], hp[1]);
}

function Gmaps_newGScr(gmaps, elm) {
	var sxy = Gscr_topoint(getZKAttr(elm, "sx"), getZKAttr(elm, "sy"));
	var oxy = Gscr_topoint(getZKAttr(elm, "ox"), getZKAttr(elm, "oy"));
	var sz = Gscr_tosize(getZKAttr(elm, "w"), getZKAttr(elm, "h"));
	var src = getZKAttr(elm, "src");
	var gscr = new GScreenOverlay(src, sxy, oxy, sz);
	gmaps.addOverlay(gscr);
	gscr._elm = elm;
	elm._gscr = gscr;
}

function Gmaps_newGGnd(gmaps, elm) {
	var lat = getZKAttr(elm, "swlat");
	var lng = getZKAttr(elm, "swlng");
	var sw = new GLatLng(lat, lng);
	lat = getZKAttr(elm, "nelat");
	lng = getZKAttr(elm, "nelng");
	var ne = new GLatLng(lat, lng);
	var src = getZKAttr(elm, "src");
	var bound = new GLatLngBounds(sw, ne);
	var ggnd = new GGroundOverlay(src, bound);
	gmaps.addOverlay(ggnd);
	ggnd._elm = elm;
	elm._ggnd = ggnd;
}

function Gmaps_newGGon(gmaps, elm) {
	var opt = new Array();
	var color = getZKAttr(elm, "cr");
	opt["color"] = color;
	//#bug #2693527, polygon disapper after zoom in
	//sideeffect weight must be int type
	var weight = parseInt(getZKAttr(elm, "wg"));
	opt["weight"] = weight;
	var lopacity = parseFloat(getZKAttr(elm, "lop"));
	opt["opacity"] = lopacity;
	var points = getZKAttr(elm, "pts");
	opt["points"] = points
	var levels = getZKAttr(elm, "lvs");
	opt["levels"] = levels;
	var zoomFactor = parseInt(getZKAttr(elm, "zf"));
	opt["zoomFactor"] = zoomFactor;
	var numLevels = parseInt(getZKAttr(elm, "nlvs"));
	opt["numLevels"] = numLevels;

	var polys = new Array(1);
	polys[0] = opt;
	
	var args = new Array();
	args["polylines"] = polys;
	var fill = (getZKAttr(elm, "fl") == "true");
	args["fill"] = fill;
	var fillColor = getZKAttr(elm, "fcr");
	args["color"] = fillColor;
	var opacity = parseFloat(getZKAttr(elm, "op"));
	args["opacity"] = opacity;
	var outline = (getZKAttr(elm, "ol") == "true");
	args["outline"] = outline;
	
	var ggon = GPolygon.fromEncoded(args);
	gmaps.addOverlay(ggon);
	
	ggon._elm = elm;
	elm._ggon = ggon;
	
	Goverlay_addListeners(ggon);
}

function Gmaps_newGPoly(gmaps, elm) {
	var opt = new Array();
	var color = getZKAttr(elm, "cr");
	opt["color"] = color;
	//#bug #2693527, polygon disapper after zoom in
	//sideeffect weight must be int type
	var weight = parseInt(getZKAttr(elm, "wg"));
	opt["weight"] = weight;
	var opacity = parseFloat(getZKAttr(elm, "lop"));
	opt["opacity"] = opacity;
	var points = getZKAttr(elm, "pts");
	opt["points"] = points
	var levels = getZKAttr(elm, "lvs");
	opt["levels"] = levels;
	var zoomFactor = parseInt(getZKAttr(elm, "zf"));
	opt["zoomFactor"] = zoomFactor;
	var numLevels = parseInt(getZKAttr(elm, "nlvs"));
	opt["numLevels"] = numLevels;

	var gpoly = new GPolyline.fromEncoded(opt);
	gmaps.addOverlay(gpoly);
	gpoly._elm = elm;
	elm._gpoly = gpoly;
	
	Goverlay_addListeners(gpoly);
}

function Gmaps_newGMarker(gmaps, elm) {
	var gicon = new GIcon(G_DEFAULT_ICON);
	var iimg = getZKAttr(elm, "iimg");
	if (iimg) {
		gicon.image = iimg;
	}
	var isdw = getZKAttr(elm, "isdw");
	if (isdw) {
		gicon.shadow = isdw;
	}
	var isz = Gmaps_parseIntArray(getZKAttr(elm, "isz"));
	if (isz) {
		gicon.iconSize = new GSize(isz[0], isz[1]);
	}
	var isdwsz = Gmaps_parseIntArray(getZKAttr(elm, "isdwsz"));
	if (isdwsz) {
		gicon.shadowSize = new GSize(isdwsz[0], isdwsz[1]);
	}
	var ianch = Gmaps_parseIntArray(getZKAttr(elm, "ianch"));
	if (ianch) {
		gicon.iconAnchor = new GPoint(ianch[0], ianch[1]);
	}
	var iinfanch = Gmaps_parseIntArray(getZKAttr(elm, "iinfanch"));
	if (iinfanch) {
		gicon.infoWindowAnchor = new GPoint(iinfanch[0], iinfanch[1]);
	}
	var iprtimg = getZKAttr(elm, "iprtimg");
	if (iprtimg) {
		gicon.printImage = iprtimg;
	}
	var imozprtimg = getZKAttr(elm, "imozprtimg");
	if (imozprtimg) {
		gicon.mozPrintImage = imozprtimg;
	}
	var iprtsdw = getZKAttr(elm, "iprtsdw");
	if (iprtsdw) {
		gicon.printShadow = iprtsdw;
	}
	var itrpt = getZKAttr(elm, "itrpt");
	if (itrpt) {
		gicon.transparent = itrpt;
	}
	var iimgmap = getZKAttr(elm, "iimgmap");
		
	var imaxhgt = getZKAttr(elm, "imaxhgt");
	if (imaxhgt) {
		gicon.maxHeight = parseInt(imaxhgt);
	}
	var idrgcrsimg = getZKAttr(elm, "idrgcrsimg");
	if (idrgcrsimg) {
		gicon.dragCrossImage = idrgcrsimg;
	}
	var idrgcrssz = Gmaps_parseIntArray(getZKAttr(elm, "idrgcrssz"));
	if (idrgcrssz) {
		gicon.dragCrossSize = new GSize(idrgcrssz[0], idrgcrssz[1]);
	}
	var idrgcrsanch = Gmaps_parseIntArray(getZKAttr(elm, "idrgcrsanch"));
	if (idrgcrsanch) {
		gicon.dragCrossAnchor = new GPoint(idrgcrsanch[0], idrgcrsanch[1]);
	}
	var anch = Gmaps_getLatLng(getZKAttr(elm, "anch"));
	var opt = new Array();
	opt["icon"] = gicon;
	opt["draggable"] = true;
	
	var title = elm.getAttribute("title");
	if (title) {
		opt["title"] = title;
	}
	var gmark = new GMarker(new GLatLng(anch[0], anch[1]), opt);
	gmark._elm = elm;
	elm._gmark = gmark;
	
//20080214, Henri Chen: support maxzoom and minzoom
//	gmaps.addOverlay(gmark);
	var maxz = getZKAttr(elm, "maxz");
	var minz = getZKAttr(elm, "minz");
	var mp = $outer(gmaps.getContainer());
	mp._mgr.addMarker(gmark, parseInt(minz), parseInt(maxz));

	//handle dragging and draggingEnable	
	var dgen = getZKAttr(elm, "dgen");
	zkGmark.setAttr(elm, "z.dgen", dgen); //allow initdrag

	Gmarker_addListeners(gmark);
}

function Gmarker_doinitdrop(gmark, elm, drop) {
	if (gmark._area) {
		var area = gmark._area; 
		setZKAttr(area, "zid", elm.id);
		setZKAttr(area, "drop", drop);
		elm._dp = area;
		if (zkau.initdrop)
			zkau.initdrop(area);
	}
}

function Gmarker_onmousedown() {
	var gmark = this;
	var elm = this._elm;
	if (elm._dg) {
		var mp = $e(getZKAttr(elm, "pid"));
		var latlng = gmark.getLatLng();
		var prxy = Gmaps_projectxy(latlng, mp, mp._gmaps);
		//let bouble up, so drag-drop can handle it
		Gmaps_fireevent("mousedown", elm._dg, prxy, 0);
	} 
}

function Gmarker_addListeners(gmark) {
	gmark._dragstart = GEvent.addListener(gmark, "dragstart", Gmarker_ondrag);
	gmark._dragend = GEvent.addListener(gmark, "dragend", Gmarker_ondrop);
	gmark._dblclick = GEvent.addListener(gmark, "dblclick", Gmarker_ondblclick);
	gmark._dragstart = GEvent.addListener(gmark, "dragstart", Gmarker_ondrag);
	gmark._mousedown = GEvent.addListener(gmark, "mousedown", Gmarker_onmousedown);
	Goverlay_addListeners(gmark);
}

function Gmarker_removeListeners(gmark) {
	if (gmark._dragstart) {
		GEvent.removeListener(gmark._dragstart);
		gmark._dragstart = null;
	}
	if (gmark._dragend) {
		GEvent.removeListener(gmark._dragend);
		gmark._dragend = null;
	}
	if (gmark._dblclick) {
		GEvent.removeListener(gmark._dblclick);
		gmark._dblclick = null;
	}
	if (gmark._mousedown) {
		GEvent.removeListener(gmark._mousedown);
		gmark._mousedown = null;
	}
	Goverlay_removeListeners(gmark);
}

function Goverlay_onmouseover() {
	if (this._elm) { //an overlay
		var elm = this._elm;
		var mp = $e(getZKAttr(elm, "pid"));
		mp._overlay = elm;
		
		// a gmark (late binding initdrag)
		if (elm._gmark) {
			// not init for drag yet
			if (!elm._dg) { 
				var dgen = getZKAttr(elm, "dgen");
				if (!dgen) {
					var drag = getZKAttr(elm, "drag");
					if (drag) {
						elm._dgok = true; //ok to initdrag
						zkau.initdrag(elm);
					}
				}
			}
		}
	} else {
		var mp = $outer(this.getContainer());
		mp._overlay = null;
	}
}

function Goverlay_onmouseout() {
	if (this._elm) { //an overlay
		var elm = this._elm;
		var mp = $e(getZKAttr(elm, "pid"));
		mp._overlay = null;
	} else {
		var mp = $outer(this.getContainer());
		mp._overlay = null;
	}
}

function Gmaps_parseIntArray(n) {
	if (!n) return null;
	var a = n.split(",");
	for (var j = 0; j < a.length; ++j) {
		a[j] = parseInt(a[j]);
	}
	return a;
}
		
/** get expected center from server */
function Gmaps_getLatLng(n) {
	if (!n) return null;
	var a = n.split(",");
	var len = a.length;
	var lat = parseFloat(a[0]);
	var lng = parseFloat(a[1]);
	if (len < 3) {
		return [lat, lng];
	}
	var zoom = parseInt(a[2]);
	if (len < 4) {
		return [lat, lng, zoom];
	}
	return [lat, lng, zoom, a[3]];
}

/** open the specified info window */
function Gmaps_openInfo(mp, elm) {
	var gmaps = mp._gmaps;
	var zktype = getZKAttr(elm, "type");
    if (zktype.lastIndexOf("mark") >= 0) { //gmapsz.gmaps.Gmark
    	//this gmarker has not addOverlay yet
    	//force gmarker in range to make it addOverlay since user request 
    	//to open infowindow
    	var gmark = elm._gmark;
		if (!gmark._attach) {
			var maxz = parseInt(getZKAttr(elm, "maxz"));
			var minz = parseInt(getZKAttr(elm, "minz"));
			var center = elm._gmark.getLatLng();
			var z = gmaps.getZoom();
			if (z > maxz) {
				gmaps.setZoom(maxz);
			} else if (z < minz) {
				gmaps.setZoom(minz);
			}
			gmaps.setCenter(center);
			gmark._open = true;
			return;
		}
	}

	var nelm = elm.cloneNode(true);
	nelm.id = elm.id+"!real";
	var infowintab = new GInfoWindowTab("tab1", nelm);
	var tabs = new Array(1);
	tabs[0] = infowintab;
	tabs._elm = elm;
	elm._tabs = tabs;
	
	mp._opening = true;
    if (zktype.lastIndexOf("mark") >= 0) { //gmapsz.gmaps.Gmark
		elm._gmark.openInfoWindowTabs(tabs);
	} else {
        var anch = Gmaps_getLatLng(getZKAttr(elm, "anch"));
		gmaps.openInfoWindowTabs(new GLatLng(anch[0], anch[1]), tabs);
	}
}

/** close the specified info window */
function Gmaps_closeInfo(mp) {
	mp._closing = true;
    mp._gmaps.closeInfoWindow();
}

/** onMarkerDrag event handler */
function Gmarker_ondrag() {
	//20080215, Henri Chen: reset the position of the gmark for the MarkerManager
	var gmark = this;
	gmark._oldcenter = gmark.getLatLng();
}

/** onMarkerDrop event handler */
function Gmarker_ondrop() {
	var gmark = this;
	var elm = gmark._elm;
	var mp = $e(getZKAttr(elm, "pid"));
	
	//20080215, Henri Chen: reset the position of the gmark for the 
	//MarkerManager to remove from it from cache.
	var mgr = mp._mgr;
	var center = gmark.getLatLng();
	var oldcenter = gmark._oldcenter;
	gmark.setLatLng(oldcenter);
	//must clean _oldcenter to allow removeOverlay of the gmarker
	gmark._oldcenter = null; 
	mgr.removeMarker(gmark);

	//move it back and add into MarkerManager
	gmark.setLatLng(center);
	var maxz = getZKAttr(elm, "maxz");
	var minz = getZKAttr(elm, "minz");
	mgr.addMarker(gmark, minz, maxz);
	
	//20080708, Henri Chen: sometimes after dragging, the marker could disappear.
	//we found move the map will make it show again, so here we force a moveend
	//event
	GEvent.trigger(mp._gmaps, "moveend"); 
	
	var lat = center.lat();
	var lng = center.lng();
	var uuid = elm.id;
	zkau.send({uuid: uuid, cmd: "onMarkerDrop", data: [lat, lng]}, zkau.asapTimeout(elm, "onMarkerDrop"));
}

function Gmarker_ondblclick() {
	var gmark = this;
	var elm = gmark._elm;
	var mp = $e(getZKAttr(elm, "pid"));
	Gmaps_dodblclick(mp, gmark, null);
}	

function Gmaps_initCenter(gmaps) {
	var mp = $outer(gmaps.getContainer());
	var uuid = mp.id;
	var center = gmaps.getCenter();
	var lat = center.lat();
	var lng = center.lng();
	mp._lat = lat;
	mp._lng = lng;
}

/** Called to update the current center and bounds to server */
function Gmaps_dobounds(gmaps, asap) {
	var mp = $outer(gmaps.getContainer());
	var uuid = mp.id;
	var center = gmaps.getCenter();
	var lat = center.lat();
	var lng = center.lng();
	mp._lat = lat;
	mp._lng = lng;
	var bounds = gmaps.getBounds();
	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	var swlat = sw.lat();
	var swlng = sw.lng();
	var nelat = ne.lat();
	var nelng = ne.lng();
	zkau.send({uuid: uuid, cmd: "onMapMove", data: [lat, lng, swlat, swlng, nelat, nelng]}, asap ? 38 : zkau.asapTimeout(mp, "onMapMove"));
}

function Gmaps_onmovestart() {
	var mp = $outer(this.getContainer());
	zkau.closeFloatsOnFocus(mp);
}

/** onMapMove event handler */
function Gmaps_onmove() {
	var gmaps = this;
	var mp = $outer(gmaps.getContainer());
	if (!mp._resizing)
		Gmaps_dobounds(this, false);
	mp._resizing = false;
}

/** onMapZoom event handler */
function Gmaps_onzoom() {
	var gmaps = this;
	var mp = $outer(gmaps.getContainer());
	var uuid = mp.id;
	var zoom = gmaps.getZoom();
	zkau.send({uuid: uuid, cmd: "onMapZoom", data: [zoom]}, zkau.asapTimeout(mp, "onMapZoom"));
}

/** onMapTypeChange event handler */
function Gmaps_onmaptypechange() {
	var gmaps = this;
	var type = gmaps.getCurrentMapType();
	if (type) {
		switch (type) {
		default:
		case G_NORMAL_MAP:
			type = "normal";
			break;
		case G_SATELLITE_MAP:
			type = "satellite";
			break;
		case G_HYBRID_MAP:
			type = "hybrid";
			break;
		case G_PHYSICAL_MAP:
			type = "physical";
			break;
		}
		var mp = $outer(gmaps.getContainer());
		var uuid = mp.id;
		zkau.send({uuid: uuid, cmd: "onMapTypeChange", data: [type]}, zkau.asapTimeout(mp, "onMapTypeChange"));
	}
}

/** when GInfoWindow of the Google Maps is opened */
function Gmaps_oninfoopen() {
	var gmaps = this;
	var mp = $outer(gmaps.getContainer());
	var opening = gmaps.getInfoWindow().getTabs()._elm;

//	var elm = opening;
//	var uuid = mp.id;
//	zkau.send({uuid: uuid, cmd: "onInfoChange", data: [elm.id]}, 350);
	mp._curInfo = opening;
	mp._opening = false;
	mp._closing = false;
	mp._zooming = false; 

}

/** when GInfoWindow of the Google Maps is closed */
function Gmaps_oninfoclose() {
	var gmaps = this;
	var mp = $outer(gmaps.getContainer());
	
	if (!mp._opening && !mp._closing && !mp._zooming) {
	    var uuid = mp.id;
		zkau.send({uuid: uuid, cmd: "onInfoChange", data: null}, 350);
		mp._curInfo = null;
	}
	mp._closing = false;
	mp._zooming = false;
}

function Gmaps_projectxy(latlng, mp, gmaps) {
	var proj = G_NORMAL_MAP.getProjection();
	var zoom = gmaps.getZoom();
    var xy = proj.fromLatLngToPixel(latlng, zoom);
    var x = xy.x;
    var y = xy.y;
    
	var bounds = gmaps.getBounds();
	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	var swlng = sw.lng();
	var nelat = ne.lat();
    xy = proj.fromLatLngToPixel(new GLatLng(nelat, swlng), zoom);
    x = x - xy.x;
    y = y - xy.y;
    
	var clientxy = Gmaps_clientxy(x, y, mp);
	return [x,y,clientxy[0],clientxy[1],clientxy[2],clientxy[3]];
}


function Gmaps_fireevent(nm, elm, prxy, btn) {  
	if (document.createEventObject){  
		// dispatch for IE  
		var evt = document.createEventObject();
		evt.button = btn == 0 ? 1 : btn == 1 ? 4 : 2;
		evt.clientX = prxy[4];
		evt.clientY = prxy[5];
		evt.x = prxy[4];
		evt.y = prxy[5];
		evt._go = true;
		elm.fireEvent('on'+nm, evt);  
	} else {  
		// dispatch for firefox + others  
		var evt = document.createEvent('MouseEvents');  
		evt.initMouseEvent(nm, true, true, window, 1, prxy[0], prxy[1], prxy[4], prxy[5], false, false, false, false, btn, null);
		evt._go = true;
		elm.dispatchEvent(evt);  
	}
}

function Gmaps_stopevent(evt) {
	if (!evt) var evt = window.event;
	//bug #2588638: Gmarker doesn't open link in content with Firefox
	var elm = evt.originalTarget;
	var ztype = getZKAttr(elm, "type");
	if (ztype && !evt._go) {
		Event.stop(evt);
	}
	return evt._go;
}

function Gmaps_zuuid(n) {
	for (; n; n = $parent(n))
		if (n.id && n.id.startsWith("z_")) {
			n = n.id;
			break;
		}
	if (!n) return "";
	var j = n.lastIndexOf('!');
	return j > 0 ? n.substring(0, j): n;
}

function Gmaps_zouter(cmp) {
	var id = Gmaps_zuuid(cmp);
	if (id) {
		var n = $e(id);
		if (n) return n;
	}
	return cmp;
}
	
function Gmaps_mouseevent(evt) {
	if (!evt) var evt = window.event;
	if (evt._go) {
		return true;
	}
	
	//fire a new event, evt2
	var nm = evt.type;
	if (document.createEventObject){
		var elm = Gmaps_zouter(evt.srcElement); //mp
		var pid = getZKAttr(elm, "pid");
		if (pid) elm = $e(pid);
		var mp = elm;
		var overlay = mp._overlay;
		if (overlay) {
			elm = overlay;
			mp._overlay = null;
		}
		
		// dispatch for IE  
		var evt2 = document.createEventObject(evt);

		//stop current event, evt
		Event.stop(evt);

		evt2._go = true;
		elm.fireEvent('on'+nm, evt2);
	} else {  
		var elm = this; //mp
		var overlay = elm._overlay;
		if (overlay) {
			elm = overlay;
		}
		// dispatch for firefox + others  
		var evt2 = document.createEvent('MouseEvents');  

		//stop current event, evt
		Event.stop(evt);

		evt2.initMouseEvent(nm, true, true, window, 1, evt.screenX, evt.screenY, evt.clientX, evt.clientY, evt.ctrlKey, evt.altKey, evt.shiftKey, evt.metaKey, evt.button, evt.relatedTarget);
		evt2._go = true;
		elm.dispatchEvent(evt2);
	}
	
	return false;	
}

//given clientX, clientY, return latlng
function Gmaps_clientLatLng(mp, gmaps) {
	var x = mp._xy[0];
	var y = mp._xy[1];
	var orgxy = zk.revisedOffset(mp);
	x = x - orgxy[0];
	y = y - orgxy[1];
	return Gmaps_projectlatlng(x, y, gmaps);
}

/** when Google Maps is clicked */
function Gmaps_onclick(overlay, latlng) {
	var gmaps = this;
	var mp = $outer(gmaps.getContainer());

	zkau.closeFloatsOnFocus(mp);

	var uuid = mp.id;
	if (!latlng) {
		latlng = Gmaps_clientLatLng(mp, gmaps);
	}

	var overlayid = overlay && overlay._elm ? overlay._elm.id : null;
	var prxy = Gmaps_projectxy(latlng, mp, gmaps);
	
	Gmaps_fireevent("click", overlayid ? overlay._elm : mp, prxy, 0);
	var keys = "";
	zkau.send({uuid: uuid, cmd: "onMapClick", data: [overlayid, latlng.lat(), latlng.lng(), prxy[0], prxy[1], prxy[2], prxy[3], keys]}, zkau.asapTimeout(mp, "onMapClick"));
	zkau.send({uuid: uuid, cmd: "onSelect", data: [overlayid, overlayid]}, zkau.asapTimeout(mp, "onSelect"));
}

function Gmaps_ondblclick(overlay, latlng) { //overlay is always null
	var mp = $outer(this.getContainer());
	Gmaps_dodblclick(mp, overlay, latlng);
}
	
function Gmaps_dodblclick(mp, overlay, latlng) { 
	zkau.closeFloatsOnFocus(mp);

	var uuid = mp.id;
	var gmaps = mp._gmaps;
	
	if (!latlng) {
		latlng = Gmaps_clientLatLng(mp, gmaps);
	}
	var overlayid = overlay && overlay._elm ? overlay._elm.id : null;
	var prxy = Gmaps_projectxy(latlng, mp, gmaps);
	var keys = "";
	zkau.send({uuid: uuid, cmd: "onMapDoubleClick", data: [overlayid, latlng.lat(), latlng.lng(), prxy[0], prxy[1], prxy[2], prxy[3], keys]}, zkau.asapTimeout(mp, "onMapDoubleClick"));
}

//compensate the scroll-top and scroll-left
function Gmaps_clientxy(x, y, mp) {
	var orgxy = zk.revisedOffset(mp);
	var scrollxy = zPos.realOffset(mp);
	var cx = x + orgxy[0];
	var cy = y + orgxy[1];
	return [cx, cy, cx - scrollxy[0], cy - scrollxy[1]];
}

function Gmaps_projectlatlng(x, y, gmaps) {
	var proj = G_NORMAL_MAP.getProjection();
	var zoom = gmaps.getZoom();
	var bounds = gmaps.getBounds();
	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	var swlng = sw.lng();
	var nelat = ne.lat();
    var xy = proj.fromLatLngToPixel(new GLatLng(nelat, swlng), zoom);
    var latx = x + xy.x;
    var lngy = y + xy.y;
    return proj.fromPixelToLatLng(new GPoint(latx,lngy), zoom);
}

function Gmaps_onsinglerightclick(point, elm, overlay) { //
	var gmaps = this;
	var mp = $outer(this.getContainer());
	zkau.closeFloatsOnFocus(mp);

	var uuid = mp.id;
	var overlayid = overlay && overlay._elm ? overlay._elm.id : null;
	
	var x = point.x;
	var y = point.y
	var clientxy = Gmaps_clientxy(x, y, mp);
	var latlng = Gmaps_projectlatlng(point.x, point.y, gmaps);
	
	//remember the context menu's lat, lng
	//#bug 2792012 onMapRightClick of gmapz doens't work
	if (overlay && overlay._elm) {
		overlay._elm._ctxlat = latlng.lat();
		overlay._elm._ctxlng = latlng.lng();
	} else {
		mp._ctxlat = latlng.lat();
		mp._ctxlng = latlng.lng();
	}
	
	//chain zkGmaps into zkMpop.context (for onOpen event on Gmaps)
	if (!zkGmaps._oldContext && typeof(zkMpop)!="undefined" && zkMpop.context) {
		zkGmaps._oldContext = zkMpop.context;
		zkMpop.context = zkGmaps._context;
	}
	
	Gmaps_fireevent("contextmenu", overlayid ? overlay._elm : mp, [x,y,clientxy[0],clientxy[1],clientxy[2],clientxy[3]], 2);

	var keys = "";
	zkau.send({uuid: uuid, cmd: "onMapRightClick", data: [overlayid, latlng.lat(), latlng.lng(), x, y, clientxy[0], clientxy[1], keys]}, zkau.asapTimeout(mp, "onMapRightClick"));
}

function Gmaps_mousemove(evt) {
	if (!evt) var evt = window.event;
	if (document.createEventObject){
		var elm = Gmaps_zouter(evt.srcElement); //mp
		var pid = getZKAttr(elm, "pid");
		if (pid) elm = $e(pid);
		elm._xy = [Event.pointerX(evt), Event.pointerY(evt)];
	} else {  
		this._xy = [Event.pointerX(evt), Event.pointerY(evt)];
	}
}

function Gmaps_addListeners(mp, gmaps) {
	gmaps._movestart = GEvent.addListener(gmaps, "movestart", Gmaps_onmovestart);
	gmaps._moveend = GEvent.addListener(gmaps, "moveend", Gmaps_onmove);
	gmaps._zoomend = GEvent.addListener(gmaps, "zoomend", Gmaps_onzoom);
	gmaps._infowindowclose = GEvent.addListener(gmaps, "infowindowclose", Gmaps_oninfoclose);
	gmaps._infowindowopen = GEvent.addListener(gmaps, "infowindowopen", Gmaps_oninfoopen);
	gmaps._click = GEvent.addListener(gmaps, "click", Gmaps_onclick);
	gmaps._dblclick = GEvent.addListener(gmaps, "dblclick", Gmaps_ondblclick);
	gmaps._singlerightclick = GEvent.addListener(gmaps, "singlerightclick", Gmaps_onsinglerightclick);
//	gmaps._addmarker = GEvent.addListener(gmaps, "addmarker", Gmaps_doaddmarker);
//	gmaps._removemarker = GEvent.addListener(gmaps, "removemarker", Gmaps_doremovemarker);
	gmaps._maptypechanged = GEvent.addListener(gmaps, "maptypechanged", Gmaps_onmaptypechange);
	zk.listen(mp, "click", Gmaps_stopevent);
	zk.listen(mp, "contextmenu", Gmaps_stopevent );
	zk.listen(mp, "mouseover", Gmaps_mouseevent );
	zk.listen(mp, "mouseout", Gmaps_mouseevent );
	zk.listen(mp, "mousemove", Gmaps_mousemove );
}

function Gmaps_removeListeners(mp, gmaps) {
	zk.unlisten(mp, "click", Gmaps_stopevent);
	zk.unlisten(mp, "contextmenu", Gmaps_stopevent );
	zk.unlisten(mp, "mouseover", Gmaps_mouseevent);
	zk.unlisten(mp, "mouseout", Gmaps_mouseevent);
	zk.unlisten(mp, "mousemove", Gmaps_mousemove);

	GEvent.removeListener(gmaps._movestart);
	gmaps._movestart = null;

	GEvent.removeListener(gmaps._moveend);
	gmaps._moveend = null;

	GEvent.removeListener(gmaps._zoomend);
	gmaps._zoomend = null;

	GEvent.removeListener(gmaps._infowindowclose);
	gmaps._infowindowclose = null;

	GEvent.removeListener(gmaps._infowindowopen);
	gmaps._infowindowopen = null;

	GEvent.removeListener(gmaps._click);
	gmaps._click = null;

	GEvent.removeListener(gmaps._dblclick);
	gmaps._dblclick = null;

	GEvent.removeListener(gmaps._singlerightclick);
	gmaps._singlerightclick = null;
	
	GEvent.removeListener(gmaps._maptypechanged);
	gmaps._maptypechanged = null;
	
//	GEvent.removeListener(gmaps._addmarker);
//	gmaps._addmarker = null;
	
//	GEvent.removeListener(gmaps._removemarker);
//	gmaps._removemarker = null;
}
	
/** Init */
zkGmaps.init = function (mp) {
	if (window.GMap2 == null) {
		$real(mp).innerHTML =
			'<p>To use <code>&lt;gmaps&gt;</code>, you have to specify the following statement in one of your page(s) that is not dynmically loaded:</p>'
			+'<code>&lt;script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=key-assigned-by-google" type="text/javascript"/&gt;</code>';
		return; //failed
	}

	if (GBrowserIsCompatible() && !mp._gmaps) {
		var gmaps = new GMap2($real(mp));
		mp._gmaps = gmaps;

		//init the GMap2
		var center = Gmaps_getLatLng(getZKAttr(mp, "init"));
		gmaps.setCenter(new GLatLng(center[0], center[1]), center[2]);

		//set map type
		var mapType = getZKAttr(mp, "mt");
		zkGmaps.setMapType(gmaps, mapType);
		
		//prepare map types
		zkGmaps.setAttr(mp, "z.nmap", getZKAttr(mp, "nmap"));
		zkGmaps.setAttr(mp, "z.smap", getZKAttr(mp, "smap"));
		zkGmaps.setAttr(mp, "z.hmap", getZKAttr(mp, "hmap"));
		zkGmaps.setAttr(mp, "z.pmap", getZKAttr(mp, "pmap"));
		
		//prepare controls
		if (center.length > 3) {
			var ctrls = center[3];
			if (ctrls.indexOf("s") >= 0) {
				zkGmaps.setAttr(mp, "z.sctrl", "true");
			}
			if (ctrls.indexOf("t") >= 0) {
				zkGmaps.setAttr(mp, "z.tctrl", "true");
			}
			if (ctrls.indexOf("l") >= 0) {
				zkGmaps.setAttr(mp, "z.lctrl", "true");
			}
			if (ctrls.indexOf("z") >= 0) {
				zkGmaps.setAttr(mp, "z.zctrl", "true");
			}
			if (ctrls.indexOf("c") >= 0) {
				zkGmaps.setAttr(mp, "z.cctrl", "true");
			}
			if (ctrls.indexOf("o") >= 0) {
				zkGmaps.setAttr(mp, "z.octrl", "true");
			}
		}
		
		//other configuration
		zkGmaps.setAttr(mp, "z.dg", getZKAttr(mp, "dg"));
		zkGmaps.setAttr(mp, "z.cz", getZKAttr(mp, "cz"));
		zkGmaps.setAttr(mp, "z.dz", getZKAttr(mp, "dz"));
		zkGmaps.setAttr(mp, "z.wz", getZKAttr(mp, "wz"));
		zkGmaps.setAttr(mp, "z.gb", getZKAttr(mp, "gb"));
		
		//prepare GInfoWindow, GMarker, GPolyline, GPolygon of the Google Maps
		mp._mgr = Gmaps_newMarkerManager(gmaps);
		var info = null;
		var cave = $e(mp.id + "!cave");
		for (j=0;j<cave.childNodes.length;j++) {
			var elm = cave.childNodes[j];
			if ($tag(elm) == "SPAN") {
				var zktype = getZKAttr(elm, "type");
				if (zktype.lastIndexOf("mark") >= 0) { //gmapsz.gmaps.Gmark
					Gmaps_newGMarker(gmaps, elm);
				} else if (zktype.lastIndexOf("poly") >= 0) { //gmapsz.gmaps.Gpoly
					Gmaps_newGPoly(gmaps, elm);
				} else if (zktype.lastIndexOf("gon") >= 0) { //gmapsz.gmaps.Ggon
					Gmaps_newGGon(gmaps, elm);
				} else if (zktype.lastIndexOf("gnd") >= 0) { //gmapsz.gmaps.Ggnd
					Gmaps_newGGnd(gmaps, elm);
				} else if (zktype.lastIndexOf("scr") >= 0) { //gmapsz.gmaps.Gscr
					Gmaps_newGScr(gmaps, elm);
				}
				if (getZKAttr(elm, "open") == "true") {
					info = elm;
				}
			}
		}

		//prepare the event listener
		Gmaps_addListeners(mp, gmaps);
		
		//register once the onunload and drag&drop handler
		if (!zkGmaps._reg) {
			zkGmaps._reg = true; //register once only
			
			//chain zkGmaps into onunload chain
			zkGmaps._oldDocUnload = window.onunload;
			window.onunload = zkGmaps._onDocUnload; //unable to use zk.listen
			
			//chain zkGmaps into zkau._sendDrop chain
			zkGmaps._oldSendDrop = zkau._sendDrop;
			zkau._sendDrop = zkGmaps._sendDrop;
		}

		Gmaps_initCenter(gmaps);

		if (info) {
			Gmaps_openInfo(mp, info);
		}
		mp._init = true;
	}
		//Note: HTML is rendered first (before gmaps.js), so we have to delay
		//until now
};
zkGmaps.cleanup = function (mp) {
	if (window.GMap2 != null) {
		Gmaps_removeListeners(mp, mp._gmaps);
		if (mp._mgr) {
			mp._mgr.clearMarkers();
			mp._mgr = null;
		}
		mp._gmaps = null;
		mp._curInfo = null;
		mp._lctrl = null;
		mp._sctrl = null;
		mp._zctrl = null;
		mp._tctrl = null;
		mp._cctrl = null;
		mp._octrl = null;
	}
};

/** Called by the server to set the attribute. */
zkGmaps.setAttr = function (mp, name, value) {
    var gmaps = mp._gmaps;
	switch (name) {
	case "z.center": 
		var center = Gmaps_getLatLng(value);
		gmaps.setCenter(new GLatLng(center[0], center[1]));
		return true;
	case "z.panTo":
		var panTo = Gmaps_getLatLng(value);
		gmaps.panTo(new GLatLng(panTo[0], panTo[1]));
		return true;
	case "z.zoom":
		gmaps.setZoom(parseInt(value));
		return true;
	case "z.lctrl":
		if (value == "true" && !mp._lctrl) {
			mp._lctrl = new GLargeMapControl();
			gmaps.addControl(mp._lctrl);
		} else if (value != "true" && mp._lctrl) {
			gmaps.removeControl(mp._lctrl);
			mp._lctrl = null;
		}
		return true;
	case "z.sctrl":			
		if (value == "true" && !mp._sctrl) {
			mp._sctrl = new GSmallMapControl();
			gmaps.addControl(mp._sctrl);
		} else if (value != "true" && mp._sctrl) {
			gmaps.removeControl(mp._sctrl);
			mp._sctrl = null;
		}
		return true;
	case "z.zctrl":			
		if (value == "true" && !mp._zctrl) {
			mp._zctrl = new GSmallZoomControl();
			gmaps.addControl(mp._zctrl);
		} else if (value != "true" && mp._zctrl) {
			gmaps.removeControl(mp._zctrl);
			mp._zctrl = null;
		}
		return true;
	case "z.tctrl":
		if (value == "true" && !mp._tctrl) {
			mp._tctrl = new GMapTypeControl();
			gmaps.addControl(mp._tctrl);
		} else if (value != "true" && mp._tctrl) {
			gmaps.removeControl(mp._tctrl);
			mp._tctrl = null;
		}
		return true;
	case "z.cctrl":
		if (value == "true" && !mp._cctrl) {
			mp._cctrl = new GScaleControl();
			gmaps.addControl(mp._cctrl);
		} else if (value != "true" && mp._cctrl) {
			gmaps.removeControl(mp._cctrl);
			mp._cctrl = null;
		}
		return true;
	case "z.octrl":
		if (value == "true" && !mp._octrl) {
			mp._octrl = new GOverviewMapControl();
			gmaps.addControl(mp._octrl);
		} else if (value != "true" && mp._octrl) {
			gmaps.removeControl(mp._octrl);
			mp._octrl = null;
		}
		return true;
    case "z.open":
        var elm = $e(value);
        Gmaps_openInfo(mp, elm);
        return true;
    case "z.close":
    	Gmaps_closeInfo(mp);
        return true;
    case "z.mt":
    	zkGmaps.setMapType(gmaps, value);
    	return true;
    case "z.nmap":
    case "z.smap":
    case "z.hmap":
    case "z.pmap":
		if (value == "true")
			zkGmaps.addMapType(gmaps, name);
		else
			zkGmaps.removeMapType(gmaps, name);
    	return true;
	case "z.dg":
		if (value == "true")
			gmaps.enableDragging();
		else
			gmaps.disableDragging();
		return true;
	case "z.cz":
		if (value == "true")
			gmaps.enableContinuousZoom();
		else
			gmaps.disableContinuousZoom();
		return true;
	case "z.dz":
		if (value == "true")
			gmaps.enableDoubleClickZoom();
		else
			gmaps.disableDoubleClickZoom();
		return true;
	case "z.wz":
		if (value == "true")
			gmaps.enableScrollWheelZoom();
		else
			gmaps.disableScrollWheelZoom();
		return true;
	case "z.gb":
		if (value == "true")
			gmaps.enableGoogleBar();
		else
			gmaps.disableGoogleBar();
		return true;
	case "z.model":
		if (value == "true")
			Gmaps_dobounds(gmaps, false);
		return false;
	}
	return false;
};

/** Handles zkau._sendDrop */
zkGmaps._sendDrop = function (dragged, dropped, x, y, keys) {
	var j = dragged.lastIndexOf("!dg"); //if dragging a gmarker
	if (j >= 0) dragged = dragged.substring(0, j); 
	if (dropped.startsWith("mtgt_unnamed_")) { //tricky! if drop on a gmarker
		dropped = getZKAttr($e(dropped), "zid"); //see Gmarker_doinitdrop
	}
	var elm = $e(dropped);
	if (getZKAttr(elm, "type").lastIndexOf("Gmaps") >= 0) { //Gmaps
		var clientx = parseInt(x);
		var clienty = parseInt(y);
		var orgxy = zk.revisedOffset(elm);
		var x = clientx - orgxy[0];
		var y = clienty - orgxy[1];
		var latlng = Gmaps_projectlatlng(x, y, elm._gmaps);
		zkau.send({uuid: dropped, cmd: "onMapDrop", data: [dragged, latlng.lat(), latlng.lng(), x, y, clientx, clienty, keys]});
	} else if (getZKAttr(elm, "type").lastIndexOf("Gmark") >= 0) { //Gmark
		var clientx = parseInt(x);
		var clienty = parseInt(y);
		var orgxy = zk.revisedOffset(elm);
		var x = clientx - orgxy[0];
		var y = clienty - orgxy[1];
		var mp = $e(getZKAttr(elm, "pid"));
		var latlng = Gmaps_projectlatlng(x, y, mp._gmaps);
		zkau.send({uuid: dropped, cmd: "onMapDrop", data: [dragged, latlng.lat(), latlng.lng(), x, y, clientx, clienty, keys]});
	} else
		zkGmaps._oldSendDrop(dragged, dropped, x, y, keys);
};

/** Handles document.unload. */
zkGmaps._onDocUnload = function () {
	if (window.GMap2 != null) {
		GUnload();
		zkGmaps._reg = null;
		if (zkGmaps._oldDocUnload) zkGmaps._oldDocUnload.apply(document);
	}
};

/** Handles zkMpop.context. */
zkGmaps._context = function(ctx, ref) {
	if (typeof(ref)!="undefined" && ref._ctxlat) { //Gmaps related contextmenu event
		if (!$visible(ctx)) {
			zkMenu._open(ctx, true);
			if (zkau.asap(ctx, "onOpen")) //backward compatible
				zkau.send({uuid: ctx.id, cmd: "onOpen",
					data: ref ? [true, ref.id]: [true]});
			zkau.send({uuid: ctx.id, cmd: "onMapOpen",
					data: [true, ref.id, null, ref._ctxlat, ref._ctxlng]});
		}
	} else
		zkGmaps._oldContext(ctx, ref);
};

/** Handles invisible -> visible and resize */
zkGmaps.onSize = zkGmaps.onVisi = function (mp) {
	var gmaps = mp._gmaps; 
	if (gmaps && gmaps.isLoaded() && mp._init && zk.isRealVisible(mp)) {
		//bug 2099729: mp's height will not resize automatically 
		if (zk.ie) { 
		    var hgh = mp.style.height;
		    if (hgh.indexOf('%') >= 0) {
		    	mp.style.height="";
		    	mp.style.height=hgh;
		    }
		}
		mp._gmaps.checkResize();//will trigger onmove
		mp._gmaps.setCenter(new GLatLng(mp._lat, mp._lng));
		mp._resizing = false; //enable send center to server
	} else {
		mp._resizing = true; //disable send center to server
	}
};

/** set map type. */
zkGmaps.setMapType = function (gmaps, mapType) {
	if (mapType) {
		switch (mapType) {
		default:
		case "normal":
			gmaps.setMapType(G_NORMAL_MAP);
			break;
		case "satellite":
			if (G_SATELLITE_MAP) //china has no satellite map
				gmaps.setMapType(G_SATELLITE_MAP);
			else
				gmaps.setMapType(G_NORMAL_MAP);
			break;
		case "hybrid":
			if (G_HYBRID_MAP)
				gmaps.setMapType(G_HYBRID_MAP);
			else 
				gmaps.setMapType(G_NORMAL_MAP);
			break;
		case "physical":
			if (G_PHYSICAL_MAP)
				gmaps.setMapType(G_PHYSICAL_MAP);
			else
				gmaps.setMapType(G_NORMAL_MAP);
			break;
		}
	}
};

/** add map type. */
zkGmaps.addMapType = function (gmaps, mapType) {
	if (mapType) {
		switch (mapType) {
		case "z.nmap":
			gmaps.addMapType(G_NORMAL_MAP);
			break;
		case "z.smap":
			gmaps.addMapType(G_SATELLITE_MAP);
			break;
		case "z.hmap":
			gmaps.addMapType(G_HYBRID_MAP);
			break;
		case "z.pmap":
			gmaps.addMapType(G_PHYSICAL_MAP);
			break;
		}
	}
};

/** remove map type. */
zkGmaps.removeMapType = function (gmaps, mapType) {
	if (mapType) {
		switch (mapType) {
		case "z.nmap":
			gmaps.removeMapType(G_NORMAL_MAP);
			break;
		case "z.smap":
			gmaps.removeMapType(G_SATELLITE_MAP);
			break;
		case "z.hmap":
			gmaps.removeMapType(G_HYBRID_MAP);
			break;
		case "z.pmap":
			gmaps.removeMapType(G_PHYSICAL_MAP);
			break;
		}
	}
};

////
zkGinfo = {};

/** Init */
zkGinfo.init = function (elm) {
	var mp = $e(getZKAttr(elm, "pid"));
	//when Ginfo.setContent(), elm is recreated
	//always take out the display:none
	elm.style.display = "";
	if (mp._gmaps && !elm._tabs) { //add or recreate
		if (getZKAttr(elm, "open") == "true") {
			Gmaps_openInfo(mp, elm);
		}
	}
	//when page loading, the zkGinfo.init is called before zkGmaps.init
	//the gmaps is not ready yet, so we cannot openInfoWindow here
};

/** Called by the server to set the attribute. */
zkGinfo.setAttr = function (elm, name, value) {
	var mp = $e(getZKAttr(elm, "pid"));
	switch (name) {
	case "z.anch":
		setZKAttr(elm, "anch", value);
		if (mp._curInfo == elm) {
			Gmaps_openInfo(mp, elm);
		}
		return true;
	case "z.content":
		var relm = $e(elm.id + "!real");
		if (relm)
			zk.setInnerHTML(relm, value);
		zk.setInnerHTML(elm, value);
		return true;
	}
		
	return false;
};

/** Called by the server to remove self. */
zkGinfo.cleanup = function (elm) {
	var mp = $e(getZKAttr(elm, "pid"));
	if (mp._curInfo == elm && mp._gmaps) { //currently opened info, close it first
		Gmaps_closeInfo(mp);
	}
	elm._tabs = null;
};

////
zkGmark = {};

/** Init */
zkGmark.init = function (elm) {
	//always take out the display:none
	elm.style.display = "";

	var mp = $e(getZKAttr(elm, "pid"));
	if (mp._gmaps && !elm._gmark) { //Google Maps is ready
		Gmaps_newGMarker(mp._gmaps, elm);
		if (getZKAttr(elm, "open") == "true") {
			Gmaps_openInfo(mp, elm);
		}
	}
	//when page loading, the zkGmark.init is called before zkGmaps.init
	//the gmaps is not ready yet, so we cannot addOverlay here.
};

/** Called by the server to remove self. */
zkGmark.cleanup = function (elm) {

	var mp = $e(getZKAttr(elm, "pid"));
	if (elm._gmark) {
		var gmark = elm._gmark;
		if (gmark) {
			if (elm._dp) {
    			if (zkau.cleandrop)
    				zkau.cleandrop(elm._dp);
				elm._dp = null;
			}
			Gmarker_removeListeners(gmark);
			if (mp._curInfo == elm) { //currently opened info, close it first
				Gmaps_closeInfo(mp);
			}
			if (mp._gmaps)
				//20080214, Henri Chen: support maxzoom, minzoom
				//mp._gmaps.removeOverlay(gmark);
				mp._mgr.removeMarker(gmark);
			elm._gmark = null;
		}
	}
};

/** Called by the server to set the attribute. */
zkGmark.setAttr = function (elm, name, value) {
	var gmark = elm._gmark;
	switch (name) {
	case "z.anch":
		setZKAttr(elm, "anch", value);
		var mp = $e(getZKAttr(elm, "pid"));
		var anch = Gmaps_getLatLng(value);
		gmark.setLatLng(new GLatLng(anch[0], anch[1]));
		
		if (mp._curInfo == elm) {
			Gmaps_openInfo(mp, elm);
		}

		return true;
	case "z.content":
		var relm = $e(elm.id + "!real");
		if (relm)
			zk.setInnerHTML(relm, value);
		zk.setInnerHTML(elm, value);
		return true;
    case "z.iimg":
    	gmark.setImage(value);
    	return true;
    case "z.dgen":
    	if (value) {
    		gmark.enableDragging();
    		zkau.setAttr(elm, name, value);
	    	if (getZKAttr(elm, "drag"))
    			zkau.cleandrag(elm);
    	} else {
    		gmark.disableDragging();
    		zkau.rmAttr(elm, name);
//20080709, Henri Chen: Cannot initdrag here, gmarker is not fully ready yet.
//see Goverlay_onmouseover
//	    	if (getZKAttr(elm, "drag"))
//    			zkau.initdrag(elm);
	   	}
    	return true;
    case "z.drag":
    	if (value)
    		zkau.setAttr(elm, name, value);
    	else
    		zkau.rmAttr(elm, name);
    	return true;
    case "z.drop":
    	if (!value) {
    		if (elm._dp) {
    			if (zkau.cleandrop)
    				zkau.cleandrop(elm._dp);
	    		elm._dp = null;
	    	}
    	} else if (!elm._dp) {
    		Gmarker_doinitdrop(gmark, elm, value);
    	}
	}
		
	return false;
};

//handle initdrag/cleandrag
zkGmark.initdrag = function (elm) {
	if (elm._dgok && !elm._dg) {
		elm._dgok = null;
		var gmark = elm._gmark;
		
		//20090515, Henri Chen: Tricky! Use area map to find the index of the real <img/> element
		var gmaps = gmark._gmaps;
		var pane1 = gmaps.getPane(G_MAP_MARKER_MOUSE_TARGET_PANE);
		var pane2 = gmaps.getPane(G_MAP_MARKER_PANE);
		var gmimap = gmark._gmimap;
		var gmimaps = pane1.getElementsByTagName("map");
		var j = 0;
		for(; j < gmimaps.length; ++j) {
			if (gmimaps[j].id == gmimap.id) {
				break;
			}
		}
		var imgs = pane2.getElementsByTagName("img");
		var img = imgs[j];
		img.id = elm.id+"!dg";
		elm._dg = img;
		zkau.initdrag(img);
	}
};

zkGmark.cleandrag = function (elm) {
	var img = elm._dg;
	if (img) {
		zkau.cleandrag(img);
		elm._dg = null;
	}
};

////
zkGpoly = {};

/** Init */
zkGpoly.init = function (elm) {
	//always display:none
	elm.style.display = "none";

	var mp = $e(getZKAttr(elm, "pid"));
	if (mp._gmaps && !elm._gpoly) { //Google Maps is ready
		Gmaps_newGPoly(mp._gmaps, elm);
	}
	//when page loading, the zkGpoly.init is called before zkGmaps.init
	//the gmaps is not ready yet, so we cannot addOverlay here.
};

/** Called by the server to remove self. */
zkGpoly.cleanup = function (elm) {
	var mp = $e(getZKAttr(elm, "pid"));
	var gpoly = elm._gpoly;
	if (gpoly) {
		Goverlay_removeListeners(gpoly);
		elm._gpoly = null;
		gpoly._elm = null;
		if (mp._gmaps)
			mp._gmaps.removeOverlay(gpoly);
	}
};

////
zkGgon = {};

/** Init */
zkGgon.init = function (elm) {
	//always display:none
	elm.style.display = "none";

	var mp = $e(getZKAttr(elm, "pid"));
	if (mp._gmaps && !elm._ggon) { //Google Maps is ready
		Gmaps_newGGon(mp._gmaps, elm);
	}
		//when page loading, the zkGgon.init is called before zkGmaps.init
		//the gmaps is not ready yet, so we cannot addOverlay here.
};

/** Called by the server to remove self. */
zkGgon.cleanup = function (elm) {
	var mp = $e(getZKAttr(elm, "pid"));
	var ggon = elm._ggon;
	if (ggon) {
		Goverlay_removeListeners(ggon);
		elm._ggon = null;
		ggon._elm = null;
		if (mp._gmaps)
			mp._gmaps.removeOverlay(ggon);
	}
};

////
zkGgnd = {};

/** Init */
zkGgnd.init = function (elm) {
	//always display:none
	elm.style.display = "none";

	var mp = $e(getZKAttr(elm, "pid"));
	if (mp._gmaps && !elm._ggnd) { //Google Maps is ready
		Gmaps_newGGnd(mp._gmaps, elm);
	}
	//when page loading, the zkGgnd.init is called before zkGmaps.init
	//the gmaps is not ready yet, so we cannot addOverlay here.
};

/** Called by the server to remove self. */
zkGgnd.cleanup = function (elm) {
	var mp = $e(getZKAttr(elm, "pid"));
	var ggnd = elm._ggnd;
	if (ggnd) {
		elm._ggnd = null;
		ggnd._elm = null;
		if (mp._gmaps)
			mp._gmaps.removeOverlay(ggnd);
	}
};

////
zkGscr = {};

/** Init */
zkGscr.init = function (elm) {
	//always display:none
	elm.style.display = "none";

	var mp = $e(getZKAttr(elm, "pid"));
	if (mp._gmaps && !elm._gscr) { //Google Maps is ready
		Gmaps_newGScr(mp._gmaps, elm);
	}
	//when page loading, the zkGscr.init is called before zkGmaps.init
	//the gmaps is not ready yet, so we cannot addOverlay here.
};

/** Called by the server to remove self. */
zkGscr.cleanup = function (elm) {
	var mp = $e(getZKAttr(elm, "pid"));
	var gscr = elm._gscr;
	if (gscr) {
		elm._gscr = null;
		gscr._elm = null;
		if (mp._gmaps)
			mp._gmaps.removeOverlay(gscr);
	}
};
