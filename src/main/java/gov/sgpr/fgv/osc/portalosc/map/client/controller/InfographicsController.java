package gov.sgpr.fgv.osc.portalosc.map.client.controller;

import gov.sgpr.fgv.osc.portalosc.map.client.components.InfographicsWidget;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.Graph;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.Infographic;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.Infographics;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author victor
 * 
 *         Controlador dos infográficos
 */
public class InfographicsController {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private final RootPanel infographicsDiv = RootPanel.get("infograficos");

	public void init() {
		logger.info("iniciando infograficos");
		// infographicsDiv.getElement().getStyle().setZIndex(101);
	}

	public void loadInfo(Infographic info) {
		switch (info) {
		case OSC_NUMEROS:
			loadInfo01();
			break;
		case OSC_RECURSOS:
			loadInfo02();
			break;
		case NATUREZA_JURIDICA:
			loadInfo03();
			break;
		default:
			break;
		}
	}

	private void loadInfo01() {
		Graph graph1 = new Graph();
		graph1.setTitle("<strong>303 mil</strong> OSCs");
		graph1.setSvgImage("infograficos/numeros/infografico_1.png");
		graph1.setSvgImageContrast("infograficos/numeros/contraste/infografico_1.png");
		graph1.setWidth(332);
		graph1.setHeight(292);

		Graph graph2 = new Graph();
		graph2.setTitle("<strong>2,1 milhões</strong> de vínculos formais");
		graph2.setSvgImage("infograficos/numeros/infografico_2.svg");
		graph2.setSvgImageContrast("infograficos/numeros/contraste/infografico_2.svg");
		graph2.setWidth(332);
		graph2.setHeight(292);

		Graph graph3 = new Graph();
		graph3.setTitle("Relação com o governo Federal <strong>15%</strong> das OSCs");
		graph3.setSvgImage("infograficos/numeros/infografico_3.svg");
		graph3.setSvgImageContrast("infograficos/numeros/contraste/infografico_3.svg");
		graph3.setWidth(332);
		graph3.setHeight(292);

		Graph graph4 = new Graph();
		graph4.setTitle("Instituições por <strong>Natureza Jurídica</strong> (%)");
		graph4.setSvgImage("infograficos/numeros/infografico_4.svg");
		graph4.setSvgImageContrast("infograficos/numeros/contraste/infografico_4.svg");
		graph4.setWidth(332);
		graph4.setHeight(292);

		Graph graph5 = new Graph();
		graph5.setTitle("Títulos e certificações");
		graph5.setSvgImage("infograficos/numeros/infografico_5.svg");
		graph5.setSvgImageContrast("infograficos/numeros/contraste/infografico_5.svg");
		graph5.setWidth(332);
		graph5.setHeight(292);

		Graph graph6 = new Graph();
		graph6.setTitle("Recursos públicos");
		graph6.setSvgImage("infograficos/numeros/infografico_7.svg");
		graph6.setSvgImageContrast("infograficos/numeros/contraste/infografico_7.svg");
		graph6.setWidth(332);
		graph6.setHeight(126);

		Graph graph7 = new Graph();
		graph7.setTitle("Participação social");
		graph7.setSvgImage("infograficos/numeros/infografico_6.svg");
		graph7.setSvgImageContrast("infograficos/numeros/contraste/infografico_6.svg");
		graph7.setWidth(332);
		graph7.setHeight(142);

		Infographics innerGraph = new Infographics(2, 1);
		innerGraph.addGraph(0, 0, graph6);
		innerGraph.addGraph(1, 0, graph7);

		Infographics info = new Infographics(3, 2);
		info.setTitle("OSCs em números");
		info.addGraph(0, 0, graph1);
		info.addGraph(0, 1, graph2);
		info.addGraph(1, 0, graph3);
		info.addGraph(1, 1, graph4);
		info.addGraph(2, 0, graph5);
		info.addGraph(2, 1, innerGraph);

		InfographicsWidget iw = new InfographicsWidget(info);
		infographicsDiv.clear();
		infographicsDiv.add(iw);
	}

