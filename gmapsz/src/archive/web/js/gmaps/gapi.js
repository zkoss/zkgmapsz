/* gapi.js

	Purpose:
		Google's AJAX APIs
		
	Description:
		http://code.google.com/apis/ajax/documentation/
		
		Used to access Google's service, e.g. Maps API, Search API, Feed API, etc..
		Since it requires a user to sign up and get a "googleAPIkey" to use it, 
		you have to	get such key on the Google's web site first:
		
		http://code.google.com/apis/maps/signup.html
		
		Then you specify the key in the ZK's page using following mechanism:
		
		<script content="zk.googleAPIkey='your-key-here'/>
		
		And that is all!
		
	History:
		Fri Dec 04 13:24:19     2009, Created by henrichen

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
(function() {
gmapsGapi = {};
gmapsGapi.GOOGLE_API_LOADING_TIMEOUT = 10000; //default to ten seconds
gmapsGapi.loadAPIs = function(wgt, callback, msg, timeout) {
	var opts = {};
	opts['condition'] = function() {return window.google && window.google.load};
	opts['callback'] = function() {callback(); delete gmapsGapi.LOADING;}
	if (!opts.condition()) {
		gmapsGapi.waitUntil(wgt, opts);
		if (!gmapsGapi.LOADING) { //avoid double loading Google Ajax APIs
			gmapsGapi.LOADING = true;
			if (!opts.condition())
				zk.loadScript('https://www.google.com/jsapi');
		}
	} else
		callback();
};
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
		var mopts = wgt._maskOpts;
		// Issue 43: Clear mask after loading timeout
		if (mopts && mopts._mask)
			gmapsGapi.clearMask(wgt, wgt._maskOpts);
	} else
		opts.callback();
}
gmapsGapi.initMask = function (wgt, opts) {
	if (wgt.desktop && wgt.isRealVisible(true)) {
		var opt = {};
		opt['anchor'] = wgt;
		// Issue 42: Give mask an unique id for each map.
		opt['id'] = wgt.uuid + '-mask';
		if (opts.message) opt['message'] = opts.message;
		opts['_mask'] = new zk.eff.Mask(opt);
	}
	return opts;
};
gmapsGapi.clearMask = function(wgt, opts) {
	if (opts._mask)
		opts._mask.destroy();
};
})();