package gov.sgpr.fgv.osc.portalosc.map.client.components.model;

import gov.sgpr.fgv.osc.portalosc.map.client.components.GraphWidget;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author victor
 * 
 *         Classe que define a visão dos infográficos
 */
public class Graph implements GraphRenderer {
	private String title;
	private String svgImage;
	private String svgImageContrast;
	private int width;
	private int height;
	private String legend;
	
	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSvgImage() {
		return svgImage;
	}

	public void setSvgImage(String svgImage) {
		this.svgImage = svgImage;
	}

	public String getSvgImageContrast() {
		return svgImageContrast;
	}

	public void setSvgImageContrast(String svgImageContrast) {
		this.svgImageContrast = svgImageContrast;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public Widget getWidget() {
		GraphWidget w = new GraphWidget(this);
		w.setStyleName("graph");
		return w;
	}

}
