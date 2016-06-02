package gov.sgpr.fgv.osc.portalosc.map.client.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.AbstractMenuItem;
import gov.sgpr.fgv.osc.portalosc.map.client.controller.MenuController;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscMenuSummary;
import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

public class OrganizationWidget extends Composite {
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
//		menuInfo.setImageUrl("imagens/org_indisponivel.jpg");
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div class='organizacao'>");
//		htmlBuilder.append("<img src='" + menuInfo.getImageUrl() + "' alt='" + menuInfo.getTitle() + "' />");
//		htmlBuilder.append("<a href='" + GWT.getHostPageBaseURL() + "Organization.html#" + History.getToken() + "'>");
		htmlBuilder.append("<span id='tooltip_' title='' class='menuTooltip'>" + menuInfo.getTitle() + "</span>");
//		htmlBuilder.append("</a>");
		htmlBuilder.append("<div id='like_counter' title='" + menuInfo.getLikeCounter() + " recomendaram esta organização.' class='tip_recomendacao'>");
		htmlBuilder.append(menuInfo.getLikeCounter());
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div id='menu_itens'></div>");
		htmlBuilder.append("<a href='" + GWT.getHostPageBaseURL() + "Organization.html#" + History.getToken() + "'>");
		htmlBuilder.append("<button type='button' name='Ver detalhes' id='org_detalhes'>Página da OSC</button>");
		htmlBuilder.append("</a>");
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
				
				public void onBrowserEvent(Event event) {
					spanPopup.getStyle().setDisplay(Display.NONE);
				}
			});
			Event.sinkEvents(span, Event.ONMOUSEUP);
			Event.sinkEvents(span, Event.ONMOUSEMOVE);
			Event.setEventListener(span, new EventListener() {
				
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

			
			public void onBrowserEvent(Event event) {
				
				if(UserController.hasLoggedUser() == true){
					if (UserController.getCurrentUser().getType() == UserType.DEFAULT || UserController.getCurrentUser().getType() == UserType.OSC_AGENT){
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
				
					}else{
						popup("Recomendar Organização", "Para recomendar uma Organização é necessário ter o CPF cadastrado no sistema. Para cadastrar seu CPF entre em Configurações.");
					}
				}else{
					popup("Recomendar Organização", "Para recomendar uma Organização é necessário realizar o Login.");
				}
			}

		});

	}
	
	private void popup(String titulo, String msg){
		final PopupPanel popup = new PopupPanel();
		popup.setStyleName("overlay");
		popup.add(getHtmlPopup(titulo,msg));
		popup.show();
		
		Element ok = DOM.getElementById("ok");
		Event.sinkEvents(ok, Event.ONCLICK);
		Event.setEventListener(ok, new EventListener() {
			public void onBrowserEvent(Event event) {
				popup.hide();
			}
		});
	}
	
	private static HTML getHtmlPopup(String titulo, String msg) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div  id='popup' class='pop_up_alert clearfix'>");
		htmlBuilder.append("<h2>"+ titulo +"</h2>");
		htmlBuilder.append("<div>");
		htmlBuilder.append("<p>"+ msg +"</p>");
		htmlBuilder.append("<form id='form_esqueci_senha' method='post'>");
		htmlBuilder.append("<div class='botoes'>");
		htmlBuilder.append("<input type='button' name='ok' id='ok'  value='Ok' style='margin-left: 180px;' /></div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</form>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
	
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

}
