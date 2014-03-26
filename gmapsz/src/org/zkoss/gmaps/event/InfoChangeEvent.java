/* InfoChangeEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Oct 13 14:51:13     2006, Created by henrichen
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps.event;

import java.util.Map;

import org.zkoss.gmaps.Ginfo;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents a Google Maps {@link org.zkoss.gmaps.Gmaps} related event which is
 * triggered whenever the currently opened {@link org.zkoss.gmaps.Ginfo} or 
 * {@link org.zkoss.gmaps.Gmarker} is changed.
 * 
 * @author henrichen
 */
public class InfoChangeEvent extends Event {
	private final Ginfo _info;

	/** Converts an AU request to a event.
	 * @since 5.0.0
	 */
	public static final InfoChangeEvent getInfoChangeEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();
		if (data == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
					new Object[] {data, request});

        Ginfo info = (Ginfo) request.getDesktop().getComponentByUuidIfAny((String)data.get("info"));
        if (info != null && info.getParent() != comp) {
            info = null;
        }
		return new InfoChangeEvent(request.getCommand(), comp, info);
	}
	
	/** Constructs a Google Maps info window change relevant event.
	 */
	public InfoChangeEvent(String name, Component target, Ginfo info) {
		super(name, target);
		_info = info;
	}
	/** Returns the new opened info window of the Google Map (null means none is opened).
	 */
	public final Ginfo getInfo() {
		return _info;
	}
}
