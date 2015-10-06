package gov.sgpr.fgv.osc.portalosc.map.client.components;

import gov.sgpr.fgv.osc.portalosc.map.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.SearchResultType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class SearchWidget extends Composite {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private List<SearchResult> items = new ArrayList<SearchResult>();
	private Map<String, String> oscItems = new LinkedHashMap<String, String>();
	private Map<String, String> stateItems = new LinkedHashMap<String, String>();
	private Map<String, String> countyItems = new LinkedHashMap<String, String>();
	private Element searchTextField;
	private PopupPanel searchResultsPanel = new PopupPanel();

	public SearchWidget() {
		initWidget(getHtml());
	}

	private HTML getSearchBoxHtml() {
		oscItems.clear();
		stateItems.clear();
		countyItems.clear();
		StringBuilder htmlBuilder = new StringBuilder();
		if (!this.items.isEmpty()) {
			for (SearchResult item : this.items) {
				if (item.getType().equals(SearchResultType.STATE))
					this.stateItems.put(item.getValue(), "P" + item.getId());
				if (item.getType().equals(SearchResultType.COUNTY))
					this.countyItems.put(item.getValue(), "P" + item.getId());
				if (item.getType().equals(SearchResultType.OSC))
					this.oscItems.put(item.getValue(), "O" + item.getId());
			}
			htmlBuilder.append("<div>");
			if (!this.oscItems.isEmpty()) {
				if (this.oscItems.size() == 1)
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Organização</strong></li>"
							+ "<li><em>" + this.oscItems.size()
							+ " encontrado</em></li>" + "</ul>");
				else
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Organização</strong></li>"
							+ "<li><em>" + this.oscItems.size()
							+ " encontrados</em></li>" + "</ul>");
				htmlBuilder.append("<ul class=\"resultado\">");
				for (Map.Entry<String, String> entry : this.oscItems.entrySet())
					htmlBuilder.append("<li><a href=\"#" + entry.getValue()
							+ "\">" + entry.getKey() + "</a></li>");

				htmlBuilder.append("</ul>" + "</div>");
			}
			if (!stateItems.isEmpty()) {
				if (this.stateItems.size() == 1)
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Estados</strong></li>" + "<li><em>"
							+ this.stateItems.size() + " encontrado</em></li>"
							+ "</ul>");
				else
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Estados</strong></li>" + "<li><em>"
							+ this.stateItems.size() + " encontrados</em></li>"
							+ "</ul>");
				htmlBuilder.append("<ul class=\"resultado\">");
				for (Map.Entry<String, String> entry : this.stateItems
						.entrySet())
					htmlBuilder.append("<li><a href=\"#" + entry.getValue()
							+ "\">" + entry.getKey() + "</a></li>");

				htmlBuilder.append("</ul>" + "</div>");
			}

			if (!countyItems.isEmpty()) {
				if (this.countyItems.size() == 1)
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Municípios</strong></li>"
							+ "<li><em>" + this.countyItems.size()
							+ " encontrado</em></li>" + "</ul>");
				else
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Municípios</strong></li>"
							+ "<li><em>" + this.countyItems.size()
							+ " encontrados</em></li>" + "</ul>");
				htmlBuilder.append("<ul class=\"resultado\">");
				for (Map.Entry<String, String> entry : this.countyItems
						.entrySet())
					htmlBuilder.append("<li><a href=\"#" + entry.getValue()
							+ "\">" + entry.getKey() + "</a></li>");

				htmlBuilder.append("</ul>" + "</div>");
			}
			htmlBuilder.append("</div>");
		} else {
			htmlBuilder.append("<div>" + "<ul class=\"total\">"
					+ "<li><strong></strong></li>" + "<li><em></em></li>" + "</ul>");
			htmlBuilder.append("<ul class=\"resultado\">");
			htmlBuilder.append("<li><a href=\"\"> Nenhum resultado encontrado </a></li>");
			htmlBuilder.append("</ul>" + "</div>");

		}
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

	private HTML getHelpHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder
				.append("<p>"
						+ "<strong>Para localizar um endereço, digite:</strong> <br />"
						+ "O nome parcial ou completo do endereço para localizá-lo. Por exemplo: "
						+ "<br /> <em>Presidente Vargas</em> ou <em>Rio Grande do Norte.</em>"
						+ "</p>"

						+ "<p>"
						+ "<strong>Para localizar uma organização da sociedade civil, digite:</strong> <br />"
						+ "O nome parcial ou completo da organização da sociedade civil, ou seu CNPJ, para localizá-la. Por exemplo: <br />"
						+ "<em>Fundação</em> ou <em>12.345.678/0001-90.</em>"
						+ "</p>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

	public void setValue(String value) {
		searchTextField.setAttribute("value", value);
	}

	public String getValue() {
		return searchTextField.getPropertyString("value");
	}

	public void setItems(List<SearchResult> items) {
		this.items = items;
		final Element searchBox = DOM.getElementById("campobusca");
		searchResultsPanel.clear();
		searchResultsPanel.add(getSearchBoxHtml());

		searchResultsPanel.getElement().setId("resultado_busca");
		searchResultsPanel.setAutoHideEnabled(true);
		searchResultsPanel.setWidth(searchBox.getOffsetWidth() + "px");
		int left = searchBox.getAbsoluteLeft();
		int top = searchBox.getAbsoluteTop() + searchBox.getOffsetHeight();
		searchResultsPanel.setPopupPosition(left, top);
		DOM.setIntStyleAttribute(searchResultsPanel.getElement(), "zIndex", 200);
		DOM.setStyleAttribute(searchResultsPanel.getElement(), "overflow",
				"auto");
		searchResultsPanel.show();
	}

	private HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<form name=\"Busca\">");
		htmlBuilder
				.append("	<label for=\"campobusca\" class=\"esconder\">Buscar organização</label>");
		htmlBuilder
				.append("	<input type=\"text\" name=\"campobusca\" id=\"campobusca\" placeholder=\"Informe a localização ou a organização desejada...\"  />");
		htmlBuilder
				.append("	<button type=\"button\" name=\"buscar\" id=\"buscar\" class=\"buscar\">Buscar</button>");
		htmlBuilder
				.append("	<button type=\"button\" name=\"ajuda\" class=\"ajuda\" id=\"ajuda\">?</button>");
		htmlBuilder.append("</form>");
		htmlBuilder.append("<div class=\"filtros\">");
		htmlBuilder
				.append("	<a href=\"#busca_filtros\" class=\"filtrar box\">Filtrar</a>");
		htmlBuilder.append("	<div class=\"ajuda filtros\">");
		htmlBuilder.append("		<p>Ajuda filtro</p>");
		htmlBuilder.append("	</div>");
		htmlBuilder
				.append("	<button type=\"button\" name=\"ajuda\" class=\"ajuda\" id=\"ajuda_filtros\">?</button>");
		htmlBuilder.append("</div>");

		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		final Element btnHelp = DOM.getElementById("ajuda");
		Event.sinkEvents(btnHelp, Event.ONCLICK);
		Event.setEventListener(btnHelp, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				PopupPanel helpPanel = new PopupPanel();
				helpPanel.add(getHelpHtml());
				helpPanel.getElement().setId("ajuda");
				helpPanel.setWidth("190px");
				helpPanel.setAutoHideEnabled(true);
				helpPanel.setPopupPosition(
						btnHelp.getAbsoluteLeft() + btnHelp.getOffsetWidth(),
						btnHelp.getAbsoluteTop() + btnHelp.getOffsetHeight());
				DOM.setIntStyleAttribute(helpPanel.getElement(), "zIndex", 100);
				DOM.setStyleAttribute(helpPanel.getElement(), "background",
						"black");
				DOM.setStyleAttribute(helpPanel.getElement(), "color", "white");
				DOM.setStyleAttribute(helpPanel.getElement(), "padding", "5px");
				helpPanel.show();
			}
		});
		searchTextField = DOM.getElementById("campobusca");

	}

	public void addFocusListener(EventListener listener) {
		final Element elem = DOM.getElementById("campobusca");
		Event.sinkEvents(elem, Event.ONFOCUS);
		Event.setEventListener(elem, listener);
	}

	public void addChangeListener(EventListener listener) {
		final Element elem = DOM.getElementById("campobusca");
		Event.sinkEvents(elem, Event.ONKEYDOWN);
		Event.setEventListener(elem, listener);
	}

	public void addSearchClickListener(EventListener listener) {
		final Element elem = DOM.getElementById("buscar");
		Event.sinkEvents(elem, Event.ONCLICK);
		Event.setEventListener(elem, listener);
	}

	public void addFilterClickListener(EventListener listener) {
		final Element elem = DOM.getElementById("ajuda_filtros");
		Event.sinkEvents(elem, Event.ONCLICK);
		Event.setEventListener(elem, listener);
	}

}
