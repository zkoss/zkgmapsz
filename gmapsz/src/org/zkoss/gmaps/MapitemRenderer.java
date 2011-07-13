/* MapitemRenderer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Jul 8 18:16:23     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

/**
 * A {@link Mapitem} renderer which can render an item to a known Mapitem 
 * such as {@link Ginfo}, {@link Gmarker}, {@link Gpolyline}, {@link Gpolygon}
 * so Maps engine can use it directly.
 * 
 * @author henrichen
 * @see Ginfo
 * @see Gmarker
 * @see Gpolyline
 * @see Gpolygon
 */
public interface MapitemRenderer {

	/**
	 * Returns the associated drawable {@link Mapitem} instance per the given 
	 * data object. 
	 * @param data any data object to be drawn on the Maps.
	 * @return the associated drawable {@link Mapitem} per the given item.
	 */
	public Mapitem newMapitem(Object data);
}
