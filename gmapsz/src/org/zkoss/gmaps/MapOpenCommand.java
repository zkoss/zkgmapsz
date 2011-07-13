/* MapOpenCommand.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Mon Jul 14 10:17:36     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import org.zkoss.gmaps.event.MapOpenEvent;
import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.Command;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.ext.client.Openable;
import org.zkoss.zk.ui.sys.ComponentCtrl;

/**
 * @author henrichen
 *
 */
public class MapOpenCommand extends Command {
	public MapOpenCommand(String evtnm, int flags) {
		super(evtnm, flags);
	}

	//-- super --//
	protected void process(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
		final String[] data = request.getData();
		if (data == null || data.length != 5)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
				new Object[] {Objects.toString(data), this});

		final boolean open = "true".equals(data[0]);
		final Component ref = request.getDesktop().getComponentByUuidIfAny(data[1]);
		final Object xc = ((ComponentCtrl)comp).getExtraCtrl();
		if (xc instanceof Openable)
			((Openable)xc).setOpenByClient(open);
		final double lat = Double.parseDouble(data[3]);
		final double lng = Double.parseDouble(data[4]);
		Events.postEvent(new MapOpenEvent(getId(), comp, open, ref,
			data[2], lat, lng));
	}
}
