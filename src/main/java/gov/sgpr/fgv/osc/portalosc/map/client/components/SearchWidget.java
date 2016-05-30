package gov.sgpr.fgv.osc.portalosc.map.client.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

import gov.sgpr.fgv.osc.portalosc.map.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.SearchResultType;

public class SearchWidget extends Composite {
	private List<SearchResult> items = new ArrayList<SearchResult>();
	private List<List<String>> oscItems = new ArrayList<List<String>>();
	private Map<String, String> regionItems = new LinkedHashMap<String, String>();
	private Map<String, String> stateItems = new LinkedHashMap<String, String>();
	private List<List<String>> countyItems = new ArrayList<List<String>>();
	private List<SearchResult> addressItems = new ArrayList<SearchResult>();
	private Element searchTextField;
	private PopupPanel searchResultsPanel = new PopupPanel();
	public Integer result = 0;
	public Integer count = 0;
	
	public SearchWidget() {
		initWidget(getHtml());
	}
	
	private HTML getSearchBoxHtml() {
		clearResults();
		
		int count = 1;
		StringBuilder htmlBuilder = new StringBuilder();
		if (!this.items.isEmpty()) {
			for (SearchResult item : this.items) {
				if (item.getType().equals(SearchResultType.REGION))
					this.regionItems.put(item.getValue(), "P" + item.getId());
				if (item.getType().equals(SearchResultType.STATE))
					this.stateItems.put(item.getValue(), "P" + item.getId());
				if (item.getType().equals(SearchResultType.COUNTY))
					this.countyItems.add(Arrays.asList("P" + item.getId(), item.getValue()));
				if (item.getType().equals(SearchResultType.OSC))
					this.oscItems.add(Arrays.asList("O" + item.getId(), item.getValue()));
				if (item.getType().equals(SearchResultType.ADDRESS))
					this.addressItems.add(item);
			}
			htmlBuilder.append("<div id=\"resultado\">");
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
				
				for (List<String> entry : this.oscItems){
					htmlBuilder.append("<li><a id=\"list"+ count + "\" href=\"#" + entry.get(0)
							+ "\">" + entry.get(1) + "</a></li>");
					count++;
				}
				
				htmlBuilder.append("</ul>" + "</div>");
			}
			
			if (!regionItems.isEmpty()) {
				if (this.regionItems.size() == 1)
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Regiões</strong></li>" + "<li><em>"
							+ this.regionItems.size() + " encontrado</em></li>"
							+ "</ul>");
				else
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Regiões</strong></li>" + "<li><em>"
							+ this.regionItems.size() + " encontrados</em></li>"
							+ "</ul>");
				htmlBuilder.append("<ul class=\"resultado\">");
				
				for (Map.Entry<String, String> entry : this.regionItems
						.entrySet()){
					htmlBuilder.append("<li><a id=\"list"+ count + "\" href=\"#" + entry.getValue()
							+ "\">" + entry.getKey() + "</a></li>");
					count++;
				}
				
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
						.entrySet()){
					htmlBuilder.append("<li><a id=\"list"+ count + "\" href=\"#" + entry.getValue()
							+ "\">" + entry.getKey() + "</a></li>");
					count++;
				}
				
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
				
				for (List<String> entry : this.countyItems){
					htmlBuilder.append("<li><a id=\"list"+ count + "\" href=\"#" + entry.get(0)
							+ "\">" + entry.get(1) + "</a></li>");
					count++;
				}
				
				htmlBuilder.append("</ul>" + "</div>");
			}
			
			if (!addressItems.isEmpty()) {
				if (this.addressItems.size() == 1)
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Endereços</strong></li>"
							+ "<li><em>" + this.addressItems.size()
							+ " encontrado</em></li>" + "</ul>");
				else
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>Endereços</strong></li>"
							+ "<li><em>" + this.addressItems.size()
							+ " encontrados</em></li>" + "</ul>");
				htmlBuilder.append("<ul class=\"resultado\">");
				
				for(SearchResult item : this.addressItems){
					htmlBuilder.append("<li><a id=\"list"+ count + "\" href=\"#A" + "?lat=" + item.getLatitude() + "&lon=" + item.getLongetude() + "\">" + item.getValue() + "</a></li>");
					count++;
				}
				
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
		htmlBuilder.append("<p>Para localizar uma organização da sociedade civil, digite o nome parcial ou completo da organização, ou o número parcial ou completo do CNPJ (com ou sem pontuação) da organização. Também é possível localizar organizações buscando por nome dos municípios ou dos estados.</p>");
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
		DOM.setStyleAttribute(searchResultsPanel.getElement(), "overflow", "auto");
		searchResultsPanel.show();
		
		result = oscItems.size() + stateItems.size() + countyItems.size();
	}
	
	public void setOscBox(String osc){
		InputElement busca = DOM.getElementById("campobusca").cast();
		busca.setValue(osc);
	}
	
	private HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<form id='formbusca' name=\"Busca\">");
		htmlBuilder.append("	<label for=\"campobusca\" class=\"esconder\">Buscar organização</label>");
		htmlBuilder.append("	<input type=\"text\" name=\"campobusca\" id=\"campobusca\" placeholder=\"Informe a localização ou a organização desejada...\"  />");
		htmlBuilder.append("	<button type=\"button\" name=\"buscar\" id=\"buscar\" class=\"buscar\">Buscar</button>");
		htmlBuilder.append("	<button type=\"button\" name=\"ajuda\" class=\"ajuda\" id=\"ajuda\">?</button>");
		htmlBuilder.append("</form>");
		htmlBuilder.append("<div class=\"filtros\">");
		htmlBuilder.append("	<a href=\"#busca_filtros\" class=\"filtrar box\">Filtrar</a>");
		htmlBuilder.append("	<div class=\"ajuda filtros\">");
		htmlBuilder.append("		<p>Ajuda filtro</p>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<button type=\"button\" name=\"ajuda\" class=\"ajuda\" id=\"ajuda_filtros\">?</button>");
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

			public void onBrowserEvent(Event event) {
				PopupPanel helpPanel = new PopupPanel();
				helpPanel.add(getHelpHtml());
				helpPanel.getElement().setId("ajuda");
				helpPanel.setWidth("210px");
				helpPanel.setAutoHideEnabled(true);
				helpPanel.setPopupPosition(btnHelp.getAbsoluteLeft() + btnHelp.getOffsetWidth(), btnHelp.getAbsoluteTop() + btnHelp.getOffsetHeight());
				DOM.setIntStyleAttribute(helpPanel.getElement(), "zIndex", 100);
				DOM.setStyleAttribute(helpPanel.getElement(), "background", "black");
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
	
	public void addFocus(EventListener listener) {
		if(result > 1 ){
			count = 1;
			final Element elem = DOM.getElementById("resultado");
			Event.sinkEvents(elem, Event.ONKEYDOWN);
			Event.setEventListener(elem, listener);
		}
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
	
	public void addresultBusca(EventListener listener) {
		final Element elem = DOM.getElementById("resultado_busca");
		Event.sinkEvents(elem, Event.ONCLICK);
		Event.setEventListener(elem, listener);
	}
	
	public void close(){
		searchResultsPanel.hide();
	}
	
	public void clearResults(){
		oscItems.clear();
		regionItems.clear();
		stateItems.clear();
		countyItems.clear();
		addressItems.clear();
	}
	
	public native void formPreventDefault() /*-{
		$wnd.jQuery('#formbusca').submit(function (event){
			event.preventDefault();
		});
	}-*/;
}
