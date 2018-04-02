/* MapModelMap.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Mon Jul 14 9:10:17     2008, Created by henrichen
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.zkoss.gmaps.event.MapDataEvent;
import org.zkoss.lang.Objects;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelMap;

/**
 * <p>This is the {@link MapModel} as a {@link java.util.Map} to be used 
 * with Maps component such as {@link Gmaps}.  Add or remove the contents 
 * of this model as a List would cause the associated Maps to change 
 * accordingly.</p> 
 *
 * @author henrichen
 * @since 2.0_9
 * @see MapModel
 * @see MapModelSet
 * @see MapModelList
 */
public class MapModelMap extends AbstractMapModel {
	private static final long serialVersionUID = 200807141725L;
	protected Map _map; //(key, value)
	
	/**
	 * Constructor.
	 *
	 * @param map the map to represent
	 * @param live whether to have a 'live' {@link ListModel} on top of
	 * the specified map.
	 * If false, the content of the specified map is copied.
	 * If true, this object is a 'facade' of the specified map,
	 * i.e., when you add or remove items from this {@link ListModelMap},
	 * the inner "live" map would be changed accordingly.
	 *
	 * However, it is not a good idea to modify <code>map</code>
	 * if it is passed to this method with live is true,
	 * since {@link Gmaps} is not smart enough to handle it.
	 * Instead, modify it thru this object.
	 * @since 2.4.0
	 */
	public MapModelMap(Map map, boolean live) {
		_map = live ? map: new LinkedHashMap(map);
	}
	
	/**
	 * Constructor.
	 */
	public MapModelMap() {
		_map = new LinkedHashMap();
	}
	
	/**
	 * Constructor.
	 * It mades a copy of the specified map (i.e., not live).
	 */
	public MapModelMap(Map map) {
		_map = new LinkedHashMap(map);
	}
	
	/**
	 * Constructor.
	 * @param initialCapacity the initial capacity for this MapModelMap.
	 */
	public MapModelMap(int initialCapacity) {
		_map = new LinkedHashMap(initialCapacity);
	}
	
	/**
	 * Constructor.
	 * @param initialCapacity the initial capacity for this MapModelMap.
	 * @param loadFactor the loadFactor to increase capacity of this MapModelMap.
	 */
	public MapModelMap(int initialCapacity, float loadFactor) {
		_map = new LinkedHashMap(initialCapacity, loadFactor);
	}

	/**
	 * Get the inner real Map.
	 */	
	public Map getInnerMap() {
		return _map;
	}
	
	//-- MapModel --//
	public void clear() {
		if (!_map.isEmpty()) {
			_map.clear();
			fireEvent(MapDataEvent.CLEARED, null);
		}
	}

	public Collection getItemsIn(double swlat, double swlng, double nelat,
			double nelng, double cenlat, double cenlng, int zoom) {
		return Collections.unmodifiableMap(_map).entrySet();
	}

	//-- Map --//
	public boolean containsKey(Object key) {
		return _map.containsKey(key);
	}
	
	public boolean containsValue(Object value) {
		return _map.containsValue(value);
	}
	
	public Set entrySet() {
		return new MyEntrySet(_map.entrySet());
	}
    
	public boolean equals(Object o) {
		return _map.equals(o instanceof MapModelMap ? ((MapModelMap)o)._map: o);
	}
	public String toString() {
		return _map.toString();
	}
	
	public Object get(Object key){
		return _map.get(key);
	}

	public int hashCode() {
		return _map.hashCode();
	}
		
	public boolean isEmpty() {
		return _map.isEmpty();
	}
    
	public Set keySet() {
		return new MyKeySet(_map.keySet(), 0);
	}

	private Entry entryOfKey(Object key) {
		for (Iterator it = _map.entrySet().iterator();;) {
			final Entry o = (Entry) it.next();
			if (Objects.equals(key, o.getKey()))
				return o;
		}
	}
	
	private Entry entryOfValue(Object val) {
		for (Iterator it = _map.entrySet().iterator();;) {
			final Entry o = (Entry) it.next();
			if (Objects.equals(val, o.getValue()))
				return o;
		}
	}
	
