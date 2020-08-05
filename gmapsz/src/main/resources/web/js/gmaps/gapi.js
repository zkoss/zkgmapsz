/* gapi.js

	Purpose:
		Load Google Map API
		
	Description:
		https://developers.google.com/maps/documentation/javascript/overview#Loading_the_Maps_API
		
	History:
		Fri Dec 04 13:24:19     2009, Created by henrichen

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 2.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
(function() {
gmapsGapi = {};
gmapsGapi.GOOGLE_API_LOADING_TIMEOUT = 10000; //default to ten seconds
gmapsGapi.GOOGLE_MAPS_API_URL = 'maps.googleapis.com/maps/api/js';

var GOOGLE_MAPS_API_LOADSCRIPT_KEY = 'googleMapsApiLoadScriptKey';

var effectiveApiParams = null;

gmapsGapi.loadGoogleMapsApi = function(protocol, apiParams, callback) {
	if (!effectiveApiParams) {
		effectiveApiParams = apiParams;
	} else if (effectiveApiParams != apiParams) {
		console.warn("ZK Gmaps - google maps api already loaded with parameters: ", effectiveApiParams);
		console.warn("ZK Gmaps - ignored parameters: ", apiParams);
	}
	if(!window.gmapsApiLoaded) {
		var scheme = !protocol ? '//' : (protocol + '://');
		zk.loadScript(scheme + gmapsGapi.GOOGLE_MAPS_API_URL + '?' + apiParams + '&callback=gmapsApiLoaded', GOOGLE_MAPS_API_LOADSCRIPT_KEY);
		window.gmapsApiLoaded = function() {
			zk.setScriptLoaded(GOOGLE_MAPS_API_LOADSCRIPT_KEY);
			zk.load('gmaps.ext');
		};
	}
	zk.afterLoad(GOOGLE_MAPS_API_LOADSCRIPT_KEY, function() {
		zk.afterLoad('gmaps.ext', callback);
	});
}

gmapsGapi.waitUntil = function(wgt, opts) {
	opts.inittime = opts.inittime || new Date().getTime();
	opts.timeout = opts.timeout || gmapsGapi.GOOGLE_API_LOADING_TIMEOUT;
	waitUntil(wgt, opts);
};
function waitUntil(wgt, opts) {
	if (!opts.condition()) {
		var timestamp0 = new Date().getTime();
		if ((timestamp0 - opts.inittime) < opts.timeout) {
			setTimeout(function() {waitUntil(wgt, opts);}, 100);
			return;
		}
	} else
		opts.callback();
}
})();
