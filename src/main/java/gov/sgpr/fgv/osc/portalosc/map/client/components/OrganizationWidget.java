package gov.sgpr.fgv.osc.portalosc.map.client.components;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.AbstractMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.client.controller.MenuController;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscMenuSummary;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;

public class OrganizationWidget extends Composite {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private OscMenuSummary menuInfo;
	private List<AbstractMenuItem> menuItems;

	private MenuController controller = new MenuController();

	public OrganizationWidget(OscMenuSummary menuInfo, List<AbstractMenuItem> menuItems) {
		this.menuInfo = menuInfo;
		this.menuItems = menuItems;

		HTMLPanel panel = new HTMLPanel(getHtml());
		Panel itens = new FlowPanel();
		for (AbstractMenuItem item : menuItems) {
			MenuItemWidget widget = new MenuItemWidget(item);
			itens.add(widget);
		}
		panel.add(itens, "menu_itens");

		initWidget(panel);

	}

	public String getHtml() {
		// TODO: Remover imagem hardcoded
		menuInfo.setImageUrl("imagens/org_indisponivel.jpg");
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div class='organizacao'>");
		htmlBuilder.append("	<img src='" + menuInfo.getImageUrl() + "' alt='" + menuInfo.getTitle() + "' />");
//		htmlBuilder.append("	<a href='" + GWT.getHostPageBaseURL() + "Organization.html#" + History.getToken() + "'>");
		htmlBuilder.append("		<span id='tooltip_' title='' class='menuTooltip'>" + menuInfo.getTitle() + "</span>");
//		htmlBuilder.append("	</a>");
		htmlBuilder.append("	<div id='like_counter' title='" + menuInfo.getLikeCounter() + " recomendaram esta organização.' class='tip_recomendacao'>");
		htmlBuilder.append(			menuInfo.getLikeCounter());
		htmlBuilder.append("	</div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div id='menu_itens'></div>");
		htmlBuilder.append("	<a href='" + GWT.getHostPageBaseURL() + "Organization.html#" + History.getToken() + "'>");
		htmlBuilder.append("		<button type='button' name='Ver detalhes' id='org_detalhes'>Ver detalhes</button>");
		htmlBuilder.append("	</a>");
		String btnText = menuInfo.isRecommended() ? "Recomendar (desfazer)" : "Recomendar";
		
		htmlBuilder.append("<button type=\"button\" name=\"Recomendar\" id=\"org_recomendar\">" + btnText + "</button>");

		return htmlBuilder.toString();
	}

	@Override
	protected void onAttach() {
		super.onAttach();

		final Element span = DOM.getElementById("tooltip_");
		final Element spanPopup = (Element) span.getFirstChildElement();
		if (spanPopup != null) {

			Event.sinkEvents(span, Event.ONMOUSEOUT);
			Event.setEventListener(span, new EventListener() {
				@Override
				public void onBrowserEvent(Event event) {
					spanPopup.getStyle().setDisplay(Display.NONE);
				}
			});
			Event.sinkEvents(span, Event.ONMOUSEUP);
			Event.sinkEvents(span, Event.ONMOUSEMOVE);
			Event.setEventListener(span, new EventListener() {
				@Override
				public void onBrowserEvent(Event event) {
					final int left = event.getClientX() - 40;
					final int bottom = event.getClientY();
					spanPopup.setTitle(menuInfo.getTitle());
					spanPopup.getStyle().setDisplay(Display.BLOCK);
					spanPopup.getStyle().setMarginLeft(left, Unit.PX);
					spanPopup.getStyle().setMarginBottom(bottom, Unit.PX);
				}
			});

		}

		final Element btnRecom = DOM.getElementById("org_recomendar");
		Event.sinkEvents(btnRecom, Event.ONCLICK);
		Event.setEventListener(btnRecom, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {

				final Element likeCounter = DOM.getElementById("like_counter");

				menuInfo.setRecommended(!menuInfo.isRecommended());
				String btnText = menuInfo.isRecommended() ? "Recomendar (desfazer)"
						: "Recomendar";
				btnRecom.setInnerText(btnText);

				controller.RecommendationManager(menuInfo.isRecommended(),
						menuInfo.getOscId());
				int increment = menuInfo.isRecommended() ? 1 : -1;

				menuInfo.setLikeCounter(menuInfo.getLikeCounter() + increment);
				likeCounter.setInnerText(String.valueOf(menuInfo
						.getLikeCounter()));
				likeCounter.setTitle(menuInfo.getLikeCounter()
						+ " Recomendaram esta organização!");
			}

		});

	}

}