	public Object put(Object key, Object o) {
		final Object ret;
		if (_map.containsKey(key)) {
			if (Objects.equals(o, _map.get(key))) {
				return o; //nothing changed
			}
			Object entry = entryOfKey(key);
			ret = _map.put(key, o);
			fireEvent(MapDataEvent.REMOVED, Collections.singleton(entry));
			fireEvent(MapDataEvent.ADDED, Collections.singleton(entry));
		} else {
			ret = _map.put(key, o);
			final Object entry = entryOfKey(key);
			fireEvent(MapDataEvent.ADDED, Collections.singleton(entry));
		}
		return ret;
	}

	public void putAll(Map c) {
		_map.putAll(c);
		fireEvent(MapDataEvent.ADDED, c.entrySet());
	}

	public Object remove(Object key) {
		if (_map.containsKey(key)) {
			final Object entry = entryOfKey(key);
			final Object ret = _map.remove(key);
			fireEvent(MapDataEvent.REMOVED, Collections.singleton(entry));
			return ret;
		}
		return null;
	}

	public int size() {
		return _map.size();
	}
	
	public Collection values() {
		return new MyCollection(_map.values());
	}

	private class MyIterator implements Iterator {
		private int _akey; //0: a key, 1: a value, 2: an entry
		private Iterator _it;
		private Object _current;
		
		public MyIterator(Iterator inner, int akey) {
			_it = inner;
			_akey = akey;
		}
		
		public boolean hasNext() {
			return _it.hasNext();
		}
		
		public Object next() {
			_current = _it.next();
			return _current;
		}
		
		public void remove() {
			_it.remove();
			fireEvent(MapDataEvent.REMOVED, 
				Collections.singleton(_akey == 0 ? entryOfKey(_current) : 
					_akey == 1 ? entryOfValue(_current) : _current));
		}
	}

	/** Represents the key set.
	 */
	private class MyKeySet implements Set {
		protected final Set _set;
		protected final int _akey;
		public MyKeySet(Set inner, int akey) {
			_set = inner;
			_akey = akey;
		}
		
		public void clear() {
			MapModelMap.this.clear();
		}
			
		public boolean remove(Object o) {
			if (_set.contains(o)) {
				MapModelMap.this.remove(o);
				return true;
			}
			return false;
		}
		
		public boolean removeAll(Collection c) {
			if (_set == c || this == c) { //special case
				clear();
				return true;
			}
			final List lst = new LinkedList();
			if (_akey  == 0) { //a key set
				final Set keys = new HashSet(c); 
				for (final Iterator it = _map.entrySet().iterator(); it.hasNext();) {
					final Entry item = (Entry) it.next();
					final Object key = item.getKey();
					if (keys.contains(key)) {
						_set.remove(key);
						lst.add(item);
					}
				}
				fireEvent(MapDataEvent.REMOVED, lst);
				return !lst.isEmpty();
			} else { //an entry set
				final boolean ret = _set.removeAll(c);
				if (ret)
					fireEvent(MapDataEvent.REMOVED, c);
				return ret;
			}
		}
	
		public boolean retainAll(Collection c) {
			if (_set == c || this == c) { //special case
				return false;
			}
			final List lst = new LinkedList();
			final Set keys = new HashSet(c); 
			if (_akey  == 0) { //a key set
				for (final Iterator it = _map.entrySet().iterator(); it.hasNext();) {
					final Entry item = (Entry) it.next();
					final Object key = item.getKey();
					if (!keys.contains(key)) {
						_set.remove(key);
						lst.add(item);
					}
				}
			} else { //an entry set
				for (final Iterator it = _map.entrySet().iterator(); it.hasNext();) {
					final Entry item = (Entry) it.next();
					if (!keys.contains(item)) {
						_set.remove(item);
						lst.add(item);
					}
				}
			}
			if (!lst.isEmpty()) {
				fireEvent(MapDataEvent.REMOVED, lst);
				return true;
			}
			return false;
		}
		
		public Iterator iterator() {
			return new MyIterator(_set.iterator(), _akey);
		}

