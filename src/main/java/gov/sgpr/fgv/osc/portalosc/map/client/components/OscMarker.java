package gov.sgpr.fgv.osc.portalosc.map.client.components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.GoogleMap.ZoomChangedHandler;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.ClickHandler;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.Projection;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.PaperClipAbstractSection;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.PaperClipCheckListSection;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.PaperClipKeyValueSection;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.PaperClipWindowInfo;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Certifications;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.DataSource;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscCoordinate;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscSummary;

public class OscMarker {
	private static Logger logger = Logger.getLogger(OscMarker.class.getName());
	public static Boolean embed = false;

	public static Marker create(final GoogleMap map,
			final OscCoordinate coordinate, boolean isSelected) {
		MarkerOptions markerOptions = MarkerOptions.create();
		markerOptions.setPosition(LatLng.create(coordinate.getY(),
				coordinate.getX()));
		String url = GWT.getHostPageBaseURL() + "pins/p-blue.png";
		if (isSelected)
			url = GWT.getHostPageBaseURL() + "pins/g-yellow.png";
		final MarkerImage img = MarkerImage.create(url);
		markerOptions.setIcon(img);
		markerOptions.setMap(map);
		final Marker marker = Marker.create(markerOptions);
		marker.addClickListener(new ClickHandler() {
			public void handle(MouseEvent event) {
				getData(map, marker, coordinate);
			}
		});
		return marker;
	}

	private static void getData(final GoogleMap map, final Marker marker,
			final OscCoordinate coordinate) {
		logger.info("Buscando dados para o popup");
		OscServiceAsync oscService = GWT.create(OscService.class);
		AsyncCallback<OscSummary> callbackSummary = new AsyncCallback<OscSummary>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}

