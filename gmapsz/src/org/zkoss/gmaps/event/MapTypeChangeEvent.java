/* MapTypeChangeEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Apr 16, 2009 9:52:08 AM, Created by henrichen
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/

package org.zkoss.gmaps.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents a Google Maps {@link org.zkoss.gmaps.Gmaps} related event which
 * is triggered whenever the map type is changed.
 *
 * @author henrichen
 *
 */
public class MapTypeChangeEvent extends Event {
	private final String _type;

	/** Constructs a Google Maps map type relevant event.
	 */
	public MapTypeChangeEvent(String name, Component target, String type) {
		super(name, target);
		_type = type;
	}
	/** Returns the map type name of the Google Map after map type changed.
	 */
	public final String getType() {
		return _type;
	}
}
