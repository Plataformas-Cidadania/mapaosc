package gov.sgpr.fgv.osc.portalosc.map.client.components;

import java.util.logging.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.Point;
import com.google.maps.gwt.client.Projection;
import gov.sgpr.fgv.osc.portalosc.map.client.controller.MapController;
import vhmeirelles.geocluster.BoundingBox;
import vhmeirelles.geocluster.Cluster;


/*
 * Eric Ferreira
 * Modified Date: 26/01/2016
 * Change cluster and boundingbox classes to library GeoCluster
 */

public class ClusterMarker {
	private static final LatLng DEFAULT_CENTER = LatLng.create(-8.581021,
			-52.785187);
	private static Logger logger = Logger.getLogger(ClusterMarker.class
			.getName());
	private static int[] levels = { 10, 100, 1000, 10000 };
	private static int[] imageSize = { 30, 39, 47, 57, 65 };
		
	public static void create(final GoogleMap map,  final Cluster cluster) {
		int level = getLevel(cluster.getQuantity());
		final LatLng latLng = LatLng.create(cluster.getY(), cluster.getX());
		Element div = DOM.createDiv();
		String url = GWT.getHostPageBaseURL() + "pins/pin-" + level + ".png";
		int imgSize = imageSize[level - 1];
		int labelTop = (imgSize - 15) / 2;
		String html = "<img src='"
				+ url
				+ "'><div align='center' style='position: absolute; top: "
				+ labelTop
				+ "px; left: 0px;width: 100%;height: 100%; -webkit-touch-callout: none; -webkit-user-select: none; "
				+ "-khtml-user-select: none; -moz-user-select: none; -ms-user-select: none; user-select: none' "
				+ "unselectable='on' onselectstart='return false;' onmousedown='return false;' >"
				+ String.valueOf(cluster.getQuantity()) + "</div>";
		div.setInnerHTML(html);
		int left = (int) map.getDiv().getParentElement().getClientWidth() / 2;
		int top = (int) map.getDiv().getParentElement().getClientHeight() / 2;
		if (map.getZoom() >= 4) {
			Point p = getMarkerPixelCoordinates(map, latLng);
			left = (int) (p.getX());
			top = (int) (p.getY());
			left -= imgSize / 2;
			top -= imgSize / 2;
		}
		div.getStyle().setPropertyPx("left", left);
		div.getStyle().setPropertyPx("top", top);
		div.getStyle().setPosition(Position.ABSOLUTE);
		div.getStyle().setFontWeight(FontWeight.BOLD);
		div.getStyle().setPropertyPx("fontSize", 18);
		div.getStyle().setZIndex(5);
		div.getStyle().setProperty("fontFamily", "Arial,sans-serif");
		div.getStyle().setCursor(Cursor.POINTER);

		DOM.getElementById(MapController.CLUSTER_GROUP).insertFirst(div);

		DOM.sinkEvents(div, Event.ONCLICK);
		DOM.setEventListener(div, new EventListener() {
			public void onBrowserEvent(Event event) {
				double zoomLevel = map.getZoom();
				map.setCenter(latLng);
				fitBounds(map, cluster.getBbox());
				if (map.getZoom() == zoomLevel){
					map.setZoom(zoomLevel + 1);
				}
			}
		});
	}

	private static void fitBounds(GoogleMap map, BoundingBox bounds) {
		LatLng sw = LatLng.create(bounds.getMinY(), bounds.getMinX());
		LatLng ne = LatLng.create(bounds.getMaxY(), bounds.getMaxX());
		LatLngBounds viewPort = LatLngBounds.create(sw, ne);
		map.fitBounds(viewPort);
	}

	private static int getLevel(int quantity) {
		if (quantity < levels[0]) {
			return 1;
		}
		if (quantity < levels[1]) {
			return 2;
		}
		if (quantity < levels[2]) {
			return 3;
		}
		if (quantity < levels[3]) {
			return 4;
		}
		return 5;
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
				(markerPoint.getX() - bottomLeft.getX()) * scale,
				((markerPoint.getY() - topRight.getY()) * scale));
		return newMarkerPoint;
	}

}
