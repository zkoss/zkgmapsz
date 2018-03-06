/* AbstractMapModel.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Jul 10 13:06:36     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.gmaps.event.MapDataEvent;
import org.zkoss.gmaps.event.MapDataListener;
import org.zkoss.io.Serializables;

/**
 * A skeletal implementation for {@link MapModel}.
 * @author henrichen
 * @since 2.0_9
 */
abstract public class AbstractMapModel implements MapModel, java.io.Serializable {
	private static final long serialVersionUID = 200807101306L;
	private transient List _listeners = new LinkedList();

	/** Fires a {@link MapDataEvent} for all registered listener
	 * (thru {@link #addMapDataListener}.
	 *
	 * <p>Note: you can invoke this method only in an event listener.
	 * @param type the {@link MapDataEvent} type
	 * @param items the items to be processed 
	 */
	protected void fireEvent(int type, Collection items) {
		final MapDataEvent evt = new MapDataEvent(this, type, items);
		for (Iterator it = _listeners.iterator(); it.hasNext();)
			((MapDataListener)it.next()).onChange(evt);
	}

	//-- MapModel --//
	public void addMapDataListener(MapDataListener listener) {
		if (listener == null)
			throw new NullPointerException();
		_listeners.add(listener);
	}
	public void removeMapDataListener(MapDataListener listener) {
		_listeners.remove(listener);
	}

	//Serializable//
	private synchronized void writeObject(java.io.ObjectOutputStream s)
	throws java.io.IOException {
		s.defaultWriteObject();

		Serializables.smartWrite(s, _listeners);
	}
	private synchronized void readObject(java.io.ObjectInputStream s)
	throws java.io.IOException, ClassNotFoundException {
		s.defaultReadObject();

		_listeners = new LinkedList();
		Serializables.smartRead(s, _listeners);
	}
}
