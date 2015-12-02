package gov.sgpr.fgv.osc.portalosc.map.client;

import gov.sgpr.fgv.osc.portalosc.map.client.controller.MapController;
import gov.sgpr.fgv.osc.portalosc.map.client.controller.MenuController;
import gov.sgpr.fgv.osc.portalosc.map.client.controller.SearchController;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;

public class Map implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private MapController maps = new MapController();
	private MenuController menu = new MenuController();
	private SearchController search = new SearchController();
	
	public void onModuleLoad() {
		logger.info("Iniciando carregamento do mapa");
		maps.init();
		menu.setMap(maps.getInstance(),search.getInstance());
		menu.init();
		search.init();
	}
}
