/* Gimage.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Jan 7, 2008 4:32:11 PM, Created by henrichen
}}IS_NOTE

Copyright (C) 2008 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/

package org.zkoss.gmaps;

import org.zkoss.lang.Objects;
import org.zkoss.lang.Strings;
import org.zkoss.xml.HTMLs;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.UiException;

/**
 * Google Maps support Gimage.
 * 
 * @author henrichen
 * @since 2.0_7
 */
public class Gimage extends HtmlBasedComponent implements Mapitem {
	protected double _swlat = 37.4419;
	protected double _swlng = -122.1419;
	protected double _nelat = 37.4419;
	protected double _nelng = -122.1419;

	/** image source */
	private String _src;
	/** The image. If not null, _src is generated automatically. */
	private org.zkoss.image.Image _image;
	/** Count the version of {@link #_image}. */
	private int _imgver;

	public Gimage() {
	}

	/** 
	 * Google Image (GGroundLayer).
	 * @param src the image url
	 * @param swlat south west latitude to put the image.
	 * @param swlng south west longitude to put the image.
	 * @param nelat north east latitude to put the image.
	 * @param nelng north east longitude to put the image.
	 */
	public Gimage(String src, double swlat, double swlng, double nelat, double nelng) {
		setSrc(src);
		setSwlat(swlat);
		setSwlng(swlng);
		setNelat(nelat);
		setNelng(nelng);
	}
	
	/**
	 * Returns the bounded south west latitude.
	 * @return the bounded south west latitude.
	 */
	public double getSwlat() {
		return _swlat;
	}
	/**
	 * Sets the bounded south west latitude.
	 * @param swlat south west latitude
	 */
	public void setSwlat(double swlat) {
		if (_swlat != swlat) {
			_swlat = swlat;
			invalidate();
		}
	}
	/**
	 * Returns the bounded south west longitude.
	 * @return the bounded south west longitude.
	 */
	public double getSwlng() {
		return _swlng;
	}
	/**
	 * Sets the bounded south west longitude.
	 * @param swlng south west longitude
	 */
	public void setSwlng(double swlng) {
		if (_swlng != swlng) {
			_swlng = swlng;
			invalidate();
		}
	}
	/**
	 * Returns the bounded north east latitude.
	 * @return the bounded north east latitude.
	 */
	public double getNelat() {
		return _nelat;
	}
	/**
	 * Sets the bounded north east latitude.
	 * @param nelat the bounded north east latitude.
	 */
	public void setNelat(double nelat) {
		if (_nelat != nelat) {
			_nelat = nelat;
			invalidate();
		}
	}
	/**
	 * Returns the bounded north east longitude.
	 * @return the bounded north east longitude.
	 */
	public double getNelng() {
		return _nelng;
	}
	/**
	 * Sets the bounded north east longitude.
	 * @param nelng the bounded north east longitude.
	 */
	public void setNelng(double nelng) {
		if (_nelng != nelng) {
			_nelng = nelng;
			invalidate();
		}
	}

	/** Returns the source URI of the image.
	 * <p>Default: null.
	 */
	public String getSrc() {
		return _src;
	}
	/** Sets the source URI of the image.
	 *
	 * <p>If {@link #setContent} is ever called with non-null,
	 * it takes heigher priority than this method.
	 *
	 * @param src the URI of the image source
	 */
	public void setSrc(String src) {
		if (src != null && src.length() == 0)
			src = null;

		if (!Objects.equals(_src, src)) {
			_src = src;
			if (_image == null)
				invalidate();
				//_src is meaningful only if _image is null
		}
	}
	/** Returns the encoded src ({@link #getSrc}).
	 */
	private String getEncodedSrc() {
		final Desktop dt = getDesktop(); //it might not belong to any desktop
		return _image != null ? getContentSrc(): //already encoded
			dt != null ? dt.getExecution().encodeURL(
				_src != null ? _src: "~./img/spacer.gif"): "";
	}

	/** Sets the content directly.
	 * Default: null.
	 *
	 * @param image the image to display. If not null, it has higher
	 * priority than {@link #getSrc}.
	 */
	public void setContent(org.zkoss.image.Image image) {
		if (image != _image) {
			_image = image;
			if (_image != null) ++_imgver; //enforce browser to reload image
			invalidate();
		}
	}
	/** Returns the content set by {@link #setContent}.
	 * <p>Note: it won't fetch what is set thru by {@link #setSrc}.
	 * It simply returns what is passed to {@link #setContent}.
	 */
	public org.zkoss.image.Image getContent() {
		return _image;
	}

	/** Returns the encoded URL for the current image content.
	 * Don't call this method unless _image is not null;
	 *
	 * <p>Used only for component template, not for application developers.
	 */
	private String getContentSrc() {
		return getDynamicMediaURI(
			this, _imgver, _image.getName(), _image.getFormat());
	}
	
	//To avoid ZK version 3.0.2 dependency, we copy-n-paste the org.zkoss.zul.impl.Utils#getDynamicMediaURI() here;
	/*package*/ static String getDynamicMediaURI(AbstractComponent comp,
	int version, String name, String format) {
		final Desktop desktop = comp.getDesktop();
		if (desktop == null) return ""; //no avail at client

		final StringBuffer sb = new StringBuffer(64).append('/');
		Strings.encode(sb, version);
		if (name != null || format != null) {
			sb.append('/');
			boolean bExtRequired = true;
			if (name != null && name.length() != 0) {
				sb.append(name.replace('\\', '/'));
				bExtRequired = name.lastIndexOf('.') < 0;
			} else {
				sb.append(comp.getId());
			}
			if (bExtRequired && format != null)
				sb.append('.').append(format);
		}
		return desktop.getDynamicMediaURI(comp, sb.toString()); //already encoded
	}

	//-- super --//
	public String getOuterAttrs() {
		final String attrs = super.getOuterAttrs();
		final StringBuffer sb = new StringBuffer(128);
		if (attrs != null) {
			sb.append(attrs);
		}
		HTMLs.appendAttribute(sb, "z.src", getEncodedSrc());
		HTMLs.appendAttribute(sb, "z.swlat", ""+getSwlat());
		HTMLs.appendAttribute(sb, "z.swlng", ""+getSwlng());
		HTMLs.appendAttribute(sb, "z.nelat", ""+getNelat());
		HTMLs.appendAttribute(sb, "z.nelng", ""+getNelng());
		HTMLs.appendAttribute(sb, "z.pid", getParent().getUuid());

		return sb.toString();
	}

	//-- Component --//
	/** Default: not childable.
	 */
	public boolean isChildable() {
		return false;
	}
}
