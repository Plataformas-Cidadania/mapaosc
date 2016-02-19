package gov.sgpr.fgv.osc.portalosc.map.client.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import gov.sgpr.fgv.osc.portalosc.map.client.components.MatrixWidget;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Place;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.PlaceType;

/**
 * @author victor
 * 
 *         Controlador da Matrix de Indicadores
 */
public class MatrixController {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private final RootPanel matrixDiv = RootPanel.get("infograficos");
	private PlaceServiceAsync placeService = GWT.create(PlaceService.class);

	public void init() {
		logger.info("iniciando Matriz de Indicadores");
		// infographicsDiv.getElement().getStyle().setZIndex(101);
	}

	public void loadMatrix(String placeCode) {
		logger.info("Carregando Matriz de Indicadores");
		AsyncCallback<Place[]> callback = new AsyncCallback<Place[]>() {

			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Place[] result) {
				loadPlaces(result);
			}
		};
		if (placeCode.equals("0")) {
			placeService.getPlaces(PlaceType.REGION, callback);

		} else {
			placeService.getPlaces(Integer.valueOf(placeCode), callback);
		}

	}

	private void loadPlaces(Place[] places) {
		final MatrixWidget matrixWidget = new MatrixWidget(places);
		EventListener indicatorListener = new EventListener() {

			
			public void onBrowserEvent(Event event) {
				String indicator = matrixWidget.getSelectedIndicator();
				matrixWidget.updateTable(indicator);
			}
		};
		matrixWidget.addIndicatorChangeListener(indicatorListener);
		matrixDiv.clear();
		matrixDiv.add(matrixWidget);
	}

	/**
	 * @param isVisible
	 *            indica se os infográficos devem estar visíveis ou não.
	 */
	protected void setVisible(boolean isVisible) {
		Display display = isVisible ? Display.BLOCK : Display.NONE;
		matrixDiv.getElement().getStyle().setDisplay(display);
	}

}
