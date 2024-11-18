package org.zkoss.gmapstest.test2;

import org.zkoss.gmaps.GadvancedMarker;
import org.zkoss.gmaps.Gcircle;
import org.zkoss.gmaps.Gimage;
import org.zkoss.gmaps.Ginfo;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.Gpolygon;
import org.zkoss.gmaps.Gpolyline;
import org.zkoss.gmaps.Gscreen;
import org.zkoss.gmaps.LatLng;
import org.zkoss.gmaps.LatLngBounds;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;

public class ZKGMAPS_36_Composer extends SelectorComposer<Component> {

	@Wire
	Gmaps myMaps;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		myMaps.setCenter(0, 0);
		myMaps.setZoom(15);
		myMaps.setMapId("3dd20b4cb742b084");
		
//		myMaps.appendChild(new Gimage("~./test/Test-Logo.svg.png", new LatLngBounds(new LatLng(0, 0),new LatLng(0.1, 0.1))));
		Ginfo gInfo = new Ginfo("Ginfo", new LatLng(-0.01, -0.01));
		gInfo.setOpen(true);
//		myMaps.appendChild(gInfo);
		Gmarker gmarker = new Gmarker("Gmarker", new LatLng(0.001, -0.01));
		myMaps.appendChild(gmarker);
		GadvancedMarker gadvancedMarker = new GadvancedMarker("Gmarker", new LatLng(0.002, -0.01));
//		myMaps.appendChild(gadvancedMarker);
		Gpolyline gpolyline = new Gpolyline();
		gpolyline.setPath("0,0,0,0.1,0.1,0.2,0,0.3,0.1,0.4,0,0");
//		myMaps.appendChild(gpolyline);
		Gpolygon gpolygon = new Gpolygon();
		gpolygon.setPath("0,0,0,-0.1,-0.1,-0.2,0,-0.3,-0.1,-0.4,0,0");
//		myMaps.appendChild(gpolygon);
		Gcircle gcircle = new Gcircle();
		gcircle.setCenter(new LatLng(0.03, 0));
		gcircle.setRadius(2000);
		gcircle.setVisible(true);
		gcircle.setStrokeColor("red");
//		myMaps.appendChild(gcircle);
		
	}
	
}
