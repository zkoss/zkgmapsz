/* Mapitem.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Tue Jul 8 15:07:06     2008, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.gmaps;

import org.zkoss.zk.ui.Component;

/**
 * A marker interface to identify all items class that can be used inside 
 * {@link Gmaps} such as {@link Ginfo}, {@link Gmarker}, {@link Gpolyline}, 
 * {@link Gpolygon}.
 * 
 * @author henrichen
 * @see Ginfo
 * @see Gmarker
 * @see Gpolyline
 * @see Gpolygon
 */
public interface Mapitem extends Component {

}
