/* MapMoveCommand.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Oct  13 14:37:25     2006, Created by henrichen
}}IS_NOTE

Copyright (C) 2006 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import org.zkoss.gmaps.event.MapMoveEvent;

import org.zkoss.lang.Objects;

import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.Command;

/**
 * Used only by {@link AuRequest} to implement the {@link MapMoveEvent}
 * relevant command.
 * 
 * @author henrichen
 */
/* package */ class MapMoveCommand extends Command {
	public MapMoveCommand(String evtnm, int flags) {
		super(evtnm, flags);
	}

	//-- super --//
	protected void process(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
		final String[] data = request.getData();
		if (data == null || data.length != 6)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
				new Object[] {Objects.toString(data), this});

		final Gmaps gmaps = (Gmaps) comp;
		final double lat = Double.parseDouble(data[0]);
		final double lng = Double.parseDouble(data[1]);
		final double swlat = Double.parseDouble(data[2]);
		final double swlng = Double.parseDouble(data[3]);
		final double nelat = Double.parseDouble(data[4]);
		final double nelng = Double.parseDouble(data[5]);
		gmaps.setCenterByClient(lat, lng);
		gmaps.setBoundsByClient(swlat, swlng, nelat, nelng);
		Events.postEvent(new MapMoveEvent(getId(), comp, lat, lng, swlat, swlng, nelat, nelng));
	}
}
