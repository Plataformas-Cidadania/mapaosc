package gov.sgpr.fgv.osc.portalosc.map.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import gov.sgpr.fgv.osc.portalosc.map.client.controller.MapController;
import gov.sgpr.fgv.osc.portalosc.map.client.controller.MenuController;
import gov.sgpr.fgv.osc.portalosc.map.client.controller.SearchController;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapServiceAsync;

public class Map implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private MapController maps = new MapController();
	private MenuController menu = new MenuController();
	private SearchController search = new SearchController();
	private MapServiceAsync mapService = GWT.create(MapService.class);

	public void onModuleLoad() {
		logger.info("Iniciando carregamento do mapa");
		try{
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, caught.getMessage());
	
				}
				
				public void onSuccess(Boolean result) {
					if (!result) {
						Window.Location.assign(GWT.getHostPageBaseURL() + "manutencao.html");
					} else {
						maps.init();
						menu.setMap(maps.getInstance(), search.getInstance());
						menu.init();
						search.init();
					}
				}
	
			};
			mapService.isClusterCreated(callback);
		}catch(Exception e){
			logger.info("Ocoreu um erro na classe " + this.getClass().getName() + ": " + e.getMessage());
			Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
		}
	}
}