			public void onSuccess(OscSummary result) {
				show(map, marker, result);
			}
		};
		oscService.getSummary(coordinate.getId(), callbackSummary);
	}

	private static void show(GoogleMap map, Marker marker, final OscSummary summary) {
		map.setCenter(marker.getPosition());
		final PopupPanel popup = new PopupPanel();
		popup.setStyleName(null);
		VerticalPanel panel = new VerticalPanel();

		PaperClipWindowInfo windowInfo = new PaperClipWindowInfo();
		if( embed == true )
			windowInfo.setCssClass("mapa_organizacao org_embed");
		else{
			windowInfo.setCssClass("mapa_organizacao clearfix");
		}
		windowInfo.setTitle(summary.getName());
		windowInfo.setFooter("Fonte");
		windowInfo.setFooterToolTip(getToolTip(summary.getDataSources()));
		windowInfo.setLikeCounter(summary.getRecomendations());

		PaperClipKeyValueSection oscInfo = new PaperClipKeyValueSection();
		oscInfo.setSectionTitle("");
		
		if (summary.getAddress() != null) {
			oscInfo.addKeyValue("Endereço",
					summary.getAddress());
		}
		
		if (summary.getCnaeDescription() != null) {
			oscInfo.addKeyValue("Área(s) de Atuação",
					summary.getCnaeDescription());
		}
		
		if (summary.getLegalTypeDescription() != null) {
			oscInfo.addKeyValue("Natureza Jurídica",
					summary.getLegalTypeDescription());
		}
		
		if (summary.getSite() != null) {
			oscInfo.addKeyValue("Site",
					summary.getSite());
		}
		
		if (summary.getFoundationYear() != null
				&& summary.getFoundationYear() != 0) {
			oscInfo.addKeyValue("Ano de Fundação",
					String.valueOf(summary.getFoundationYear()));
		}

//		PaperClipCheckListSection oscCertifications = new PaperClipCheckListSection();
//		oscCertifications.setSectionTitle("");

//		if (summary.getPartnerships() != null) {
//			oscCertifications.addKeyValue("Projetos em curso",String.valueOf(summary.getPartnerships()));
//	        }
//		 
//		if (summary.getGlobalValue() != null) {
//			NumberFormat fmtCurrency = NumberFormat.getCurrencyFormat();
//			oscCertifications.addKeyValue("Valor de Parcerias",fmtCurrency.format(summary.getGlobalValue()));
//		}
//		 
//		if (summary.getLength() != null) {
//			oscCertifications.addKeyValue("Vínculos de Trabalho", summary.getLength());
//		}
//		
//		if (summary.getGlobalValue() != null) {
//			oscCertifications.addKeyValue("Correção de Inflação", "Os valores exibidos estão corrigidos pela inflação do mês e ano correntes");
//		}
		
/*		oscCertifications.setSectionTitle("Dados");//Certificações");
		Certifications cert = summary.getCertifications();
		boolean oscip = cert.getOscipPublication() != null;
		oscCertifications.addElementToList("OSCIP", oscip);
		boolean upf = cert.getUpfDeclaration() != null;
		oscCertifications.addElementToList("UPF", upf);
		boolean cebasSUS = cert.getCebasSusBeginning() != null
				|| cert.getCebasSusEnd() != null;
		oscCertifications.addElementToList("Cebas Saúde", cebasSUS);
		boolean cebasMEC = cert.getCebasMecBeginning() != null
				|| cert.getCebasMecEnd() != null;
		oscCertifications.addElementToList("Cebas Educação", cebasMEC);
		boolean cebasMDS = cert.getCebasMdsBeginning() != null
				|| cert.getCebasMdsEnd() != null;
		oscCertifications
				.addElementToList("Cebas Assistência Social", cebasMDS);
		boolean cnea = cert.getCneaPublication() != null;
		oscCertifications.addElementToList("CNEA", cnea);*/

		List<PaperClipAbstractSection> sections = new ArrayList<PaperClipAbstractSection>();
		sections.add(oscInfo);
//		sections.add(oscCertifications);

		MagnetPaperClipWidget paperclip = new MagnetPaperClipWidget(windowInfo,
				sections);

		panel.add(paperclip);
		DOM.setIntStyleAttribute(popup.getElement(), "zIndex", 100);
		marker.setZIndex(101);
		Point markerPoint = getMarkerPixelCoordinates(map, marker.getPosition());
		int coordinateX = (int) (markerPoint.getX());
		int coordinateY = (int) (markerPoint.getY());
		popup.setPopupPosition(coordinateX, coordinateY);
		popup.add(panel);
		map.addZoomChangedListener(new ZoomChangedHandler() {
			public void handle() {
				popup.hide();
			}
		});
		popup.setAutoHideEnabled(true);
		popup.show();
		
		paperclip.addTitleClickListener(new EventListener(){

			
			public void onBrowserEvent(Event event) {
				if(embed == true)
					Window.open("https://mapaosc.ipea.gov.br/Map.html#O"+ summary.getId(), "_blank", "");
				else
					History.newItem("O" + summary.getId());
			}
			
		});
	}

	private static Point getMarkerPixelCoordinates(GoogleMap map, LatLng latLng) {
		Projection proj = map.getProjection();
		Point markerPoint = proj.fromLatLngToPoint(latLng);
		Point topRight = map.getProjection().fromLatLngToPoint(
				map.getBounds().getNorthEast());
		Point bottomLeft = map.getProjection().fromLatLngToPoint(
				map.getBounds().getSouthWest());
		double scale = Math.pow(2, map.getZoom());
		Point newMarkerPoint = Point.create(
				((markerPoint.getX() - bottomLeft.getX()) * scale) - 55,
				((markerPoint.getY() - topRight.getY()) * scale) + 125);
		return newMarkerPoint;
	}

	private static String getDataSourceAcronyms_(DataSource[] dataSources) {
		StringBuilder dsBuilder = new StringBuilder("Fonte: ");
		for (DataSource ds : dataSources) {
			dsBuilder.append(ds.getAcronym());
			dsBuilder.append(", ");
		}
		dsBuilder.deleteCharAt(dsBuilder.length() - 2);
		return dsBuilder.toString();
	}

	private static String getToolTip(DataSource[] dataSources) {
		DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy");
		StringBuilder dsBuilder = new StringBuilder();
		for (DataSource ds : dataSources) {
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
			if (ds.getAcquisitionDate() != null){
				dsBuilder.append("<br> Data de Aquisição:");
				dsBuilder.append(fmt.format(ds.getAcquisitionDate()));
			}
			dsBuilder.append("</p>");
			dsBuilder.append("<br>");
		}
		return dsBuilder.toString();
	}
}