		public boolean add(Object o) {
			throw new UnsupportedOperationException("add()");
		}
		
		public boolean addAll(Collection col) {
			throw new UnsupportedOperationException("addAll()");
		}

		public boolean contains(Object o) {
			return _set == null ? false : _set.contains(o);
		}
		
		public boolean containsAll(Collection c) {
			return _set == null ? false : _set.containsAll(c);
		}
		
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o instanceof MyKeySet) {
				return Objects.equals(((MyKeySet)o)._set, _set);
			} else {
				return Objects.equals(_set, o);
			}
		}
		
		public int hashCode(){
			return _set == null ? 0 : _set.hashCode();
		}
		
		public boolean isEmpty() {
			return _set == null ? true : _set.isEmpty();
		}

		public int size() {
			return _set == null ? 0 : _set.size();
		}
		
		public Object[] toArray() {
			return _set == null ? new Object[0] : _set.toArray();
		}
		
		public Object[] toArray(Object[] a) {
			return _set == null ? a : _set.toArray(a);
		}
	}
	private class MyEntrySet extends MyKeySet {
		private MyEntrySet(Set inner) {
			super(inner, 2);
		}
	}

	private class MyCollection implements Collection {
		private Collection _inner;
		
		public MyCollection(Collection inner) {
			_inner = inner;
		}

		public void clear() {
			_inner.clear();
			fireEvent(MapDataEvent.CLEARED, null);
		}
		
		public boolean remove(Object o) {
			final boolean ret = _inner.remove(o);
			if (ret) {
				fireEvent(MapDataEvent.REMOVED, Collections.singleton(entryOfValue(o)));
			}
			return ret;
		}

		public boolean removeAll(Collection c) {
			if (_inner == c || this == c) { //special case
				clear();
				return true;
			}
			final List lst = new LinkedList();
			final Set values = new HashSet(c);
			for (final Iterator it = _map.entrySet().iterator(); it.hasNext();) {
				final Entry item = (Entry) it.next();
				final Object val = item.getValue();
				if (values.contains(val)) {
					_inner.remove(val);
					lst.add(item);
				}
			}
			if (!lst.isEmpty()) {
				fireEvent(MapDataEvent.REMOVED, lst);
				return true;
			}
			return false;
		}
		
		public boolean retainAll(Collection c) {
			if (_inner == c || this == c) { //special case
				return false;
			}
			final List lst = new LinkedList();
			final Set values = new HashSet(c);
			for (final Iterator it = _map.entrySet().iterator(); it.hasNext();) {
				final Entry item = (Entry) it.next();
				final Object val = item.getValue();
				if (!values.contains(val)) {
					_inner.remove(val);
					lst.add(item);
				}
			}
			if (!lst.isEmpty()) {
				fireEvent(MapDataEvent.REMOVED, lst);
				return true;
			}
			return false;
		}
		
		public Iterator iterator() {
			return new MyIterator(_inner.iterator(), 1);
		}

		public boolean add(Object o) {
			throw new UnsupportedOperationException("add()");
		}
		
		public boolean addAll(Collection col) {
			throw new UnsupportedOperationException("addAll()");
		}

		public boolean contains(Object o) {
			return _inner == null ? false : _inner.contains(o);
		}
		
		public boolean containsAll(Collection c) {
			return _inner == null ? false : _inner.containsAll(c);
		}
		
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o instanceof MyCollection) {
				return Objects.equals(((MyCollection)o)._inner, _inner);
			} else {
				return Objects.equals(_inner, o);
			}
		}
		
		public int hashCode(){
			return _inner == null ? 0 : _inner.hashCode();
		}
		
		public boolean isEmpty() {
			return _inner == null ? true : _inner.isEmpty();
		}

		public int size() {
			return _inner == null ? 0 : _inner.size();
		}
		
		public Object[] toArray() {
			return _inner == null ? new Object[0] : _inner.toArray();
		}
		
		public Object[] toArray(Object[] a) {
			return _inner == null ? a : _inner.toArray(a);
		}
	}
}
