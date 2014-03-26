/* MapModelSet.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Jul 11 10:23:06     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.gmaps.event.MapDataEvent;

/**
 * <p>This is the {@link MapModel} as a {@link java.util.Set} to be used with Maps component such as {@link Gmaps}.
 * Add or remove the contents of this model as a Set would cause the associated Maps to change accordingly.</p> 
 *
 * @author Henri Chen
 * @see MapModel
 * @see MapModelList
 * @see MapModelMap
 * @since 2.0_9
 */
public class MapModelSet extends AbstractMapModel
implements Set, java.io.Serializable {
	private static final long serialVersionUID = 200807021330L;
	protected Set _set;

	/**
	 * Constructor
	 *
	 * @param set the set for initial entry in Set
	 * @param live whether to have a 'live' {@link MapModel} on top of
	 * the specified set.
	 * If false, the content of the specified set is copied.
	 * If true, this object is a 'facade' of the specified set,
	 * i.e., when you add or remove items from this {@link MapModelSet},
	 * the inner "live" set would be changed accordingly.
	 *
	 * However, it is not a good idea to modify <code>set</code>
	 * if it is passed to this method with live is true,
	 * since Maps component is not smart enough to handle it.
	 * Instead, modify it thru this object.
	 */
	public MapModelSet(Set set, boolean live) {
		_set = live ? set: new HashSet(set);
	}
	
	/**
	 * Constructor.
	 */
	public MapModelSet() {
		_set = new HashSet();
	}

	/**
	 * Constructor.
	 * It mades a copy of the specified collection (i.e., not live).
	 */
	public MapModelSet(Collection c) {
		_set = new HashSet(c);
	}
	/**
	 * Constructor.
	 * It mades a copy of the specified array (i.e., not live).
	 */
	public MapModelSet(Object[] array) {
		_set = new HashSet(array.length);
		for (int j = 0; j < array.length; ++j)
			_set.add(array[j]);
	}
	
	/**
	 * Constructor.
	 * @param initialCapacity the initial capacity for this MapModelSet.
	 */
	public MapModelSet(int initialCapacity) {
		_set = new HashSet(initialCapacity);
	}

	/**
	 * Constructor.
	 * @param initialCapacity the initial capacity for this MapModelSet.
	 * @param loadFactor the loadFactor to increase capacity of this MapModelSet.
	 */
	public MapModelSet(int initialCapacity, float loadFactor) {
		_set = new HashSet(initialCapacity, loadFactor);
	}

	/**
	 * Get the inner real set.
	 */	
	public Set getInnerSet() {
		return _set;
	}
	
	//-- MapModel --//
	public Collection getItemsIn(double swlat, double swlng, 
		double nelat, double nelng, double cenlat, double cenlng, int zoom) {
		return Collections.unmodifiableSet(_set);
	}

	//-- Set --//
 	public boolean add(Object o) {
		boolean ret = _set.add(o);
		if (ret) {
			fireEvent(MapDataEvent.ADDED, Collections.singleton(o));
		}
		return ret;
	}

	public boolean addAll(Collection c) {
		final boolean ret = _set.addAll(c);
		if (ret) {
			fireEvent(MapDataEvent.ADDED, Collections.unmodifiableCollection(c));
		}
		return ret;
	}
	
	public void clear() {
		if (!_set.isEmpty()) {
			_set.clear();
			fireEvent(MapDataEvent.CLEARED, null);
		}
	}
	
	public boolean contains(Object elem) {
		return _set.contains(elem);
	}
	
	public boolean containsAll(Collection c) {
		return _set.containsAll(c);
	}
	
	public boolean equals(Object o) {
		return _set.equals(o instanceof MapModelSet ? ((MapModelSet)o)._set: o);
	}
	
	public int hashCode() {
		return _set.hashCode();
	}
		
	public boolean isEmpty() {
		return _set.isEmpty();
	}
	public String toString() {
		return _set.toString();
	}
	public Iterator iterator() {
		return new Iterator() {
			private Iterator _it = _set.iterator();
			private Object _current = null;
			public boolean hasNext() {
				return _it.hasNext();
			}
			public Object next() {
				_current = _it.next();
				return _current;
			}
			public void remove() {
				_it.remove();
				fireEvent(MapDataEvent.REMOVED, Collections.singleton(_current));
			}
		};
	}
	public boolean remove(Object o) {
		if (_set.contains(o)) {
			_set.remove(o);
			fireEvent(MapDataEvent.REMOVED, Collections.singleton(o));
			return true;
		}
		return false;
	}
	public boolean removeAll(Collection c) {
		if (_set == c || this == c) { //special case
			clear();
			return true;
		}
		final boolean ret = _set.removeAll(c);
		if (ret) {
			fireEvent(MapDataEvent.REMOVED, Collections.unmodifiableCollection(c));
		}
		return ret;
	}
	public boolean retainAll(Collection c) {
		if (_set == c || this == c) { //special case
			return false;
		}
		final boolean ret = _set.retainAll(c);
		if (ret) {
			fireEvent(MapDataEvent.BOUNDS_CHANGED, Collections.unmodifiableSet(_set));
		}
		return ret;
	}
	public int size() {
		return _set.size();
	}
	
	public Object[] toArray() {
		return _set.toArray();
	}

	public Object[] toArray(Object[] a) {
		return _set.toArray(a);
	}
}
