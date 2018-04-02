package org.zkoss.gmapstest.test2;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.LatLng;
import org.zkoss.gmaps.MapModelList;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

public class ZKGMAPS_8_MyGmapsTestComposer extends SelectorComposer {

	@Wire("#myGmaps")
	private Gmaps myGmaps;
	private MapModelList myMapModel = new MapModelList();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		myGmaps.setWidth("600px");
		myGmaps.setHeight("600px");
		for (int j = 0; j < 10; j++) {
			Gmarker thisMarker = new Gmarker("test"+j, new LatLng(0+j, 0+j));
			thisMarker.setContent("<html><strong>test"+j+"</strong></html>");
			thisMarker.setOpen(true);
			myMapModel.add(thisMarker);
		}
		myGmaps.setModel(myMapModel);
		
	}
		
	@Listen("onClick = #btnCenter")
	public void handleCenter(){
		myGmaps.setCenter(new LatLng(5, 5));
		myGmaps.setZoom(6);
	}
	
	@Listen("onClick = #btnHide")
	public void handleHide(){
		 for (Object object: myMapModel.getInnerList()) {
             if (object instanceof Gmarker) {
            	 Gmarker location = (Gmarker)object;
            	 String msg = "toggleLabels:for content=" + location.getContent()+ " isOpen="+location.isOpen() + " changing to= false";
                 location.setOpen(false);
                 System.out.println(msg.concat(" / result (" + location.isOpen() + ")"));
             }
         }
	}

	@Listen("onClick = #btnShow")
	public void handleShow(){
		 for (Object object: myMapModel.getInnerList()) {
             if (object instanceof Gmarker) {
            	 Gmarker location = (Gmarker)object;
            	 String msg = "toggleLabels:for content=" + location.getContent()+ " isOpen="+location.isOpen() + " changing to= true";
                 location.setOpen(true);
                 System.out.println(msg.concat(" / result (" + location.isOpen() + ")"));
             }
         }
	}

}
