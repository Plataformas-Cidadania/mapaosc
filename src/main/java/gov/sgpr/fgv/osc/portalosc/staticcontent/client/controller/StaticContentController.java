package gov.sgpr.fgv.osc.portalosc.staticcontent.client.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import gov.sgpr.fgv.osc.portalosc.staticcontent.shared.interfaces.StaticContentService;
import gov.sgpr.fgv.osc.portalosc.staticcontent.shared.interfaces.StaticContentServiceAsync;
import gov.sgpr.fgv.osc.portalosc.staticcontent.shared.model.Content;

/**
 * @author Gabriel
 *
 */
public class StaticContentController 
{
	private static Logger logger = Logger.getLogger(StaticContentController.class.getName());
	private StaticContentServiceAsync staticContentService = com.google.gwt.core.shared.GWT.create(StaticContentService.class);
	private String url;
	
	/**
	 * 
	 */
	public void init() 
	{
		AsyncCallback<Content> callback = new AsyncCallback<Content>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}

			public void onSuccess(Content result) {
				logger.info("Resposta de Conteúdo Estático recebida");
				url = result.getUrl();
				setTitle(result.getTitle());
				DOM.getElementById("static_content").addClassName(result.getCssClass());
				if(url.length() > 0)
					loadStaticContent("static_content");
				else
					DOM.getElementById("static_content").setInnerText("Unknown Page Requested");
			}

		};
		String param = Window.Location.getParameter("page");
		staticContentService.getContentFromParam(param, callback);
	}
	
	/**
	 * @param element_id
	 */
	public void loadStaticContent(String element_id) 
	{
		final Element d = DOM.getElementById(element_id);
		if(d != null)
		{
			final Element s = DOM.createSpan();
			s.setInnerText("Ocorreu um erro de comunicação com o servidor");
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
			try {
				builder.sendRequest(null, new RequestCallback() {
					public void onError(Request request, Throwable exception) {
						logger.info("Falha no carregamento do conteúdo estático");
						d.appendChild(s);
					}

					public void onResponseReceived(Request request, Response response) {
						if (200 == response.getStatusCode()) {
							logger.info("Carregando conteúdo estático do Joomla");
							d.setInnerHTML(response.getText());
						} else {
							logger.info("Falha no carregamento do conteúdo estático");
							d.appendChild(s);
						}
					}
				});
			} catch (RequestException e) {
				logger.info("Falha no carregamento do conteúdo estático");
				d.appendChild(s);
			}
		}
	}
	
	public void setTitle(String title)
	{
		Element h = DOM.getElementById("page_header");
		h.setInnerText(title);
		Window.setTitle("Mapa das Organizações da Sociedade Civil - " + title);
	}
}
