package gov.sgpr.fgv.osc.portalosc.map.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

import gov.sgpr.fgv.osc.portalosc.map.client.controller.MapController;
import gov.sgpr.fgv.osc.portalosc.map.client.controller.MenuController;
import gov.sgpr.fgv.osc.portalosc.map.client.controller.SearchController;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.ConfigService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.ConfigServiceAsync;

public class Map implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private MapController maps = new MapController();
	private MenuController menu = new MenuController();
	private SearchController search = new SearchController();
	//private MapServiceAsync mapService = GWT.create(MapService.class);
	private ConfigServiceAsync configService = GWT.create(ConfigService.class);

	public void onModuleLoad() {
		logger.info("Iniciando carregamento do mapa");
		try{
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, caught.getMessage());
					Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
				}
				
				public void onSuccess(Boolean result) {
					if (!result) {
						Window.Location.assign(GWT.getHostPageBaseURL() + "manutencao.html");
					} else {
						
						String session = Storage.getSessionStorageIfSupported().getItem("popup-exibida");
						if(session == null){
							openPopup();
							Storage.getSessionStorageIfSupported().setItem("popup-exibida", "1");
						}
						
						maps.init();
						menu.setMap(maps.getInstance(), search.getInstance());
						menu.init();
						search.init();
					}
				}
	
			};
			configService.isClusterCreated(callback);
		}catch(Exception e){
			logger.info("Ocoreu um erro na classe " + this.getClass().getName() + ": " + e.getMessage());
			Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
		}
	}
	
	private void openPopup(){
		final PopupPanel popup = new PopupPanel();
		popup.add(getHtmlPopup());
		popup.show();
		
		addClose(new EventListener() {
			public void onBrowserEvent(Event event) {
				popup.hide();
			}
		});
		
		addStopPropagation(new EventListener() {
			public void onBrowserEvent(Event event) {
				event.stopPropagation();
			}
		});
	}
	
	private void addClose(EventListener listener){
		Element fechar = DOM.getElementById("fechar");
		Element nInteresse = DOM.getElementById("nInteresse");
		Element overlay = DOM.getElementById("overlay");
		Event.sinkEvents(fechar, Event.ONCLICK);
		Event.sinkEvents(nInteresse, Event.ONCLICK);
		Event.sinkEvents(overlay, Event.ONCLICK);
		Event.setEventListener(fechar, listener);	
		Event.setEventListener(nInteresse, listener);
		Event.setEventListener(overlay, listener);
	}
	
	private void addStopPropagation(EventListener listener){
		Element pop = DOM.getElementById("popup");
		Event.sinkEvents(pop, Event.ONCLICK);
		Event.setEventListener(pop, listener);
	}
	
	private static HTML getHtmlPopup() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div id='overlay' class='overlay'>");
		htmlBuilder.append("<div id='popup' class='popup_representante'>");
		htmlBuilder.append("<a id='fechar' class='fechar'>X</a>");
		htmlBuilder.append("<div style='padding-top: 80px'>");
		htmlBuilder.append("<div>As OSCs agora podem inserir ou editar dados em suas páginas no Mapa. <strong>Representante de OSC</strong>, aumente a transparência da sua entidade e mantenha a sua página atualizada!.</div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div style='padding-top: 40px'>");
		htmlBuilder.append("<div class='botoes'><a class='botao' target='_blank' href='tutorial.pdf'>Saiba Como</a><a class='botao' id='nInteresse' >Não tenho interesse</a></div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
			
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
}
