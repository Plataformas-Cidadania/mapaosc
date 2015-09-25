package gov.sgpr.fgv.osc.portalosc.map.client.controller;

import gov.sgpr.fgv.osc.portalosc.map.client.components.BreadcrumbWidget;
import gov.sgpr.fgv.osc.portalosc.map.client.components.MenuWidget;
import gov.sgpr.fgv.osc.portalosc.map.client.components.OrganizationWidget;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.AbstractMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.AnchorListMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.BreadcrumbItem;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.Infographic;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.KeyValueMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.SimpleTextMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.BoundingBox;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.DataSource;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscDetail;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscMain;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscMenuSummary;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Place;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.PlaceType;
import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.LatLng;

public class MenuController implements ValueChangeHandler<String> {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private final RootPanel menuPanel = RootPanel.get("menu_mapa");
	private final RootPanel indicatorsPanel = RootPanel.get("menu_infograficos");
	private PlaceServiceAsync placeService = GWT.create(PlaceService.class);
	private OscServiceAsync oscService = GWT.create(OscService.class);
	private MapServiceAsync mapService = GWT.create(MapService.class);
	private List<Place> placesList = new ArrayList<Place>();
	private List<String> historyTokens = new ArrayList<String>();
	private final BreadcrumbWidget breadcrumb = new BreadcrumbWidget();
	private static MapController map;
	private static SearchController search;
	private static InfographicsController info;
	private static MatrixController matrix;
	private HandlerRegistration handleControl;

	public void setMap(MapController map, SearchController search) {
		MenuController.map = map;
		MenuController.search = search;
	}

