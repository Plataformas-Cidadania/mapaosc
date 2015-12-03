package gov.sgpr.fgv.osc.portalosc.map.client.controller;

import gov.sgpr.fgv.osc.portalosc.map.client.components.ClusterMarker;
import gov.sgpr.fgv.osc.portalosc.map.client.components.OscMarker;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.BoundingBox;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Cluster;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Coordinate;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscCoordinate;
import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.maps.gwt.client.ControlPosition;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.BoundsChangedHandler;
import com.google.maps.gwt.client.GoogleMap.ZoomChangedHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.PanControlOptions;
import com.google.maps.gwt.client.ZoomControlOptions;

public class MapController {
	public static final String MAP_CONTAINER = "mapa_google";
	public static final String CLUSTER_GROUP = "clusters";
	private static final LatLng DEFAULT_CENTER = LatLng.create(-8.581021, -52.785187);
	private static final int DELAY = 300;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private HashMap<Integer, Marker> markerMap = new HashMap<Integer, Marker>();
	private MapServiceAsync mapService = GWT.create(MapService.class);
	private Element loading = null;
	private GoogleMap map = null;
	private Set<Marker> markers = new HashSet<Marker>();
	private Date changeDate;
	private int selectedId;
	private boolean isSelected = false;
	private HandlerRegistration handleControl;

	public MapController getInstance() {
		return this;
	}

	public HashMap<Integer, Marker> getMarkerMap() {
		return markerMap;
	}

	public void changeToSelectedIcon(int oscId) {
		selectedId = oscId;
		isSelected = true;
	}

	public void resetIcon() {
		isSelected = false;
	}

	public void centerMap(LatLng center) {
		map.setZoom(20);
		map.setCenter(center);
	}

	public LatLng getCenter() {
		return map.getCenter();
	}

	public void init() {
		initFunction();
		// logger.info("Iniciando log");
		LatLng center = LatLng.create(-13.239945, -51.606447);

		logger.info("Criando mapa");
		MapOptions opts = MapOptions.create();

		// logger.info("Centrando mapa");
		opts.setCenter(center);
		opts.setMapTypeId(MapTypeId.ROADMAP);
		
		/* limit zoom-out */
		opts.setMinZoom(3);
		
		opts.setZoom(4);

		// logger.info("Setando controles");
		PanControlOptions panControlOptions = PanControlOptions.create();
		panControlOptions.setPosition(ControlPosition.RIGHT_TOP);
		ZoomControlOptions zoomControlOptions = ZoomControlOptions.create();
		zoomControlOptions.setPosition(ControlPosition.RIGHT_TOP);
		opts.setPanControlOptions(panControlOptions);
		opts.setZoomControlOptions(zoomControlOptions);
		
		// logger.info("Criando mapa");
		map = GoogleMap.create(Document.get().getElementById(MAP_CONTAINER), opts);
		Element clusterGroup = DOM.createDiv();
		clusterGroup.setId(CLUSTER_GROUP);
		DOM.getElementById(MAP_CONTAINER).insertFirst(clusterGroup);
		
		map.addBoundsChangedListener(new BoundsChangedHandler() {
			public void handle() {
				boundsChangeAction();
			}
		});
		
		map.addZoomChangedListener(new ZoomChangedHandler() {
			public void handle() {
				boundsChangeAction();
			}
		});
		
//		Element btnSalvar = DOM.getElementById("btnTelaCheia");
//		Event.sinkEvents(btnSalvar, Event.ONCLICK);
//		Event.setEventListener(btnSalvar, new EventListener() {
//			public void onBrowserEvent(Event event) {
//				if(Window.Location.getHref().contains("/Map.html")){
//					Window.Location.replace(Window.Location.getHref().replace("/Map.html", "/MapFull.html"));
//				}
//				else{
//					Window.Location.replace(GWT.getHostPageBaseURL() + "Map.html");
//				}
//			}
//		});
		
		addResizeHandler();
	}

