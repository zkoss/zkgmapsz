package org.zkoss.gmaps;

import org.zkoss.gmaps.event.MapTypeChangeEvent;
import org.zkoss.gmaps.event.MapZoomEvent;
import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.Command;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;

public class MapTypeChangeCommand extends Command {
	public MapTypeChangeCommand(String evtnm, int flags) {
		super(evtnm, flags);
	}

	protected void process(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
		final String[] data = request.getData();
		if (data == null || data.length != 1)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
				new Object[] {Objects.toString(data), this});

		final Gmaps gmaps = (Gmaps) comp;
		final String type = data[0];
		gmaps.setMapTypeByClient(type);
		Events.postEvent(new MapTypeChangeEvent(getId(), comp, type));
	}
}
