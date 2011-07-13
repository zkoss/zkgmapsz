/* MapModelList.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Thu Jul 17 10:17:36     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.zkoss.gmaps.event.MapDataEvent;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zul.ListModel;

/**
 * <p>This is the {@link MapModel} as a {@link java.util.List} to be used 
 * with Maps component such as {@link Gmaps}.  Add or remove the contents 
 * of this model as a List would cause the associated Maps to change 
 * accordingly.</p> 
 *
 * @author henrichen
 * @since 2.0_9
 * @see MapModel
 * @see MapModelSet
 * @see MapModelMap
 *
 */
public class MapModelList extends AbstractMapModel 
implements List, java.io.Serializable {
	private static final long serialVersionUID = 200807141409L;
	protected List _list;
	
	/**
	 * Constructor
	 *
	 * @param list the list to represent
	 * @param live whether to have a 'live' {@link ListModel} on top of
	 * the specified list.
	 * If false, the content of the specified list is copied.
	 * If true, this object is a 'facade' of the specified list,
	 * i.e., when you add or remove items from this ListModelList,
	 * the inner "live" list would be changed accordingly.
	 *
	 * However, it is not a good idea to modify <code>list</code>
	 * if it is passed to this method with live is true,
	 * since {@link Gmaps} is not smart enough to handle it.
	 * Instead, modify it thru this object.
	 */
	public MapModelList(List list, boolean live) {
		_list = live ? list: new ArrayList(list);
	}

	/**
	 * Constructor.
	 */
	public MapModelList() {
		_list = new ArrayList();
	}
	
	/**
	 * Constructor.
	 * It mades a copy of the specified collection (i.e., not live).
	 */
	public MapModelList(Collection c) {
		_list = new ArrayList(c);
	}
	/**
	 * Constructor.
	 * It mades a copy of the specified array (i.e., not live).
	 */
	public MapModelList(Object[] array) {
		_list = new ArrayList(array.length);
		for (int j = 0; j < array.length; ++j)
			_list.add(array[j]);
	}
	
	/**
	 * Constructor.
	 * @param initialCapacity the initial capacity for this MapModelList.
	 */
	public MapModelList(int initialCapacity) {
		_list = new ArrayList(initialCapacity);
	}

	/**
	 * Remove from fromIndex(inclusive) to toIndex(exclusive). If fromIndex equals toIndex, 
	 * this methods do nothing.
	 * @param fromIndex the begin index (inclusive) to be removed.
	 * @param toIndex the end index (exclusive) to be removed.
	 */
	public void removeRange(int fromIndex, int toIndex) {
		if (fromIndex > toIndex) {
			throw new UiException("fromIndex must less than toIndex: fromIndex: "+fromIndex+", toIndex: "+toIndex);
		}
		if (fromIndex == toIndex) {
			return;
		}
		int sz = _list.size();
		if (sz == fromIndex) {
			return;
		}
		int index = fromIndex;
		final List lst = new LinkedList();
		for (final Iterator it = _list.listIterator(fromIndex); it.hasNext() && index < toIndex; ++index){
			final Object item = it.next();
			it.remove();
			lst.add(item);
		}
		fireEvent(MapDataEvent.REMOVED, lst);
	}

	/**
	 * Get the inner real List.
	 */	
	public List getInnerList() {
		return _list;
	}

	//-- MapModel --//
	public void clear() {
		if (!_list.isEmpty()) {
			_list.clear();
			fireEvent(MapDataEvent.CLEARED, null);
		}
	}

	public Collection getItemsIn(double swlat, double swlng, double nelat,
			double nelng, double cenlat, double cenlng, int zoom) {
		return Collections.unmodifiableList(_list);
	}

	//-- List --//
 	public void add(int index, Object element){
 		_list.add(index, element);
 		fireEvent(MapDataEvent.ADDED, _list.subList(index, index+1));
 	}
 	
	public boolean add(Object o) {
		int i1 = _list.size();
		boolean ret = _list.add(o);
		fireEvent(MapDataEvent.ADDED, _list.subList(i1, i1+1));
		return ret;
	}

	public boolean addAll(Collection c) {
		int sz = c.size();
		if (sz <= 0) {
			return false;
		}
		int i1 = _list.size();
		int i2 = i1 + sz - 1;
		boolean ret = _list.addAll(c);
		fireEvent(MapDataEvent.ADDED, _list.subList(i1, i2));
		return ret;
	}
	
	public boolean addAll(int index, Collection c) {
		int sz = c.size();
		if (sz <= 0) {
			return false;
		}
		int i2 = index + sz - 1;
		boolean ret = _list.addAll(index, c);
		fireEvent(MapDataEvent.ADDED, _list.subList(index, i2));
		return ret;
	}
		
	public boolean contains(Object elem) {
		boolean ret = _list.contains(elem);
		return ret;
	}
	
	public boolean containsAll(Collection c) {
		return _list.containsAll(c);
	}
	
	public boolean equals(Object o) {
		return _list.equals(o instanceof MapModelList ? ((MapModelList)o)._list: o);
	}
	
	public Object get(int index){
		return _list.get(index);
	}

	public int hashCode() {
		return _list.hashCode();
	}
	public String toString() {
		return _list.toString();
	}

	public int indexOf(Object elem) {
		return _list.indexOf(elem);
	}
	
	public boolean isEmpty() {
		return _list.isEmpty();
	}
    
    public Iterator iterator() {
		return new Iterator() {
			private Iterator _it = _list.iterator();
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
				fireEvent(MapDataEvent.REMOVED, Collections.singletonList(_current));
			}
		};
    }
    
    public int lastIndexOf(Object elem) {
    	return _list.lastIndexOf(elem);
    }
	
	public ListIterator listIterator() {
		return _list.listIterator();
	}
	
	public ListIterator listIterator(final int index) {
		return new ListIterator() {
			private ListIterator _it = _list.listIterator(index);
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
				fireEvent(MapDataEvent.REMOVED, Collections.singletonList(_current));
			}
			public void add(Object arg0) {
				_it.add(arg0);
				fireEvent(MapDataEvent.ADDED, Collections.singletonList(arg0));
			}
			public boolean hasPrevious() {
				return _it.hasPrevious();
			}
			public int nextIndex() {
				return _it.nextIndex();
			}
			public Object previous() {
				_current = _it.previous();
				return _current;
			}
			public int previousIndex() {
				return _it.previousIndex();
			}
			public void set(Object arg0) {
				_it.set(arg0);
				fireEvent(MapDataEvent.REMOVED, Collections.singletonList(_current));
				fireEvent(MapDataEvent.ADDED, Collections.singletonList(arg0));
			}
		};
	}
	
	public Object remove(int index) {
		Object ret = _list.remove(index);
		if (ret != null) {
			fireEvent(MapDataEvent.REMOVED, Collections.singleton(ret));
		}
		return ret;
	}

	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index >= 0) {
			remove(index);
		}
		return false;
	}
	
	public boolean removeAll(Collection c) {
		if (_list == c || this == c) { //special case
			clear();
			return true;
		}
		return removePartial(c, true);
	}

	public boolean retainAll(Collection c) {
		if (_list == c || this == c) { //special case
			return false;
		}
		return removePartial(c, false);
	}
	
	private boolean removePartial(Collection c, boolean exclude) {
		boolean removed = false;
		int index = 0;
		int begin = -1;
		final List lst = new LinkedList();
		for(final Iterator it = _list.iterator(); it.hasNext(); ++index) {
			Object item = it.next();
			if (c.contains(item) == exclude) {
				if (begin < 0) {
					begin = index;
				}
				removed = true;
				it.remove();
				lst.add(item);
			}
		}
		if (begin >= 0) {
			fireEvent(MapDataEvent.REMOVED, lst);
		}
			
		return removed;
	}
		
	public Object set(int index, Object element) {
		Object ret = _list.set(index, element);
		fireEvent(MapDataEvent.REMOVED, Collections.singletonList(ret));
		fireEvent(MapDataEvent.ADDED, Collections.singletonList(element));
		return ret;
	}
	
	public int size() {
		return _list.size();
	}

	public List subList(int fromIndex, int toIndex) {
		List list = _list.subList(fromIndex, toIndex);
		return new MapModelList(list);
	}
	
	public Object[] toArray() {
		return _list.toArray();
	}

	public Object[] toArray(Object[] a) {
		return _list.toArray(a);
	}

}
