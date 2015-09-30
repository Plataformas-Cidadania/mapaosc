package gov.sgpr.fgv.osc.portalosc.map.client.components;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.Graph;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.Infographics;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

/**
 * @author victor Componente gráfico que apresenta os Infográficos na tela.
 */
public class GraphWidget extends Composite {
	/**
	 * Construtor
	 */
	public GraphWidget(Graph graph) {
		initWidget(getHtml(graph));
	}

	private HTML getHtml(Graph graph) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div>");
		htmlBuilder.append("<h3>");
		htmlBuilder.append(graph.getTitle());
		htmlBuilder.append("</h3>");
		htmlBuilder.append("<img class=\"normal\" src=\"");
		htmlBuilder.append(graph.getSvgImage());
		htmlBuilder.append("\" width=\"");
		htmlBuilder.append(graph.getWidth());
		htmlBuilder.append("\" height=\"");
		htmlBuilder.append(graph.getHeight());
		htmlBuilder.append("\" />");
		htmlBuilder.append("<img class=\"contraste\" style='display:none;' src=\"");
		htmlBuilder.append(graph.getSvgImageContrast());
		htmlBuilder.append("\" width=\"");
		htmlBuilder.append(graph.getWidth());
		htmlBuilder.append("\" height=\"");
		htmlBuilder.append(graph.getHeight());
		htmlBuilder.append("\" />");
		htmlBuilder.append("</div>");

		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

}
