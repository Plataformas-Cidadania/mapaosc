package gov.sgpr.fgv.osc.portalosc.user.client.components;

import gov.sgpr.fgv.osc.portalosc.user.shared.model.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class SearchWidget extends Composite {
	private List<SearchResult> items = new ArrayList<SearchResult>();
	private List<List<String>> oscItems = new ArrayList<List<String>>();
	private Map<String, String> regionItems = new LinkedHashMap<String, String>();
	private Map<String, String> stateItems = new LinkedHashMap<String, String>();
	private List<List<String>> countyItems = new ArrayList<List<String>>();
	private List<SearchResult> addressItems = new ArrayList<SearchResult>();
	private PopupPanel searchResultsPanel = new PopupPanel();
	public Integer result = 0;
	public Integer count = 1;

	public void setItems(List<SearchResult> items, Boolean osc, String idElement) {
		this.items = items;
		Element searchBox = null;
		if(osc)
			searchBox = DOM.getElementById(idElement);
		else
			searchBox = DOM.getElementById("enome");
		searchResultsPanel.clear();
		searchResultsPanel.add(getSearchHtml());
		searchResultsPanel.getElement().setId("resultado_busca");
		searchResultsPanel.setAutoHideEnabled(true);
		searchResultsPanel.setWidth(searchBox.getOffsetWidth() + "px");
		int left = searchBox.getAbsoluteLeft();
		int top = searchBox.getAbsoluteTop() + searchBox.getOffsetHeight();
		searchResultsPanel.setPopupPosition(left, top);
		searchResultsPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		searchResultsPanel.show();
		
		result = oscItems.size() + stateItems.size() + countyItems.size();
	}

	public HTML getSearchHtml() {
		
		clearResults();
		
		StringBuilder htmlBuilder = new StringBuilder();
		
		if (!this.items.isEmpty()) {
			
			for (SearchResult item : this.items) {
				if (item.getType().toString() == "REGION")
					this.regionItems.put(item.getValue(), "P" + item.getId());
				if (item.getType().toString() == "STATE")
					this.stateItems.put(item.getValue(), "P" + item.getId());
				if (item.getType().toString() == "COUNTY")
					this.countyItems.add(Arrays.asList("P" + item.getId(), item.getValue()));
				if (item.getType().toString() == "OSC")
					this.oscItems.add(Arrays.asList("O" + item.getId(), item.getValue()));
				if (item.getType().toString() == "ADDRESS")
					this.addressItems.add(item);
			}
			htmlBuilder.append("<div>");
			
			if (!this.oscItems.isEmpty()) {
				htmlBuilder.append("<div>"); 
				htmlBuilder.append("<ul class=\"resultado\">");
				
				for (List<String> entry : this.oscItems){
					htmlBuilder.append("<div id='resultList' ><li id='resultList"+ count +"' value = '"
							+ entry.get(0) + "' "
							+ "name='"+ entry.get(0) +"' ><strong>"
							+ entry.get(1) + "</strong></li></div>");
					count++;
				}
				htmlBuilder.append("</ul>" + "</div>");
			}
			
			if (!regionItems.isEmpty()) {
				if(regionItems.size() == 1)
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>"+ this.regionItems.size() 
							+" Região </strong></li></ul>");
				else
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>"+ this.regionItems.size() 
							+" Regiões </strong></li></ul>");
				
				htmlBuilder.append("<ul class=\"resultado\">");
				
				for (Map.Entry<String, String> entry : this.regionItems
						.entrySet()){
					htmlBuilder.append("<div id='resultList' ><li id='resultList"+ count +"' value = '"
							+ entry.getValue() + "' "
							+ "name='"+ entry.getValue() +"' ><strong>"
							+ entry.getKey() + "</strong></li></div>");
					count++;
				}
				htmlBuilder.append("</ul>" + "</div>");
			}
			
			if (!stateItems.isEmpty()) {
				if(stateItems.size() == 1)
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>" + this.stateItems.size()
							+ " Estado </strong></li></ul>");
				else
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>" + this.stateItems.size()
							+ " Estados </strong></li></ul>");
			
				htmlBuilder.append("<ul class=\"resultado\">");
				
				for (Map.Entry<String, String> entry : this.stateItems
						.entrySet()){
					htmlBuilder.append("<div id='resultList' ><li id='resultList"+ count +"' value = '"
							+ entry.getValue() + "' "
							+ "name='"+ entry.getValue() +"' ><strong>"
							+ entry.getKey() + "</strong></li></div>");
					count++;
				}
				htmlBuilder.append("</ul>" + "</div>");
			}
			
			if (!countyItems.isEmpty()) {
				if(countyItems.size() == 1)
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>" + this.countyItems.size()
							+ " Município </strong></li></ul>");
				else
					htmlBuilder.append("<div>" + "<ul class=\"total\">"
							+ "<li><strong>" + this.countyItems.size()
							+ " Municípios </strong></li></ul>");
				
				htmlBuilder.append("<ul class=\"resultado\">");
				
				for (List<String> entry : this.countyItems){
					htmlBuilder.append("<div id='resultList' ><li id='resultList"+ count +"' value = '"
							+ entry.get(0) + "' "
							+ "name='"+ entry.get(0) +"' ><strong>"
							+ entry.get(1) + "</strong></li></div>");
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

	public void addSearchListener(EventListener listener) {
		for (int i = 1; i<count; i++){
			if(DOM.getElementById("resultList"+i) != null){
				Element elem = DOM.getElementById("resultList"+i);
				for (int j = 0; j < elem.getChildCount(); j++) {
					final Node child = elem.getChild(j);
					if (child.getNodeType() == Node.ELEMENT_NODE) {
						final Element childElement = (Element) child;
						Event.sinkEvents(childElement, Event.ONCLICK);
						Event.setEventListener(childElement, listener);
					}
				}
			}
		}
	}
	
	public void clearResults(){
		oscItems.clear();
		regionItems.clear();
		stateItems.clear();
		countyItems.clear();
		addressItems.clear();
	}
	
	public void hideItens(){
		searchResultsPanel.hide();
	}
}