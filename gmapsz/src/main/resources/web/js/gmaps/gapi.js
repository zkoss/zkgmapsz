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

gmapsGapi.GOOGLE_MAPS_API_LOADSCRIPT_KEY = 'googleMapsApiLoadScriptKey';

var effectiveApiParams = null;

gmapsGapi.loadGoogleMapsApi = function(protocol, apiParams, callback) {
	if (!effectiveApiParams) {
		effectiveApiParams = apiParams;
	} else if (effectiveApiParams != apiParams) {
		console.warn("ZK Gmaps - google maps api already loaded with parameters: ", effectiveApiParams);
		console.warn("ZK Gmaps - ignored parameters: ", apiParams);
	}
	if(!window.gmapsApiLoaded) {
		gmapsGapi.loadScript(apiParams, callback);
	}
}

gmapsGapi.loadScript = async function loadScript(apiParams, callback) {
	if(!window._gmapsApiLoaded){
		window._gmapsApiLoaded = "loading";
		gmapsGapi.loadedCallbacksArray.push(callback);

		(g => {
			var h, a, k, p = "The Google Maps JavaScript API",
				c = "google",
				l = "importLibrary",
				q = "__ib__",
				m = document,
				b = window;
			b = b[c] || (b[c] = {});
			var d = b.maps || (b.maps = {}),
				r = new Set,
				e = new URLSearchParams,
				u = () => h || (h = new Promise(async (f, n) => {
					await (a = m.createElement("script"));
					e.set("libraries", [...r] + "");
					for (k in g) e.set(k.replace(/[A-Z]/g, t => "_" + t[0].toLowerCase()), g[k]);
					e.set("callback", c+".maps."+q);
					a.src = `https://maps.${c}apis.com/maps/api/js?` + e;
					d[q] = f;
					a.onerror = () => h = n(Error(p + " could not load."));
					let querySel = m.querySelector("script[nonce]");
					a.nonce = querySel?querySel.nonce:"" || "";
					m.head.append(a)
				}));
			d[l] ? console.warn(p + " only loads once. Ignoring:", g) : d[l] = (f, ...n) => r.add(f) && u().then(() => d[l](f, ...n))
		})({
			key: apiParams.key,
			v: "weekly"
		});
		
		
		
		/*var script = document.createElement('script');
		script.type = 'text/javascript';
		script.src = 'https://maps.googleapis.com/maps/api/js?'+ apiParams +'&' + 'libraries=places&'+'callback=googleMapsApiLoaded';
		script.async = true;
		*/
		await Promise.all([google.maps.importLibrary("marker"),google.maps.importLibrary("geometry")]);
		gmapsGapi.loadedCallbacks();
	}else{
		if(window._gmapsApiLoaded == "loading"){
			gmapsGapi.loadedCallbacksArray.push(callback);
		}else{
			callback();
		}
	}
}

gmapsGapi.loadedCallbacksArray = [];

gmapsGapi.loadedCallbacks = function(){
	window._gmapsApiLoaded = true;
	while (gmapsGapi.loadedCallbacksArray.length){
		let callback = gmapsGapi.loadedCallbacksArray.pop();
		callback();
	}
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
