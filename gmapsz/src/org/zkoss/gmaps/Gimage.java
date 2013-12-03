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

import java.util.HashMap;
import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.ext.render.DynamicMedia;
import org.zkoss.zul.impl.Padding;
import org.zkoss.zul.impl.Utils;
import org.zkoss.zul.impl.XulElement;

/**
 * Google Maps support Gimage.
 * 
 * @author henrichen
 * @since 2.0_7
 */
public class Gimage extends XulElement implements Mapitem {
	private static final long serialVersionUID = 200801071632L;
	protected LatLngBounds _bounds = new LatLngBounds(new LatLng(37.4419, -122.1419), new LatLng(37.4419, -122.1419));

	/** image source */
	private String _src;
	/** The image. If not null, _src is generated automatically. */
	private org.zkoss.image.Image _image;
	/** Count the version of {@link #_image}. */
	private int _imgver;

	public Gimage() {
	}
	
	public Gimage(String src, LatLngBounds bounds) {
		setSrc(src);
		setBounds(bounds);
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
		this(src, new LatLngBounds(new LatLng(swlat, swlng), new LatLng(nelat, nelng)));
	}
	
	/**
	 * Returns the bounded south west latitude.
	 * @return the bounded south west latitude.
	 */
	public void setBounds(LatLngBounds bounds) {
		if (!_bounds.equals(bounds)) {
			_bounds = bounds;
			smartRerender();
		}
	}	
	
	/**
	 * Returns the bounded south west latitude.
	 * @return the bounded south west latitude.
	 */
	public LatLngBounds getBounds() {
		return _bounds;
	}
	
	/**
	 * Returns the bounded south west latitude.
	 * @return the bounded south west latitude.
	 */
	public double getSwlat() {
		return _bounds.getSouthWest().getLatitude();
	}
	/**
	 * Sets the bounded south west latitude.
	 * @param swlat south west latitude
	 */
	public void setSwlat(double swlat) {
		if (!Objects.equals(_bounds.getSouthWest().getLatitude(), swlat)) {
			setBounds(new LatLngBounds(new LatLng(swlat, _bounds.getSouthWest().getLongitude()), _bounds.getNorthEast()));
		}
	}
	/**
	 * Returns the bounded south west longitude.
	 * @return the bounded south west longitude.
	 */
	public double getSwlng() {
		return _bounds.getSouthWest().getLongitude();
	}
	/**
	 * Sets the bounded south west longitude.
	 * @param swlng south west longitude
	 */
	public void setSwlng(double swlng) {
		if (!Objects.equals(_bounds.getSouthWest().getLongitude(), swlng)) {
			setBounds(new LatLngBounds(new LatLng(_bounds.getSouthWest().setLatitude(), swlng), _bounds.getNorthEast()));
		}
	}
	/**
	 * Returns the bounded north east latitude.
	 * @return the bounded north east latitude.
	 */
	public double getNelat() {
		return _bounds.getNorthEast().getLatitude();
	}
	/**
	 * Sets the bounded north east latitude.
	 * @param nelat the bounded north east latitude.
	 */
	public void setNelat(double nelat) {
		if (!Objects.equals(_bounds.getNorthEast().getLatitude(), nelat)) {
			setBounds(new LatLngBounds(_bounds.getSouthWest(), new LatLng(nelat, _bounds.getNorthEast().getLongitude())));
		}
	}
	/**
	 * Returns the bounded north east longitude.
	 * @return the bounded north east longitude.
	 */
	public double getNelng() {
		return _bounds.getNorthEast().getLongitude();
	}
	/**
	 * Sets the bounded north east longitude.
	 * @param nelng the bounded north east longitude.
	 */
	public void setNelng(double nelng) {
		if (!Objects.equals(_bounds.getNorthEast().getLongitude(), nelng)) {
			setBounds(new LatLngBounds(_bounds.getSouthWest(), new LatLng(_bounds.getNorthEast().getLatitude(), nelng)));
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

		if (_image != null || !Objects.equals(_src, src)) {
			_src = src;
			_image = null;
			smartRerender();
		}
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
			smartRerender();
		}
	}
	/** Returns the content set by {@link #setContent}.
	 * <p>Note: it won't fetch what is set thru by {@link #setSrc}.
	 * It simply returns what is passed to {@link #setContent}.
	 */
	public org.zkoss.image.Image getContent() {
		return _image;
	}

	//-- Component --//
	/** Default: not childable.
	 */
	public boolean isChildable() {
		return false;
	}

	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);
		render(renderer, "src", getEncodedURL());
		render(renderer, "swlat", new Double( _bounds.getSouthWest().getLatitude()));
		render(renderer, "swlng", new Double( _bounds.getSouthWest().getLongitude()));
		render(renderer, "nelat", new Double( _bounds.getNorthEast().getLatitude()));
		render(renderer, "nelng", new Double( _bounds.getNorthEast().getLongitude()));
	}
	
	/** Returns the encoded URL of the image (never null).
	 */
	private String getEncodedURL() {
		if (_image != null)
			return Utils.getDynamicMediaURI( //already encoded
				this, _imgver, "c/" + _image.getName(), _image.getFormat());

		final Desktop dt = getDesktop(); //it might not belong to any desktop
		return dt != null ? dt.getExecution()
			.encodeURL(_src != null ? _src: "~./img/spacer.gif"): "";
	}

	public Object getExtraCtrl() {
		return new ExtraCtrl();
	}

	/** A utility class to implement {@link #getExtraCtrl}.
	 * It is used only by component developers.
	 */
	protected class ExtraCtrl extends XulElement.ExtraCtrl
	implements DynamicMedia {
		//-- DynamicMedia --//
		public Media getMedia(String pathInfo) {
			return _image;
		}
	}
	
	private void smartRerender() {
		final Map info = new HashMap();
		info.put("src", getEncodedURL());
		info.put("swlat", new Double( _bounds.getSouthWest().getLatitude()));
		info.put("swlng", new Double( _bounds.getSouthWest().getLongitude()));
		info.put("nelat", new Double( _bounds.getNorthEast().getLatitude()));
		info.put("nelng", new Double( _bounds.getNorthEast().getLongitude()));
		
		smartUpdate("rerender_", info);
	}
}
