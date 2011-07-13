/* MapModelList.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Fri Jul 18 14:45:17     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps.event;

/**
 * Defines the methods used to listener when the content of
 * {@link org.zkoss.gmaps.MapModel} is changed.
 * 
 * @author henrichen
 * @see org.zkoss.gmaps.MapModel
 * @see MapDataEvent
 */
public interface MapDataListener {
	/**
	 * Sent when the contents of the Maps has changed.
	 * @param event
	 */
	public void onChange(MapDataEvent event);
}
