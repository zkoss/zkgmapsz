/* MapModel.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Jul 8 14:06:36     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import java.util.Collection;

import org.zkoss.gmaps.event.MapDataListener;

/** This interface defines the methods that {@link Gmaps} used to get the content of items.
 * 
 * @author henrichen
 * @see Gmaps
 * @see MapitemRenderer
 */
public interface MapModel {
	/**
	 * Returns the items in the specified bounds (south-west to north-east), 
	 * center latitude, center longitude, and zoom level.
	 * @param swlat south-west latitude
	 * @param swlng south-west longitude
	 * @param nelat north-east latitude
	 * @param nelng north-east longitude
	 * @param cenlat center latitude
	 * @param cenlng center longitude
	 * @param zoom current zoom level
	 * @return the items in the specified bounds (south-west to north-east)
	 * center latitude, center longitude, and zoom level.
	 */
	public Collection getItemsIn(double swlat, double swlng, 
			double nelat, double nelng, double cenlat, double cenlng, int zoom);

	/**
	 * Clear all items of this model.
	 */
	public void clear();
	
	/**
	 * Adds a listener to the Maps that's notified each time a change
	 * to the data model occurs.
	 * @param listener
	 */
	public void addMapDataListener(MapDataListener listener);
	
	/**
	 * Remove a listener from the list that's notified each time 
	 * a change to the the data model occurs. 
	 * @param listener
	 */
	public void removeMapDataListener(MapDataListener listener);
}
