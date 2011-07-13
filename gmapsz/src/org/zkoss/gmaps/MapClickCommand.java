/* MapClickCommand.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Jan  19 16:13:37     2007, Created by henrichen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import org.zkoss.gmaps.event.MapMouseEvent;

import org.zkoss.lang.Objects;

import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.Command;
import org.zkoss.zk.au.in.Commands;

/**
 * Used only by {@link AuRequest} to implement the {@link MapMouseEvent}
 * relevant command.
 * 
 * @author henrichen
 */
/* package */ class MapClickCommand extends Command {
	public MapClickCommand(String evtnm, int flags) {
		super(evtnm, flags);
	}

	//-- super --//
	protected void process(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
		final String[] data = request.getData();
		if (data == null || data.length != 8)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
				new Object[] {Objects.toString(data), this});

		final Gmaps gmaps = (Gmaps) comp;
		final Component ref = data[0] != null ? gmaps.getDesktop().getComponentByUuidIfAny(data[0]) : null;
		final double lat = Double.parseDouble(data[1]);
		final double lng = Double.parseDouble(data[2]);
		//bug #2167700 FF3 only. Exception when click on ZK Pet Shop Maps
		final int x = (int) Math.round(Double.parseDouble(data[3]));
		final int y = (int) Math.round(Double.parseDouble(data[4]));
		final int clientX = (int) Math.round(Double.parseDouble(data[5]));
		final int clientY = (int) Math.round(Double.parseDouble(data[6]));
		Events.postEvent(new MapMouseEvent(getId(), comp, ref, lat, lng, x, y, clientX, clientY, Commands.parseKeys(data[7])));
	}
}
