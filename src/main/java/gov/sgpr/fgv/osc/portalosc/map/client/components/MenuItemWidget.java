package gov.sgpr.fgv.osc.portalosc.map.client.components;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.AbstractMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.AnchorListMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.KeyValueMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.ListMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.SimpleTextMenuItem;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.Element;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class MenuItemWidget extends Composite {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private AbstractMenuItem item;
	private boolean opened = false;

	public MenuItemWidget(AbstractMenuItem menuItem) {
		this.item = menuItem;
		initWidget(getHtml());
	}

	private HTML getHtml() {

		StringBuilder htmlBuilder = new StringBuilder();

		String resumedTitle;
		int limit = item.getItemTitle().toUpperCase()
				.equals(item.getItemTitle()) ? 25 : 35;
		if (item.getItemTitle().length() > limit) {
			resumedTitle = item.getItemTitle().subSequence(0, limit - 1)
					+ "...";
		} else {
			resumedTitle = item.getItemTitle();
		}
		String titleToolTip = item.getTitleToolTip() != null ? item
				.getTitleToolTip() : item.getItemTitle();

		htmlBuilder.append("<h4>");
		htmlBuilder.append("	<a href=\"#\" title=\"Detalhes\">");
		htmlBuilder.append("<em id=\"btnItem_" + item.getId()
				+ "\" class=\"dados\">");
		htmlBuilder.append(item.getItemTitle());
		htmlBuilder.append("</em>");
		htmlBuilder.append("</a>");

		htmlBuilder.append("<span id =\"popup"+item.getItemValue()+"\" title=\"");
		htmlBuilder.append("\" class=\"menuTooltip\"><span class=\"hidden\">"+titleToolTip+"</span>");
		if (item.getItemValue() != null && !item.getItemValue().isEmpty()) {
			htmlBuilder.append("<a id=\"nome_"+item.getItemValue() +"\" href=\"#");
			htmlBuilder.append(item.getItemValue());
			htmlBuilder.append("\"><strong>");
			htmlBuilder.append(resumedTitle);
			htmlBuilder.append("</strong></a>");
		} else {
		htmlBuilder.append(resumedTitle);
	}
		htmlBuilder.append("</span>");
		htmlBuilder.append("</h4>");

		if (item.getContent() != null) {
			htmlBuilder.append("<ul id=\"item_body_" + item.getId()
					+ "\" class=\"");
			htmlBuilder.append(item.getCssClass());
			htmlBuilder.append("\">");

			if (item instanceof SimpleTextMenuItem)
				htmlBuilder.append(getText((SimpleTextMenuItem) item));
			else if (item instanceof ListMenuItem)
				htmlBuilder.append(getList((ListMenuItem) item));
			else if (item instanceof KeyValueMenuItem)
				htmlBuilder.append(getKeyValue((KeyValueMenuItem) item));
			else if (item instanceof AnchorListMenuItem)
				htmlBuilder.append(getAnchorList((AnchorListMenuItem) item));

			if (item.getInfoSource() != null
					&& !item.getInfoSource().equals("")) {
				htmlBuilder.append("<li class=\"fonte\">");
				htmlBuilder.append("<em>Fonte: </em>");
				htmlBuilder.append(getHelpButton("ajuda_" + item.getId()));
				htmlBuilder.append("</li>");
			}
			htmlBuilder.append("</ul>");
		}
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

	private String getList(ListMenuItem item) {
		StringBuilder htmlBuilder = new StringBuilder();
		for (String value : item.getContent()) {
			htmlBuilder.append("<li>" + value + "</li>");
		}
		return htmlBuilder.toString();
	}

	private String getKeyValue(KeyValueMenuItem item) {
		StringBuilder htmlBuilder = new StringBuilder();
		for (String key : item.getContent().keySet()) {
			String value = item.getContent().get(key);
			htmlBuilder.append("<li><strong>" + key + "</strong>: " + value
					+ "</li>");
		}
		return htmlBuilder.toString();
	}

	private String getAnchorList(AnchorListMenuItem item) {
		StringBuilder htmlBuilder = new StringBuilder();
		for (String name : item.getContent().keySet()) {
			String anchor = item.getContent().get(name);
			if (anchor != null && !anchor.isEmpty()) {
				htmlBuilder.append("<li><a href=\"" + anchor + "\" '>" + name
						+ "</a></li>");
			} else {
				htmlBuilder.append("<li title = 'Não disponível'>" + name
						+ "</li>");
			}
		}
		return htmlBuilder.toString();
	}

	private String getText(SimpleTextMenuItem item) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append(item.getContent());
		return htmlBuilder.toString();
	}

	private String getHelpButton(String IdItem) {
		StringBuilder helpBuilder = new StringBuilder("<button id=\"" + IdItem
				+ "\" ");
		helpBuilder.append(">?</button>");
		return helpBuilder.toString();
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		final Element itemBody = DOM
				.getElementById("item_body_" + item.getId());
		if (itemBody != null) {
			itemBody.getStyle().setDisplay(Display.NONE);
		}

//		final Element anchor = DOM.getElementById("nome_" + item.getItemValue());
		final Element spanPopup = DOM.getElementById("popup"+item.getId());
		final Element span = (Element) spanPopup.getFirstChildElement();
		if (span != null) {
			Event.sinkEvents(spanPopup, Event.ONMOUSEOVER);
			Event.sinkEvents(spanPopup, Event.ONMOUSEMOVE);
			Event.setEventListener(spanPopup, new EventListener() {
				@Override
				public void onBrowserEvent(Event event) {
					final int left = event.getClientX() + 5;
					final int bottom = event.getClientY() - span.getClientHeight() - 5;
					span.getStyle().setLeft(left, Unit.PX);
					span.getStyle().setTop(bottom, Unit.PX);
				}
			});
		}

		final Element btnItem = DOM.getElementById("btnItem_" + item.getId());
		if (btnItem != null) {
			if (item.getContent() == null) {
				btnItem.getStyle().setDisplay(Display.NONE);
			}
			Event.sinkEvents(btnItem, Event.ONCLICK);
			Event.setEventListener(btnItem, new EventListener() {

				@Override
				public void onBrowserEvent(Event event) {
					if (opened) {
						btnItem.setClassName("dados");
						itemBody.getStyle().setDisplay(Display.NONE);
					} else {
						btnItem.setClassName("dados_abertos");
						itemBody.getStyle().setDisplay(Display.BLOCK);
					}
					opened = !opened;
				}
			});
		}
		final Element btnHelp = DOM.getElementById("ajuda_" + item.getId());
		if (btnHelp != null) {
			Event.sinkEvents(btnHelp, Event.ONCLICK);
			Event.setEventListener(btnHelp, new EventListener() {

				@Override
				public void onBrowserEvent(Event event) {
					PopupPanel helpPanel = new PopupPanel();
					HTML html = new HTML(item.getInfoSource());
					ScrollPanel scPanel = new ScrollPanel(html);
					helpPanel.add(scPanel);
					helpPanel.setWidth("275px");
					helpPanel.setHeight("200px");
					helpPanel.setAutoHideEnabled(true);
					helpPanel.setPopupPosition(btnHelp.getAbsoluteLeft()
							+ btnHelp.getOffsetWidth(),
							btnHelp.getAbsoluteTop() - 200);
					DOM.setIntStyleAttribute(helpPanel.getElement(), "zIndex",
							110);
					DOM.setStyleAttribute(helpPanel.getElement(), "background",
							"black");
					DOM.setStyleAttribute(helpPanel.getElement(), "color",
							"white");
					DOM.setStyleAttribute(helpPanel.getElement(), "padding",
							"5px");
					helpPanel.show();

				}

			});
		}
	}
}
