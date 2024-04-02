package org.zkoss.gmapstest.test2;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class ZKGMAPS_35_Composer  extends GenericForwardComposer {

	  @Wire private Gmarker marker;
	  @Wire private Gmaps gmaps;

	  private double actualLatitude = 37.4410;

	  public void onClick$btn(Event e) throws InterruptedException {
	    actualLatitude = actualLatitude + 0.00001;
	    gmaps.getChildren().clear();
	    Gmarker marker2 = new Gmarker("New", actualLatitude, -122.1490);
	    marker2.setParent(gmaps);
	    marker2.setIconImage("https://acf.rockybytes.com/i/962/java-development-kit.jpg");
	  }

	  public void doAfterCompose(Component comp) throws Exception {
	    super.doAfterCompose(comp);
	  }
	}