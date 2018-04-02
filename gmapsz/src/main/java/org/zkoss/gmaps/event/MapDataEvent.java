/* MapDataEvent.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Jul 18 9:21:17     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps.event;

import java.util.Collection;

import org.zkoss.gmaps.MapModel;

/**
 * Defines an event that encapsulates changes to a Maps.
 * @author henrichen
 * @since 2.0_9
 */
public class MapDataEvent {
	/** When contents of the specified item in the MapModel changed. */
	public static final int CONTENTS_CHANGED = 0;
	/** When add items to the MapModel. */
	public static final int ADDED = 1;
	/** When remove items from the MapModel. */
	public static final int REMOVED = 2;
	/** When Maps bound changed or the MapModel changed */
	public static final int BOUNDS_CHANGED = 3;
	/** When clear all items form the MapModel. */
	public static final int CLEARED = 4;
	
	private MapModel _model;
	private int _type;
	private Collection _items;
	
	public MapDataEvent(MapModel model, int type, Collection items) {
		_model = model;
		_type = type;
		_items = items;
	}
	
	public MapModel getModel() {
		return _model;
	}
	
	public int getType() {
		return _type;
	}
	
	public Collection getItems() {
		return _items;
	}
}
