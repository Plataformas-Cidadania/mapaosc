package gov.sgpr.fgv.osc.portalosc.configuration.client.components;

import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

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
	public PopupPanel searchResultsPanel = new PopupPanel();
	private List<SearchResult> items = new ArrayList<SearchResult>();

	public void setItems(List<SearchResult> items) {
		this.items = items;
		final Element searchBox = DOM.getElementById("enome");
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
	}

	public HTML getSearchHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div id='popUp' name='popUp'>");
		if (!this.items.isEmpty()) {
			htmlBuilder.append("<ul id='resultList' class='resultado'>");

			for (int index = 0; index < items.size(); index++) {
				htmlBuilder.append("<li value='"
						+ this.items.get(index).getId() + "'><strong>"
						+ this.items.get(index).getValue() + "</strong></li>");
			}
			htmlBuilder.append("</ul>");
		} else {
			htmlBuilder.append("<div>" + "<ul class='total'>"
					+ "<li><strong></strong></li>" + "<li><em></em></li>"
					+ "</ul>");
			htmlBuilder.append("<ul class=\"resultado\">");
			htmlBuilder
					.append("<li><a href=''> Nenhum resultado encontrado </a></li>");
			htmlBuilder.append("</ul>" + "</div>");

		}
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

	public void addSearchListener(EventListener listener) {
		Element elem = DOM.getElementById("resultList");
		for (int i = 0; i < elem.getChildCount(); i++) {
			final Node child = elem.getChild(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				final Element childElement = (Element) child;
				Event.sinkEvents(childElement, Event.ONCLICK);
				Event.setEventListener(childElement, listener);
			}
		}
	}
}