	private void loadMarkers(final Date thisDate) {
		int zoomLevel = (int) map.getZoom();

		boolean all = UserController.isMasterUser();

		AsyncCallback<Coordinate[]> callback = new AsyncCallback<Coordinate[]>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Coordinate[] result) {
				if (result.length > 0) {
					setMarkers(result, thisDate);
				}
				setLoading(false);
			}
		};
		BoundingBox bbox = new BoundingBox();
		LatLngBounds bounds = map.getBounds();
		bbox.setBounds(bounds.getSouthWest().lng(),
				bounds.getSouthWest().lat(), bounds.getNorthEast().lng(),
				bounds.getNorthEast().lat());
		// logger.info(bbox.toString());
		setLoading(true);
		mapService.getOSCCoordinates(bbox, zoomLevel, all, callback);
	}

	private void setMarkers(Coordinate[] locations, final Date thisDate) {
		for (final Coordinate location : locations) {
			if (!changeDate.equals(thisDate)) {
				break;
			}
			if (location instanceof OscCoordinate) {
				OscCoordinate osc = (OscCoordinate) location;
				boolean sel = isSelected && osc.getId() == selectedId;
				Marker oscMarker = OscMarker.create(map, osc, sel);
				markers.add(oscMarker);
			}
			if (location instanceof Cluster) {
				Cluster cluster = (Cluster) location;
				ClusterMarker.create(map, cluster);
			}
		}
	}

	private void boundsChangeAction() {
		cleanMap();
		changeDate = new Date();
		final Date thisDate = changeDate;
		Timer t = new Timer() {
			@Override
			public void run() {
				if (changeDate.equals(thisDate)) {
					if (map.getZoom() < 4) {
						map.setCenter(DEFAULT_CENTER);
					}
					loadMarkers(thisDate);
				}
			}
		};
		t.schedule(DELAY);
	}

	private void cleanMap() {
		if (!markers.isEmpty()) {
			for (Marker marker : markers) {
				marker.setMap((GoogleMap) null);
			}
			markers.clear();
		}
		Element elem = DOM.getElementById(CLUSTER_GROUP);
		if (elem != null) {
			elem.removeFromParent();
			Element newGroup = DOM.createDiv();
			newGroup.setId(CLUSTER_GROUP);
			DOM.getElementById(MAP_CONTAINER).insertFirst(newGroup);
		}
	}

	public void fitBoundsToView(BoundingBox bounds) {
		LatLng sw = LatLng.create(bounds.getMinY(), bounds.getMinX());
		LatLng ne = LatLng.create(bounds.getMaxY(), bounds.getMaxX());
		LatLngBounds viewPort = LatLngBounds.create(sw, ne);
		this.map.fitBounds(viewPort);
	}

	private void addLoadingBar() {
		loading = DOM.createDiv();
		String url = GWT.getHostPageBaseURL() + "imagens/loading.gif";
		// logger.info(url);
		String html = "<img src='" + url + "'>";
		loading.setInnerHTML(html);
		int left = (int) map.getDiv().getParentElement().getClientWidth() - 120;
		int top = (int) map.getDiv().getParentElement().getClientHeight() - 80;
		loading.getStyle().setPropertyPx("left", left);
		loading.getStyle().setPropertyPx("top", top);
		loading.getStyle().setPosition(Position.ABSOLUTE);
		loading.getStyle().setZIndex(5);
		
		DOM.getElementById(MAP_CONTAINER).insertFirst(loading);
	}
	
	private void setLoading(boolean isLoading) {
		if (loading == null) {
			addLoadingBar();
		}
		Visibility vis = isLoading ? Visibility.VISIBLE : Visibility.HIDDEN;
		loading.getStyle().setVisibility(vis);
	}

	/**
	 * @param isVisible
	 *            indica se o mapa deve estar visível ou não.
	 */
	protected void setVisible(boolean isVisible) {
		final Element mapDiv = DOM.getElementById(MAP_CONTAINER);
		Display display = isVisible ? Display.BLOCK : Display.NONE;
		mapDiv.getStyle().setDisplay(display);
	}

	protected void clear() {
		RootPanel mapDiv = RootPanel.get(MAP_CONTAINER);
		mapDiv.clear();
	}

	public void removeResizeHandler() {
		if (handleControl != null)
			handleControl.removeHandler();
	}

	public void addResizeHandler() {
		handleControl = Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				initFunction();
			}

		});
	}

	public static native void initFunction() /*-{
		$wnd.jQuery($doc).ready(
				function() {
					if ($wnd.jQuery('.organizacao span').text().length > 90) {
						$wnd.jQuery('.organizacao span').text(
								$wnd.jQuery('.organizacao span').text()
										.substring(0, 90)
										+ '...');
					}
					$wnd.jQuery('.contraste').hide();
					$wnd.redimensionarMapa();

				});

	}-*/;
}