	private void loadInfo02() {
		Graph graph1 = new Graph();
		graph1.setTitle("Cerca de <strong>11 mil</strong> OSCs");
		graph1.setSvgImage("infograficos/recursos/infografico_7.png");
		graph1.setSvgImageContrast("infograficos/recursos/contraste/infografico_7.png");
		graph1.setWidth(332);
		graph1.setHeight(292);

		Graph graph2 = new Graph();
		graph2.setTitle("OSCs por faixas de vínculos formais");
		graph2.setSvgImage("infograficos/recursos/infografico_8.svg");
		graph2.setSvgImageContrast("infograficos/recursos/contraste/infografico_8.svg");
		graph2.setWidth(332);
		graph2.setHeight(292);

		Graph graph3 = new Graph();
		graph3.setTitle("Títulos e certificações");
		graph3.setSvgImage("infograficos/recursos/infografico_5.svg");
		graph3.setSvgImageContrast("infograficos/recursos/contraste/infografico_5.svg");
		graph3.setWidth(332);
		graph3.setHeight(292);

		Graph graph4 = new Graph();
		graph4.setTitle("Instituições por <strong>Natureza Jurídica</strong> (%)");
		graph4.setSvgImage("infograficos/recursos/infografico_6.svg");
		graph4.setSvgImageContrast("infograficos/recursos/contraste/infografico_6.svg");
		graph4.setWidth(332);
		graph4.setHeight(292);

		Graph graph5 = new Graph();
		graph5.setTitle("SICONV");
		graph5.setSvgImage("infograficos/recursos/infografico_3.svg");
		graph5.setSvgImageContrast("infograficos/recursos/contraste/infografico_3.svg");
		graph5.setWidth(332);
		graph5.setHeight(292);

		Graph graph6 = new Graph();
		graph6.setTitle("Leis de incentivo e FINEP");
		graph6.setSvgImage("infograficos/recursos/infografico_4.svg");
		graph6.setSvgImageContrast("infograficos/recursos/contraste/infografico_4.svg");
		graph6.setWidth(332);
		graph6.setHeight(291);

		Infographics info = new Infographics(3, 2);
		info.setTitle("OSCs e os recursos");
		info.addGraph(0, 0, graph1);
		info.addGraph(0, 1, graph2);
		info.addGraph(1, 0, graph3);
		info.addGraph(1, 1, graph4);
		info.addGraph(2, 0, graph5);
		info.addGraph(2, 1, graph6);

		InfographicsWidget iw = new InfographicsWidget(info);
		infographicsDiv.clear();
		infographicsDiv.add(iw);

	}

	private void loadInfo03() {
		Graph graph1 = new Graph();
		graph1.setTitle("<ul id=\"info03_1\" class=\"clearfix\"><li>Natureza Jurídica</li><li>Distribuição de OSCs por região (%)</li></ul>");
		graph1.setSvgImage("infograficos/natureza_juridica/infografico_1.svg");
		graph1.setSvgImageContrast("infograficos/natureza_juridica/contraste/infografico_1.svg");
		graph1.setWidth(600);
		graph1.setHeight(410);

		Graph graph2 = new Graph();
		graph2.setTitle("<ul id=\"info03_2\" class=\"clearfix\"><li>Distribuição de Vínculos por Faixas de Vínculos (%)</li></ul>");
		graph2.setSvgImage("infograficos/natureza_juridica/infografico_2.svg");
		graph2.setSvgImageContrast("infograficos/natureza_juridica/contraste/infografico_2.svg");
		graph2.setWidth(600);
		graph2.setHeight(410);

		Infographics info = new Infographics(2, 1);
		info.setTitle("OSCs natureza jurídica / faixa de vínculos");
		info.addGraph(0, 0, graph1);
		info.addGraph(1, 0, graph2);

		InfographicsWidget iw = new InfographicsWidget(info);
		iw.setStyleName("info03");
		infographicsDiv.clear();
		infographicsDiv.add(iw);

	}

	/**
	 * @param isVisible
	 *            indica se os infográficos devem estar visíveis ou não.
	 */
	protected void setVisible(boolean isVisible) {
		Display display = isVisible ? Display.BLOCK : Display.NONE;
		infographicsDiv.getElement().getStyle().setDisplay(display);
	}

}
