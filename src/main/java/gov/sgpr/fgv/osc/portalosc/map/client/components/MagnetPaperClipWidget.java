package gov.sgpr.fgv.osc.portalosc.map.client.components;

import java.util.List;
import java.util.logging.Logger;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.PaperClipAbstractSection;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.PaperClipCheckListSection;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.PaperClipKeyValueSection;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.PaperClipListSection;
import gov.sgpr.fgv.osc.portalosc.map.client.components.model.PaperClipWindowInfo;

/**
 * Renderiza visualmente um PaperClip a partir de um objeto
 * "PaperClipWindowInfo" e de uma lista de objetos "PaperClipSections"
 * 
 * @author Thassae Santos
 * 
 */
public class MagnetPaperClipWidget extends Composite {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private PaperClipWindowInfo windowInfo;

	/**
	 * @param windowInfo
	 *            O objeto PaperClipWindowInfo que fornecerá as informações para
	 *            a janela principal do PaperClip
	 * @param sections
	 *            coleção de sessões do PaperClip
	 */
	public MagnetPaperClipWidget(PaperClipWindowInfo windowInfo,
			List<PaperClipAbstractSection> sections) {
		super();
		this.windowInfo = windowInfo;
		initWidget(getPaperClipHtml(windowInfo, sections));
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		final Element btnHelp = DOM.getElementById("ppajuda");
		Event.sinkEvents(btnHelp, Event.ONCLICK);
		Event.setEventListener(btnHelp, new EventListener() {

			
			public void onBrowserEvent(Event event) {
				PopupPanel helpPanel = new PopupPanel();
				HTML html = new HTML(windowInfo.getFooterToolTip());
				ScrollPanel scPanel = new ScrollPanel(html);
				helpPanel.add(scPanel);
				helpPanel.getElement().setId("ajuda");
				helpPanel.setWidth("275px");
				helpPanel.setHeight("300px");
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
		final Element spanPopup = DOM.getElementById("spantitle");
		final Element span = (Element) spanPopup.getFirstChildElement();
		if (span != null) {
			Event.sinkEvents(spanPopup, Event.ONMOUSEOVER);
			Event.sinkEvents(spanPopup, Event.ONMOUSEMOVE);
			Event.setEventListener(spanPopup, new EventListener() {
				
				public void onBrowserEvent(Event event) {
					final int left = event.getClientX() + 5;
					final int bottom = event.getClientY() - 5;
					span.getStyle().setLeft(left, Unit.PX);
					span.getStyle().setTop(bottom, Unit.PX);
				}
			});
		}
	}


	/**
	 * Adiciona um listener para o evento de click no título
	 * @param listener
	 */
	public void addTitleClickListener(EventListener listener) {
		final Element title = DOM.getElementById("title");
		final Element btnDetails = DOM.getElementById("ppdetails");
		Event.sinkEvents(title, Event.ONCLICK);
		Event.sinkEvents(btnDetails, Event.ONCLICK);
		Event.setEventListener(title, listener);
		Event.setEventListener(btnDetails, listener);
	}

	/**
	 * Gera o objeto GWT HTML do PaperClip
	 * 
	 * @return HTML do PaperClip
	 */
	private HTML getPaperClipHtml(PaperClipWindowInfo windowInfo,
			List<PaperClipAbstractSection> sections) {
		String resumedTitle;
		if (windowInfo.getTitle().length() > 39) {
			resumedTitle = windowInfo.getTitle().subSequence(0, 38) + "...";
		} else {
			resumedTitle = windowInfo.getTitle();
		}
		String subTitle = " ";
		if (windowInfo.getSubTitle() != null
				&& !windowInfo.getSubTitle().isEmpty()) {
			subTitle = windowInfo.getSubTitle();
		}
		StringBuilder htmlBuilder = new StringBuilder(
				"<div class=\""
						+ windowInfo.getCssClass()
						+ "\">"
						+ "<span id =\"spantitle\" class=\"magneticTooltip\" >"
						+"<span id =\"\" >"
						+windowInfo.getTitle() 
						+"</span>"
						+"<a id='title' title=\""
						+ "\">"
						+ "<h2>"
						+ resumedTitle
						+ "</h2>"
						+ "</a>"
						+ "<h3>"
						+ subTitle
						+ "</h3></span>"
						+ "<h4><em title=\""
						+ windowInfo.getLikeCounter()
						+ " recomendaram esta organização!\" class=\"tip_recomendacao\">"
						+ windowInfo.getLikeCounter() + "</em></h4>");

		for (PaperClipAbstractSection section : sections) {
			if (sections.indexOf(section) == 0) {
				htmlBuilder.append("<div class=\"coluna1\">");
			} 
//			else {
//				htmlBuilder.append("<div class=\"coluna2\">");
//			}
			
			htmlBuilder.append("<strong>" + section.getSectionTitle()
					+ "</strong>");

			if (section instanceof PaperClipKeyValueSection) {
				String sectionContent = getKeyValueSection((PaperClipKeyValueSection) section);
				htmlBuilder.append(sectionContent);
			} else if (section instanceof PaperClipListSection) {
				String sectionContent = getListSection((PaperClipListSection) section);
				htmlBuilder.append(sectionContent);
			} else if (section instanceof PaperClipCheckListSection) {
				String sectionContent = getCheckListSection((PaperClipCheckListSection) section);
				htmlBuilder.append(sectionContent);
			}
			
			htmlBuilder.append("<div  align = \"right\">"+windowInfo.getFooter());
			htmlBuilder.append(getHelpButton()+"</div>");
			htmlBuilder.append("</div>");
			htmlBuilder.append("</div>");
		}
		
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
	
	private String getDetailsButton() {
		StringBuilder helpBuilder = new StringBuilder("<button id=\"ppdetails\" class=\"details\"");
		helpBuilder.append(">Detalhes</button>");
		return helpBuilder.toString();
	}

	private String getHelpButton() {
		StringBuilder helpBuilder = new StringBuilder("<button id=\"ppajuda\" class=\"ajuda\"");
		helpBuilder.append(">?</button>");
		return helpBuilder.toString();
	}

	private String getKeyValueSection(PaperClipKeyValueSection section) {
		StringBuilder htmlBuilder = new StringBuilder();
		for (String key : section.getSectionContent().keySet()) {
			htmlBuilder.append("<strong>");
			htmlBuilder.append(key + ":");
			htmlBuilder.append("</strong>");
			htmlBuilder.append(section.getSectionContent().get(key));
			htmlBuilder.append("<br>");
		}
		htmlBuilder.append("<div  align = \"left\">");
		htmlBuilder.append(getDetailsButton()+"</div>");
		return htmlBuilder.toString();
	}

	private String getListSection(PaperClipListSection section) {
		StringBuilder htmlBuilder = new StringBuilder("<ul>");
		for (String value : section.getSectionContent()) {
			htmlBuilder.append("<li>");
			htmlBuilder.append(value);
			htmlBuilder.append("</li>");
		}
		htmlBuilder.append("</ul>");
		return htmlBuilder.toString();
	}

	/*private String getCheckListSection(PaperClipCheckListSection section) {
		StringBuilder htmlBuilder = new StringBuilder("<ul>");
		for (String key : section.getSectionContent().keySet()) {
			boolean checked = section.getSectionContent().get(key);
			if (checked)
				htmlBuilder.append("<li class='checked'>");
			else
				htmlBuilder.append("<li class='unchecked'>");
			htmlBuilder.append(key);
			htmlBuilder.append("</li>");
		}
		htmlBuilder.append("</ul>");
		return htmlBuilder.toString();
	}*/
	private String getCheckListSection(PaperClipCheckListSection section) {
		StringBuilder htmlBuilder = new StringBuilder("<ul>");
		for (Map.Entry<String, String> entry : section.getSectionContent().entrySet()) {
			htmlBuilder.append("<li>");
			htmlBuilder.append("<strong>"+entry.getKey()+":</strong> ");
			htmlBuilder.append(entry.getValue());
			htmlBuilder.append("</li>");
		}
		
		htmlBuilder.append("</ul>");
		return htmlBuilder.toString();
	}
}
