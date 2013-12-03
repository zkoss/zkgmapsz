/* PathChangeEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Dec 2 11:07:30     2013, Created by RaymondChao
}}IS_NOTE

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps.event;

import java.util.Map;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents a Google Maps {@link org.zkoss.gmaps.Gmaps} related event which is
 * triggered whenever the path of {@link org.zkoss.gmaps.Gpolyline} or 
 * {@link org.zkoss.gmaps.Gpolygon} is changed.
 * 
 * @author RaymondChao
 * @since 3.0.2
 */
public class PathChangeEvent extends Event {
	private String _path;

	/** Converts an AU request to a event.
	 */
	public static final PathChangeEvent getPathChangeEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();
		if (data == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
					new Object[] {data, request});
		
		return new PathChangeEvent(request.getCommand(), comp, (String) data.get("path"));
	}
	
	/** Constructs a Google Maps path change relevant event.
	 */
	public PathChangeEvent(String name, Component target, String path) {
		super(name, target);
		_path = path;
	}
	/** Returns the changed path of the Google Map.
	 */
	public final String getPath() {
		return _path;
	}
}