	public void init() {
		logger.info("iniciando menu");
		loadInfographicsMenu();
		String initToken = History.getToken();
		
		if (initToken.length() == 0) History.newItem(null);
		else History.newItem(initToken);

		History.addValueChangeHandler(this);
		History.fireCurrentHistoryState();
		
		AsyncCallback<Place[]> callbackPlaces = new AsyncCallback<Place[]>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Place[] result) {
				loadPlaces(result, true);
			}
		};
		placeService.getPlaces(PlaceType.REGION, callbackPlaces);
	}

	private void loadPlaces(Place[] places, boolean clearBreadcrumb) {
		String type = "P";
		menuPanel.clear();
		@SuppressWarnings("rawtypes")
		List<AbstractMenuItem> menuItems = new ArrayList<AbstractMenuItem>();
		
		for (Place place : places) {
			placesList.add(place);
			NumberFormat fmtCurrency = NumberFormat.getCurrencyFormat();
			NumberFormat fmtNumber = NumberFormat.getDecimalFormat();
			KeyValueMenuItem item = new KeyValueMenuItem();
			String id = type+String.valueOf(place.getId());
			
			item.setId(id);
			item.setCssClass("dados");
			item.setItemTitle(place.getName());
			item.setItemValue(id);
			
			for (Map.Entry<String, Double> entry : place.getIndicators().entrySet()) {
				if (entry.getKey().contains("Valor"))
					item.addInfo(entry.getKey(), fmtCurrency.format(entry.getValue()));
				else
					item.addInfo(entry.getKey(), fmtNumber.format(entry.getValue()));
			}
			menuItems.add(item);
		}
		
		MenuWidget menu = new MenuWidget(menuItems);
		if (clearBreadcrumb) breadcrumb.clearBreadcrumb();
		else menuPanel.add(breadcrumb.getBreadcrumbHtml());
		
		HTML instruction = new HTML("<h3>Selecione a localização:</h3>");
		menuPanel.add(instruction);
		menuPanel.add(menu);
		// initFunction();
	}

	private void loadInfographicsMenu() {

		StringBuilder igmBuilder = new StringBuilder();
		igmBuilder.append("<h3>Selecione o infográfico:</h3>");
		igmBuilder.append("<h4><a id=\"I01\" href=\"#I01\">OSCs em Números</a></h4>");
		igmBuilder.append("<h4><a id=\"I02\" href=\"#I02\">OSCs e os Recursos</a></h4>");
		igmBuilder.append("<h4><a id=\"I03\" href=\"#I03\">OSCs Natureza jurídica/Faixas de vínculos</a></h4>");
		igmBuilder.append("<h3>ou </h3>");
		igmBuilder.append("<h4><a id=\"M0\" href=\"#M0\">Matriz de indicadores</a></h4>");

		HTML menu = new HTML(igmBuilder.toString());
		indicatorsPanel.add(menu);
		info = new InfographicsController();
		info.setVisible(false);
		matrix = new MatrixController();
		matrix.setVisible(false);

		final Element tabMapa = DOM.getElementById("tab_mapa");
		Event.sinkEvents(tabMapa, Event.ONCLICK);
		Event.setEventListener(tabMapa, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				final Element divMapa = DOM.getElementById("mapa");

				map.setVisible(true);
				search.setVisible(true);
				info.setVisible(false);
				divMapa.getStyle().setDisplay(Display.BLOCK);
				divMapa.setClassName("");
				initFunction();
				clearHash();
				removeResizeHandler();
				map.addResizeHandler();
			}
		});
	}

	private void setupInfographics() {
		final Element divMapa = DOM.getElementById("mapa");
		map.setVisible(false);
		search.setVisible(false);
		matrix.setVisible(false);
		info.setVisible(true);
		divMapa.getStyle().setDisplay(Display.INLINE_BLOCK);
		divMapa.setClassName("infograficos");
		initFunction();
		map.removeResizeHandler();
		addResizeHandler();
	}

	private void setupMatrix() {
		final Element divMapa = DOM.getElementById("mapa");
		map.setVisible(false);
		search.setVisible(false);
		info.setVisible(false);
		matrix.setVisible(true);
		divMapa.getStyle().setDisplay(Display.INLINE);
		divMapa.setClassName("infograficos");
		initFunction();
		map.removeResizeHandler();
		addResizeHandler();
	}

	private void loadOrganizations(SortedMap<String, Integer> oscs) {
		menuPanel.clear();
		if (!oscs.isEmpty()) {
			@SuppressWarnings("rawtypes")
			List<AbstractMenuItem> menuItems = new ArrayList<AbstractMenuItem>();
			
			for (Map.Entry<String, Integer> osc : oscs.entrySet()) {
				KeyValueMenuItem item = new KeyValueMenuItem();
				item.setId("O" + osc.getValue());
				item.setCssClass("dados");
				item.setItemTitle(osc.getKey());
				item.setItemValue("O" + osc.getValue());
				menuItems.add(item);
			}
			
			MenuWidget menu = new MenuWidget(menuItems);
			menuPanel.add(breadcrumb.getBreadcrumbHtml());
			HTML instruction = new HTML("<h3>Selecione a entidade:</h3>");
			menuPanel.add(instruction);
			menuPanel.add(menu);
			// initFunction();
		} 
		else {
			menuPanel.add(breadcrumb.getBreadcrumbHtml());
			HTML instruction = new HTML("<h3>Desculpe, mas não há entidades neste município.</h3>");
			menuPanel.add(instruction);
		}
	}

	private void loadOrganization(OscDetail osc) {
		final OscMenuSummary menuInfo = new OscMenuSummary();
		final KeyValueMenuItem mainItem = new KeyValueMenuItem(osc.getMain());
		OscMain oscmain = osc.getMain();
		
		menuInfo.setTitle(oscmain.getName());
		menuInfo.setOscId(oscmain.getId());
		menuInfo.setLikeCounter(osc.getRecommendations());
		menuInfo.setRecommended(osc.isRecommended());
		mainItem.setItemTitle("Dados gerais");
		mainItem.setId("dados_gerais");
		mainItem.setCssClass("dados");
		
		if (osc.getMain().getDataSources().length > 0)
			mainItem.setInfoSource(getHelpContent(oscmain.getDataSources()));

		final AnchorListMenuItem documentsItem = new AnchorListMenuItem();
		documentsItem.setItemTitle("Acesso a informação (Documentos)");
		documentsItem.setId("doc");
		documentsItem.setCssClass("dados documentos");
		documentsItem.addInfo("Prestação de contas", osc.getAccountabilityPath());
		documentsItem.addInfo("Estatuto", osc.getByLawPath());
		documentsItem.addInfo("Quadro de diretores", osc.getDirectorsBoardPath());
		documentsItem.addInfo("Convênios", osc.getTreatyPath());
		DataSource[] ds = { osc.getDocumentDataSource() };
		documentsItem.setInfoSource(getHelpContent(ds));

		final KeyValueMenuItem localizationItem = new KeyValueMenuItem();
		localizationItem.setItemTitle("Localização");
		localizationItem.setId("local");
		localizationItem.setCssClass("dados clearfix");
		localizationItem.addInfo("Latitude", String.valueOf(osc.getCoordinate().getY()));
		localizationItem.addInfo("Longitude", String.valueOf(osc.getCoordinate().getX()));

		final KeyValueMenuItem publicResourcesItem = new KeyValueMenuItem(osc.getPublicResources());
		publicResourcesItem.setItemTitle("Recursos públicos");
		publicResourcesItem.setId("recursos");
		publicResourcesItem.setCssClass("dados");
		String titleToolTip = "Os recurso públicos aqui apresentados são referentes </br>"
				+ "às parcerias realizadas com o governo federal através do<br> SICONV e os recursos "
				+ "obtidos através de Leis de Incentivo";
		
		publicResourcesItem.setTitleToolTip(titleToolTip);
		
		if (osc.getPublicResources().getDataSources().length > 0)
			publicResourcesItem.setInfoSource(getHelpContent(osc.getPublicResources().getDataSources()));

		final KeyValueMenuItem workRelationshipItem = new KeyValueMenuItem(osc.getWorkRelationship());
		workRelationshipItem.setItemTitle("Relações de trabalho");
		workRelationshipItem.setId("trab");
		workRelationshipItem.setCssClass("dados");
		if (osc.getWorkRelationship().getDataSources().length > 0)
			workRelationshipItem.setInfoSource(getHelpContent(osc.getWorkRelationship().getDataSources()));

		KeyValueMenuItem certificationsItem = new KeyValueMenuItem(osc.getCertifications());
		certificationsItem.setItemTitle("Certificações");
		certificationsItem.setId("cert");
		certificationsItem.setCssClass("dados");

		if (osc.getCertifications().getDataSources().length > 0)
			certificationsItem.setInfoSource(getHelpContent(osc.getCertifications().getDataSources()));

		AbstractMenuItem<?> committeesItem = null;
		
		if (!osc.getCommittees().getCommittees().isEmpty()) {

			final KeyValueMenuItem keyValueItem = new KeyValueMenuItem(osc.getCommittees());

			keyValueItem.setItemTitle("Conselhos e comissões");
			keyValueItem.setId("cons");
			keyValueItem.setCssClass("dados");
			
			if (osc.getCommittees().getDataSources().length > 0)
				keyValueItem.setInfoSource(getHelpContent(osc.getCommittees().getDataSources()));
			
			committeesItem = keyValueItem;
		}
		else {
			final SimpleTextMenuItem textItem = new SimpleTextMenuItem();
			textItem.setItemTitle("Conselhos e Comissões");
			textItem.setId("cons");
			textItem.setCssClass("dados");
			textItem.setInfo("Esta Organização não participa de nenhum conselho ou comissão. ");
			committeesItem = textItem;
		}
		
		@SuppressWarnings("rawtypes")
		List<AbstractMenuItem> menuItems = new ArrayList<AbstractMenuItem>();

		menuItems.add(mainItem);
		menuItems.add(publicResourcesItem);
		menuItems.add(workRelationshipItem);
		menuItems.add(certificationsItem);
		menuItems.add(documentsItem);
		menuItems.add(committeesItem);
		// menuItems.add(localizationItem);

		final OrganizationWidget oscInfo = new OrganizationWidget(menuInfo, menuItems);

		menuPanel.add(breadcrumb.getBreadcrumbHtml());
		menuPanel.add(oscInfo);
		// initFunction();

		centerMap(LatLng.create(osc.getCoordinate().getY(), osc.getCoordinate().getX()));

		changeIcons(osc.getMain().getId());
	}
	
	public void onValueChange(ValueChangeEvent<String> event) {
		final String token = event.getValue();
		
		if (!token.isEmpty()) {
			resetAllIcons();
			final String tokenType = String.valueOf(token.charAt(0));
			final String tokenId = token.substring(1);
			
			if (isInteger(tokenId, 10)){
				if (tokenType.equals("P") && !token.equals("P0"))	processPlaces(token, tokenId);
				else if (tokenType.equals("O"))	processOrganizations(token, tokenId);
				else if (token.equals("P0"))	processPlace(token, tokenId);
				else if (tokenType.equals("I"))	processInfographic(token);
				else if (tokenType.equals("M"))	processMatrix(tokenId);
			}
		}
	}

	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	
	private void processMatrix(String tokenId) {
		matrix.loadMatrix(tokenId);
		matrix.setVisible(true);
		setupMatrix();
	}

	private void processInfographic(String token) {
		Infographic i = Infographic.get(token);
		info.loadInfo(i);
		info.setVisible(true);
		setupInfographics();
		historyTokens.add(token);
	}

	private void processPlace(String token, String tokenId) {
		breadcrumb.clearBreadcrumb();
		AsyncCallback<Place[]> callbackPlaces = new AsyncCallback<Place[]>() {

			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Place[] result) {
				loadPlaces(result, true);
			}
		};
		
		placeService.getPlaces(PlaceType.REGION, callbackPlaces);
	}

	private void processOrganizations(String token, String tokenId) {
		String email = Cookies.getCookie("oscUid");
		Integer idToken = Integer.parseInt(tokenId);
		
		AsyncCallback<OscDetail> callbackDetails = new AsyncCallback<OscDetail>() {

			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(final OscDetail resultOsc) {

				AsyncCallback<Place[]> callbackBreadcrumb = new AsyncCallback<Place[]>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
					}

					public void onSuccess(Place[] result) {
						menuPanel.clear();
						if (result.length > 0)
							breadcrumb.clearBreadcrumb();
						for (Place place : result) {
							BreadcrumbItem item = new BreadcrumbItem();
							item.setItemId("P" + place.getId());
							item.setItemText(place.getName());
							
							if (!breadcrumb.isItemOnBreadcrumb(item.getItemId())) breadcrumb.addItem(item);
							else breadcrumb.removeLastItemUntil(item);
						}

						//verificação retirada segundo a tarefa MOSC-156
						loadOrganization(resultOsc);
					}
				};
				placeService.getPlaceAncestorsInfo(resultOsc.getMain().getCountyId(), callbackBreadcrumb);
			}
		};
		
		oscService.getDetail(idToken, email, callbackDetails);
	}

	private void processPlaces(final String token, String tokenId) {
		Integer idToken = Integer.parseInt(tokenId);
		
		breadcrumb.setType("P");
		
		AsyncCallback<Place[]> callbackBreadcrumb = new AsyncCallback<Place[]>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Place[] result) {
				if (result.length > 0)	breadcrumb.clearBreadcrumb();
				
				for (Place place : result) {
					BreadcrumbItem item = new BreadcrumbItem();
					item.setItemId(String.valueOf(place.getId()));
					item.setItemText(place.getName());
					
					if (!breadcrumb.isItemOnBreadcrumb(item.getItemId()))
						breadcrumb.addItem(item);
					else
						breadcrumb.removeLastItemUntil(item);
				}
				historyTokens.add(token);
			}
		};
		
		placeService.getPlaceAncestorsInfo(idToken, callbackBreadcrumb);

		AsyncCallback<Place> callbackPlace = new AsyncCallback<Place>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Place result) {
				fitBoundsMap(result.getBoundingBox());
			}
		};
		
		placeService.getPlaceInfo(idToken, callbackPlace);
		
		if (tokenId.length() != 7) {
			AsyncCallback<Place[]> callbackPlaces = new AsyncCallback<Place[]>() {
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, caught.getMessage());
				}

				public void onSuccess(Place[] result) {
					loadPlaces(result, false);
				}
			};
			placeService.getPlaces(idToken, callbackPlaces);
		}
		else {
			AsyncCallback<SortedMap<String, Integer>> callbackOsc = new AsyncCallback<SortedMap<String, Integer>>() {
				public void onFailure(Throwable caught) {
					logger.log(Level.SEVERE, caught.getMessage());
				}

				public void onSuccess(SortedMap<String, Integer> result) {
					loadOrganizations(result);
				}
			};
			
			boolean all = UserController.isMasterUser();
			
			placeService.getOsc(idToken, all, callbackOsc);
		}
	}

	/**
	 * @param recommended
	 *            recomendação do usuário Envia a recomendação para banco
	 */

	public void RecommendationManager(boolean recommended, int oscId) {
		String email = Cookies.getCookie("oscUid");

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Void result) {
				logger.info("Recomendacao add com sucesso");
			}
		};
		
		//Verificação de usuário logado para poder recomendar uma OSC - MOSC-156
		if (UserController.hasLoggedUser() == true) oscService.setRecommendation(oscId, email, recommended, callback);
		else  Window.Location.replace(com.google.gwt.core.client.GWT.getHostPageBaseURL() + "User.html");
	}

	private static String getHelpContent(DataSource[] dataSources) {
		DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy");
		StringBuilder dsBuilder = new StringBuilder();
		
		for (DataSource ds : dataSources) {
			if (ds == null) continue;
			
			dsBuilder.append("<p>");
			dsBuilder.append("<a class=\"ajuda\" href='");
			dsBuilder.append(ds.getSiteURL());
			dsBuilder.append("'>");
			dsBuilder.append("<strong>");
			dsBuilder.append(ds.getAcronym());
			dsBuilder.append("</strong>");
			dsBuilder.append("</a>");
			dsBuilder.append("<br>");
			dsBuilder.append(ds.getName());
			
			if (ds.getAcquisitionDate() != null) {
				dsBuilder.append("<br> Data de Aquisição:");
				dsBuilder.append(fmt.format(ds.getAcquisitionDate()));
			}
			
			dsBuilder.append("</p>");
			dsBuilder.append("<br>");
		}
		return dsBuilder.toString();
	}

	public static void fitBoundsMap(BoundingBox box) {
		map.fitBoundsToView(box);
	}

	public static void changeIcons(int oscId) {
		map.changeToSelectedIcon(oscId);
	}

	public static void centerMap(LatLng center) {
		map.centerMap(center);
	}

	public static void resetAllIcons() {
		map.resetIcon();
	}

	public void removeResizeHandler() {
		if (handleControl != null) handleControl.removeHandler();
	}

	public void addResizeHandler() {
		handleControl = Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				initFunction();
			}
		});
	}

	public static native void clearHash() /*-{
		$wnd.location.hash = '';
		$wnd.history.pushState('', $wnd.document.title, $wnd.location.pathname);
	}-*/;
	
	public static native void initFunction() /*-{
		$wnd.jQuery($doc).ready(
				function() {
					if ($wnd.jQuery('.organizacao span').text().length > 90) {
						$wnd.jQuery('.organizacao span').text(
								$wnd.jQuery('.organizacao span').text()
										.substring(0, 90)
										+ '...');
					}
					//$wnd.jQuery('.contraste').hide();

					//$wnd.tooltips_padrao();
					$wnd.redimensionarMapa();
					$wnd.redimensionarGraficos();

				});
		function tooltips_padrao() {
			$wnd.jQuery('.tooltip').qtip({
				position : {
					my : 'bottom center',
					at : 'top center',
					viewport : $wnd.jQuery(window)
				},
				style : {
					classes : 'qtip-tipsy'
				}
			});
			$wnd.jQuery('#mapa_navegacao h4 a').qtip({
				position : {
					my : 'bottom left',
					at : 'top center'
				},
				style : {
					classes : 'qtip-tipsy'
				}
			});
			$wnd.jQuery('#ajuda').qtip({
				position : {
					viewport : $wnd.jQuery(window)
				},
				content : {
					text : $wnd.jQuery('.ajuda')
				},
				style : {
					classes : 'qtip-tipsy'
				}
			});
			$wnd.jQuery('.tip_recomendacao').qtip({
				position : {
					my : 'bottom center',
					at : 'top center',
					viewport : $wnd.jQuery(window)
				},
				style : {
					classes : 'qtip-tipsy',
					width : 140
				}
			});
			$wnd.jQuery('#logo').qtip({
				position : {
					my : 'top center',
					at : 'bottom center',
					viewport : $wnd.jQuery(window)
				},
				style : {
					classes : 'qtip-tipsy'
				}
			});
			$wnd.jQuery('.tip_menu').qtip({
				position : {
					my : 'bottom center',
					at : 'top center',
					viewport : $wnd.jQuery(window)
				},
				style : {
					classes : 'qtip-tipsy'
				}
			});
		}

		function tooltips_contraste() {
			$wnd.jQuery('.tooltip').qtip({
				position : {
					my : 'bottom center',
					at : 'top center',
					viewport : $wnd.jQuery(window)
				},
				style : {
					classes : 'qtip-tipsy-contraste'
				}
			});
			$wnd.jQuery('#mapa_navegacao h4 a').qtip({
				style : {
					classes : 'qtip-tipsy-contraste'
				}
			});
			$wnd.jQuery('#ajuda').qtip({
				position : {
					viewport : $wnd.jQuery(window)
				},
				content : {
					text : $wnd.jQuery('.ajuda')
				},
				style : {
					classes : 'qtip-tipsy-contraste'
				}
			});
			$wnd.jQuery('.tip_recomendacao').qtip({
				position : {
					my : 'bottom center',
					at : 'top center',
					viewport : $wnd.jQuery(window)
				},
				style : {
					classes : 'qtip-tipsy-contraste',
					width : 140
				}
			});
			$wnd.jQuery('#logo_contraste').qtip({
				position : {
					my : 'top center',
					at : 'bottom center',
					viewport : $wnd.jQuery(window)
				},
				style : {
					classes : 'qtip-tipsy-contraste'
				}
			});

		}
		
		if($wnd.jQuery("#mapa.infograficos").length > 0) {
			$wnd.jQuery("#tab_indicadores").click();
		}
		
		$wnd.jQuery("#contraste_normal").click(function() {
			$wnd.chooseStyle('none');
			$wnd.jQuery('.normal').show();
			$wnd.jQuery('.contraste').hide();
			$wnd.tooltips_padrao();
		});

		$wnd.jQuery("#alto_contraste").click(function() {
			$wnd.chooseStyle('contrast', 0);
			$wnd.jQuery('.contraste').show();
			$wnd.jQuery('.normal').hide();
			$wnd.tooltips_contraste();
		});

	}-*/;
}